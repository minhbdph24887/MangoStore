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
}
