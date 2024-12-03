package com.example.duantn.service;
import com.example.duantn.entity.SanPham;
import com.example.duantn.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


@Service
public class SanPhamService {
    @Autowired
    private static SanPhamRepository sanPhamRepository;


    @Autowired
    public SanPhamService(SanPhamRepository sanPhamRepository) {
        this.sanPhamRepository = sanPhamRepository;
    }


    public List<Object[]> getAllSanPhams() {
        return sanPhamRepository.getAllSanPham();
    }
    public List<Map<String, Object>> getSPBanHangTaiQuay() {
        List<Object[]> results = sanPhamRepository.getAllSanPhamBanHang();
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("idSanPham", row[0]);
            map.put("maSanPham", row[1]);
            map.put("tenSanPham", row[2]);
            map.put("giaBan", row[3]);
            map.put("moTa", row[4]);
            map.put("tenDanhMuc", row[5]);
            map.put("tenDotGiamGia", row[6]);
            map.put("giaKhuyenMai", row[7]);
            map.put("giaTriGiamGia", row[8]);
            map.put("coKhuyenMai", row[9]);
            map.put("ngayBatDauKhuyenMai", row[10]);
            map.put("ngayKetThucKhuyenMai", row[11]);
            map.put("urlAnh", row[12]);
            map.put("thuTu", row[13]);
            resultList.add(map);
        }
        return resultList;
    }
    public List<Object[]> getSanPhamChiTiet(Integer idSanPham) {
        List<Object[]> results = sanPhamRepository.getSanPhamCTBanHang(idSanPham);
        return results;
    }
    public List<Object[]> getSanPhamById(String idSanPham) {
        return sanPhamRepository.getSanPhamById(idSanPham);
    }


    public List<Object[]> getSanPhamsByDanhMuc(Integer idDanhMuc) {
        return sanPhamRepository.getSanPhamByDanhMuc(idDanhMuc); // Trả về danh sách từ repository
    }

    public List<SanPham> getSanPhamForBanHang() {
        return sanPhamRepository.findSanPhamForBanHang();
    }

}
