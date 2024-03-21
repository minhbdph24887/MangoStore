package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Rank;
import com.example.mangostore.entity.Role;
import com.example.mangostore.entity.Voucher;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.RankRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.repository.VoucherRepository;
import com.example.mangostore.request.RestoreVoucherRequest;
import com.example.mangostore.service.VoucherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class VoucherServiceImpl implements VoucherService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final VoucherRepository voucherRepository;
    private final RankRepository rankRepository;
    private final Gender gender;

    public VoucherServiceImpl(AccountRepository accountRepository,
                              RoleRepository roleRepository,
                              VoucherRepository voucherRepository,
                              RankRepository rankRepository,
                              Gender gender) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.voucherRepository = voucherRepository;
        this.rankRepository = rankRepository;
        this.gender = gender;
    }

    @Override
    public String indexVoucher(Model model,
                               HttpSession session,
                               String keyword) {
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

                List<Voucher> itemsVoucher = voucherRepository.getAllVoucherByStatus1And2();
                if (keyword != null) {
                    itemsVoucher = voucherRepository.searchVoucher(keyword);
                    model.addAttribute("keyword", keyword);
                }
                model.addAttribute("listVoucher", itemsVoucher);

                List<Voucher> itemsVoucherInactive = voucherRepository.getAllVoucherByStatus0();
                model.addAttribute("listVoucherInactive", itemsVoucherInactive);

                model.addAttribute("addVoucher", new Voucher());

                List<Rank> itemsRank = rankRepository.getAllRankByStatus1();
                model.addAttribute("listRank", itemsRank);
                return "admin/voucher/IndexVoucher";
            }
        }
    }

    @Override
    public String addVoucher(Voucher addVoucher,
                             BindingResult result,
                             HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);

        Voucher newVoucher = new Voucher();
        newVoucher.setCodeVoucher(gender.generateCode());
        newVoucher.setNameVoucher(addVoucher.getNameVoucher());
        newVoucher.setQuantity(addVoucher.getQuantity());
        newVoucher.setReducedValue(addVoucher.getReducedValue());
        newVoucher.setVoucherFrom(addVoucher.getVoucherFrom());
        newVoucher.setRank(addVoucher.getRank());
        newVoucher.setStartDay(addVoucher.getStartDay());
        newVoucher.setEndDate(addVoucher.getEndDate());
        newVoucher.setUserCreate(detailAccount.getFullName());
        newVoucher.setUserUpdate(detailAccount.getFullName());
        newVoucher.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newVoucher.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        Integer checkStatus = 0;
        if (addVoucher.getStartDay().equals(LocalDate.now())) {
            checkStatus = 1;
        } else if (addVoucher.getStartDay().isAfter(LocalDate.now())) {
            checkStatus = 2;
        }
        newVoucher.setVoucherStatus(checkStatus);
        newVoucher.setStatus(1);
        voucherRepository.save(newVoucher);
        return "redirect:/mangostore/admin/voucher";
    }

    @Override
    public String detailVoucher(Model model,
                                HttpSession session,
                                Long idVoucher) {
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

                List<Voucher> itemsVoucherInactive = voucherRepository.getAllVoucherByStatus0();
                model.addAttribute("listVoucherInactive", itemsVoucherInactive);

                List<Rank> itemsRank = rankRepository.getAllRankByStatus1();
                model.addAttribute("listRank", itemsRank);

                Voucher detailVoucher = voucherRepository.findById(idVoucher).orElse(null);
                model.addAttribute("detailVoucher", detailVoucher);
                return "admin/voucher/DetailVoucher";
            }
        }
    }

    @Override
    public String updateVoucher(BindingResult result,
                                HttpSession session,
                                Voucher voucher) {
        Voucher detailVoucher = voucherRepository.findById(voucher.getId()).orElse(null);
        detailVoucher.setNameVoucher(voucher.getNameVoucher());
        detailVoucher.setQuantity(voucher.getQuantity());
        detailVoucher.setReducedValue(voucher.getReducedValue());
        detailVoucher.setVoucherFrom(voucher.getVoucherFrom());
        detailVoucher.setRank(voucher.getRank());
        detailVoucher.setStartDay(voucher.getStartDay());
        detailVoucher.setEndDate(voucher.getEndDate());

        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        detailVoucher.setUserUpdate(detailAccount.getFullName());
        detailVoucher.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));

        Integer checkStatus = 0;
        if (voucher.getStartDay().equals(LocalDate.now())) {
            checkStatus = 1;
        } else if (voucher.getStartDay().isAfter(LocalDate.now())) {
            checkStatus = 2;
        }
        detailVoucher.setVoucherStatus(checkStatus);
        if (voucher.getQuantity() == 0) {
            detailVoucher.setStatus(0);
        } else {
            detailVoucher.setStatus(voucher.getStatus());
        }
        voucherRepository.save(detailVoucher);
        return "redirect:/mangostore/admin/voucher";
    }

    @Override
    public String deleteVoucher(Long idVoucher) {
        Voucher detailVoucher = voucherRepository.findById(idVoucher).orElse(null);
        assert detailVoucher != null;
        detailVoucher.setStatus(0);
        voucherRepository.save(detailVoucher);
        return "redirect:/mangostore/admin/voucher";
    }

    @Override
    public boolean restoreVoucherAPI(RestoreVoucherRequest request) {
        Voucher voucher = voucherRepository.findById(request.getIdVoucher()).orElse(null);
        voucher.setQuantity(request.getQuantity());
        voucher.setStatus(1);
        voucherRepository.save(voucher);
        return true;
    }
}
