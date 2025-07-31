package com.bemojr.bankAccountManagement.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class BankAccountDetails {
    @Id
    @SequenceGenerator(
            name = "bank_account_details_sequence",
            sequenceName = "bank_account_details_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "bank_account_details_sequence"
    )
    @Column(
            updatable = false
    )
    private Long id;

    @Column(
            nullable = false
    )
    private String accountNumber;

    @Column(
            nullable = false
    )
    private String accountName;

    @Column(
            nullable = false
    )
    private String firstName;

    @Column(
            nullable = false
    )
    private String lastName;

    @Column(
            nullable = false
    )
    private String otherName;

    @Column(
            nullable = false
    )
    private String bankName;

    @Column(
            nullable = false
    )
    private String bankCode;

    @Column(
            nullable = false
    )
    private LocalDateTime createdAt;
}
