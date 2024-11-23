package com.assessment.consumer_content.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "charge_failure_log")
public class ChargeFailureLog extends BaseEntity{
    private Long smsId;
    private String transactionId;
    private String operator;
    private String shortCode;
    private String msisdn;
    private String gameName;
    private Integer statusCode;
    private String message;
}
