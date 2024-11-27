package com.zyke.minibank.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponse(@JsonProperty String message) {
}
