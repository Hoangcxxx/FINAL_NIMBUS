package com.example.duantn.repository;

import com.example.duantn.entity.VaiTro;
import com.example.duantn.entity.Voucher;
import com.example.duantn.entity.VoucherNguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoucherNguoiDungRepository extends JpaRepository<VoucherNguoiDung, Integer> {
    // Thêm phương thức tìm kiếm theo voucher
    List<VoucherNguoiDung> findAllByVoucher(Voucher voucher);
    @Query("SELECT CASE WHEN COUNT(vn) > 0 THEN TRUE ELSE FALSE END " +
            "FROM VoucherNguoiDung vn " +
            "WHERE vn.voucher.idVoucher = :idVoucher AND vn.nguoiDung.idNguoiDung = :idNguoiDung")
    boolean existsByIdVoucherAndIdNguoiDung(@Param("idVoucher") Integer idVoucher,
                                            @Param("idNguoiDung") Integer idNguoiDung);
}
