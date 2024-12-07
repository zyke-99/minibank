package com.zyke.minibank.mapper;

import com.zyke.minibank.dto.AddressDto;
import com.zyke.minibank.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDto toAddressDto(Address address) {

        if (address == null) {

            return null;
        }

        return AddressDto.builder()
                .id(address.getId())
                .country(address.getCountry())
                .city(address.getCity())
                .build();
    }

    public Address fromAddressDto(AddressDto addressDto) {

        return Address.builder()
                .id(addressDto.id())
                .country(addressDto.country())
                .city(addressDto.city())
                .build();
    }
}
