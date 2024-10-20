package com.example.duantn.service;

import com.example.duantn.entity.KhoHang;
import com.example.duantn.repository.KhoHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KhoHangService {

    @Autowired
    private KhoHangRepository khoHangRepository;

    public List<Object[]> searchProducts(Integer idSanPham, Integer idChatLieu, Integer idMauSac, Integer idKichThuoc) {
        System.out.println("Searching products with params: "
                + "idSanPham=" + idSanPham + ", "
                + "idChatLieu=" + idChatLieu + ", "
                + "idMauSac=" + idMauSac + ", "
                + "idKichThuoc=" + idKichThuoc);
        return khoHangRepository.getSanPhamCTByAll(idSanPham, idChatLieu, idMauSac, idKichThuoc);
    }

    public List<Object[]> searchSanPham(Integer idDanhMuc) {
        System.out.println("Searching products with params: "
                + "idDanhMuc=" + idDanhMuc);
        return khoHangRepository.getSanPhamCTByDanhMuc(idDanhMuc);
    }
    public List<Object[]> searchChatLieu( Integer idSanPhamCT) {
        System.out.println("Searching products with params: "
                + "idSanPham=" + idSanPhamCT);
        return khoHangRepository.getChatLieuByIdSanPhamCT(idSanPhamCT);
    }
    public List<Object[]> searchMauSac(Integer idSanPham, Integer idChatLieu) {
        System.out.println("Searching products with params: "
                + "idSanPham=" + idSanPham + ", "
                + "idChatLieu=" + idChatLieu);
        return khoHangRepository.getMauSacByIdSanPhamCTAndIdChatLieu(idSanPham, idChatLieu);
    }
    public List<Object[]> searchKichThuoc(Integer idSanPham, Integer idChatLieu, Integer idMauSac) {
        System.out.println("Searching products with params: "
                + "idSanPham=" + idSanPham + ", "
                + "idChatLieu=" + idChatLieu + ", "
                + "idMauSac=" + idMauSac );
        return khoHangRepository.getKichThuocByIdSanPhamCTAndIdChatLieuAndMauSac(idSanPham, idChatLieu, idMauSac);
    }
    public void updateStock(Integer idSanPhamChiTiet, Integer soLuongThem) {
        System.out.println("Updating stock for ID: " + idSanPhamChiTiet + ", Quantity to add: " + soLuongThem);
        khoHangRepository.updateStock(idSanPhamChiTiet, soLuongThem);

        Integer currentQuantity = khoHangRepository.findQuantityById(idSanPhamChiTiet);
        System.out.println("Current stock after update: " + currentQuantity);
    }

    @Transactional
    public void toggleStatusByIdSanPhamCT(Integer idSanPhamCT) {
        khoHangRepository.updateStatusByIdSanPhamCT(idSanPhamCT);
    }

}
