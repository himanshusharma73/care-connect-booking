package org.careconnect.careconnectbooking.service;

import org.careconnect.careconnectcommon.response.dto.DoctorDto;
import org.springframework.web.bind.annotation.PathVariable;

public interface DoctorService {
    <T>  T getDoctorById(@PathVariable long doctorId);
    DoctorDto getDoctorBySpecialization(@PathVariable String specialization);
}
