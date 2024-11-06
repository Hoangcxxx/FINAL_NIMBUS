package com.example.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonDTO {
    private Integer idHoaDon;
    private String maHoaDon;
    private String tenNguoiNhan;
    private double phiShip;
    private String diaChi;
    private String sdtNguoiNhan;
    private String tinh;
    private String huyen;
    private String xa;
    private String email;
    private Integer idDiaChiVanChuyen;
    private Integer idNguoiDung;
    private Integer idvocher;
    private Integer idtrangthaihoadon;
    private Integer idphuongthucthanhtoanhoadon;
}