package com.example.mangostore.controller;

import com.example.mangostore.entity.Account;
import com.example.mangostore.service.ClientService;
import com.example.mangostore.service.PasswordChangeService;
import com.example.mangostore.service.VoucherShopClientService;
import com.example.mangostore.service.ProfileClientService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/mangostore/")
public class ClientController {
    private final ClientService clientService;
    private final VoucherShopClientService voucherShopClientService;
    private final ProfileClientService profileClientService;
    private final PasswordChangeService passwordChangeService;

    public ClientController(ClientService clientService,
                            VoucherShopClientService voucherShopClientService,
                            ProfileClientService profileClientService,
                            PasswordChangeService passwordChangeService) {
        this.clientService = clientService;
        this.voucherShopClientService = voucherShopClientService;
        this.profileClientService = profileClientService;
        this.passwordChangeService = passwordChangeService;
    }

    @GetMapping(value = "home")
    public String indexClient(Model model,
                              HttpSession session) {
        return clientService.indexClient(model, session);
    }

    @GetMapping(value = "product")
    public String viewProductClient(Model model,
                                    HttpSession session,
                                    @Param("sortDirection") String sortDirection,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        return clientService.viewProductClient(model, session, sortDirection, pageNo);
    }

    @GetMapping(value = "voucher")
    public String indexVoucherShop(Model model,
                                   HttpSession session) {
        return voucherShopClientService.indexVoucherShop(model, session);
    }

    @GetMapping(value = "profile")
    public String viewProfileClient(Model model,
                                    HttpSession session) {
        return profileClientService.indexProfile(model, session);
    }

    @PostMapping(value = "profile/update")
    public String updateProfileClient(@Valid Account profile,
                                      @RequestParam("imageFile") MultipartFile imageFile,
                                      HttpSession session) {
        return profileClientService.updateProfile(profile, imageFile, session);
    }

    @GetMapping(value = "change-password")
    public String viewChangePassword(Model model,
                                     HttpSession session) {
        return passwordChangeService.changePassword(model, session);
    }

    @PostMapping(value = "change-password/update")
    public String changePassword(@Valid Account profile,
                                 HttpSession session) {
        return passwordChangeService.updateChangePassword(profile, session);
    }
}
