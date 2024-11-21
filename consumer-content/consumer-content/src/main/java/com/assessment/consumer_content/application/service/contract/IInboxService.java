package com.assessment.consumer_content.application.service.contract;

import com.assessment.consumer_content.application.dtos.request.InboxRequest;
import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.domain.entities.Inbox;

import java.util.List;

public interface IInboxService extends IBaseService<Inbox, InboxRequest>{
    Envelope bulkContentSave(List<InboxRequest> requestList);
}
