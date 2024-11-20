package com.assessment.consumer_content.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "inbox")
public class Inbox extends BaseEntity{
    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "operator",length = 15)
    private String operator;

    @Column(name = "short_code",length = 20)
    private String shortCode;

    @Column(name = "msisdn",length = 13)
    private String msisdn;

    @Column(name = "keyword",length = 20)
    private String keyword;

    @Column(name = "game_name")
    private String gameName;

    @Column(name = "sms",length = 20)
    private String sms;

    @Column(name = "status",length = 1)
    private String status;


}
