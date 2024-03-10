package com.example.mangostore.restcontroller;

import com.example.mangostore.request.RestoreProductDetailRequest;
import com.example.mangostore.request.RestoreVoucherRequest;
import com.example.mangostore.service.VoucherService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/mangostore/admin/voucher")
public class VoucherRestController {
    private final VoucherService voucherService;

    public VoucherRestController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @PostMapping(value = "restore")
    public boolean restoreVoucher(@RequestBody RestoreVoucherRequest request) {
        return voucherService.restoreVoucherAPI(request);
    }
}
