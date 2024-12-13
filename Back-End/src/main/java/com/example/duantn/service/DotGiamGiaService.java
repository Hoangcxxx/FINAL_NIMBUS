package com.example.duantn.service;

import com.example.duantn.dto.DotGiamGiaDetail;
import com.example.duantn.entity.*;
import com.example.duantn.repository.DotGiamGiaRepository;
import com.example.duantn.repository.GiamGiaSanPhamRepository;
import com.example.duantn.repository.SanPhamRepository;
import com.example.duantn.repository.TrangThaiGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DotGiamGiaService {

    @Autowired
    private DotGiamGiaRepository dotGiamGiaRepository;

    @Autowired
    private GiamGiaSanPhamRepository giamGiaSanPhamRepository;

    @Autowired
    private TrangThaiGiamGiaRepository trangThaiGiamGiaRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;
    // Tìm kiếm theo tên voucher
    public List<DotGiamGia> findByTenVoucher(String tenVoucher) {
        return dotGiamGiaRepository.findByTenDotGiamGiaContaining(tenVoucher);
    }

    // Tìm kiếm theo kiểu giảm giá
    public List<DotGiamGia> findByKieuGiamGia(Boolean kieuGiamGia) {
        return dotGiamGiaRepository.findByKieuGiamGia(kieuGiamGia);
    }
    public List<DotGiamGia> getAllDotGiamGia() {
        return dotGiamGiaRepository.findAll();
    }
    public List<Object[]> getAllSanPhamChuaGiamGia() {
        return dotGiamGiaRepository.getAllSanPhamChuaGiamGia();
    }
    public List<Object[]> getAllSanPhamDaGiamGia(Integer idDotGiamGia) {
        return dotGiamGiaRepository.getAllSanPhamDaGiamGia(idDotGiamGia);
    }
    public List<Object[]> getSanPhamChuaGiamGiaByDanhMuc(Integer idDanhMuc) {
        return dotGiamGiaRepository.getAllSanPhamChuaGiamGiaByDanhMuc(idDanhMuc); // Trả về danh sách từ repository
    }

    public DotGiamGia addDotGiamGia(DotGiamGia dotGiamGia) {
        dotGiamGia.setNgayTao(new Date());
        dotGiamGia.setNgayCapNhat(new Date());

        // Kiểm tra ngày bắt đầu và ngày kết thúc
        validateDotGiamGiaDates(dotGiamGia);

        setTrangThaiDotGiamGia(dotGiamGia);

        // Lưu đợt giảm giá
        DotGiamGia createdDotGiamGia = dotGiamGiaRepository.save(dotGiamGia);

        // Tính toán lại giá khuyến mãi cho các sản phẩm đã có
        updateGiaKhuyenMaiForSanPham(createdDotGiamGia);

        return createdDotGiamGia;
    }
    public boolean isTenDotGiamGiaExists(String tenDotGiamGia) {
        return dotGiamGiaRepository.findByTenDotGiamGia(tenDotGiamGia).isPresent();
    }
    public boolean isKieuDotGiamGiaExists(Boolean kieuGiamGia) {
        return dotGiamGiaRepository.findDotGiamGiaByKieuGiamGia(kieuGiamGia).isPresent();
    }
    private void updateGiaKhuyenMaiForSanPham(DotGiamGia dotGiamGia) {
        List<GiamGiaSanPham> giamGiaSanPhamList = giamGiaSanPhamRepository.findByDotGiamGia(dotGiamGia);
        for (GiamGiaSanPham giamGiaSanPham : giamGiaSanPhamList) {
            SanPham sanPham = giamGiaSanPham.getSanPham();
            BigDecimal giaTriGiamGia = dotGiamGia.getGiaTriGiamGia();
            BigDecimal giaKhuyenMai = calculateDiscountedPrice(sanPham.getGiaBan(), giaTriGiamGia);
            giamGiaSanPham.setGiaKhuyenMai(giaKhuyenMai);
            giamGiaSanPhamRepository.save(giamGiaSanPham);
        }
    }

    public DotGiamGia updateDotGiamGia(Integer id, DotGiamGia dotGiamGiaDetails) {
        Optional<DotGiamGia> optionalDotGiamGia = dotGiamGiaRepository.findById(id);
        if (optionalDotGiamGia.isPresent()) {
            DotGiamGia dotGiamGia = optionalDotGiamGia.get();
            dotGiamGia.setTenDotGiamGia(dotGiamGiaDetails.getTenDotGiamGia());
            dotGiamGia.setKieuGiamGia(dotGiamGiaDetails.getKieuGiamGia());
            dotGiamGia.setGiaTriGiamGia(dotGiamGiaDetails.getGiaTriGiamGia());
            dotGiamGia.setMoTa(dotGiamGiaDetails.getMoTa());
            dotGiamGia.setNgayBatDau(dotGiamGiaDetails.getNgayBatDau());
            dotGiamGia.setNgayKetThuc(dotGiamGiaDetails.getNgayKetThuc());

            // Kiểm tra ngày bắt đầu và ngày kết thúc
            validateDotGiamGiaDates(dotGiamGia);

            setTrangThaiDotGiamGia(dotGiamGia);
            return dotGiamGiaRepository.save(dotGiamGia);
        }
        return null;
    }
    public Optional<DotGiamGiaDetail> getDotGiamGiaDetailById(Integer id) {
        Optional<DotGiamGia> dotGiamGiaOptional = dotGiamGiaRepository.findById(id);
        if (dotGiamGiaOptional.isPresent()) {
            DotGiamGia dotGiamGia = dotGiamGiaOptional.get();
            List<GiamGiaSanPham> giamGiaSanPhamList = giamGiaSanPhamRepository.findByDotGiamGia(dotGiamGia);
            return Optional.of(new DotGiamGiaDetail(dotGiamGia, giamGiaSanPhamList));
        }
        return Optional.empty();
    }
    private void validateDotGiamGiaDates(DotGiamGia dotGiamGia) {
        if (dotGiamGia.getNgayBatDau() != null && dotGiamGia.getNgayKetThuc() != null) {
            if (dotGiamGia.getNgayBatDau().after(dotGiamGia.getNgayKetThuc())) {
                throw new IllegalArgumentException("Ngày bắt đầu không thể sau ngày kết thúc!");
            }
        }
    }

    public void deleteDotGiamGia(Integer id) {
        // Tìm đợt giảm giá theo ID
        Optional<DotGiamGia> dotGiamGiaOptional = dotGiamGiaRepository.findById(id);

        if (dotGiamGiaOptional.isPresent()) {
            DotGiamGia dotGiamGia = dotGiamGiaOptional.get();

            // Xóa tất cả sản phẩm liên quan đến đợt giảm giá
            List<GiamGiaSanPham> giamGiaSanPhamList = giamGiaSanPhamRepository.findByDotGiamGia(dotGiamGia);
            giamGiaSanPhamRepository.deleteAll(giamGiaSanPhamList);

            // Sau đó xóa đợt giảm giá
            dotGiamGiaRepository.delete(dotGiamGia);
        } else {
            throw new RuntimeException("Đợt giảm giá không tồn tại");
        }
    }


    private void setTrangThaiDotGiamGia(DotGiamGia dotGiamGia) {
        Date currentDate = new Date();
        if (dotGiamGia.getNgayBatDau() == null || dotGiamGia.getNgayKetThuc() == null) {
            throw new RuntimeException("Ngày bắt đầu hoặc ngày kết thúc không được để trống");
        } else if (dotGiamGia.getNgayBatDau().after(currentDate)) {
            TrangThaiGiamGia status = trangThaiGiamGiaRepository.findById(4).orElseThrow();
            dotGiamGia.setTrangThaiGiamGia(status);
        } else if (dotGiamGia.getNgayKetThuc().before(currentDate)) {
            TrangThaiGiamGia status = trangThaiGiamGiaRepository.findById(3).orElseThrow();
            dotGiamGia.setTrangThaiGiamGia(status);
        } else {
            TrangThaiGiamGia status = trangThaiGiamGiaRepository.findById(1).orElseThrow();
            dotGiamGia.setTrangThaiGiamGia(status);
        }
    }

    public void addSanPhamToDotGiamGia(Integer idDotGiamGia, GiamGiaSanPham giamGiaSanPham) {
        Optional<DotGiamGia> dotGiamGiaOptional = dotGiamGiaRepository.findById(idDotGiamGia);
        if (dotGiamGiaOptional.isPresent()) {
            Optional<SanPham> sanPhamOptional = sanPhamRepository.findById(giamGiaSanPham.getSanPham().getIdSanPham());
            if (sanPhamOptional.isPresent()) {
                SanPham sanPham = sanPhamOptional.get();
                giamGiaSanPham.setSanPham(sanPham);
                giamGiaSanPham.setDotGiamGia(dotGiamGiaOptional.get());
                giamGiaSanPham.setNgayTao(new Date());
                giamGiaSanPham.setNgayCapNhat(new Date());

                // Tính toán giá khuyến mãi dựa trên kiểu giảm giá
                BigDecimal giaTriGiamGia = dotGiamGiaOptional.get().getGiaTriGiamGia();
                BigDecimal giaBan = sanPham.getGiaBan(); // Giả sử có trường này trong SanPham
                BigDecimal giaKhuyenMai;

                // Kiểm tra kiểu giảm giá
                if (dotGiamGiaOptional.get().getKieuGiamGia()) { // Giảm giá theo tiền
                    giaKhuyenMai = giaBan.subtract(giaTriGiamGia);
                } else { // Giảm giá theo phần trăm
                    giaKhuyenMai = giaBan.subtract(giaBan.multiply(giaTriGiamGia).divide(BigDecimal.valueOf(100)));
                }

                // Đảm bảo giá khuyến mãi không âm
                if (giaKhuyenMai.compareTo(BigDecimal.ZERO) < 0) {
                    giaKhuyenMai = BigDecimal.ZERO;
                }

                giamGiaSanPham.setGiaKhuyenMai(giaKhuyenMai);

                giamGiaSanPhamRepository.save(giamGiaSanPham);
            } else {
                throw new RuntimeException("Sản phẩm không tồn tại");
            }
        } else {
            throw new RuntimeException("Đợt giảm giá không tồn tại");
        }
    }

    private BigDecimal calculateDiscountedPrice(BigDecimal originalPrice, BigDecimal discountValue) {
        if (discountValue.compareTo(BigDecimal.ZERO) > 0) {
            return originalPrice.subtract(discountValue);
        }
        return originalPrice; // Không có giảm giá, trả về giá gốc
    }

}
