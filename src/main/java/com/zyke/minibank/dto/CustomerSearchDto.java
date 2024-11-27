package com.zyke.minibank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerSearchDto {

    private String name;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String city;
}
