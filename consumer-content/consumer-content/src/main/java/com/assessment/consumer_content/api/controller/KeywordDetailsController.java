package com.assessment.consumer_content.api.controller;

import com.assessment.consumer_content.application.dtos.request.KeywordDetailsRequest;
import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.application.service.contract.IBaseService;
import com.assessment.consumer_content.application.service.contract.IKeywordDetailsService;
import com.assessment.consumer_content.domain.entities.KeywordDetails;
import com.assessment.consumer_content.infrastructure.annotation.ApiController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@ApiController
@RequestMapping("api/v1/keyword-details")
public class KeywordDetailsController extends BaseController<KeywordDetails, KeywordDetailsRequest> {
    private final IKeywordDetailsService _keywordDetailsService;
    public KeywordDetailsController(IBaseService<KeywordDetails, KeywordDetailsRequest> service, IKeywordDetailsService keywordDetailsService) {
        super(service);
        this._keywordDetailsService = keywordDetailsService;
    }

    @PostMapping("/save-bulk-data")
    public Envelope saveKeywords(@RequestBody List<KeywordDetailsRequest> request) {
       return _keywordDetailsService.bulkSave(request);
    }
}
