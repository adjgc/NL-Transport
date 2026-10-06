package com.nexuslogistic.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexuslogistic.transport.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
