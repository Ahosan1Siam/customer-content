package com.assessment.consumer_content.application.helper;


import com.assessment.consumer_content.application.dtos.response.StatusResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
@Component
public class ResponseStatusChecker  {
    public <T extends StatusResponse> Mono<T> checkStatus(T response) {
        if (response.getStatusCode() != 200) {
            logErrorBasedOnStatus(response);
        }
        return Mono.just(response);
    }
    private void logErrorBasedOnStatus(StatusResponse response) {
        switch (response.getStatusCode()) {
            case 400 -> System.err.println("Bad Request: " + response.getMessage());
            case 500 -> System.err.println("Internal Server Error: " + response.getMessage());
            case 503 -> System.err.println("Service Unavailable: " + response.getMessage());
            case 504 -> System.err.println("Gateway Timeout: " + response.getMessage());
            default -> System.err.println("Unexpected error: " + response.getMessage());
        }
    }

}
