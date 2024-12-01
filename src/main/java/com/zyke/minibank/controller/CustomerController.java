package com.zyke.minibank.controller;

import com.zyke.minibank.dto.CreateCustomerDto;
import com.zyke.minibank.dto.CustomerDto;
import com.zyke.minibank.orchestrator.CustomerOrchestrator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerOrchestrator customerOrchestrator;

    @PostMapping
    public ResponseEntity<CustomerDto> createClient(@RequestBody @Valid CreateCustomerDto customer) {

        return new ResponseEntity<>(customerOrchestrator.createCustomer(customer), CREATED);
    }
}
