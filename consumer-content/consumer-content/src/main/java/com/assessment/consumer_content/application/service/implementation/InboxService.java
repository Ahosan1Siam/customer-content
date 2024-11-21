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

    public Envelope bulkContentSave(List<InboxRequest> requestList){
        try {
            List<Inbox> list = new ArrayList<>();
            requestList.forEach(x->{
                Inbox inbox= new Inbox();
                BeanUtils.copyProperties(x,inbox);
                list.add(inbox);
            });
            _repository.saveAll(list);
            return CommonResponse.makeResponse(true,"SuccessFul",true);
        }catch (Exception ex){
            return CommonResponse.makeResponse(ex,ex.getMessage(),false);
        }
    }
}
