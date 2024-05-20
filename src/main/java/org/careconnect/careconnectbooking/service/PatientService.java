package org.careconnect.careconnectbooking.service;

import org.careconnect.careconnectcommon.entity.PatientEntity;
import org.careconnect.careconnectcommon.entity.PatientIllnessEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface PatientService {

    <T> T getPatientById(@PathVariable long patientId);

    <T> T getPatientIllness(@PathVariable long patientId);
}
