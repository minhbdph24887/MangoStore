package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Role;
import com.example.mangostore.entity.Voucher;
import com.example.mangostore.entity.VoucherClient;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.repository.VoucherClientRepository;
import com.example.mangostore.repository.VoucherRepository;
import com.example.mangostore.request.IdVoucherRequest;
import com.example.mangostore.service.VoucherShopClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class VoucherShopClientServiceImpl implements VoucherShopClientService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final VoucherRepository voucherRepository;
    private final Gender gender;
    private final VoucherClientRepository voucherClientRepository;

    public VoucherShopClientServiceImpl(AccountRepository accountRepository,
                                        RoleRepository roleRepository,
                                        VoucherRepository voucherRepository,
                                        Gender gender,
                                        VoucherClientRepository voucherClientRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.voucherRepository = voucherRepository;
        this.gender = gender;
        this.voucherClientRepository = voucherClientRepository;
    }

    @Override
    public String indexVoucherShop(Model model, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email != null) {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);
            Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
            if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                model.addAttribute("checkAuthentication", detailRoleByEmail);
            }
        }

        List<Voucher> getAllVoucher = voucherRepository.getAllVoucherOnline();
        model.addAttribute("listVoucher", getAllVoucher);
        model.addAttribute("voucherValidity", gender.voucherValidity());
        return "client/VoucherShop";
    }

    @Override
    public boolean addVoucherClient(IdVoucherRequest request, HttpSession session) {
        Voucher voucher = voucherRepository.findById(request.getIdVoucher()).orElse(null);
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        assert voucher != null;
        VoucherClient existingVoucherClient = voucherClientRepository.voucherClientByAccountAndVoucher(detailAccount.getId(), voucher.getId());
        if (existingVoucherClient != null) {
            return false;
        } else {
            VoucherClient voucherClient = new VoucherClient();
            voucherClient.setCodeVoucher(gender.generateCode());
            voucherClient.setNameVoucher(voucher.getNameVoucher());
            voucherClient.setReducedValue(voucher.getReducedValue());
            voucherClient.setAccount(detailAccount);
            voucherClient.setVoucher(voucher);
            voucherClient.setSaveDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            voucherClient.setStartDay(voucher.getStartDay());
            voucherClient.setEndDate(voucher.getEndDate());
            voucherClient.setVoucherStatus(voucher.getVoucherStatus());
            voucherClient.setStatus(voucher.getStatus());
            voucherClientRepository.save(voucherClient);

            int newQuantity = voucher.getQuantity() - 1;
            if (newQuantity == 0) {
                voucher.setQuantity(0);
                voucher.setStatus(0);
                voucherRepository.save(voucher);
            } else {
                voucher.setQuantity(newQuantity);
                voucherRepository.save(voucher);
            }
            return true;
        }
    }
}
