package com.zyke.minibank.service;

import com.zyke.minibank.entity.Account;
import com.zyke.minibank.entity.Customer;
import com.zyke.minibank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenCustomerProvided_createForCustomer_createAccount() {

        // set up
        Customer customer = Customer.builder().build();

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // execute
        Account actual = accountService.createForCustomer(customer);

        // verify
        verify(accountRepository).save(accountCaptor.capture());
        Account capturedAccount = accountCaptor.getValue();

        assertEquals(BigDecimal.ZERO, capturedAccount.getBalance());
        assertTrue(capturedAccount.getCustomers().contains(customer));
        assertThat(actual).usingRecursiveComparison().isEqualTo(capturedAccount);
    }
}
