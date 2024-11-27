package com.zyke.minibank.mapper;

import com.zyke.minibank.dto.AddressDto;
import com.zyke.minibank.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDto toDto(Address address) {

        if (address == null) {

            return null;
        }

        return AddressDto.builder()
                .id(address.getId())
                .country(address.getCountry())
                .city(address.getCity())
                .build();
    }
}
