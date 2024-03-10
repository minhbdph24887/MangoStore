package com.example.mangostore.service;

import com.example.mangostore.entity.Voucher;
import com.example.mangostore.request.RestoreVoucherRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface VoucherService {
    String indexVoucher(Model model, HttpSession session, String keyword);

    String addVoucher(Voucher addVoucher, BindingResult result, HttpSession session);

    String detailVoucher(Model model, HttpSession session, Long idVoucher);

    String updateVoucher(BindingResult result, HttpSession session, Voucher voucher);

    String deleteVoucher(Long idVoucher);

    boolean restoreVoucherAPI(RestoreVoucherRequest request);
}
