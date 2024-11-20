package com.assessment.consumer_content.domain.repository;

import com.assessment.consumer_content.domain.entities.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository <E extends BaseEntity> extends JpaRepository<E,Long> {

}
