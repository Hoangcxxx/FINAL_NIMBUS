package com.example.duantn.service;

import com.example.duantn.entity.PtThanhToanHoaDon;
import com.example.duantn.repository.PhuongThucThanhToanHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PtThanhToanHoaDonService {

    @Autowired
    private PhuongThucThanhToanHoaDonRepository ptThanhToanHoaDonRepository;

    public PtThanhToanHoaDon createPaymentMethod(PtThanhToanHoaDon paymentMethod) {
        return ptThanhToanHoaDonRepository.save(paymentMethod);
    }
}
