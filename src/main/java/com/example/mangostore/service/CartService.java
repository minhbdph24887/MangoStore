package com.example.mangostore.service;

import com.example.mangostore.entity.AddressClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface CartService {
    String cartIndex(Model model,
                     HttpSession session);

    String reduceQuantity(Long idShoppingCartDetail);

    String increaseQuantity(Long idShoppingCartDetail);

    String deleteProductCart(Long idShoppingCartDetail);

    String viewCheckOut(Model model, HttpSession session);

    String addAddressClientForClient(AddressClient newAddressClient,
                                     HttpSession session);

    String updateStatusClientAddress(Long id, HttpSession session);

    String addVoucherToInvoice(Long id, HttpSession session);

    String addPointClientToInovice(HttpSession session);

    String updateCashInvoice(Long idInvoice);

    String bankingVnPay(Long idInvoice, HttpServletRequest request, HttpSession session);
}
