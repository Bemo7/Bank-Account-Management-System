package com.bemojr.bankAccountManagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(path = "/api/v1/user")
public class AppUserController {
    @GetMapping("/me")
    public ResponseEntity<?> getAppUser(
            SecurityContext contextHolder
    ) {
        return new ResponseEntity<>(contextHolder.getAuthentication().getPrincipal(), HttpStatus.FOUND);
    }
}
