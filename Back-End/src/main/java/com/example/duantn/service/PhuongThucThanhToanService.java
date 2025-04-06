package com.example.duantn.service;

import com.example.duantn.dto.PhuongThucThanhToanDTO;
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

        // Lọc danh sách phương thức thanh toán để chỉ lấy các id là 1, 2, 3
        return danhSachPhuongThuc.stream()
                .filter(pt -> pt.getIdPTThanhToan() == 1 || pt.getIdPTThanhToan() == 3)
                .map(pt -> new PhuongThucThanhToanDTO(pt.getIdPTThanhToan(), pt.getTenPhuongThuc()))
                .collect(Collectors.toList());
    }

    public PhuongThucThanhToan findById(Integer id) {
        return phuongThucThanhToanRepository.findById(id).orElse(null);
    }
}
