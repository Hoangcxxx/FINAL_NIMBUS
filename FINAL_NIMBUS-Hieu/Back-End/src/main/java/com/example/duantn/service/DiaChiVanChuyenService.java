package com.example.duantn.service;

import com.example.duantn.DTO.DiaChiVanChuyenDTO;
import com.example.duantn.entity.DiaChiVanChuyen;
import com.example.duantn.repository.DiaChiVanChuyenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiaChiVanChuyenService {
    @Autowired
    private DiaChiVanChuyenRepository diaChiVanChuyenRepository;

    public List<DiaChiVanChuyenDTO> getAllDiaChiVanChuyen() {
        List<DiaChiVanChuyen> addresses = diaChiVanChuyenRepository.findAll();
        return addresses.stream()
                .map(address -> new DiaChiVanChuyenDTO(
                        address.getIdDiaChiVanChuyen(),
                        address.getTinh(),
                        address.getHuyen(),
                        address.getXa(),
                        address.getSoTienVanChuyen(),  // Lấy giá trị soTienVanChuyen
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
//
//    public List<DiaChiVanChuyen> getDiaChiVanChuyen( String tinh) {
//        return diaChiVanChuyenRepository.findBySoTienVanChuyenAndTinh(tinh);
//    }

    // Lấy địa chỉ theo xã
    public List<DiaChiVanChuyenDTO> getDiaChiByXa(String xa) {
        List<DiaChiVanChuyen> addresses = diaChiVanChuyenRepository.findByXa(xa);
        return convertToDTOs(addresses);
    }

    // Lấy địa chỉ theo id
    public DiaChiVanChuyenDTO getDiaChiById(Integer idDiaChiVanChuyen) {
        DiaChiVanChuyen address = diaChiVanChuyenRepository.findById(idDiaChiVanChuyen)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));
        return new DiaChiVanChuyenDTO(
                address.getIdDiaChiVanChuyen(),
                address.getTinh(),
                address.getHuyen(),
                address.getXa(),
                address.getSoTienVanChuyen(),
                address.getMoTa()
        );
    }

    // Lấy phí ship dựa trên tỉnh, huyện và xã
    public BigDecimal getShippingFee(String tinh, String huyen, String xa) {
        DiaChiVanChuyen address = diaChiVanChuyenRepository.findByTinhAndHuyenAndXa(tinh, huyen, xa)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));
        return address.getSoTienVanChuyen(); // Lấy phí ship từ trường soTienVanChuyen
    }

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
