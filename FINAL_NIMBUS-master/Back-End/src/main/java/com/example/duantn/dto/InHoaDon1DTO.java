package com.example.duantn.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InHoaDon1DTO {
    private String tenSanPham;
    private int soLuong;
    private double giaBan;

}
