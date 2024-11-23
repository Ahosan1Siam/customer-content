package com.assessment.consumer_content.application.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ContentConsumerResponse extends StatusResponse{
    private Long contentCount;
    private List<ContentsResponse> contents;
}
