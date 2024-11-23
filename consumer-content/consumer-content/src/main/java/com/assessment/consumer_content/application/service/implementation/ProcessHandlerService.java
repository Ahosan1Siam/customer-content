package com.assessment.consumer_content.application.service.implementation;

import com.assessment.consumer_content.application.dtos.request.PerformChargingRequest;
import com.assessment.consumer_content.application.dtos.request.UnlockCodeRequest;
import com.assessment.consumer_content.application.dtos.response.*;
import com.assessment.consumer_content.application.helper.ChargeCodeParser;
import com.assessment.consumer_content.application.helper.ValidateKeyWord;
import com.assessment.consumer_content.application.service.contract.IChargeFailureLogService;
import com.assessment.consumer_content.application.service.contract.IChargeSuccessLogService;
import com.assessment.consumer_content.application.service.contract.IInboxService;
import com.assessment.consumer_content.application.service.contract.IProcessHandlerService;
import com.assessment.consumer_content.domain.entities.ChargeFailureLog;
import com.assessment.consumer_content.domain.entities.ChargeSuccessLog;
import com.assessment.consumer_content.domain.entities.Inbox;
import com.assessment.consumer_content.infrastructure.client.ContentProviderClient;
import com.assessment.consumer_content.infrastructure.client.ServiceDiscoveryClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ProcessHandlerService implements IProcessHandlerService {
    private final ValidateKeyWord _validateKeyWord;
    private final ContentProviderClient _contentProviderClient;
    private final IInboxService _inboxService;
    private final KeywordDetailsService _keywordDetailsService;
    private final ChargeCodeParser _chargeCodeParser;
    private final ChargeConfigService _chargeConfigService;
    private final IChargeSuccessLogService _chargeSuccessLogService;
    private final IChargeFailureLogService _chargeFailureLogService;
    public ProcessHandlerService(ValidateKeyWord validateKeyWord, ContentProviderClient contentProviderClient, IInboxService inboxService, KeywordDetailsService keywordDetailsService, ChargeCodeParser chargeCodeParser, ChargeConfigService chargeConfigService, IChargeSuccessLogService chargeSuccessLogService, IChargeFailureLogService chargeFailureLogService) {
        this._validateKeyWord = validateKeyWord;
        _contentProviderClient = contentProviderClient;
        _inboxService = inboxService;
        _keywordDetailsService = keywordDetailsService;
        this._chargeCodeParser = chargeCodeParser;
        _chargeConfigService = chargeConfigService;
        _chargeSuccessLogService = chargeSuccessLogService;
        _chargeFailureLogService = chargeFailureLogService;
    }

    public Envelope HandleProcess(List<Inbox> inboxes) {
        if(inboxes.isEmpty()){
           inboxes.addAll(_inboxService.findAll());
        }
        Set<String> keywords = _keywordDetailsService.getAllKeywordDetails();
        List<ChargeCodeResponse> chargeCodes = _chargeConfigService.getChargeConfigs();
        List<ChargeSuccessLog> successLogs = new ArrayList<>();
        List<ChargeFailureLog> failures = new ArrayList<>();
        for (Inbox inbox : inboxes) {
            boolean valid= _validateKeyWord.validate(keywords,inbox.getKeyword());
            if (valid) {
                UnlockCodeRequest request = new UnlockCodeRequest();
                BeanUtils.copyProperties(inbox, request);
                Mono<UnlockCodeResponse> unlockCodeResponse= _contentProviderClient.getUnlockCode(request);
                UnlockCodeResponse codeResponse = unlockCodeResponse.block();
                if (codeResponse.getStatusCode() == 200) {
                   String chargeCode =  _chargeCodeParser.getChargeCode(chargeCodes,inbox.getOperator());
                   PerformChargingRequest performChargingRequest = new PerformChargingRequest();
                   BeanUtils.copyProperties(inbox,performChargingRequest);
                   performChargingRequest.setChargeCode(chargeCode);
                   Mono<PerformChargingResponse> chargingResponseMono = _contentProviderClient.performCharging(performChargingRequest);
                   PerformChargingResponse chargingResponse = chargingResponseMono.block();
                   if (chargingResponse.getStatusCode() ==200) {
                       ChargeSuccessLog chargeSuccessLog = new ChargeSuccessLog();
                       BeanUtils.copyProperties(chargingResponse,chargeSuccessLog);
                       chargeSuccessLog.setSmsId(inbox.getId());
                       chargeSuccessLog.setKeyword(inbox.getKeyword());
                       chargeSuccessLog.setGameName(inbox.getGameName());
                       inbox.setStatus("S");
                   }else {
                       ChargeFailureLog chargeFailureLog = new ChargeFailureLog();
                       BeanUtils.copyProperties(chargingResponse,chargeFailureLog);
                       chargeFailureLog.setSmsId(inbox.getId());
                       chargeFailureLog.setKeyword(inbox.getKeyword());
                       chargeFailureLog.setGameName(inbox.getGameName());
                       chargeFailureLog.setStatusCode(chargingResponse.getStatusCode());
                       chargeFailureLog.setMessage(chargingResponse.getMessage());
                       inbox.setStatus("F");
                   }
               }
            }
        }
        _chargeSuccessLogService.bulkSave(successLogs);
        _chargeFailureLogService.bulkSave(failures);
        _inboxService.bulkContentSave(inboxes);

        return CommonResponse.makeResponse(true,"",true);
    }
}
