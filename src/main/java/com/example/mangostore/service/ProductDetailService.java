package com.example.mangostore.service;

import com.example.mangostore.entity.ProductDetail;
import com.example.mangostore.request.CreateProductRequest;
import com.example.mangostore.request.ProductDetailRequest;
import com.example.mangostore.request.RestoreProductDetailRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.util.Map;

public interface ProductDetailService {
    String indexProductDetail(Model model, HttpSession session, String keyword);

    String viewCreateProductDetail(Model model, HttpSession session);

    String addProductDetail(ProductDetailRequest productDetailForm, BindingResult result, HttpSession session, Model model);

    boolean saveProductDetailAPI(CreateProductRequest request, HttpSession session, HttpServletResponse response) throws IOException;

    String editProductDetail(Long idProductDetail, Model model, HttpSession session);

    String updateProductDetail(ProductDetail editProductDetail, BindingResult result, HttpSession session);

    String deleteProductDetail(Long idProductDetail);

    boolean restoreProductDetailAPI(RestoreProductDetailRequest request);
}
