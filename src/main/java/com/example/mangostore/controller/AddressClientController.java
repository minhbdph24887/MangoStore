package com.example.mangostore.controller;

import com.example.mangostore.entity.AddressClient;
import com.example.mangostore.service.AddressClientService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/mangostore/admin/")
public class AddressClientController {
    private final AddressClientService addressClientService;

    public AddressClientController(AddressClientService addressClientService) {
        this.addressClientService = addressClientService;
    }

    @GetMapping(value = "address-client")
    public String indexAddressClient(Model model,
                                     HttpSession session,
                                     @Param("keyword") String keyword) {
        return addressClientService.indexAddressClient(model, session, keyword);
    }

    @PostMapping(value = "address-client/add")
    public String addAddressClient(@Valid AddressClient addAddressClient,
                                   BindingResult result,
                                   HttpSession session) {
        return addressClientService.addAddressClient(addAddressClient, result, session);
    }
}
