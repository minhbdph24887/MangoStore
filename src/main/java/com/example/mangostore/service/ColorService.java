package com.example.mangostore.service;

import com.example.mangostore.entity.Color;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface ColorService {
    String indexColor(Model model, HttpSession session, String keyword);

    String addColor(Color addColor, BindingResult result, HttpSession session);

    String detailColor(Model model, HttpSession session, Long idColor);

    String updateColor(BindingResult result, HttpSession session, Color color);

    String deleteColor(Long idColor);

    String restoreColor(Long idColor);
}
