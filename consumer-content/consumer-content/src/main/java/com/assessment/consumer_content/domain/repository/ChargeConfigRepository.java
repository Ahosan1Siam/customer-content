package com.assessment.consumer_content.domain.repository;

import com.assessment.consumer_content.application.dtos.response.ChargeCodeResponse;
import com.assessment.consumer_content.domain.entities.ChargeConfig;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ChargeConfigRepository extends BaseRepository<ChargeConfig>{
    @Query("""
        SELECT com.assessment.consumer_content.application.dtos.response.ChargeCodeResponse(c.operator,c.chargeCode) FROM ChargeConfig c
        """)
    List<ChargeCodeResponse> getOperatorsWithChargeCode();

}
