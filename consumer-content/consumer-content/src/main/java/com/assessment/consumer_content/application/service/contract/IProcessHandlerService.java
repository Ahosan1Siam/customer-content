package com.assessment.consumer_content.application.service.contract;

import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.domain.entities.Inbox;

import java.util.List;

public interface IProcessHandlerService {
    Envelope HandleProcess(List<Inbox> inboxes);
}
