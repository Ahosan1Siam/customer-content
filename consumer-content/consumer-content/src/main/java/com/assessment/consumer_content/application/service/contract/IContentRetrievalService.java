package com.assessment.consumer_content.application.service.contract;

import com.assessment.consumer_content.application.dtos.request.InboxRequest;
import com.assessment.consumer_content.application.dtos.response.ContentRetrievalResponse;
import com.assessment.consumer_content.application.dtos.response.Envelope;

import java.util.List;

public interface IContentRetrievalService {
    Envelope getContents();
}
