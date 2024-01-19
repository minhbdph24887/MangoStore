package com.example.mangostore.service;

import com.example.mangostore.entity.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface RoleService {
    String getAllRoleByStatus1(Model model, HttpSession session, int page);

    String restoreRole(RedirectAttributes redirectAttributes, Long idRole);

    String detailRole(Model model, HttpSession session, Long idRole, int page);

    String updateRole(RedirectAttributes redirectAttributes, BindingResult result, Long idRole, Role role);

    String deleteRole(RedirectAttributes redirectAttributes, Long idRole);

    String addRole(RedirectAttributes redirectAttributes, BindingResult result, Role addRole);
}
