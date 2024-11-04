package com.example.duantn.dto;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ThongKeTongDoanhThuDTO {
    private long soSanPhamBanRa;
    private long tongSoLuongBanRa;
    private BigDecimal tongDoanhThu;
}
