package com.example.duantn.service;

import com.example.duantn.dto.ProductDetailUpdateRequest;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
public class SanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    private ChatLieuChiTietRepository chatLieuChiTietRepository; // Change to correct type
    @Autowired
    private MauSacChiTietRepository mauSacChiTietRepository; // Change to correct type
    @Autowired
    private KichThuocChiTietRepository kichThuocChiTietRepository; // Change to correct type

    public List<SanPhamChiTiet> getAll() {
        return sanPhamChiTietRepository.findAll();
    }

    public List<Object[]> getById(Integer idSanPhamCT) {
        return sanPhamChiTietRepository.getSanPhamById(idSanPhamCT);
    }

    public List<Object[]> getAllSanPhamCTById(Integer idSanPham) {
        return sanPhamChiTietRepository.getAllSanPhamByIdSanPham(idSanPham);
    }
    public List<SanPhamChiTiet> timSanPhamChiTiet(Integer idSanPham, Integer idChatLieu, Integer idMauSac, Integer idKichThuoc) {
        return sanPhamChiTietRepository.findByMauSacChatLieuKichThuocSanPham(idSanPham, idChatLieu, idMauSac, idKichThuoc);
    }
    public List<Object[]> getSanPhamCTByIdSanPhamLonHon0(Integer idSanPhamCT) {
        return sanPhamChiTietRepository.getSanPhamCTByIdSanPhamLonHon0(idSanPhamCT);
    }
    public List<Object[]> getMauSacById(Integer idSanPhamCT) {
        return sanPhamChiTietRepository.getMauSacByIdSanPham(idSanPhamCT);
    }

    public List<Object[]> getKichThuocById(Integer idSanPhamCT) {
        return sanPhamChiTietRepository.getKichThuocByIdSanPham(idSanPhamCT);
    }

    public List<Object[]> getChatLieuById(Integer idSanPhamCT) {
        return sanPhamChiTietRepository.getChatLieuByIdSanPham(idSanPhamCT);
    }

    public SanPhamChiTiet create(SanPhamChiTiet sanPhamChiTiet) {
        return sanPhamChiTietRepository.save(sanPhamChiTiet);
    }

    public SanPhamChiTiet update(Integer id, SanPhamChiTiet sanPhamChiTiet) {
        sanPhamChiTiet.setIdSanPhamChiTiet(id);
        return sanPhamChiTietRepository.save(sanPhamChiTiet);
    }

    public void deleteByIds(List<Integer> idSanPhamCTs) {
        sanPhamChiTietRepository.deleteByIds(idSanPhamCTs);
    }
    @Transactional
    public void deleteByIdSanPhamCTs(Integer idSanPhamCT) {
        sanPhamChiTietRepository.deleteSanPhamChiTietByIdSanPhamChiTiet(idSanPhamCT);
    }

    // Giả sử bạn có một phương thức lưu cho ChatLieuChiTiet

    public void updateSoLuongSanPhamCT(List<ProductDetailUpdateRequest> payload) {
        for (ProductDetailUpdateRequest request : payload) {
            System.out.println("Updating ID: " + request.getIdSanPhamCT() + " with Quantity: " + request.getNewQuantity());

            // Kiểm tra xem giá trị có hợp lệ không
            if (request.getNewQuantity() == null) {
                System.out.println("New quantity is NULL for ID: " + request.getIdSanPhamCT());
            }

            sanPhamChiTietRepository.updateSoLuongSanPhamCT(request.getNewQuantity(), request.getIdSanPhamCT());

            // Kiểm tra lại giá trị đã cập nhật
            Integer updatedQuantity = sanPhamChiTietRepository.findQuantityById(request.getIdSanPhamCT());
            System.out.println("Quantity after update for ID " + request.getIdSanPhamCT() + ": " + updatedQuantity);
        }

    }





    public List<SanPhamChiTiet> createMultiple(List<SanPhamChiTiet> sanPhamChiTietList, Integer idSanPham) throws IOException {
        // Lấy tổng số lượng sản phẩm chi tiết hiện có trong DB để tăng mã đúng
        long currentCount = sanPhamChiTietRepository.count();

        List<SanPhamChiTiet> savedSanPhamChiTietList = new ArrayList<>();

        for (int i = 0; i < sanPhamChiTietList.size(); i++) {
            SanPhamChiTiet spct = sanPhamChiTietList.get(i);

            // Kiểm tra trùng lặp sản phẩm chi tiết
            boolean exists = sanPhamChiTietRepository.existsBySanPham_IdSanPhamAndMauSacChiTiet_IdMauSacChiTietAndChatLieuChiTiet_IdChatLieuChiTietAndKichThuocChiTiet_IdKichThuocChiTiet(
                    idSanPham,
                    spct.getMauSacChiTiet() != null ? spct.getMauSacChiTiet().getIdMauSacChiTiet() : null,
                    spct.getChatLieuChiTiet() != null ? spct.getChatLieuChiTiet().getIdChatLieuChiTiet() : null,
                    spct.getKichThuocChiTiet() != null ? spct.getKichThuocChiTiet().getIdKichThuocChiTiet() : null
            );

            if (exists) {
                throw new IllegalArgumentException("Sản phẩm chi tiết với màu sắc, chất liệu và kích thước đã tồn tại.");
            }

            // Tạo mã sản phẩm chi tiết
            String generatedMaHoaDon = "SPCT" + String.format("%03d", currentCount + 1 + i);

            // Thiết lập các thuộc tính
            SanPham sanPham = new SanPham();
            sanPham.setIdSanPham(idSanPham); // Gán ID sản phẩm từ tham số
            spct.setMaSanPhamCT(generatedMaHoaDon); // Gán mã sản phẩm chi tiết đã tạo
            spct.setSanPham(sanPham);
            spct.setSoLuong(0);
            spct.setTrangThai(true); // Trạng thái là true
            spct.setNgayTao(new Date()); // Ngày tạo là ngày hiện tại
            spct.setNgayCapNhat(new Date()); // Ngày cập nhật là ngày hiện tại

            savedSanPhamChiTietList.add(spct);
        }

        return sanPhamChiTietRepository.saveAll(savedSanPhamChiTietList);
    }

    public Map<String, String> checkSoLuong(Integer idSanPhamChiTiet) {
        Optional<SanPhamChiTiet> sanPhamChiTietOpt = sanPhamChiTietRepository.findById(idSanPhamChiTiet);

        Map<String, String> response = new HashMap<>();

        if (sanPhamChiTietOpt.isPresent()) {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietOpt.get();

            if (sanPhamChiTiet.getSoLuong() == 0) {
                response.put("message", "Sản phẩm đã hết hàng!");
            } else {
                response.put("message", "Sản phẩm còn " + sanPhamChiTiet.getSoLuong() + " sản phẩm.");
            }
        } else {
            response.put("message", "Sản phẩm không tồn tại!");
        }

        return response;
    }
    public SanPhamChiTiet getSanPhamChiTietById(Integer idSanPhamChiTiet) {
        return sanPhamChiTietRepository.findById(idSanPhamChiTiet).orElse(null);
    }
}
