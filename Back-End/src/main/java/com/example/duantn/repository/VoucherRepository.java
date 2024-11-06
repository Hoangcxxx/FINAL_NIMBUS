package com.example.duantn.repository;

import com.example.duantn.entity.Voucher;
import com.example.duantn.query.SanPhamQuery;
import com.example.duantn.query.VoucherQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    @Query(value = VoucherQuery.GET_VOUCHER, nativeQuery = true)
    List<Object[]> getAllVoucher();

}

