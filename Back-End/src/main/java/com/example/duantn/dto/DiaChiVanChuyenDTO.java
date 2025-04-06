package com.example.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiaChiVanChuyenDTO {
        private Integer idDiaChiVanChuyen;
        private Integer idTinh;
        private Integer idHuyen;
        private Integer idXa;
        private Integer idNguoiDung;  // Sử dụng Integer thay vì String
        private String moTa;

}
