package com.example.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimKiemNguoiDungDTO {
    private Integer idNguoiDung;
    private String tenNguoiDung;
    private String sdt;
}