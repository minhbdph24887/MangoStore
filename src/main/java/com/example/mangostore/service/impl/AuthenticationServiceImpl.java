package com.example.mangostore.service.impl;

import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Authentication;
import com.example.mangostore.entity.Role;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.AuthenticationRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.service.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationRepository authenticationRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public AuthenticationServiceImpl(AuthenticationRepository authenticationRepository,
                                     AccountRepository accountRepository,
                                     RoleRepository roleRepository) {
        this.authenticationRepository = authenticationRepository;
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public String getAllRole(Model model, HttpSession session, int page) {
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

            Page<Authentication> itemsAuthentication = authenticationRepository.findAll(PageRequest.of(page, 4));
            model.addAttribute("listAuthentication", itemsAuthentication);

            model.addAttribute("currentPage", page);

            model.addAttribute("checkMenuAdmin", true);
            return "admin/authentication/IndexAuthentication";
        }
    }

    @Override
    public String detailAuthentication(Model model, HttpSession session, Long idAuthentication, int page) {
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

            Authentication detailAuthentication = authenticationRepository.findById(idAuthentication).orElse(null);
            model.addAttribute("detailAuthentication", detailAuthentication);

            List<Role> itemsRole = roleRepository.getAllRole();
            model.addAttribute("listRole", itemsRole);

            model.addAttribute("checkMenuAdmin", true);
            return "admin/authentication/DetailAuthentication";
        }
    }

    @Override
    public String updateAuthentication(Authentication updateAuthentication, Role roleSelect, RedirectAttributes redirectAttributes) {
        Authentication update = authenticationRepository.findById(updateAuthentication.getId()).orElse(null);
        update.setRole(roleSelect);
        authenticationRepository.save(update);
        return "redirect:/mangostore/admin/authentication";
    }
}
