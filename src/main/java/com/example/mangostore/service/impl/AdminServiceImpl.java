package com.example.mangostore.service.impl;

import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Role;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;

@Service
public class AdminServiceImpl implements AdminService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public AdminServiceImpl(AccountRepository accountRepository,
                            RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public String indexAdmin(Model model, HttpSession session) {
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

            Role detailRole = roleRepository.getRoleByEmail(email);
            if (detailRole.getName().equals("ADMIN")) {
                model.addAttribute("checkMenuAdmin", true);
            } else {
                model.addAttribute("checkMenuAdmin", false);
            }
            return "admin/Index";
        }
    }
}
