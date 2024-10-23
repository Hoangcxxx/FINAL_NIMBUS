//package com.example.duantn.service;
//
//
//
//import com.example.duantn.dto.GioHangChiTietDTO;
//import com.example.duantn.repository.GioHangChiTietRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class GioHangChiTietService {
//
//    @Autowired
//    private GioHangChiTietRepository gioHangChiTietRepository;
//
//    public List<GioHangChiTietDTO> getGioHangChiTietWithSanPham(Integer idGioHang) {
//        return gioHangChiTietRepository.findByGioHang_IdGioHangAndSanPhamChiTiet_IdSanPhamChiTiet(idGioHang);
//    }
//}
