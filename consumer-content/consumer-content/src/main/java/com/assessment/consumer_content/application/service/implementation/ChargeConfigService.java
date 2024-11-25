package com.assessment.consumer_content.application.service.implementation;

import com.assessment.consumer_content.application.dtos.request.ChargeConfigRequest;
import com.assessment.consumer_content.application.dtos.response.ChargeCodeResponse;
import com.assessment.consumer_content.application.dtos.response.CommonResponse;
import com.assessment.consumer_content.application.dtos.response.Envelope;
import com.assessment.consumer_content.application.service.contract.IChargeConfigService;
import com.assessment.consumer_content.domain.entities.ChargeConfig;
import com.assessment.consumer_content.domain.repository.BaseRepository;
import com.assessment.consumer_content.domain.repository.ChargeConfigRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Override
    public Envelope bulkSave(List<ChargeConfigRequest> requests) {
        List<ChargeConfig> configs = new ArrayList<>();
        for (ChargeConfigRequest request : requests) {
            ChargeConfig config = new ChargeConfig();
            BeanUtils.copyProperties(request, config);
            configs.add(config);
        }
        var saveList =_chargeConfigRepository.saveAll(configs);
        return CommonResponse.makeResponse(saveList,"Saved Response",!saveList.isEmpty());
    }
}
