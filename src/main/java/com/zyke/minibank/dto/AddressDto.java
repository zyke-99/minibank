package com.zyke.minibank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record AddressDto(
        @JsonProperty("id") Long id,
        @JsonProperty("country") @NotEmpty(message = "Address country cannot be empty") String country,
        @JsonProperty("city") @NotEmpty(message = "Address city cannot be empty") String city) {
}
