package com.example.duantn.dto;

import com.example.duantn.entity.GiamGiaSanPham;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Getter@Setter
public class DotGiamGiaRequest {
    private String tenDotGiamGia;
    private BigDecimal giaTriGiamGia;
    private String moTa;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private boolean kieuGiamGia;
    private List<GiamGiaSanPham> sanPhamList;

}
