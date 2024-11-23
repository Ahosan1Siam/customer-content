package com.assessment.consumer_content.application.service.contract;

import com.assessment.consumer_content.domain.entities.ChargeSuccessLog;

import java.util.List;

public interface IChargeSuccessLogService {
    List<ChargeSuccessLog> bulkSave(List<ChargeSuccessLog> requestList);
}
