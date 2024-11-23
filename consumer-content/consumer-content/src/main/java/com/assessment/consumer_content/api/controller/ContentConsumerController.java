package com.assessment.consumer_content.api.controller;

import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.application.service.contract.IContentConsumerService;
import com.assessment.consumer_content.infrastructure.annotation.ApiController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@ApiController
@RequestMapping("api/v1/content-retrieval")
public class ContentConsumerController {
    private final IContentConsumerService service;

    public ContentConsumerController(IContentConsumerService service) {
        this.service = service;
    }

    @GetMapping("/get")
    public Envelope getContents() {
        return service.getContents();
    }
}
