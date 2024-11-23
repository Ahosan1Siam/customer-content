package com.assessment.consumer_content.application.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChargeConfigRequest extends BaseRequest {
    private String operator;
    private String chargeCode;
}
