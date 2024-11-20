package com.assessment.consumer_content.api.controller;

import com.assessment.consumer_content.application.dtos.request.BaseRequest;
import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.application.service.contract.IBaseService;
import com.assessment.consumer_content.domain.entities.BaseEntity;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
public abstract class BaseController<E extends BaseEntity, D extends BaseRequest> {

    private final IBaseService<E, D> service;

    @PostMapping()
    public Envelope create(@RequestBody D d) {
        return service.create(d);
    }

    @PutMapping()
    public Envelope update(@RequestBody D d) {
        return service.update(d);
    }



    @GetMapping("/get-by-id/{id}")
    public Envelope get(@PathVariable Long id) {
        return service.get(id);
    }


}
