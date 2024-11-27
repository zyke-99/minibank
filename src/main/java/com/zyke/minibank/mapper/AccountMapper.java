package com.zyke.minibank.mapper;

import com.zyke.minibank.dto.AccountDto;
import com.zyke.minibank.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    private final CustomerMapper customerMapper;

    public AccountDto toDto(Account account) {

        if (account == null) {

            return null;
        }

        return AccountDto.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .customers(account.getCustomers().stream()
                        .map(customerMapper::toDto).toList())
                .build();
    }
}
