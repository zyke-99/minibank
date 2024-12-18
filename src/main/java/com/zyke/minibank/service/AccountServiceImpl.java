package com.zyke.minibank.service;

import com.zyke.minibank.entity.Account;
import com.zyke.minibank.entity.AccountStatus;
import com.zyke.minibank.entity.AccountType;
import com.zyke.minibank.entity.Currency;
import com.zyke.minibank.entity.Customer;
import com.zyke.minibank.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public Account createForCustomer(Customer customer) {

        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .accountType(AccountType.CHECKING)
                .balance(BigDecimal.ZERO)
                .availableBalance(BigDecimal.ZERO)
                .currency(Currency.EUR)
                .status(AccountStatus.ACTIVE)
                .build();

        account.addCustomer(customer);
        return accountRepository.save(account);
    }

    private String generateAccountNumber() {

        return UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
}
