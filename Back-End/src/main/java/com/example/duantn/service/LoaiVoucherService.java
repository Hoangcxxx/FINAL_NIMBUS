package com.example.duantn.service;

import com.example.duantn.entity.LoaiVoucher;
import com.example.duantn.repository.LoaiVoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LoaiVoucherService {
    @Autowired
    private LoaiVoucherRepository loaiVoucherRepository;

    public List<LoaiVoucher> getAllLoaiVouchers() {
        return loaiVoucherRepository.findAll();
    }

    public Optional<LoaiVoucher> getLoaiVoucherById(Integer id) {
        return loaiVoucherRepository.findById(id);
    }

    public LoaiVoucher createLoaiVoucher(LoaiVoucher loaiVoucher) {
        return loaiVoucherRepository.save(loaiVoucher);
    }

    public LoaiVoucher updateLoaiVoucher(Integer id, LoaiVoucher loaiVoucherDetails) {
        LoaiVoucher loaiVoucher = loaiVoucherRepository.findById(id).orElseThrow();
        loaiVoucher.setTenLoaiVoucher(loaiVoucherDetails.getTenLoaiVoucher());
        loaiVoucher.setMoTa(loaiVoucherDetails.getMoTa());
        loaiVoucher.setNgayCapNhat(new Date());
        loaiVoucher.setNgayTao(new Date());
        return loaiVoucherRepository.save(loaiVoucher);
    }

    public void deleteLoaiVoucher(Integer id) {
        loaiVoucherRepository.deleteById(id);
    }
}
