package com.example.duantn.service;

import com.example.duantn.dto.*;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
    private PhiVanChuyenRepository phiVanChuyenRepository;
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
        dto.setPhiShip(hoaDon.getPhiShip());
        // Gán thông tin hóa đơn
        dto.setIdHoaDon(hoaDon.getIdHoaDon());
        dto.setMaHoaDon(hoaDon.getMaHoaDon());
        dto.setNgayTao(hoaDon.getNgayTao());
        dto.setTenNguoiNhan(hoaDon.getTenNguoiNhan());
        dto.setSdtNguoiNhan(hoaDon.getSdtNguoiNhan());
        dto.setDiaChi(hoaDon.getDiaChi());
        dto.setThanhTien(hoaDon.getThanhTien());
        dto.setGhiChu(hoaDon.getMoTa());
        dto.setIdNguoiDung(hoaDon.getNguoiDung().getIdNguoiDung());



        if (hoaDon.getVoucher() != null) {
            dto.setGiaTriMavoucher(hoaDon.getVoucher().getGiaTriGiamGia());
            dto.setMavoucher(hoaDon.getVoucher().getMaVoucher());
            dto.setKieuGiamGia(hoaDon.getVoucher().getKieuGiamGia());
        } else {
            dto.setGiaTriMavoucher(BigDecimal.ZERO); // Default value if no voucher
            dto.setMavoucher(null);
            dto.setKieuGiamGia(null); // Ensure this is null if no voucher is present
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
                dto.setXa(String.valueOf(diaChiVanChuyen.getXa().getIdXa()));
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
                    spDTO.setTienSanPham(chiTiet.getTienSanPham());
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

            if (loaiTrangThai.getIdLoaiTrangThai() == 5 || loaiTrangThai.getIdLoaiTrangThai() == 6 || loaiTrangThai.getIdLoaiTrangThai() == 7) {
                // Điều kiện kiểm tra loại trạng thái (5, 6, hoặc 7)

                // Tính toán ngày giao dự kiến
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(hoaDon.getNgayTao());  // Lấy ngày tạo
                int daysToAdd = 5;  // Thêm 5 ngày vào ngày tạo
                calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);  // Cộng thêm số ngày vào ngày tạo
                Date ngayGiaoDuKien = calendar.getTime();  // Đặt ngày giao dự kiến

                // Tính toán ngày kết thúc giao dự kiến (ví dụ 2 ngày sau ngày giao dự kiến)
                calendar.add(Calendar.DAY_OF_MONTH, 2);  // Thêm 2 ngày vào ngày giao dự kiến
                Date ngayKetThucGiaoDuKien = calendar.getTime();  // Đặt ngày kết thúc giao dự kiến

                // Gán giá trị vào DTO
                dto.setNgayGiaoDuKien(ngayGiaoDuKien);
                dto.setNgayKetThucGiaoDuKien(ngayKetThucGiaoDuKien);
            }
        }
        return dto;
    }



    private void saveCODPayment(HoaDon hoaDon, String generatedMaHoaDon, HoaDonDTO hoaDonDTO) {
        // Tạo mã hóa đơn duy nhất
        generatedMaHoaDon = generateMaHoaDon();
        hoaDon.setMaHoaDon(generatedMaHoaDon); // Gán mã hóa đơn cho đối tượng hoaDon

        hoaDonRepository.save(hoaDon); // Lưu hóa đơn vào cơ sở dữ liệu

        // Tạo thông tin phương thức thanh toán COD cho hóa đơn
        PhuongThucThanhToanHoaDon phuongThucThanhToanHoaDon = new PhuongThucThanhToanHoaDon();
        phuongThucThanhToanHoaDon.setPhuongThucThanhToan(phuongThucThanhToanRepository.findById(4).get()); // COD
        phuongThucThanhToanHoaDon.setNgayGiaoDich(new Date());
        phuongThucThanhToanHoaDon.setMoTa("Thanh toán COD cho đơn hàng " + generatedMaHoaDon);
        phuongThucThanhToanHoaDon.setHoaDon(hoaDon);
        phuongThucThanhToanHoaDonRepository.save(phuongThucThanhToanHoaDon);

        hoaDon.setPhuongThucThanhToanHoaDon(phuongThucThanhToanHoaDon);
        hoaDonRepository.save(hoaDon);

        // Kiểm tra và lấy thông tin người dùng
        NguoiDung nguoiDung = nguoiDungRepository.findById(hoaDonDTO.getIdNguoiDung())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Người dùng với ID: " + hoaDonDTO.getIdNguoiDung()));
        hoaDon.setNguoiDung(nguoiDung);

        // Tạo và lưu lịch sử thanh toán
        LichSuThanhToan lichSuThanhToan = new LichSuThanhToan();
        lichSuThanhToan.setSoTienThanhToan(null);
        lichSuThanhToan.setNgayTao(new Date());
        lichSuThanhToan.setNgayGiaoDich(null);  // Hoặc bạn có thể dùng ngày giao dịch từ hệ thống thanh toán
        lichSuThanhToan.setNgayCapNhat(new Date());
        lichSuThanhToan.setTrangThaiThanhToan(false); // Thực tế có thể thay đổi tùy vào trạng thái thanh toán thực tế
        lichSuThanhToan.setHoaDon(hoaDon);
        lichSuThanhToan.setNguoiDung(nguoiDung);  // Liên kết với người dùng

        // Lưu lịch sử thanh toán
        lichSuThanhToanRepository.save(lichSuThanhToan);

        // Tạo trạng thái cho hóa đơn
        TrangThaiHoaDon trangThaiHoaDonTaoDon = new TrangThaiHoaDon();
        TrangThaiHoaDon trangThaiHoaDonChoXuLy = new TrangThaiHoaDon();

        // Trạng thái "Tạo đơn hàng"
        LoaiTrangThai trangThaiTaoDon = loaiTrangThaiRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("Loại trạng thái không tồn tại!"));
        trangThaiHoaDonTaoDon.setMoTa("Tạo đơn hàng");
        trangThaiHoaDonTaoDon.setNgayTao(new Date());
        trangThaiHoaDonTaoDon.setIdNhanVien(1);
        trangThaiHoaDonTaoDon.setNgayCapNhat(new Date());
        trangThaiHoaDonTaoDon.setLoaiTrangThai(trangThaiTaoDon);
        trangThaiHoaDonTaoDon.setHoaDon(hoaDon);
        trangThaiHoaDonRepository.save(trangThaiHoaDonTaoDon);

        // Trạng thái "Chờ xử lý" sau khi thanh toán được chọn
        LoaiTrangThai trangThaiChoXuLy = loaiTrangThaiRepository.findById(2)
                .orElseThrow(() -> new IllegalArgumentException("Loại trạng thái không tồn tại!"));
        trangThaiHoaDonChoXuLy.setMoTa("Chờ xác nhận");
        trangThaiHoaDonChoXuLy.setNgayTao(new Date());
        trangThaiHoaDonChoXuLy.setIdNhanVien(1);
        trangThaiHoaDonChoXuLy.setNgayCapNhat(new Date());
        trangThaiHoaDonChoXuLy.setLoaiTrangThai(trangThaiChoXuLy);
        trangThaiHoaDonChoXuLy.setHoaDon(hoaDon);
        trangThaiHoaDonRepository.save(trangThaiHoaDonChoXuLy);

        // Kiểm tra và lấy thông tin Tỉnh, Huyện, Xã
        if (hoaDonDTO.getTinh() == null || hoaDonDTO.getHuyen() == null || hoaDonDTO.getXa() == null) {
            throw new RuntimeException("Thông tin Tỉnh, Huyện, hoặc Xã không hợp lệ");
        }


        // Lấy hoặc lưu Tỉnh theo mã
        String cityCode = hoaDonDTO.getTinh().toString();  // Lấy mã Tỉnh từ hoaDonDTO

// Truy vấn danh sách tỉnh có mã tỉnh trùng
        List<Tinh> cities = tinhRepository.findAllByMaTinh(cityCode);

// Nếu danh sách không trống, lấy giá trị đầu tiên. Nếu rỗng, gọi API GHN để thêm mới
        Tinh selectedCity = cities.stream()
                .findFirst()  // Lấy phần tử đầu tiên nếu tồn tại
                .orElseGet(() -> {
                    Tinh city = fetchCityInfoFromGHN(cityCode);  // Gọi API GHN nếu không tìm thấy
                    return tinhRepository.save(city);           // Lưu vào DB
                });

// Thêm cảnh báo nếu có nhiều tỉnh trùng mã (tùy chọn)
        if (cities.size() > 1) {
            System.out.println("Cảnh báo: Tìm thấy nhiều tỉnh trùng mã [" + cityCode + "]. Chỉ lấy kết quả đầu tiên.");
        }

// Lấy hoặc lưu Huyện theo mã
        String districtCode = hoaDonDTO.getHuyen().toString();  // Lấy mã Huyện từ hoaDonDTO

// Truy vấn danh sách huyện có mã trùng
        List<Huyen> districts = huyenRepository.findAllByMaHuyen(districtCode);

// Nếu danh sách không trống, lấy giá trị đầu tiên. Nếu rỗng, gọi API GHN để thêm mới
        Huyen selectedDistrict = districts.stream()
                .findFirst()
                .orElseGet(() -> {
                    Huyen district = fetchDistrictInfoFromGHN(districtCode);  // Gọi API GHN nếu không có trong DB
                    district.setTinh(selectedCity);                          // Gán tỉnh liên quan
                    return huyenRepository.save(district);                   // Lưu vào DB
                });

        if (districts.size() > 1) {
            System.out.println("Cảnh báo: Tìm thấy nhiều huyện trùng mã [" + districtCode + "]. Chỉ lấy kết quả đầu tiên.");
        }

// Lấy hoặc lưu Xã theo mã
        String wardCode = hoaDonDTO.getXa().toString();  // Lấy mã Xã từ hoaDonDTO

// Truy vấn danh sách xã có mã trùng
        List<Xa> wards = xaRepository.findAllByMaXa(wardCode);

// Nếu danh sách không trống, lấy giá trị đầu tiên. Nếu rỗng, gọi API GHN để thêm mới
        Xa selectedWard = wards.stream()
                .findFirst()
                .orElseGet(() -> {
                    Xa ward = fetchWardInfoFromGHN(districtCode, wardCode);  // Gọi API GHN với mã huyện và mã xã
                    ward.setHuyen(selectedDistrict);                        // Gán huyện liên quan
                    return xaRepository.save(ward);                         // Lưu vào DB
                });

        if (wards.size() > 1) {
            System.out.println("Cảnh báo: Tìm thấy nhiều xã trùng mã [" + wardCode + "]. Chỉ lấy kết quả đầu tiên.");
        }



        // Tạo và lưu đối tượng DiaChiVanChuyen
        DiaChiVanChuyen diaChiVanChuyen = new DiaChiVanChuyen();
        diaChiVanChuyen.setTinh(selectedCity);
        diaChiVanChuyen.setNguoiDung(nguoiDung);
        diaChiVanChuyen.setDiaChiCuThe(hoaDonDTO.getDiaChiCuThe());
        diaChiVanChuyen.setHuyen(selectedDistrict);
        diaChiVanChuyen.setXa(selectedWard);
        diaChiVanChuyen.setTrangThai(true);
        diaChiVanChuyen.setNgayTao(new Date());
        diaChiVanChuyen.setNgayCapNhat(new Date());
        diaChiVanChuyen.setMoTa(String.format("Địa chỉ: %s, %s, %s", selectedWard.getTenXa(), selectedDistrict.getTenHuyen(), selectedCity.getTenTinh()));

        diaChiVanChuyen = diaChiVanChuyenRepository.save(diaChiVanChuyen);

        // Gán địa chỉ vận chuyển đã lưu cho hóa đơn
        hoaDon.setDiaChiVanChuyen(diaChiVanChuyen);

        // Khởi tạo đối tượng Phí vận chuyển
        PhiVanChuyen phiVanChuyen = new PhiVanChuyen();
        phiVanChuyen.setHoaDon(hoaDon);  // Liên kết với hóa đơn đã lưu
        phiVanChuyen.setDiaChiVanChuyen(diaChiVanChuyen);
        phiVanChuyen.setSoTienVanChuyen(BigDecimal.valueOf(22000));
        phiVanChuyen.setTrangThai(true);
        phiVanChuyen.setMoTa("Phí vận chuyển cho hóa đơn mã: " + hoaDon.getMaHoaDon());

        // Lưu phí vận chuyển
        phiVanChuyen = phiVanChuyenRepository.save(phiVanChuyen);

        // Gán phí vận chuyển vào hóa đơn
        hoaDon.setPhiShip(phiVanChuyen.getSoTienVanChuyen());

        // Xử lý các sản phẩm trong giỏ hàng
        processCartItems(hoaDonDTO, hoaDon);
    }


    private void saveVNPayPayment(HoaDon hoaDon, String generatedMaHoaDon, HoaDonDTO hoaDonDTO, HttpServletRequest req) {
        try {
            // Gửi yêu cầu thanh toán VNPay và lấy URL thanh toán
            String paymentUrl = paymentService.createPayment(hoaDonDTO.getThanhTien().longValue(), "vnpay", req);
            System.out.println("URL thanh toán VNPay: " + paymentUrl);
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
            hoaDonChiTiet.setTienSanPham(sanPham.getGiaTien());
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

    public HoaDon apithanhtoanvnpay(HoaDonDTO hoaDonDTO, HttpServletRequest request, HttpServletResponse response, String generatedMaHoaDon, HoaDon hoaDon) {
        if (hoaDonDTO.getIdNguoiDung() == null) {
            throw new RuntimeException("ID Người dùng không được null");
        }
        long currentCount = hoaDonRepository.count();
        generatedMaHoaDon = "HD00" + (currentCount + 1);

        hoaDon.setMaHoaDon(generatedMaHoaDon);

        // Kiểm tra và lấy thông tin người dùng
        NguoiDung nguoiDung = nguoiDungRepository.findById(hoaDonDTO.getIdNguoiDung()).orElseThrow(() -> new RuntimeException("Không tìm thấy Người dùng với ID: " + hoaDonDTO.getIdNguoiDung()));
        hoaDon.setNguoiDung(nguoiDung);


        // Kiểm tra nếu idvoucher không phải null trước khi thực hiện các thao tác
        if (hoaDonDTO.getIdvoucher() != null) {
            Voucher voucher = voucherRepository.findById(hoaDonDTO.getIdvoucher()).orElseThrow(() -> new RuntimeException("Không tìm thấy Voucher với ID: " + hoaDonDTO.getIdvoucher()));

            if (voucher.getSoLuong() <= 0) {
                throw new RuntimeException("Voucher này đã hết số lượng sử dụng.");
            }

            voucher.setSoLuong(voucher.getSoLuong() - 1);
            voucherRepository.save(voucher);

            hoaDon.setVoucher(voucher);
        } else {
            hoaDon.setVoucher(null);
        }

        hoaDon.setNgayTao(new Date());
        hoaDon.setTenNguoiNhan(hoaDonDTO.getTenNguoiNhan());
        hoaDon.setDiaChi(hoaDonDTO.getDiaChi());
        hoaDon.setSdtNguoiNhan(hoaDonDTO.getSdtNguoiNhan());
        hoaDon.setThanhTien(hoaDonDTO.getThanhTien());
        hoaDon.setMoTa(hoaDonDTO.getGhiChu());
        hoaDon.setTrangThai(true);
        hoaDon.setLoai(1);


        hoaDonRepository.save(hoaDon);
        PhuongThucThanhToanHoaDon phuongThucThanhToanHoaDon = new PhuongThucThanhToanHoaDon();
        phuongThucThanhToanHoaDon.setPhuongThucThanhToan(phuongThucThanhToanRepository.findById(2).get()); // COD
        phuongThucThanhToanHoaDon.setNgayGiaoDich(new Date());
        phuongThucThanhToanHoaDon.setMoTa("Thanh toán vnpay cho đơn hàng " + generatedMaHoaDon);
        phuongThucThanhToanHoaDon.setHoaDon(hoaDon);
        phuongThucThanhToanHoaDonRepository.save(phuongThucThanhToanHoaDon);
        hoaDon.setPhuongThucThanhToanHoaDon(phuongThucThanhToanHoaDon);
        hoaDonRepository.save(hoaDon);


        // Tạo và lưu lịch sử thanh toán
        LichSuThanhToan lichSuThanhToan = new LichSuThanhToan();
        lichSuThanhToan.setSoTienThanhToan(hoaDonDTO.getThanhTien());
        lichSuThanhToan.setNgayTao(new Date());
        lichSuThanhToan.setNgayGiaoDich(new Date());  // Hoặc bạn có thể dùng ngày giao dịch từ hệ thống thanh toán
        lichSuThanhToan.setNgayCapNhat(new Date());
        lichSuThanhToan.setTrangThaiThanhToan(false); // Thực tế có thể thay đổi tùy vào trạng thái thanh toán thực tế
        lichSuThanhToan.setHoaDon(hoaDon);
        lichSuThanhToan.setNguoiDung(nguoiDung);  // Liên kết với người dùng
        lichSuThanhToanRepository.save(lichSuThanhToan);
        // Thêm trạng thái "Tạo đơn hàng" - Trạng thái mới của hóa đơn sau khi tạo
        TrangThaiHoaDon trangThaiHoaDonTaoDon = new TrangThaiHoaDon();
        TrangThaiHoaDon trangThaiHoaDonChoXuLy = new TrangThaiHoaDon();

        // Trạng thái "Tạo đơn hàng"
        LoaiTrangThai trangThaiTaoDon = loaiTrangThaiRepository.findById(1).orElseThrow(() -> new IllegalArgumentException("Loại trạng thái không tồn tại!"));
        trangThaiHoaDonTaoDon.setMoTa("Tạo đơn hàng");
        trangThaiHoaDonTaoDon.setNgayTao(new Date());
        trangThaiHoaDonTaoDon.setNgayCapNhat(new Date());
        trangThaiHoaDonTaoDon.setIdNhanVien(1);
        trangThaiHoaDonTaoDon.setLoaiTrangThai(trangThaiTaoDon);
        trangThaiHoaDonTaoDon.setHoaDon(hoaDon);
        trangThaiHoaDonRepository.save(trangThaiHoaDonTaoDon);

        // Trạng thái "Chờ xử lý" sau khi thanh toán được chọn
        LoaiTrangThai trangThaiChoXuLy = loaiTrangThaiRepository.findById(2).orElseThrow(() -> new IllegalArgumentException("Loại trạng thái không tồn tại!"));
        trangThaiHoaDonChoXuLy.setMoTa("Chờ xác nhận");
        trangThaiHoaDonChoXuLy.setNgayTao(new Date());
        trangThaiHoaDonChoXuLy.setNgayCapNhat(new Date());
        trangThaiHoaDonChoXuLy.setIdNhanVien(1);
        trangThaiHoaDonChoXuLy.setLoaiTrangThai(trangThaiChoXuLy);
        trangThaiHoaDonChoXuLy.setHoaDon(hoaDon);
        trangThaiHoaDonRepository.save(trangThaiHoaDonChoXuLy);

        lichSuThanhToanRepository.save(lichSuThanhToan);
        // Lấy hoặc lưu thông tin Tỉnh, Huyện, Xã
        if (hoaDonDTO.getTinh() == null || hoaDonDTO.getHuyen() == null || hoaDonDTO.getXa() == null) {
            throw new RuntimeException("Thông tin Tỉnh, Huyện, hoặc Xã không hợp lệ");
        }

        // Lấy hoặc lưu Tỉnh theo mã
        String cityCode = hoaDonDTO.getTinh().toString();  // Lấy mã Tỉnh từ hoaDonDTO

// Truy vấn danh sách tỉnh có mã tỉnh trùng
        List<Tinh> cities = tinhRepository.findAllByMaTinh(cityCode);

// Nếu danh sách không trống, lấy giá trị đầu tiên. Nếu rỗng, gọi API GHN để thêm mới
        Tinh selectedCity = cities.stream()
                .findFirst()  // Lấy phần tử đầu tiên nếu tồn tại
                .orElseGet(() -> {
                    Tinh city = fetchCityInfoFromGHN(cityCode);  // Gọi API GHN nếu không tìm thấy
                    return tinhRepository.save(city);           // Lưu vào DB
                });

// Thêm cảnh báo nếu có nhiều tỉnh trùng mã (tùy chọn)
        if (cities.size() > 1) {
            System.out.println("Cảnh báo: Tìm thấy nhiều tỉnh trùng mã [" + cityCode + "]. Chỉ lấy kết quả đầu tiên.");
        }

// Lấy hoặc lưu Huyện theo mã
        String districtCode = hoaDonDTO.getHuyen().toString();  // Lấy mã Huyện từ hoaDonDTO

// Truy vấn danh sách huyện có mã trùng
        List<Huyen> districts = huyenRepository.findAllByMaHuyen(districtCode);

// Nếu danh sách không trống, lấy giá trị đầu tiên. Nếu rỗng, gọi API GHN để thêm mới
        Huyen selectedDistrict = districts.stream()
                .findFirst()
                .orElseGet(() -> {
                    Huyen district = fetchDistrictInfoFromGHN(districtCode);  // Gọi API GHN nếu không có trong DB
                    district.setTinh(selectedCity);                          // Gán tỉnh liên quan
                    return huyenRepository.save(district);                   // Lưu vào DB
                });

        if (districts.size() > 1) {
            System.out.println("Cảnh báo: Tìm thấy nhiều huyện trùng mã [" + districtCode + "]. Chỉ lấy kết quả đầu tiên.");
        }

// Lấy hoặc lưu Xã theo mã
        String wardCode = hoaDonDTO.getXa().toString();  // Lấy mã Xã từ hoaDonDTO

// Truy vấn danh sách xã có mã trùng
        List<Xa> wards = xaRepository.findAllByMaXa(wardCode);

// Nếu danh sách không trống, lấy giá trị đầu tiên. Nếu rỗng, gọi API GHN để thêm mới
        Xa selectedWard = wards.stream()
                .findFirst()
                .orElseGet(() -> {
                    Xa ward = fetchWardInfoFromGHN(districtCode, wardCode);  // Gọi API GHN với mã huyện và mã xã
                    ward.setHuyen(selectedDistrict);                        // Gán huyện liên quan
                    return xaRepository.save(ward);                         // Lưu vào DB
                });

        if (wards.size() > 1) {
            System.out.println("Cảnh báo: Tìm thấy nhiều xã trùng mã [" + wardCode + "]. Chỉ lấy kết quả đầu tiên.");
        }


        // Tạo và lưu đối tượng DiaChiVanChuyen
        DiaChiVanChuyen diaChiVanChuyen = new DiaChiVanChuyen();
        diaChiVanChuyen.setTinh(selectedCity);
        diaChiVanChuyen.setNguoiDung(nguoiDung);
        diaChiVanChuyen.setDiaChiCuThe(hoaDonDTO.getDiaChiCuThe());
        diaChiVanChuyen.setHuyen(selectedDistrict);
        diaChiVanChuyen.setXa(selectedWard);
        diaChiVanChuyen.setTrangThai(true);
        diaChiVanChuyen.setNgayTao(new Date());
        diaChiVanChuyen.setNgayCapNhat(new Date());
        diaChiVanChuyen.setMoTa(String.format("Địa chỉ: %s, %s, %s", selectedWard.getTenXa(), selectedDistrict.getTenHuyen(), selectedCity.getTenTinh()));

        diaChiVanChuyen = diaChiVanChuyenRepository.save(diaChiVanChuyen);

        // Gán địa chỉ vận chuyển đã lưu cho hóa đơn
        hoaDon.setDiaChiVanChuyen(diaChiVanChuyen);
        // Khởi tạo đối tượng Phí vận chuyển

        PhiVanChuyen phiVanChuyen = new PhiVanChuyen();
        phiVanChuyen.setHoaDon(hoaDon);  // Liên kết với hóa đơn đã lưu
        phiVanChuyen.setDiaChiVanChuyen(diaChiVanChuyen);
        phiVanChuyen.setSoTienVanChuyen(BigDecimal.valueOf(22000));
        phiVanChuyen.setTrangThai(true);
        phiVanChuyen.setMoTa("Phí vận chuyển cho hóa đơn mã: " + hoaDon.getMaHoaDon());

        // Lưu phí vận chuyển
        phiVanChuyen = phiVanChuyenRepository.save(phiVanChuyen);

        // Gán phí vận chuyển vào hóa đơn
        hoaDon.setPhiShip(phiVanChuyen.getSoTienVanChuyen());


        processCartItemsvnpay(hoaDonDTO, hoaDon);
        updateHoaDonAfterPaymentSuccess(hoaDon, generatedMaHoaDon);
        // Không lưu hóa đơn ngay tại đây, sẽ lưu sau khi nhận thông báo thanh toán từ VNPa
        return hoaDon;

    }


    private void processCartItemsvnpay(HoaDonDTO hoaDonDTO, HoaDon hoaDon) {
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
            hoaDonChiTiet.setTienSanPham(sanPham.getGiaTien());
            hoaDonChiTietRepository.save(hoaDonChiTiet);
        });

        // Sau khi tạo hóa đơn, clear giỏ hàng
        clearGioHangByUserId(hoaDonDTO.getCartId());
    }

    public HoaDon createOrder(HoaDonDTO hoaDonDTO, HttpServletRequest res, HttpServletResponse res1) {
        if (hoaDonDTO.getIdNguoiDung() == null) {
            throw new RuntimeException("ID Người dùng không được null");
        }
        long currentCount = hoaDonRepository.count();
        String generatedMaHoaDon = "HD00" + (currentCount + 1);

        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon(generatedMaHoaDon);


        // Kiểm tra và lấy thông tin người dùng
        NguoiDung nguoiDung = nguoiDungRepository.findById(hoaDonDTO.getIdNguoiDung()).orElseThrow(() -> new RuntimeException("Không tìm thấy Người dùng với ID: " + hoaDonDTO.getIdNguoiDung()));
        hoaDon.setNguoiDung(nguoiDung);


        // Kiểm tra nếu idvoucher không phải null trước khi thực hiện các thao tác
        if (hoaDonDTO.getIdvoucher() != null) {
            Voucher voucher = voucherRepository.findById(hoaDonDTO.getIdvoucher()).orElseThrow(() -> new RuntimeException("Không tìm thấy Voucher với ID: " + hoaDonDTO.getIdvoucher()));

            if (voucher.getSoLuong() <= 0) {
                throw new RuntimeException("Voucher này đã hết số lượng sử dụng.");
            }

            voucher.setSoLuong(voucher.getSoLuong() - 1);
            voucherRepository.save(voucher);

            hoaDon.setVoucher(voucher);
        } else {
            hoaDon.setVoucher(null);
        }

        hoaDon.setNgayTao(new Date());
        hoaDon.setTenNguoiNhan(hoaDonDTO.getTenNguoiNhan());
        hoaDon.setDiaChi(hoaDonDTO.getDiaChi());
        hoaDon.setSdtNguoiNhan(hoaDonDTO.getSdtNguoiNhan());
        hoaDon.setThanhTien(hoaDonDTO.getThanhTien());
        hoaDon.setMoTa(hoaDonDTO.getGhiChu());
        hoaDon.setTrangThai(true);
        hoaDon.setLoai(1);

        LoaiTrangThai loaiTrangThai;
        switch (hoaDonDTO.getTenPhuongThucThanhToan()) {
            case "cod":
                saveCODPayment(hoaDon, generatedMaHoaDon, hoaDonDTO);
                loaiTrangThai = loaiTrangThaiRepository.findById(2).orElseThrow(() -> new IllegalArgumentException("Loại trạng thái không tồn tại!"));
                break;
            case "vnpay":
                saveVNPayPayment(hoaDon, generatedMaHoaDon, hoaDonDTO, res);
                break;
            default:
                throw new RuntimeException("Phương thức thanh toán không hợp lệ");
        }

        hoaDonDTO.setIdHoaDon(hoaDon.getIdHoaDon());
        return hoaDon;
    }

    // Lấy thông tin Tỉnh từ GHN API
    private Tinh fetchCityInfoFromGHN(String cityCode) {
        String url = "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", "347f8e0e-981c-11ef-a905-420459bb4727"); // Thay bằng token của bạn
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Gửi yêu cầu và nhận phản hồi từ API
        Map<String, Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class).getBody();

        if (response != null && response.get("code").equals(200)) {
            // Lấy danh sách tỉnh từ phản hồi
            List<Map<String, Object>> provinces = (List<Map<String, Object>>) response.get("data");

            for (Map<String, Object> province : provinces) {
                // Kiểm tra nếu `ProvinceID` trùng với `cityCode`
                if (province.get("ProvinceID").toString().equals(cityCode)) {
                    // Tạo đối tượng Tinh từ dữ liệu JSON
                    Tinh tinh = new Tinh();

                    // Lấy và gán `maTinh` (Mã Tỉnh)
                    String maTinhString = province.get("ProvinceID").toString();
                    try {
                        tinh.setMaTinh(String.valueOf(Integer.parseInt(maTinhString))); // Chuyển đổi sang chuỗi nếu cần
                    } catch (NumberFormatException e) {
                        System.out.println("Lỗi khi chuyển đổi mã tỉnh: " + maTinhString);
                        tinh.setMaTinh("0"); // Gán giá trị mặc định nếu lỗi
                    }

                    // Gán `tenTinh` (Tên Tỉnh)
                    tinh.setTenTinh(province.get("ProvinceName").toString());

                    // Gán thông tin ngày tạo và cập nhật
                    tinh.setNgayTao(new Date());
                    tinh.setNgayCapNhat(new Date());

                    return tinh;
                }
            }
        }

        // Nếu không tìm thấy mã tỉnh, ném ngoại lệ
        throw new RuntimeException("Không thể lấy dữ liệu tỉnh từ GHN API");
    }


    // Lấy thông tin Huyện từ GHN API
    private Huyen fetchDistrictInfoFromGHN(String districtCode) {
        String url = "https://online-gateway.ghn.vn/shiip/public-api/master-data/district";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", "347f8e0e-981c-11ef-a905-420459bb4727"); // Thay bằng token của bạn
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Gửi yêu cầu và nhận phản hồi từ API
        Map<String, Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class).getBody();

        // Kiểm tra nếu phản hồi hợp lệ và mã trạng thái là 200
        if (response != null && response.get("code").equals(200)) {
            // Lấy danh sách huyện từ phản hồi
            List<Map<String, Object>> districts = (List<Map<String, Object>>) response.get("data");

            // Duyệt qua danh sách huyện
            for (Map<String, Object> district : districts) {
                // Kiểm tra nếu `DistrictID` trùng với `districtCode`
                if (district.get("DistrictID").toString().equals(districtCode)) {
                    // Tạo đối tượng Huyen từ dữ liệu JSON
                    Huyen huyen = new Huyen();

                    // Lấy mã huyện và chuyển đổi
                    String maHuyenString = district.get("DistrictID").toString();
                    try {
                        // Chuyển đổi mã huyện từ String sang Integer
                        huyen.setMaHuyen(String.valueOf(Integer.parseInt(maHuyenString)));
                    } catch (NumberFormatException e) {
                        System.out.println("Lỗi khi chuyển đổi mã huyện: " + maHuyenString);
                        huyen.setMaHuyen(String.valueOf(0));  // Gán giá trị mặc định nếu không chuyển đổi được
                    }

                    // Gán tên huyện
                    huyen.setTenHuyen(district.get("DistrictName").toString());

                    // Gán thông tin ngày tạo và cập nhật
                    huyen.setNgayTao(new Date());
                    huyen.setNgayCapNhat(new Date());

                    return huyen;  // Trả về đối tượng Huyen sau khi đã gán thông tin
                }
            }
        }

        // Nếu không tìm thấy huyện theo mã `districtCode`, ném ngoại lệ
        throw new RuntimeException("Không thể lấy dữ liệu huyện từ GHN API");
    }



    // Lấy thông tin Xã từ GHN API
    private Xa fetchWardInfoFromGHN(String districtCode, String wardCode) {
        String url = "https://online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=" + districtCode;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", "347f8e0e-981c-11ef-a905-420459bb4727"); // Thay bằng token của bạn
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Gọi API
        Map<String, Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class).getBody();

        // Kiểm tra dữ liệu trả về
        if (response != null && response.get("code").equals(200)) {
            // Lấy danh sách xã từ phản hồi
            List<Map<String, Object>> wards = (List<Map<String, Object>>) response.get("data");

            // Duyệt qua danh sách xã để tìm xã theo mã `wardCode`
            for (Map<String, Object> ward : wards) {
                // Kiểm tra nếu `WardCode` trùng với `wardCode`
                if (ward.get("WardCode").toString().equals(wardCode)) {
                    Xa xa = new Xa();

                    // Lấy mã xã và chuyển đổi
                    String maXaString = ward.get("WardCode").toString();
                    try {
                        // Chuyển đổi mã xã từ String sang Integer
                        xa.setMaXa(String.valueOf(Integer.parseInt(maXaString)));
                    } catch (NumberFormatException e) {
                        System.out.println("Lỗi khi chuyển đổi mã xã: " + maXaString);
                        xa.setMaXa(String.valueOf(0));  // Gán giá trị mặc định nếu không chuyển đổi được
                    }

                    // Gán tên xã
                    xa.setTenXa(ward.get("WardName").toString());

                    // Gán ngày tạo và ngày cập nhật cho xã
                    xa.setNgayTao(new Date());
                    xa.setNgayCapNhat(new Date());

                    return xa;  // Trả về đối tượng Xa sau khi đã gán giá trị
                }
            }
        }

        // Nếu không tìm thấy xã hoặc có lỗi, ném ngoại lệ
        throw new RuntimeException("Không thể lấy dữ liệu xã từ GHN API");
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

    public HoaDon createHoaDon(HoaDon hoaDon, Integer idNhanVien) {
        String newMaHoaDon = generateMaHoaDon();
        hoaDon.setMaHoaDon(newMaHoaDon);
        hoaDon.setIdNhanVien(idNhanVien);
        hoaDon.setLoai(0);
        hoaDon.setTrangThai(false);
        HoaDon savedHoaDon = hoaDonRepository.save(hoaDon);

        // Tạo trạng thái hóa đơn với loại trạng thái là 1
        TrangThaiHoaDon trangThaiHoaDon1 = new TrangThaiHoaDon();
        trangThaiHoaDon1.setMoTa("Tạo đơn hàng");
        trangThaiHoaDon1.setNgayTao(new Date());
        trangThaiHoaDon1.setNgayCapNhat(new Date());
        trangThaiHoaDon1.setIdNhanVien(idNhanVien);  // Lưu idNhanVien vào trạng thái hóa đơn

        LoaiTrangThai loaiTrangThai1 = loaiTrangThaiRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("Loại trạng thái không tồn tại!"));

        trangThaiHoaDon1.setLoaiTrangThai(loaiTrangThai1);
        trangThaiHoaDon1.setHoaDon(savedHoaDon);
        trangThaiHoaDonRepository.save(trangThaiHoaDon1);

        // Tạo trạng thái hóa đơn với loại trạng thái là 4
        TrangThaiHoaDon trangThaiHoaDon2 = new TrangThaiHoaDon();
        trangThaiHoaDon2.setMoTa("Chờ xác nhận");
        trangThaiHoaDon2.setNgayTao(new Date());
        trangThaiHoaDon2.setNgayCapNhat(new Date());
        trangThaiHoaDon2.setIdNhanVien(idNhanVien);  // Lưu idNhanVien vào trạng thái hóa đơn

        LoaiTrangThai loaiTrangThai2 = loaiTrangThaiRepository.findById(2)
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
    public HoaDonResponseDTO updateHoaDon(int idHoaDon, HoaDonUpdateDTO updateHoaDonDTO, Integer idNhanVien) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

        hoaDon.setTrangThai(true);
        hoaDon.setPhiShip(updateHoaDonDTO.getPhiShip());
        hoaDon.setNgayThanhToan(updateHoaDonDTO.getNgayThanhToan());
        hoaDon.setThanhTien(updateHoaDonDTO.getThanhTien());
        hoaDon.setSdtNguoiNhan(updateHoaDonDTO.getSetSdtNguoiNhan());

        PhuongThucThanhToanHoaDon existingPtThanhToan = ptThanhToanHoaDonRepository.findById(updateHoaDonDTO.getIdThanhToanHoaDon())
                .orElseThrow(() -> new RuntimeException("Phương thức thanh toán không tồn tại."));
        hoaDon.setPhuongThucThanhToanHoaDon(existingPtThanhToan);

        if (updateHoaDonDTO.getIdVoucher() > 0) {
            Voucher voucher = voucherRepository.findById(updateHoaDonDTO.getIdVoucher())
                    .orElseThrow(() -> new RuntimeException("Voucher không tồn tại"));
            hoaDon.setVoucher(voucher);
        }

        // Thêm trạng thái hóa đơn loại 13 (Chờ thanh toán)
        TrangThaiHoaDon trangThaiHoaDon13 = new TrangThaiHoaDon();
        trangThaiHoaDon13.setMoTa("Chờ thanh toán");
        trangThaiHoaDon13.setNgayTao(new Date());
        trangThaiHoaDon13.setNgayCapNhat(new Date());
        trangThaiHoaDon13.setHoaDon(hoaDon);
        trangThaiHoaDon13.setIdNhanVien(idNhanVien);

        LoaiTrangThai loaiTrangThai13 = loaiTrangThaiRepository.findById(13)
                .orElseThrow(() -> new RuntimeException("Loại trạng thái không tồn tại"));
        trangThaiHoaDon13.setLoaiTrangThai(loaiTrangThai13);

        trangThaiHoaDonRepository.save(trangThaiHoaDon13);

        // Thêm trạng thái hóa đơn loại 14 (Đã thanh toán thành công)
        TrangThaiHoaDon trangThaiHoaDon14 = new TrangThaiHoaDon();
        trangThaiHoaDon14.setMoTa("Đã thanh toán thành công");
        trangThaiHoaDon14.setNgayTao(new Date());
        trangThaiHoaDon14.setNgayCapNhat(new Date());
        trangThaiHoaDon14.setHoaDon(hoaDon);
        trangThaiHoaDon14.setIdNhanVien(idNhanVien);

        LoaiTrangThai loaiTrangThai14 = loaiTrangThaiRepository.findById(14)
                .orElseThrow(() -> new RuntimeException("Loại trạng thái không tồn tại"));
        trangThaiHoaDon14.setLoaiTrangThai(loaiTrangThai14);

        trangThaiHoaDonRepository.save(trangThaiHoaDon14);

        // Thêm trạng thái hóa đơn loại 15 (Hoàn thành)
        TrangThaiHoaDon trangThaiHoaDon15 = new TrangThaiHoaDon();
        trangThaiHoaDon15.setMoTa("Hoàn thành");
        trangThaiHoaDon15.setNgayTao(new Date());
        trangThaiHoaDon15.setNgayCapNhat(new Date());
        trangThaiHoaDon15.setHoaDon(hoaDon);
        trangThaiHoaDon15.setIdNhanVien(idNhanVien);

        LoaiTrangThai loaiTrangThai15 = loaiTrangThaiRepository.findById(15)
                .orElseThrow(() -> new RuntimeException("Loại trạng thái không tồn tại"));
        trangThaiHoaDon15.setLoaiTrangThai(loaiTrangThai15);

        trangThaiHoaDonRepository.save(trangThaiHoaDon15);

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
                    dto.setXa(String.valueOf(diaChiVanChuyen.getXa().getIdXa()));
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

    // Tìm kiếm hóa đơn theo mã hóa đơn
    public List<Object[]> searchHoaDonByMaHoaDon(String maHoaDon) {
        return hoaDonRepository.searchHoaDonByMaHoaDon(maHoaDon);
    }

    public String hoanTraSanPhamTheoId(Integer idSanPhamChiTiet, Integer soLuongHoanTra, Integer idHoaDon, Integer idLoaiTrangThai, Integer idNhanVien) {
        // Kiểm tra nếu hóa đơn và loại trạng thái tồn tại
        Optional<HoaDon> hoaDonOpt = hoaDonRepository.findById(idHoaDon);
        Optional<LoaiTrangThai> loaiTrangThaiOpt = loaiTrangThaiRepository.findById(idLoaiTrangThai);

        if (!hoaDonOpt.isPresent()) {
            return "Không tìm thấy hóa đơn với ID " + idHoaDon;
        }

        if (!loaiTrangThaiOpt.isPresent()) {
            return "Không tìm thấy loại trạng thái với ID " + idLoaiTrangThai;
        }

        HoaDon hoaDon = hoaDonOpt.get();
        LoaiTrangThai loaiTrangThai = loaiTrangThaiOpt.get();

        if (idLoaiTrangThai == 8) {
            // Lưu trạng thái hủy
            TrangThaiHoaDon trangThaiHoaDon = new TrangThaiHoaDon();
            trangThaiHoaDon.setHoaDon(hoaDon);
            trangThaiHoaDon.setLoaiTrangThai(loaiTrangThai);
            trangThaiHoaDon.setNgayTao(new Date());
            trangThaiHoaDon.setNgayCapNhat(new Date());
            trangThaiHoaDon.setIdNhanVien(idNhanVien); // Gán idNhanVien cho trạng thái hiện tại
            trangThaiHoaDon.setMoTa(loaiTrangThai.getMoTa());

            // Lưu trạng thái hủy vào database
            TrangThaiHoaDon savedTrangThai = trangThaiHoaDonRepository.save(trangThaiHoaDon);
            System.out.println("Trạng thái 'Hủy đơn hàng' đã được lưu: " + savedTrangThai);

            // Tìm sản phẩm chi tiết bằng ID
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(idSanPhamChiTiet).orElse(null);

            if (sanPhamChiTiet == null) {
                return "Không tìm thấy sản phẩm chi tiết với ID " + idSanPhamChiTiet;
            }

            // Tìm các chi tiết hóa đơn tương ứng với sản phẩm chi tiết này
            List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findBySanPhamChiTiet(sanPhamChiTiet);

            if (hoaDonChiTietList.isEmpty()) {
                return "Không có chi tiết hóa đơn nào với sản phẩm này.";
            }

            // Duyệt qua các chi tiết hóa đơn và thực hiện hoàn trả
            for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {
                // Kiểm tra xem có đủ số lượng để hoàn trả không
                if (hoaDonChiTiet.getSoLuong() < soLuongHoanTra) {
                    return "Số lượng hoàn trả vượt quá số lượng đã mua cho sản phẩm " + sanPhamChiTiet.getSanPham().getTenSanPham();
                }

                // Cập nhật số lượng tồn kho
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + soLuongHoanTra);
                sanPhamChiTietRepository.save(sanPhamChiTiet);

                // Cập nhật số lượng trong HoaDonChiTiet
                hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() - soLuongHoanTra);

                // Lưu lại thay đổi cho HoaDonChiTiet
                hoaDonChiTietRepository.save(hoaDonChiTiet);
            }
        } else {
            return "Chỉ có thể hoàn trả khi hóa đơn đã hoàn thành.";
        }

        return "Hoàn trả thành công!";
    }
    public void luuVoucher(String maHoaDon, String maVoucher, String tenVoucher, BigDecimal giaTriGiamGiaApDung, Boolean kieuGiamGiaApDung) {
        // Tìm hóa đơn theo mã hóa đơn, sử dụng Optional để kiểm tra có tìm thấy hay không
        Optional<HoaDon> hoaDonOptional = hoaDonRepository.findByMaHoaDon(maHoaDon);

        // Nếu không tìm thấy hóa đơn, throw exception
        if (!hoaDonOptional.isPresent()) {
            throw new IllegalArgumentException("Hóa đơn không tồn tại.");
        }

        HoaDon hoaDon = hoaDonOptional.get();

        // Cập nhật các giá trị voucher vào hóa đơn
        hoaDon.setMaVoucherApDung(maVoucher);
        hoaDon.setTenVoucherApDung(tenVoucher);
        hoaDon.setGiaTriGiamGiaApDung(giaTriGiamGiaApDung != null ? giaTriGiamGiaApDung : BigDecimal.ZERO);
        hoaDon.setKieuGiamGiaApDung(kieuGiamGiaApDung != null ? kieuGiamGiaApDung : false);

        // Lưu hóa đơn và cập nhật ngay lập tức bằng phương thức saveAndFlush
        hoaDonRepository.saveAndFlush(hoaDon);
    }

}
