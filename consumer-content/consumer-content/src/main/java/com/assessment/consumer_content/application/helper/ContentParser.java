package com.assessment.consumer_content.application.helper;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ContentParser {
    public Map<String, String> parseContent(String content) {
        Map<String, String> parsedMap = new HashMap<>();
        String[] parts = content.split(" ");
        if (parts.length >= 5) {
            parsedMap.put("Keyword", parts[0]);
            parsedMap.put("Game name", parts[1]);
            parsedMap.put("IMEI", parts[2]);
            parsedMap.put("Device Model", parts[4]);
        } else {
            return null;
        }
        return parsedMap;
    }
}
