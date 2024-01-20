package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Product;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.ProductRepository;
import com.example.mangostore.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final Gender gender;

    public ProductServiceImpl(AccountRepository accountRepository,
                              ProductRepository productRepository,
                              Gender gender) {
        this.accountRepository = accountRepository;
        this.productRepository = productRepository;
        this.gender = gender;
    }

    @Override
    public String indexProduct(Model model, HttpSession session, int page, String keyword) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);

            LocalDateTime checkDate = LocalDateTime.now();
            int hour = checkDate.getHour();
            if (hour >= 5 && hour < 10) {
                model.addAttribute("dates", "Morning");
            } else if (hour >= 10 && hour < 13) {
                model.addAttribute("dates", "Noon");
            } else if (hour >= 13 && hour < 18) {
                model.addAttribute("dates", "Afternoon");
            } else {
                model.addAttribute("dates", "Evening");
            }
            model.addAttribute("checkMenuAdmin", true);

            Page<Product> itemsProduct = productRepository.getAllProductByStatus1(PageRequest.of(page, 6));
            if (keyword != null) {
                itemsProduct = productRepository.searchProduct(PageRequest.of(page, 6), keyword);
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("listProduct", itemsProduct);

            Page<Product> itemsProductInactive = productRepository.getAllProductByStatus0(PageRequest.of(page, 6));
            model.addAttribute("listProductInactive", itemsProductInactive);

            model.addAttribute("currentPage", page);
            model.addAttribute("addProduct", new Product());
            return "admin/product/IndexProduct";
        }
    }

    @Override
    public String addProduct(Product addProduct, BindingResult result, HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "Add Product Fail");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-danger");
        } else {
            Product newProduct = new Product();
            newProduct.setCodeProduct(gender.generateCode());
            newProduct.setNameProduct(addProduct.getNameProduct());
            newProduct.setNameUserCreate(detailAccount.getFullName());
            newProduct.setNameUserUpdate(detailAccount.getFullName());
            newProduct.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            newProduct.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            newProduct.setStatus(1);
            productRepository.save(newProduct);
            redirectAttributes.addFlashAttribute("message", "Add Product Successfully");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-success");
        }
        return "redirect:/mangostore/admin/product";
    }

    @Override
    public String detailProduct(Model model, HttpSession session, Long idProduct, int page) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);

            LocalDateTime checkDate = LocalDateTime.now();
            int hour = checkDate.getHour();
            if (hour >= 5 && hour < 10) {
                model.addAttribute("dates", "Morning");
            } else if (hour >= 10 && hour < 13) {
                model.addAttribute("dates", "Noon");
            } else if (hour >= 13 && hour < 18) {
                model.addAttribute("dates", "Afternoon");
            } else {
                model.addAttribute("dates", "Evening");
            }
            model.addAttribute("checkMenuAdmin", true);

            Page<Product> itemsProductInactive = productRepository.getAllProductByStatus0(PageRequest.of(page, 6));
            model.addAttribute("listProductInactive", itemsProductInactive);
            model.addAttribute("currentPage", page);

            Product detailProduct = productRepository.findById(idProduct).orElse(null);
            model.addAttribute("detailProduct", detailProduct);
            return "admin/product/DetailProduct";
        }
    }

    @Override
    public String updateProduct(RedirectAttributes redirectAttributes, BindingResult result, HttpSession session, Product product) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "Update Product Fail");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-danger");
        } else {
            Product detailProduct = productRepository.findById(product.getId()).orElse(null);
            detailProduct.setNameProduct(product.getNameProduct());

            String email = (String) session.getAttribute("loginEmail");
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            detailProduct.setNameUserUpdate(detailAccount.getFullName());
            detailProduct.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            detailProduct.setStatus(product.getStatus());

            productRepository.save(detailProduct);
            redirectAttributes.addFlashAttribute("message", "Update Product Successfully");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-success");
        }
        return "redirect:/mangostore/admin/product";
    }

    @Override
    public String deleteProduct(RedirectAttributes redirectAttributes, Long idProduct) {
        try {
            Product detailProduct = productRepository.findById(idProduct).orElse(null);
            assert detailProduct != null;
            detailProduct.setStatus(0);
            productRepository.save(detailProduct);
            redirectAttributes.addFlashAttribute("message", "Delete Product Successfully");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Delete Product Fail");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-danger");
        }
        return "redirect:/mangostore/admin/product";
    }

    @Override
    public String restoreProduct(RedirectAttributes redirectAttributes, Long idProduct) {
        Product detailProduct = productRepository.findById(idProduct).orElse(null);
        if (detailProduct != null) {
            detailProduct.setStatus(1);
            productRepository.save(detailProduct);
            redirectAttributes.addFlashAttribute("message", "Restore Product Successfully");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-success");
        } else {
            redirectAttributes.addFlashAttribute("message", "Restore Product Fail");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-danger");
        }
        return "redirect:/mangostore/admin/product";
    }
}
