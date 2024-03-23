package com.example.mangostore.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.List;

public interface ClientService {
    String indexClient(Model model,
                       HttpSession session);

    String viewProductClient(Model model,
                             HttpSession session,
                             String sortDirection,
                             Integer pageNo,
                             List<Long> sizes,
                             List<Long> colors);
}
