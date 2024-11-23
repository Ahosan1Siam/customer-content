package com.assessment.consumer_content.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "keyword_details")
public class KeywordDetails extends BaseEntity {
}
