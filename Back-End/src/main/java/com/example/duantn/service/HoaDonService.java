package com.example.duantn.service;

import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.dto.SanPhamChiTietDTO;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HoaDonService {
    @Autowired
    private HoaDonRepository hoaDonRepository;
    public List<Object[]> getAllHoaDon() {
        return hoaDonRepository.getAllHoaDon();
    }
    public Optional<HoaDon> getHoaDonById(Integer idHoaDon) {
        return hoaDonRepository.findById(idHoaDon);
    }
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Autowired
    private PhiVanChuyenRepository phiVanChuyenRepository;

    @Autowired
    private PhuongThucThanhToanHoaDonRepository phuongThucThanhToanHoaDonRepository;

    @Autowired
    private GioHangRepository gioHangRepository;


    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    private DiaChiVanChuyenRepository diaChiVanChuyenRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private TrangThaiHoaDonRepository trangThaiHoaDonRepository;



    public List<HoaDonDTO> hienthi(String maHoaDon) {
        List<HoaDon> hoaDonList;

        if (maHoaDon != null && !maHoaDon.isEmpty()) {
            hoaDonList = hoaDonRepository.findByMaHoaDon(maHoaDon)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        } else {
            hoaDonList = hoaDonRepository.findAll();
        }

        return hoaDonList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private HoaDonDTO convertToDTO(HoaDon hoaDon) {
        HoaDonDTO dto = new HoaDonDTO();

        dto.setIdHoaDon(hoaDon.getIdHoaDon());
        dto.setMaHoaDon(hoaDon.getMaHoaDon());
        dto.setNgayTao(hoaDon.getNgayTao());
        dto.setTenNguoiNhan(hoaDon.getTenNguoiNhan());
        dto.setSdtNguoiNhan(hoaDon.getSdtNguoiNhan());
        dto.setDiaChi(hoaDon.getDiaChi());
//        dto.setPhiShip(hoaDon.getPhiShip().doubleValue());
        dto.setThanhTien(hoaDon.getThanhTien());
        dto.setGhiChu(hoaDon.getMoTa());


        // Thông tin người dùng
        if (hoaDon.getNguoiDung() != null) {
            dto.setIdNguoiDung(hoaDon.getNguoiDung().getIdNguoiDung());
            dto.setEmail(hoaDon.getNguoiDung().getEmail());
        }

        // Thông tin địa chỉ vận chuyển
        if (hoaDon.getDiaChiVanChuyen() != null) {
            dto.setIdDiaChiVanChuyen(hoaDon.getDiaChiVanChuyen().getIdDiaChiVanChuyen());
            dto.setTinh(hoaDon.getDiaChiVanChuyen().getTinh().getIdTinh());
            dto.setHuyen(hoaDon.getDiaChiVanChuyen().getHuyen().getIdHuyen());
            dto.setXa(hoaDon.getDiaChiVanChuyen().getXa().getIdXa());
        }

        // Thông tin trạng thái hóa đơn
        if (hoaDon.getTrangThaiHoaDon() != null) {
            dto.setIdtrangthaihoadon(hoaDon.getTrangThaiHoaDon().getIdTrangThaiHoaDon());
        }

        // Thông tin phương thức thanh toán
        if (hoaDon.getPhuongThucThanhToanHoaDon() != null) {
            dto.setIdphuongthucthanhtoanhoadon(hoaDon.getPhuongThucThanhToanHoaDon().getIdThanhToanHoaDon());
            dto.setTenPhuongThucThanhToan(hoaDon.getPhuongThucThanhToanHoaDon()
                    .getPhuongThucThanhToan()
                    .getTenPhuongThuc());
        }

        // Danh sách sản phẩm chi tiết
        List<HoaDonChiTiet> chiTietList = hoaDonChiTietRepository.findByHoaDon_IdHoaDon(hoaDon.getIdHoaDon());
        dto.setListSanPhamChiTiet(chiTietList.stream()
                .map(chiTiet -> {
                    SanPhamChiTietDTO spDTO = new SanPhamChiTietDTO();
                    spDTO.setIdspct(chiTiet.getSanPhamChiTiet().getIdSanPhamChiTiet());
                    spDTO.setSoLuong(chiTiet.getSoLuong());
                    spDTO.setGiaTien(chiTiet.getTongTien());
                    spDTO.setTenkichthuoc(chiTiet.getSanPhamChiTiet().getKichThuocChiTiet().getKichThuoc().getTenKichThuoc());
                    spDTO.setTenmausac(chiTiet.getSanPhamChiTiet().getMauSacChiTiet().getMauSac().getTenMauSac());
                    spDTO.setTenchatlieu(chiTiet.getSanPhamChiTiet().getChatLieuChiTiet().getChatLieu().getTenChatLieu());
                    spDTO.setTenSanPham(chiTiet.getSanPhamChiTiet().getSanPham().getTenSanPham());
                    spDTO.setMoTa(chiTiet.getSanPhamChiTiet().getSanPham().getMoTa());
                    spDTO.setIdSanPham(chiTiet.getSanPhamChiTiet().getSanPham().getIdSanPham());
                    return spDTO;
                })
                .collect(Collectors.toList()));

        return dto;
    }


    private void saveCODPayment(HoaDon hoaDon, String generatedMaHoaDon, HoaDonDTO hoaDonDTO) {
        hoaDonRepository.save(hoaDon);
        PhuongThucThanhToanHoaDon phuongThucThanhToanHoaDon = new PhuongThucThanhToanHoaDon();
        phuongThucThanhToanHoaDon.setPhuongThucThanhToan(phuongThucThanhToanRepository.findById(4).get()); // COD
        phuongThucThanhToanHoaDon.setNgayGiaoDich(new Date());
        phuongThucThanhToanHoaDon.setMoTa("Thanh toán COD cho đơn hàng " + generatedMaHoaDon);
        phuongThucThanhToanHoaDon.setHoaDon(hoaDon);
        phuongThucThanhToanHoaDonRepository.save(phuongThucThanhToanHoaDon);
        hoaDon.setPhuongThucThanhToanHoaDon(phuongThucThanhToanHoaDon);
        hoaDonRepository.save(hoaDon);

        processCartItems(hoaDonDTO, hoaDon);
    }


    private void updateHoaDonAfterPaymentSuccess(HoaDon hoaDon, String paymentUrl) {
        hoaDon.setTrangThaiHoaDon(hoaDon.getTrangThaiHoaDon());
        hoaDon.setThanhTien(hoaDon.getThanhTien());
        hoaDonRepository.save(hoaDon);
    }



    private void processCartItems(HoaDonDTO hoaDonDTO, HoaDon hoaDon) {
        // Xử lý các sản phẩm trong giỏ hàng
        hoaDonDTO.getListSanPhamChiTiet().forEach(sanPham -> {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(sanPham.getIdspct())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết"));

            // Kiểm tra số lượng tồn kho
            if (sanPhamChiTiet.getSoLuong() <= 0 || sanPham.getSoLuong() > sanPhamChiTiet.getSoLuong()) {
                throw new RuntimeException("Số lượng tồn kho không đủ");
            }

            // Cập nhật số lượng tồn kho
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - sanPham.getSoLuong());
            sanPhamChiTietRepository.save(sanPhamChiTiet);

            // Lưu chi tiết hóa đơn
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSoLuong(sanPham.getSoLuong());
            hoaDonChiTiet.setTongTien(sanPham.getGiaTien().multiply(BigDecimal.valueOf(sanPham.getSoLuong())));
            hoaDonChiTiet.setNgayTao(new Date());
            hoaDonChiTiet.setTrangThai(true);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
        });

        // Sau khi tạo hóa đơn, clear giỏ hàng
        clearGioHangByUserId(hoaDonDTO.getCartId());
    }

    public void clearGioHangByUserId(Integer idNguoiDung) {
        GioHang gioHang = gioHangRepository.findByNguoiDung_IdNguoiDung(idNguoiDung);
        if (gioHang == null) {
            throw new RuntimeException("Giỏ hàng không tồn tại cho người dùng với ID: " + idNguoiDung);
        }

        List<GioHangChiTiet> chiTietList = gioHangChiTietRepository.findByGioHang_IdGioHang(gioHang.getIdGioHang());
        gioHangChiTietRepository.deleteAll(chiTietList);
        gioHang.setNgayCapNhat(new Date());
        gioHangRepository.save(gioHang);
    }

    public List<HoaDonDTO> getHoaDonByUserId(Integer idNguoiDung) {
        // Lấy danh sách hóa đơn từ repository
        List<HoaDon> hoaDonList = hoaDonRepository.findByNguoiDung_IdNguoiDung(idNguoiDung);

        // Chuyển đổi từ HoaDon sang HoaDonDTO
        return hoaDonList.stream().map(hoaDon -> {
            HoaDonDTO dto = new HoaDonDTO();
            dto.setIdHoaDon(hoaDon.getIdHoaDon());
            dto.setMaHoaDon(hoaDon.getMaHoaDon());
            dto.setNgayTao(hoaDon.getNgayTao());
            dto.setTenNguoiNhan(hoaDon.getTenNguoiNhan());
            dto.setSdtNguoiNhan(hoaDon.getSdtNguoiNhan());
            dto.setDiaChi(hoaDon.getDiaChi());
//            dto.setPhiShip(hoaDon.getPhiShip().doubleValue());
            dto.setThanhTien(hoaDon.getThanhTien());
            dto.setGhiChu(hoaDon.getMoTa());


            // Thông tin người dùng
            if (hoaDon.getNguoiDung() != null) {
                dto.setIdNguoiDung(hoaDon.getNguoiDung().getIdNguoiDung());
                dto.setEmail(hoaDon.getNguoiDung().getEmail());
            }

            // Thông tin địa chỉ vận chuyển
            if (hoaDon.getDiaChiVanChuyen() != null) {
                dto.setIdDiaChiVanChuyen(hoaDon.getDiaChiVanChuyen().getIdDiaChiVanChuyen());
                dto.setTinh(hoaDon.getDiaChiVanChuyen().getTinh().getIdTinh());
                dto.setHuyen(hoaDon.getDiaChiVanChuyen().getHuyen().getIdHuyen());
                dto.setXa(hoaDon.getDiaChiVanChuyen().getXa().getIdXa());
            }

            // Thông tin phương thức thanh toán
            if (hoaDon.getPhuongThucThanhToanHoaDon() != null) {
                dto.setIdphuongthucthanhtoanhoadon(hoaDon.getPhuongThucThanhToanHoaDon().getIdThanhToanHoaDon());
                dto.setTenPhuongThucThanhToan(hoaDon.getPhuongThucThanhToanHoaDon()
                        .getPhuongThucThanhToan()
                        .getTenPhuongThuc());
            }

            // trangthaihoadon
            if (hoaDon.getTrangThaiHoaDon() != null) {
                dto.setIdtrangthaihoadon(hoaDon.getTrangThaiHoaDon().getIdTrangThaiHoaDon());
            }

            // Danh sách sản phẩm chi tiết
            List<HoaDonChiTiet> chiTietList = hoaDonChiTietRepository.findByHoaDon_IdHoaDon(hoaDon.getIdHoaDon());
            dto.setListSanPhamChiTiet(chiTietList.stream()
                    .map(chiTiet -> {
                        SanPhamChiTietDTO spDTO = new SanPhamChiTietDTO();
                        spDTO.setIdspct(chiTiet.getSanPhamChiTiet().getIdSanPhamChiTiet());
                        spDTO.setSoLuong(chiTiet.getSoLuong());
                        spDTO.setGiaTien(chiTiet.getTongTien());
                        spDTO.setTenkichthuoc(chiTiet.getSanPhamChiTiet().getKichThuocChiTiet().getKichThuoc().getTenKichThuoc());
                        spDTO.setTenmausac(chiTiet.getSanPhamChiTiet().getMauSacChiTiet().getMauSac().getTenMauSac());
                        spDTO.setTenchatlieu(chiTiet.getSanPhamChiTiet().getChatLieuChiTiet().getChatLieu().getTenChatLieu());
                        spDTO.setTenSanPham(chiTiet.getSanPhamChiTiet().getSanPham().getTenSanPham());
                        spDTO.setMoTa(chiTiet.getSanPhamChiTiet().getSanPham().getMoTa());
                        spDTO.setIdSanPham(chiTiet.getSanPhamChiTiet().getSanPham().getIdSanPham());
                        return spDTO;
                    })
                    .collect(Collectors.toList()));

            return dto;
        }).collect(Collectors.toList());
    }


}
