package com.assessment.consumer_content.application.dtos.response;

import lombok.Data;

@Data
public class Envelope {
    private boolean Status;
    private String Message;
    private Object Payload;
}
