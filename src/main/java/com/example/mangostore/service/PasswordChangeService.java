package com.example.mangostore.service;

import com.example.mangostore.entity.Account;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface PasswordChangeService {
    String changePassword(Model model,
                          HttpSession session);

    String updateChangePassword(Account profile,
                                HttpSession session);
}
