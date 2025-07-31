package com.bemojr.bankAccountManagement.controller;

import com.bemojr.bankAccountManagement.dto.AccountCodeAndNumberDto;
import com.bemojr.bankAccountManagement.dto.BankAccountDetailsDto;
import com.bemojr.bankAccountManagement.dto.GenericResponseDto;
import com.bemojr.bankAccountManagement.dto.CreateAccountRequestDto;
import com.bemojr.bankAccountManagement.service.BankAccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/bank-account")
@Slf4j
public class BankAccountController {
    public final BankAccountService bankAccountService;

    @PostMapping()
    public ResponseEntity<?> createAccountNumber(
            @Valid @RequestBody CreateAccountRequestDto request
    ) {
        log.info("User request {}", request);
        return new ResponseEntity<>(bankAccountService.createBankAccount(request), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<?> getAccountDetails(@RequestParam("accountNumber") String accountNumber, @Nullable @RequestParam("bankCode") String bankCode){
        log.info("Account Number {}", accountNumber);
        BankAccountDetailsDto bankAccountDetailsDto = bankAccountService.getAccountDetails(new AccountCodeAndNumberDto(accountNumber, bankCode));
        return new ResponseEntity<>(bankAccountDetailsDto, HttpStatus.OK);
    }

    @GetMapping("verify")
    public ResponseEntity<?> checkAccountExist(@RequestParam("accountNumber") String accountNumber, @Nullable @RequestParam("bankCode") String bankCode) {
        bankAccountService.checkAccountExist(new AccountCodeAndNumberDto(accountNumber, bankCode));
        return new ResponseEntity<>(new GenericResponseDto("Account number exists"), HttpStatus.OK);
    }
}
