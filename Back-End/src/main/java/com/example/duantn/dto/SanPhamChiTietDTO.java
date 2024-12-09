package com.example.duantn.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SanPhamChiTietDTO {
	private Integer idspct;
	private String MaSPCT;
	private Integer soLuong;

	@Override
	public String toString() {
		return "SanPhamChiTietDTO{" +
				"idspct=" + idspct +
				", MaSPCT='" + MaSPCT + '\'' +
				", soLuong=" + soLuong +
				", tenkichthuoc='" + tenkichthuoc + '\'' +
				", giaTien=" + giaTien +
				", tenmausac='" + tenmausac + '\'' +
				", tenchatlieu='" + tenchatlieu + '\'' +
				", TenSanPham='" + TenSanPham + '\'' +
				", MoTa='" + MoTa + '\'' +
				", idSanPham=" + idSanPham +
				", DotGiamGia=" + DotGiamGia +
				'}';
	}

	private String tenkichthuoc;
	private BigDecimal giaTien;
	private String tenmausac;

	private String tenchatlieu;

	private String TenSanPham;

	private String MoTa;

	private Integer idSanPham;

	private  Integer DotGiamGia;

}
