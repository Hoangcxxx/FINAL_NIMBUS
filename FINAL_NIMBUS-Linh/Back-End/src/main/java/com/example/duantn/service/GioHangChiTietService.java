package com.example.duantn.service;

import com.example.duantn.entity.GioHangChiTiet;
import com.example.duantn.repository.GioHangChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GioHangChiTietService {
    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;

    public Optional<GioHangChiTiet> findById(Integer id) {
        System.out.println("Tìm kiếm chi tiết giỏ hàng với ID: " + id);
        return gioHangChiTietRepository.findById(id);
    }
    public void updateQuantity(int idGioHangChiTiet, int newQuantity) {
        Optional<GioHangChiTiet> optionalGioHangChiTiet = gioHangChiTietRepository.findById(idGioHangChiTiet);

        if (optionalGioHangChiTiet.isPresent()) {
            GioHangChiTiet gioHangChiTiet = optionalGioHangChiTiet.get();
            gioHangChiTiet.setSoLuong(newQuantity);
            gioHangChiTietRepository.save(gioHangChiTiet);
            System.out.println("Cập nhật số lượng cho chi tiết giỏ hàng ID: " + idGioHangChiTiet + " thành " + newQuantity);
        } else {
            System.out.println("Không tìm thấy chi tiết giỏ hàng với ID: " + idGioHangChiTiet);
        }
    }
    public void deleteGioHangChiTietByUserId(Long userId) {
        gioHangChiTietRepository.deleteByUserId(userId);
    }
}
