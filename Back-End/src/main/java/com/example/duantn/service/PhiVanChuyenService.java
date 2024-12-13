package com.example.duantn.service;

import com.example.duantn.entity.PhiVanChuyen;
import com.example.duantn.repository.PhiVanChuyenRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PhiVanChuyenService {

    private final PhiVanChuyenRepository phiVanChuyenRepository;

    // Giả lập phí vận chuyển
    private final Map<Integer, Integer> shippingFees = new HashMap<>();

    public PhiVanChuyenService(PhiVanChuyenRepository phiVanChuyenRepository) {
        this.phiVanChuyenRepository = phiVanChuyenRepository;

        // Khởi tạo phí vận chuyển giả lập
        shippingFees.put(generateKey(1, 1, 1), 10000);
        shippingFees.put(generateKey(2, 2, 1), 15000);
        shippingFees.put(generateKey(4, 1, 1), 20000);
    }

    public Integer calculateShippingFee(Integer cityCode, Integer districtCode, Integer wardCode) {
        Integer key = generateKey(cityCode, districtCode, wardCode);
        return shippingFees.getOrDefault(key, 30000); // 30000 là giá mặc định nếu không có phí
    }

    public PhiVanChuyen saveShippingFee(Integer cityCode, Integer districtCode, Integer wardCode) {
        Integer key = generateKey(cityCode, districtCode, wardCode);
        Integer fee = calculateShippingFee(cityCode, districtCode, wardCode);

        // Tạo đối tượng và lưu
        PhiVanChuyen phiVanChuyen = new PhiVanChuyen();
        phiVanChuyen.setSoTienVanChuyen(BigDecimal.valueOf(fee));
        phiVanChuyen.setTrangThai(true);
        phiVanChuyen.setMoTa("Phí vận chuyển cho mã khóa " + key);
        phiVanChuyen.setNgayTao(new Date());
        phiVanChuyen.setNgayCapNhat(new Date());

        return phiVanChuyenRepository.save(phiVanChuyen);
    }

    private Integer generateKey(Integer cityCode, Integer districtCode, Integer wardCode) {
        return cityCode * 1000000 + districtCode * 1000 + wardCode;
    }
}
