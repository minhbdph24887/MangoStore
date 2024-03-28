package com.example.mangostore.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface CartService {
    String cartIndex(Model model,
                     HttpSession session);
}
