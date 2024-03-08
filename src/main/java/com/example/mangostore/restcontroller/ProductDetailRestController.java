package com.example.mangostore.restcontroller;

import com.example.mangostore.request.CreateProductRequest;
import com.example.mangostore.request.RestoreProductDetailRequest;
import com.example.mangostore.service.ProductDetailService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

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
