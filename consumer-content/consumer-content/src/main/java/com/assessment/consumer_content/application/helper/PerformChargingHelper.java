package com.assessment.consumer_content.application.helper;

import com.assessment.consumer_content.application.dtos.request.PerformChargingRequest;
import com.assessment.consumer_content.application.dtos.response.ChargeCodeResponse;
import com.assessment.consumer_content.application.dtos.response.PerformChargingResponse;
import com.assessment.consumer_content.domain.entities.Inbox;
import com.assessment.consumer_content.infrastructure.client.ContentProviderClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class PerformChargingHelper {

    private final ChargeCodeParser _chargeCodeParser;
    private final ContentProviderClient _contentProviderClient;
    public PerformChargingHelper(ChargeCodeParser chargeCodeParser, ContentProviderClient contentProviderClient) {
        _chargeCodeParser = chargeCodeParser;
        _contentProviderClient = contentProviderClient;
    }

    public PerformChargingResponse perform(List<ChargeCodeResponse> chargeCodes, Inbox inbox) {
        String chargeCode = _chargeCodeParser.getChargeCode(chargeCodes, inbox.getOperator());
        PerformChargingRequest chargingRequest = new PerformChargingRequest();
        BeanUtils.copyProperties(inbox, chargingRequest);
        chargingRequest.setChargeCode(chargeCode);
        Mono<PerformChargingResponse> chargingResponseMono = _contentProviderClient.performCharging(chargingRequest);
        return chargingResponseMono.block();
    }
}
