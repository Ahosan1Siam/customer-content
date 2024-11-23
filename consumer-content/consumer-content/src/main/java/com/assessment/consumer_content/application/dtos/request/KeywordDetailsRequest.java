package com.assessment.consumer_content.application.dtos.request;

import lombok.Data;

@Data
public class KeywordDetailsRequest extends BaseRequest {
    private String keyword;
}
