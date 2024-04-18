package com.example.mangostore.service.impl;

import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Invoice;
import com.example.mangostore.entity.Role;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.InvoiceRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.service.PurchaseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final InvoiceRepository invoiceRepository;

    public PurchaseServiceImpl(AccountRepository accountRepository,
                               RoleRepository roleRepository,
                               InvoiceRepository invoiceRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public String indexViewPurchase(Model model,
                                    HttpSession session,
                                    String status,
                                    Integer pageNo,
                                    Integer pageSize) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);
            Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
            if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                model.addAttribute("checkAuthentication", detailRoleByEmail);
            }

            Page<Invoice> itemsAllInvoiceOnline;
            PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize);
            itemsAllInvoiceOnline = switch (status) {
                case "waitForConfirmation" ->
                        invoiceRepository.findAllInvoiceOnline1(detailAccount.getId(), pageRequest);
                case "waitingForGoods" -> invoiceRepository.findAllInvoiceOnline2(detailAccount.getId(), pageRequest);
                case "areDelivering" -> invoiceRepository.findAllInvoiceOnline3(detailAccount.getId(), pageRequest);
                case "success" -> invoiceRepository.findAllInvoiceOnline4(detailAccount.getId(), pageRequest);
                case "cancelled" -> invoiceRepository.findAllInvoiceOnline5(detailAccount.getId(), pageRequest);
                default -> invoiceRepository.findAllInvoiceOnline(detailAccount.getId(), pageRequest);
            };
            model.addAttribute("listInvoice", itemsAllInvoiceOnline);
            model.addAttribute("totalPage", itemsAllInvoiceOnline.getTotalPages());
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("status", status);

            int startCount = (pageNo - 1) * pageSize;
            model.addAttribute("startCount", startCount);
        }
        return "client/profile/detailView/IndexPurchase";
    }

    @Override
    public String cancelPurchase(Long idInvoice) {
        Invoice detailInvoice = invoiceRepository.findById(idInvoice).orElse(null);
        assert detailInvoice != null;
        detailInvoice.setInvoiceStatus(5);
        invoiceRepository.save(detailInvoice);
        return "redirect:/mangostore/purchase";
    }
}
