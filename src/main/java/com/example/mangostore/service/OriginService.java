package com.example.mangostore.service;

import com.example.mangostore.entity.Origin;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface OriginService {
    String indexOrigin(Model model, HttpSession session, String keyword);

    String addOrigin(Origin addOrigin, BindingResult result, HttpSession session);

    String detailOrigin(Model model, HttpSession session, Long idOrigin);

    String updateOrigin(BindingResult result, HttpSession session, Origin origin);

    String deleteOrigin(Long idOrigin);

    String restoreOrigin(Long idOrigin);
}
