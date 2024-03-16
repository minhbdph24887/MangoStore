package com.example.mangostore.restcontroller;

import com.example.mangostore.request.InvoiceRequest;
import com.example.mangostore.service.SellOfflineService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/mangostore/admin/sell/")
public class SellRestController {
    private final SellOfflineService sellOfflineService;

    public SellRestController(SellOfflineService sellOfflineService) {
        this.sellOfflineService = sellOfflineService;
    }

    @PostMapping(value = "create")
    public ResponseEntity<String> createInvoiceAPI(HttpSession session){
        return sellOfflineService.createInvoiceAPI(session);
    }

    @PostMapping(value = "update-status")
    public boolean updateStatusInvoice(@RequestBody InvoiceRequest request){
        return sellOfflineService.updateStatusInvoice(request);
    }
}
