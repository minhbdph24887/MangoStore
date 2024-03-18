package com.example.mangostore.controller;

import com.example.mangostore.entity.Voucher;
import com.example.mangostore.service.SellOfflineService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/mangostore/admin/")
public class SellOfflineController {
    private final SellOfflineService sellOfflineService;

    public SellOfflineController(SellOfflineService sellOfflineService) {
        this.sellOfflineService = sellOfflineService;
    }

    @GetMapping(value = "sell")
    public String indexSellOffline(Model model,
                                   HttpSession session) {
        return sellOfflineService.indexSellOffline(model, session);
    }

    @GetMapping(value = "sell/edit")
    public String editInvoice(@RequestParam("id") Long idInvoice, Model model, HttpSession session) {
        return sellOfflineService.editInvoice(idInvoice, model, session);
    }

    @PostMapping(value = "sell/update-client")
    public String updateInvoiceClient(@RequestParam("id") Long idInvoice,
                                      @RequestParam("numberPhoneClient") String numberPhoneClient) {
        return sellOfflineService.updateClient(idInvoice, numberPhoneClient);
    }

    @PostMapping(value = "sell/update-point")
    public String updateInvoicePoint(@RequestParam("id") Long idInvoice,
                                     @RequestParam("point") Integer pointClient) {
        return sellOfflineService.updatePoint(idInvoice, pointClient);
    }

    @PostMapping(value = "sell/update-voucher")
    public String updateInvoiceVoucher(@RequestParam("id") Long idInvoice,
                                       @RequestParam("voucher") Voucher voucher) {
        return sellOfflineService.updateVoucher(idInvoice, voucher);
    }

    @PostMapping(value = "sell/delete")
    public String cancelInvoice(@RequestParam("id") Long idInvoice) {
        return sellOfflineService.cancelInvoice(idInvoice);
    }

    @GetMapping(value = "sell/add-product")
    public String addProductDetail(@RequestParam("idProductDetail") Long idProductDetail,
                                   @RequestParam("idInvoice") Long idInvoice,
                                   @RequestParam("quantityNew") Integer newQuantity) {
        return sellOfflineService.addProduct(idInvoice, idProductDetail, newQuantity);
    }

    @GetMapping(value = "sell/delete-product")
    public String deleteProduct(@RequestParam("id") Long idInvoiceDetail) {
        return sellOfflineService.deleteProduct(idInvoiceDetail);
    }

    @PostMapping(value = "sell/reduce")
    public String reduceQuantity(@RequestParam("id") Long idInvoiceDetail) {
        return sellOfflineService.reduceQuantity(idInvoiceDetail);
    }

    @PostMapping(value = "sell/increase")
    public String increaseQuantity(@RequestParam("id") Long idInvoiceDetail) {
        return sellOfflineService.increaseQuantity(idInvoiceDetail);
    }

    @GetMapping(value = "sell/banking")
    public String bankingVnPay(@RequestParam("id") Long idInvoice,
                               HttpServletRequest request,
                               HttpSession session) {
        return sellOfflineService.paymentVnPay(idInvoice, request, session);
    }

    @GetMapping(value = "sell/banking/success")
    public String bankingSuccess(HttpServletRequest request, HttpSession session) {
        return sellOfflineService.bankingSuccess(request, session);
    }
}
