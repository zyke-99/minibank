package com.zyke.minibank.orchestrator;

import com.zyke.minibank.dto.CreateCustomerDto;
import com.zyke.minibank.dto.CustomerDto;
import com.zyke.minibank.entity.Customer;
import com.zyke.minibank.mapper.CustomerMapper;
import com.zyke.minibank.service.AccountService;
import com.zyke.minibank.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerOrchestrator {

    private final CustomerMapper customerMapper;

    private final CustomerService customerService;
    private final AccountService accountService;

    public CustomerDto createCustomer(CreateCustomerDto createCustomerDto) {

        Customer customer = customerMapper.fromCreateCustomerDto(createCustomerDto);

        customer = customerService.create(customer);
        accountService.createForCustomer(customer);

        return customerMapper.toCustomerDto(customer);
    }

    public CustomerDto updateCustomer(Long id, CreateCustomerDto createCustomerDto) {

        Customer customer = customerMapper.fromCreateCustomerDto(createCustomerDto);
        return customerMapper.toCustomerDto(customerService.update(id, customer));
    }
}
