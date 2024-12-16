package com.example.duantn.dto;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class DiaChiVanChuyenRequest {
    private Integer idTinh;
    private Integer idHuyen;
    private Integer idXa;
    private String diaChiCuThe;
    private String moTa;
    private Integer idNguoiDung;

}
