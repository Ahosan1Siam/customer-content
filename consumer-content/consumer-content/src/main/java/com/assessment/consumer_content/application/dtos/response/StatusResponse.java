package com.assessment.consumer_content.application.dtos.response;

import lombok.Data;

@Data
public class StatusResponse {
    private int statusCode;
    private String message;
}
