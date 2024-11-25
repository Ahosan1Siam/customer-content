package com.assessment.consumer_content.domain.repository;

import com.assessment.consumer_content.domain.entities.KeywordDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface KeywordDetailsRepository extends BaseRepository<KeywordDetails>{
    @Query("""
        SELECT distinct (kd.keyword) FROM KeywordDetails kd
        """)
    Set<String> findKeywords();
}
