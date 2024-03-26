package com.example.mangostore.service;

import com.example.mangostore.request.IdVoucherRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface VoucherShopClientService {
    String indexVoucherShop(Model model,
                            HttpSession session);

    boolean addVoucherClient(IdVoucherRequest request,
                             HttpSession session);
}
