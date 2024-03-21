package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Color;
import com.example.mangostore.entity.Role;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.ColorRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.service.ColorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ColorServiceImpl implements ColorService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final ColorRepository colorRepository;
    private final Gender gender;

    public ColorServiceImpl(AccountRepository accountRepository,
                            RoleRepository roleRepository,
                            ColorRepository colorRepository,
                            Gender gender) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.colorRepository = colorRepository;
        this.gender = gender;
    }

    @Override
    public String indexColor(Model model,
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

                List<Color> itemsColor = colorRepository.getAllColorByStatus1();
                if (keyword != null) {
                    itemsColor = colorRepository.searchColor(keyword);
                    model.addAttribute("keyword", keyword);
                }
                model.addAttribute("listColor", itemsColor);

                List<Color> itemsColorInactive = colorRepository.getAllColorByStatus0();
                model.addAttribute("listColorInactive", itemsColorInactive);

                model.addAttribute("addColor", new Color());
                return "admin/color/IndexColor";
            }
        }
    }

    @Override
    public String addColor(Color addColor,
                           BindingResult result,
                           HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        Color newColor = new Color();
        newColor.setCodeColor(gender.generateCode());
        newColor.setNameColor(addColor.getNameColor());
        newColor.setNameUserCreate(detailAccount.getFullName());
        newColor.setNameUserUpdate(detailAccount.getFullName());
        newColor.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newColor.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newColor.setStatus(1);
        colorRepository.save(newColor);
        return "redirect:/mangostore/admin/color";
    }

    @Override
    public String detailColor(Model model,
                              HttpSession session,
                              Long idColor) {
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

                List<Color> itemsColorInactive = colorRepository.getAllColorByStatus0();
                model.addAttribute("listColorInactive", itemsColorInactive);

                Color detailColor = colorRepository.findById(idColor).orElse(null);
                model.addAttribute("detailColor", detailColor);
                return "admin/color/DetailColor";
            }
        }
    }

    @Override
    public String updateColor(BindingResult result,
                              HttpSession session,
                              Color color) {
        Color detailColor = colorRepository.findById(color.getId()).orElse(null);
        detailColor.setNameColor(color.getNameColor());

        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        detailColor.setNameUserUpdate(detailAccount.getFullName());
        detailColor.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        detailColor.setStatus(color.getStatus());

        colorRepository.save(detailColor);
        return "redirect:/mangostore/admin/color";
    }

    @Override
    public String deleteColor(Long idColor) {
        Color detailColor = colorRepository.findById(idColor).orElse(null);
        assert detailColor != null;
        detailColor.setStatus(0);
        colorRepository.save(detailColor);
        return "redirect:/mangostore/admin/color";
    }

    @Override
    public String restoreColor(Long idColor) {
        Color detailColor = colorRepository.findById(idColor).orElse(null);
        detailColor.setStatus(1);
        colorRepository.save(detailColor);
        return "redirect:/mangostore/admin/color";
    }
}
