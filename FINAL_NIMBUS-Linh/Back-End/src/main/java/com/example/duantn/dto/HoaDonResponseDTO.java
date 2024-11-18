package com.example.duantn.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HoaDonResponseDTO {
    private BigDecimal phiShip;
    private Date ngayThanhToan;
    private BigDecimal thanhTien;
    private int idPtThanhToanHoaDon;

}
