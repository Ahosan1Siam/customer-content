package com.assessment.consumer_content.application.service.contract;

import com.assessment.consumer_content.application.dtos.request.KeywordDetailsRequest;
import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.domain.entities.KeywordDetails;

import java.util.List;
import java.util.Set;

public interface IKeywordDetailsService extends IBaseService<KeywordDetails, KeywordDetailsRequest>{
    Set<String> getAllKeywordDetails();
    Envelope bulkSave(List<KeywordDetailsRequest> requests);
}
