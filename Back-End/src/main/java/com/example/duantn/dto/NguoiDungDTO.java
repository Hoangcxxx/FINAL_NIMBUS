package com.example.duantn.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter@Setter
public class NguoiDungDTO {
    private Integer idNguoiDung;
    private String tenNguoiDung;
    private String email;
    private String sdt;
    private Date ngayTao;
    private Boolean trangThai;
    private String gioiTinh;
    private String tenVaiTro;

}
