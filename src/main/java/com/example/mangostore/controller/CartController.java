package com.example.mangostore.controller;

import com.example.mangostore.service.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mangostore/")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping(value = "cart")
    public String cartIndex(Model model,
                            HttpSession session) {
        return cartService.cartIndex(model, session);
    }

    @PostMapping(value = "cart/reduce")
    public String reduceProductCart(@RequestParam("id") Long idShoppingCartDetail) {
        return cartService.reduceQuantity(idShoppingCartDetail);
    }

    @PostMapping(value = "cart/increase")
    public String increaseProductCart(@RequestParam("id") Long idShoppingCartDetail) {
        return cartService.increaseQuantity(idShoppingCartDetail);
    }

    @GetMapping(value = "cart/delete")
    public String deleteProductCart(@RequestParam("id") Long idShoppingCartDetail) {
        return cartService.deleteProductCart(idShoppingCartDetail);
    }

    @GetMapping(value = "cart/checkout")
    public String viewCheckOut(Model model,
                               HttpSession session) {
        return cartService.viewCheckOut(model, session);
    }

    @PostMapping(value = "cart/checkout/update")
    public String updateStatusInvoiceOnline(HttpSession session) {
        return cartService.updateStatusInvoiceOnline(session);
    }
}
