package com.example.duantn.service;

import com.example.duantn.repository.HoaDonChiTietRepository;
import com.example.duantn.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HoaDonService {
    @Autowired
    private HoaDonRepository hoaDonRepository;
    public List<Object[]> getAllHoaDon() {
        return hoaDonRepository.getAllHoaDon();
    }

}
