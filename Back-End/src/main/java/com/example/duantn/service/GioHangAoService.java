package com.example.duantn.service;

import com.example.duantn.dto.ProductInCart;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class GioHangAoService {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    private VaiTroRepository vaiTroRepository; // Inject VaiTroRepository để truy vấn VaiTro

    // Thêm nhiều sản phẩm vào giỏ hàng
    public GioHang addProductsToCart(Integer idNguoiDung, List<ProductInCart> products) {
        // Kiểm tra người dùng có tồn tại không
        NguoiDung nguoiDung;
        if (idNguoiDung == null || !nguoiDungRepository.existsById(idNguoiDung)) {
            // Người dùng không tồn tại, tạo người dùng mới
            nguoiDung = createNewUser();
        } else {
            // Nếu người dùng đã tồn tại, lấy thông tin người dùng
            nguoiDung = nguoiDungRepository.findById(idNguoiDung)
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        }

        // 2. Tạo giỏ hàng mới
        GioHang gioHang = new GioHang();
        gioHang.setNguoiDung(nguoiDung);
        gioHang.setTrangThai(true);  // Trạng thái giỏ hàng có thể thay đổi theo yêu cầu
        gioHang.setNgayTao(new Date());
        gioHang.setNgayCapNhat(new Date());

        // Lưu giỏ hàng vào cơ sở dữ liệu
        GioHang savedGioHang = gioHangRepository.save(gioHang);

        // 3. Thêm các sản phẩm vào giỏ hàng
        for (ProductInCart product : products) {
            if (product.getIdSanPhamChiTiet() == null) {
                throw new RuntimeException("ID Sản phẩm chi tiết không thể null");
            }

            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(product.getIdSanPhamChiTiet())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm chi tiết không tồn tại"));

            GioHangChiTiet gioHangChiTiet = new GioHangChiTiet();
            gioHangChiTiet.setGioHang(savedGioHang);
            gioHangChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            gioHangChiTiet.setSoLuong(product.getSoLuong());
            gioHangChiTiet.setThanhTien(sanPhamChiTiet.getSanPham().getGiaBan().multiply(BigDecimal.valueOf(product.getSoLuong())));  // Cập nhật giá từ sản phẩm chi tiết
            gioHangChiTiet.setNgayTao(new Date());
            gioHangChiTiet.setNgayCapNhat(new Date());

            // Lưu sản phẩm vào giỏ hàng chi tiết
            gioHangChiTietRepository.save(gioHangChiTiet);
        }

        return savedGioHang;
    }

    // Tạo người dùng mới với mã người dùng ngẫu nhiên
    private NguoiDung createNewUser() {
        long currentCount = nguoiDungRepository.count();
        String generatedMaNguoiDung = "ND00" + (currentCount + 1);
        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setMaNguoiDung(generatedMaNguoiDung);  // Mã người dùng ngẫu nhiên
        VaiTro vaiTro = getVaiTroById(3);
        nguoiDung.setVaiTro(vaiTro);   // Gán vai trò cho người dùng
        nguoiDung.setNgayTao(new Date());  // Ngày tạo
        nguoiDung.setNgayCapNhat(new Date());  // Ngày cập nhật

        // In thông tin vai trò ra log để kiểm tra
        System.out.println("Vai trò người dùng: " + vaiTro);

        // Lưu người dùng vào cơ sở dữ liệu
        return nguoiDungRepository.save(nguoiDung);
    }

    // Phương thức lấy VaiTro từ id
    private VaiTro getVaiTroById(Integer id) {
        return vaiTroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vai trò không tồn tại"));
    }



}
