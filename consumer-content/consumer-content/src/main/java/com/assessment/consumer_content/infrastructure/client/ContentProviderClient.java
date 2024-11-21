package com.assessment.consumer_content.infrastructure.client;

import com.assessment.consumer_content.application.dtos.response.ContentRetrievalResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ContentProviderClient {
    private final WebClient client;
    private final ServiceDiscoveryClient serviceDiscoveryClient;

    public ContentProviderClient(WebClient.Builder builder, ServiceDiscoveryClient serviceDiscoveryClient) {
        this.client = builder.build();
        this.serviceDiscoveryClient = serviceDiscoveryClient;
    }

    public Mono<ContentRetrievalResponse> getContents(){
        String url = serviceDiscoveryClient.getContentProviderUrl();
        return this.client
                .get()
                .uri(url)
                .header("*/*")
                .retrieve()
                .bodyToMono(ContentRetrievalResponse.class)
                .flatMap(Mono::just)
                .onErrorResume(ex -> Mono.empty());
    }

}
