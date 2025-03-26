package com.example.duantn.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
@Getter@Setter
public class LichSuThanhToanRequest {
    private BigDecimal soTienThanhToan;
    private Date ngayGiaoDich;
    private Boolean trangThaiThanhToan;
    private String moTa;
    private Integer idNhanVien;
}
