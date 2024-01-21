package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Origin;
import com.example.mangostore.entity.Role;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.OriginRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.service.OriginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OriginServiceImpl implements OriginService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final OriginRepository originRepository;
    private final Gender gender;

    public OriginServiceImpl(AccountRepository accountRepository,
                             RoleRepository roleRepository,
                             OriginRepository originRepository,
                             Gender gender) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.originRepository = originRepository;
        this.gender = gender;
    }

    @Override
    public String indexOrigin(Model model, HttpSession session, int page, String keyword) {
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

                Page<Origin> itemsOrigin = originRepository.getAllOriginByStatus1(PageRequest.of(page, 6));
                if (keyword != null) {
                    itemsOrigin = originRepository.searchOrigin(PageRequest.of(page, 6), keyword);
                    model.addAttribute("keyword", keyword);
                }
                model.addAttribute("listOrigin", itemsOrigin);

                Page<Origin> itemsOriginInactive = originRepository.getAllOriginByStatus0(PageRequest.of(page, 6));
                model.addAttribute("listOriginInactive", itemsOriginInactive);

                model.addAttribute("currentPage", page);
                model.addAttribute("addOrigin", new Origin());
                return "admin/origin/IndexOrigin";
            }
        }
    }

    @Override
    public String addOrigin(Origin addOrigin, BindingResult result, HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "Add Origin Fail");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-danger");
        } else {
            Origin newOrigin = new Origin();
            newOrigin.setCodeOrigin(gender.generateCode());
            newOrigin.setNameOrigin(addOrigin.getNameOrigin());
            newOrigin.setNameUserCreate(detailAccount.getFullName());
            newOrigin.setNameUserUpdate(detailAccount.getFullName());
            newOrigin.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            newOrigin.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            newOrigin.setStatus(1);
            originRepository.save(newOrigin);
            redirectAttributes.addFlashAttribute("message", "Add Origin Successfully");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-success");
        }
        return "redirect:/mangostore/admin/origin";
    }

    @Override
    public String detailOrigin(Model model, HttpSession session, Long idOrigin, int page) {
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

                Page<Origin> itemsOriginInactive = originRepository.getAllOriginByStatus0(PageRequest.of(page, 6));
                model.addAttribute("listOriginInactive", itemsOriginInactive);
                model.addAttribute("currentPage", page);

                Origin detailOrigin = originRepository.findById(idOrigin).orElse(null);
                model.addAttribute("detailOrigin", detailOrigin);
                return "admin/origin/DetailOrigin";
            }
        }
    }

    @Override
    public String updateOrigin(RedirectAttributes redirectAttributes, BindingResult result, HttpSession session, Origin origin) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "Update Origin Fail");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-danger");
        } else {
            Origin detailOrigin = originRepository.findById(origin.getId()).orElse(null);
            detailOrigin.setNameOrigin(origin.getNameOrigin());

            String email = (String) session.getAttribute("loginEmail");
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            detailOrigin.setNameUserUpdate(detailAccount.getFullName());
            detailOrigin.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            detailOrigin.setStatus(origin.getStatus());

            originRepository.save(detailOrigin);
            redirectAttributes.addFlashAttribute("message", "Update Origin Successfully");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-success");
        }
        return "redirect:/mangostore/admin/origin";
    }

    @Override
    public String deleteOrigin(RedirectAttributes redirectAttributes, Long idOrigin) {
        try {
            Origin detailOrigin = originRepository.findById(idOrigin).orElse(null);
            assert detailOrigin != null;
            detailOrigin.setStatus(0);
            originRepository.save(detailOrigin);
            redirectAttributes.addFlashAttribute("message", "Delete Origin Successfully");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Delete Origin Fail");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-danger");
        }
        return "redirect:/mangostore/admin/origin";
    }

    @Override
    public String restoreOrigin(RedirectAttributes redirectAttributes, Long idOrigin) {
        Origin detailOrigin = originRepository.findById(idOrigin).orElse(null);
        if (detailOrigin != null) {
            detailOrigin.setStatus(1);
            originRepository.save(detailOrigin);
            redirectAttributes.addFlashAttribute("message", "Restore Origin Successfully");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-success");
        } else {
            redirectAttributes.addFlashAttribute("message", "Restore Origin Fail");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-danger");
        }
        return "redirect:/mangostore/admin/origin";
    }
}
