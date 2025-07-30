package com.bemojr.bankAccountManagement.dto;

public record GenericResponseDto(
        String status,
        String message
) {

    public GenericResponseDto(String message) {
        this("found",message);
    }
}
