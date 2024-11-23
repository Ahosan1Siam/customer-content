package com.assessment.consumer_content.application.service.implementation;

import com.assessment.consumer_content.application.dtos.request.InboxRequest;
import com.assessment.consumer_content.application.dtos.response.CommonResponse;
import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.application.service.contract.IInboxService;
import com.assessment.consumer_content.domain.entities.Inbox;
import com.assessment.consumer_content.domain.repository.BaseRepository;
import com.assessment.consumer_content.domain.repository.InboxRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InboxService extends BaseService<Inbox, InboxRequest> implements IInboxService {
    private final InboxRepository _repository;
    protected InboxService(BaseRepository<Inbox> repository, InboxRepository repository1) {
        super(repository);
        _repository = repository1;
    }

    @Override
    public Envelope create(InboxRequest inboxRequest) {
        return super.create(inboxRequest);
    }

    public List<Inbox> bulkContentSave(List<Inbox> requestList){
        try {
            List<Inbox> inboxes= _repository.saveAll(requestList);
            return inboxes;
        }catch (Exception ex){
            return null;
        }
    }

    public List<Inbox> findAll(){
       return _repository.findAll();
    }
}
