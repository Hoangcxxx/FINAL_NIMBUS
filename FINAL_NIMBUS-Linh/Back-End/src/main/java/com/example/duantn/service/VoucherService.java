package com.example.duantn.service;

import com.example.duantn.entity.Voucher;
import com.example.duantn.repository.VoucherRepository; // Giả sử bạn đã tạo VoucherRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    public Voucher useVoucher(String maVoucher, BigDecimal tongTien) {
        // Lấy voucher từ repository
        Voucher voucher = voucherRepository.findByMaVoucher(maVoucher);

        // Kiểm tra nếu voucher không tồn tại
        if (voucher == null) {
            throw new IllegalArgumentException("Voucher không tồn tại.");
        }
        if (voucher.getTrangThaiGiamGia() == null || voucher.getTrangThaiGiamGia().getIdTrangThaiGiamGia() != 1) {
            throw new IllegalArgumentException("Voucher không thể sử dụng vì không đang phát hành.");
        }
        // Kiểm tra số lượng
        if (voucher.getSoLuong() <= 0) {
            throw new IllegalStateException("Voucher đã hết số lượng.");
        }

        // Kiểm tra loại voucher, chỉ áp dụng loại 1 hoặc loại 2
        if (voucher.getKieuGiamGia() == null ||
                (voucher.getKieuGiamGia() != true && voucher.getKieuGiamGia() != false)) {
            throw new IllegalArgumentException("Voucher không hợp lệ. Chỉ áp dụng voucher kiểu giảm giá 0 hoặc 1");
        }

        // Kiểm tra điều kiện ngày (voucher chưa hết hạn và chưa bắt đầu)
        if (voucher.getNgayKetThuc() != null && voucher.getNgayKetThuc().before(new java.util.Date())) {
            throw new IllegalArgumentException("Voucher đã hết hạn.");
        }

        if (voucher.getNgayBatDau() != null && voucher.getNgayBatDau().after(new java.util.Date())) {
            throw new IllegalArgumentException("Voucher chưa bắt đầu.");
        }

        System.out.println("Tổng tiền: " + tongTien);
        System.out.println("Số tiền tối thiểu của voucher: " + voucher.getSoTienToiThieu());
        System.out.println("Số tiền tối đa của voucher: " + voucher.getGiaTriToiDa());

        if (tongTien.compareTo(voucher.getSoTienToiThieu()) < 0) {
            throw new IllegalArgumentException("Tổng tiền không đạt mức tối thiểu để áp dụng voucher.");
        }

        if (voucher.getGiaTriToiDa() != null && tongTien.compareTo(voucher.getGiaTriToiDa()) > 0) {
            throw new IllegalArgumentException("Tổng tiền vượt quá mức tối đa để áp dụng voucher.");
        }

        // Nếu tất cả điều kiện đều thỏa mãn, giảm số lượng voucher
        voucher.setSoLuong(voucher.getSoLuong() - 1);
        voucherRepository.save(voucher);

        return voucher;
    }
    public Voucher apdungvoucher(String maVoucher, BigDecimal tongTien) {
        // Lấy voucher từ repository
        Voucher voucher = voucherRepository.findByMaVoucher(maVoucher);

        // Kiểm tra nếu voucher không tồn tại
        if (voucher == null) {
            throw new IllegalArgumentException("Voucher không tồn tại.");
        }
        if (voucher.getTrangThaiGiamGia() == null || voucher.getTrangThaiGiamGia().getIdTrangThaiGiamGia() != 1) {
            throw new IllegalArgumentException("Voucher không thể sử dụng vì không đang phát hành.");
        }
        // Kiểm tra số lượng
        if (voucher.getSoLuong() <= 0) {
            throw new IllegalStateException("Voucher đã hết số lượng.");
        }

        // Kiểm tra loại voucher, chỉ áp dụng loại 1 hoặc loại 2
        if (voucher.getKieuGiamGia() == null ||
                (voucher.getKieuGiamGia() != true && voucher.getKieuGiamGia() != false)) {
            throw new IllegalArgumentException("Voucher không hợp lệ. Chỉ áp dụng voucher kiểu giảm giá 0 hoặc 1");
        }

        // Kiểm tra điều kiện ngày (voucher chưa hết hạn và chưa bắt đầu)
        if (voucher.getNgayKetThuc() != null && voucher.getNgayKetThuc().before(new java.util.Date())) {
            throw new IllegalArgumentException("Voucher đã hết hạn.");
        }

        if (voucher.getNgayBatDau() != null && voucher.getNgayBatDau().after(new java.util.Date())) {
            throw new IllegalArgumentException("Voucher chưa bắt đầu.");
        }

        System.out.println("Tổng tiền: " + tongTien);
        System.out.println("Số tiền tối thiểu của voucher: " + voucher.getSoTienToiThieu());
        System.out.println("Số tiền tối đa của voucher: " + voucher.getGiaTriToiDa());

        if (tongTien.compareTo(voucher.getSoTienToiThieu()) < 0) {
            throw new IllegalArgumentException("Tổng tiền không đạt mức tối thiểu để áp dụng voucher.");
        }

        if (voucher.getGiaTriToiDa() != null && tongTien.compareTo(voucher.getGiaTriToiDa()) > 0) {
            throw new IllegalArgumentException("Tổng tiền vượt quá mức tối đa để áp dụng voucher.");
        }
        // Nếu tất cả điều kiện đều thỏa mãn, giảm số lượng voucher
        voucher.setSoLuong(voucher.getSoLuong() - 1);
        voucherRepository.save(voucher);
        return voucher;
    }

    public List<Voucher> getValidVouchers(BigDecimal tongTien) {
        return voucherRepository.findAll()
                .stream()
                .filter(voucher ->
                        voucher.getSoLuong() > 0 &&
                                (voucher.getKieuGiamGia() == true ||
                                        voucher.getKieuGiamGia() == false) &&
                                voucher.getSoTienToiThieu().compareTo(tongTien) <= 0 &&

                                (voucher.getGiaTriToiDa() == null || voucher.getGiaTriToiDa().compareTo(tongTien) >= 0) &&
                                // Kiểm tra voucher vẫn còn hiệu lực (chưa hết hạn)
                                !voucher.getNgayKetThuc().before(new java.util.Date()) &&
                                !voucher.getNgayBatDau().after(new java.util.Date()) &&
                                voucher.getTrangThaiGiamGia().getIdTrangThaiGiamGia() == 1 // Đang phát hành
                )
                .collect(Collectors.toList());
    }
    public List<Voucher> getAllVouchersWithStatus(BigDecimal tongTien) {
        // Lấy danh sách tất cả voucher từ database
        List<Voucher> allVouchers = voucherRepository.findAll();

        // Lọc và đánh dấu voucher có thể sử dụng được
        Date currentDate = new Date();

        return allVouchers.stream()
                .peek(voucher -> {
                    // Kiểm tra các điều kiện để voucher có thể sử dụng được hay không
                    boolean isUsable = voucher.getSoLuong() > 0 &&
                            (voucher.getKieuGiamGia() == true ||
                                    voucher.getKieuGiamGia() == false) &&
                            voucher.getSoTienToiThieu().compareTo(tongTien) <= 0 &&
                            (voucher.getGiaTriToiDa() == null || voucher.getGiaTriToiDa().compareTo(tongTien) >= 0) &&
                            !voucher.getNgayKetThuc().before(currentDate) &&
                            !voucher.getNgayBatDau().after(currentDate) &&
                            voucher.getTrangThaiGiamGia().getIdTrangThaiGiamGia() == 1; // Đang phát hành

                    // Gán trạng thái có thể sử dụng hay không vào thuộc tính `isUsable`
                    voucher.setIsUsable(isUsable);
                })
                .collect(Collectors.toList());
    }

}
