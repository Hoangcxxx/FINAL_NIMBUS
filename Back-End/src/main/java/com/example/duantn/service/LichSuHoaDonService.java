package com.example.duantn.service;

import com.example.duantn.entity.LichSuHoaDon;
import com.example.duantn.repository.LichSuHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LichSuHoaDonService {
    @Autowired
    private  LichSuHoaDonRepository lichSuHoaDonRepository;

    public List<LichSuHoaDon> getLichSuHoaDonByNguoiDungId(Integer nguoiDungId) {
        return lichSuHoaDonRepository.findByNguoiDung_IdNguoiDungOrderByIdLichSuHoaDon(nguoiDungId);
    }
}
