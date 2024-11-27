package com.example.duantn.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter@Setter
public class NguoiDungDTO {
    private String tenNguoiDung;
    private String email;
    private String sdt;
    private LocalDate ngaySinh;
    private String diaChi;
    private String gioiTinh;
    private String anhDaiDien;

}
