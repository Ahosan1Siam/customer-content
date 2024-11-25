package com.assessment.consumer_content.application.service.contract;

import com.assessment.consumer_content.application.dtos.response.PerformChargingResponse;
import com.assessment.consumer_content.domain.entities.ChargeFailureLog;
import com.assessment.consumer_content.domain.entities.Inbox;

import java.util.List;

public interface IChargeFailureLogService {
    List<ChargeFailureLog> bulkSave(List<ChargeFailureLog> requestList);
    ChargeFailureLog createChargeFailureLogObject(PerformChargingResponse chargingResponse, Inbox inbox);
}
