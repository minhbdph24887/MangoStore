package com.example.mangostore.restcontroller;

import com.example.mangostore.request.CreateProductRequest;
import com.example.mangostore.request.RestoreProductDetailRequest;
import com.example.mangostore.service.ProductDetailService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/mangostore/admin/product-detail/")
public class ProductDetailRestController {
    private final ProductDetailService productDetailService;

    public ProductDetailRestController(ProductDetailService productDetailService) {
        this.productDetailService = productDetailService;
    }

    @PostMapping(value = "add")
    public boolean saveProductDetailAPI(@RequestBody CreateProductRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        return productDetailService.saveProductDetailAPI(request, session, response);
    }

    @PostMapping(value = "restore")
    public boolean restoreProductDetail(@RequestBody RestoreProductDetailRequest request) {
        return productDetailService.restoreProductDetailAPI(request);
    }
}
