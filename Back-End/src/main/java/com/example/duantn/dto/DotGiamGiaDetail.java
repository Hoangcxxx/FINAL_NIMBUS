package com.example.duantn.dto;

import com.example.duantn.entity.DotGiamGia;
import com.example.duantn.entity.GiamGiaSanPham;

import java.util.List;

public class DotGiamGiaDetail {
    private DotGiamGia dotGiamGia;
    private List<GiamGiaSanPham> giamGiaSanPhamList;

    public DotGiamGiaDetail(DotGiamGia dotGiamGia, List<GiamGiaSanPham> giamGiaSanPhamList) {
        this.dotGiamGia = dotGiamGia;
        this.giamGiaSanPhamList = giamGiaSanPhamList;
    }

    // Getters và Setters
    public DotGiamGia getDotGiamGia() {
        return dotGiamGia;
    }

    public void setDotGiamGia(DotGiamGia dotGiamGia) {
        this.dotGiamGia = dotGiamGia;
    }

    public List<GiamGiaSanPham> getGiamGiaSanPhamList() {
        return giamGiaSanPhamList;
    }

    public void setGiamGiaSanPhamList(List<GiamGiaSanPham> giamGiaSanPhamList) {
        this.giamGiaSanPhamList = giamGiaSanPhamList;
    }
}
