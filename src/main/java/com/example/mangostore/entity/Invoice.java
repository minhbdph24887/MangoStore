package com.example.mangostore.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeInvoice;
    private String nameInvoice;
    @ManyToOne
    @JoinColumn(name = "idAccount")
    private Account account;
    private String invoiceForm;
    @ManyToOne
    @JoinColumn(name = "idVoucher")
    private Voucher voucher;
    @ManyToOne
    @JoinColumn(name = "idVoucherClient")
    private VoucherClient voucherClient;
    @ManyToOne
    @JoinColumn(name = "idAddressClient")
    private AddressClient addressClient;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime invoiceCreationDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime invoicePaymentDate;
    private Integer customerPoints;
    private Integer totalInvoiceAmount;
    private Integer totalPayment;
    private Integer invoiceStatus;

    public Invoice(Long id,
                   String codeInvoice,
                   String nameInvoice,
                   Account account,
                   String invoiceForm,
                   Voucher voucher,
                   VoucherClient voucherClient,
                   AddressClient addressClient,
                   LocalDateTime invoiceCreationDate,
                   LocalDateTime invoicePaymentDate,
                   Integer customerPoints,
                   Integer totalInvoiceAmount,
                   Integer totalPayment,
                   Integer invoiceStatus) {
        this.id = id;
        this.codeInvoice = codeInvoice;
        this.nameInvoice = nameInvoice;
        this.account = account;
        this.invoiceForm = invoiceForm;
        this.voucher = voucher;
        this.voucherClient = voucherClient;
        this.addressClient = addressClient;
        this.invoiceCreationDate = invoiceCreationDate;
        this.invoicePaymentDate = invoicePaymentDate;
        this.customerPoints = customerPoints;
        this.totalInvoiceAmount = totalInvoiceAmount;
        this.totalPayment = totalPayment;
        this.invoiceStatus = invoiceStatus;
    }

    public Invoice() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeInvoice() {
        return codeInvoice;
    }

    public void setCodeInvoice(String codeInvoice) {
        this.codeInvoice = codeInvoice;
    }

    public String getNameInvoice() {
        return nameInvoice;
    }

    public void setNameInvoice(String nameInvoice) {
        this.nameInvoice = nameInvoice;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getInvoiceForm() {
        return invoiceForm;
    }

    public void setInvoiceForm(String invoiceForm) {
        this.invoiceForm = invoiceForm;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public VoucherClient getVoucherClient() {
        return voucherClient;
    }

    public void setVoucherClient(VoucherClient voucherClient) {
        this.voucherClient = voucherClient;
    }

    public AddressClient getAddressClient() {
        return addressClient;
    }

    public void setAddressClient(AddressClient addressClient) {
        this.addressClient = addressClient;
    }

    public LocalDateTime getInvoiceCreationDate() {
        return invoiceCreationDate;
    }

    public void setInvoiceCreationDate(LocalDateTime invoiceCreationDate) {
        this.invoiceCreationDate = invoiceCreationDate;
    }

    public LocalDateTime getInvoicePaymentDate() {
        return invoicePaymentDate;
    }

    public void setInvoicePaymentDate(LocalDateTime invoicePaymentDate) {
        this.invoicePaymentDate = invoicePaymentDate;
    }

    public Integer getCustomerPoints() {
        return customerPoints;
    }

    public void setCustomerPoints(Integer customerPoints) {
        this.customerPoints = customerPoints;
    }

    public Integer getTotalInvoiceAmount() {
        return totalInvoiceAmount;
    }

    public void setTotalInvoiceAmount(Integer totalInvoiceAmount) {
        this.totalInvoiceAmount = totalInvoiceAmount;
    }

    public Integer getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(Integer totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Integer getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }
}
