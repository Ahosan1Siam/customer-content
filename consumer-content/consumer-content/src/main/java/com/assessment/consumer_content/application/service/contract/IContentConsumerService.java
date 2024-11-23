package com.assessment.consumer_content.application.service.contract;

import com.assessment.consumer_content.application.dtos.response.Envelope;

public interface IContentConsumerService {
    Envelope getContents();
}
