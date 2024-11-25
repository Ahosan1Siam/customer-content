package com.assessment.consumer_content.application.service.implementation;

import com.assessment.consumer_content.application.dtos.request.KeywordDetailsRequest;
import com.assessment.consumer_content.application.dtos.response.CommonResponse;
import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.application.service.contract.IKeywordDetailsService;
import com.assessment.consumer_content.domain.entities.KeywordDetails;
import com.assessment.consumer_content.domain.repository.BaseRepository;
import com.assessment.consumer_content.domain.repository.KeywordDetailsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
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
    @CacheEvict(value = "keywords", allEntries = true)
    public Envelope create(KeywordDetailsRequest request) {
        KeywordDetails e = convertForCreate(request);
        KeywordDetails res = _keywordDetailsRepository.save(e);
        return CommonResponse.makeResponse(res,"Successful",true);
    }
    @Cacheable(value = "keywords")
    @Override
    public Set<String> getAllKeywordDetails() {
        return _keywordDetailsRepository.findKeywords();
    }
    @Override
    public Envelope bulkSave(List<KeywordDetailsRequest> requests) {
        List<KeywordDetails> keywords = new ArrayList<>();
        for (KeywordDetailsRequest request : requests) {
            KeywordDetails keywordDetails = new KeywordDetails();
            BeanUtils.copyProperties(request, keywordDetails);
            keywords.add(keywordDetails);
        }
        var saveList =_keywordDetailsRepository.saveAll(keywords);
        return CommonResponse.makeResponse(saveList,"Saved Response",!saveList.isEmpty());
    }
}
