package com.example.duantn.service;

import com.example.duantn.dto.ThongKeDoanhThuDTO;
import com.example.duantn.dto.ThongKeTongDoanhThuDTO;
import com.example.duantn.query.ThongKeQuery;
import com.example.duantn.repository.HoaDonChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class ThongKeService {

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    public List<ThongKeDoanhThuDTO> thongKeDoanhThuSanPham() {
        return hoaDonChiTietRepository.thongKeDoanhThuSanPham();
    }
    public ThongKeTongDoanhThuDTO thongKeTongDoanhThu() {
        return hoaDonChiTietRepository.thongKeTongDoanhThu();
    }
    public List<ThongKeDoanhThuDTO> thongKeDoanhThuTheoNgay(Date fromDate, Date toDate) {
        return hoaDonChiTietRepository.thongKeDoanhThuTheoNgay(fromDate, toDate);
    }
    public List<ThongKeDoanhThuDTO> thongKeDoanhThuTheoDanhMuc(String tenDanhMuc) {
        return hoaDonChiTietRepository.thongKeDoanhThuTheoDanhMuc(tenDanhMuc);
    }
}
