package com.example.mangostore.service;

import com.example.mangostore.entity.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface ProductService {
    String indexProduct(Model model, HttpSession session, int page, String keyword);

    String addProduct(Product addProduct, BindingResult result, HttpSession session, RedirectAttributes redirectAttributes);

    String detailProduct(Model model, HttpSession session, Long idProduct, int page);

    String updateProduct(RedirectAttributes redirectAttributes, BindingResult result, HttpSession session, Product product);

    String deleteProduct(RedirectAttributes redirectAttributes, Long idProduct);

    String restoreProduct(RedirectAttributes redirectAttributes, Long idProduct);
}
