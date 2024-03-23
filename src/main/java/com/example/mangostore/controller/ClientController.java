package com.example.mangostore.controller;

import com.example.mangostore.service.ClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/mangostore/")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value = "home")
    public String indexClient(Model model,
                              HttpSession session) {
        return clientService.indexClient(model, session);
    }

    @GetMapping(value = "product")
    public String viewProductClient(Model model,
                                    HttpSession session,
                                    @Param("sortDirection") String sortDirection,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "size", required = false) List<Long> sizes,
                                    @RequestParam(name = "color", required = false) List<Long> colors) {
        return clientService.viewProductClient(model, session, sortDirection, pageNo, sizes, colors);
    }
}
