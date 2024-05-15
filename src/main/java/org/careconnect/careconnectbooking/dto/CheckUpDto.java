package org.careconnect.careconnectbooking.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CheckUpDto {
    private long patientId;
    private long doctorId;
    private LocalDate appointmentDate;
}
