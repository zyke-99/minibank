package com.zyke.minibank.repository;

import com.zyke.minibank.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    @Query("SELECT * FROM Customer c" +
            "WHERE c.name = :name " +
            "AND c.lastName = :lastName" +
            "AND c.phoneNumber = :phoneNumber" +
            "AND c.email = :email")
    Optional<Customer> findByPersonalData(@Param("name") String name,
                                          @Param("lastName") String lastName,
                                          @Param("phoneNumber") String phoneNumber,
                                          @Param("email") String email);
}
