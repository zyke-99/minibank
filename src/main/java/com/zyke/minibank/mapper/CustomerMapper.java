package com.zyke.minibank.mapper;

import com.zyke.minibank.dto.CreateCustomerDto;
import com.zyke.minibank.dto.CustomerAccountDto;
import com.zyke.minibank.dto.CustomerDto;
import com.zyke.minibank.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class CustomerMapper {

    private final AddressMapper addressMapper;

    public CustomerDto toDto(Customer customer) {

        if (customer == null) {

            return null;
        }

        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .lastName(customer.getLastName())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .type(customer.getType())
                .addresses(customer.getAddresses().stream()
                        .map(addressMapper::toDto).toList())
                .accounts(customer.getAccounts().stream()
                        .map(account -> CustomerAccountDto.builder()
                                .id(account.getId())
                                .balance(account.getBalance())
                                .build()
                        ).toList())
                .createdBy(customer.getCreatedBy())
                .creationDate(customer.getCreationDate())
                .lastModifiedBy(customer.getLastModifiedBy())
                .lastModifiedDate(customer.getLastModifiedDate())
                .build();
    }

    public Customer fromCreateCustomerDto(CreateCustomerDto createCustomerDto) {

        Customer customer = Customer.builder()
                .name(createCustomerDto.name())
                .lastName(createCustomerDto.lastName())
                .phoneNumber(createCustomerDto.phoneNumber())
                .email(createCustomerDto.email())
                .type(createCustomerDto.type())
                .build();

        customer.setAddresses(new ArrayList<>());
        return customer;
    }
}
