package com.bemojr.bankAccountManagement.serviceImpl;

import com.bemojr.bankAccountManagement.dto.AccountCodeAndNumberDto;
import com.bemojr.bankAccountManagement.dto.BankAccountDetailsDto;
import com.bemojr.bankAccountManagement.dto.CreateAccountRequestDto;
import com.bemojr.bankAccountManagement.entity.BankAccountDetails;
import com.bemojr.bankAccountManagement.exception.ResourceNotFoundException;
import com.bemojr.bankAccountManagement.integration.AccountDetailsProvider;
import com.bemojr.bankAccountManagement.repository.BankAccountDetailsRepository;
import com.bemojr.bankAccountManagement.service.BankAccountService;
import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    @Value("${vendor-bank-name}")
    String vendorBankName;

    @Value("${vendor-bank-code}")
    String vendorBankCode;

    @Autowired
    public AccountDetailsProvider accountDetailsProvider;

    @Autowired
    public BankAccountDetailsRepository bankAccountDetailsRepository;

    private static final int[] WEIGHTS = {3, 7, 3, 3, 7, 3, 3, 7, 3, 3, 7, 3};

    @Override
    @Transactional
    public BankAccountDetails createBankAccount(CreateAccountRequestDto requestDto) {
        String newAccountNumber;
        int maxRetries = 10; // prevent infinite loops
        int attempts = 0;

        do {
            newAccountNumber = generateNubanAccountNumber();
            log.info("Generated Account Number -> {}", newAccountNumber);
            attempts++;
        } while (checkAccountExist(new AccountCodeAndNumberDto(newAccountNumber, vendorBankCode)) && attempts < maxRetries);

        if (attempts == maxRetries) {
            throw new RuntimeException("Failed to generate a unique account number");
        }

        boolean accountExist = checkAccountExist(
                new AccountCodeAndNumberDto(newAccountNumber, vendorBankCode)
        );

        if (accountExist) {
            throw new EntityExistsException("User account number");
        }

        BankAccountDetails bankAccountDetails = new BankAccountDetails();
        bankAccountDetails.setAccountNumber(newAccountNumber);
        bankAccountDetails.setFirstName(requestDto.firstName());
        bankAccountDetails.setLastName(requestDto.lastName());
        bankAccountDetails.setOtherName(requestDto.otherName());
        bankAccountDetails.setAccountName(
                requestDto.firstName() + " " + requestDto.otherName() + " " + requestDto.lastName()
        );
        bankAccountDetails.setBankCode(vendorBankCode);
        bankAccountDetails.setBankName(vendorBankName);
        bankAccountDetails.setCreatedAt(LocalDateTime.now());

        return bankAccountDetailsRepository.save(bankAccountDetails);
    }

    @Override
    public boolean checkAccountExist(AccountCodeAndNumberDto requestDto){
        if (requestDto.bankCode() != null) {
            BankAccountDetailsDto providerResponse= checkFromProvider(requestDto);

            if (providerResponse != null) {
                return true;
            } else if (!requestDto.bankCode().equals(vendorBankCode)) {
                return false;
            }
        }

        Optional<BankAccountDetails> bankAccountDetails = bankAccountDetailsRepository.findByAccountNumber(
                requestDto.accountNumber()
        );

        return bankAccountDetails.isPresent();
    }

    @Override
    public BankAccountDetailsDto getAccountDetails(AccountCodeAndNumberDto requestDto) {
        if (requestDto.bankCode() != null) {
            BankAccountDetailsDto providerResponse= checkFromProvider(requestDto);

            if (providerResponse != null) {
                return providerResponse;
            } else if (!requestDto.bankCode().equals(vendorBankCode)){
                throw new ResourceNotFoundException("Account does not exist with account number { " + requestDto.accountNumber() + " } and bank code { " + requestDto.bankCode() +" }");
            }
        }

        BankAccountDetails bankAccountDetails = bankAccountDetailsRepository.findByAccountNumber(
                requestDto.accountNumber()
        ).orElseThrow(
                ()-> new ResourceNotFoundException("Account does not exist with account number { " + requestDto.accountNumber() + " }")
        );

        return this.transform(bankAccountDetails);
    }

    private BankAccountDetailsDto checkFromProvider(AccountCodeAndNumberDto requestDto) {
        BankAccountDetailsDto apiResponse = null;

        try {
            apiResponse = accountDetailsProvider.call(
                    new AccountCodeAndNumberDto(
                            requestDto.accountNumber(),
                            requestDto.bankCode() != null ? requestDto.bankCode() : vendorBankCode
                    )
            );
        } catch (ResourceNotFoundException e) {
            log.info(e.getMessage());
        }

        log.info("Response from third party API -> {}", apiResponse);

        return apiResponse;
    }

    private String generateNubanAccountNumber() {
        // Step 1: Generate random 9-digit base number with leading zeros
        int baseNum = new Random().nextInt(1_000_000_000); // from 0 to 999,999,999
        String baseAccountNumber = String.format("%09d", baseNum);

        // Step 2: Calculate checksum
        int checksum = calculateChecksum(vendorBankCode, baseAccountNumber);

        // Step 3: Return full 10-digit account number
        return baseAccountNumber + checksum;
    }

    private int calculateChecksum(String bankCode, String baseAccountNumber) {
        String combined = bankCode + baseAccountNumber; // 3 + 9 = 12 digits
        int sum = 0;

        for (int i = 0; i < WEIGHTS.length; i++) {
            int digit = Character.getNumericValue(combined.charAt(i));
            sum += digit * WEIGHTS[i];
        }

        int remainder = sum % 10;
        return (10 - remainder) % 10;
    }

    public BankAccountDetailsDto transform(BankAccountDetails bankAccountDetails) {
        BankAccountDetailsDto bankAccountDetailsDto = new BankAccountDetailsDto();
        bankAccountDetailsDto.setBankName(bankAccountDetails.getBankName());
        bankAccountDetailsDto.setBankCode(bankAccountDetails.getBankCode());
        bankAccountDetailsDto.setAccountName(bankAccountDetails.getAccountName());
        bankAccountDetailsDto.setAccountNumber(bankAccountDetails.getAccountNumber());
        bankAccountDetailsDto.setFirstName(bankAccountDetails.getFirstName());
        bankAccountDetailsDto.setLastName(bankAccountDetails.getLastName());
        bankAccountDetailsDto.setOtherName(bankAccountDetails.getOtherName());
        bankAccountDetailsDto.setCreatedAt(bankAccountDetails.getCreatedAt());

        return bankAccountDetailsDto;
    }
}
