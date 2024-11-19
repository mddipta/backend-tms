package com.lawencon.ticket.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lawencon.ticket.persistence.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByCompanyId(String id);

    Optional<Customer> findByUserId(String userId);

    List<Customer> findByPicUser_Id(String id);
}
