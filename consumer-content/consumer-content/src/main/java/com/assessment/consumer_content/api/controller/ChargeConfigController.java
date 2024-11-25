package com.assessment.consumer_content.api.controller;

import com.assessment.consumer_content.application.dtos.request.ChargeConfigRequest;
import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.application.service.contract.IBaseService;
import com.assessment.consumer_content.application.service.contract.IChargeConfigService;
import com.assessment.consumer_content.domain.entities.ChargeConfig;
import com.assessment.consumer_content.infrastructure.annotation.ApiController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@ApiController
@RequestMapping("api/v1/charge-config")
public class ChargeConfigController extends BaseController<ChargeConfig, ChargeConfigRequest> {
    private final IChargeConfigService _chargeConfigService;
    public ChargeConfigController(IBaseService<ChargeConfig, ChargeConfigRequest> service, IChargeConfigService chargeConfigService) {
        super(service);
        _chargeConfigService = chargeConfigService;
    }


    @PostMapping("/save-bulk-keyword")
    public Envelope saveKeywords(@RequestBody List<ChargeConfigRequest> request) {
        return _chargeConfigService.bulkSave(request);
    }
}
