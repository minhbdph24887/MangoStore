package com.example.mangostore.service.impl;

import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Role;
import com.example.mangostore.entity.VoucherClient;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.repository.VoucherClientRepository;
import com.example.mangostore.service.VoucherClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class VoucherClientServiceImpl implements VoucherClientService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final VoucherClientRepository voucherClientRepository;

    public VoucherClientServiceImpl(AccountRepository accountRepository,
                                    RoleRepository roleRepository,
                                    VoucherClientRepository voucherClientRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.voucherClientRepository = voucherClientRepository;
    }

    @Override
    public String indexVoucherClient(Model model, HttpSession session) {
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

            List<VoucherClient> itemsVoucherClient = voucherClientRepository.findAllVoucherForClient(detailAccount.getRank().getId(), detailAccount.getId());
            model.addAttribute("listVoucherClient", itemsVoucherClient);
        }
        return "client/profile/detailView/VoucherClient";
    }

    @Override
    public String removeVoucherClient(Long idVoucherClient) {
        VoucherClient detailVoucherClient = voucherClientRepository.findById(idVoucherClient).orElse(null);
        assert detailVoucherClient != null;
        detailVoucherClient.setStatus(0);
        voucherClientRepository.save(detailVoucherClient);
        return "redirect:/mangostore/voucher-wallet";
    }
}
