package com.zyke.minibank.service;

import com.zyke.minibank.dto.CreateCustomerDto;
import com.zyke.minibank.dto.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    Page<CustomerDto> search(Pageable page, String searchTerm);

    CustomerDto create(CreateCustomerDto customer);

    CustomerDto update(CreateCustomerDto customer, Long customerId);
}
