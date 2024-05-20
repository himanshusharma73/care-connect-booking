package org.careconnect.careconnectbooking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.careconnect.careconnectbooking.util.ValidAppointmentTime;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingDto {
    
    private Long doctorId;

    @NotNull(message = "Patient id cannot be null")
    private Long patientId;

    @Future(message = "Appointment Date should be in future")
    @NotNull(message = "Appointment Date cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @ValidAppointmentTime
    @NotNull(message = "Appointment Date cannot be null")
    @DateTimeFormat(pattern = "HH:mm"/*, message = "Appointment Time should be in the format HH:mm"*/)
    private LocalTime appointmentStartTime;

    private String specialization;
}
