package com.zyke.minibank.service;

import com.zyke.minibank.entity.Account;
import com.zyke.minibank.entity.Customer;

public interface AccountService {

    Account createForCustomer(Customer customer);
}
