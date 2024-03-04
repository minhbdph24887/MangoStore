package com.example.mangostore.request;

import java.util.Date;
import java.util.List;

public class CreateProductRequest {
    private Long idProduct;
    private String imagesProduct;
    private Long idMaterial;
    private Long idOrigin;
    private String describe;
    private String userCreate;
    private String userUpdate;
    private Date dateCreate;
    private Date dateUpdate;
    private List<VariantRequest> variantRequests;

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public String getImagesProduct() {
        return imagesProduct;
    }

    public void setImagesProduct(String imagesProduct) {
        this.imagesProduct = imagesProduct;
    }

    public Long getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(Long idMaterial) {
        this.idMaterial = idMaterial;
    }

    public Long getIdOrigin() {
        return idOrigin;
    }

    public void setIdOrigin(Long idOrigin) {
        this.idOrigin = idOrigin;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public String getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(String userUpdate) {
        this.userUpdate = userUpdate;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public List<VariantRequest> getVariantRequests() {
        return variantRequests;
    }

    public void setVariantRequests(List<VariantRequest> variantRequests) {
        this.variantRequests = variantRequests;
    }
}
