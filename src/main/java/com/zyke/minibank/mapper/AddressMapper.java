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
                .postcode(address.getPostcode())
                .region(address.getRegion())
                .town(address.getTown())
                .streetType(address.getStreetType())
                .streetName(address.getStreetName())
                .streetNumber(address.getStreetNumber())
                .floor(address.getFloor())
                .build();
    }

    public Address fromAddressDto(AddressDto addressDto) {

        if (addressDto == null) {

            return null;
        }

        return Address.builder()
                .id(addressDto.id())
                .country(addressDto.country())
                .postcode(addressDto.postcode())
                .region(addressDto.region())
                .town(addressDto.town())
                .streetType(addressDto.streetType())
                .streetName(addressDto.streetName())
                .streetNumber(addressDto.streetNumber())
                .floor(addressDto.floor())
                .build();
    }
}
