package com.example.duantn.DTO;

import lombok.Data;

@Data
public class NguoiDungDTO {

        private String tenNguoiDung;
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
