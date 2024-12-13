package com.example.duantn.service;

import com.example.duantn.config.Config;
import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.FlashMapManager;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
@Service
public class vnpayhahaSevice {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private LichSuThanhToanRepository lichSuThanhToanRepository;
    @Autowired
    private LichSuHoaDonRepository lichSuHoaDonRepository;
    @Autowired
    private GiamGiaSanPhamRepository giamGiaSanPhamRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

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
    private vnpayhahaSevice paymentService;

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
    private PhiVanChuyenRepository phiVanChuyenRepository;
    @Autowired
    private LoaiTrangThaiRepository loaiTrangThaiRepository;
    @Autowired
    private TrangThaiHoaDonRepository trangThaiHoaDonRepository;

    @Autowired
    private PhuongThucThanhToanHoaDonRepository ptThanhToanHoaDonRepository;

    public String createPayment(long amount, String paymentMethod, HttpServletRequest req) throws UnsupportedEncodingException {
        String orderType = "other"; // Có thể thay đổi tùy theo loại đơn hàng
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = Config.getIpAddress(req);
        String vnp_TmnCode = Config.vnp_TmnCode;

        // Tạo tham số VNPAY
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", Config.vnp_Version);
        vnp_Params.put("vnp_Command", Config.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // VNPAY yêu cầu tiền tệ tính bằng đơn vị nhỏ nhất
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB"); // Có thể thay đổi mã ngân hàng
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toán đơn hàng: " + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        // Ngôn ngữ
        String locale = req.getParameter("language");
        vnp_Params.put("vnp_Locale", (locale != null && !locale.isEmpty()) ? locale : "vn");

        // Địa chỉ trả về
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        // Thời gian tạo và thời gian hết hạn
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15); // Thời gian hết hạn 15 phút
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Tạo query và hash data
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();

        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                // Tạo hash data
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Tạo query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        return Config.vnp_PayUrl + "?" + queryUrl;
    }

    public String handleVnpayReturn(HttpServletRequest request) {
        String transactionStatus = request.getParameter("vnp_TransactionStatus");
        String transactionNo = request.getParameter("vnp_TransactionNo");
        String amount = request.getParameter("vnp_Amount");
        String bankCode = request.getParameter("vnp_BankCode");
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");

        // Tạo lại hash từ các tham số và so sánh với hash trả về
        StringBuilder hashData = new StringBuilder();
        List<String> fieldNames = new ArrayList<>(request.getParameterMap().keySet());
        Collections.sort(fieldNames);

        for (String fieldName : fieldNames) {
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty() && !fieldName.equals("vnp_SecureHash")) {
                hashData.append(fieldName).append('=').append(fieldValue).append('&');
            }
        }

        // Tạo vnp_SecureHash từ thông tin vừa lấy
        String generatedSecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());

        // Kiểm tra SecureHash và trạng thái giao dịch
        if (generatedSecureHash.equals(vnp_SecureHash) && "00".equals(transactionStatus)) {
            HoaDonDTO hoaDonDTO = new HoaDonDTO();
            if (hoaDonDTO.getIdNguoiDung() == null) {
                throw new RuntimeException("ID Người dùng không được null");
            }

            long currentCount = hoaDonRepository.count();
            String generatedMaHoaDon = "HD00" + (currentCount + 1);

            HoaDon hoaDon = new HoaDon();
            hoaDon.setMaHoaDon(generatedMaHoaDon);
            // Giao dịch thành công


            hoaDon.setMaHoaDon(generatedMaHoaDon);  // Tạo mã hóa đơn
            hoaDon.setThanhTien(new BigDecimal(amount).divide(new BigDecimal(100)));  // Chuyển đổi tiền từ VNPay (đơn vị: đồng)
            hoaDon.setNgayTao(new Date());
            hoaDon.setTrangThai(true);
            hoaDonRepository.save(hoaDon);

            // Lưu phương thức thanh toán VNPay cho hóa đơn
            PhuongThucThanhToanHoaDon phuongThucThanhToanHoaDon = new PhuongThucThanhToanHoaDon();
            phuongThucThanhToanHoaDon.setPhuongThucThanhToan(phuongThucThanhToanRepository.findById(4).get()); // VNPAY
            phuongThucThanhToanHoaDon.setNgayGiaoDich(new Date());
            phuongThucThanhToanHoaDon.setMoTa("Thanh toán qua VNPay cho đơn hàng " + generatedMaHoaDon);
            phuongThucThanhToanHoaDon.setHoaDon(hoaDon);
            phuongThucThanhToanHoaDonRepository.save(phuongThucThanhToanHoaDon);

            hoaDon.setPhuongThucThanhToanHoaDon(phuongThucThanhToanHoaDon);
            hoaDonRepository.save(hoaDon);

            // Kiểm tra và lấy thông tin người dùng
            NguoiDung nguoiDung = nguoiDungRepository.findById(hoaDonDTO.getIdNguoiDung()).orElseThrow(() -> new RuntimeException("Không tìm thấy Người dùng với ID: " + hoaDonDTO.getIdNguoiDung()));
            hoaDon.setNguoiDung(nguoiDung);

            // Tạo và lưu lịch sử thanh toán
            LichSuThanhToan lichSuThanhToan = new LichSuThanhToan();
            lichSuThanhToan.setSoTienThanhToan(new BigDecimal(amount).divide(new BigDecimal(100))); // Chuyển đổi sang đơn vị tiền tệ của bạn
            lichSuThanhToan.setNgayTao(new Date());
            lichSuThanhToan.setTrangThaiThanhToan(true);  // Giao dịch thành công
            lichSuThanhToan.setHoaDon(hoaDon);
            lichSuThanhToan.setNguoiDung(nguoiDung);
            lichSuThanhToanRepository.save(lichSuThanhToan);

            // Cập nhật trạng thái hóa đơn
            TrangThaiHoaDon trangThaiHoaDonTaoDon = new TrangThaiHoaDon();
            TrangThaiHoaDon trangThaiHoaDonChoXuLy = new TrangThaiHoaDon();

            LoaiTrangThai trangThaiTaoDon = loaiTrangThaiRepository.findById(1).orElseThrow(() -> new IllegalArgumentException("Loại trạng thái không tồn tại!"));
            trangThaiHoaDonTaoDon.setMoTa("Tạo đơn hàng");
            trangThaiHoaDonTaoDon.setNgayTao(new Date());
            trangThaiHoaDonTaoDon.setNgayCapNhat(new Date());
            trangThaiHoaDonTaoDon.setLoaiTrangThai(trangThaiTaoDon);
            trangThaiHoaDonTaoDon.setHoaDon(hoaDon);
            trangThaiHoaDonRepository.save(trangThaiHoaDonTaoDon);

            LoaiTrangThai trangThaiChoXuLy = loaiTrangThaiRepository.findById(2).orElseThrow(() -> new IllegalArgumentException("Loại trạng thái không tồn tại!"));
            trangThaiHoaDonChoXuLy.setMoTa("Chờ xác nhận");
            trangThaiHoaDonChoXuLy.setNgayTao(new Date());
            trangThaiHoaDonChoXuLy.setNgayCapNhat(new Date());
            trangThaiHoaDonChoXuLy.setLoaiTrangThai(trangThaiChoXuLy);
            trangThaiHoaDonChoXuLy.setHoaDon(hoaDon);
            trangThaiHoaDonRepository.save(trangThaiHoaDonChoXuLy);
            hoaDonRepository.save(hoaDon);
            // Xử lý các sản phẩm trong giỏ hàng
            processCartItems(hoaDonDTO, hoaDon);

            return "http://127.0.0.1:5502/#!/thanhcong";
        } else {
            // Giao dịch thất bại
            return "http://127.0.0.1:5502/index.html#!/thanh_toan_that_bai";
        }
    }

    private void processCartItems(HoaDonDTO hoaDonDTO, HoaDon hoaDon) {
        hoaDonDTO.getListSanPhamChiTiet().forEach(sanPham -> {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(sanPham.getIdspct()).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết"));

            if (sanPhamChiTiet.getSoLuong() <= 0 || sanPham.getSoLuong() > sanPhamChiTiet.getSoLuong()) {
                throw new RuntimeException("Số lượng tồn kho không đủ");
            }

            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - sanPham.getSoLuong());
            sanPhamChiTietRepository.save(sanPhamChiTiet);

            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setNgayGiaoDich(new Date());
            lichSuHoaDon.setSoTienThanhToan(sanPham.getGiaTien().multiply(BigDecimal.valueOf(sanPham.getSoLuong())));
            lichSuHoaDon.setNguoiDung(hoaDon.getNguoiDung());
            lichSuHoaDonRepository.save(lichSuHoaDon);

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


}

