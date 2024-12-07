package com.zyke.minibank.service;

import com.zyke.minibank.entity.Customer;

public interface CustomerService {

    Customer create(Customer customer);

    Customer update(Long id, Customer customer);
}
