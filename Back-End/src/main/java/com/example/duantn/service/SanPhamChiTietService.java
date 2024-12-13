package com.example.duantn.service;

import com.example.duantn.dto.ProductDetailUpdateRequest;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        for (int i = 0; i < sanPhamChiTietList.size(); i++) {
            SanPhamChiTiet spct = sanPhamChiTietList.get(i);

            // Tạo mã sản phẩm chi tiết cho từng sản phẩm trong danh sách
            String generatedMaHoaDon = "SPCT" + String.format("%03d", currentCount + 1 + i);

            // Kiểm tra và thiết lập sản phẩm
            SanPham sanPham = new SanPham();
            sanPham.setIdSanPham(idSanPham); // Gán ID sản phẩm từ tham số
            spct.setMaSanPhamCT(generatedMaHoaDon); // Gán mã sản phẩm chi tiết đã tạo
            spct.setSanPham(sanPham);
            spct.setSoLuong(0);
            spct.setTrangThai(true); // Trạng thái là true
            spct.setNgayTao(new Date()); // Ngày tạo là ngày hiện tại
            spct.setNgayCapNhat(new Date()); // Ngày cập nhật là ngày hiện tại

            // Lưu chatLieuChiTiet nếu cần
            ChatLieuChiTiet chatLieuChiTiet = spct.getChatLieuChiTiet();
            if (chatLieuChiTiet != null && chatLieuChiTiet.getIdChatLieuChiTiet() != null) {
                chatLieuChiTiet = chatLieuChiTietRepository.findById(chatLieuChiTiet.getIdChatLieuChiTiet())
                        .orElseThrow(() -> new IllegalArgumentException("ChatLieuChiTiet không tồn tại"));
            }
            spct.setChatLieuChiTiet(chatLieuChiTiet);

            // Lưu mauSacChiTiet nếu cần
            MauSacChiTiet mauSacChiTiet = spct.getMauSacChiTiet();
            if (mauSacChiTiet != null && mauSacChiTiet.getIdMauSacChiTiet() != null) {
                mauSacChiTiet = mauSacChiTietRepository.findById(mauSacChiTiet.getIdMauSacChiTiet())
                        .orElseThrow(() -> new IllegalArgumentException("MauSacChiTiet không tồn tại"));
            }
            spct.setMauSacChiTiet(mauSacChiTiet);

            // Lưu kichThuocChiTiet nếu cần
            KichThuocChiTiet kichThuocChiTiet = spct.getKichThuocChiTiet();
            if (kichThuocChiTiet != null && kichThuocChiTiet.getIdKichThuocChiTiet() != null) {
                kichThuocChiTiet = kichThuocChiTietRepository.findById(kichThuocChiTiet.getIdKichThuocChiTiet())
                        .orElseThrow(() -> new IllegalArgumentException("KichThuocChiTiet không tồn tại"));
            }
            spct.setKichThuocChiTiet(kichThuocChiTiet);
        }

        return sanPhamChiTietRepository.saveAll(sanPhamChiTietList);
    }

    public SanPhamChiTiet getSanPhamChiTietById(Integer idSanPhamChiTiet) {
        return sanPhamChiTietRepository.findById(idSanPhamChiTiet).orElse(null);
    }
}
