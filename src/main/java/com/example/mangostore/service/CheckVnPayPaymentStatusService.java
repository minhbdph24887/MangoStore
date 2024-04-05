package com.example.mangostore.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public interface CheckVnPayPaymentStatusService {
    String bankingSuccess(HttpServletRequest request,
                          HttpSession session);
}
