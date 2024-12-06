package com.zyke.minibank.service;

import com.zyke.minibank.entity.Address;
import com.zyke.minibank.entity.Customer;
import com.zyke.minibank.entity.CustomerType;
import com.zyke.minibank.exception.CustomerNotFoundException;
import com.zyke.minibank.exception.CustomerNotUniqueException;
import com.zyke.minibank.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenCustomer_whenCreate_thenSaveCustomer() {

        // set up
        Customer customer = buildTestCustomer();

        when(customerRepository.save(customer)).thenReturn(customer);

        // execute
        Customer actual = customerService.create(customer);

        // verify
        assertThat(actual).usingRecursiveComparison().isEqualTo(customer);
    }

    @Test
    public void givenCustomerWithDuplicatePersonalData_whenCreate_thenThrowException() {

        // set up
        Customer customer = buildTestCustomer();

        when(customerRepository.findByPersonalData(customer.getName(), customer.getLastName(),
                customer.getPhoneNumber(), customer.getEmail())).thenReturn(Optional.of(customer));

        // execute and verify
        assertThrows(CustomerNotUniqueException.class, () -> customerService.create(customer));
    }

    @Test
    public void givenCustomer_whenUpdate_thenUpdateCustomer() {

        // set up
        Customer existingCustomer = buildTestCustomer();
        Long customerId = existingCustomer.getId();

        Customer updatedCustomer = buildTestCustomer();
        updatedCustomer.setName("UpdatedName");
        updatedCustomer.setLastName("UpdatedLastName");
        updatedCustomer.setPhoneNumber("UpdatedPhoneNumber");
        updatedCustomer.setEmail("UpdatedEmail");
        updatedCustomer.setType(CustomerType.PUBLIC);
        updatedCustomer.getAddresses().forEach(address -> address.setCity("UpdatedCity"));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        // execute
        Customer actual = customerService.update(customerId, updatedCustomer);

        // verify
        assertThat(actual).usingRecursiveComparison().isEqualTo(updatedCustomer);
    }

    @Test
    public void givenCustomerThatDoesNotExist_whenUpdate_thenExceptionThrown() {

        // set up
        Customer updatedCustomer = buildTestCustomer();
        Long customerId = updatedCustomer.getId();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // execute and verify
        assertThrows(CustomerNotFoundException.class, () -> customerService.update(customerId, updatedCustomer));
    }

    @Test
    public void givenCustomerWithDuplicatePersonalData_whenUpdate_thenExceptionThrown() {

        // set up
        Customer customerWithSamePersonalData = Customer.builder().id(2L).build();

        Customer updatedCustomer = buildTestCustomer();
        Long customerId = updatedCustomer.getId();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(updatedCustomer));

        when(customerRepository.findByPersonalData(updatedCustomer.getName(), updatedCustomer.getLastName(),
                updatedCustomer.getPhoneNumber(), updatedCustomer.getEmail())).thenReturn(Optional.of(customerWithSamePersonalData));

        // execute and verify
        assertThrows(CustomerNotUniqueException.class, () -> customerService.update(customerId, updatedCustomer));
    }

    private Customer buildTestCustomer() {

        return Customer.builder()
                .id(1L)
                .name("Testman")
                .lastName("Testberg")
                .phoneNumber("123456789")
                .email("test@test.test")
                .type(CustomerType.PRIVATE)
                .addresses(List.of(
                        Address.builder()
                                .id(1L)
                                .country("Testuania")
                                .city("Testnius")
                                .build()
                ))
                .build();
    }
}
