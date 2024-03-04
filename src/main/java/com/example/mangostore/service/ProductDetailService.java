package com.example.mangostore.service;

import com.example.mangostore.request.CreateProductRequest;
import com.example.mangostore.request.ProductDetailRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.io.IOException;

public interface ProductDetailService {
    String indexProductDetail(Model model, HttpSession session, String keyword);

    String viewCreateProductDetail(Model model, HttpSession session);

    String addProductDetail(ProductDetailRequest productDetailForm, BindingResult result, HttpSession session, Model model);

    boolean saveProductDetailAPI(CreateProductRequest request, HttpSession session, HttpServletResponse response) throws IOException;
}
