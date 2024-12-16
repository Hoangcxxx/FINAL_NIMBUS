package com.example.duantn.service;

import com.example.duantn.entity.PhuongThucThanhToanHoaDon;
import com.example.duantn.repository.PhuongThucThanhToanHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PtThanhToanHoaDonService {

    @Autowired
    private PhuongThucThanhToanHoaDonRepository ptThanhToanHoaDonRepository;

    public PhuongThucThanhToanHoaDon createPaymentMethod(PhuongThucThanhToanHoaDon paymentMethod) {
        return ptThanhToanHoaDonRepository.save(paymentMethod);
    }
}
