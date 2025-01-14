package com.example.duantn.service;

import com.example.duantn.dto.GioHangChiTietDTO;
import com.example.duantn.dto.GioHangRequest;
import com.example.duantn.dto.GioHangResponse;
import com.example.duantn.dto.SanPhamChiTietDTO;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GioHangService {

    @Autowired
    private GioHangRepository gioHangRepository;
    @Autowired
    private GiamGiaSanPhamRepository giamGiaSanPhamRepository;
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
            if (!nguoiDung.getTrangThai()) {
                throw new RuntimeException("Tài khoản của bạn đã bị khóa, không thể thêm sản phẩm vào giỏ hàng.");
            }
            gioHang.setNguoiDung(nguoiDung); // Gán đối tượng NguoiDung vào giỏ hàng
            gioHang.setTrangThai(true);
            gioHang.setNgayTao(new Date());
            gioHang.setNgayCapNhat(new Date());
            gioHang = gioHangRepository.save(gioHang);
        }
        // Kiểm tra trạng thái sản phẩm trước khi thêm vào giỏ hàng
        Optional<SanPham> sanPham = sanPhamRepository.findById(gioHangChiTietDTO.getIdSanPham());
        if (sanPham.isEmpty() || !sanPham.get().getTrangThai()) {
            System.out.println("Sản phẩm không còn khả dụng (tình trạng sản phẩm: không kích hoạt).");

            // Trả về thông báo lỗi mà không hiển thị stack trace
            return null; // Hoặc bạn có thể trả về giỏ hàng rỗng nếu cần
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

    public GioHang updateGioHangChiTiet(Integer idNguoiDung, GioHangChiTietDTO gioHangChiTietDTO) {
        GioHang gioHang = gioHangRepository.findByNguoiDung_IdNguoiDung(idNguoiDung);
        if (gioHang == null) {
            throw new RuntimeException("Giỏ hàng không tồn tại cho người dùng với ID: " + idNguoiDung);
        }

        GioHangChiTiet chiTiet = gioHangChiTietRepository
                .findByGioHang_IdGioHangAndSanPhamChiTiet_IdSanPhamChiTiet(gioHang.getIdGioHang(),
                        gioHangChiTietDTO.getIdSanPhamChiTiet())
                .orElseThrow(() -> new RuntimeException("Chi tiết sản phẩm không tồn tại trong giỏ hàng"));

        // Cập nhật số lượng và thành tiền
        chiTiet.setSoLuong(gioHangChiTietDTO.getSoLuong());
        chiTiet.setThanhTien(chiTiet.getDonGia().multiply(BigDecimal.valueOf(gioHangChiTietDTO.getSoLuong())));
        gioHangChiTietRepository.save(chiTiet);

        return gioHang;
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

    public List<Object[]> getGioHangChiTiets(Integer idNguoiDung) {
        return gioHangRepository.getAllGioHang(idNguoiDung);
    }




    public GioHang deleteGioHangChiTiet(Integer idNguoiDung, Integer idSanPhamChiTiet) {
        GioHang gioHang = gioHangRepository.findByNguoiDung_IdNguoiDung(idNguoiDung);
        if (gioHang == null) {
            throw new RuntimeException("Giỏ hàng không tồn tại cho người dùng với ID: " + idNguoiDung);
        }

        GioHangChiTiet chiTiet = gioHangChiTietRepository
                .findByGioHang_IdGioHangAndSanPhamChiTiet_IdSanPhamChiTiet(gioHang.getIdGioHang(), idSanPhamChiTiet)
                .orElseThrow(() -> new RuntimeException("Chi tiết sản phẩm không tồn tại trong giỏ hàng"));

        gioHangChiTietRepository.delete(chiTiet);

        return gioHang;
    }


    public void themSanPhamVaoGioHang(Integer idNguoiDung, GioHangRequest request) {
        GioHang gioHang = gioHangRepository.findByIdNguoiDung(idNguoiDung);
        if (gioHang == null) {
            gioHang = new GioHang();
            NguoiDung nguoiDung = nguoiDungRepository.findById(idNguoiDung)
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
            gioHang.setNguoiDung(nguoiDung);
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

        // Sử dụng BigDecimal cho donGia và thanhTien
        BigDecimal donGia = BigDecimal.valueOf(request.getDonGia());
        BigDecimal thanhTien = BigDecimal.valueOf(request.getThanhTien());

        if (gioHangChiTiet != null) {
            gioHangChiTiet.setSoLuong(gioHangChiTiet.getSoLuong() + request.getSoLuong());
            gioHangChiTiet.setDonGia(donGia);
            gioHangChiTiet.setThanhTien(thanhTien);
            gioHangChiTiet.setNgayCapNhat(new Date());
        } else {
            gioHangChiTiet = new GioHangChiTiet();
            gioHangChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            gioHangChiTiet.setGioHang(gioHang);
            gioHangChiTiet.setSoLuong(request.getSoLuong());
            gioHangChiTiet.setThanhTien(thanhTien);
            gioHangChiTiet.setDonGia(donGia);
            gioHangChiTiet.setTrangThai(true);
            gioHangChiTiet.setNgayTao(new Date());
            gioHangChiTiet.setNgayCapNhat(new Date());
        }

        // Giảm số lượng sản phẩm
        sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - request.getSoLuong());

        sanPhamChiTietRepository.save(sanPhamChiTiet);
        gioHangChiTietRepository.save(gioHangChiTiet);
    }


    public List<GioHangResponse> layDanhSachSanPhamTrongGioHang(Integer idNguoiDung) {
        GioHang gioHang = gioHangRepository.findByIdNguoiDung(idNguoiDung);
        if (gioHang == null) {
            return List.of();
        }

        // Truy vấn trực tiếp danh sách chi tiết giỏ hàng
        List<GioHangChiTiet> gioHangChiTietList = gioHangRepository.findGioHangChiTietByGioHangId(gioHang.getIdGioHang());
        // Chuyển đổi chi tiết giỏ hàng sang danh sách response
        return gioHangChiTietList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private GioHangResponse convertToResponse(GioHangChiTiet chiTiet) {
        GioHangResponse response = new GioHangResponse();
        response.setIdSanPhamChiTiet(chiTiet.getSanPhamChiTiet().getIdSanPhamChiTiet());
        response.setTenSanPham(chiTiet.getSanPhamChiTiet().getSanPham().getTenSanPham());
        response.setMauSac(chiTiet.getSanPhamChiTiet().getMauSacChiTiet().getMauSac().getTenMauSac());
        response.setKichThuoc(chiTiet.getSanPhamChiTiet().getKichThuocChiTiet().getKichThuoc().getTenKichThuoc());
        response.setChatLieu(chiTiet.getSanPhamChiTiet().getChatLieuChiTiet().getChatLieu().getTenChatLieu());
        response.setSoLuong(chiTiet.getSoLuong());
        response.setGiaBan(chiTiet.getDonGia());  // BigDecimal giữ nguyên, không cần chuyển đổi
        response.setThanhTien(chiTiet.getThanhTien());  // BigDecimal giữ nguyên, không cần chuyển đổi
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

            // Cập nhật số lượng và tính lại thanh tiền
            int oldQuantity = gioHangChiTiet.getSoLuong();
            gioHangChiTiet.setSoLuong(soLuong);
            BigDecimal giaBan = sanPhamChiTiet.getSanPham().getGiaBan();

            // Kiểm tra giá khuyến mãi
            Optional<BigDecimal> giaKhuyenMaiOpt = giamGiaSanPhamRepository.findGiaKhuyenMaiHienTaiBySanPhamId(
                    sanPhamChiTiet.getSanPham().getIdSanPham()
            );

            BigDecimal giaApDung = giaKhuyenMaiOpt.orElse(giaBan); // Ưu tiên giá khuyến mãi nếu có

            // Tính thành tiền
            BigDecimal thanhTien = giaApDung.multiply(BigDecimal.valueOf(soLuong));
            gioHangChiTiet.setThanhTien(thanhTien);
            gioHangChiTiet.setNgayCapNhat(new Date());
            gioHangChiTietRepository.save(gioHangChiTiet);

            // Cập nhật số lượng sản phẩm trong kho
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + oldQuantity - soLuong);
            sanPhamChiTietRepository.save(sanPhamChiTiet);
        } else {
            throw new RuntimeException("Chi tiết giỏ hàng không tồn tại.");
        }
    }
    @Transactional
    public void xoaTatCaSanPhamKhoiGioHang(Integer idNguoiDung) {
        GioHang gioHang = gioHangRepository.findByIdNguoiDung(idNguoiDung);
        if (gioHang == null) {
            throw new RuntimeException("Giỏ hàng không tồn tại.");
        }

        // Lấy danh sách chi tiết sản phẩm trong giỏ hàng
        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietRepository.findByGioHang(gioHang);
        if (gioHangChiTietList.isEmpty()) {
            throw new RuntimeException("Giỏ hàng không có sản phẩm nào.");
        }

        // Cộng lại số lượng sản phẩm trong kho
        for (GioHangChiTiet chiTiet : gioHangChiTietList) {
            Integer idSanPhamChiTiet = chiTiet.getSanPhamChiTiet().getIdSanPhamChiTiet();
            Optional<SanPhamChiTiet> optionalSanPhamChiTiet = sanPhamChiTietRepository.findById(idSanPhamChiTiet);
            if (optionalSanPhamChiTiet.isPresent()) {
                SanPhamChiTiet sanPhamChiTiet = optionalSanPhamChiTiet.get();
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + chiTiet.getSoLuong());
                sanPhamChiTietRepository.save(sanPhamChiTiet);
            }
        }

        // Xóa tất cả sản phẩm trong giỏ hàng
        gioHangChiTietRepository.deleteAll(gioHangChiTietList);
    }

    public boolean checkSanPhamTrongGioHang(Integer idGioHang, Integer idSanPhamChiTiet) {
        // Lấy sản phẩm trong giỏ hàng theo gioHangId và sanPhamChiTietId
        Optional<GioHangChiTiet> gioHangChiTiet = gioHangChiTietRepository.findByGioHang_IdGioHangAndSanPhamChiTiet_IdSanPhamChiTiet(idGioHang, idSanPhamChiTiet);

        if (gioHangChiTiet != null) {
            // Lấy số lượng sản phẩm hiện tại trong kho
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(idSanPhamChiTiet)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
            int soLuongConLai = sanPhamChiTiet.getSoLuong();

            // Kiểm tra số lượng trong giỏ hàng và trong kho
            if (gioHangChiTiet.get().getSoLuong() > soLuongConLai) {
                return false; // Nếu số lượng trong giỏ hàng lớn hơn số lượng còn lại trong kho, trả về false
            }
        }

        return true; // Sản phẩm đủ số lượng trong giỏ hàng
    }

}
