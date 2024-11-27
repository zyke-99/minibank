package com.zyke.minibank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zyke.minibank.entity.CustomerType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateCustomerDto(
        @JsonProperty("id") Long id,
        @JsonProperty("name") @NotEmpty(message = "Customer name cannot be empty") String name,
        @JsonProperty("lastName") @NotEmpty(message = "Customer last name cannot be empty") String lastName,
        @JsonProperty("phoneNumber") @NotEmpty(message = "Customer phone number cannot be empty") String phoneNumber,
        @JsonProperty("email") @NotEmpty(message = "Customer email cannot be empty") String email,
        @JsonProperty("type") @NotNull(message = "Customer type must be provided") CustomerType type,
        @JsonProperty("addresses") @Valid List<AddressDto> addresses
) {
}
