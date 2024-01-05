package com.example.mangostore.service;

import com.example.mangostore.entity.Account;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface ProfileService {
    String getAllAccountByStatus1(Model model, HttpSession session, int page);

    String restoreAccount(RedirectAttributes redirectAttributes, Long idAccount);

    String detailAccount(Model model, HttpSession session, Long idAccount);

    String updateAccount(RedirectAttributes redirectAttributes, BindingResult result, String newPassword, MultipartFile imageFile, Account account);

    String deleteAccount(RedirectAttributes redirectAttributes, Long idAccount);

    String addAccount(RedirectAttributes redirectAttributes, BindingResult result, Account addProfile);
}
