package com.example.mangostore.controller;

import com.example.mangostore.service.SellOfflineService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

//    @GetMapping(value = "sell/create")
//    public String createInvoice(Model model, HttpSession session) {
//        return sellOfflineService.createInvoice(model, session);
//    }
}
