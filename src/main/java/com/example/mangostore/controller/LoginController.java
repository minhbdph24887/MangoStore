package com.example.mangostore.controller;

import com.example.mangostore.entity.Account;
import com.example.mangostore.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping(value = "/mangostore/login/")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping(value = "from")
    public String loginFrom() {
        return "login/Form";
    }

    @PostMapping(value = "success")
    public String loginSuccess(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               HttpSession session) throws IOException {
        return loginService.loginAccount(email, password, session);
    }

    @GetMapping(value = "forgot")
    public String viewForgot() {
        return "login/ForgotPassword";
    }

    @PostMapping(value = "code")
    public String veryCodeEmail(@RequestParam("forgotEmail") String email,
                                HttpSession session) {
        session.setAttribute("loginEmail", email);
        return loginService.forgotEmail(email);
    }

    @PostMapping(value = "forgot/password")
    public String authenticationPassword(@RequestParam("codeForgot") String codeForgot,
                                         HttpSession session) {
        return loginService.authenticationCode(codeForgot, session);
    }

    @GetMapping(value = "password/refresh")
    public String refreshPassword() {
        return "login/NewPassword";
    }

    @PostMapping(value = "refresh/success")
    public String refreshPasswordSuccess(@RequestParam("passwordRefresh") String passwordRefresh,
                                         @RequestParam("passwordRefreshRE") String passwordRefreshRE,
                                         HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (!Objects.equals(passwordRefresh, passwordRefreshRE)) {
            return "login/NewPassword";
        } else {
            return loginService.refreshPassword(email, passwordRefresh);
        }
    }

    @GetMapping(value = "signup")
    public String signUpAccount() {
        return "login/SignUp";
    }

    @PostMapping(value = "signup/success")
    public String signUpAccountSuccess(@RequestParam("fullName") String fullName,
                                       @RequestParam("email") String email,
                                       @RequestParam("passwordRefresh") String passwordRefresh,
                                       @RequestParam("passwordRefreshRE") String passwordRefreshRE) {
        if (!Objects.equals(passwordRefresh, passwordRefreshRE)) {
            return "login/SignUp";
        }else{
            return loginService.signUpAccount(fullName, email, passwordRefresh);
        }
    }
}
