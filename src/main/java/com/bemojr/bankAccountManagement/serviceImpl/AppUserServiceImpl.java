package com.bemojr.bankAccountManagement.serviceImpl;

import com.bemojr.bankAccountManagement.repository.AppUserRepository;
import com.bemojr.bankAccountManagement.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username)
                .orElseThrow(()-> new IllegalStateException("No user with username [" + username + "] found"));
    }
}
