package com.example.duantn.repository;

import com.example.duantn.entity.VaiTro;
import com.example.duantn.entity.Voucher;
import com.example.duantn.entity.VoucherNguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoucherNguoiDungRepository extends JpaRepository<VoucherNguoiDung, Integer> {
    // Thêm phương thức tìm kiếm theo voucher
    List<VoucherNguoiDung> findAllByVoucher(Voucher voucher);
}
