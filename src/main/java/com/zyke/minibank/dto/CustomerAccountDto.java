package com.zyke.minibank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CustomerAccountDto(
        @JsonProperty("id") Long id,
        @JsonProperty("balance") BigDecimal balance) {
}
