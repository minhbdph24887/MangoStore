package com.example.mangostore.controller;

import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Authentication;
import com.example.mangostore.entity.Role;
import com.example.mangostore.service.AuthenticationService;
import com.example.mangostore.service.ProfileService;
import com.example.mangostore.service.RoleService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Controller
@RequestMapping(value = "/mangostore/admin/")
public class ProfileController {
    private final ProfileService profileService;
    private final RoleService roleService;
    private final AuthenticationService authenticationService;

    public ProfileController(ProfileService profileService,
                             RoleService roleService,
                             AuthenticationService authenticationService) {
        this.profileService = profileService;
        this.roleService = roleService;
        this.authenticationService = authenticationService;
    }

    @GetMapping(value = "account")
    public String viewAccount(Model model,
                              HttpSession session) {
        return profileService.getAllAccountByStatus1(model, session);
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
    public String updateAccount(@Valid Account account,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("rePassword") String rePassword,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                @RequestParam("id") Long idAccount,
                                BindingResult result) {
        if (!Objects.equals(newPassword, rePassword)) {
            return "redirect:/mangostore/admin/account/detail/" + idAccount;
        } else {
            return profileService.updateAccount(result, newPassword, imageFile, account);
        }
    }

    @GetMapping(value = "account/delete/{id}")
    public String deleteAccount(@PathVariable("id") Long idAccount) {
        return profileService.deleteAccount(idAccount);
    }

    @PostMapping(value = "account/add")
    public String addAccount(@Valid Account addProfile,
                             BindingResult result) {
        return profileService.addAccount(result, addProfile);
    }

    @GetMapping(value = "role")
    public String viewRole(Model model,
                           HttpSession session) {
        return roleService.getAllRoleByStatus1(model, session);
    }

    @GetMapping(value = "role/restore/{id}")
    public String restoreRole(@PathVariable("id") Long idRole) {
        return roleService.restoreRole(idRole);
    }

    @GetMapping(value = "role/detail/{id}")
    public String detailRole(Model model,
                             HttpSession session,
                             @PathVariable("id") Long idRole) {
        return roleService.detailRole(model, session, idRole);
    }

    @PostMapping(value = "role/update")
    public String updateRole(@Valid Role role,
                             @RequestParam("id") Long idRole,
                             BindingResult result) {
        return roleService.updateRole(result, idRole, role);
    }

    @GetMapping(value = "role/delete/{id}")
    public String deleteRole(@PathVariable("id") Long idRole) {
        return roleService.deleteRole(idRole);
    }

    @PostMapping(value = "role/add")
    public String addRole(@Valid Role addRole,
                          BindingResult result) {
        return roleService.addRole(result, addRole);
    }

    @GetMapping(value = "authentication")
    public String viewAuthentication(Model model,
                                     HttpSession session) {
        return authenticationService.getAllRole(model, session);
    }

    @GetMapping(value = "authentication/detail/{id}")
    public String detailAuthentication(Model model,
                                       HttpSession session,
                                       @PathVariable("id") Long idAuthentication) {
        return authenticationService.detailAuthentication(model, session, idAuthentication);
    }

    @PostMapping(value = "authentication/update")
    public String updateAuthentication(Authentication updateAuthentication,
                                       @RequestParam("role") Role roleSelect) {
        return authenticationService.updateAuthentication(updateAuthentication, roleSelect);
    }
}
