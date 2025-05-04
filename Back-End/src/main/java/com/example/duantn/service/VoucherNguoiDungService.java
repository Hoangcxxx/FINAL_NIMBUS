package com.example.duantn.service;

import com.example.duantn.entity.NguoiDung;
import com.example.duantn.entity.Voucher;
import com.example.duantn.entity.VoucherNguoiDung;
import com.example.duantn.repository.VoucherNguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoucherNguoiDungService {

    @Autowired
    private VoucherNguoiDungRepository repository;

    private static final int MAX_USE_COUNT = 5; // Giới hạn số lần sử dụng voucher

    public Integer countVoucherByUser(Integer voucherId, Integer nguoiDungId) {
        Voucher voucher = new Voucher();
        voucher.setIdVoucher(voucherId);

        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setIdNguoiDung(nguoiDungId);

        // Lấy số lần voucher này đã được sử dụng bởi người dùng
        Integer count = repository.countByVoucherAndNguoiDung(voucher, nguoiDung);

        // Trả về số lần sử dụng nếu chưa vượt quá giới hạn
        return count;
    }

    public boolean isVoucherEligibleForUse(Integer voucherId, Integer nguoiDungId) {
        Integer count = countVoucherByUser(voucherId, nguoiDungId);
        return count < MAX_USE_COUNT;
    }
}
