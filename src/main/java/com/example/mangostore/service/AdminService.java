package com.example.mangostore.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface AdminService {
    String indexAdmin(Model model, HttpSession session);
}
