package com.example.mangostore.service;

import com.example.mangostore.entity.Size;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface SizeService {
    String indexSize(Model model, HttpSession session, String keyword);

    String addSize(Size addSize, BindingResult result, HttpSession session);

    String detailSize(Model model, HttpSession session, Long idSize);

    String updateSize(BindingResult result, HttpSession session, Size size);

    String deleteSize(Long idSize);

    String restoreSize(Long idSize);
}
