package org.careconnect.careconnectbooking.controller;

import jakarta.validation.Valid;
import org.careconnect.careconnectbooking.dto.BookingDto;
import org.careconnect.careconnectbooking.dto.CheckUpDto;
import org.careconnect.careconnectbooking.dto.DoctorDto;
import org.careconnect.careconnectbooking.dto.PatientDto;
import org.careconnect.careconnectcommon.entity.BookingAppointment;
import org.careconnect.careconnectbooking.repo.BookingAppointmentRepository;
import org.careconnect.careconnectbooking.responce.ApiResponse;
import org.careconnect.careconnectbooking.service.BookingService;
import org.careconnect.careconnectbooking.service.serviceImpl.PatientServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/patient/appointments")
    public ResponseEntity<ApiResponse> postBooking(@Valid @RequestBody BookingDto bookingDto) {
        PatientDto patientDto = patientService.getPatientById(bookingDto.getPatientId());
        DoctorDto doctorDto = bookingService.getDoctor(bookingDto);

        bookingService.checkConflictOfDateAndTime(bookingDto,doctorDto);

        BookingAppointment bookingAppointment = bookingService.bookAppointment(doctorDto,patientDto,bookingDto);
        BookingAppointment savedAppointment = bookingAppointmentRepository.save(bookingAppointment);
        logger.info("Booking confirmed {}", bookingAppointment);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(savedAppointment);
        return ResponseEntity.ok(apiResponse);

    }

    @GetMapping("/booking")
    public ResponseEntity<ApiResponse> retrieveBooking(@RequestBody BookingDto bookingDto){
        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setData(bookingService.getBookingAppointments(bookingDto));
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/checkup")
    public ResponseEntity<ApiResponse> checkUp(@RequestBody CheckUpDto checkUpDto){
        BookingAppointment bookingAppointments=bookingService.checkUp(checkUpDto);
        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setData(bookingAppointments);
        return ResponseEntity.ok(apiResponse);
    }
}
