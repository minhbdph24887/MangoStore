package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Product;
import com.example.mangostore.entity.ProductDetail;
import com.example.mangostore.entity.Role;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.ProductDetailRepository;
import com.example.mangostore.repository.ProductRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final Gender gender;

    public ProductServiceImpl(AccountRepository accountRepository,
                              RoleRepository roleRepository,
                              ProductRepository productRepository,
                              Gender gender) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.productRepository = productRepository;
        this.gender = gender;
    }

    @Override
    public String indexProduct(Model model, HttpSession session, String keyword) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            if (detailAccount.getStatus() == 0) {
                session.invalidate();
                return "redirect:/mangostore/home";
            } else {
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

                Role detailRole = roleRepository.getRoleByEmail(email);
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkMenuAdmin", true);
                } else {
                    model.addAttribute("checkMenuAdmin", false);
                }

                List<Product> itemsProduct = productRepository.getAllProductByStatus1();
                if (keyword != null) {
                    itemsProduct = productRepository.searchProduct(keyword);
                    model.addAttribute("keyword", keyword);
                }
                model.addAttribute("listProduct", itemsProduct);

                List<Product> itemsProductInactive = productRepository.getAllProductByStatus0();
                model.addAttribute("listProductInactive", itemsProductInactive);

                model.addAttribute("addProduct", new Product());
                return "admin/product/IndexProduct";
            }
        }
    }

    @Override
    public String addProduct(Product addProduct, BindingResult result, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        Product newProduct = new Product();
        newProduct.setCodeProduct(gender.generateCode());
        newProduct.setNameProduct(addProduct.getNameProduct());
        newProduct.setNameUserCreate(detailAccount.getFullName());
        newProduct.setNameUserUpdate(detailAccount.getFullName());
        newProduct.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newProduct.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newProduct.setStatus(1);
        productRepository.save(newProduct);
        return "redirect:/mangostore/admin/product";
    }

    @Override
    public String detailProduct(Model model, HttpSession session, Long idProduct) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            if (detailAccount.getStatus() == 0) {
                session.invalidate();
                return "redirect:/mangostore/home";
            } else {
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

                Role detailRole = roleRepository.getRoleByEmail(email);
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkMenuAdmin", true);
                } else {
                    model.addAttribute("checkMenuAdmin", false);
                }

                List<Product> itemsProductInactive = productRepository.getAllProductByStatus0();
                model.addAttribute("listProductInactive", itemsProductInactive);

                Product detailProduct = productRepository.findById(idProduct).orElse(null);
                model.addAttribute("detailProduct", detailProduct);
                return "admin/product/DetailProduct";
            }
        }
    }

    @Override
    public String updateProduct(BindingResult result, HttpSession session, Product product) {
        Product detailProduct = productRepository.findById(product.getId()).orElse(null);
        detailProduct.setNameProduct(product.getNameProduct());

        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        detailProduct.setNameUserUpdate(detailAccount.getFullName());
        detailProduct.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        detailProduct.setStatus(product.getStatus());

        productRepository.save(detailProduct);
        return "redirect:/mangostore/admin/product";
    }

    @Override
    public String deleteProduct(Long idProduct) {
        Product detailProduct = productRepository.findById(idProduct).orElse(null);
        assert detailProduct != null;
        detailProduct.setStatus(0);
        productRepository.save(detailProduct);
        return "redirect:/mangostore/admin/product";
    }

    @Override
    public String restoreProduct(Long idProduct) {
        Product detailProduct = productRepository.findById(idProduct).orElse(null);
        detailProduct.setStatus(1);
        productRepository.save(detailProduct);
        return "redirect:/mangostore/admin/product";
    }
}
