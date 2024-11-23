package com.assessment.consumer_content.application.helper;

import com.assessment.consumer_content.application.dtos.response.ChargeCodeResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ChargeCodeParser {

    public String getChargeCode(List<ChargeCodeResponse> responseList, String operator){
       Optional<ChargeCodeResponse> response= responseList
               .stream().
               filter(x->x.getOperator()
               .equals(operator)).findFirst();
        return response.map(ChargeCodeResponse::getChargeCode).orElse(null);
    }
}
