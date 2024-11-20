package com.assessment.consumer_content.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String uuid;

    @NotNull
    private Boolean isDeleted;

    @NotNull
    private String createdBy;

    @NotNull
    private LocalDateTime createdDate;

    private String modifyBy;

    private LocalDateTime modifiedDate;
}
