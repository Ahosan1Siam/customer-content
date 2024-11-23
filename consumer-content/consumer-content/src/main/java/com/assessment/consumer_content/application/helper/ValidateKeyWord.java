package com.assessment.consumer_content.application.helper;

import com.assessment.consumer_content.application.service.contract.IKeywordDetailsService;
import org.springframework.stereotype.Component;


import java.util.Set;

@Component
public class ValidateKeyWord {
    private final IKeywordDetailsService _keywordDetailsService;

    public ValidateKeyWord(IKeywordDetailsService keywordDetailsService) {
        _keywordDetailsService = keywordDetailsService;
    }
    public boolean validate(Set<String> stringSet, String content) {
        return stringSet.contains(content);
    }
}
