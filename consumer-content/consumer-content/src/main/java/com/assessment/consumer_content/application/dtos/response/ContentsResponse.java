package com.assessment.consumer_content.application.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentsResponse {
    private String transactionId;
    private String operator;
    private String shortCode;
    private String msisdn;
    private String sms;
}
