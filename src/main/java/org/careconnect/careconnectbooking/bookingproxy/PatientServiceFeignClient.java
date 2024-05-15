package org.careconnect.careconnectbooking.bookingproxy;

import org.careconnect.careconnectbooking.responce.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "care-connect-patient")
public interface PatientServiceFeignClient {
    @GetMapping("/patients/{patientId}")
    public ApiResponse getPatientById(@PathVariable Long patientId);
}
