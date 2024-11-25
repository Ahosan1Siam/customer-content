package com.assessment.consumer_content.application.dtos.request;

import lombok.Data;

import java.util.List;

@Data
public class BulkKeywordSaveRequest {
    List<KeywordDetailsRequest> keywords;
}
