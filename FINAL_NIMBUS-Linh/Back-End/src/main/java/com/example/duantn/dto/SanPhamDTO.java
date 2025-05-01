package com.example.duantn.dto;

import java.math.BigDecimal;
import java.util.Date;

public class SanPhamDTO {
    private Integer idSanPham;
    private String maSanPham;
    private String tenSanPham;
    private BigDecimal giaBan;
    private String moTa;
    private Boolean trangThai;
    private String urlAnh;
    private String tenDanhMuc;  // Tên danh mục
    private String tenDotGiamGia;  // Tên đợt giảm giá
    private BigDecimal giaKhuyenMai;  // Giá khuyến mãi
    private BigDecimal giaTriKhuyenMai;  // Giá trị khuyến mãi
    private Boolean kieuGiamGia;  // Kiểu giảm giá
    private Date ngayBatDau;  // Ngày bắt đầu khuyến mãi
    private Date ngayKetThuc;  // Ngày kết thúc khuyến mãi
    private Integer thuTu;  // Thứ tự sản phẩm

    // Constructor, getters, setters
    public SanPhamDTO(Integer idSanPham, String maSanPham, String tenSanPham, BigDecimal giaBan, String moTa, Boolean trangThai, String urlAnh,
                      String tenDanhMuc, String tenDotGiamGia, BigDecimal giaKhuyenMai, BigDecimal giaTriKhuyenMai, Boolean kieuGiamGia,
                      Date ngayBatDau, Date ngayKetThuc, Integer thuTu) {
        this.idSanPham = idSanPham;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaBan = giaBan;
        this.moTa = moTa;
        this.trangThai = trangThai;
        this.urlAnh = urlAnh;
        this.tenDanhMuc = tenDanhMuc;
        this.tenDotGiamGia = tenDotGiamGia;
        this.giaKhuyenMai = giaKhuyenMai;
        this.giaTriKhuyenMai = giaTriKhuyenMai;
        this.kieuGiamGia = kieuGiamGia;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.thuTu = thuTu;
    }
    public SanPhamDTO(BigDecimal giaBan, BigDecimal giaKhuyenMai) {
        this.giaBan = giaBan;
        this.giaKhuyenMai = giaKhuyenMai;
    }


    // Getters và setters
    public Integer getIdSanPham() {
        return idSanPham;
    }

    public void setIdSanPham(Integer idSanPham) {
        this.idSanPham = idSanPham;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public BigDecimal getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(BigDecimal giaBan) {
        this.giaBan = giaBan;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String getUrlAnh() {
        return urlAnh;
    }

    public void setUrlAnh(String urlAnh) {
        this.urlAnh = urlAnh;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public String getTenDotGiamGia() {
        return tenDotGiamGia;
    }

    public void setTenDotGiamGia(String tenDotGiamGia) {
        this.tenDotGiamGia = tenDotGiamGia;
    }

    public BigDecimal getGiaKhuyenMai() {
        return giaKhuyenMai;
    }

    public void setGiaKhuyenMai(BigDecimal giaKhuyenMai) {
        this.giaKhuyenMai = giaKhuyenMai;
    }

    public BigDecimal getGiaTriKhuyenMai() {
        return giaTriKhuyenMai;
    }

    public void setGiaTriKhuyenMai(BigDecimal giaTriKhuyenMai) {
        this.giaTriKhuyenMai = giaTriKhuyenMai;
    }

    public Boolean getKieuGiamGia() {
        return kieuGiamGia;
    }

    public void setKieuGiamGia(Boolean kieuGiamGia) {
        this.kieuGiamGia = kieuGiamGia;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public Integer getThuTu() {
        return thuTu;
    }

    public void setThuTu(Integer thuTu) {
        this.thuTu = thuTu;
    }
}
