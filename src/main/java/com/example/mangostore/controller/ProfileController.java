package com.example.mangostore.controller;

import com.example.mangostore.entity.Account;
import com.example.mangostore.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Controller
@RequestMapping(value = "/mangostore/admin/")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping(value = "account")
    public String viewAccount(Model model,
                              HttpSession session,
                              @RequestParam(defaultValue = "0") int page) {
        return profileService.getAllAccountByStatus1(model, session, page);
    }

    @GetMapping(value = "account/restore/{id}")
    public String restoreAccount(@PathVariable("id") Long idAccount) {
        return profileService.restoreAccount(idAccount);
    }

    @GetMapping(value = "account/detail/{id}")
    public String detailAccount(Model model,
                                HttpSession session,
                                @PathVariable("id") Long idAccount) {
        return profileService.detailAccount(model, session, idAccount);
    }

    @PostMapping(value = "account/update")
    public String updateAccount(@RequestParam("newPassword") String newPassword,
                                @RequestParam("rePassword") String rePassword,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                @RequestParam("id") Long idAccount,
                                Account account) {
        if (!Objects.equals(newPassword, rePassword)) {
            return "redirect:/mangostore/admin/account/detail/" + idAccount;
        } else {
            return profileService.updateAccount(newPassword, imageFile, account);
        }
    }

    @GetMapping(value = "account/delete/{id}")
    public String deleteAccount(@PathVariable("id") Long idAccount) {
        return profileService.deleteAccount(idAccount);
    }

    @PostMapping(value = "account/add")
    public String addAccount(Account addProfile) {
        return profileService.addAccount(addProfile);
    }
}
