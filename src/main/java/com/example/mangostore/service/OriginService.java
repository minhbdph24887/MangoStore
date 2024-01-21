package com.example.mangostore.service;

import com.example.mangostore.entity.Origin;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface OriginService {
    String indexOrigin(Model model, HttpSession session, int page, String keyword);

    String addOrigin(Origin addOrigin, BindingResult result, HttpSession session, RedirectAttributes redirectAttributes);

    String detailOrigin(Model model, HttpSession session, Long idOrigin, int page);

    String updateOrigin(RedirectAttributes redirectAttributes, BindingResult result, HttpSession session, Origin origin);

    String deleteOrigin(RedirectAttributes redirectAttributes, Long idOrigin);

    String restoreOrigin(RedirectAttributes redirectAttributes, Long idOrigin);
}
