package com.zyke.minibank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zyke.minibank.entity.CustomerType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record CreateCustomerDto(
        @JsonProperty("name") @NotEmpty(message = "Customer name cannot be empty") String name,
        @JsonProperty("lastName") @NotEmpty(message = "Customer last name cannot be empty") String lastName,
        @JsonProperty("phoneNumber") @NotEmpty(message = "Customer phone number cannot be empty") String phoneNumber,
        @JsonProperty("email") @NotEmpty(message = "Customer email cannot be empty") String email,
        @JsonProperty("type") @NotNull(message = "Customer type must be provided") CustomerType type,
        @JsonProperty("nationalId") @NotNull(message = "Customer national ID must be provided") String nationalId,
        @JsonProperty("dateOfBirth") @NotNull(message = "Customer date of birth must be provided") LocalDate dateOfBirth,
        @JsonProperty("addresses") @Valid List<AddressDto> addresses
) {
}
