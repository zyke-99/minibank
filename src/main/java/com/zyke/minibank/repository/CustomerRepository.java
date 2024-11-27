package com.zyke.minibank.repository;

import com.zyke.minibank.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Optional<Customer> findByNameAndLastNameAndPhoneNumberAndEmail(String name, String lastName, String phoneNumber, String email);
}
