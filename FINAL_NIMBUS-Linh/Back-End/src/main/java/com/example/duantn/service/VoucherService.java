package com.example.duantn.service;

import com.example.duantn.entity.Voucher;
import com.example.duantn.repository.VoucherRepository; // Giả sử bạn đã tạo VoucherRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    public boolean applyVoucher(String maVoucher) {
        Optional<Voucher> optionalVoucher = voucherRepository.findByMaVoucher(maVoucher);
        if (optionalVoucher.isPresent()) {
            Voucher voucher = optionalVoucher.get();

            // Kiểm tra số lượng voucher
            if (voucher.getSo_luong() > 0) {

                // Kiểm tra ngày bắt đầu và ngày kết thúc
                LocalDate currentDate = LocalDate.now();
                LocalDate startDate = voucher.getNgay_bat_dau().toLocalDate();
                LocalDate endDate = voucher.getNgayKetThuc().toLocalDate();

                if (!currentDate.isBefore(startDate) && !currentDate.isAfter(endDate)) {
                    // Trừ số lượng voucher
                    voucher.setSo_luong(voucher.getSo_luong() - 1);
                    voucherRepository.save(voucher); // Lưu voucher đã cập nhật
                    return true; // Voucher hợp lệ và đã được áp dụng
                }
            }
        }
        return false; // Voucher không hợp lệ, đã hết hàng, hoặc không còn hiệu lực
    }

    public Optional<Voucher> timVoucherTuongUng(BigDecimal tongTien) {
        // Tìm các voucher thỏa mãn điều kiện về tổng tiền tối thiểu, giá trị tối đa và trạng thái còn hiệu lực
        List<Voucher> vouchers = voucherRepository.findBySoTienToiThieuLessThanEqualAndGiaTriToiDaGreaterThanEqualAndTrangThaiAndNgayKetThucAfter(
                tongTien, tongTien, true, new Date(System.currentTimeMillis()));

        // Lọc voucher có giá trị giảm cao nhất trong các voucher hợp lệ
        return vouchers.stream()
                .max(Comparator.comparing(Voucher::getGia_tri_giam_gia));
    }
}
