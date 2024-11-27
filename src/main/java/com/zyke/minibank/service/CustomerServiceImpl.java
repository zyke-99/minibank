package com.zyke.minibank.service;

import com.zyke.minibank.dto.AccountDto;
import com.zyke.minibank.dto.AddressDto;
import com.zyke.minibank.dto.CreateAccountDto;
import com.zyke.minibank.dto.CreateCustomerDto;
import com.zyke.minibank.dto.CustomerDto;
import com.zyke.minibank.entity.Account;
import com.zyke.minibank.entity.Address;
import com.zyke.minibank.entity.Customer;
import com.zyke.minibank.exception.AddressOwnershipException;
import com.zyke.minibank.exception.CustomerNotFoundException;
import com.zyke.minibank.exception.CustomerNotUniqueException;
import com.zyke.minibank.mapper.CustomerMapper;
import com.zyke.minibank.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final CustomerSpecificationFactory customerSpecificationFactory;

    private final AccountService accountService;

    @Override
    public Page<CustomerDto> search(Pageable page, String searchTerm) {

        Page<Customer> customers = StringUtils.isEmpty(searchTerm) ? customerRepository.findAll(page) :
                customerRepository.findAll(
                        customerSpecificationFactory.getSearchTermSpecification(searchTerm),
                        page
                );

        return customers.map(customerMapper::toDto);
    }

    @Override
    @Transactional
    public CustomerDto create(CreateCustomerDto customer) {

        validateCreate(customer);

        Customer newCustomer = Customer.builder()
                .name(customer.name())
                .lastName(customer.lastName())
                .phoneNumber(customer.phoneNumber())
                .email(customer.email())
                .type(customer.type())
                .build();

        newCustomer.getAddresses().addAll(
                customer.addresses().stream()
                        .map(addressDto -> Address.builder()
                                .country(addressDto.country())
                                .city(addressDto.city()).build()).toList()
        );

        newCustomer = customerRepository.save(newCustomer);
        AccountDto customerAccount = accountService.create(
                CreateAccountDto.builder()
                        .customerIds(List.of(newCustomer.getId()))
                        .build()
        );

        newCustomer.getAccounts().add(
                Account.builder()
                        .id(customerAccount.id())
                        .balance(customerAccount.balance())
                        .build()
        );
        return customerMapper.toDto(newCustomer);
    }

    @Override
    @Transactional
    public CustomerDto update(CreateCustomerDto customer, Long customerId) {

        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(HttpStatus.NOT_FOUND, String.format("Customer with ID '%s' not found", customerId)));

        validateUpdate(customer, customerId);

        existingCustomer.setName(customer.name());
        existingCustomer.setLastName(customer.lastName());
        existingCustomer.setPhoneNumber(customer.phoneNumber());
        existingCustomer.setEmail(customer.email());
        existingCustomer.setType(customer.type());

        updateCustomerAddresses(existingCustomer, customer.addresses());

        return customerMapper.toDto(customerRepository.save(existingCustomer));
    }

    private void updateCustomerAddresses(Customer existingCustomer, List<AddressDto> addresses) {

        existingCustomer.getAddresses().removeIf(existingAddress -> {
            Long existingAddressId = existingAddress.getId();

            return addresses.isEmpty() || addresses.stream()
                    .noneMatch(updatedAddress -> updatedAddress.id() != null && updatedAddress.id().equals(existingAddressId));
        });

        addresses.forEach(updatedAddress -> {
            Long addressId = updatedAddress.id();

            if (addressId != null) {

                existingCustomer.getAddresses().stream()
                        .filter(existingAddress -> existingAddress.getId().equals(addressId))
                        .findFirst()
                        .ifPresentOrElse(existingAddress -> {
                            existingAddress.setCity(updatedAddress.city());
                            existingAddress.setCountry(updatedAddress.country());
                        }, () -> {
                            throw new AddressOwnershipException(HttpStatus.BAD_REQUEST, "Provided address ID does not belong to the user");
                        });
            } else {

                existingCustomer.getAddresses().add(
                        Address.builder()
                                .country(updatedAddress.country())
                                .city(updatedAddress.city())
                                .build()
                );
            }
        });
    }

    private void validateCreate(CreateCustomerDto customer) {

        if (getCustomerByPersonalData(customer).isPresent()) {

            throw new CustomerNotUniqueException(HttpStatus.BAD_REQUEST, "Customer already exists with such data");
        }
    }

    private void validateUpdate(CreateCustomerDto customer, Long customerId) {

        Optional<Customer> foundCustomer = getCustomerByPersonalData(customer);
        if (foundCustomer.isPresent() && !foundCustomer.get().getId().equals(customerId)) {

            throw new CustomerNotUniqueException(HttpStatus.BAD_REQUEST, "Customer already exists with such data");
        }
    }

    private Optional<Customer> getCustomerByPersonalData(CreateCustomerDto customer) {

        return customerRepository.findByNameAndLastNameAndPhoneNumberAndEmail(customer.name(),
                customer.lastName(),
                customer.phoneNumber(),
                customer.email());
    }
}
