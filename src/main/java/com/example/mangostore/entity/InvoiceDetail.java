package com.example.mangostore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "invoice_detail")
public class InvoiceDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "idProductDetail")
    private ProductDetail productDetail;
    @ManyToOne
    @JoinColumn(name = "idInvoice")
    private Invoice invoice;
    private Integer quantity;
    private Integer price;
    private Integer capitalSum;

    public InvoiceDetail(Long id,
                         ProductDetail productDetail,
                         Invoice invoice,
                         Integer quantity,
                         Integer price,
                         Integer capitalSum) {
        this.id = id;
        this.productDetail = productDetail;
        this.invoice = invoice;
        this.quantity = quantity;
        this.price = price;
        this.capitalSum = capitalSum;
    }

    public InvoiceDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCapitalSum() {
        return capitalSum;
    }

    public void setCapitalSum(Integer capitalSum) {
        this.capitalSum = capitalSum;
    }
}
