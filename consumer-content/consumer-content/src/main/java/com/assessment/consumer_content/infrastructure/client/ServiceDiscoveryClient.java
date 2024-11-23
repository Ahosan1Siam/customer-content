package com.assessment.consumer_content.infrastructure.client;

import org.springframework.stereotype.Component;

@Component
public class ServiceDiscoveryClient {
    public String getContentProviderUrl(){
        return "http://demo.webmanza.com/a55dbz923ace647v/api/v1.0/services/content";
    }
    public String getCodeRetrievalUrl(){
        return "http://demo.webmanza.com/a55dbz923ace647v/api/v1.0/services/unlockCode";
    }
    public String getPerformChargingUrl(){
        return "http://demo.webmanza.com/a55dbz923ace647v/api/v1.0/services/charge";
    }
}
