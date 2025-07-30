package com.bemojr.bankAccountManagement.dto;

import lombok.Data;

@Data
public class BankAccountDetailsDto {
    private String accountNumber;
    private String accountName;
    private String firstName;
    private String lastName;
    private String otherName;
    private String bankName;
    private String bankCode;
}
