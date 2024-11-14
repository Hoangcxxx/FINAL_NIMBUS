package com.example.duantn.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
@Setter
public class GioHangChiTietDTO {
	private Integer idGioHangChiTiet;
	private Integer soLuong;
	private BigDecimal donGia;
	private BigDecimal thanhTien;
	private Integer idSanPhamChiTiet;
	private Integer idGioHang;
	private Integer idSanPham;
	private Integer idMauSac;
	private Integer idKichThuoc;
	private Integer idChatLieu;
}
