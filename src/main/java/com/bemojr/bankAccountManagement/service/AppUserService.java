package com.bemojr.bankAccountManagement.service;

import com.bemojr.bankAccountManagement.entity.AppUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AppUserService extends UserDetailsService {
    @Override
    default UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
