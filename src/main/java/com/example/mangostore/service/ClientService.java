package com.example.mangostore.service;

import com.example.mangostore.request.AddToCartRequest;
import com.example.mangostore.request.AddToFavouriteRequest;
import com.example.mangostore.request.QuantityRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface ClientService {
    String indexClient(Model model,
                       HttpSession session);

    String viewProductClient(Model model,
                             HttpSession session,
                             String sortDirection,
                             Integer pageNo);

    String detailProductClient(Long idProductDetail,
                               Model model,
                               HttpSession session);

    ResponseEntity<?> quantityProductDetail(QuantityRequest request);

    boolean addToCart(AddToCartRequest request,
                      HttpSession session);

    boolean addToFavourite(AddToFavouriteRequest request,
                           HttpSession session);
}
