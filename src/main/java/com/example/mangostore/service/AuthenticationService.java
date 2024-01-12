package com.example.mangostore.service;

import com.example.mangostore.entity.Authentication;
import com.example.mangostore.entity.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface AuthenticationService {
    String getAllRole(Model model, HttpSession session, int page);

    String detailAuthentication(Model model, HttpSession session, Long idAuthentication, int page);

    String updateAuthentication(Authentication updateAuthentication, Role roleSelect, RedirectAttributes redirectAttributes);
}
