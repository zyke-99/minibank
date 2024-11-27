package com.zyke.minibank.controller;

import com.zyke.minibank.dto.CreateCustomerDto;
import com.zyke.minibank.dto.CustomerDto;
import com.zyke.minibank.service.CustomerServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerServiceImpl customerService;

    @GetMapping
    public ResponseEntity<Page<CustomerDto>> getClients(@SortDefault(sort = "lastModifiedDate", direction = Sort.Direction.DESC) @PageableDefault(size = 20) Pageable page,
                                                        @RequestParam(value = "searchTerm", required = false) String searchTerm) {

        return new ResponseEntity<>(customerService.search(page, searchTerm), OK);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createClient(@RequestBody @Valid CreateCustomerDto customer) {

        return new ResponseEntity<>(customerService.create(customer), CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("id") Long customerId, @RequestBody @Valid CreateCustomerDto customer) {

        return new ResponseEntity<>(customerService.update(customer, customerId), HttpStatus.OK);
    }
}
