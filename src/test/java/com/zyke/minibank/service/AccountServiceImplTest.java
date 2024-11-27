package com.zyke.minibank.service;

import com.zyke.minibank.dto.AccountDto;
import com.zyke.minibank.dto.CreateAccountDto;
import com.zyke.minibank.entity.Account;
import com.zyke.minibank.entity.Customer;
import com.zyke.minibank.exception.CustomerNotFoundException;
import com.zyke.minibank.exception.MinibankException;
import com.zyke.minibank.mapper.AccountMapper;
import com.zyke.minibank.repository.AccountRepository;
import com.zyke.minibank.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSuccess() {

        // set up
        Long customerId = 1L;
        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .customerIds(List.of(customerId))
                .build();

        Customer customer = Customer.builder()
                .id(customerId)
                .name("Test")
                .lastName("Testman")
                .build();

        Account account = Account.builder()
                .id(1L)
                .balance(BigDecimal.ZERO)
                .customers(List.of(customer))
                .build();

        AccountDto accountDto = AccountDto.builder()
                .id(1L)
                .balance(BigDecimal.ZERO)
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        // execute
        AccountDto result = accountService.create(createAccountDto);

        // verify
        verify(customerRepository).findById(customerId);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());

        Account capturedAccount = accountCaptor.getValue();
        assertNotNull(capturedAccount);
        assertEquals(BigDecimal.ZERO, capturedAccount.getBalance());
        assertEquals(1, capturedAccount.getCustomers().size());
        assertEquals(customerId, capturedAccount.getCustomers().get(0).getId());

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.balance());
    }

    @Test
    void testCreateThrowsExceptionWhenCustomerNotFound() {

        // set up
        Long customerId = 1L;
        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .customerIds(List.of(customerId))
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // execute and verify
        MinibankException exception = assertThrows(CustomerNotFoundException.class, () -> accountService.create(createAccountDto));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        verify(customerRepository).findById(customerId);
        verify(accountRepository, never()).save(any());
    }
}
