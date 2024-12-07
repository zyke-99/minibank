package com.zyke.minibank.controller;

import com.zyke.minibank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

//    @PostMapping
//    public ResponseEntity<AccountDto> createAccount(@RequestBody @Valid CreateAccountDto account) {
//
//        return new ResponseEntity<>(accountService.create(account), CREATED);
//    }
}
