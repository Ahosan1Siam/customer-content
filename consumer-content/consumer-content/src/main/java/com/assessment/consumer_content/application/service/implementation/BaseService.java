package com.assessment.consumer_content.application.service.implementation;

import com.assessment.consumer_content.application.dtos.request.BaseRequest;
import com.assessment.consumer_content.application.dtos.response.CommonResponse;
import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.application.helper.BaseServiceHelper;
import com.assessment.consumer_content.application.service.contract.IBaseService;
import com.assessment.consumer_content.domain.entities.BaseEntity;
import com.assessment.consumer_content.domain.repository.BaseRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

public abstract class BaseService<E extends BaseEntity, D extends BaseRequest>extends BaseServiceHelper<E, D> implements IBaseService<E,D> {
    private final BaseRepository<E> repository;
    private ModelMapper modelMapper;
    private EntityManager entityManager;

    protected BaseService(BaseRepository<E> repository) {
        this.repository = repository;
    }

    @Transactional
    public Envelope create(D d) {
        try {
            E e = convertForCreate(d);
            E res = repository.save(e);
            return CommonResponse.makeResponse(res, "Save Successfully", true);
        } catch (Exception e) {
            return CommonResponse.makeResponse(e, e.getMessage(), false);
        }
    }

    @Transactional
    public Envelope update(D d) {
        try {
            Long updateId = d.getId();
            E res = null;
            Optional<E> entityOptional = repository.findById(updateId);
            if (entityOptional.isPresent()) {
                E entity = entityOptional.get();
                Long id = entity.getId();
                BeanUtils.copyProperties(d, entity);
                entity.setId(id);
                res = repository.save(entity);
            }
            return CommonResponse.makeResponse(res, "Update Successfully", true);
        } catch (Exception e) {
            return CommonResponse.makeResponse(e, e.getMessage(), false);
        }
    }
    public Envelope get(long id){
        Optional<E> entityOptional = repository.findById(id);
        E entity = null;
        if(entityOptional.isPresent()) {
            entity = entityOptional.get();
        }
        return CommonResponse.makeResponse(entity, "Successfully Fetched", true);
    }
}
