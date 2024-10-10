package com.example.duantn.service;
import com.example.duantn.entity.SanPham;
import com.example.duantn.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class SanPhamService {
    @Autowired
    private SanPhamRepository sanPhamRepository;
    public List<Object[]> getAllSanPhams() {
        return sanPhamRepository.getAllSanPham();
    }
    private Map<String, Object> mapSanPhamSizes(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idSanPham", row[0]);               // Id sản phẩm
        map.put("tenSanPham", row[1]);              // Tên sản phẩm
        map.put("giaBan", row[2]);                  // Giá bán
        map.put("tongSoLuong", row[3]);             // Tổng số lượng
        map.put("moTa", row[4]);                     // Mô tả
        map.put("mauSac", row[5]);                   // Tên màu sắc
        map.put("tenChatLieu", row[6]);              // Tên chất liệu
        map.put("danhSachKichThuoc", row[7]);       // Danh sách kích thước
        return map;
    }

    public List<Map<String, Object>> getKichThuocBySanPhamAndMauSac(Integer idSanPham, Integer idMauSac) {
        List<Object[]> results = sanPhamRepository.findKichThuocBySanPhamAndMauSac(idSanPham, idMauSac);
        List<Map<String, Object>> kichThuocList = new ArrayList<>();

        for (Object[] result : results) {
            kichThuocList.add(mapSanPhamSizes(result)); // Sử dụng phương thức map để chuyển đổi
        }
        return kichThuocList;
    }


    public List<Object[]> getSanPhamById(String idSanPham) {
        return sanPhamRepository.getSanPhamById(idSanPham);
    }

    public List<Object[]> getSanPhamsByDanhMuc(Integer idDanhMuc) {
        return sanPhamRepository.getSanPhamByDanhMuc(idDanhMuc); // Trả về danh sách từ repository
    }

    public SanPham createSanPham(SanPham sanPham) {
        sanPham.setNgayTao(new Date());
        sanPham.setNgayCapNhat(new Date());
        return sanPhamRepository.save(sanPham);
    }

    public SanPham updateSanPham(Integer id, SanPham sanPham) {
        sanPham.setIdSanPham(id);
        return sanPhamRepository.save(sanPham);
    }

    public void deleteSanPham(Integer id) {
        sanPhamRepository.deleteById(id);
    }
}
