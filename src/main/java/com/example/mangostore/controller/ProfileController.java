package com.example.mangostore.controller;

import com.example.mangostore.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
