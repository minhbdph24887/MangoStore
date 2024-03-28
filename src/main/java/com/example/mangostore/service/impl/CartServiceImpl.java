package com.example.mangostore.service.impl;

import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Role;
import com.example.mangostore.entity.ShoppingCart;
import com.example.mangostore.entity.ShoppingCartDetail;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.repository.ShoppingCartDetailRepository;
import com.example.mangostore.repository.ShoppingCartRepository;
import com.example.mangostore.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartDetailRepository shoppingCartDetailRepository;

    public CartServiceImpl(AccountRepository accountRepository,
                           RoleRepository roleRepository,
                           ShoppingCartRepository shoppingCartRepository,
                           ShoppingCartDetailRepository shoppingCartDetailRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartDetailRepository = shoppingCartDetailRepository;
    }

    @Override
    public String cartIndex(Model model, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);
            Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
            if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                model.addAttribute("checkAuthentication", detailRoleByEmail);
            }

            ShoppingCart detailShoppingCart = shoppingCartRepository.findByIdAccount(detailAccount.getId());
            List<ShoppingCartDetail> listShoppingCartByAccount = shoppingCartDetailRepository.getAllShoppingCart(detailShoppingCart.getId());
            model.addAttribute("listShoppingCartByAccount", listShoppingCartByAccount);
            return "client/CartIndex";
        }
    }
}
