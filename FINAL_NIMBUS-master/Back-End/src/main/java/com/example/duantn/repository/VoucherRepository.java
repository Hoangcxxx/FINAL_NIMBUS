package com.example.duantn.repository;

import com.example.duantn.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    Optional<Voucher> findByMaVoucher(String maVoucher);
    List<Voucher> findBySoTienToiThieuLessThanEqualAndGiaTriToiDaGreaterThanEqualAndTrangThaiAndNgayKetThucAfter(
            BigDecimal soTienToiThieu, BigDecimal giaTriToiDa, boolean trangThai, Date ngayKetThuc);

}
