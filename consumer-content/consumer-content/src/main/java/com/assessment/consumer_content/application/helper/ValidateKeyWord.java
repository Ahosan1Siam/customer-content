package com.assessment.consumer_content.application.helper;

import org.springframework.stereotype.Component;


import java.util.Set;

@Component
public class ValidateKeyWord {

    public boolean validate(Set<String> stringSet, String content) {
        return stringSet.contains(content);
    }
}
