package com.example.mangostore.controller;

import com.example.mangostore.service.CheckVnPayPaymentStatusService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckVnPayPaymentStatus {
    private final CheckVnPayPaymentStatusService service;

    public CheckVnPayPaymentStatus(CheckVnPayPaymentStatusService service) {
        this.service = service;
    }

    @GetMapping(value = "/mangostore/banking/success")
    public String bankingSuccess(HttpServletRequest request,
                                 HttpSession session) {
        return service.bankingSuccess(request, session);
    }
}
