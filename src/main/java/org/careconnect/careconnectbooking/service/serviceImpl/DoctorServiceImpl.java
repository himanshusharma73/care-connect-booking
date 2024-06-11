package org.careconnect.careconnectbooking.service.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.careconnect.careconnectbooking.bookingproxy.AdminServiceFeignClient;
import org.careconnect.careconnectbooking.controller.BookingController;
import org.careconnect.careconnectcommon.response.dto.DoctorDto;
import org.careconnect.careconnectcommon.exception.BookingDtoException;
import org.careconnect.careconnectcommon.exception.ResourceNotFoundException;
import org.careconnect.careconnectbooking.responce.ApiResponse;
import org.careconnect.careconnectbooking.service.DoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;

@Service
public class DoctorServiceImpl implements DoctorService {
    private Logger logger= LoggerFactory.getLogger(BookingController.class);
    @Autowired
    AdminServiceFeignClient adminServiceFeignClient;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public <T> T getDoctorById(@PathVariable long doctorId) {
        try {
            ApiResponse apiResponse = adminServiceFeignClient.getDoctorById(doctorId);

            if (apiResponse.getData() != null) {
                DoctorDto doctorDto = objectMapper.convertValue(apiResponse.getData(), DoctorDto.class);

                this.logger.info("Doctor retrieved successfully"+ Collections.singletonList(doctorDto));
                return (T) doctorDto;
            }
            else{
                throw new ResourceNotFoundException("Doctor", "Id", String.valueOf(doctorId));
            }
        }catch (FeignException e){
            throw new ResourceNotFoundException("Doctor", "Id", String.valueOf(doctorId));
        }

    }


    @Override
    public DoctorDto getDoctorBySpecialization(String specialization) {
        try {
            ApiResponse apiResponse = adminServiceFeignClient.getDoctorBySpecialization(specialization);

            if (apiResponse.getData() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                DoctorDto doctorDto = objectMapper.convertValue(apiResponse.getData(), DoctorDto.class);

                this.logger.info("Doctor retrieved successfully"+ Collections.singletonList(doctorDto));
                return doctorDto;
            }
            else{
                throw new BookingDtoException((String) apiResponse.getError());
            }
        }catch (FeignException e) {
            String errorMessage = e.contentUTF8();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = null;
            try {
                jsonNode = objectMapper.readTree(errorMessage);

            } catch (Exception e1) {
                e1.printStackTrace();
            }
            throw new BookingDtoException(jsonNode.get("error").get("errorDetails").asText());
        }

    }
}
