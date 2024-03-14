package com.example.mangostore.service;

import com.example.mangostore.entity.Voucher;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface SellOfflineService {
    String indexSellOffline(Model model, HttpSession session);

    ResponseEntity<String> createInvoiceAPI(HttpSession session);

    String editInvoice(Long idInvoice, Model model, HttpSession session);

    String updateClient(Long idInvoice, String numberPhoneClient);

    String updatePoint(Long idInvoice, Integer pointClient);

    String updateVoucher(Long idInvoice, Voucher voucher);

    String cancelInvoice(Long idInvoice);

    String addProduct(Long idInvoice, Long idProductDetail, Integer newQuantity);

    String deleteProduct(Long idInvoiceDetail);

    String updateStatusInvoice(Long idInvoice, Integer returnClientMoney);
}
