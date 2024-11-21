package com.assessment.consumer_content.application.service.implementation;

import com.assessment.consumer_content.application.dtos.response.CommonResponse;
import com.assessment.consumer_content.application.dtos.response.ContentRetrievalResponse;
import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.application.service.contract.IContentRetrievalService;
import com.assessment.consumer_content.application.service.contract.IInboxService;
import com.assessment.consumer_content.infrastructure.client.ContentProviderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ContentRetrievalService implements IContentRetrievalService {
    private final IInboxService iInboxService;
    private final ContentProviderClient _contentProvideClient;
    public ContentRetrievalService(IInboxService iInboxService, ContentProviderClient contentProvideClient) {
        this.iInboxService = iInboxService;
        _contentProvideClient = contentProvideClient;
    }

    public Envelope getContents(){
        try {
            Mono<ContentRetrievalResponse> contentRetrievalClientResponse = _contentProvideClient.getContents();
            ContentRetrievalResponse response = contentRetrievalClientResponse.block();
            log.info("info =>",response.getMessage());
            //this.iInboxService.bulkContentSave(response.getContents());
            return CommonResponse.makeResponse(true,"Successful",true);
        }catch (Exception ex){
            return CommonResponse.makeResponse(ex, ex.getMessage(), false);
        }
    }
}
