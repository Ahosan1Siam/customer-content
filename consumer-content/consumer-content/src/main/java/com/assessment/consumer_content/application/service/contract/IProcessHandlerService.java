package com.assessment.consumer_content.application.service.contract;

import com.assessment.consumer_content.domain.entities.Inbox;

import java.util.List;

public interface IProcessHandlerService {
    List<Inbox> HandleProcess(List<Inbox> inboxes);
}
