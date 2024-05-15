package org.careconnect.careconnectbooking.responce;

import lombok.Data;
import org.careconnect.careconnectbooking.dto.DoctorDto;

import java.util.List;

@Data
public class ApiResponse {
    private Object data;
    private Object error;

}