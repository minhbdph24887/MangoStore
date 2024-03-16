package com.example.mangostore.config;

import com.example.mangostore.entity.ProductDetail;
import com.example.mangostore.entity.Voucher;
import com.example.mangostore.repository.ProductDetailRepository;
import com.example.mangostore.repository.VoucherRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UpdateAuto {
    private final VoucherRepository voucherRepository;
    private final ProductDetailRepository productDetailRepository;

    public UpdateAuto(VoucherRepository voucherRepository,
                      ProductDetailRepository productDetailRepository) {
        this.voucherRepository = voucherRepository;
        this.productDetailRepository = productDetailRepository;
    }

    @Scheduled(fixedRate = 5000)
    public void updateVoucherStatusesAuto() {
        for (Voucher voucher : voucherRepository.findAll()) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime voucherStartDate = voucher.getStartDay().atStartOfDay();
            LocalDateTime voucherEndDate = voucher.getEndDate().atStartOfDay();

            if (voucherStartDate.isBefore(now) && voucherEndDate.isBefore(now)) {
                voucher.setVoucherStatus(0);
                voucher.setStatus(0);
            } else if (voucherStartDate.isBefore(now) && voucherEndDate.isAfter(now)) {
                voucher.setVoucherStatus(1);
            } else if (voucherStartDate.isAfter(now) && voucherEndDate.isAfter(now)) {
                voucher.setVoucherStatus(2);
            }

            if (voucher.getQuantity() == 0) {
                voucher.setStatus(0);
            } else {
                voucher.setStatus(1);
            }
            voucherRepository.save(voucher);
        }
    }

    @Scheduled(fixedRate = 5000)
    public void updateStatusProductAuto() {
        for (ProductDetail productDetail : productDetailRepository.findAll()) {
            if (productDetail.getQuantity() == 0) {
                productDetail.setStatus(0);
            } else {
                productDetail.setStatus(1);
            }
            productDetailRepository.save(productDetail);
        }
    }
}
