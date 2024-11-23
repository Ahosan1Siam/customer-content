package com.assessment.consumer_content.application.service.implementation;

import com.assessment.consumer_content.application.dtos.request.InboxRequest;
import com.assessment.consumer_content.application.dtos.response.CommonResponse;
import com.assessment.consumer_content.application.dtos.response.ContentsResponse;
import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.application.helper.ContentParser;
import com.assessment.consumer_content.application.service.contract.IInboxService;
import com.assessment.consumer_content.domain.entities.Inbox;
import com.assessment.consumer_content.domain.repository.BaseRepository;
import com.assessment.consumer_content.domain.repository.InboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.assessment.consumer_content.application.constant.GlobalConstant.BATCH_SIZE;
@Slf4j
@Service
public class InboxService extends BaseService<Inbox, InboxRequest> implements IInboxService {
    private final InboxRepository _repository;
    private final ContentParser contentParser;
    protected InboxService(BaseRepository<Inbox> repository, InboxRepository repository1, ContentParser contentParser) {
        super(repository);
        _repository = repository1;
        this.contentParser = contentParser;
    }

    @Override
    public Envelope create(InboxRequest inboxRequest) {
        return super.create(inboxRequest);
    }

    public List<Inbox> bulkContentSave(List<Inbox> requestList){
        try {
            return _repository.saveAll(requestList);
        }catch (Exception ex){
            return null;
        }
    }
    @Override
    public List<Inbox> findAll(){
       return _repository.findAll();
    }
    @Override
    public List<Inbox> modifyAndSaveList(List<ContentsResponse> responseList) {
        List<Inbox> savedList = new ArrayList<>();

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        List<Inbox> inboxes = Collections.synchronizedList(new ArrayList<>());
        try {
            responseList.parallelStream().forEach(content -> executor.submit(() -> {
                Map<String, String> stringMap = contentParser.parseContent(content.getSms());
                if (stringMap == null) {
                    log.error("Invalid SMS Format. Skipping content: {}", content.getSms());
                    return;
                }
                inboxes.add(createInboxObject(content,stringMap));
                synchronized (inboxes) {
                    if (inboxes.size() >= BATCH_SIZE) {
                        List<Inbox> batchToSave = new ArrayList<>(inboxes);
                        inboxes.clear();
                        var bulkContentSave = bulkContentSave(batchToSave);
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
                var finalBulkSave = bulkContentSave(new ArrayList<>(inboxes));
                savedList.addAll(finalBulkSave);
            }
        }
        return savedList;
    }
    private Inbox createInboxObject(ContentsResponse content,Map<String,String > stringMap){
        Inbox inbox = new Inbox();
        BeanUtils.copyProperties(content, inbox);
        inbox.setStatus("N");
        inbox.setKeyword(stringMap.get("Keyword"));
        inbox.setGameName(stringMap.get("Game name"));
        return inbox;
    }
}
