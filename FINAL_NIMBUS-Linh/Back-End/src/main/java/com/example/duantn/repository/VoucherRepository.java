package com.example.duantn.repository;

import com.example.duantn.entity.TrangThaiGiamGia;
import com.example.duantn.entity.Voucher;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    // Phương thức tìm kiếm theo trạng thái giảm giá
    @Query("SELECT v FROM Voucher v WHERE v.maVoucher LIKE %?1%")
    Voucher findByMaVoucher(String maVoucher);
    @Query("SELECT v FROM Voucher v WHERE v.maVoucher LIKE :maVoucher%")
    List<Voucher> timTheoMa(String maVoucher);
    List<Voucher> findByKieuGiamGia(Boolean kieuGiamGia);
    // Thêm phương thức findAll với Sort theo ngày tạo
    List<Voucher> findAll(Sort sort);
    List<Voucher> findByMaVoucherContaining(String maVoucher);
    List<Voucher> findByTrangThaiGiamGia(TrangThaiGiamGia trangThaiGiamGia);
    List<Voucher> findByTenVoucherContaining(String tenVoucher);
    List<Voucher> findByMaVoucherContainingAndTrangThaiGiamGiaAndKieuGiamGia(
            String maVoucher, TrangThaiGiamGia trangThaiGiamGia, Boolean kieuGiamGia);

    // Tìm kiếm theo mã voucher và trạng thái
    List<Voucher> findByMaVoucherContainingAndTrangThaiGiamGia(
            String maVoucher, TrangThaiGiamGia trangThaiGiamGia);

    // Tìm kiếm theo mã voucher và kiểu giảm giá
    List<Voucher> findByMaVoucherContainingAndKieuGiamGia(
            String maVoucher, Boolean kieuGiamGia);

    // Tìm kiếm theo trạng thái và kiểu giảm giá
    List<Voucher> findByTrangThaiGiamGiaAndKieuGiamGia(
            TrangThaiGiamGia trangThaiGiamGia, Boolean kieuGiamGia);


}

