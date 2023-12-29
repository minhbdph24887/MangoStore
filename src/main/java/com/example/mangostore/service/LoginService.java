package com.example.mangostore.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface LoginService {
    String loginAccount(String email, String password, HttpSession session) throws IOException;

    void checkLoginGoogleAccount(HttpServletResponse response, Authentication authentication) throws IOException;

    String forgotEmail(String email);

    String authenticationCode(String codeForgot, HttpSession session);
}
