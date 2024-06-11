package org.careconnect.careconnectbooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.careconnect.careconnectbooking.dto.*;
import org.careconnect.careconnectbooking.service.serviceImpl.JSONToPDF;
import org.careconnect.careconnectcommon.entity.BookingAppointment;
import org.careconnect.careconnectbooking.repo.BookingAppointmentRepository;
import org.careconnect.careconnectbooking.responce.ApiResponse;
import org.careconnect.careconnectbooking.service.BookingService;
import org.careconnect.careconnectbooking.service.serviceImpl.PatientServiceImpl;
import org.careconnect.careconnectcommon.response.BookingResponse;
import org.careconnect.careconnectcommon.response.dto.DoctorDto;
import org.careconnect.careconnectcommon.response.dto.PatientDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookingController {

    private Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    BookingAppointmentRepository bookingAppointmentRepository;

    @Autowired
    PatientServiceImpl patientService;

    @Autowired
    BookingService bookingService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JSONToPDF jsonToPDF;

    /*
     * This API is for making appointments with a doctor
     *
     * @param bookingDto BookingDto
     * @return apiResponse
     */
    @Operation(summary = "Make appointment with a doctor")
    @PostMapping("/patient/appointments")
    public ResponseEntity<ApiResponse> postBooking(@Valid @RequestBody BookingDto bookingDto) {

        PatientDto patientDto = objectMapper.convertValue(patientService.getPatientById(bookingDto.getPatientId()),PatientDto.class);
        DoctorDto doctorDto = bookingService.getDoctor(bookingDto);

        bookingService.checkConflictOfDateAndTime(bookingDto,doctorDto);

        BookingAppointment bookingAppointment = bookingService.bookAppointment(doctorDto,patientDto,bookingDto);
        BookingAppointment savedAppointment = bookingAppointmentRepository.save(bookingAppointment);
        logger.info("Booking confirmed {}", bookingAppointment);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(savedAppointment);
        return ResponseEntity.ok(apiResponse);

    }

    @PostMapping("/appointment")
    public ResponseEntity<ApiResponse> retrieveBooking(@RequestBody BookingRetrieveDto bookingRetrieveDto){
        ApiResponse apiResponse=new ApiResponse();
       apiResponse.setData(bookingService.getBooking(bookingRetrieveDto));
        return ResponseEntity.ok(apiResponse);
    }

    /*
    * this api is for only completing the booking
    * */
    @PostMapping("/checkup")
    @Hidden
    public ResponseEntity<ApiResponse> checkUp(@RequestBody RequestDto requestDto){
        BookingAppointment bookingAppointments=bookingService.checkUp(requestDto);
        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setData(bookingAppointments);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/appointment/download")
    public ResponseEntity<byte[]> downloadBooking(@RequestBody BookingRetrieveDto bookingRetrieveDto) {
        List<BookingResponse> booking = bookingService.getBooking(bookingRetrieveDto);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(booking);

        byte[] pdfBytes;
        try {
            String jsonResponse = objectMapper.writeValueAsString(apiResponse);
            pdfBytes = jsonToPDF.generatePDF(jsonResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=appointment_details.pdf");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
