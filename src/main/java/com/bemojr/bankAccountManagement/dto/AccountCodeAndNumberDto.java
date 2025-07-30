package com.bemojr.bankAccountManagement.dto;

import jakarta.validation.constraints.Pattern;

public record AccountCodeAndNumberDto(
        @Pattern(regexp = "\\d{10}", message = "Must be exactly 10 digits")
        String accountNumber,

        @Pattern(regexp = "\\d{6}", message = "Must be exactly 6 digits")
        String bankCode
) { }
