package com.assessment.consumer_content.application.service.contract;

import com.assessment.consumer_content.application.dtos.request.ChargeConfigRequest;
import com.assessment.consumer_content.application.dtos.response.ChargeCodeResponse;
import com.assessment.consumer_content.domain.entities.ChargeConfig;

import java.util.List;

public interface IChargeConfigService extends IBaseService<ChargeConfig,ChargeConfigRequest>{
    List<ChargeCodeResponse> getChargeConfigs();
}
