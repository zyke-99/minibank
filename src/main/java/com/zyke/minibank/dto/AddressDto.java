package com.zyke.minibank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record AddressDto(
        @JsonProperty("id") Long id,
        @JsonProperty("country") @NotEmpty(message = "Address country cannot be empty") String country,
        @JsonProperty("postcode") String postcode,
        @JsonProperty("region") String region,
        @JsonProperty("town") @NotEmpty(message = "Address town cannot be empty") String town,
        @JsonProperty("streetType") String streetType,
        @JsonProperty("streetName") @NotEmpty(message = "Address street name cannot be empty") String streetName,
        @JsonProperty("streetNumber") @NotEmpty(message = "Address street number cannot be empty") String streetNumber,
        @JsonProperty("floor") String floor) {
}