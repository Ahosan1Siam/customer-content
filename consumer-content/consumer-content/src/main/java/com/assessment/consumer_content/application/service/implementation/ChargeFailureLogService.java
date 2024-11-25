package com.assessment.consumer_content.application.service.implementation;

import com.assessment.consumer_content.application.dtos.response.PerformChargingResponse;
import com.assessment.consumer_content.application.service.contract.IChargeFailureLogService;
import com.assessment.consumer_content.domain.entities.ChargeFailureLog;
import com.assessment.consumer_content.domain.entities.Inbox;
import com.assessment.consumer_content.domain.repository.ChargeFailureLogRepository;
import org.springframework.beans.BeanUtils;
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
    @Override
    public ChargeFailureLog createChargeFailureLogObject(PerformChargingResponse chargingResponse, Inbox inbox) {
        ChargeFailureLog failureLog = new ChargeFailureLog();
        BeanUtils.copyProperties(chargingResponse, failureLog);
        failureLog.setSmsId(inbox.getId());
        failureLog.setKeyword(inbox.getKeyword());
        failureLog.setGameName(inbox.getGameName());
        failureLog.setStatusCode(chargingResponse.getStatusCode());
        failureLog.setMessage(chargingResponse.getMessage());
        failureLog.setMsisdn(inbox.getMsisdn());
        failureLog.setOperator(inbox.getOperator());
        failureLog.setShortCode(inbox.getShortCode());
        failureLog.setTransactionId(inbox.getTransactionId());
        return failureLog;
    }
}
