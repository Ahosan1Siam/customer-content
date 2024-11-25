package com.assessment.consumer_content.application.service.implementation;

import com.assessment.consumer_content.application.dtos.response.*;
import com.assessment.consumer_content.application.helper.*;
import com.assessment.consumer_content.application.service.contract.IChargeFailureLogService;
import com.assessment.consumer_content.application.service.contract.IChargeSuccessLogService;
import com.assessment.consumer_content.application.service.contract.IInboxService;
import com.assessment.consumer_content.application.service.contract.IProcessHandlerService;
import com.assessment.consumer_content.domain.entities.ChargeFailureLog;
import com.assessment.consumer_content.domain.entities.ChargeSuccessLog;
import com.assessment.consumer_content.domain.entities.Inbox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.assessment.consumer_content.application.constant.GlobalConstant.DIVISOR_SIZE;

@Slf4j
@Service
public class ProcessHandlerService implements IProcessHandlerService {
    private final ValidateKeyWord _validateKeyWord;
    private final IInboxService _inboxService;
    private final KeywordDetailsService _keywordDetailsService;
    private final ChargeConfigService _chargeConfigService;
    private final IChargeSuccessLogService _chargeSuccessLogService;
    private final IChargeFailureLogService _chargeFailureLogService;
    private final ListPartitioner _listPartitioner;
    private final PerformChargingHelper _performChargingHelper;
    private final UnlockCodeHelper _unlockCodeHelper;
    private final BatchSaveHelper _batchSaveHelper;
    public ProcessHandlerService(ValidateKeyWord validateKeyWord, IInboxService inboxService, KeywordDetailsService keywordDetailsService, ChargeConfigService chargeConfigService, IChargeSuccessLogService chargeSuccessLogService, IChargeFailureLogService chargeFailureLogService, ListPartitioner listPartitioner, PerformChargingHelper performChargingHelper, UnlockCodeHelper unlockCodeHelper, BatchSaveHelper batchSaveHelper) {
        this._validateKeyWord = validateKeyWord;
        this._inboxService = inboxService;
        this._keywordDetailsService = keywordDetailsService;
        this._chargeConfigService = chargeConfigService;
        this._chargeSuccessLogService = chargeSuccessLogService;
        this._chargeFailureLogService = chargeFailureLogService;
        this._listPartitioner = listPartitioner;
        this._performChargingHelper = performChargingHelper;
        this._unlockCodeHelper = unlockCodeHelper;
        this._batchSaveHelper = batchSaveHelper;
    }


    public List<Inbox> HandleProcess(List<Inbox> inboxes){
        try (ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<List<Inbox>>> futures = _listPartitioner.partitionList(inboxes,DIVISOR_SIZE).stream()
                    .map(partition -> CompletableFuture.supplyAsync(() -> ExecuteProcess(partition), virtualThreadExecutor))
                    .toList();

            return futures.stream()
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        }
    }

    private List<Inbox> ExecuteProcess(List<Inbox> inboxes) {

        List<ChargeCodeResponse> chargeCodes = _chargeConfigService.getChargeConfigs();

        List<ChargeSuccessLog> successLogs = Collections.synchronizedList(new ArrayList<>());
        List<ChargeFailureLog> failures = Collections.synchronizedList(new ArrayList<>());
        List<Inbox> processedInboxes = Collections.synchronizedList(new ArrayList<>());

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            try {
                inboxes.parallelStream().forEach(inbox -> executor.submit(() -> {
                    Set<String> keywords = _keywordDetailsService.getAllKeywordDetails();
                    boolean isValid = _validateKeyWord.validate(keywords, inbox.getKeyword());
                    if (isValid) {
                        UnlockCodeResponse codeResponse = _unlockCodeHelper.unlock(inbox);
                        if (codeResponse.getStatusCode() == 200) {
                            PerformChargingResponse chargingResponse = _performChargingHelper.perform(chargeCodes, inbox);
                            if (chargingResponse.getStatusCode() == 200) {
                                inbox.setStatus("S");
                                synchronized (successLogs) {
                                    successLogs.add(_chargeSuccessLogService.createChargeSuccessLogObject(chargingResponse, inbox));
                                    _batchSaveHelper.saveBatch(successLogs,_chargeSuccessLogService::bulkSave,false);
                                }
                            } else {
                                inbox.setStatus("F");
                                synchronized (failures) {
                                    failures.add(_chargeFailureLogService.createChargeFailureLogObject(chargingResponse, inbox));
                                    _batchSaveHelper.saveBatch(failures, _chargeFailureLogService::bulkSave,false);
                                }
                            }
                        }
                    }
                    synchronized (processedInboxes) {
                        processedInboxes.add(inbox);
                        _batchSaveHelper.saveBatch(processedInboxes, _inboxService::bulkContentSave,false);
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

                _batchSaveHelper.saveBatch(successLogs, _chargeSuccessLogService::bulkSave,true);
                _batchSaveHelper.saveBatch(failures, _chargeFailureLogService::bulkSave,true);
                _batchSaveHelper.saveBatch(processedInboxes, _inboxService::bulkContentSave,true);
            }
        }

        return inboxes;
    }


}
