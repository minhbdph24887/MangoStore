package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Invoice;
import com.example.mangostore.entity.Role;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.InvoiceRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.service.SellOfflineService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SellOfflineServiceImpl implements SellOfflineService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final InvoiceRepository invoiceRepository;
    private final Gender gender;

    public SellOfflineServiceImpl(AccountRepository accountRepository,
                                  RoleRepository roleRepository,
                                  InvoiceRepository invoiceRepository,
                                  Gender gender) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.invoiceRepository = invoiceRepository;
        this.gender = gender;
    }

    @Override
    public String indexSellOffline(Model model, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            if (detailAccount.getStatus() == 0) {
                session.invalidate();
                return "redirect:/mangostore/home";
            } else {
                model.addAttribute("profile", detailAccount);

                LocalDateTime checkDate = LocalDateTime.now();
                int hour = checkDate.getHour();
                if (hour >= 5 && hour < 10) {
                    model.addAttribute("dates", "Morning");
                } else if (hour >= 10 && hour < 13) {
                    model.addAttribute("dates", "Noon");
                } else if (hour >= 13 && hour < 18) {
                    model.addAttribute("dates", "Afternoon");
                } else {
                    model.addAttribute("dates", "Evening");
                }

                Role detailRole = roleRepository.getRoleByEmail(email);
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkMenuAdmin", true);
                } else {
                    model.addAttribute("checkMenuAdmin", false);
                }

                List<Invoice> itemsInvoice = invoiceRepository.getAllInvoiceByAccount(detailAccount.getId());
                model.addAttribute("listInvoiceByAccount", itemsInvoice);
                return "sellOffline/IndexSell";
            }
        }
    }

    @Override
    public ResponseEntity<String> createInvoiceAPI(HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        List<Invoice> checkInvoice = invoiceRepository.getAllInvoiceByAccount(detailAccount.getId());
        if (checkInvoice.size() >= 5) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Invoice newInvoice = new Invoice();
            newInvoice.setCodeInvoice(gender.generateCode());
            newInvoice.setNameInvoice(gender.generateNameInvoice());
            newInvoice.setAccount(detailAccount);
            newInvoice.setInvoiceForm("offline");
            newInvoice.setInvoiceCreationDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            newInvoice.setInvoiceStatus(0);
            invoiceRepository.save(newInvoice);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
