package com.example.mangostore.service;

import com.example.mangostore.entity.Material;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface MaterialService {
    String indexMaterial(Model model, HttpSession session, int page, String keyword);

    String addMaterial(Material addMaterial, BindingResult result, HttpSession session, RedirectAttributes redirectAttributes);

    String detailMaterial(Model model, HttpSession session, Long idMaterial, int page);

    String updateMaterial(RedirectAttributes redirectAttributes, BindingResult result, HttpSession session, Material material);

    String deleteMaterial(RedirectAttributes redirectAttributes, Long idMaterial);

    String restoreMaterial(RedirectAttributes redirectAttributes, Long idMaterial);
}
