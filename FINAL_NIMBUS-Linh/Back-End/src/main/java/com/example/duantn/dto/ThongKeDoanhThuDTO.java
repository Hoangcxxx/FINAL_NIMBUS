package com.example.duantn.dto;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ThongKeDoanhThuDTO {
    private String tenSanPham;
    private Long soLuongDaBan;
    private BigDecimal tongDoanhThu;
}
