package com.example.duantn.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HoaDonUpdateDTO {
    private BigDecimal phiShip = BigDecimal.ZERO;
    private Date ngayThanhToan;
    private BigDecimal thanhTien;
    private int idPtThanhToanHoaDon;
    private Integer trangThaiHoaDon;
    private String setSdtNguoiNhan;
}
