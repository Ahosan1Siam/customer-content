package com.assessment.consumer_content.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "inbox")
public class Inbox extends BaseEntity{
    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "operator",length = 20)
    private String operator;

    @Column(name = "short_code",length = 25)
    private String shortCode;

    @Column(name = "msisdn",length = 20)
    private String msisdn;

    @Column(name = "keyword",length = 30)
    private String keyword;

    @Column(name = "game_name")
    private String gameName;

    @Column(name = "sms")
    private String sms;

    @Column(name = "status",length = 2)
    private String status;


}
