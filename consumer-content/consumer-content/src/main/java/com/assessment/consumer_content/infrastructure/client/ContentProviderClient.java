package com.assessment.consumer_content.infrastructure.client;

import com.assessment.consumer_content.application.dtos.request.PerformChargingRequest;
import com.assessment.consumer_content.application.dtos.request.UnlockCodeRequest;
import com.assessment.consumer_content.application.dtos.response.ContentConsumerResponse;
import com.assessment.consumer_content.application.dtos.response.PerformChargingResponse;
import com.assessment.consumer_content.application.dtos.response.StatusResponse;
import com.assessment.consumer_content.application.dtos.response.UnlockCodeResponse;
import com.assessment.consumer_content.application.helper.ResponseStatusChecker;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ContentProviderClient {
    private final WebClient client;
    private final ServiceDiscoveryClient serviceDiscoveryClient;
    private final ResponseStatusChecker responseStatusChecker;

    public ContentProviderClient(WebClient.Builder builder, ServiceDiscoveryClient serviceDiscoveryClient, ResponseStatusChecker responseStatusChecker) {
        this.client = builder.build();
        this.serviceDiscoveryClient = serviceDiscoveryClient;
        this.responseStatusChecker = responseStatusChecker;
    }

    public Mono<UnlockCodeResponse> getUnlockCode(UnlockCodeRequest request){
        String url = serviceDiscoveryClient.getCodeRetrievalUrl();
        return this.client
                .post()
                .uri(url)
                .body(BodyInserters.fromValue(request))
                .header("*/*")
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(UnlockCodeResponse.class)
                                .flatMap(responseStatusChecker::checkStatus);
                    } else {
                        return response.bodyToMono(UnlockCodeResponse.class)
                                .map(errorResponse -> {
                                    if (errorResponse.getStatusCode() == 0) {
                                        errorResponse.setStatusCode(response.statusCode().value());
                                    }
                                    if (errorResponse.getMessage() == null || errorResponse.getMessage().isEmpty()) {
                                        errorResponse.setMessage("An error occurred");
                                    }
                                    return errorResponse;
                                });
                    }
                }).onErrorResume(ex -> {
                    UnlockCodeResponse defaultResponse = new UnlockCodeResponse();
            return Mono.just(defaultResponse);
        });
    }
    public Mono<PerformChargingResponse> performCharging(PerformChargingRequest request){
        String url = serviceDiscoveryClient.getPerformChargingUrl();
        return this.client
                .post()
                .uri(url)
                .body(BodyInserters.fromValue(request))
                .header("*/*")
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(PerformChargingResponse.class)
                                .flatMap(responseStatusChecker::checkStatus);
                    } else {
                        return response.bodyToMono(PerformChargingResponse.class)
                                .map(errorResponse -> {
                                    if (errorResponse.getStatusCode() == 0) {
                                        errorResponse.setStatusCode(response.statusCode().value());
                                    }
                                    if (errorResponse.getMessage() == null || errorResponse.getMessage().isEmpty()) {
                                        errorResponse.setMessage("An error occurred");
                                    }
                                    return errorResponse;
                                });
                    }
                }).onErrorResume(ex -> {
                    PerformChargingResponse defaultResponse = new PerformChargingResponse();
                    defaultResponse.setMessage(ex.getMessage());
                    return Mono.just(defaultResponse);
                });
    }

    public Mono<ContentConsumerResponse> getContent() {
        String url = serviceDiscoveryClient.getContentProviderUrl();
        WebClient webClient = WebClient.builder().exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build())
                .build();
        return webClient
                .get()
                .uri(url)
                .header("*/*")
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(ContentConsumerResponse.class)
                                .flatMap(responseStatusChecker::checkStatus);
                    } else {
                        return response.bodyToMono(ContentConsumerResponse.class)
                                .map(errorResponse -> {
                                    if (errorResponse.getStatusCode() == 0) {
                                        errorResponse.setStatusCode(response.statusCode().value());
                                    }
                                    if (errorResponse.getMessage() == null || errorResponse.getMessage().isEmpty()) {
                                        errorResponse.setMessage("An error occurred");
                                    }
                                    return errorResponse;
                                });
                    }
                }).onErrorResume(ex -> {
                    ContentConsumerResponse defaultResponse = new ContentConsumerResponse();
                    defaultResponse.setMessage(ex.getMessage());
                    return Mono.just(defaultResponse);
                });
    }
}
