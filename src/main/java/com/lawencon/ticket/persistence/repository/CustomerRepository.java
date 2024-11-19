package com.lawencon.ticket.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawencon.ticket.persistence.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByCompanyId(String id);

    Optional<Customer> findByUserId(String id);
}
