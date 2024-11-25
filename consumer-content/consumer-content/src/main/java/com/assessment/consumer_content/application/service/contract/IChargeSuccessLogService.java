package com.assessment.consumer_content.application.service.contract;

import com.assessment.consumer_content.application.dtos.response.PerformChargingResponse;
import com.assessment.consumer_content.domain.entities.ChargeSuccessLog;
import com.assessment.consumer_content.domain.entities.Inbox;

import java.util.List;

public interface IChargeSuccessLogService {
    List<ChargeSuccessLog> bulkSave(List<ChargeSuccessLog> requestList);
    ChargeSuccessLog createChargeSuccessLogObject(PerformChargingResponse chargingResponse, Inbox inbox);
}
