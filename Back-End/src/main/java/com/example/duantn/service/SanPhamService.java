package com.example.duantn.service;
import com.example.duantn.dto.SanPhamDTO;
import com.example.duantn.entity.SanPham;
import com.example.duantn.entity.SanPhamChiTiet;
import com.example.duantn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class SanPhamService {
    @Autowired
    private static SanPhamRepository sanPhamRepository;
    @Autowired
    private static GioHangChiTietRepository gioHangChiTietRepository;
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    private static DotGiamGiaRepository dotGiamGiaRepository;
    @Autowired
    private DanhGiaRepository danhGiaRepository;
    @Autowired
    public SanPhamService(SanPhamRepository sanPhamRepository) {
        this.sanPhamRepository = sanPhamRepository;
    }


    public List<Object[]> getAllSanPhams() {
        return sanPhamRepository.getAllSanPham();
    }
    public Page<Object[]> getAllSanPhams(Pageable pageable) {
        return sanPhamRepository.getAllSanPham(pageable);
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
        // Xóa các đánh giá liên quan
        danhGiaRepository.deleteBySanPhamIdSanPham(idSanPham);
        // Xóa các chi tiết sản phẩm có trong giỏ hàng chi tiết
        sanPhamRepository.deleteGioHangChiTietBySanPhamId(idSanPham);
        // Xóa các giảm giá sản phẩm có liên quan
        sanPhamRepository.deleteBySanPhamId(idSanPham);  // Thêm phương thức xóa giảm giá

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

    // Phương thức kiểm tra trạng thái sản phẩm
    public Boolean checkTrangThaiSanPham(Integer id) {
        SanPham sanPham = sanPhamRepository.findById(id).orElse(null);
        if (sanPham != null) {
            return sanPham.getTrangThai();  // Trả về trạng thái sản phẩm
        }
        return null;  // Nếu không tìm thấy sản phẩm, trả về null hoặc có thể ném ngoại lệ
    }



    // Chuyển đổi từ Entity SanPham sang DTO SanPhamDTO
    private SanPhamDTO convertToDTO(SanPham sanPham) {
        if (sanPham == null) {
            return null;
        }

        // Lấy thông tin từ các entity liên quan
        String urlAnh = sanPham.getUrlAnh();
        if (urlAnh == null || urlAnh.isEmpty()) {
            urlAnh = "default.jpg"; // Gán ảnh mặc định nếu không có ảnh
        }

        String tenDanhMuc = sanPham.getDanhMuc() != null ? sanPham.getDanhMuc().getTenDanhMuc() : null;

        // Giả sử lấy thông tin giảm giá từ đợt giảm giá đầu tiên
        String tenDotGiamGia = sanPham.getGiamGiaSanPham() != null && !sanPham.getGiamGiaSanPham().isEmpty()
                ? sanPham.getGiamGiaSanPham().get(0).getDotGiamGia().getTenDotGiamGia() : null;
        BigDecimal giaKhuyenMai = sanPham.getGiamGiaSanPham() != null && !sanPham.getGiamGiaSanPham().isEmpty()
                ? sanPham.getGiamGiaSanPham().get(0).getGiaKhuyenMai() : null;
        BigDecimal giaTriKhuyenMai = sanPham.getGiamGiaSanPham() != null && !sanPham.getGiamGiaSanPham().isEmpty()
                ? sanPham.getGiamGiaSanPham().get(0).getDotGiamGia().getGiaTriGiamGia() : null;
        Boolean kieuGiamGia = sanPham.getGiamGiaSanPham() != null && !sanPham.getGiamGiaSanPham().isEmpty()
                ? sanPham.getGiamGiaSanPham().get(0).getDotGiamGia().getKieuGiamGia() : null;
        Date ngayBatDau = sanPham.getGiamGiaSanPham() != null && !sanPham.getGiamGiaSanPham().isEmpty()
                ? sanPham.getGiamGiaSanPham().get(0).getDotGiamGia().getNgayBatDau() : null;
        Date ngayKetThuc = sanPham.getGiamGiaSanPham() != null && !sanPham.getGiamGiaSanPham().isEmpty()
                ? sanPham.getGiamGiaSanPham().get(0).getDotGiamGia().getNgayKetThuc() : null;

        Integer thuTu = sanPham.getSanPhamChiTiet() != null && !sanPham.getSanPhamChiTiet().isEmpty()
                ? sanPham.getSanPhamChiTiet().get(0).getIdSanPhamChiTiet() : null; // Giả sử lấy thứ tự chi tiết sản phẩm

        // Chuyển đổi Entity thành DTO và trả về
        return new SanPhamDTO(
                sanPham.getIdSanPham(),
                sanPham.getMaSanPham(),
                sanPham.getTenSanPham(),
                sanPham.getGiaBan(),
                sanPham.getMoTa(),
                sanPham.getTrangThai(),
                urlAnh,
                tenDanhMuc,
                tenDotGiamGia,
                giaKhuyenMai,
                giaTriKhuyenMai,
                kieuGiamGia,
                ngayBatDau,
                ngayKetThuc,
                thuTu
        );
    }

    // Phương thức tìm kiếm sản phẩm
    public List<SanPhamDTO> searchProducts(BigDecimal minPrice, BigDecimal maxPrice, Integer danhMucId, Integer chatLieuId, Integer mauSacId, Integer kichThuocId) {
        List<SanPham> sanPhams = sanPhamRepository.searchProducts(minPrice, maxPrice, danhMucId, chatLieuId, mauSacId, kichThuocId);
        return sanPhams.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public BigDecimal getGiaSanPham(Integer idSanPham) {
        Optional<SanPham> sanPham = sanPhamRepository.findById(idSanPham);
        return sanPham.map(SanPham::getGiaBan).orElse(null);
    }


}
