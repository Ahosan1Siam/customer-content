package com.assessment.consumer_content.application.dtos.request;

import lombok.Data;

@Data
public class UnlockCodeRequest {
    private String transactionId;
    private String operator;
    private String shortCode;
    private String msisdn;
    private String keyword;
    private String gameName;
}
