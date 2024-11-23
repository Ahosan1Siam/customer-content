package com.assessment.consumer_content.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "charge_config")
@AttributeOverrides({
        @AttributeOverride(name = "keyword", column = @Column(name = "keyword", insertable = false, updatable = false))
})
public class ChargeConfig extends BaseEntity {

    @Transient
    private String keyword;
    @Column(name = "operator",length = 20)
    private String operator;

    @Column(name = "charge_code",length = 25)
    private String chargeCode;
}
