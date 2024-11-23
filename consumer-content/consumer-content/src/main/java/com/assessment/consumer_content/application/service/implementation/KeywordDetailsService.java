package com.assessment.consumer_content.application.service.implementation;

import com.assessment.consumer_content.application.dtos.request.KeywordDetailsRequest;
import com.assessment.consumer_content.application.dtos.response.CommonResponse;
import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.application.service.contract.IKeywordDetailsService;
import com.assessment.consumer_content.domain.entities.KeywordDetails;
import com.assessment.consumer_content.domain.repository.BaseRepository;
import com.assessment.consumer_content.domain.repository.KeywordDetailsRepository;
import org.springframework.stereotype.Service;


import java.util.Set;

@Service
public class KeywordDetailsService extends BaseService<KeywordDetails, KeywordDetailsRequest> implements IKeywordDetailsService {
    private final KeywordDetailsRepository _keywordDetailsRepository;
    protected KeywordDetailsService(BaseRepository<KeywordDetails> repository, KeywordDetailsRepository keywordDetailsRepository) {
        super(repository);
        _keywordDetailsRepository = keywordDetailsRepository;
    }

    @Override
    public Envelope get(long id) {
        return super.get(id);
    }

    @Override
    public Envelope create(KeywordDetailsRequest request) {
        KeywordDetails e = convertForCreate(request);
        KeywordDetails res = _keywordDetailsRepository.save(e);
        return CommonResponse.makeResponse(res,"Successful",true);
    }

    @Override
    public Set<String> getAllKeywordDetails() {
        return _keywordDetailsRepository.findKeywords();
    }
}
