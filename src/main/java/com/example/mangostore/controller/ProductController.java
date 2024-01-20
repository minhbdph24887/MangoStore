package com.example.mangostore.controller;

import com.example.mangostore.entity.Product;
import com.example.mangostore.entity.Role;
import com.example.mangostore.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/mangostore/admin/")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "product")
    public String indexProduct(Model model,
                               HttpSession session,
                               @RequestParam(defaultValue = "0") int page,
                               @Param("keyword") String keyword) {
        return productService.indexProduct(model, session, page, keyword);
    }

    @PostMapping(value = "product/add")
    public String addProduct(@Valid Product addProduct,
                             BindingResult result,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        return productService.addProduct(addProduct, result, session, redirectAttributes);
    }

    @GetMapping(value = "product/detail/{id}")
    public String detailProduct(Model model,
                                HttpSession session,
                                @PathVariable("id") Long idProduct,
                                @RequestParam(defaultValue = "0") int page) {
        return productService.detailProduct(model, session, idProduct, page);
    }

    @PostMapping(value = "product/update")
    public String updateProduct(@Valid Product product,
                                BindingResult result,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        return productService.updateProduct(redirectAttributes, result, session, product);
    }

    @GetMapping(value = "product/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long idProduct,
                                RedirectAttributes redirectAttributes) {
        return productService.deleteProduct(redirectAttributes, idProduct);
    }

    @GetMapping(value = "product/restore/{id}")
    public String restoreProduct(RedirectAttributes redirectAttributes,
                                 @PathVariable("id") Long idProduct) {
        return productService.restoreProduct(redirectAttributes, idProduct);
    }
}
