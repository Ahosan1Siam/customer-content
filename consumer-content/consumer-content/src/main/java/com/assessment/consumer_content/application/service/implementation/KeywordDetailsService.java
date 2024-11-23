package com.assessment.consumer_content.application.service.implementation;

import com.assessment.consumer_content.application.dtos.request.KeywordDetailsRequest;
import com.assessment.consumer_content.application.dtos.response.CommonResponse;
import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.application.service.contract.IKeywordDetailsService;
import com.assessment.consumer_content.domain.entities.KeywordDetails;
import com.assessment.consumer_content.domain.repository.BaseRepository;
import com.assessment.consumer_content.domain.repository.KeywordDetailsRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class KeywordDetailsService extends BaseService<KeywordDetails, KeywordDetailsRequest> implements IKeywordDetailsService {
    private final KeywordDetailsRepository _keywordDetailsRepository;
    private final CacheManager cacheManager;
    protected KeywordDetailsService(BaseRepository<KeywordDetails> repository, KeywordDetailsRepository keywordDetailsRepository, CacheManager cacheManager) {
        super(repository);
        _keywordDetailsRepository = keywordDetailsRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    public Envelope get(long id) {
        return super.get(id);
    }

    @CacheEvict(value = "keywords",allEntries = true)
    @Override
    public Envelope create(KeywordDetailsRequest request) {
        KeywordDetails e = convertForCreate(request);
        KeywordDetails res = _keywordDetailsRepository.save(e);
        Objects.requireNonNull(cacheManager.getCache("keywords")).clear();
        return CommonResponse.makeResponse(res,"Successful",true);
    }

    @Cacheable(value = "keywords")
    @Override
    public Set<String> getAllKeywordDetails() {
        return _keywordDetailsRepository.findKeywords();
    }
}
