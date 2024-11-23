package com.assessment.consumer_content.application.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerformChargingResponse extends StatusResponse {
    private String transactionId;
    private String operator;
    private String shortCode;
    private String msisdn;
    private String chargeCode;
}
