package com.assessment.consumer_content.application.service.implementation;

import com.assessment.consumer_content.application.dtos.request.ChargeConfigRequest;
import com.assessment.consumer_content.application.dtos.response.ChargeCodeResponse;
import com.assessment.consumer_content.application.service.contract.IChargeConfigService;
import com.assessment.consumer_content.domain.entities.ChargeConfig;
import com.assessment.consumer_content.domain.repository.BaseRepository;
import com.assessment.consumer_content.domain.repository.ChargeConfigRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChargeConfigService extends BaseService<ChargeConfig, ChargeConfigRequest> implements IChargeConfigService {
    private final ChargeConfigRepository _chargeConfigRepository;
    protected ChargeConfigService(BaseRepository<ChargeConfig> repository, ChargeConfigRepository chargeConfigRepository) {
        super(repository);
        _chargeConfigRepository = chargeConfigRepository;
    }
    public List<ChargeCodeResponse> getChargeConfigs() {
        return _chargeConfigRepository.getOperatorsWithChargeCode();
    }
}
