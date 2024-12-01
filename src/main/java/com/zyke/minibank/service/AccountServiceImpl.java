package com.zyke.minibank.service;

import com.zyke.minibank.entity.Account;
import com.zyke.minibank.entity.Customer;
import com.zyke.minibank.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public Account createForCustomer(Customer customer) {

        Account account = Account.builder()
                .balance(BigDecimal.ZERO)
                .build();

        account.addCustomer(customer);
        return accountRepository.save(account);
    }
}
