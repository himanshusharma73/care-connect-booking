package org.careconnect.careconnectbooking.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.careconnect.careconnectbooking.dto.*;
import org.careconnect.careconnectbooking.util.CopyProperties;
import org.careconnect.careconnectcommon.entity.BookingAppointment;
import org.careconnect.careconnectcommon.entity.DoctorEntity;
import org.careconnect.careconnectcommon.exception.BookingDtoException;
import org.careconnect.careconnectbooking.repo.BookingAppointmentRepository;
import org.careconnect.careconnectbooking.service.BookingService;
import org.careconnect.careconnectbooking.service.DoctorService;
import org.careconnect.careconnectbooking.service.PatientService;
import org.careconnect.careconnectcommon.response.BookingResponse;
import org.careconnect.careconnectcommon.response.dto.DoctorDto;
import org.careconnect.careconnectcommon.response.dto.PatientDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    DoctorService doctorService;

    @Autowired
    BookingAppointmentRepository bookingAppointmentRepository;

    @Autowired
    PatientService patientService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CopyProperties copyProperties;

    private final Logger logger= LoggerFactory.getLogger(BookingServiceImpl.class);

    @Override
    public DoctorDto getDoctor(@NotNull BookingDto bookingDto) {
        if (bookingDto.getDoctorId() != null) {
            DoctorEntity doctorEntity = doctorService.getDoctorById(bookingDto.getDoctorId());
            DoctorDto doctorDto=new DoctorDto();
            doctorDto.setDoctorId(doctorEntity.getDoctorId());
            return doctorDto;

        } else if (bookingDto.getSpecialization() != null) {
            return doctorService.getDoctorBySpecialization(bookingDto.getSpecialization());
        } else {
            throw new BookingDtoException("At least the specialty or doctor ID must be filled in");
        }
    }

    public void checkConflictOfDateAndTime(BookingDto bookingDto, DoctorDto doctorDto) {
        List<BookingAppointment> bookingAppointment = bookingAppointmentRepository.findByDoctorId(doctorDto.getDoctorId());
        for (BookingAppointment appointment : bookingAppointment) {
            if ((appointment.getAppointmentStartTime().equals(bookingDto.getAppointmentStartTime())
                    || bookingDto.getAppointmentStartTime().isBefore(appointment.getAppointmentEndTime()))
                    && appointment.getAppointmentDate().equals(bookingDto.getAppointmentDate())
            && "active".equals(appointment.getStatus()))  {
                if ((appointment.getPatientId().equals(bookingDto.getPatientId()))
                        && appointment.getAppointmentDate().equals(bookingDto.getAppointmentDate())) {
                    throw new BookingDtoException("This patient have already appointment on the given" +
                            " date " +
                            "Appointment Time:" + appointment.getAppointmentStartTime());
                }

                throw new BookingDtoException("Doctor have already appointment on this Date and Time");
            }
            if (appointment.getPatientId().equals(bookingDto.getPatientId())
                    && "active".equals(appointment.getStatus())) {
                throw new BookingDtoException("This patient have already appointment on the date: " + appointment.getAppointmentDate()+
                        " Time: " + appointment.getAppointmentStartTime());

            }

        }
    }

    @Override
    public BookingAppointment bookAppointment(DoctorDto doctorDto, PatientDto patientDto, BookingDto bookingDto) {
        return new BookingAppointment(doctorDto.getDoctorId(), patientDto.getPatientId(), bookingDto.getAppointmentDate(), bookingDto.getAppointmentStartTime(), bookingDto.getAppointmentStartTime().plusMinutes(30), "active");
    }

    public  List<BookingAppointment> getBookingAppointments(BookingRetrieveDto bookingRetrieveDto) {
        if (bookingRetrieveDto == null) {
            throw new BookingDtoException("Enter details to retrieve Booking details");
        }

        return Optional.of(bookingAppointmentRepository.findAll().stream()
                        .filter(booking -> matchesCriteria(booking, bookingRetrieveDto))
                        .collect(Collectors.toList()))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new BookingDtoException("No booking matches the criteria"));
    }

    @Override
    public List<BookingResponse> getBooking(BookingRetrieveDto bookingRetrieveDto) {
        List<BookingAppointment> bookingAppointments=getBookingAppointments(bookingRetrieveDto);
        List<BookingResponse> bookingResponse = bookingAppointments.stream().map(bookingAppointment -> copyProperties.copyPropertiesToBooking(bookingAppointment)).collect(Collectors.toList()); ;
        return bookingResponse;

    }


    private boolean matchesCriteria(BookingAppointment booking, BookingRetrieveDto bookingRetrieveDto) {
        return (bookingRetrieveDto.getDoctorId() == null || booking.getDoctorId() == (bookingRetrieveDto.getDoctorId()))
                && (bookingRetrieveDto.getPatientId() == null || booking.getPatientId().equals(bookingRetrieveDto.getPatientId()))
                && (bookingRetrieveDto.getAppointmentDate() == null || booking.getAppointmentDate().equals(bookingRetrieveDto.getAppointmentDate()))
                && (bookingRetrieveDto.getAppointmentStartTime() == null || booking.getAppointmentStartTime().equals(bookingRetrieveDto.getAppointmentStartTime()))
                && (bookingRetrieveDto.getStatus() == null || booking.getStatus().equals(bookingRetrieveDto.getStatus()));
    }

    @Override
    public BookingAppointment checkUp(RequestDto requestDto) {

        BookingAppointment bookingAppointment=bookingAppointmentRepository.findByPatientIdAndDoctorIdAndAppointmentDate(requestDto.getPatientId()
                ,requestDto.getDoctorId(),requestDto.getAppointmentDate());
        if (bookingAppointment!=null){
            logger.info("Booking retrieved successfully {} ",bookingAppointment);
            bookingAppointment.setStatus("completed");
            bookingAppointmentRepository.save(bookingAppointment);
            return bookingAppointment;
        }
        else throw  new BookingDtoException("No Patient appointment found with entered details");
    }
}


