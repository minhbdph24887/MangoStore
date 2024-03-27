package com.example.mangostore.restcontroller;

import com.example.mangostore.request.AddToCartRequest;
import com.example.mangostore.request.AddToFavouriteRequest;
import com.example.mangostore.request.QuantityRequest;
import com.example.mangostore.service.ClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/mangostore/")
public class UpdateQuantityRestController {
    private final ClientService clientService;

    public UpdateQuantityRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping(value = "update-quantity/client")
    public ResponseEntity<?> returnQuantityProductDetail(@RequestBody QuantityRequest request) {
        return clientService.quantityProductDetail(request);
    }

    @PostMapping(value = "add-to-cart/client")
    public boolean addToCart(@RequestBody AddToCartRequest request, HttpSession session) {
        return clientService.addToCart(request, session);
    }

    @PostMapping(value = "add-to-favourite/client")
    public boolean addToFavourite(@RequestBody AddToFavouriteRequest request, HttpSession session) {
        return clientService.addToFavourite(request, session);
    }
}
