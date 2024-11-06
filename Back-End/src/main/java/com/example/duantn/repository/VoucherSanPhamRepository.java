package com.example.duantn.repository;

import com.example.duantn.entity.Voucher;
import com.example.duantn.entity.VoucherSanPham;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherSanPhamRepository extends JpaRepository<VoucherSanPham, Integer> {

}
