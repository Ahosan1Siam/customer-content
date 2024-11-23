package com.assessment.consumer_content.application.service.implementation;

import com.assessment.consumer_content.application.dtos.response.CommonResponse;
import com.assessment.consumer_content.application.dtos.response.ContentConsumerResponse;

import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.application.helper.ContentParser;
import com.assessment.consumer_content.application.service.contract.IContentConsumerService;
import com.assessment.consumer_content.application.service.contract.IInboxService;
import com.assessment.consumer_content.application.service.contract.IProcessHandlerService;
import com.assessment.consumer_content.domain.entities.Inbox;
import com.assessment.consumer_content.infrastructure.client.ContentProviderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import java.util.concurrent.*;

import static com.assessment.consumer_content.application.constant.GlobalConstant.BATCH_SIZE;

@Slf4j
@Service
public class ContentConsumerService  implements IContentConsumerService {
    private final IInboxService iInboxService;
    private final ContentProviderClient _contentProvideClient;
    private final  ContentParser contentParser;
    private final IProcessHandlerService _processHandlerService;
    public ContentConsumerService(IInboxService iInboxService, ContentProviderClient contentProvideClient, ContentParser contentParser, IProcessHandlerService processHandlerService) {
        this.iInboxService = iInboxService;
        _contentProvideClient = contentProvideClient;
        this.contentParser = contentParser;
        _processHandlerService = processHandlerService;
    }

    public Envelope getContents(){
        try {
            Mono<ContentConsumerResponse> contentRetrievalClientResponse = _contentProvideClient.getContent();
            ContentConsumerResponse response = contentRetrievalClientResponse.block();
            if(response.getStatusCode()!=200) return CommonResponse.makeResponse(response.getStatusCode(),response.getMessage(),false);
            List<Inbox> savedList = new ArrayList<>();

            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
            List<Inbox> inboxes = Collections.synchronizedList(new ArrayList<>());

            try {
                response.getContents().parallelStream().forEach(content -> executor.submit(() -> {
                    Map<String, String> stringMap = contentParser.parseContent(content.getSms());
                    if (stringMap == null) {
                        log.error("Invalid SMS Format. Skipping content: {}", content.getSms());
                        return;
                    }

                    Inbox inbox = new Inbox();
                    BeanUtils.copyProperties(content, inbox);
                    inbox.setStatus("N");
                    inbox.setKeyword(stringMap.get("Keyword"));
                    inbox.setGameName(stringMap.get("Game name"));
                    inboxes.add(inbox);

                    synchronized (inboxes) {
                        if (inboxes.size() >= BATCH_SIZE) {
                            List<Inbox> batchToSave = new ArrayList<>(inboxes);
                            inboxes.clear();
                            var bulkContentSave = iInboxService.bulkContentSave(batchToSave);
                            savedList.addAll(bulkContentSave);
                        }
                    }
                }));
            } catch (Exception ex) {
                log.error("Error processing contents: {}", ex.getMessage());
            } finally {
                executor.shutdown();
                try {
                    if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                        log.error("Executor did not terminate in the specified time.");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Executor termination interrupted: {}", e.getMessage());
                }

                if (!inboxes.isEmpty()) {
                    var finalBulkSave = iInboxService.bulkContentSave(new ArrayList<>(inboxes));
                    savedList.addAll(finalBulkSave);
                }
            }
            _processHandlerService.HandleProcess(savedList);
            return CommonResponse.makeResponse(savedList,"Successful",!savedList.isEmpty());
        }catch (Exception ex){
            return CommonResponse.makeResponse(ex, ex.getMessage(), false);
        }
    }
}
