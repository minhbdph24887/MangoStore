package com.example.mangostore.service;

import com.example.mangostore.entity.Account;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    String getAllAccountByStatus1(Model model, HttpSession session);

    String restoreAccount(Long idAccount);

    String detailAccount(Model model, HttpSession session, Long idAccount);

    String updateAccount(BindingResult result, String newPassword, MultipartFile imageFile, Account account);

    String deleteAccount(Long idAccount);

    String addAccount(BindingResult result, Account addProfile);
}
