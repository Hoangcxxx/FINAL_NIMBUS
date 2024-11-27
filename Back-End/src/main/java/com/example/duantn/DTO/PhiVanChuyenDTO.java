package com.example.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhiVanChuyenDTO {
    private Integer idDiaChiVanChuyen;
    private BigDecimal SoTienVanChuyen;
    private Boolean trangThai;
    private Integer idPhiVanChuyen;
    private Integer idHoaDon;
}
