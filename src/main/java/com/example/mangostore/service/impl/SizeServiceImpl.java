package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Role;
import com.example.mangostore.entity.Size;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.repository.SizeRepository;
import com.example.mangostore.service.SizeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final SizeRepository sizeRepository;
    private final Gender gender;

    public SizeServiceImpl(AccountRepository accountRepository,
                           RoleRepository roleRepository,
                           SizeRepository sizeRepository,
                           Gender gender) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.sizeRepository = sizeRepository;
        this.gender = gender;
    }

    @Override
    public String indexSize(Model model,
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

                List<Size> itemsSize = sizeRepository.getAllSizeByStatus1();
                if (keyword != null) {
                    itemsSize = sizeRepository.searchSize(keyword);
                    model.addAttribute("keyword", keyword);
                }
                model.addAttribute("listSize", itemsSize);

                List<Size> itemsSizeInactive = sizeRepository.getAllSizeByStatus0();
                model.addAttribute("listSizeInactive", itemsSizeInactive);

                model.addAttribute("addSize", new Size());
                return "admin/size/IndexSize";
            }
        }
    }

    @Override
    public String addSize(Size addSize,
                          BindingResult result,
                          HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        if (result.hasErrors()) {
        } else {
            Size newSize = new Size();
            newSize.setCodeSize(gender.generateCode());
            newSize.setNameSize(addSize.getNameSize());
            newSize.setNameUserCreate(detailAccount.getFullName());
            newSize.setNameUserUpdate(detailAccount.getFullName());
            newSize.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            newSize.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            newSize.setStatus(1);
            sizeRepository.save(newSize);
        }
        return "redirect:/mangostore/admin/size";
    }

    @Override
    public String detailSize(Model model,
                             HttpSession session,
                             Long idSize) {
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

                List<Size> itemsSizeInactive = sizeRepository.getAllSizeByStatus0();
                model.addAttribute("listSizeInactive", itemsSizeInactive);

                Size detailSize = sizeRepository.findById(idSize).orElse(null);
                model.addAttribute("detailSize", detailSize);
                return "admin/size/DetailSize";
            }
        }
    }

    @Override
    public String updateSize(BindingResult result,
                             HttpSession session,
                             Size size) {
        Size detailSize = sizeRepository.findById(size.getId()).orElse(null);
        detailSize.setNameSize(size.getNameSize());

        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        detailSize.setNameUserUpdate(detailAccount.getFullName());
        detailSize.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        detailSize.setStatus(size.getStatus());

        sizeRepository.save(detailSize);
        return "redirect:/mangostore/admin/size";
    }

    @Override
    public String deleteSize(Long idSize) {
        Size detailSize = sizeRepository.findById(idSize).orElse(null);
        assert detailSize != null;
        detailSize.setStatus(0);
        sizeRepository.save(detailSize);
        return "redirect:/mangostore/admin/size";
    }

    @Override
    public String restoreSize(Long idSize) {
        Size detailSize = sizeRepository.findById(idSize).orElse(null);
        detailSize.setStatus(1);
        sizeRepository.save(detailSize);
        return "redirect:/mangostore/admin/size";
    }
}
