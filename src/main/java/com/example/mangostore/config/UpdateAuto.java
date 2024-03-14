package com.example.mangostore.config;

import com.example.mangostore.entity.Voucher;
import com.example.mangostore.repository.VoucherRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UpdateAuto {
    private final VoucherRepository voucherRepository;

    public UpdateAuto(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    @Scheduled(fixedRate = 5000)
    public void updateVoucherStatuses() {
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
}
