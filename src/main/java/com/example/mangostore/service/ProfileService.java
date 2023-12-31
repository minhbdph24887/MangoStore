package com.example.mangostore.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface ProfileService {
    String getAllAccountByStatus1(Model model, HttpSession session, int page);

    String restoreAccount(Long idAccount);
}
