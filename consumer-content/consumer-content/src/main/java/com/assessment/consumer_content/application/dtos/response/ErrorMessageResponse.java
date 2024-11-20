package com.assessment.consumer_content.application.dtos.response;

import lombok.Data;

@Data
public class ErrorMessageResponse {
    private int StatusCode;
    private String MainErrorMsg;
    private String PublicErrorMsg;
}
