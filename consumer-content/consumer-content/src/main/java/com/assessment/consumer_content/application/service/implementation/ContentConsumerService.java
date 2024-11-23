package com.assessment.consumer_content.application.service.implementation;

import com.assessment.consumer_content.application.dtos.response.CommonResponse;
import com.assessment.consumer_content.application.dtos.response.ContentConsumerResponse;

import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.application.service.contract.IContentConsumerService;
import com.assessment.consumer_content.application.service.contract.IInboxService;
import com.assessment.consumer_content.application.service.contract.IProcessHandlerService;
import com.assessment.consumer_content.domain.entities.Inbox;
import com.assessment.consumer_content.infrastructure.client.ContentProviderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;


@Slf4j
@Service
public class ContentConsumerService  implements IContentConsumerService {
    private final IInboxService iInboxService;
    private final ContentProviderClient _contentProvideClient;
    private final IProcessHandlerService _processHandlerService;
    public ContentConsumerService(IInboxService iInboxService, ContentProviderClient contentProvideClient, IProcessHandlerService processHandlerService) {
        this.iInboxService = iInboxService;
        _contentProvideClient = contentProvideClient;
        _processHandlerService = processHandlerService;
    }

    public Envelope getContents(){
        try {
            Mono<ContentConsumerResponse> contentRetrievalClientResponse = _contentProvideClient.getContent();
            ContentConsumerResponse response = contentRetrievalClientResponse.block();
            if(response.getStatusCode()!=200) return CommonResponse.makeResponse(response.getStatusCode(),response.getMessage(),false);
            List<Inbox> savedList =  iInboxService.modifyAndSaveList(response.getContents());
            _processHandlerService.HandleProcess(savedList);
            return CommonResponse.makeResponse(savedList,"Successful",!savedList.isEmpty());
        }catch (Exception ex){
            return CommonResponse.makeResponse(ex, ex.getMessage(), false);
        }
    }
}
