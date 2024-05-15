package org.careconnect.careconnectbooking.bookingproxy;

import org.careconnect.careconnectbooking.responce.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="care-connect-admin")
public interface AdminServiceFeignClient {

    @GetMapping("/doctors")
    public ApiResponse getALLDoctor();

    @GetMapping("/doctors/{doctorId}")
    ApiResponse getDoctorById(@PathVariable long doctorId);

    @GetMapping("/doctor/specialization/{specialization}")
    ApiResponse getDoctorBySpecialization(@PathVariable String specialization);
}
