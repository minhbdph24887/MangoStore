package com.example.mangostore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/mangostore/")
public class ClientController {

    @GetMapping(value = "home")
    public String indexClient(HttpSession session) {
        String detailEmail = (String) session.getAttribute("loginEmail");
        System.out.println("111111111111 " + detailEmail);
        return "client/List";
    }
}
