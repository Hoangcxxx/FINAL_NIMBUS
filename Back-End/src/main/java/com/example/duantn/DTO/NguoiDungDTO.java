package com.example.duantn.dto;

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
        private Integer vaiTro;  // e.g., "USER", "ADMIN"
        private Boolean trangThai; // User active status

        // Getters and Setters

}
