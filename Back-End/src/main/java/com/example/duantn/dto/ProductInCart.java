package com.example.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInCart {
    private Integer idGioHangChiTiet;
    private Integer soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private Integer idSanPhamChiTiet;
    private Integer idGioHang;
    private Integer idSanPham;
    private Integer idMauSac;
    private Integer idKichThuoc;
    private Integer idChatLieu;
    private String tenDotGiamGia;
}
