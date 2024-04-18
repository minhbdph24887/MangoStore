package com.example.mangostore.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface VoucherClientService {
    String indexVoucherClient(Model model, HttpSession session);

    String removeVoucherClient(Long idVoucherClient);
}
