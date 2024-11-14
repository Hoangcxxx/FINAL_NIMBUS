package com.example.duantn.service;

import com.example.duantn.DTO.HoaDonDTO;
import com.example.duantn.entity.HoaDon;
import com.example.duantn.repository.HoaDonRepository;
import com.example.duantn.repository.TrangThaiHoaDonRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class VNPayService {

    @Value("${vnpay.merchantCode}")
    private String merchantCode;

    @Value("${vnpay.secretKey}")
    private String secretKey;

    @Value("${vnpay.apiUrl}")
    private String apiUrl;

    @Value("${vnpay.returnUrl}")
    private String returnUrl;

    private final HoaDonRepository hoaDonRepository;
    private final TrangThaiHoaDonRepository trangThaiHoaDonRepository;

    public VNPayService(HoaDonRepository hoaDonRepository, TrangThaiHoaDonRepository trangThaiHoaDonRepository) {
        this.hoaDonRepository = hoaDonRepository;
        this.trangThaiHoaDonRepository = trangThaiHoaDonRepository;
    }

    public String createPaymentUrl(HoaDonDTO hoaDonDTO) throws NoSuchAlgorithmException {
        MultiValueMap<String, String> vnpParams = preparePaymentParams(hoaDonDTO);
        vnpParams.add("vnp_SecureHash", generateSecureHash(vnpParams));
        return UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParams(vnpParams)
                .toUriString();
    }
    private MultiValueMap<String, String> preparePaymentParams(HoaDonDTO hoaDonDTO) {
        MultiValueMap<String, String> vnpParams = new LinkedMultiValueMap<>();
        String vnp_IpAddr = "27.73.209.243";
        vnpParams.add("vnp_Version", "2.0.0");
        vnpParams.add("vnp_Command", "pay");
        vnpParams.add("vnp_TmnCode", merchantCode);
        vnpParams.add("vnp_Amount", String.valueOf(hoaDonDTO.getThanhTien().multiply(BigDecimal.valueOf(100)).longValue()));
        vnpParams.add("vnp_CurrCode", "VND");
        vnpParams.add("vnp_BankCode", "NCB");
        vnpParams.add("vnp_OrderInfo", "Thanh toán đơn hàng " + hoaDonDTO.getMaHoaDon());
        vnpParams.add("vnp_OrderType", "other");
        vnpParams.add("vnp_Locale", "vn");
        vnpParams.add("vnp_ReturnUrl", returnUrl);
        vnpParams.add("vnp_TxnRef", hoaDonDTO.getMaHoaDon());
        vnpParams.add("vnp_CreateDate", String.valueOf(new Date().getTime()));
        vnpParams.add("vnp_IpAddr", vnp_IpAddr);
        return vnpParams;
    }

    private String generateSecureHash(MultiValueMap<String, String> vnpParams) throws NoSuchAlgorithmException {
        StringBuilder hashData = new StringBuilder();

        vnpParams.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> entry.getValue().forEach(value ->
                        hashData.append(entry.getKey()).append("=").append(value).append("&")));
        String data = hashData.substring(0, hashData.length() - 1);
        return encryptMD5(data + secretKey);
    }

    private String encryptMD5(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(data.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            String hex = Integer.toHexString(0xFF & b);
            hexString.append(hex.length() == 1 ? '0' : "").append(hex);
        }
        return hexString.toString().toUpperCase();
    }

    public boolean verifyPaymentResponse(Map<String, String> responseParams) throws NoSuchAlgorithmException {
        String vnp_SecureHash = responseParams.get("vnp_SecureHash");
        responseParams.remove("vnp_SecureHash");

        // Tạo MultiValueMap từ các tham số trả về
        MultiValueMap<String, String> vnpParams = new LinkedMultiValueMap<>();
        responseParams.forEach(vnpParams::add);

        // Sinh mã bảo mật từ các tham số phản hồi và kiểm tra
        String secureHash = generateSecureHash(vnpParams);

        if (secureHash.equals(vnp_SecureHash)) {
            // Kiểm tra trạng thái giao dịch và cập nhật trạng thái hóa đơn
            String transactionStatus = responseParams.get("vnp_TransactionStatus");
            String orderCode = responseParams.get("vnp_TxnRef");

            Optional<HoaDon> hoaDonOpt = hoaDonRepository.findByMaHoaDon(orderCode);
            if (hoaDonOpt.isPresent()) {
                HoaDon hoaDon = hoaDonOpt.get();
                int status = "00".equals(transactionStatus) ? 2 : 3;  // Thành công (00) hoặc thất bại (khác 00)
                hoaDon.setTrangThaiHoaDon(trangThaiHoaDonRepository.findById(status).orElse(null));
                hoaDonRepository.save(hoaDon);
                return true;
            }
        }
        return false;
    }

}
