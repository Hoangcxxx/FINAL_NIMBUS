package com.example.duantn.repository;

import com.example.duantn.entity.TrangThaiGiamGia;
import com.example.duantn.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    // Phương thức tìm kiếm theo trạng thái giảm giá
    List<Voucher> findByTrangThaiGiamGia(TrangThaiGiamGia trangThaiGiamGia);
    Voucher findByMaVoucher(String maVoucher);
}

