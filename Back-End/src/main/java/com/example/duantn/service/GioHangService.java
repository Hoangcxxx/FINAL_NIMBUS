package com.example.duantn.service;

import com.example.duantn.dto.GioHangChiTietDTO;
import com.example.duantn.dto.SanPhamChiTietDTO;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GioHangService {

    private static final Logger log = LoggerFactory.getLogger(GioHangService.class);
    @Autowired
    private GioHangRepository gioHangRepository;
    @Autowired
    private LoaiThongBaoRepository loaiThongBaoRepository;
    @Autowired
    private ThongBaoRepository thongBaoRepository;
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    private SanPhamRepository sanPhamRepository;
    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    public GioHang addGioHang(Integer idUser, GioHangChiTietDTO gioHangChiTietDTO) {
        GioHang gioHang = gioHangRepository.findByNguoiDung_IdNguoiDungOrderByIdGioHang(idUser);

        if (gioHang == null) {
            gioHang = new GioHang();


            NguoiDung nguoiDung = nguoiDungRepository.findById(idUser)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + idUser));

            gioHang.setNguoiDung(nguoiDung); // Gán đối tượng NguoiDung vào giỏ hàng
            gioHang.setTrangThai(true);
            gioHang.setNgayTao(new Date());
            gioHang.setNgayCapNhat(new Date());
            gioHang = gioHangRepository.save(gioHang);
        }

        // Phần còn lại của mã không thay đổi
        Optional<SanPhamChiTiet> sanPhamChiTiet = sanPhamChiTietRepository.findByAttributes(
                gioHangChiTietDTO.getIdMauSac(), gioHangChiTietDTO.getIdKichThuoc(), gioHangChiTietDTO.getIdChatLieu(),
                gioHangChiTietDTO.getIdSanPham());
        if (sanPhamChiTiet.isEmpty()) {
            throw new RuntimeException("Không có ID sản phẩm này: " + gioHangChiTietDTO.getIdSanPhamChiTiet());
        }
        GioHangChiTiet gioHangChiTiet = new GioHangChiTiet();
        gioHangChiTiet.setSanPhamChiTiet(sanPhamChiTiet.get());
        gioHangChiTiet.setGioHang(gioHang);
        gioHangChiTiet.setDonGia(sanPhamRepository.findById(gioHangChiTietDTO.getIdSanPham()).get().getGiaBan());
        gioHangChiTiet.setTrangThai(true);
        gioHangChiTiet.setNgayTao(new Date());
        gioHangChiTiet.setNgayCapNhat(new Date());


//        Tăng Số Lượng Mua Sản Phảm vào Giỏ Hàng
        if (gioHangChiTietRepository.existsByIdGioHangAndIdSanPhamChiTiet(gioHang.getIdGioHang(),
                sanPhamChiTiet.get().getIdSanPhamChiTiet())) {
            GioHangChiTiet chiTietExist = gioHangChiTietRepository.findByGioHang_IdGioHangAndSanPhamChiTiet_IdSanPhamChiTiet(gioHang.getIdGioHang(),
                    sanPhamChiTiet.get().getIdSanPhamChiTiet()).get();
            gioHangChiTiet.setIdGioHangChiTiet(chiTietExist.getIdGioHangChiTiet());
            gioHangChiTiet.setSoLuong(chiTietExist.getSoLuong() + 1);
        } else {
            gioHangChiTiet.setSoLuong(gioHangChiTietDTO.getSoLuong());
        }
        gioHangChiTiet.setThanhTien(sanPhamRepository.findById(gioHangChiTietDTO.getIdSanPham()).get().getGiaBan()
                .multiply(BigDecimal.valueOf(gioHangChiTiet.getSoLuong())));
        gioHangChiTietRepository.save(gioHangChiTiet);
        // Lấy thông tin mã sản phẩm và tên sản phẩm từ sanPhamChiTiet
        String productCode = sanPhamChiTiet.get().getSanPham().getMaSanPham(); // Mã sản phẩm
        String productName = sanPhamChiTiet.get().getSanPham().getTenSanPham(); // Tên sản phẩm

        // Tạo thông báo sau khi thêm sản phẩm vào giỏ hàng
        LoaiThongBao loaiThongBao = loaiThongBaoRepository.findById(9).orElseThrow(() -> new RuntimeException("Loại thông báo không tồn tại"));
        ThongBao thongBao = new ThongBao();
        thongBao.setNguoiDung(gioHang.getNguoiDung());
        thongBao.setLoaiThongBao(loaiThongBao);
        thongBao.setNoiDung("Bạn đã thêm sản phẩm " + productName + " vào giỏ hàng với Mã sản phẩm là: " + productCode);
        thongBao.setTrangThai(true);
        thongBao.setNgayGui(new Date());
        thongBaoRepository.save(thongBao);
        return gioHang;
    }

    public GioHang updateGioHangChiTiet(Integer idGioHang, GioHangChiTietDTO gioHangChiTietDTO) {
        GioHang gioHang = gioHangRepository.findById(idGioHang)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

        // Tìm chi tiết sản phẩm trong giỏ hàng dựa trên id sản phẩm chi tiết
        GioHangChiTiet chiTiet = gioHangChiTietRepository
                .findByGioHang_IdGioHangAndSanPhamChiTiet_IdSanPhamChiTiet(idGioHang,
                        gioHangChiTietDTO.getIdSanPhamChiTiet())
                .orElseThrow(() -> new RuntimeException("Chi tiết sản phẩm không tồn tại trong giỏ hàng"));

        // Cập nhật số lượng và thành tiền
        chiTiet.setSoLuong(gioHangChiTietDTO.getSoLuong());
        chiTiet.setThanhTien(chiTiet.getDonGia().multiply(BigDecimal.valueOf(gioHangChiTietDTO.getSoLuong())));
        gioHangChiTietRepository.save(chiTiet); // Lưu lại chi tiết giỏ hàng

        return gioHang; // Trả về giỏ hàng sau khi cập nhật
    }

    public GioHang clearGioHang(Integer idGioHang) {
        // Kiểm tra xem giỏ hàng có tồn tại hay không
        GioHang gioHang = gioHangRepository.findById(idGioHang)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

        // Xóa tất cả chi tiết sản phẩm liên quan đến giỏ hàng
        List<GioHangChiTiet> chiTietList = gioHangChiTietRepository.findByGioHang_IdGioHang(idGioHang);
        gioHangChiTietRepository.deleteAll(chiTietList); // Xóa tất cả sản phẩm trong giỏ hàng

        // Đặt lại ngày cập nhật giỏ hàng
        gioHang.setNgayCapNhat(new Date());
        return gioHangRepository.save(gioHang); // Lưu và trả về giỏ hàng đã xóa sản phẩm
    }

    public List<SanPhamChiTietDTO> getGioHangChiTiet(Integer idNguoiDung) {
        // Fetch the shopping cart by ID
        GioHang gioHang = gioHangRepository.findByNguoiDung_IdNguoiDungOrderByIdGioHang(idNguoiDung);

        List<GioHangChiTiet> chiTietList = gioHangChiTietRepository.findByGioHang_IdGioHang(idNguoiDung);
        List<SanPhamChiTietDTO> sanpham = new ArrayList<>();

        for (GioHangChiTiet gioHangChiTiet : chiTietList) {
            SanPhamChiTietDTO s = new SanPhamChiTietDTO();
            SanPhamChiTiet sanPhamChiTiet = gioHangChiTiet.getSanPhamChiTiet();
            s.setIdspct(sanPhamChiTiet.getIdSanPhamChiTiet());
            s.setSoLuong(gioHangChiTiet.getSoLuong());
            s.setTenSanPham(sanPhamChiTiet.getSanPham().getTenSanPham());
            s.setTenchatlieu(sanPhamChiTiet.getChatLieuChiTiet().getChatLieu().getTenChatLieu());
            s.setTenkichthuoc(sanPhamChiTiet.getKichThuocChiTiet().getKichThuoc().getTenKichThuoc());
            s.setTenmausac(sanPhamChiTiet.getMauSacChiTiet().getMauSac().getTenMauSac());
            s.setIdSanPham(sanPhamChiTiet.getSanPham().getIdSanPham());
            s.setMoTa(sanPhamChiTiet.getSanPham().getMoTa());
            s.setGiaTien(sanPhamChiTiet.getSanPham().getGiaBan());
            sanpham.add(s);
        }

        return sanpham; // Return list of product details
    }

    public GioHang deleteGioHangChiTiet(Integer idGioHang, Integer idSanPhamChiTiet) {
        GioHang gioHang = gioHangRepository.findById(idGioHang)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

        GioHangChiTiet chiTiet = gioHangChiTietRepository
                .findByGioHang_IdGioHangAndSanPhamChiTiet_IdSanPhamChiTiet(idGioHang, idSanPhamChiTiet)
                .orElseThrow(() -> new RuntimeException("Chi tiết sản phẩm không tồn tại trong giỏ hàng"));

        gioHangChiTietRepository.delete(chiTiet); // Xóa chi tiết sản phẩm

        return gioHang; // Trả về giỏ hàng sau khi xóa sản phẩm
    }

}
