package com.zyke.minibank.service;

import com.zyke.minibank.dto.AccountDto;
import com.zyke.minibank.dto.CreateAccountDto;

public interface AccountService {

    AccountDto createAccount(CreateAccountDto createAccountDto);
}
