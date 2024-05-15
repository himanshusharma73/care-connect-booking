package org.careconnect.careconnectbooking.service;

import org.careconnect.careconnectbooking.dto.PatientDto;
import org.springframework.web.bind.annotation.PathVariable;

public interface PatientService {
    PatientDto getPatientById(@PathVariable long patientId);
}
