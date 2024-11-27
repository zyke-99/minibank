package com.zyke.minibank.service;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    // mock implementation of fetching current user mainly for auditing as no auth requirements for now
    public Optional<String> getCurrentUser() {

        return Optional.of("minibank_user");
    }
}
