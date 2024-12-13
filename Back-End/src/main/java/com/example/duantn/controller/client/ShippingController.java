package com.example.duantn.controller.client;

import com.example.duantn.entity.DiaChiVanChuyen;
import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.PhiVanChuyen;
import com.example.duantn.repository.DiaChiVanChuyenRepository;
import com.example.duantn.repository.HoaDonRepository;
import com.example.duantn.repository.PhiVanChuyenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/nguoi_dung/test")
public class ShippingController {

    private final PhiVanChuyenRepository phiVanChuyenRepository;
    private final HoaDonRepository hoaDonRepository;
    private final DiaChiVanChuyenRepository diaChiVanChuyenRepository;

    public ShippingController(PhiVanChuyenRepository phiVanChuyenRepository, HoaDonRepository hoaDonRepository, DiaChiVanChuyenRepository diaChiVanChuyenRepository) {
        this.phiVanChuyenRepository = phiVanChuyenRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.diaChiVanChuyenRepository = diaChiVanChuyenRepository;
    }

    @GetMapping("/shipping-fee/{cityCode}/{districtCode}/{wardCode}")
    public ResponseEntity<Integer> getShippingFee(@PathVariable Integer cityCode,
                                                  @PathVariable Integer districtCode,
                                                  @PathVariable Integer wardCode) {
        Integer fee = 22000;  // Mặc định là 22k cho các tỉnh

        // Kiểm tra nếu là Hà Nội (cityCode = 1) thì phí là 10k, các tỉnh khác sẽ tính phí 22k
        if (cityCode == 1) {  // Hà Nội
            fee = 10000;
        } else if (districtCode != null) {
            // Thêm logic tính phí vận chuyển cho từng quận huyện nếu cần
            fee = 22000;  // Ví dụ vẫn là 22k nếu không có logic cụ thể cho quận huyện
        }

        // Lưu phí vận chuyển vào cơ sở dữ liệu
        saveShippingFee(cityCode, fee);

        return ResponseEntity.ok(fee);  // Trả về phí vận chuyển
    }


    private void saveShippingFee(Integer cityCode, Integer fee) {
        // Kiểm tra nếu có hóa đơn tồn tại, nếu không tạo mới
        HoaDon hoaDon = hoaDonRepository.findById(1).orElse(new HoaDon());  // Giả sử chỉ lấy hoa đơn với ID là 1
        hoaDon.setPhiShip(BigDecimal.valueOf(fee));
        hoaDon = hoaDonRepository.save(hoaDon);  // Lưu hóa đơn

        // Lưu địa chỉ vận chuyển
        DiaChiVanChuyen diaChiVanChuyen = diaChiVanChuyenRepository.findById(1).orElse(new DiaChiVanChuyen());
        diaChiVanChuyen = diaChiVanChuyenRepository.save(diaChiVanChuyen);

        // Lưu phí vận chuyển
        PhiVanChuyen phiVanChuyen = new PhiVanChuyen();
        phiVanChuyen.setHoaDon(hoaDon);  // Gán hóa đơn
        phiVanChuyen.setDiaChiVanChuyen(diaChiVanChuyen);  // Gán địa chỉ vận chuyển
        phiVanChuyen.setSoTienVanChuyen(BigDecimal.valueOf(fee));  // Chuyển đổi phí vận chuyển thành BigDecimal
        phiVanChuyen.setTrangThai(true);  // Giả sử trạng thái là true
        phiVanChuyen.setMoTa("Phí vận chuyển cho mã tỉnh " + cityCode);  // Mô tả

        phiVanChuyenRepository.save(phiVanChuyen);  // Lưu phí vận chuyển
    }

}
