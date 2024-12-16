package com.example.duantn.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GioHangRequest {
    private int idSanPhamChiTiet;
    private int soLuong;
    private Double donGia;
    private Double thanhTien;
}
