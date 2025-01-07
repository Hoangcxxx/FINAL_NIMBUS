package com.example.duantn.service;
import com.example.duantn.entity.SanPham;
import com.example.duantn.entity.SanPhamChiTiet;
import com.example.duantn.repository.DotGiamGiaRepository;
import com.example.duantn.repository.SanPhamChiTietRepository;
import com.example.duantn.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


@Service
public class SanPhamService {
    @Autowired
    private static SanPhamRepository sanPhamRepository;
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    private static DotGiamGiaRepository dotGiamGiaRepository;

    @Autowired
    public SanPhamService(SanPhamRepository sanPhamRepository) {
        this.sanPhamRepository = sanPhamRepository;
    }


    public List<Object[]> getAllSanPhams() {
        return sanPhamRepository.getAllSanPham();
    }
    public List<Object[]> getAllSanPhamAD() {
        return sanPhamRepository.getAllSanPhamAD();
    }
    public List<Object[]> getAllSanPhamGiamGia() {
        return sanPhamRepository.getAllSanPhamGiamGia();
    }
    public SanPham getSanPhamById(Integer idSanPham) {
        return sanPhamRepository.findById(idSanPham).orElse(null);
    }

    public List<Object[]> getSanPhamsByDanhMuc(Integer idDanhMuc) {
        return sanPhamRepository.getSanPhamByDanhMuc(idDanhMuc); // Trả về danh sách từ repository
    }
    public List<Object[]> getSanPhamsByIdDotGiamGia(Integer idDotGiamGia) {
        return sanPhamRepository.getSanPhamByIdDotGiamGia(idDotGiamGia); // Trả về danh sách từ repository
    }
    public SanPham updateSanPham(Integer idSanPham, SanPham sanPham) {
        sanPham.setIdSanPham(idSanPham);
        return sanPhamRepository.save(sanPham);
    }

    public void deleteSanPham(Integer idSanPham) {
        // Xóa các hình ảnh liên quan
        sanPhamRepository.deleteHinhAnhBySanPhamId(idSanPham);

        // Xóa các chi tiết sản phẩm liên quan
        sanPhamRepository.deleteChiTietBySanPhamId(idSanPham);

        // Xóa sản phẩm chính
        sanPhamRepository.deleteSanPhamByIdSanPham(idSanPham);
    }




    @Transactional
    public Integer addSanPham(Integer idDanhMuc, String maSanPham, String tenSanPham, BigDecimal giaBan, String moTa, Date ngayTao, Date ngayCapNhat, Boolean trangThai) {
        Integer idSanPham = sanPhamRepository.addSanPham(idDanhMuc,maSanPham, tenSanPham, giaBan, moTa, ngayTao, ngayCapNhat, trangThai);
        return idSanPham;
    }


    public void addHinhAnhSanPham(Integer idSanPham, String urlAnh, Integer thuTu, String loaiHinhAnh) {
        // Log thông tin ID sản phẩm
        System.out.println("Adding image for product ID: " + idSanPham);
        sanPhamRepository.addHinhAnhSanPham(idSanPham, urlAnh, thuTu, loaiHinhAnh);
    }
    public Integer getLatestSanPhamId() {
        // Lấy danh sách ID sản phẩm mới nhất, chỉ lấy 1 phần tử đầu tiên
        List<Integer> result = sanPhamRepository.getLatestSanPhamId();
        if (result != null && !result.isEmpty()) {
            return result.get(0);  // Trả về ID sản phẩm mới nhất
        } else {
            return null;  // Trường hợp không có sản phẩm
        }
    }


    @Transactional
    public void toggleStatusById(Integer idSanPham) {
        sanPhamRepository.updateStatusById(idSanPham);
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

    public List<SanPham> getSanPhamForBanHang() {
        return sanPhamRepository.findSanPhamForBanHang();
    }
    public List<SanPham> timSanPhamTheoTen(String tenSanPham) {
        return sanPhamRepository.findByTenSanPham(tenSanPham);
    }



    public boolean checkTrangThaiSanPhamByChiTiet(int idSanPhamChiTiet) {
        Optional<SanPhamChiTiet> sanPhamChiTiet = sanPhamChiTietRepository.findById(idSanPhamChiTiet);

        if (sanPhamChiTiet.isPresent()) {
            int idSanPham = sanPhamChiTiet.get().getSanPham().getIdSanPham();  // Lấy id_san_pham từ bảng san_pham_chi_tiet
            Optional<SanPham> sanPham = sanPhamRepository.findById(idSanPham);

            if (sanPham.isPresent()) {
                return sanPham.get().getTrangThai() == true;  // Kiểm tra trạng thái của sản phẩm
            }
        }
        return false; // Nếu không tìm thấy hoặc trạng thái không hợp lệ
    }
}
