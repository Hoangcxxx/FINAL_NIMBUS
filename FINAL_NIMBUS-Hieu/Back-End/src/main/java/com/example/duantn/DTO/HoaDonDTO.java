package com.example.duantn.DTO;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HoaDonDTO {
	private Integer cartId;
	private Integer idHoaDon;
	private String maHoaDon;
	private String tenNguoiNhan;
	private double phiShip;
	private String diaChi;
	private String sdtNguoiNhan;
	private String ghiChu;
	private String tinh;
	private String huyen;
	private String xa;
	private String email;
	private Integer idDiaChiVanChuyen;
	private Integer idNguoiDung;
	private Integer idvoucher;
	private Integer idtrangthaihoadon;
	private Integer idphuongthucthanhtoanhoadon;
	private String tenPhuongThucThanhToan;
	private BigDecimal thanhTien;
	private List<SanphamchiTietDTO> listSanPhamChiTiet;
}