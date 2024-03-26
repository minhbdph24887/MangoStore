package com.example.mangostore.service;

import com.example.mangostore.entity.Account;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface ProfileClientService {
    String indexProfile(Model model,
                        HttpSession session);

    String updateProfile(Account profile,
                         MultipartFile imageFile,
                         HttpSession session);
}
