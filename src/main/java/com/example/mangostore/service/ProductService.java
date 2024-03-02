package com.example.mangostore.service;

import com.example.mangostore.entity.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface ProductService {
    String indexProduct(Model model, HttpSession session, String keyword);

    String addProduct(Product addProduct, BindingResult result, HttpSession session);

    String detailProduct(Model model, HttpSession session, Long idProduct);

    String updateProduct(BindingResult result, HttpSession session, Product product);

    String deleteProduct(Long idProduct);

    String restoreProduct(Long idProduct);
}
