package com.example.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhuongThucThanhToanHoaDonDTO {
    private Integer idThanhToanHoaDon;
    private String tenPhuongThucThanhToan;
    private String maHoaDon;
    private Date ngayGiaoDich;
    private String moTa;
    private String trangThai;
    private String noiDungThanhToan;
}
