package com.assessment.consumer_content.domain.repository;

import com.assessment.consumer_content.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
