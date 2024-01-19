package com.example.mangostore.service.impl;

import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Role;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.service.RoleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public RoleServiceImpl(AccountRepository accountRepository,
                           RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public String getAllRoleByStatus1(Model model, HttpSession session, int page) {
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

            Page<Role> itemsRole = roleRepository.getAllRoleByStatus1(PageRequest.of(page, 5));
            model.addAttribute("listRole", itemsRole);

            Page<Role> itemsRoleInactive = roleRepository.getAllRoleByStatus0(PageRequest.of(page, 5));
            model.addAttribute("listRoleInactive", itemsRoleInactive);

            model.addAttribute("currentPage", page);

            model.addAttribute("addRole", new Role());

            model.addAttribute("checkMenuAdmin", true);
            return "admin/role/IndexRole";
        }
    }

    @Override
    public String restoreRole(RedirectAttributes redirectAttributes, Long idRole) {
        Role detailRole = roleRepository.findById(idRole).orElse(null);
        if (detailRole != null) {
            detailRole.setStatus(1);
            roleRepository.save(detailRole);
            redirectAttributes.addFlashAttribute("message", "Restore Role Successfully");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-success");
        } else {
            redirectAttributes.addFlashAttribute("message", "Restore Role Fail");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-danger");
        }
        return "redirect:/mangostore/admin/role";
    }

    @Override
    public String detailRole(Model model, HttpSession session, Long idRole, int page) {
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

            Page<Role> itemsRoleInactive = roleRepository.getAllRoleByStatus0(PageRequest.of(page, 4));
            model.addAttribute("listRoleInactive", itemsRoleInactive);
            model.addAttribute("currentPage", page);

            Role detailRole = roleRepository.findById(idRole).orElse(null);
            model.addAttribute("detailRole", detailRole);

            List<Account> itemsAccount = accountRepository.getAllAccountByRole(idRole);
            model.addAttribute("listAccountByRole", itemsAccount);

            model.addAttribute("checkMenuAdmin", true);
            return "admin/role/DetailRole";
        }
    }

    @Override
    public String updateRole(RedirectAttributes redirectAttributes, BindingResult result, Long idRole, Role role) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "Update Role Fail");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-danger");
        } else {
            Role detailRole = roleRepository.findById(role.getId()).orElse(null);
            detailRole.setNote(role.getNote());
            detailRole.setStatus(role.getStatus());

            roleRepository.save(detailRole);
            redirectAttributes.addFlashAttribute("message", "Update Role Successfully");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-success");
        }
        return "redirect:/mangostore/admin/role";
    }

    @Override
    public String deleteRole(RedirectAttributes redirectAttributes, Long idRole) {
        try {
            Role detaiRole = roleRepository.findById(idRole).orElse(null);
            assert detaiRole != null;
            detaiRole.setStatus(0);
            roleRepository.save(detaiRole);
            redirectAttributes.addFlashAttribute("message", "Delete Role Successfully");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Delete Role Fail");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-danger");
        }
        return "redirect:/mangostore/admin/role";
    }

    @Override
    public String addRole(RedirectAttributes redirectAttributes, BindingResult result, Role addRole) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "Add Role Fail");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-danger");
        } else {
            Role newRole = new Role();
            newRole.setName(addRole.getName());
            newRole.setNote(addRole.getNote());
            newRole.setStatus(1);
            roleRepository.save(newRole);
            redirectAttributes.addFlashAttribute("message", "Add Role Successfully");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-success");
        }
        return "redirect:/mangostore/admin/role";
    }
}
