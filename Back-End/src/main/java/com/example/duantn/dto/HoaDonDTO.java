package com.example.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
	private String diaChiCuThe;
	private String sdtNguoiNhan;
	private String ghiChu;
	private Integer tinh;
	private Integer huyen;
	private Integer xa;
	private String tenTinh; // Thêm trường tên Tỉnh
	private String tenHuyen; // Thêm trường tên Huyện
	private String tenXa;    // Thêm trường tên Xã
	private String email;
	private Integer idDiaChiVanChuyen;
	private Integer idNguoiDung;
	private Date ngayTao;
	private Integer idvoucher;
	private Integer idphuongthucthanhtoanhoadon;
	private String tenPhuongThucThanhToan;
	private BigDecimal thanhTien;
	private List<SanPhamChiTietDTO> listSanPhamChiTiet;
	private Integer idTrangThaiHoaDon;
	private String tenTrangThai;
	private Integer idLoaiTrangThai;
	private String tenLoaiTrangThai;
	private Integer Loai;
	private String Mavoucher;
	private BigDecimal GiaTriMavoucher;
}