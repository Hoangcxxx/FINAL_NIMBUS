package com.example.duantn.service;

import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.dto.SanPhamChiTietDTO;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class HoaDonService {
    @Autowired
    private HoaDonRepository hoaDonRepository;

    public HoaDonService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

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
    private TrangThaiHoaDonRepository trangThaiHoaDonRepository;




    private final RestTemplate restTemplate;

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

//        // Thông tin trạng thái hóa đơn
//        if (hoaDon.getTrangThaiHoaDon() != null) {
//            dto.setIdtrangthaihoadon(hoaDon.getTrangThaiHoaDon().getIdTrangThaiHoaDon());
//        }

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


    public String createOrder(HoaDonDTO hoaDonDTO, HttpServletRequest res) {
        long currentCount = hoaDonRepository.count();
        String generatedMaHoaDon = "HD00" + (currentCount + 1);

        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon(generatedMaHoaDon);

        // Kiểm tra và lấy thông tin người dùng
        NguoiDung nguoiDung = nguoiDungRepository.findById(hoaDonDTO.getIdNguoiDung())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Người dùng"));
        hoaDon.setNguoiDung(nguoiDung);

        // Lấy hoặc lưu thông tin Tỉnh
        Tinh tinh = tinhRepository.findById(hoaDonDTO.getTinh())
                .orElseGet(() -> tinhRepository.save(fetchCityInfo(hoaDonDTO.getTinh().toString())));

        // Lấy hoặc lưu thông tin Huyện
        Huyen huyen = huyenRepository.findById(hoaDonDTO.getHuyen())
                .orElseGet(() -> huyenRepository.save(fetchDistrictInfo(hoaDonDTO.getHuyen().toString())));

        // Lấy hoặc lưu thông tin Xã
        Xa xa = xaRepository.findById(hoaDonDTO.getXa())
                .orElseGet(() -> xaRepository.save(fetchWardInfo(hoaDonDTO.getXa().toString())));

        // Tạo và lưu đối tượng DiaChiVanChuyen
        DiaChiVanChuyen diaChiVanChuyen = new DiaChiVanChuyen();
        diaChiVanChuyen.setTinh(tinh);
        diaChiVanChuyen.setHuyen(huyen);
        diaChiVanChuyen.setXa(xa);
        diaChiVanChuyen.setTrangThai(true);
        diaChiVanChuyen.setMoTa(String.format("Địa chỉ: %s, %s, %s", xa.getTenXa(), huyen.getTenHuyen(), tinh.getTenTinh()));

        diaChiVanChuyen = diaChiVanChuyenRepository.save(diaChiVanChuyen);

        // Gán địa chỉ vận chuyển đã lưu cho hóa đơn
        hoaDon.setDiaChiVanChuyen(diaChiVanChuyen);

        // Gán thông tin khác cho hóa đơn
        hoaDon.setNgayTao(new Date());
        hoaDon.setTenNguoiNhan(hoaDonDTO.getTenNguoiNhan());
        hoaDon.setDiaChi(hoaDonDTO.getDiaChi());
        hoaDon.setSdtNguoiNhan(hoaDonDTO.getSdtNguoiNhan());
        hoaDon.setThanhTien(hoaDonDTO.getThanhTien());
        hoaDon.setMoTa(hoaDonDTO.getGhiChu());
        hoaDon.setTrangThai(true);

        // Xử lý phương thức thanh toán
        switch (hoaDonDTO.getTenPhuongThucThanhToan()) {
            case "cod":
                saveCODPayment(hoaDon, generatedMaHoaDon, hoaDonDTO);
                break;
            case "vnpay":
                saveVNPayPayment(hoaDon, generatedMaHoaDon, hoaDonDTO, res);
                break;
            case "zalopay":
                throw new RuntimeException("Chức năng ZaloPay đang phát triển");
            default:
                throw new RuntimeException("Phương thức thanh toán không hợp lệ");
        }

        // Lưu hóa đơn sau khi đã gán đầy đủ dữ liệu
        hoaDonRepository.save(hoaDon);

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
            return xa;
        }
        throw new RuntimeException("Không thể lấy dữ liệu xã từ API");
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
//        hoaDon.setTrangThaiHoaDon(hoaDon.getTrangThaiHoaDon());
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
        log.info("Xoa Thanh Cong");
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
                DiaChiVanChuyen diaChiVanChuyen = hoaDon.getDiaChiVanChuyen();

                // Ánh xạ ID của địa chỉ vận chuyển
                dto.setIdDiaChiVanChuyen(diaChiVanChuyen.getIdDiaChiVanChuyen());

                // Ánh xạ các đối tượng Tinh, Huyen, Xa (thay vì ánh xạ đối tượng, lấy ID)
                if (diaChiVanChuyen.getTinh() != null) {
                    dto.setTinh(diaChiVanChuyen.getTinh().getIdTinh()); // Gán ID của Tỉnh vào DTO
                }
                if (diaChiVanChuyen.getHuyen() != null) {
                    dto.setHuyen(diaChiVanChuyen.getHuyen().getIdHuyen()); // Gán ID của Huyện vào DTO
                }
                if (diaChiVanChuyen.getXa() != null) {
                    dto.setXa(diaChiVanChuyen.getXa().getIdXa()); // Gán ID của Xã vào DTO
                }
            }


            // Thông tin phương thức thanh toán
            if (hoaDon.getPhuongThucThanhToanHoaDon() != null) {
                dto.setIdphuongthucthanhtoanhoadon(hoaDon.getPhuongThucThanhToanHoaDon().getIdThanhToanHoaDon());
                dto.setTenPhuongThucThanhToan(hoaDon.getPhuongThucThanhToanHoaDon()
                        .getPhuongThucThanhToan()
                        .getTenPhuongThuc());
            }

//            // trangthaihoadon
//            if (hoaDon.getTrangThaiHoaDon() != null) {
//                dto.setIdtrangthaihoadon(hoaDon.getTrangThaiHoaDon().getIdTrangThaiHoaDon());
//            }

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