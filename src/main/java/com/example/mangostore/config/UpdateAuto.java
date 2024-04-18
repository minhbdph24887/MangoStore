package com.example.mangostore.config;

import com.example.mangostore.entity.Invoice;
import com.example.mangostore.entity.ProductDetail;
import com.example.mangostore.entity.Voucher;
import com.example.mangostore.entity.VoucherClient;
import com.example.mangostore.repository.InvoiceRepository;
import com.example.mangostore.repository.ProductDetailRepository;
import com.example.mangostore.repository.VoucherClientRepository;
import com.example.mangostore.repository.VoucherRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class UpdateAuto {
    private final VoucherRepository voucherRepository;
    private final ProductDetailRepository productDetailRepository;
    private final InvoiceRepository invoiceRepository;
    private final VoucherClientRepository voucherClientRepository;

    public UpdateAuto(VoucherRepository voucherRepository,
                      ProductDetailRepository productDetailRepository,
                      InvoiceRepository invoiceRepository,
                      VoucherClientRepository voucherClientRepository) {
        this.voucherRepository = voucherRepository;
        this.productDetailRepository = productDetailRepository;
        this.invoiceRepository = invoiceRepository;
        this.voucherClientRepository = voucherClientRepository;
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

            if (voucher.getQuantity() == 0 || voucher.getVoucherStatus() == 0) {
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

    @Scheduled(fixedRate = 1000)
    public void updateInvoiceClient() {
        LocalDateTime today = LocalDateTime.now();
        for (Invoice invoice : invoiceRepository.findAll()) {
            Invoice detailInvoiceClient = invoiceRepository.findInvoiceByIdAccount(invoice.getIdCustomer());
            if (detailInvoiceClient != null) {
                if (detailInvoiceClient.getInvoiceCreationDate().plusMinutes(30).isBefore(today)) {
                    detailInvoiceClient.setInvoiceStatus(5);
                    invoiceRepository.save(detailInvoiceClient);
                }
            }
        }
    }

    @Scheduled(fixedRate = 1000)
    public void updateValidityVoucherClient() {
        for (VoucherClient voucherClient : voucherClientRepository.findAll()) {
            LocalDate startDate = voucherClient.getStartDay();
            LocalDate endDate = voucherClient.getEndDate();
            LocalDate today = LocalDate.now();

            if (today.isBefore(startDate)) {
                long daysUntilStart = ChronoUnit.DAYS.between(today, startDate);
                voucherClient.setValidity("Sử dụng sau: " + daysUntilStart + " ngày");
            } else if (today.isAfter(endDate)) {
                voucherClient.setValidity("Voucher đã kết thúc");
            } else {
                long daysOfValidity = ChronoUnit.DAYS.between(today, endDate) + 1;
                voucherClient.setValidity("Hạn sử dụng: " + daysOfValidity + " ngày");
            }
            voucherClientRepository.save(voucherClient);
        }
    }

    @Scheduled(fixedRate = 1000)
    public void updateValidityVoucher() {
        for (Voucher voucher : voucherRepository.findAll()) {
            LocalDate startDate = voucher.getStartDay();
            LocalDate endDate = voucher.getEndDate();
            LocalDate today = LocalDate.now();

            if (today.isBefore(startDate)) {
                long daysUntilStart = ChronoUnit.DAYS.between(today, startDate);
                voucher.setValidity("Sử dụng sau: " + daysUntilStart + " ngày");
            } else if (today.isAfter(endDate)) {
                voucher.setValidity("Voucher đã kết thúc");
            } else {
                long daysOfValidity = ChronoUnit.DAYS.between(today, endDate) + 1;
                voucher.setValidity("Hạn sử dụng: " + daysOfValidity + " ngày");
            }
            voucherRepository.save(voucher);
        }
    }
}
