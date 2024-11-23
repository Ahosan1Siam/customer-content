package com.assessment.consumer_content.application.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeCodeResponse {
    private String chargeCode;
    private String operator;
}
