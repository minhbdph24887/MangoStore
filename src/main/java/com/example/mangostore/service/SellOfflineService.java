package com.example.mangostore.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface SellOfflineService {
    String indexSellOffline(Model model, HttpSession session);

    ResponseEntity<String> createInvoiceAPI(HttpSession session);
}
