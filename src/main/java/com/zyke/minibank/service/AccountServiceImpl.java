package com.zyke.minibank.service;

import com.zyke.minibank.dto.AccountDto;
import com.zyke.minibank.dto.CreateAccountDto;
import com.zyke.minibank.entity.Account;
import com.zyke.minibank.entity.Customer;
import com.zyke.minibank.exception.MinibankException;
import com.zyke.minibank.mapper.AccountMapper;
import com.zyke.minibank.repository.AccountRepository;
import com.zyke.minibank.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public AccountDto createAccount(CreateAccountDto createAccountDto) {

        List<Customer> accountCustomers = createAccountDto.customerIds().stream()
                .map(customerId -> {
                    Optional<Customer> customerOptional = customerRepository.findById(customerId);
                    if (customerOptional.isPresent()) {

                        return customerOptional.get();
                    }
                    throw new MinibankException(HttpStatus.BAD_REQUEST, String.format("No customer with provided id '%s' found when creating an account", customerId));
                })
                .toList();

        Account account = Account.builder()
                .balance(BigDecimal.ZERO)
                .customers(accountCustomers)
                .build();

        return accountMapper.toDto(accountRepository.save(account));
    }
}
