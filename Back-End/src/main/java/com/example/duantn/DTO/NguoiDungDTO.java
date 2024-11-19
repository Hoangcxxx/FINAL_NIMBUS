package com.example.duantn.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NguoiDungDTO {
        private Integer id;
        private String tenNguoiDung;
//        private LocalDate ngaytao;
        private String email;
        private String maNguoiDung;
        private String sdtNguoiDung;
        private String diaChi;
        private String gioiTinh;
        private String matKhau;
        private Integer vaiTro;
        private Boolean trangThai;

        // Getters and Setters

}
