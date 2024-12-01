package com.example.duantn.service;

import com.example.duantn.dto.*;
import com.example.duantn.entity.GioHang;
import com.example.duantn.entity.GioHangChiTiet;
import com.example.duantn.entity.SanPhamChiTiet;
import com.example.duantn.repository.GioHangChiTietRepository;
import com.example.duantn.repository.GioHangRepository;
import com.example.duantn.repository.SanPhamChiTietRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GIoHangService {
    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    public void themSanPhamVaoGioHang(Integer idNguoiDung, GioHangRequest request) {
        GioHang gioHang = gioHangRepository.findByIdNguoiDung(idNguoiDung);
        if (gioHang == null) {
            gioHang = new GioHang();
            gioHang.setIdNguoiDung(idNguoiDung);
            gioHang.setTrangThai(true);
            gioHang.setNgayTao(new Date());
            gioHang.setNgayCapNhat(new Date());
            gioHangRepository.save(gioHang);
        }
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(request.getIdSanPhamChiTiet())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
        if (request.getSoLuong() > sanPhamChiTiet.getSoLuong()) {
            throw new RuntimeException("Số lượng yêu cầu vượt quá số lượng sản phẩm có sẵn.");
        }
        List<GioHangChiTiet> danhSachGioHangChiTiet = gioHangChiTietRepository.findByGioHang(gioHang);
        GioHangChiTiet gioHangChiTiet = danhSachGioHangChiTiet.stream()
                .filter(item -> item.getSanPhamChiTiet().equals(sanPhamChiTiet))
                .findFirst()
                .orElse(null);
        if (gioHangChiTiet != null) {
            gioHangChiTiet.setSoLuong(gioHangChiTiet.getSoLuong() + request.getSoLuong());
            gioHangChiTiet.setThanhTien(gioHangChiTiet.getDonGia() * gioHangChiTiet.getSoLuong());
            gioHangChiTiet.setNgayCapNhat(new Date());
        } else {
            gioHangChiTiet = new GioHangChiTiet();
            gioHangChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            gioHangChiTiet.setGioHang(gioHang);
            gioHangChiTiet.setSoLuong(request.getSoLuong());
            gioHangChiTiet.setDonGia(sanPhamChiTiet.getSanPham().getGiaBan().doubleValue());
            gioHangChiTiet.setThanhTien(sanPhamChiTiet.getSanPham().getGiaBan().doubleValue() * request.getSoLuong());
            gioHangChiTiet.setTrangThai(true);
            gioHangChiTiet.setNgayTao(new Date());
            gioHangChiTiet.setNgayCapNhat(new Date());
        }
        sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - request.getSoLuong());

        sanPhamChiTietRepository.save(sanPhamChiTiet);
        gioHangChiTietRepository.save(gioHangChiTiet);
    }
    public List<GioHangResponse> layDanhSachSanPhamTrongGioHang(Integer idNguoiDung) {
        GioHang gioHang = gioHangRepository.findByIdNguoiDung(idNguoiDung);
        if (gioHang == null) {
            return List.of();
        }
        return gioHang.getGioHangChiTietList().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private GioHangResponse convertToResponse(GioHangChiTiet chiTiet) {
        GioHangResponse response = new GioHangResponse();
        response.setIdSanPhamChiTiet(chiTiet.getSanPhamChiTiet().getIdSanPhamChiTiet());
        response.setTenSanPham(chiTiet.getSanPhamChiTiet().getSanPham().getTenSanPham());
        response.setMauSac(chiTiet.getSanPhamChiTiet().getMauSacChiTiet().getMauSac().getTenMauSac());
        response.setKichThuoc(chiTiet.getSanPhamChiTiet().getKichThuocChiTiet().getKichThuoc().getTenKichThuoc());
        response.setSoLuong(chiTiet.getSoLuong());
        response.setGiaBan(BigDecimal.valueOf(chiTiet.getDonGia()));
        String urlAnh = chiTiet.getSanPhamChiTiet().getSanPham().getUrlAnh();
        response.setUrlAnh(urlAnh);
        return response;
    }
    @Transactional
    public void xoaSanPhamKhoiGioHang(Integer idNguoiDung, Integer idSanPhamChiTiet) {
        GioHang gioHang = gioHangRepository.findByIdNguoiDung(idNguoiDung);
        if (gioHang == null) {
            throw new RuntimeException("Giỏ hàng không tồn tại.");
        }
        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietRepository.findByGioHang(gioHang);
        GioHangChiTiet gioHangChiTietToRemove = gioHangChiTietList.stream()
                .filter(chiTiet -> chiTiet.getSanPhamChiTiet().getIdSanPhamChiTiet().equals(idSanPhamChiTiet))
                .findFirst()
                .orElse(null);

        if (gioHangChiTietToRemove != null) {
            Optional<SanPhamChiTiet> optionalSanPhamChiTiet = sanPhamChiTietRepository.findById(idSanPhamChiTiet);
            if (optionalSanPhamChiTiet.isPresent()) {
                SanPhamChiTiet sanPhamChiTiet = optionalSanPhamChiTiet.get();
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + gioHangChiTietToRemove.getSoLuong());
                sanPhamChiTietRepository.save(sanPhamChiTiet);
            }
            gioHangChiTietRepository.delete(gioHangChiTietToRemove);
        } else {
            throw new RuntimeException("Sản phẩm không tồn tại trong giỏ hàng.");
        }
    }
    @Transactional
    public void xoaTatCaSanPhamKhoiGioHangKhongCapNhatSoLuong(Integer idNguoiDung) {
        // Tìm giỏ hàng của người dùng
        GioHang gioHang = gioHangRepository.findByIdNguoiDung(idNguoiDung);
        if (gioHang == null) {
            throw new RuntimeException("Giỏ hàng không tồn tại.");
        }

        // Lấy danh sách tất cả chi tiết giỏ hàng
        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietRepository.findByGioHang(gioHang);

        // Xóa từng chi tiết giỏ hàng mà không cập nhật lại số lượng sản phẩm trong kho
        gioHangChiTietRepository.deleteAll(gioHangChiTietList);

        // Xóa giỏ hàng sau khi đã xóa tất cả chi tiết
        gioHangRepository.delete(gioHang);
    }

    @Transactional
    public void capNhatSoLuong(Integer idNguoiDung, Integer idSanPhamChiTiet, Integer soLuong) {
        GioHang gioHang = gioHangRepository.findByIdNguoiDung(idNguoiDung);
        if (gioHang == null) {
            throw new RuntimeException("Giỏ hàng không tồn tại.");
        }

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(idSanPhamChiTiet)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại."));

        Optional<GioHangChiTiet> gioHangChiTietOptional = gioHangChiTietRepository.findByGioHangAndSanPhamChiTiet(gioHang, sanPhamChiTiet);

        if (gioHangChiTietOptional.isPresent()) {
            GioHangChiTiet gioHangChiTiet = gioHangChiTietOptional.get();
            if (sanPhamChiTiet.getSoLuong() + gioHangChiTiet.getSoLuong() < soLuong) {
                throw new RuntimeException("Số lượng yêu cầu vượt quá số lượng sản phẩm có sẵn.");
            }
            int oldQuantity = gioHangChiTiet.getSoLuong();
            gioHangChiTiet.setSoLuong(soLuong);
            gioHangChiTiet.setThanhTien(sanPhamChiTiet.getSanPham().getGiaBan().doubleValue() * soLuong);
            gioHangChiTiet.setNgayCapNhat(new Date());
            gioHangChiTietRepository.save(gioHangChiTiet);
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + oldQuantity - soLuong);
            sanPhamChiTietRepository.save(sanPhamChiTiet);
        } else {
            throw new RuntimeException("Chi tiết giỏ hàng không tồn tại.");
        }
    }


}
