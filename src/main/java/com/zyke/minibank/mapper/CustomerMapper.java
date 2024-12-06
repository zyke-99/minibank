package com.zyke.minibank.mapper;

import com.zyke.minibank.dto.CreateCustomerDto;
import com.zyke.minibank.dto.CustomerAccountDto;
import com.zyke.minibank.dto.CustomerDto;
import com.zyke.minibank.entity.Address;
import com.zyke.minibank.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerMapper {

    private final AddressMapper addressMapper;

    public CustomerDto toCustomerDto(Customer customer) {

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
                        .map(addressMapper::toAddressDto).toList())
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

        if (createCustomerDto == null) {

            return null;
        }

        Customer customer = Customer.builder()
                .name(createCustomerDto.name())
                .lastName(createCustomerDto.lastName())
                .phoneNumber(createCustomerDto.phoneNumber())
                .email(createCustomerDto.email())
                .type(createCustomerDto.type())
                .build();

        if (createCustomerDto.addresses() != null) {

            List<Address> addresses = createCustomerDto.addresses().stream()
                    .map(addressMapper::fromAddressDto)
                    .toList();

            customer.setAddresses(addresses);
        }

        return customer;
    }
}
