package com.example.duantn.service;

import com.example.duantn.dto.*;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HoaDonService {
    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private LichSuHoaDonRepository lichSuHoaDonRepository;
    @Autowired
    private GiamGiaSanPhamRepository giamGiaSanPhamRepository;
    public HoaDonService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final RestTemplate restTemplate;
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Autowired
    private LichSuThanhToanRepository lichSuThanhToanRepository;
    @Autowired
    private TinhRepository tinhRepository;

    @Autowired
    private HuyenRepository huyenRepository;

    @Autowired
    private XaRepository xaRepository;

    @Autowired
    private PhuongThucThanhToanHoaDonRepository phuongThucThanhToanHoaDonRepository;

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private PaymentService paymentService;

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
    private VoucherRepository voucherRepository;
    @Autowired
    private LoaiTrangThaiRepository loaiTrangThaiRepository;
    @Autowired
    private TrangThaiHoaDonRepository trangThaiHoaDonRepository;

    @Autowired
    private PhuongThucThanhToanHoaDonRepository ptThanhToanHoaDonRepository;

    public List<Object[]> getAllHoaDon() {
        return hoaDonRepository.getAllHoaDon();
    }

    public List<Object[]> getTrangThaiHoaDonByIdHoaDon(Integer idHoaDon) {
        return hoaDonRepository.getTrangThaiHoaDonByIdHoaDon(idHoaDon);
    }

    public List<Object[]> getVoucherHoaDonByIdHoaDon(Integer idHoaDon) {
        return hoaDonRepository.getVoucherHoaDonByIdHoaDon(idHoaDon);
    }

    public List<Object[]> getSanPhamCTHoaDonByIdHoaDon(Integer idHoaDon) {
        return hoaDonRepository.getSanPhamCTHoaDonByIdHoaDon(idHoaDon);
    }

    public Optional<HoaDon> getHoaDonById(Integer idHoaDon) {
        return hoaDonRepository.findById(idHoaDon);
    }


    public List<HoaDonDTO> hienthi(String maHoaDon) {
        List<HoaDon> hoaDonList;

        if (maHoaDon != null && !maHoaDon.isEmpty()) {
            // Tìm hóa đơn theo mã
            hoaDonList = hoaDonRepository.findByMaHoaDon(maHoaDon)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        } else {
            // Lấy toàn bộ hóa đơn
            hoaDonList = hoaDonRepository.findAll();
        }

        return hoaDonList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private HoaDonDTO convertToDTO(HoaDon hoaDon) {
        HoaDonDTO dto = new HoaDonDTO();

        // Gán thông tin hóa đơn
        dto.setIdHoaDon(hoaDon.getIdHoaDon());
        dto.setMaHoaDon(hoaDon.getMaHoaDon());
        dto.setNgayTao(hoaDon.getNgayTao());
        dto.setTenNguoiNhan(hoaDon.getTenNguoiNhan());
        dto.setSdtNguoiNhan(hoaDon.getSdtNguoiNhan());
        dto.setDiaChi(hoaDon.getDiaChi());
        dto.setThanhTien(hoaDon.getThanhTien());
        dto.setGhiChu(hoaDon.getMoTa());
// Kiểm tra xem voucher có tồn tại không
        if (hoaDon.getVoucher() != null) {
            dto.setGiaTriMavoucher(hoaDon.getVoucher().getGiaTriGiamGia());
            dto.setMavoucher(hoaDon.getVoucher().getMaVoucher()); // Nếu cần thêm mã voucher
        } else {
            dto.setGiaTriMavoucher(BigDecimal.ZERO); // Hoặc giá trị mặc định phù hợp
            dto.setMavoucher(null);
        }

        // Thông tin người dùng
        if (hoaDon.getNguoiDung() != null) {
            dto.setIdNguoiDung(hoaDon.getNguoiDung().getIdNguoiDung());
            dto.setEmail(hoaDon.getNguoiDung().getEmail());
        }

        // Thông tin địa chỉ vận chuyển
        if (hoaDon.getDiaChiVanChuyen() != null) {
            DiaChiVanChuyen diaChiVanChuyen = hoaDon.getDiaChiVanChuyen();

            dto.setIdDiaChiVanChuyen(diaChiVanChuyen.getIdDiaChiVanChuyen());

            if (diaChiVanChuyen.getTinh() != null) {
                dto.setTinh(diaChiVanChuyen.getTinh().getIdTinh());
                dto.setTenTinh(diaChiVanChuyen.getTinh().getTenTinh());
            }
            if (diaChiVanChuyen.getHuyen() != null) {
                dto.setHuyen(diaChiVanChuyen.getHuyen().getIdHuyen());
                dto.setTenHuyen(diaChiVanChuyen.getHuyen().getTenHuyen());
            }
            if (diaChiVanChuyen.getXa() != null) {
                dto.setXa(diaChiVanChuyen.getXa().getIdXa());
                dto.setTenXa(diaChiVanChuyen.getXa().getTenXa());
            }
        }

        // Thông tin phương thức thanh toán
        if (hoaDon.getPhuongThucThanhToanHoaDon() != null) {
            dto.setIdphuongthucthanhtoanhoadon(hoaDon.getPhuongThucThanhToanHoaDon().getIdThanhToanHoaDon());
            dto.setTenPhuongThucThanhToan(hoaDon.getPhuongThucThanhToanHoaDon()
                    .getPhuongThucThanhToan()
                    .getTenPhuongThuc());
        }

        // Lấy danh sách sản phẩm chi tiết
        List<HoaDonChiTiet> chiTietList = hoaDonChiTietRepository.findByHoaDon_IdHoaDon(hoaDon.getIdHoaDon());
        dto.setListSanPhamChiTiet(chiTietList.stream()
                .map(chiTiet -> {
                    SanPhamChiTietDTO spDTO = new SanPhamChiTietDTO();
                    spDTO.setIdspct(chiTiet.getSanPhamChiTiet().getIdSanPhamChiTiet());
                    spDTO.setSoLuong(chiTiet.getSoLuong());
                    spDTO.setMaSPCT(chiTiet.getSanPhamChiTiet().getMaSanPhamCT());
                    spDTO.setTongtien(chiTiet.getTongTien());
                    spDTO.setGiaTien(chiTiet.getSanPhamChiTiet().getSanPham().getGiaBan());
                    spDTO.setTenkichthuoc(chiTiet.getSanPhamChiTiet().getKichThuocChiTiet().getKichThuoc().getTenKichThuoc());
                    spDTO.setTenmausac(chiTiet.getSanPhamChiTiet().getMauSacChiTiet().getMauSac().getTenMauSac());
                    spDTO.setTenchatlieu(chiTiet.getSanPhamChiTiet().getChatLieuChiTiet().getChatLieu().getTenChatLieu());
                    spDTO.setTenSanPham(chiTiet.getSanPhamChiTiet().getSanPham().getTenSanPham());
                    spDTO.setMoTa(chiTiet.getSanPhamChiTiet().getSanPham().getMoTa());
                    spDTO.setIdSanPham(chiTiet.getSanPhamChiTiet().getSanPham().getIdSanPham());
                    // Lấy giá khuyến mãi (nếu có)
                    BigDecimal giaKhuyenMai = giamGiaSanPhamRepository.findGiaKhuyenMaiBySanPhamId(spDTO.getIdSanPham());
                    spDTO.setGiaKhuyenMai(giaKhuyenMai != null ? giaKhuyenMai : spDTO.getGiaTien()); // Nếu không có khuyến mãi, dùng giá bán
                    return spDTO;
                })
                .collect(Collectors.toList()));

        // Lấy trạng thái hóa đơn và loại trạng thái
        List<Object[]> hoaDonWithTrangThai = hoaDonRepository.findHoaDonWithTrangThaiAndLoaiByMaHoaDon(hoaDon.getMaHoaDon());
        if (hoaDonWithTrangThai != null && !hoaDonWithTrangThai.isEmpty()) {
            Object[] row = hoaDonWithTrangThai.get(0);
            TrangThaiHoaDon trangThai = (TrangThaiHoaDon) row[1];
            LoaiTrangThai loaiTrangThai = (LoaiTrangThai) row[2];

            dto.setIdLoaiTrangThai(loaiTrangThai.getIdLoaiTrangThai());
            dto.setTenLoaiTrangThai(loaiTrangThai.getTenLoaiTrangThai());
            dto.setIdTrangThaiHoaDon(trangThai.getIdTrangThaiHoaDon());
            dto.setTenTrangThai(trangThai.getLoaiTrangThai().getTenLoaiTrangThai());
        }

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

    private void saveVNPayPayment(HoaDon hoaDon, String generatedMaHoaDon, HoaDonDTO hoaDonDTO, HttpServletRequest req) {
        try {
            // Gửi yêu cầu thanh toán VNPay và lấy URL thanh toán
            String paymentUrl = paymentService.createPayment(hoaDonDTO.getThanhTien().longValue(), "vnpay", req);
            System.out.println("URL thanh toán VNPay: " + paymentUrl);

            // Lưu hóa đơn
            hoaDonRepository.save(hoaDon);
            PhuongThucThanhToanHoaDon phuongThucThanhToanHoaDon = new PhuongThucThanhToanHoaDon();
            phuongThucThanhToanHoaDon.setPhuongThucThanhToan(phuongThucThanhToanRepository.findById(2).orElseThrow(() -> new RuntimeException("Phương thức thanh toán không hợp lệ"))); // 2 là mã phương thức thanh toán cho VNPay
            phuongThucThanhToanHoaDon.setNgayGiaoDich(new Date());
            phuongThucThanhToanHoaDon.setMoTa("Thanh toán VNPay cho đơn hàng " + generatedMaHoaDon);
            phuongThucThanhToanHoaDon.setHoaDon(hoaDon);
            phuongThucThanhToanHoaDonRepository.save(phuongThucThanhToanHoaDon);
            hoaDon.setPhuongThucThanhToanHoaDon(phuongThucThanhToanHoaDon);
            hoaDonRepository.save(hoaDon);

            processCartItems(hoaDonDTO, hoaDon);
            updateHoaDonAfterPaymentSuccess(hoaDon, paymentUrl);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo URL thanh toán VNPay", e);
        }
    }

    private void updateHoaDonAfterPaymentSuccess(HoaDon hoaDon, String paymentUrl) {
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
// Tạo hoặc lấy LichSuHoaDon
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setNgayGiaoDich(new Date());
            lichSuHoaDon.setSoTienThanhToan(
                    hoaDonDTO.getListSanPhamChiTiet().stream()
                            .map(sp -> sp.getGiaTien().multiply(BigDecimal.valueOf(sp.getSoLuong())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
            );
            lichSuHoaDon.setNguoiDung(hoaDon.getNguoiDung());
            lichSuHoaDonRepository.save(lichSuHoaDon);
            // Lưu chi tiết hóa đơn
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSoLuong(sanPham.getSoLuong());
            hoaDonChiTiet.setTongTien(sanPham.getGiaTien().multiply(BigDecimal.valueOf(sanPham.getSoLuong())));
            hoaDonChiTiet.setNgayTao(new Date());
            hoaDonChiTiet.setNgayCapNhat(new Date());
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

    public String createOrder(HoaDonDTO hoaDonDTO, HttpServletRequest res) {
        if (hoaDonDTO.getIdNguoiDung() == null) {
            throw new RuntimeException("ID Người dùng không được null");
        }

        long currentCount = hoaDonRepository.count();
        String generatedMaHoaDon = "HD00" + (currentCount + 1);

        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon(generatedMaHoaDon);

        // Kiểm tra và lấy thông tin người dùng
        NguoiDung nguoiDung = nguoiDungRepository.findById(hoaDonDTO.getIdNguoiDung())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Người dùng với ID: " + hoaDonDTO.getIdNguoiDung()));
        hoaDon.setNguoiDung(nguoiDung);

        // Lấy hoặc lưu thông tin Tỉnh, Huyện, Xã
        if (hoaDonDTO.getTinh() == null || hoaDonDTO.getHuyen() == null || hoaDonDTO.getXa() == null) {
            throw new RuntimeException("Thông tin Tỉnh, Huyện, hoặc Xã không hợp lệ");
        }

        Tinh tinh = tinhRepository.findById(hoaDonDTO.getTinh())
                .orElseGet(() -> tinhRepository.save(fetchCityInfo(hoaDonDTO.getTinh().toString())));

        Huyen huyen = huyenRepository.findById(hoaDonDTO.getHuyen())
                .orElseGet(() -> huyenRepository.save(fetchDistrictInfo(hoaDonDTO.getHuyen().toString())));

        Xa xa = xaRepository.findById(hoaDonDTO.getXa())
                .orElseGet(() -> xaRepository.save(fetchWardInfo(hoaDonDTO.getXa().toString())));

        // Tạo và lưu đối tượng DiaChiVanChuyen
        DiaChiVanChuyen diaChiVanChuyen = new DiaChiVanChuyen();
        diaChiVanChuyen.setTinh(tinh);
        diaChiVanChuyen.setNguoiDung(nguoiDung);
        diaChiVanChuyen.setDiaChiCuThe(hoaDonDTO.getDiaChiCuThe());
        diaChiVanChuyen.setHuyen(huyen);
        diaChiVanChuyen.setXa(xa);
        diaChiVanChuyen.setTrangThai(true);
        diaChiVanChuyen.setNgayTao(new Date());
        diaChiVanChuyen.setNgayCapNhat(new Date());
        diaChiVanChuyen.setMoTa(String.format("Địa chỉ: %s, %s, %s", xa.getTenXa(), huyen.getTenHuyen(), tinh.getTenTinh()));

        diaChiVanChuyen = diaChiVanChuyenRepository.save(diaChiVanChuyen);

        // Gán địa chỉ vận chuyển đã lưu cho hóa đơn
        hoaDon.setDiaChiVanChuyen(diaChiVanChuyen);

// Kiểm tra nếu idvoucher không phải null trước khi thực hiện các thao tác
        if (hoaDonDTO.getIdvoucher() != null) {
            // Tìm Voucher theo ID
            Voucher voucher = voucherRepository.findById(hoaDonDTO.getIdvoucher())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Voucher với ID: " + hoaDonDTO.getIdvoucher()));

            // Kiểm tra số lượng của voucher còn đủ để sử dụng
            if (voucher.getSoLuong() <= 0) {
                throw new RuntimeException("Voucher này đã hết số lượng sử dụng.");
            }

            // Trừ đi số lượng voucher
            voucher.setSoLuong(voucher.getSoLuong() - 1);

            // Lưu lại thông tin voucher đã được trừ số lượng
            voucherRepository.save(voucher);

            // Gán voucher vào hóa đơn nếu có
            hoaDon.setVoucher(voucher);
        } else {
            // Nếu không có idvoucher thì không làm gì, hoặc có thể thực hiện hành động khác nếu cần
            hoaDon.setVoucher(null);  // Đảm bảo rằng không có voucher nào được gán vào hóa đơn
        }


        // Gán thông tin khác cho hóa đơn
        hoaDon.setNgayTao(new Date());
        hoaDon.setTenNguoiNhan(hoaDonDTO.getTenNguoiNhan());
        hoaDon.setDiaChi(hoaDonDTO.getDiaChi());
        hoaDon.setSdtNguoiNhan(hoaDonDTO.getSdtNguoiNhan());
        hoaDon.setThanhTien(hoaDonDTO.getThanhTien());
        hoaDon.setMoTa(hoaDonDTO.getGhiChu());
        hoaDon.setTrangThai(true);
        hoaDon.setLoai(1);

        // Xử lý phương thức thanh toán
        LoaiTrangThai loaiTrangThai;
        switch (hoaDonDTO.getTenPhuongThucThanhToan()) {
            case "cod":
                saveCODPayment(hoaDon, generatedMaHoaDon, hoaDonDTO);
                loaiTrangThai = loaiTrangThaiRepository.findById(2)
                        .orElseThrow(() -> new IllegalArgumentException("Loại trạng thái không tồn tại!"));
                break;
            case "vnpay":
                saveVNPayPayment(hoaDon, generatedMaHoaDon, hoaDonDTO, res);
                loaiTrangThai = loaiTrangThaiRepository.findById(2)
                        .orElseThrow(() -> new IllegalArgumentException("Loại trạng thái không tồn tại!"));
                break;
            case "zalopay":
                throw new RuntimeException("Chức năng ZaloPay đang phát triển");
            default:
                throw new RuntimeException("Phương thức thanh toán không hợp lệ");
        }

        // Lưu hóa đơn sau khi đã gán đầy đủ dữ liệu
        hoaDonRepository.save(hoaDon);

        // Thêm trạng thái hóa đơn
        TrangThaiHoaDon trangThaiHoaDon = new TrangThaiHoaDon();
        trangThaiHoaDon.setMoTa("Tạo đơn hàng");
        trangThaiHoaDon.setNgayTao(new Date());
        trangThaiHoaDon.setNgayCapNhat(new Date());
        trangThaiHoaDon.setLoaiTrangThai(loaiTrangThai);
        trangThaiHoaDon.setHoaDon(hoaDon);
        trangThaiHoaDonRepository.save(trangThaiHoaDon);
        // Tạo và lưu lịch sử thanh toán
        LichSuThanhToan lichSuThanhToan = new LichSuThanhToan();
        lichSuThanhToan.setSoTienThanhToan(hoaDonDTO.getThanhTien());
        lichSuThanhToan.setNgayTao(new Date());
        lichSuThanhToan.setNgayGiaoDich(new Date());  // Hoặc bạn có thể dùng ngày giao dịch từ hệ thống thanh toán
        lichSuThanhToan.setNgayCapNhat(new Date());
        lichSuThanhToan.setTrangThaiThanhToan(true); // Thực tế có thể thay đổi tùy vào trạng thái thanh toán thực tế
        lichSuThanhToan.setHoaDon(hoaDon);
        lichSuThanhToan.setNguoiDung(nguoiDung);  // Liên kết với người dùng

        lichSuThanhToanRepository.save(lichSuThanhToan);


        return generatedMaHoaDon;
    }

    // Lấy thông tin Tỉnh từ API
    private Tinh fetchCityInfo(String cityCode) {
        String url = "https://provinces.open-api.vn/api/p/" + cityCode;
        Map<String, Object> cityData = restTemplate.getForObject(url, Map.class);

        if (cityData != null && cityData.containsKey("code") && cityData.containsKey("name")) {
            Tinh tinh = new Tinh();
            tinh.setIdTinh(Integer.parseInt(cityData.get("code").toString()));
            tinh.setTenTinh(cityData.get("name").toString());
            tinh.setNgayTao(new Date());
            tinh.setNgayCapNhat(new Date());
            return tinh;
        }
        throw new RuntimeException("Không thể lấy dữ liệu tỉnh từ API");
    }

    // Lấy thông tin Huyện từ API
    private Huyen fetchDistrictInfo(String districtCode) {
        String url = "https://provinces.open-api.vn/api/d/" + districtCode;
        Map<String, Object> districtData = restTemplate.getForObject(url, Map.class);

        if (districtData != null && districtData.containsKey("code") && districtData.containsKey("name")) {
            Huyen huyen = new Huyen();
            huyen.setIdHuyen(Integer.parseInt(districtData.get("code").toString()));
            huyen.setTenHuyen(districtData.get("name").toString());
            huyen.setNgayTao(new Date());
            huyen.setNgayCapNhat(new Date());
            return huyen;
        }
        throw new RuntimeException("Không thể lấy dữ liệu huyện từ API");
    }

    // Lấy thông tin Xã từ API
    private Xa fetchWardInfo(String wardCode) {
        String url = "https://provinces.open-api.vn/api/w/" + wardCode;
        Map<String, Object> wardData = restTemplate.getForObject(url, Map.class);

        if (wardData != null && wardData.containsKey("code") && wardData.containsKey("name")) {
            Xa xa = new Xa();
            xa.setIdXa(Integer.parseInt(wardData.get("code").toString()));
            xa.setTenXa(wardData.get("name").toString());
            xa.setNgayTao(new Date());
            xa.setNgayCapNhat(new Date());
            return xa;
        }
        throw new RuntimeException("Không thể lấy dữ liệu xã từ API");
    }


    public List<HoaDon> getHoaDonChuaThanhToan() {
        return hoaDonRepository.findAllByTrangThaiFalse();
    }


    @Transactional
    public void deleteHoaDon(Integer id) {
        if (!hoaDonRepository.existsById(id)) {
            throw new EntityNotFoundException("Hóa đơn không tồn tại với ID: " + id);
        }
        trangThaiHoaDonRepository.deleteByHoaDonId(id);
        hoaDonRepository.deleteById(id);
    }

    public List<HoaDonDTO> getHoaDonWithDetails() {
        return hoaDonRepository.getHoaDonWithDetails();
    }

    public HoaDon createHoaDon(HoaDon hoaDon) {
        String newMaHoaDon = generateMaHoaDon();
        hoaDon.setMaHoaDon(newMaHoaDon);
        hoaDon.setLoai(0);
        hoaDon.setTrangThai(false);
        HoaDon savedHoaDon = hoaDonRepository.save(hoaDon);

        // Tạo trạng thái hóa đơn với loại trạng thái là 1
        TrangThaiHoaDon trangThaiHoaDon1 = new TrangThaiHoaDon();
        trangThaiHoaDon1.setMoTa("Tạo đơn hàng");
        trangThaiHoaDon1.setNgayTao(new Date());
        trangThaiHoaDon1.setNgayCapNhat(new Date());

        LoaiTrangThai loaiTrangThai1 = loaiTrangThaiRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("Loại trạng thái không tồn tại!"));

        trangThaiHoaDon1.setLoaiTrangThai(loaiTrangThai1);
        trangThaiHoaDon1.setHoaDon(savedHoaDon);
        trangThaiHoaDonRepository.save(trangThaiHoaDon1);

        // Tạo trạng thái hóa đơn với loại trạng thái là 4
        TrangThaiHoaDon trangThaiHoaDon2 = new TrangThaiHoaDon();
        trangThaiHoaDon2.setMoTa("Chờ Thanh Toán");
        trangThaiHoaDon2.setNgayTao(new Date());
        trangThaiHoaDon2.setNgayCapNhat(new Date());

        LoaiTrangThai loaiTrangThai2 = loaiTrangThaiRepository.findById(4)
                .orElseThrow(() -> new IllegalArgumentException("Loại trạng thái không tồn tại!"));

        trangThaiHoaDon2.setLoaiTrangThai(loaiTrangThai2);
        trangThaiHoaDon2.setHoaDon(savedHoaDon);
        trangThaiHoaDonRepository.save(trangThaiHoaDon2);

        return savedHoaDon;
    }


    private String generateMaHoaDon() {
        List<String> lastMaHoaDonList = hoaDonRepository.findLastMaHoaDon();
        if (!lastMaHoaDonList.isEmpty()) {
            String lastMaHoaDon = lastMaHoaDonList.get(0);
            int newId = Integer.parseInt(lastMaHoaDon.substring(2)) + 1;
            return String.format("HD%03d", newId);
        } else {
            return "HD001";
        }
    }

    public HoaDon findById(Integer id) {
        return hoaDonRepository.findById(id).orElse(null);
    }

    public boolean updateInvoiceStatus(Integer id, boolean trangThai) {
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(id);
        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();
            hoaDon.setTrangThai(trangThai);
            hoaDonRepository.save(hoaDon);
            return true;
        }
        return false;
    }

    public HoaDon getInvoiceById(Integer id) {
        // Gọi repository để tìm hóa đơn theo ID
        return hoaDonRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy hóa đơn với ID: " + id));
    }

    @Transactional
    public HoaDonResponseDTO updateHoaDon(int idHoaDon, HoaDonUpdateDTO updateHoaDonDTO) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

        hoaDon.setPhiShip(updateHoaDonDTO.getPhiShip());
        hoaDon.setNgayThanhToan(updateHoaDonDTO.getNgayThanhToan());
        hoaDon.setThanhTien(updateHoaDonDTO.getThanhTien());
        hoaDon.setSdtNguoiNhan(updateHoaDonDTO.getSetSdtNguoiNhan());

        PhuongThucThanhToanHoaDon ptThanhToan = new PhuongThucThanhToanHoaDon();
        ptThanhToan.setIdThanhToanHoaDon(updateHoaDonDTO.getIdThanhToanHoaDon());

        PhuongThucThanhToanHoaDon existingPtThanhToan = ptThanhToanHoaDonRepository.findById(ptThanhToan.getIdThanhToanHoaDon())
                .orElseThrow(() -> new RuntimeException("Phương thức thanh toán không tồn tại."));
        hoaDon.setPhuongThucThanhToanHoaDon(existingPtThanhToan);

        if (updateHoaDonDTO.getIdVoucher() > 0) {
            Voucher voucher = voucherRepository.findById(updateHoaDonDTO.getIdVoucher())
                    .orElseThrow(() -> new RuntimeException("Voucher không tồn tại"));
            hoaDon.setVoucher(voucher);
        }

        if (!existingPtThanhToan.getPhuongThucThanhToan().getIdPTThanhToan().equals(2) &&
                !existingPtThanhToan.getPhuongThucThanhToan().getIdPTThanhToan().equals(3)) {
            // Tạo trạng thái hóa đơn loại 5
            TrangThaiHoaDon trangThaiHoaDon1 = new TrangThaiHoaDon();
            trangThaiHoaDon1.setMoTa("Đã thanh toán thành công");
            trangThaiHoaDon1.setNgayTao(new Date());
            trangThaiHoaDon1.setNgayCapNhat(new Date());
            trangThaiHoaDon1.setHoaDon(hoaDon);

            LoaiTrangThai loaiTrangThai1 = loaiTrangThaiRepository.findById(5)
                    .orElseThrow(() -> new RuntimeException("Loại trạng thái không tồn tại"));
            trangThaiHoaDon1.setLoaiTrangThai(loaiTrangThai1);

            trangThaiHoaDonRepository.save(trangThaiHoaDon1);

            // Tạo trạng thái hóa đơn loại 8
            TrangThaiHoaDon trangThaiHoaDon2 = new TrangThaiHoaDon();
            trangThaiHoaDon2.setMoTa("Hoàn thành    ");
            trangThaiHoaDon2.setNgayTao(new Date());
            trangThaiHoaDon2.setNgayCapNhat(new Date());
            trangThaiHoaDon2.setHoaDon(hoaDon);

            LoaiTrangThai loaiTrangThai2 = loaiTrangThaiRepository.findById(8)
                    .orElseThrow(() -> new RuntimeException("Loại trạng thái không tồn tại"));
            trangThaiHoaDon2.setLoaiTrangThai(loaiTrangThai2);

            trangThaiHoaDonRepository.save(trangThaiHoaDon2);
        }

        hoaDonRepository.save(hoaDon);

        return new HoaDonResponseDTO(
                hoaDon.getPhiShip(),
                hoaDon.getNgayThanhToan(),
                hoaDon.getThanhTien(),
                hoaDon.getPhuongThucThanhToanHoaDon().getIdThanhToanHoaDon()
        );
    }


    public List<HoaDonDTO> getHoaDonByUserId(Integer idNguoiDung) {
        List<Object[]> hoaDonWithTrangThai = hoaDonRepository.findHoaDonWithTrangThaiAndLoai(idNguoiDung);

        return hoaDonWithTrangThai.stream().map(objects -> {
            HoaDon hoaDon = (HoaDon) objects[0];
            TrangThaiHoaDon trangThai = (TrangThaiHoaDon) objects[1];
            LoaiTrangThai loaiTrangThai = (LoaiTrangThai) objects[2];

            HoaDonDTO dto = new HoaDonDTO();

            dto.setIdHoaDon(hoaDon.getIdHoaDon());
            dto.setMaHoaDon(hoaDon.getMaHoaDon());
            dto.setNgayTao(hoaDon.getNgayTao());
            dto.setTenNguoiNhan(hoaDon.getTenNguoiNhan());
            dto.setSdtNguoiNhan(hoaDon.getSdtNguoiNhan());
            dto.setDiaChi(hoaDon.getDiaChi());
            dto.setThanhTien(hoaDon.getThanhTien());
            dto.setGhiChu(hoaDon.getMoTa());

            // Thông tin người dùng
            if (hoaDon.getNguoiDung() != null) {
                dto.setIdNguoiDung(hoaDon.getNguoiDung().getIdNguoiDung());
                dto.setEmail(hoaDon.getNguoiDung().getEmail());
            }

            // Thông tin địa chỉ vận chuyển
            if (hoaDon.getDiaChiVanChuyen() != null) {
                DiaChiVanChuyen diaChiVanChuyen = hoaDon.getDiaChiVanChuyen();
                dto.setIdDiaChiVanChuyen(diaChiVanChuyen.getIdDiaChiVanChuyen());

                if (diaChiVanChuyen.getTinh() != null) {
                    dto.setTinh(diaChiVanChuyen.getTinh().getIdTinh());
                    dto.setTenTinh(diaChiVanChuyen.getTinh().getTenTinh());
                }
                if (diaChiVanChuyen.getHuyen() != null) {
                    dto.setHuyen(diaChiVanChuyen.getHuyen().getIdHuyen());
                    dto.setTenHuyen(diaChiVanChuyen.getHuyen().getTenHuyen());
                }
                if (diaChiVanChuyen.getXa() != null) {
                    dto.setXa(diaChiVanChuyen.getXa().getIdXa());
                    dto.setTenXa(diaChiVanChuyen.getXa().getTenXa());
                }
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

            // Chỉ ánh xạ trạng thái cuối cùng
            if (loaiTrangThai != null && trangThai != null) {
                dto.setIdLoaiTrangThai(loaiTrangThai.getIdLoaiTrangThai());
                dto.setTenLoaiTrangThai(loaiTrangThai.getTenLoaiTrangThai());
                dto.setIdTrangThaiHoaDon(trangThai.getIdTrangThaiHoaDon());
                dto.setTenTrangThai(trangThai.getLoaiTrangThai().getTenLoaiTrangThai());
            }

            return dto;
        }).collect(Collectors.toList());
    }



    public List<HoaDonChiTietDTO> createMultipleHoaDonChiTiet(List<HoaDonChiTietDTO> dtoList, Integer userId) {
        List<HoaDonChiTietDTO> createdHoaDonChiTietList = new ArrayList<>();
        for (HoaDonChiTietDTO dto : dtoList) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            // Tìm kiếm người dùng
            NguoiDung nguoiDung = nguoiDungRepository.findById(userId)
                    .orElseThrow(() -> new ExpressionException("Người dùng không tìm thấy"));
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(dto.getIdSanPhamChiTiet())
                    .orElseThrow(() -> new ExpressionException("Sản phẩm chi tiết không tìm thấy"));
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            HoaDon hoaDon = hoaDonRepository.findById(dto.getIdHoaDon())
                    .orElseThrow(() -> new ExpressionException("Hóa đơn không tìm thấy"));
            GiamGiaSanPham giamGiaSanPham = giamGiaSanPhamRepository.findBySanPham(sanPhamChiTiet.getSanPham())
                    .orElse(null);

            BigDecimal giaKhuyenMai = giamGiaSanPham != null ? giamGiaSanPham.getGiaKhuyenMai() : null;
            BigDecimal giaBan = sanPhamChiTiet.getSanPham().getGiaBan();

            // Tính giá thực tế
            BigDecimal giaTinh = (giaKhuyenMai != null && giaKhuyenMai.compareTo(BigDecimal.ZERO) > 0)
                    ? giaKhuyenMai
                    : giaBan;

            // Tính tổng tiền
            BigDecimal tongTien = giaTinh.multiply(BigDecimal.valueOf(dto.getSoLuong()));
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSoLuong(dto.getSoLuong());
            hoaDonChiTiet.setTongTien(tongTien);
            hoaDonChiTiet.setSoTienThanhToan(dto.getSoTienThanhToan());
            hoaDonChiTiet.setTienTraLai(dto.getTienTraLai());
            hoaDonChiTiet.setMoTa(dto.getMoTa());
            Date now = new Date();
            hoaDonChiTiet.setNgayTao(now);
            hoaDonChiTiet.setNgayCapNhat(now);
            hoaDonChiTiet.setTrangThai(true);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setNgayGiaoDich(now);
            lichSuHoaDon.setSoTienThanhToan(dto.getSoTienThanhToan());
            lichSuHoaDon.setNguoiDung(nguoiDung);
            lichSuHoaDonRepository.save(lichSuHoaDon);
            hoaDonChiTiet.setLichSuHoaDon(lichSuHoaDon);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
            HoaDonChiTietDTO createdDto = new HoaDonChiTietDTO(
                    sanPhamChiTiet.getIdSanPhamChiTiet(),
                    hoaDon.getIdHoaDon(),
                    hoaDon.getMaHoaDon(),
                    hoaDonChiTiet.getSoLuong(),
                    hoaDonChiTiet.getTongTien(),
                    hoaDonChiTiet.getSoTienThanhToan(),
                    hoaDonChiTiet.getTienTraLai(),
                    hoaDonChiTiet.getMoTa()
            );
            createdHoaDonChiTietList.add(createdDto);
        }
        return createdHoaDonChiTietList;
    }



}
