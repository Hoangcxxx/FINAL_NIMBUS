package com.example.duantn.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrangThaiHoaDonDTO {
    private Integer idTrangThaiHoaDon;
    private String tenLoaiTrangThai; // Tên của loại trạng thái

}
