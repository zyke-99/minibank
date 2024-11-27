package com.zyke.minibank.service;

import com.zyke.minibank.dto.AccountDto;
import com.zyke.minibank.dto.AddressDto;
import com.zyke.minibank.dto.CreateAccountDto;
import com.zyke.minibank.dto.CreateCustomerDto;
import com.zyke.minibank.dto.CustomerDto;
import com.zyke.minibank.entity.Address;
import com.zyke.minibank.entity.Customer;
import com.zyke.minibank.entity.CustomerType;
import com.zyke.minibank.exception.AddressOwnershipException;
import com.zyke.minibank.exception.CustomerNotFoundException;
import com.zyke.minibank.exception.CustomerNotUniqueException;
import com.zyke.minibank.mapper.CustomerMapper;
import com.zyke.minibank.repository.CustomerRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerServiceImplTest {

    @Mock
    private CustomerSpecificationFactory customerSpecificationFactory;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchUseSpecificationWhenSearchTermProvided() {

        // set up
        Pageable pageable = PageRequest.of(0, 10);
        String searchTerm = "Testman";

        Customer customer = Customer.builder()
                .id(1L)
                .build();

        Page<Customer> customerPage = new PageImpl<>(List.of(customer));

        Specification<Customer> specification = (Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("testField"), searchTerm);

        when(customerSpecificationFactory.getSearchTermSpecification(searchTerm)).thenReturn(specification);
        when(customerRepository.findAll(specification, pageable)).thenReturn(customerPage);
        CustomerDto customerDto = CustomerDto.builder()
                .id(1L)
                .build();

        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        // execute
        Page<CustomerDto> result = customerService.search(pageable, searchTerm);

        // verify
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).id());
        verify(customerRepository).findAll(specification, pageable);
    }

    @Test
    void testSearchFindAllWhenNoSearchTermProvided() {

        // set up
        Pageable pageable = PageRequest.of(0, 10);

        Customer customer = Customer.builder()
                .id(1L)
                .build();

        Page<Customer> customerPage = new PageImpl<>(List.of(customer));

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);
        CustomerDto customerDto = CustomerDto.builder()
                .id(1L)
                .build();

        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        // execute
        Page<CustomerDto> result = customerService.search(pageable, null);

        // verify
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).id());
        verify(customerRepository).findAll(pageable);
    }

    @Test
    void testCreateSuccess() {

        // set up
        CreateCustomerDto createCustomerDto = CreateCustomerDto.builder()
                .name("Testman")
                .lastName("Testberg")
                .phoneNumber("123456789")
                .email("testman@example.com")
                .type(CustomerType.PRIVATE)
                .addresses(List.of(
                        AddressDto.builder()
                                .country("US")
                                .city("LA")
                                .build()
                ))
                .build();

        Customer newCustomer = Customer.builder().id(1L).build();
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        when(customerRepository.save(customerCaptor.capture())).thenReturn(newCustomer);
        when(customerRepository.findByNameAndLastNameAndPhoneNumberAndEmail(createCustomerDto.name(), createCustomerDto.lastName(),
                createCustomerDto.phoneNumber(), createCustomerDto.email())).thenReturn(Optional.empty());

        AccountDto accountDto = AccountDto.builder().build();
        when(accountService.create(any())).thenReturn(accountDto);

        when(customerMapper.toDto(any())).thenReturn(CustomerDto.builder().build());

        // execute
        customerService.create(createCustomerDto);

        // verify
        // account created for correct customer
        ArgumentCaptor<CreateAccountDto> createAccountDtoArgumentCaptor = ArgumentCaptor.forClass(CreateAccountDto.class);
        verify(accountService).create(createAccountDtoArgumentCaptor.capture());
        CreateAccountDto caputeredCreateAccountDto = createAccountDtoArgumentCaptor.getValue();
        assertEquals(1, caputeredCreateAccountDto.customerIds().size());
        assertEquals(newCustomer.getId(), caputeredCreateAccountDto.customerIds().get(0));

        // customer created with correct input
        Customer capturedCustomer = customerCaptor.getValue();
        assertInputMatchesSaveArguments(createCustomerDto, capturedCustomer);
    }

    @Test
    void testCreateThrowsExceptionIfCustomerExists() {

        // setup
        CreateCustomerDto createCustomerDto = CreateCustomerDto.builder()
                .name("Testman")
                .lastName("Testberg")
                .phoneNumber("123456789")
                .email("john@example.com")
                .type(CustomerType.PRIVATE)
                .build();

        when(customerRepository.findByNameAndLastNameAndPhoneNumberAndEmail(createCustomerDto.name(), createCustomerDto.lastName(),
                createCustomerDto.phoneNumber(), createCustomerDto.email())).thenReturn(Optional.of(new Customer()));

        // execute and verify
        assertThrows(CustomerNotUniqueException.class, () -> customerService.create(createCustomerDto));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void testUpdate() {
        // set up
        Long customerId = 1L;
        CreateCustomerDto updateDto = CreateCustomerDto.builder()
                .name("Testianna")
                .lastName("Testberg")
                .phoneNumber("987654321")
                .email("testianna@example.com")
                .type(CustomerType.PRIVATE)
                .addresses(List.of(
                        AddressDto.builder()
                                .country("USA")
                                .city("LA")
                                .build()
                ))
                .build();

        Customer existingCustomer = Customer.builder().id(customerId).name("Testman").lastName("Testberg").build();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        when(customerRepository.save(customerCaptor.capture())).thenReturn(existingCustomer);

        // execute
        customerService.update(updateDto, customerId);

        // verify
        Customer capturedCustomer = customerCaptor.getValue();
        assertInputMatchesSaveArguments(updateDto, capturedCustomer);
    }

    @Test
    void testUpdateThrowsExceptionIfCustomerNotFound() {

        // setup
        Long customerId = 1L;
        CreateCustomerDto updateDto = CreateCustomerDto.builder().build();
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // execute and verify
        assertThrows(CustomerNotFoundException.class, () -> customerService.update(updateDto, customerId));
    }

    @Test
    void testUpdateThrowsExceptionIfCustomerDataNotUnique() {

        // setup
        Long customerId = 1L;
        CreateCustomerDto updateDto = CreateCustomerDto.builder()
                .name("Testman")
                .lastName("Testberg")
                .phoneNumber("123456789")
                .email("testman@example.com")
                .addresses(List.of())
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(Customer.builder().id(1L).build()));

        when(customerRepository.findByNameAndLastNameAndPhoneNumberAndEmail(updateDto.name(), updateDto.lastName(),
                updateDto.phoneNumber(), updateDto.email())).thenReturn(Optional.of(Customer.builder().id(2L).build()));

        // execute and verify
        assertThrows(CustomerNotUniqueException.class, () -> customerService.update(updateDto, customerId));
    }

    @Test
    void testUpdateThrowsExceptionIfProvidedAddressDoesNotBelongToUser() {

        // setup
        Long customerId = 1L;
        CreateCustomerDto updateDto = CreateCustomerDto.builder()
                .addresses(List.of(
                        AddressDto.builder()
                                .id(2L)
                                .build()
                ))
                .build();

        Customer existingCustomer = Customer.builder().build();
        existingCustomer.getAddresses().add(
                Address.builder()
                        .id(1L)
                        .build()
        );
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        // execute and verify
        assertThrows(AddressOwnershipException.class, () -> customerService.update(updateDto, customerId));
    }

    private void assertInputMatchesSaveArguments(CreateCustomerDto createCustomerDto, Customer capturedCustomer) {

        assertEquals(createCustomerDto.name(), capturedCustomer.getName());
        assertEquals(createCustomerDto.lastName(), capturedCustomer.getLastName());
        assertEquals(createCustomerDto.phoneNumber(), capturedCustomer.getPhoneNumber());
        assertEquals(createCustomerDto.email(), capturedCustomer.getEmail());
        assertEquals(createCustomerDto.type(), capturedCustomer.getType());

        assertNotNull(capturedCustomer.getAddresses());
        assertEquals(createCustomerDto.addresses().size(), capturedCustomer.getAddresses().size());

        for (int i = 0; i < createCustomerDto.addresses().size(); i++) {
            AddressDto expectedAddress = createCustomerDto.addresses().get(i);
            Address capturedAddress = capturedCustomer.getAddresses().get(i);

            assertEquals(expectedAddress.country(), capturedAddress.getCountry());
            assertEquals(expectedAddress.city(), capturedAddress.getCity());
        }
    }
}
