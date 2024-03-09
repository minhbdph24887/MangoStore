package com.example.mangostore.service;

import com.example.mangostore.entity.AddressClient;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface AddressClientService {
    String indexAddressClient(Model model, HttpSession session, String keyword);

    String addAddressClient(AddressClient addAddressClient, BindingResult result, HttpSession session);
}
