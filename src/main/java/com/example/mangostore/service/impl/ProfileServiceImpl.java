package com.example.mangostore.service.impl;

import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Role;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    public ProfileServiceImpl(AccountRepository accountRepository,
                              RoleRepository roleRepository,
                              PasswordEncoder encoder) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
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

    @Override
    public String detailAccount(Model model, HttpSession session, Long idAccount) {
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

            Account detailProfile = accountRepository.findById(idAccount).orElse(null);
            model.addAttribute("detailProfile", detailProfile);

            String accountRole = roleRepository.getRoleByEmail(detailProfile.getEmail()).getName();
            model.addAttribute("accountRole", accountRole);
            return "admin/account/DetailAccount";
        }
    }

    @Override
    public String updateAccount(String newPassword, MultipartFile imageFile, Account account) {
        Account detailAccount = accountRepository.findById(account.getId()).orElse(null);
        detailAccount.setFullName(account.getFullName());
        detailAccount.setNumberPhone(account.getNumberPhone());
        detailAccount.setEmail(account.getEmail());
        detailAccount.setBirthday(account.getBirthday());
        detailAccount.setGender(account.getGender());

        String oldImagePath = accountRepository.findById(account.getId()).get().getImages();
        if (imageFile.isEmpty()) {
            detailAccount.setImages(oldImagePath);
        } else {
            String fileName = imageFile.getOriginalFilename();
            detailAccount.setImages(fileName);
        }

        detailAccount.setEncryptionPassword(newPassword == null ? detailAccount.getEncryptionPassword() : encoder.encode(newPassword));
        detailAccount.setAddress(account.getAddress());
        detailAccount.setStatus(account.getStatus());
        accountRepository.save(detailAccount);
        return "redirect:/mangostore/admin/account";
    }

    @Override
    public String deleteAccount(Long idAccount) {
        Account detailAccount = accountRepository.findById(idAccount).orElse(null);
        assert detailAccount != null;
        detailAccount.setStatus(0);
        accountRepository.save(detailAccount);
        return "redirect:/mangostore/admin/account";
    }
}
