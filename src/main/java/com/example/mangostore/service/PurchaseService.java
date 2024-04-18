package com.example.mangostore.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface PurchaseService {
    String indexViewPurchase(Model model, HttpSession session, String status, Integer pageNo, Integer pageSize);

    String cancelPurchase(Long idInvoice);
}
