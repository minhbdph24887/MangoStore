package com.example.mangostore.service;

import com.example.mangostore.entity.Authentication;
import com.example.mangostore.entity.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface AuthenticationService {
    String getAllRole(Model model, HttpSession session);

    String detailAuthentication(Model model, HttpSession session, Long idAuthentication);

    String updateAuthentication(Authentication updateAuthentication, Role roleSelect);
}
