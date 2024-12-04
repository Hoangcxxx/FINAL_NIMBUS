package com.example.duantn.dto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GioHangResponse {
    private int idSanPhamChiTiet;
    private String tenSanPham;
    private String mauSac;
    private String kichThuoc;
    private Integer soLuong;
    private BigDecimal giaBan;
    private BigDecimal thanhTien;
    private String urlAnh;
}
