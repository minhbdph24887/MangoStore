package com.example.mangostore.service;

import com.example.mangostore.entity.Voucher;
import com.example.mangostore.request.InvoiceRequest;
import jakarta.servlet.http.HttpServletRequest;
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

    boolean updateStatusInvoice(InvoiceRequest request);

    String reduceQuantity(Long idInvoiceDetail);

    String increaseQuantity(Long idInvoiceDetail);

    String paymentVnPay(Long idInvoice, HttpServletRequest request, HttpSession session);

    String bankingSuccess(HttpServletRequest request, HttpSession session);
}
