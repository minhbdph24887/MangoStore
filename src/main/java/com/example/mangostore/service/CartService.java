package com.example.mangostore.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface CartService {
    String cartIndex(Model model,
                     HttpSession session);

    String reduceQuantity(Long idShoppingCartDetail);

    String increaseQuantity(Long idShoppingCartDetail);

    String deleteProductCart(Long idShoppingCartDetail);

    String viewCheckOut(Model model, HttpSession session);

    String updateStatusInvoiceOnline(HttpSession session);
}
