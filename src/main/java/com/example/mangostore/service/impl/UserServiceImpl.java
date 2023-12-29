package com.example.mangostore.service.impl;

import com.example.mangostore.entity.Account;
import com.example.mangostore.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    public UserServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account detailAccount = accountRepository.detailAccountByEmail(username);
        if (detailAccount == null) {
            throw new UsernameNotFoundException(username);
        }
        return detailAccount;
    }
}
