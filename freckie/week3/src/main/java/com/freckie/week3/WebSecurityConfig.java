package com.freckie.week3;

import com.freckie.week3.service.logic.UserServiceLogic;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserServiceLogic userService;

    @Override
    protected void configure(HttpSecurity sec) throws Exception {
        sec.authorizeRequests()
                .antMatchers("/v3/api-docs", "/configuration/ui",
                        "/swagger-ui/**", "/configuration/security",
                        "/swagger-resources/**", "/webjars/**", "/swagger/**").permitAll()
                .antMatchers("/api/v1/signin", "/api/v1/signout", "/api/v1/signup").permitAll()
                .anyRequest().permitAll();
    }

    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
