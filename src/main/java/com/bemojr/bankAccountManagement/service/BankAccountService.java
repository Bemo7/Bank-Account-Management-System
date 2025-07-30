package com.bemojr.bankAccountManagement.service;

import com.bemojr.bankAccountManagement.dto.AccountCodeAndNumberDto;
import com.bemojr.bankAccountManagement.dto.BankAccountDetailsDto;
import com.bemojr.bankAccountManagement.dto.CreateAccountRequestDto;
import com.bemojr.bankAccountManagement.entity.BankAccountDetails;

public interface BankAccountService {
    BankAccountDetails createBankAccount(CreateAccountRequestDto requestDto);
    boolean checkAccountExist(AccountCodeAndNumberDto requestDto);
    BankAccountDetailsDto getAccountDetails(AccountCodeAndNumberDto requestDto);
}
