package org.careconnect.careconnectbooking.service.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.careconnect.careconnectbooking.bookingproxy.PatientServiceFeignClient;
import org.careconnect.careconnectbooking.controller.BookingController;
import org.careconnect.careconnectcommon.exception.BookingDtoException;
import org.careconnect.careconnectcommon.exception.ResourceNotFoundException;
import org.careconnect.careconnectbooking.responce.ApiResponse;
import org.careconnect.careconnectbooking.service.PatientService;
import org.careconnect.careconnectcommon.response.dto.PatientDto;
import org.careconnect.careconnectcommon.response.dto.PatientIllnessDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private Logger logger = LoggerFactory.getLogger(BookingController.class);


    @Autowired
    PatientServiceFeignClient patientServiceFeignClient;

    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public <T> T getPatientById(@PathVariable long patientId) {
        try {
            ApiResponse apiResponse = patientServiceFeignClient.getPatientById(patientId);
            if (apiResponse.getData() != null) {

                PatientDto patientDto = objectMapper.convertValue(apiResponse.getData(), PatientDto.class);


                this.logger.info("Patient retrieved successfully" + Collections.singletonList(patientDto));
                return (T) patientDto;
            } else {
                throw new ResourceNotFoundException("Patient", "Id", String.valueOf(patientId));
            }
        } catch (FeignException e) {
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
    @Override
    public <T> T getPatientIllness(@PathVariable long patientId) {
        try {
            ApiResponse apiResponse = patientServiceFeignClient.getIllness(patientId);
            if (apiResponse.getData() != null) {

                List<PatientIllnessDto> patientIllnessDtos =(List<PatientIllnessDto>) apiResponse.getData();

                this.logger.info("Patient illness retrieved successfully" + Collections.singletonList(patientIllnessDtos));
                return (T) patientIllnessDtos;
            } else {
                throw new ResourceNotFoundException("Patient illness", "Id", String.valueOf(patientId));
            }
        } catch (FeignException e) {
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
