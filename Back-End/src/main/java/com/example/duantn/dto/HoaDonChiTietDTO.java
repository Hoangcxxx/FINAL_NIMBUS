package com.example.duantn.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HoaDonChiTietDTO {
    private int idSanPhamChiTiet;
    private int idHoaDon;
    private String MaHoaDon;
    private int soLuong;
    private BigDecimal tongTien;
    private BigDecimal soTienThanhToan;
    private BigDecimal tienTraLai;
    private String moTa;
}
