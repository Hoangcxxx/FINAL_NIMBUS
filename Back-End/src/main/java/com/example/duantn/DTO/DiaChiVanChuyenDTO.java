package com.example.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiaChiVanChuyenDTO {
    private Integer idDiaChiVanChuyen;
    private String tinh; // Tỉnh
    private String huyen; // Huyện
    private String xa; // Xã
    private BigDecimal soTienVanChuyen;
    private String moTa;
}
