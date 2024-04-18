package com.example.mangostore.controller;

import com.example.mangostore.entity.Account;
import com.example.mangostore.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/mangostore/")
public class ClientController {
    private final ClientService clientService;
    private final VoucherShopClientService voucherShopClientService;
    private final ProfileClientService profileClientService;
    private final PasswordChangeService passwordChangeService;
    private final VoucherClientService voucherClientService;
    private final PurchaseService purchaseService;

    public ClientController(ClientService clientService,
                            VoucherShopClientService voucherShopClientService,
                            ProfileClientService profileClientService,
                            PasswordChangeService passwordChangeService,
                            VoucherClientService voucherClientService,
                            PurchaseService purchaseService) {
        this.clientService = clientService;
        this.voucherShopClientService = voucherShopClientService;
        this.profileClientService = profileClientService;
        this.passwordChangeService = passwordChangeService;
        this.voucherClientService = voucherClientService;
        this.purchaseService = purchaseService;
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

    @GetMapping(value = "product/detail/{id}")
    public String detailProductClient(@PathVariable("id") Long idProductDetail,
                                      Model model,
                                      HttpSession session) {
        return clientService.detailProductClient(idProductDetail, model, session);
    }

    @GetMapping(value = "voucher-wallet")
    public String viewVoucherClient(HttpSession session,
                                    Model model) {
        return voucherClientService.indexVoucherClient(model, session);
    }

    @GetMapping(value = "voucher-wallet/remove")
    public String removeVoucherClient(@RequestParam("id") Long idVoucherClient) {
        return voucherClientService.removeVoucherClient(idVoucherClient);
    }

    @GetMapping(value = "purchase")
    public String viewPurchase(Model model,
                               HttpSession session,
                               @RequestParam(name = "status", defaultValue = "all") String status,
                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                               @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
        return purchaseService.indexViewPurchase(model, session, status, pageNo, pageSize);
    }

    @GetMapping(value = "purchase/remove")
    public String cancelPurchase(@RequestParam("id") Long idInvoice) {
        return purchaseService.cancelPurchase(idInvoice);
    }
}
