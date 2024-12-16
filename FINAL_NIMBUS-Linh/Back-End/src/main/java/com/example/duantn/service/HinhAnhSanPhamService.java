package com.example.duantn.service;

import com.example.duantn.entity.HinhAnhSanPham;
import com.example.duantn.entity.SanPham;
import com.example.duantn.repository.HinhAnhSanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class HinhAnhSanPhamService {

    @Autowired
    private HinhAnhSanPhamRepository hinhAnhSanPhamRepository;

    public List<HinhAnhSanPham> getHinhAnhBySanPhamId(Integer idSanPham) {
        List<Object[]> results = hinhAnhSanPhamRepository.getHinhAnhSanPhamByIdSanPham(idSanPham);
        List<HinhAnhSanPham> hinhAnhs = new ArrayList<>();

        for (Object[] result : results) {
            HinhAnhSanPham hinhAnh = new HinhAnhSanPham();
            hinhAnh.setUrlAnh((String) result[0]);
            hinhAnh.setThuTu((Integer) result[1]);

            hinhAnhs.add(hinhAnh);
        }
        return hinhAnhs;
    }

}
