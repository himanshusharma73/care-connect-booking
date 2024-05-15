package org.careconnect.careconnectbooking.service.serviceImpl;

import jakarta.validation.constraints.NotNull;
import org.careconnect.careconnectbooking.controller.BookingController;
import org.careconnect.careconnectbooking.dto.BookingDto;
import org.careconnect.careconnectbooking.dto.CheckUpDto;
import org.careconnect.careconnectbooking.dto.DoctorDto;
import org.careconnect.careconnectbooking.dto.PatientDto;
import org.careconnect.careconnectcommon.entity.BookingAppointment;
import org.careconnect.careconnectcommon.exception.BookingDtoException;
import org.careconnect.careconnectbooking.repo.BookingAppointmentRepository;
import org.careconnect.careconnectbooking.service.BookingService;
import org.careconnect.careconnectbooking.service.DoctorService;
import org.careconnect.careconnectbooking.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    DoctorService doctorService;

    @Autowired
    BookingAppointmentRepository bookingAppointmentRepository;

    @Autowired
    PatientService patientService;

    private final Logger logger= LoggerFactory.getLogger(BookingServiceImpl.class);

    public DoctorDto getDoctor(@NotNull BookingDto bookingDto) {
        if (bookingDto.getDoctorId() != null) {
            return doctorService.getDoctorById(bookingDto.getDoctorId());

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

    public BookingAppointment bookAppointment(DoctorDto doctorDto, PatientDto patientDto, BookingDto bookingDto) {
        return new BookingAppointment(doctorDto.getDoctorId(), patientDto.getPatientId(), bookingDto.getAppointmentDate(), bookingDto.getAppointmentStartTime(), bookingDto.getAppointmentStartTime().plusMinutes(30), "active");
    }

    public List<BookingAppointment> getBookingAppointments(BookingDto bookingDto) {
        if (bookingDto == null) {
            throw new BookingDtoException("Enter details to retrieve Booking details");
        }

        return bookingAppointmentRepository.findAll().stream().filter(booking -> matchesCriteria(booking, bookingDto)).collect(Collectors.toList());
    }


    private boolean matchesCriteria(BookingAppointment booking, BookingDto bookingDto) {
        return (bookingDto.getDoctorId() == null || booking.getDoctorId() == (bookingDto.getDoctorId()))
                && (bookingDto.getPatientId() == null || booking.getPatientId().equals(bookingDto.getPatientId()))
                && (bookingDto.getAppointmentDate() == null || booking.getAppointmentDate().equals(bookingDto.getAppointmentDate()))
                && (bookingDto.getAppointmentStartTime() == null || booking.getAppointmentStartTime().equals(bookingDto.getAppointmentStartTime()));
    }

    @Override
    public BookingAppointment checkUp(CheckUpDto checkUpDto) {

        BookingAppointment bookingAppointment=bookingAppointmentRepository.findByPatientIdAndDoctorIdAndAppointmentDate(checkUpDto.getPatientId()
                ,checkUpDto.getDoctorId(),checkUpDto.getAppointmentDate());
        if (bookingAppointment!=null){
            logger.info("Booking retrieved successfully {} ",bookingAppointment);
            bookingAppointment.setStatus("completed");
            bookingAppointmentRepository.save(bookingAppointment);
            return bookingAppointment;
        }
        else throw  new BookingDtoException("No Patient appointment found with entered details");
    }
}


