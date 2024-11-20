package com.example.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiaChiVanChuyenDTO {
    private Integer idDiaChiVanChuyen;
    private String tinh;
    private String huyen;
    private String xa;
    private String moTa;
}
