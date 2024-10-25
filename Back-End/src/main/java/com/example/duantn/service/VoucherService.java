package com.example.duantn.service;

import com.example.duantn.entity.Voucher;
import com.example.duantn.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    public List<Object[]> getAllVouchers() {
        return voucherRepository.getAllVoucher();
    }
    public Voucher addVoucher(Voucher voucher) {
        Date now = new Date();
        voucher.setNgayTao(now);
        voucher.setNgayCapNhat(now);
        System.out.println("Ngày tạo: " + voucher.getNgayTao());
        System.out.println("Ngày cập nhật: " + voucher.getNgayCapNhat());
        return voucherRepository.save(voucher);
    }
}

