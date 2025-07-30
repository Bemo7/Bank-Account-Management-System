package com.bemojr.bankAccountManagement.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequestDto(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotBlank
        String otherName
) { }
