package com.assessment.consumer_content.application.helper;

import com.assessment.consumer_content.application.dtos.request.UnlockCodeRequest;
import com.assessment.consumer_content.application.dtos.response.UnlockCodeResponse;
import com.assessment.consumer_content.domain.entities.Inbox;
import com.assessment.consumer_content.infrastructure.client.ContentProviderClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
@Component
public class UnlockCodeHelper {
    private final ContentProviderClient _contentProviderClient;

    public UnlockCodeHelper(ContentProviderClient contentProviderClient) {
        _contentProviderClient = contentProviderClient;
    }

    public UnlockCodeResponse unlock(Inbox inbox) {
        UnlockCodeRequest request = new UnlockCodeRequest();
        BeanUtils.copyProperties(inbox, request);
        Mono<UnlockCodeResponse> unlockCodeResponse = _contentProviderClient.getUnlockCode(request);
        return unlockCodeResponse.block();
    }
}
