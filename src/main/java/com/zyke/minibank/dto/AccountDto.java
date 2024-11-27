package com.zyke.minibank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record AccountDto(
        @JsonProperty("id") Long id,
        @JsonProperty("balance") BigDecimal balance,
        @JsonProperty("customers") @NotEmpty List<CustomerDto> customers) {
}
