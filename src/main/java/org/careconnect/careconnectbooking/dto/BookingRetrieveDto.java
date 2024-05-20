package org.careconnect.careconnectbooking.dto;

import lombok.Data;
import org.careconnect.careconnectbooking.util.ValidAppointmentTime;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingRetrieveDto {
    private Long patientId;
    private Long doctorId;
    private LocalDate appointmentDate;
    @ValidAppointmentTime
    @DateTimeFormat(pattern = "HH:mm"/*, message = "Appointment Time should be in the format HH:mm"*/)
    private LocalTime appointmentStartTime;
    private String status;
}
