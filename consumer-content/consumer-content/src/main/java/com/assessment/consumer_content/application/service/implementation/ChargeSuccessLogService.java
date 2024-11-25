package com.assessment.consumer_content.application.service.implementation;

import com.assessment.consumer_content.application.dtos.response.PerformChargingResponse;
import com.assessment.consumer_content.application.service.contract.IChargeSuccessLogService;
import com.assessment.consumer_content.domain.entities.ChargeSuccessLog;
import com.assessment.consumer_content.domain.entities.Inbox;
import com.assessment.consumer_content.domain.repository.ChargeSuccessLogRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChargeSuccessLogService  implements IChargeSuccessLogService {
    private final ChargeSuccessLogRepository _chargeSuccessLogRepository;

    public ChargeSuccessLogService(ChargeSuccessLogRepository chargeSuccessLogRepository) {
        _chargeSuccessLogRepository = chargeSuccessLogRepository;
    }

    public List<ChargeSuccessLog> bulkSave(List<ChargeSuccessLog> requestList){
        try {
            return _chargeSuccessLogRepository.saveAll(requestList);
        }catch (Exception ex){
            return null;
        }
    }
    @Override
    public ChargeSuccessLog createChargeSuccessLogObject(PerformChargingResponse chargingResponse, Inbox inbox) {
        ChargeSuccessLog successLog = new ChargeSuccessLog();
        BeanUtils.copyProperties(chargingResponse, successLog);
        successLog.setSmsId(inbox.getId());
        successLog.setKeyword(inbox.getKeyword());
        successLog.setGameName(inbox.getGameName());
        return successLog;
    }
}
