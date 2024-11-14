package com.example.duantn.DTO;

public class ProductDetailUpdateRequest {
    private Integer idSanPhamCT;
    private Integer newQuantity;

    // Getters and Setters
    public Integer getIdSanPhamCT() {
        return idSanPhamCT;
    }

    public void setIdSanPhamCT(Integer idSanPhamCT) {
        this.idSanPhamCT = idSanPhamCT;
    }

    public Integer getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(Integer newQuantity) {
        this.newQuantity = newQuantity;
    }
}
