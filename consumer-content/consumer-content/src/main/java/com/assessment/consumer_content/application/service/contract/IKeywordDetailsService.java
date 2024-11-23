package com.assessment.consumer_content.application.service.contract;

import com.assessment.consumer_content.application.dtos.request.KeywordDetailsRequest;
import com.assessment.consumer_content.domain.entities.KeywordDetails;

import java.util.Set;

public interface IKeywordDetailsService extends IBaseService<KeywordDetails, KeywordDetailsRequest>{
    Set<String> getAllKeywordDetails();
}
