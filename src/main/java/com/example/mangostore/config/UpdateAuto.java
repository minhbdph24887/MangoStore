package com.example.mangostore.config;

import com.example.mangostore.entity.Invoice;
import com.example.mangostore.entity.ProductDetail;
import com.example.mangostore.entity.Voucher;
import com.example.mangostore.repository.InvoiceRepository;
import com.example.mangostore.repository.ProductDetailRepository;
import com.example.mangostore.repository.VoucherRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UpdateAuto {
    private final VoucherRepository voucherRepository;
    private final ProductDetailRepository productDetailRepository;
    private final InvoiceRepository invoiceRepository;

    public UpdateAuto(VoucherRepository voucherRepository,
                      ProductDetailRepository productDetailRepository,
                      InvoiceRepository invoiceRepository) {
        this.voucherRepository = voucherRepository;
        this.productDetailRepository = productDetailRepository;
        this.invoiceRepository = invoiceRepository;
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
}
