package com.zyke.minibank.service;

import com.zyke.minibank.dto.CustomerDto;
import com.zyke.minibank.entity.Address;
import com.zyke.minibank.entity.Customer;
import com.zyke.minibank.exception.AddressOwnershipException;
import com.zyke.minibank.exception.CustomerNotFoundException;
import com.zyke.minibank.exception.CustomerNotUniqueException;
import com.zyke.minibank.mapper.CustomerMapper;
import com.zyke.minibank.repository.CustomerRepository;
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

    public Page<CustomerDto> search(Pageable page, String searchTerm) {

        Page<Customer> customers = StringUtils.isEmpty(searchTerm) ? customerRepository.findAll(page) :
                customerRepository.findAll(
                        customerSpecificationFactory.getSearchTermSpecification(searchTerm),
                        page
                );

        return customers.map(customerMapper::toCustomerDto);
    }

    @Override
    public Customer create(Customer customer) {

        if (getCustomerByPersonalData(customer).isPresent()) {

            throw new CustomerNotUniqueException(HttpStatus.BAD_REQUEST, "Customer already exists with such data");
        }

        return customerRepository.save(customer);
    }

    public Customer update(Long id, Customer customer) {

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(HttpStatus.NOT_FOUND, String.format("Customer with ID '%s' not found", id)));

        Optional<Customer> foundCustomer = getCustomerByPersonalData(customer);
        if (foundCustomer.isPresent() && !foundCustomer.get().getId().equals(id)) {

            throw new CustomerNotUniqueException(HttpStatus.BAD_REQUEST, "Customer already exists with such data");
        }

        existingCustomer.setName(customer.getName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setType(customer.getType());

        updateAddresses(existingCustomer, customer.getAddresses());

        return existingCustomer;
    }

    private void updateAddresses(Customer existingCustomer, List<Address> updatedAddresses) {
        List<Address> existingAddresses = existingCustomer.getAddresses();

        updatedAddresses.forEach(updatedAddress -> {
            Address existingAddress = existingAddresses.stream()
                    .filter(address -> address.getId().equals(updatedAddress.getId()))
                    .findFirst()
                    .orElseThrow(() -> new AddressOwnershipException(HttpStatus.BAD_REQUEST,
                            String.format("Address with ID '%s' does not belong to the customer", updatedAddress.getId())));

            existingAddress.setCountry(updatedAddress.getCountry());
            existingAddress.setPostcode(updatedAddress.getPostcode());
            existingAddress.setRegion(updatedAddress.getRegion());
            existingAddress.setTown(updatedAddress.getTown());
            existingAddress.setStreetType(updatedAddress.getStreetType());
            existingAddress.setStreetName(updatedAddress.getStreetName());
            existingAddress.setStreetNumber(updatedAddress.getStreetNumber());
            existingAddress.setFloor(updatedAddress.getFloor());
        });
    }

    private Optional<Customer> getCustomerByPersonalData(Customer customer) {

        return customerRepository.findByPersonalData(customer.getName(),
                customer.getLastName(),
                customer.getPhoneNumber(),
                customer.getEmail());
    }
}
