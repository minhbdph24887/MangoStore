package com.example.mangostore.service.impl;

import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.ProductDetail;
import com.example.mangostore.entity.Role;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.ProductDetailRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.service.ClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final ProductDetailRepository productDetailRepository;

    public ClientServiceImpl(AccountRepository accountRepository,
                             RoleRepository roleRepository,
                             ProductDetailRepository productDetailRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.productDetailRepository = productDetailRepository;
    }

    @Override
    public String indexClient(Model model, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email != null) {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            if (detailAccount.getStatus() == 0) {
                session.invalidate();
                return "redirect:/mangostore/home";
            } else {
                model.addAttribute("profile", detailAccount);
                Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
                if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                    model.addAttribute("checkAuthentication", detailRoleByEmail);
                }
            }
        }
        return "client/Home";
    }

    @Override
    public String viewProductClient(Model model) {
        List<ProductDetail> itemsAllProductDetail = productDetailRepository.findAll();
        model.addAttribute("listProductDetail", itemsAllProductDetail);
        return "client/List";
    }
}
