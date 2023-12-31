package com.example.mangostore.service.impl;

import com.example.mangostore.entity.Account;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final AccountRepository accountRepository;

    public ProfileServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public String getAllAccountByStatus1(Model model, HttpSession session, int page) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);

            LocalDateTime checkDate = LocalDateTime.now();
            int hour = checkDate.getHour();
            if (hour >= 5 && hour < 10) {
                model.addAttribute("dates", "Morning");
            } else if (hour >= 10 && hour < 13) {
                model.addAttribute("dates", "Noon");
            } else if (hour >= 13 && hour < 18) {
                model.addAttribute("dates", "Afternoon");
            } else {
                model.addAttribute("dates", "Evening");
            }

            Page<Account> itemsAccount = accountRepository.getAllAccountByStatus1(PageRequest.of(page, 4));
            model.addAttribute("listAccount", itemsAccount);

            Page<Account> itemsAccountInactive = accountRepository.getAllAccountByStatus0(PageRequest.of(page, 4));
            model.addAttribute("listAccountInactive", itemsAccountInactive);

            model.addAttribute("currentPage", page);
            return "admin/account/IndexAccount";
        }
    }

    @Override
    public String restoreAccount(Long idAccount) {
        Account detailAccount = accountRepository.findById(idAccount).orElse(null);
        if (detailAccount != null) {
            detailAccount.setStatus(1);
            accountRepository.save(detailAccount);
        }
        return "redirect:/mangostore/admin/account";
    }
}
