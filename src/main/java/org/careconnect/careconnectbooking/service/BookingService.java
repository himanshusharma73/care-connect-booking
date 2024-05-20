package org.careconnect.careconnectbooking.service;

import jakarta.validation.constraints.NotNull;
import org.careconnect.careconnectbooking.dto.*;
import org.careconnect.careconnectcommon.entity.BookingAppointment;
import org.careconnect.careconnectcommon.response.BookingResponse;
import org.careconnect.careconnectcommon.response.dto.DoctorDto;
import org.careconnect.careconnectcommon.response.dto.PatientDto;

import java.util.List;

public interface BookingService {
    DoctorDto getDoctor(@NotNull BookingDto bookingDto);

    void checkConflictOfDateAndTime(BookingDto bookingDto,DoctorDto doctorDto);

    BookingAppointment bookAppointment(DoctorDto doctorDto, PatientDto patientDto, BookingDto bookingDto);

    List<BookingResponse> getBooking(BookingRetrieveDto bookingRetrieveDto);

    BookingAppointment checkUp(RequestDto requestDto);
}
