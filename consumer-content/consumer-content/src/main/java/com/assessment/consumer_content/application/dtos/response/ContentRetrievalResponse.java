package com.assessment.consumer_content.application.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class ContentRetrievalResponse {
    private Long statusCode;
    private String message;
    private Long contentCount;
    private List<ContentsResponse> contents;
}
