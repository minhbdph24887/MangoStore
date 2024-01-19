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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                              HttpSession session,
                              @RequestParam(defaultValue = "0") int page) {
        return profileService.getAllAccountByStatus1(model, session, page);
    }

    @GetMapping(value = "account/restore/{id}")
    public String restoreAccount(RedirectAttributes redirectAttributes,
                                 @PathVariable("id") Long idAccount) {
        return profileService.restoreAccount(redirectAttributes, idAccount);
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
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (!Objects.equals(newPassword, rePassword)) {
            return "redirect:/mangostore/admin/account/detail/" + idAccount;
        } else {
            return profileService.updateAccount(redirectAttributes, result, newPassword, imageFile, account);
        }
    }

    @GetMapping(value = "account/delete/{id}")
    public String deleteAccount(@PathVariable("id") Long idAccount,
                                RedirectAttributes redirectAttributes) {
        return profileService.deleteAccount(redirectAttributes, idAccount);
    }

    @PostMapping(value = "account/add")
    public String addAccount(@Valid Account addProfile,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        return profileService.addAccount(redirectAttributes, result, addProfile);
    }

    @GetMapping(value = "role")
    public String viewRole(Model model,
                           HttpSession session,
                           @RequestParam(defaultValue = "0") int page) {
        return roleService.getAllRoleByStatus1(model, session, page);
    }

    @GetMapping(value = "role/restore/{id}")
    public String restoreRole(RedirectAttributes redirectAttributes,
                              @PathVariable("id") Long idRole) {
        return roleService.restoreRole(redirectAttributes, idRole);
    }

    @GetMapping(value = "role/detail/{id}")
    public String detailRole(Model model,
                             HttpSession session,
                             @PathVariable("id") Long idRole,
                             @RequestParam(defaultValue = "0") int page) {
        return roleService.detailRole(model, session, idRole, page);
    }

    @PostMapping(value = "role/update")
    public String updateRole(@Valid Role role,
                             @RequestParam("id") Long idRole,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        return roleService.updateRole(redirectAttributes, result, idRole, role);
    }

    @GetMapping(value = "role/delete/{id}")
    public String deleteRole(@PathVariable("id") Long idRole,
                             RedirectAttributes redirectAttributes) {
        return roleService.deleteRole(redirectAttributes, idRole);
    }

    @PostMapping(value = "role/add")
    public String addRole(@Valid Role addRole,
                          BindingResult result,
                          RedirectAttributes redirectAttributes) {
        return roleService.addRole(redirectAttributes, result, addRole);
    }

    @GetMapping(value = "authentication")
    public String viewAuthentication(Model model,
                                     HttpSession session,
                                     @RequestParam(defaultValue = "0") int page) {
        return authenticationService.getAllRole(model, session, page);
    }

    @GetMapping(value = "authentication/detail/{id}")
    public String detailAuthentication(Model model,
                                       HttpSession session,
                                       @PathVariable("id") Long idAuthentication,
                                       @RequestParam(defaultValue = "0") int page) {
        return authenticationService.detailAuthentication(model, session, idAuthentication, page);
    }

    @PostMapping(value = "authentication/update")
    public String updateAuthentication(Authentication updateAuthentication,
                                       @RequestParam("role") Role roleSelect,
                                       RedirectAttributes redirectAttributes) {
        return authenticationService.updateAuthentication(updateAuthentication, roleSelect, redirectAttributes);
    }
}
