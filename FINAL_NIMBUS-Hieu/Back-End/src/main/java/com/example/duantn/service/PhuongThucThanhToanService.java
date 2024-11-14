package com.example.duantn.service;

import com.example.duantn.DTO.PhuongThucThanhToanDTO;
import com.example.duantn.entity.PhuongThucThanhToan;
import com.example.duantn.repository.PhuongThucThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhuongThucThanhToanService {

    @Autowired
    private PhuongThucThanhToanRepository phuongThucThanhToanRepository;
    public List<PhuongThucThanhToanDTO> getAllTenPhuongThucThanhToan() {
        List<PhuongThucThanhToan> danhSachPhuongThuc = phuongThucThanhToanRepository.findAll();
        return danhSachPhuongThuc.stream()
                .map(pt -> new PhuongThucThanhToanDTO(pt.getIdPtThanhToan(), pt.getTenPhuongThuc()))
                .collect(Collectors.toList());
    }
    public PhuongThucThanhToan findById(Integer id) {
        return phuongThucThanhToanRepository.findById(id).orElse(null);
    }
}
