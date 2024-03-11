package com.example.mangostore.controller;

import com.example.mangostore.service.SellOfflineService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
