package com.assessment.consumer_content.application.service.contract;

import com.assessment.consumer_content.domain.entities.ChargeFailureLog;

import java.util.List;

public interface IChargeFailureLogService {
    List<ChargeFailureLog> bulkSave(List<ChargeFailureLog> requestList);
}
