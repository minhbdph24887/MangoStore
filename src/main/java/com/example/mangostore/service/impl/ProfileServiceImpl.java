package com.example.mangostore.service.impl;

import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Authentication;
import com.example.mangostore.entity.Role;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.AuthenticationRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationRepository authenticationRepository;

    public ProfileServiceImpl(AccountRepository accountRepository,
                              RoleRepository roleRepository,
                              PasswordEncoder encoder,
                              AuthenticationRepository authenticationRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    public String getAllAccountByStatus1(Model model, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            if (detailAccount.getStatus() == 0) {
                session.invalidate();
                return "redirect:/mangostore/home";
            } else {
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

                List<Account> itemsAccount = accountRepository.getAllAccountByStatus1();
                model.addAttribute("listAccount", itemsAccount);

                List<Account> itemsAccountInactive = accountRepository.getAllAccountByStatus0();
                model.addAttribute("listAccountInactive", itemsAccountInactive);


                Role detailRole = roleRepository.getRoleByEmail(email);
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkIndexAccount", true);
                    model.addAttribute("checkMenuAdmin", true);
                    model.addAttribute("addProfile", new Account());
                } else {
                    model.addAttribute("checkIndexAccount", false);
                    model.addAttribute("checkMenuAdmin", false);
                }
                return "admin/account/IndexAccount";
            }
        }
    }

    @Override
    public String restoreAccount(Long idAccount) {
        Account detailAccount = accountRepository.findById(idAccount).orElse(null);
        detailAccount.setStatus(1);
        accountRepository.save(detailAccount);
        return "redirect:/mangostore/admin/account";
    }

    @Override
    public String detailAccount(Model model, HttpSession session, Long idAccount) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            if (detailAccount.getStatus() == 0) {
                session.invalidate();
                return "redirect:/mangostore/home";
            } else {
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

                Role detailRole = roleRepository.getRoleByEmail(email);
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkDetailAccount", true);
                    model.addAttribute("checkMenuAdmin", true);
                } else {
                    model.addAttribute("checkDetailAccount", false);
                    model.addAttribute("checkMenuAdmin", false);
                }
                return "admin/account/DetailAccount";
            }
        }
    }

    @Override
    public String updateAccount(BindingResult result, String newPassword, MultipartFile imageFile, Account account) {
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

    @Override
    public String addAccount(BindingResult result, Account addProfile) {
        Account newAccount = new Account();
        newAccount.setFullName(addProfile.getFullName());
        newAccount.setNumberPhone(addProfile.getNumberPhone());
        newAccount.setEmail(addProfile.getEmail());
        newAccount.setBirthday(addProfile.getBirthday());
        newAccount.setGender(addProfile.getGender());
        newAccount.setImages(addProfile.getImages());
        newAccount.setEncryptionPassword(encoder.encode(addProfile.getPassword()));
        newAccount.setAddress(addProfile.getAddress());
        newAccount.setStatus(1);
        accountRepository.save(newAccount);

        Role roleUser = roleRepository.getAllRoleByUser();
        Authentication newAuthentication = new Authentication();
        newAuthentication.setAccount(newAccount);
        newAuthentication.setRole(roleUser);
        authenticationRepository.save(newAuthentication);
//        int checkEmailExists = accountRepository.existsByEmail(addProfile.getEmail());
//        if (checkEmailExists == 1) {
//            redirectAttributes.addFlashAttribute("message", "Email already exists. Add Account Failed.");
//            redirectAttributes.addFlashAttribute("cssClass", "alert alert-danger");
//        } else {
//
//
//            redirectAttributes.addFlashAttribute("message", "Add Account Successfully");
//            redirectAttributes.addFlashAttribute("cssClass", "alert alert-success");
//        }
        return "redirect:/mangostore/admin/account";
    }
}
