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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.assessment.consumer_content.application.constant.GlobalConstant.BATCH_SIZE;
@Slf4j
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
        if (inboxes.isEmpty()) {
            inboxes.addAll(_inboxService.findAll());
        }

        Set<String> keywords = _keywordDetailsService.getAllKeywordDetails();
        List<ChargeCodeResponse> chargeCodes = _chargeConfigService.getChargeConfigs();

        List<ChargeSuccessLog> successLogs = Collections.synchronizedList(new ArrayList<>());
        List<ChargeFailureLog> failures = Collections.synchronizedList(new ArrayList<>());
        List<Inbox> processedInboxes = Collections.synchronizedList(new ArrayList<>());

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        try {
            inboxes.parallelStream().forEach(inbox -> executor.submit(() -> {
                boolean isValid = _validateKeyWord.validate(keywords, inbox.getKeyword());
                if (isValid) {
                    UnlockCodeRequest request = new UnlockCodeRequest();
                    BeanUtils.copyProperties(inbox, request);
                    Mono<UnlockCodeResponse> unlockCodeResponse = _contentProviderClient.getUnlockCode(request);
                    UnlockCodeResponse codeResponse = unlockCodeResponse.block();

                    if (codeResponse.getStatusCode() == 200) {
                        String chargeCode = _chargeCodeParser.getChargeCode(chargeCodes, inbox.getOperator());
                        PerformChargingRequest chargingRequest = new PerformChargingRequest();
                        BeanUtils.copyProperties(inbox, chargingRequest);
                        chargingRequest.setChargeCode(chargeCode);

                        Mono<PerformChargingResponse> chargingResponseMono = _contentProviderClient.performCharging(chargingRequest);
                        PerformChargingResponse chargingResponse = chargingResponseMono.block();

                        if (chargingResponse.getStatusCode() == 200) {
                            ChargeSuccessLog successLog = new ChargeSuccessLog();
                            BeanUtils.copyProperties(chargingResponse, successLog);
                            successLog.setSmsId(inbox.getId());
                            successLog.setKeyword(inbox.getKeyword());
                            successLog.setGameName(inbox.getGameName());
                            inbox.setStatus("S");

                            synchronized (successLogs) {
                                successLogs.add(successLog);
                                saveBatchIfFull(successLogs, BATCH_SIZE, _chargeSuccessLogService::bulkSave);
                            }
                        } else {
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
                            inbox.setStatus("F");

                            synchronized (failures) {
                                failures.add(failureLog);
                                saveBatchIfFull(failures, BATCH_SIZE, _chargeFailureLogService::bulkSave);
                            }
                        }
                    }
                }

                synchronized (processedInboxes) {
                    processedInboxes.add(inbox);
                    saveBatchIfFull(processedInboxes, BATCH_SIZE, _inboxService::bulkContentSave);
                }
            }));

        } catch (Exception ex) {
            log.error("Error processing inboxes: {}", ex.getMessage());
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.error("Executor did not terminate in the specified time.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Executor termination interrupted: {}", e.getMessage());
            }

            saveRemainingLogs(successLogs, _chargeSuccessLogService::bulkSave);
            saveRemainingLogs(failures, _chargeFailureLogService::bulkSave);
            saveRemainingLogs(processedInboxes, _inboxService::bulkContentSave);
        }

        return CommonResponse.makeResponse(true, "Process completed successfully", true);
    }

    private <T> void saveBatchIfFull(List<T> list, int batchSize, java.util.function.Consumer<List<T>> saveFunction) {
        if (list.size() >= batchSize) {
            List<T> batch = new ArrayList<>(list);
            list.clear();
            saveFunction.accept(batch);
        }
    }

    private <T> void saveRemainingLogs(List<T> list, java.util.function.Consumer<List<T>> saveFunction) {
        if (!list.isEmpty()) {
            saveFunction.accept(new ArrayList<>(list));
            list.clear();
        }
    }
}
