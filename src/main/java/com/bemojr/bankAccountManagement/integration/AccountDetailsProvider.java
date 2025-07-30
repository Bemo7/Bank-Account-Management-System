package com.bemojr.bankAccountManagement.integration;

import com.bemojr.bankAccountManagement.dto.AccountCodeAndNumberDto;
import com.bemojr.bankAccountManagement.dto.BankAccountDetailsDto;

public interface AccountDetailsProvider {
    BankAccountDetailsDto call(AccountCodeAndNumberDto userQueryRequestDto);
}
