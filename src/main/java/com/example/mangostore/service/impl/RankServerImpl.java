package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Rank;
import com.example.mangostore.entity.Role;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.RankRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.service.RankService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class RankServerImpl implements RankService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final RankRepository rankRepository;
    private final Gender gender;

    public RankServerImpl(AccountRepository accountRepository,
                          RoleRepository roleRepository,
                          RankRepository rankRepository,
                          Gender gender) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.rankRepository = rankRepository;
        this.gender = gender;
    }

    @Override
    public String indexRank(Model model, HttpSession session, String keyword) {
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

                List<Rank> itemsRank = rankRepository.getAllRankByStatus1();
                if (keyword != null) {
                    itemsRank = rankRepository.searchRank(keyword);
                    model.addAttribute("keyword", keyword);
                }
                model.addAttribute("listRank", itemsRank);

                List<Rank> itemsRankInactive = rankRepository.getAllRankByStatus0();
                model.addAttribute("listRankInactive", itemsRankInactive);

                model.addAttribute("addRank", new Rank());
                return "admin/rank/IndexRank";
            }
        }
    }

    @Override
    public String addRank(Rank addRank, BindingResult result, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        Rank newRank = new Rank();
        newRank.setCodeRank(gender.generateCode());
        newRank.setNameRank(addRank.getNameRank());
        newRank.setMinimumScore(addRank.getMinimumScore());
        newRank.setMaximumScore(addRank.getMaximumScore());
        newRank.setUserCreate(detailAccount.getFullName());
        newRank.setUserUpdate(detailAccount.getFullName());
        newRank.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newRank.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newRank.setStatus(1);
        rankRepository.save(newRank);
        return "redirect:/mangostore/admin/rank";
    }

    @Override
    public String detailRank(Model model, HttpSession session, Long idRank) {
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

                List<Rank> itemsRankInactive = rankRepository.getAllRankByStatus0();
                model.addAttribute("listRankInactive", itemsRankInactive);

                Rank detailRank = rankRepository.findById(idRank).orElse(null);
                model.addAttribute("detailRank", detailRank);
                return "admin/rank/DetailRank";
            }
        }
    }

    @Override
    public String updateRank(BindingResult result, HttpSession session, Rank rank) {
        Rank detailRank = rankRepository.findById(rank.getId()).orElse(null);
        detailRank.setNameRank(rank.getNameRank());
        detailRank.setMinimumScore(rank.getMinimumScore());
        detailRank.setMaximumScore(rank.getMaximumScore());
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        detailRank.setUserUpdate(detailAccount.getFullName());
        detailRank.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        detailRank.setStatus(rank.getStatus());
        rankRepository.save(detailRank);
        return "redirect:/mangostore/admin/rank";
    }

    @Override
    public String deleteRank(Long idRank) {
        Rank detailRank = rankRepository.findById(idRank).orElse(null);
        assert detailRank != null;
        detailRank.setStatus(0);
        rankRepository.save(detailRank);
        return "redirect:/mangostore/admin/rank";
    }

    @Override
    public String restoreRank(Long idRank) {
        Rank detailRank = rankRepository.findById(idRank).orElse(null);
        detailRank.setStatus(1);
        rankRepository.save(detailRank);
        return "redirect:/mangostore/admin/rank";
    }
}
