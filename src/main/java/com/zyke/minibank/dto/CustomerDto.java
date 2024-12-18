package com.zyke.minibank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zyke.minibank.entity.CustomerStatus;
import com.zyke.minibank.entity.CustomerType;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Builder
public record CustomerDto(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("lastName") String lastName,
        @JsonProperty("phoneNumber") String phoneNumber,
        @JsonProperty("email") String email,
        @JsonProperty("type") CustomerType type,
        @JsonProperty("nationalId") String nationalId,
        @JsonProperty("dateOfBirth") LocalDate dateOfBirth,
        @JsonProperty("status") CustomerStatus status,
        @JsonProperty("addresses") List<AddressDto> addresses,
        @JsonProperty("accounts") List<CustomerAccountDto> accounts,
        @JsonProperty("createdBy") String createdBy,
        @JsonProperty("creationDate") Instant creationDate,
        @JsonProperty("lastModifiedBy") String lastModifiedBy,
        @JsonProperty("lastModifiedDate") Instant lastModifiedDate) {
}
