package com.bemojr.bankAccountManagement.configuration;

import com.bemojr.bankAccountManagement.service.AppUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity
    ) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable
                ))
                .authorizeHttpRequests(
                        (requestMatcherRegistry)-> {
                            requestMatcherRegistry.requestMatchers(
                                    "/api/v*/bank-account/**",
                                            "/api/v*/user/**",
                                            "/swagger-ui/**",
                                            "/swagger-ui.html",
                                            "/v*/api-docs/**",
                                            "/h2-console/**")
                                    .permitAll()
                                    .anyRequest()
                                    .authenticated();
                        }
                )
//                .formLogin()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity httpSecurity,
            AppUserService appUserService,
            PasswordEncoder bCryptPasswordEncoder
    ) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(appUserService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }
}
