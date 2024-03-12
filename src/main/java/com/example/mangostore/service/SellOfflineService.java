package com.example.mangostore.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface SellOfflineService {
    String indexSellOffline(Model model, HttpSession session);

    ResponseEntity<String> createInvoiceAPI(HttpSession session);

    String editInvoice(Long idInvoice, Model model, HttpSession session);

    String updateClient(Long idInvoice, String numberPhoneClient);

    String updatePoint(Long idInvoice, Integer pointClient);

    String cancelInvoice(Long idInvoice);
}
