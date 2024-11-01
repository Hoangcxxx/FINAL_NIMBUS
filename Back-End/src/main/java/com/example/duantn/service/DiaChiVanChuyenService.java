package com.example.duantn.service;

import com.example.duantn.dto.DiaChiVanChuyenDTO;
import com.example.duantn.entity.DiaChiVanChuyen;
import com.example.duantn.repository.DiaChiVanChuyenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiaChiVanChuyenService {
    @Autowired
    private DiaChiVanChuyenRepository diaChiVanChuyenRepository;

    // Lấy tất cả địa chỉ
    public List<DiaChiVanChuyenDTO> getAllDiaChiVanChuyen() {
        List<DiaChiVanChuyen> addresses = diaChiVanChuyenRepository.findAll();
        return addresses.stream()
                .map(address -> new DiaChiVanChuyenDTO(
                        address.getIdDiaChiVanChuyen(),
                        address.getTinh(),
                        address.getHuyen(),
                        address.getXa(),
                        address.getSoTienVanChuyen(),
                        address.getMoTa()
                ))
                .collect(Collectors.toList());
    }

    // Lấy địa chỉ theo tỉnh
    public List<DiaChiVanChuyenDTO> getDiaChiByTinh(String tinh) {
        List<DiaChiVanChuyen> addresses = diaChiVanChuyenRepository.findByTinh(tinh);
        return convertToDTOs(addresses);
    }

    // Lấy địa chỉ theo huyện
    public List<DiaChiVanChuyenDTO> getDiaChiByHuyen(String huyen) {
        List<DiaChiVanChuyen> addresses = diaChiVanChuyenRepository.findByHuyen(huyen);
        return convertToDTOs(addresses);
    }

    // Lấy địa chỉ theo xã
    public List<DiaChiVanChuyenDTO> getDiaChiByXa(String xa) {
        List<DiaChiVanChuyen> addresses = diaChiVanChuyenRepository.findByXa(xa);
        return convertToDTOs(addresses);
    }

    // Phương thức hỗ trợ để chuyển đổi từ Entity sang DTO
    private List<DiaChiVanChuyenDTO> convertToDTOs(List<DiaChiVanChuyen> addresses) {
        return addresses.stream()
                .map(address -> new DiaChiVanChuyenDTO(
                        address.getIdDiaChiVanChuyen(),
                        address.getTinh(),
                        address.getHuyen(),
                        address.getXa(),
                        address.getSoTienVanChuyen(),
                        address.getMoTa()
                ))
                .collect(Collectors.toList());
    }
}

