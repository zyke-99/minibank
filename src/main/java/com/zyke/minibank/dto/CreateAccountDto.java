package com.zyke.minibank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateAccountDto(@JsonProperty @NotEmpty List<Long> customerIds) {
}
