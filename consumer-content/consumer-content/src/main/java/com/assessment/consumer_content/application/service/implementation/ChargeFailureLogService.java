package com.assessment.consumer_content.application.service.implementation;

import com.assessment.consumer_content.application.service.contract.IChargeFailureLogService;
import com.assessment.consumer_content.domain.entities.ChargeFailureLog;
import com.assessment.consumer_content.domain.repository.ChargeFailureLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChargeFailureLogService implements IChargeFailureLogService {
    private final ChargeFailureLogRepository _chargeFailureLogRepository;

    public ChargeFailureLogService(ChargeFailureLogRepository chargeFailureLogRepository) {
        _chargeFailureLogRepository = chargeFailureLogRepository;
    }

    public List<ChargeFailureLog> bulkSave(List<ChargeFailureLog> requestList){
        try {
            return _chargeFailureLogRepository.saveAll(requestList);
        }catch (Exception ex){
            return null;
        }
    }
}
