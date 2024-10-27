package com.example.duantn.service;

import com.example.duantn.entity.Voucher;
import com.example.duantn.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    public Optional<Voucher> getVoucherById(Integer id) {
        return voucherRepository.findById(id);
    }

    public Voucher createVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    public Voucher updateVoucher(Integer id, Voucher voucherDetails) {
        Voucher voucher = voucherRepository.findById(id).orElseThrow();
        voucher.setMaVoucher(voucherDetails.getMaVoucher());
        voucher.setGiaTriGiamGia(voucherDetails.getGiaTriGiamGia());
        voucher.setSoLuong(voucherDetails.getSoLuong());
        voucher.setGiaTriToiDa(voucherDetails.getGiaTriToiDa());
        voucher.setSoTienToiThieu(voucherDetails.getSoTienToiThieu());
        voucher.setTrangThai(voucherDetails.getTrangThai());
        voucher.setMoTa(voucherDetails.getMoTa());
        voucher.setNgayBatDau(voucherDetails.getNgayBatDau());
        voucher.setNgayKetThuc(voucherDetails.getNgayKetThuc());
        voucher.setNgayCapNhat(new Date());
        return voucherRepository.save(voucher);
    }

    public void deleteVoucher(Integer id) {
        voucherRepository.deleteById(id);
    }
}
