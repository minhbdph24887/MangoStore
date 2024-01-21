package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.Account;
import com.example.mangostore.entity.Material;
import com.example.mangostore.entity.Role;
import com.example.mangostore.repository.AccountRepository;
import com.example.mangostore.repository.MaterialRepository;
import com.example.mangostore.repository.RoleRepository;
import com.example.mangostore.service.MaterialService;
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
public class MaterialServiceImpl implements MaterialService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final MaterialRepository materialRepository;
    private final Gender gender;

    public MaterialServiceImpl(AccountRepository accountRepository,
                               MaterialRepository materialRepository,
                               RoleRepository roleRepository,
                               Gender gender) {
        this.accountRepository = accountRepository;
        this.materialRepository = materialRepository;
        this.roleRepository = roleRepository;
        this.gender = gender;
    }

    @Override
    public String indexMaterial(Model model, HttpSession session, int page, String keyword) {
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

                Page<Material> itemsMaterial = materialRepository.getAllMaterialByStatus1(PageRequest.of(page, 6));
                if (keyword != null) {
                    itemsMaterial = materialRepository.searchMaterial(PageRequest.of(page, 6), keyword);
                    model.addAttribute("keyword", keyword);
                }
                model.addAttribute("listMaterial", itemsMaterial);

                Page<Material> itemsMaterialInactive = materialRepository.getAllMaterialByStatus0(PageRequest.of(page, 6));
                model.addAttribute("listMaterialInactive", itemsMaterialInactive);

                model.addAttribute("currentPage", page);
                model.addAttribute("addMaterial", new Material());
                return "admin/material/IndexMaterial";
            }
        }
    }

    @Override
    public String addMaterial(Material addMaterial, BindingResult result, HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "Add Material Fail");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-danger");
        } else {
            Material newMaterial = new Material();
            newMaterial.setCodeMaterial(gender.generateCode());
            newMaterial.setNameMaterial(addMaterial.getNameMaterial());
            newMaterial.setNameUserCreate(detailAccount.getFullName());
            newMaterial.setNameUserUpdate(detailAccount.getFullName());
            newMaterial.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            newMaterial.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            newMaterial.setStatus(1);
            materialRepository.save(newMaterial);
            redirectAttributes.addFlashAttribute("message", "Add Material Successfully");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-success");
        }
        return "redirect:/mangostore/admin/material";
    }

    @Override
    public String detailMaterial(Model model, HttpSession session, Long idMaterial, int page) {
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

                Page<Material> itemsMaterialInactive = materialRepository.getAllMaterialByStatus0(PageRequest.of(page, 6));
                model.addAttribute("listMaterialInactive", itemsMaterialInactive);
                model.addAttribute("currentPage", page);

                Material detailMaterial = materialRepository.findById(idMaterial).orElse(null);
                model.addAttribute("detailMaterial", detailMaterial);
                return "admin/material/DetailMaterial";
            }
        }
    }

    @Override
    public String updateMaterial(RedirectAttributes redirectAttributes, BindingResult result, HttpSession session, Material material) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "Update Material Fail");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-danger");
        } else {
            Material detailMaterial = materialRepository.findById(material.getId()).orElse(null);
            detailMaterial.setNameMaterial(material.getNameMaterial());

            String email = (String) session.getAttribute("loginEmail");
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            detailMaterial.setNameUserUpdate(detailAccount.getFullName());
            detailMaterial.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            detailMaterial.setStatus(material.getStatus());

            materialRepository.save(detailMaterial);
            redirectAttributes.addFlashAttribute("message", "Update Material Successfully");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-success");
        }
        return "redirect:/mangostore/admin/material";
    }

    @Override
    public String deleteMaterial(RedirectAttributes redirectAttributes, Long idMaterial) {
        try {
            Material detailMaterial = materialRepository.findById(idMaterial).orElse(null);
            assert detailMaterial != null;
            detailMaterial.setStatus(0);
            materialRepository.save(detailMaterial);
            redirectAttributes.addFlashAttribute("message", "Delete Material Successfully");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Delete Material Fail");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-danger");
        }
        return "redirect:/mangostore/admin/material";
    }

    @Override
    public String restoreMaterial(RedirectAttributes redirectAttributes, Long idMaterial) {
        Material detailMaterial = materialRepository.findById(idMaterial).orElse(null);
        if (detailMaterial != null) {
            detailMaterial.setStatus(1);
            materialRepository.save(detailMaterial);
            redirectAttributes.addFlashAttribute("message", "Restore Material Successfully");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-success");
        } else {
            redirectAttributes.addFlashAttribute("message", "Restore Material Fail");
            redirectAttributes.addFlashAttribute("cssClass", "alert alert-danger");
        }
        return "redirect:/mangostore/admin/material";
    }
}
