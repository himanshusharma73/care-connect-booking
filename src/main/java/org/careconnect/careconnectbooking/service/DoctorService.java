package org.careconnect.careconnectbooking.service;

import org.careconnect.careconnectbooking.dto.DoctorDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

public interface DoctorService {
    DoctorDto getDoctorById(@PathVariable long doctorId);
    DoctorDto getDoctorBySpecialization(@PathVariable String specialization);
}
