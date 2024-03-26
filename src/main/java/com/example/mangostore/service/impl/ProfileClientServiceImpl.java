package com.example.mangostore.service.impl;

import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Role;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.service.ProfileClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class ProfileClientServiceImpl implements ProfileClientService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public ProfileClientServiceImpl(AccountRepository accountRepository,
                                    RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public String indexProfile(Model model,
                               HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);
            Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
            if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                model.addAttribute("checkAuthentication", detailRoleByEmail);
            }
        }
        return "client/profile/detailView/ProfileClient";
    }

    @Override
    public String updateProfile(Account profile,
                                MultipartFile imageFile,
                                HttpSession session) {

        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            Account updateAccount = accountRepository.findById(detailAccount.getId()).orElse(null);
            assert updateAccount != null;
            updateAccount.setFullName(profile.getFullName());
            updateAccount.setNumberPhone(profile.getNumberPhone());
            updateAccount.setEmail(profile.getEmail());
            updateAccount.setGender(profile.getGender());
            updateAccount.setBirthday(profile.getBirthday());

            String oldImagePath = accountRepository.findById(detailAccount.getId()).get().getImages();
            if (imageFile.isEmpty()) {
                updateAccount.setImages(oldImagePath);
            } else {
                String fileName = imageFile.getOriginalFilename();
                updateAccount.setImages(fileName);
            }
            accountRepository.save(updateAccount);
        }
        return "redirect:/mangostore/profile";
    }
}
