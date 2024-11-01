package com.example.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhiVanChuyenDTO {
    private Integer idDiaChiVanChuyen;
    private String tenPhiVanChuyen;
    private Boolean trangThai;
}
