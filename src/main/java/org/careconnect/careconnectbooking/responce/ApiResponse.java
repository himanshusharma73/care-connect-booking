package org.careconnect.careconnectbooking.responce;

import lombok.Data;

@Data
public class ApiResponse {
    private Object data;
    private Object error;

}