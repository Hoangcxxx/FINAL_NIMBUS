package com.example.duantn.dto;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InHoaDonDTO {
    private String maHoaDon;
    private String tenNguoiDung;
    private String sdtNguoiDung;
    private String diaChi;
    private double tongTien;
    private List<InHoaDon1DTO> sanPhamList;
}
