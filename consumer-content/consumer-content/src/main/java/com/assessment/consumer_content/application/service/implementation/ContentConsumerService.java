package com.assessment.consumer_content.application.service.implementation;

import com.assessment.consumer_content.application.dtos.response.CommonResponse;
import com.assessment.consumer_content.application.dtos.response.ContentConsumerResponse;

import com.assessment.consumer_content.application.dtos.response.ContentsResponse;
import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.application.helper.ListPartitioner;
import com.assessment.consumer_content.application.service.contract.IContentConsumerService;
import com.assessment.consumer_content.application.service.contract.IInboxService;
import com.assessment.consumer_content.application.service.contract.IProcessHandlerService;
import com.assessment.consumer_content.domain.entities.Inbox;
import com.assessment.consumer_content.infrastructure.client.ContentProviderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.assessment.consumer_content.application.constant.GlobalConstant.DIVISOR_SIZE;


@Slf4j
@Service
public class ContentConsumerService  implements IContentConsumerService {
    private final IInboxService iInboxService;
    private final ContentProviderClient _contentProvideClient;
    private final IProcessHandlerService _processHandlerService;
    private final ListPartitioner _listPartitioner;
    public ContentConsumerService(IInboxService iInboxService, ContentProviderClient contentProvideClient, IProcessHandlerService processHandlerService, ListPartitioner listPartitioner) {
        this.iInboxService = iInboxService;
        _contentProvideClient = contentProvideClient;
        _processHandlerService = processHandlerService;
        _listPartitioner = listPartitioner;
    }

    public Envelope getContents(){
        try {
            Mono<ContentConsumerResponse> contentRetrievalClientResponse = _contentProvideClient.getContent();
            ContentConsumerResponse response = contentRetrievalClientResponse.block();
            if(response.getStatusCode()!=200) return CommonResponse.makeResponse(response.getStatusCode(),response.getMessage(),false);
            List<Inbox> savedContents = partitionContentsAndSave(response.getContents());
            List<Inbox> result = _processHandlerService.HandleProcess(savedContents);
            String successMessage = String.format("Total : %d data Processed",result.size());
            return CommonResponse.makeResponse(result,successMessage,!result.isEmpty());
        }catch (Exception ex){
            return CommonResponse.makeResponse(ex, ex.getMessage(), false);
        }
    }

    private List<Inbox> partitionContentsAndSave(List<ContentsResponse> responses){
        try (ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<List<Inbox>>> futures = _listPartitioner.partitionList(responses,DIVISOR_SIZE).stream()
                    .map(partition -> CompletableFuture.supplyAsync(() -> iInboxService.modifyAndSaveList(partition), virtualThreadExecutor))
                    .toList();

             return futures.stream()
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        }
    }


}
