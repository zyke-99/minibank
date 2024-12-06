package com.zyke.minibank.controller;

import com.zyke.minibank.dto.CreateCustomerDto;
import com.zyke.minibank.dto.CustomerDto;
import com.zyke.minibank.orchestrator.CustomerOrchestrator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerOrchestrator customerOrchestrator;

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody @Valid CreateCustomerDto customerDto) {

        return new ResponseEntity<>(customerOrchestrator.createCustomer(customerDto), CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable(name = "id") Long id, @RequestBody @Valid CreateCustomerDto customerDto) {

        return new ResponseEntity<>(customerOrchestrator.updateCustomer(id, customerDto), OK);
    }
}
