package com.example.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonChuaThanhToanDTO {
    private Integer idHoaDon;
    private Integer idNguoiDung;
    private String maHoaDon;
    private String tenNguoiDung;
    private Boolean trangThai;
    private Date ngayTao;
}
