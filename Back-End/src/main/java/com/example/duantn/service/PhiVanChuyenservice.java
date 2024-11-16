//package com.example.duantn.service;
//
//
//import com.example.duantn.DTO.HoaDonDTO;
//import com.example.duantn.DTO.PhiVanChuyenDTO;
//import com.example.duantn.entity.DiaChiVanChuyen;
//import com.example.duantn.entity.HoaDon;
//import com.example.duantn.entity.PhiVanChuyen;
//import com.example.duantn.repository.DiaChiVanChuyenRepository;
//import com.example.duantn.repository.HoaDonRepository;
//import com.example.duantn.repository.PhiVanChuyenRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//
//@Service
//public class PhiVanChuyenservice {
//    @Autowired
//    private PhiVanChuyenRepository phiVanChuyenRepository;
//
//
//    @Autowired
//    private DiaChiVanChuyenRepository chuyenRepository;
//
//
//    @Autowired
//    private HoaDonRepository hoaDonRepository;
//
//    public void Batphivanchuen(PhiVanChuyenDTO phiVanChuyenDTO, HoaDonDTO hoaDonDTO){
//        PhiVanChuyen phiVanChuyen = new PhiVanChuyen();
//        HoaDon hoaDon = new HoaDon();
//        DiaChiVanChuyen diaChiVanChuyen = chuyenRepository
//                .findByTinhAndHuyenAndXa(hoaDonDTO.getTinh(), hoaDonDTO.getHuyen(), hoaDonDTO.getXa())
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ vận chuyển"));
//        hoaDon.setDiaChiVanChuyen(diaChiVanChuyen);
//        hoaDon.setPhiShip(diaChiVanChuyen.getSoTienVanChuyen());
//        hoaDon.setNgayTao(new Date());
//
//    }
//}
