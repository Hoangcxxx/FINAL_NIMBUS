package com.example.duantn.repository;

import com.example.duantn.entity.TrangThaiGiamGia;
import com.example.duantn.entity.Voucher;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    // Phương thức tìm kiếm theo trạng thái giảm giá
    List<Voucher> findByTrangThaiGiamGia(TrangThaiGiamGia trangThaiGiamGia);
    Voucher findByMaVoucher(String maVoucher);
    List<Voucher> findByKieuGiamGia(Boolean kieuGiamGia);
    // Thêm phương thức findAll với Sort theo ngày tạo
    List<Voucher> findAll(Sort sort);

    // Tìm kiếm voucher theo tên voucher (sử dụng LIKE)
    @Query("SELECT v FROM Voucher v WHERE v.tenVoucher LIKE %?1%")
    List<Voucher> findByTenVoucherContaining(String tenVoucher);

}

