package com.assessment.consumer_content.application.service.contract;

import com.assessment.consumer_content.application.dtos.request.BaseRequest;
import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.domain.entities.BaseEntity;

public interface IBaseService <E extends BaseEntity, D extends BaseRequest>{
    Envelope create(D d);

    Envelope update(D d);

    Envelope get(long id);
}
