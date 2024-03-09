package com.example.mangostore.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "address_client")
public class AddressClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeAddress;
    private String nameClient;
    private String phoneNumber;
    private String specificAddress;
    private String commune;
    private String district;
    private String province;
    @DateTimeFormat(pattern = "yyyy-MM-dd : HH:mm:ss")
    private LocalDateTime dateCreate;
    @ManyToOne
    @JoinColumn(name = "idAccount")
    private Account account;
    private Integer status;

    public AddressClient(Long id,
                         String codeAddress,
                         String nameClient,
                         String phoneNumber,
                         String specificAddress,
                         String commune,
                         String district,
                         String province,
                         LocalDateTime dateCreate,
                         Account account,
                         Integer status) {
        this.id = id;
        this.codeAddress = codeAddress;
        this.nameClient = nameClient;
        this.phoneNumber = phoneNumber;
        this.specificAddress = specificAddress;
        this.commune = commune;
        this.district = district;
        this.province = province;
        this.dateCreate = dateCreate;
        this.account = account;
        this.status = status;
    }

    public AddressClient() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeAddress() {
        return codeAddress;
    }

    public void setCodeAddress(String codeAddress) {
        this.codeAddress = codeAddress;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSpecificAddress() {
        return specificAddress;
    }

    public void setSpecificAddress(String specificAddress) {
        this.specificAddress = specificAddress;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
