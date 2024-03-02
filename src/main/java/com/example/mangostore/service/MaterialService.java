package com.example.mangostore.service;

import com.example.mangostore.entity.Material;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface MaterialService {
    String indexMaterial(Model model, HttpSession session, String keyword);

    String addMaterial(Material addMaterial, BindingResult result, HttpSession session);

    String detailMaterial(Model model, HttpSession session, Long idMaterial);

    String updateMaterial(BindingResult result, HttpSession session, Material material);

    String deleteMaterial(Long idMaterial);

    String restoreMaterial(Long idMaterial);
}
