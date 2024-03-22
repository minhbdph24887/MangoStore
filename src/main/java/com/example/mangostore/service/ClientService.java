package com.example.mangostore.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface ClientService {
    String indexClient(Model model,
                       HttpSession session);

    String viewProductClient(Model model,
                             HttpSession session,
                             String sortDirection,
                             Integer pageNo);
}
