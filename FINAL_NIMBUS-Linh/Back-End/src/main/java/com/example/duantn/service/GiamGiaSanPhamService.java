package com.example.duantn.service;

import com.example.duantn.repository.GiamGiaSanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class GiamGiaSanPhamService {
    @Autowired
    private GiamGiaSanPhamRepository giamGiaSanPhamRepository;

    // Phương thức xóa sản phẩm giảm giá theo id sản phẩm và id đợt giảm giá
    @Transactional
    public void xoaSanPhamGiamGiaTheoDot(Integer idDotGiamGia, Integer idSanPham) {
        // In ra thông tin các tham số để kiểm tra
        System.out.println("Xóa sản phẩm giảm giá theo đợt giảm giá với idDotGiamGia: " + idDotGiamGia + ", idSanPham: " + idSanPham);

        // Thực hiện xóa
        giamGiaSanPhamRepository.deleteBySanPhamIdSanPhamAndDotGiamGiaIdDotGiamGia(idSanPham, idDotGiamGia);
    }
}
