package com.example.duantn.service;

import com.example.duantn.entity.DiaChiVanChuyen;
import com.example.duantn.repository.DiaChiVanChuyenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TestApi63tutour {

    private static final Logger logger = LoggerFactory.getLogger(TestApi63tutour.class);

    @Autowired
    private DiaChiVanChuyenRepository diaChiVanChuyenRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String PROVINCE_API = "https://provinces.open-api.vn/api/";

    // Đồng bộ dữ liệu từ API vào CSDL
    public void syncDataFromApi() {
        try {
            // Lấy danh sách tỉnh/thành
            List<Map<String, Object>> provinces = fetchDataFromApi(PROVINCE_API + "?depth=1");
            if (provinces == null || provinces.isEmpty()) {
                throw new RuntimeException("Không có dữ liệu tỉnh/thành từ API.");
            }

            for (Map<String, Object> province : provinces) {
                String tinh = (String) province.get("name");
                Integer provinceCode = (Integer) province.get("code");

                // Lấy danh sách quận/huyện
                List<Map<String, Object>> districts = fetchDataFromApi(PROVINCE_API + "p/" + provinceCode + "?depth=2");
                if (districts == null || districts.isEmpty()) {
                    logger.error("Không có dữ liệu quận/huyện từ API cho tỉnh: " + tinh);
                    continue; // Skip this province and move to the next
                }

                for (Map<String, Object> district : districts) {
                    String huyen = (String) district.get("name");
                    Integer districtCode = (Integer) district.get("code");

                    // Lấy danh sách xã/phường
                    List<Map<String, Object>> wards = fetchDataFromApi(PROVINCE_API + "d/" + districtCode + "?depth=2");
                    if (wards == null || wards.isEmpty()) {
                        logger.error("Không có dữ liệu xã/phường từ API cho quận: " + huyen);
                        continue; // Skip this district and move to the next
                    }

                    for (Map<String, Object> ward : wards) {
                        String xa = (String) ward.get("name");

                        // Tạo đối tượng và lưu vào CSDL
                        DiaChiVanChuyen diaChi = new DiaChiVanChuyen();
                        diaChi.setTinh(tinh);
                        diaChi.setHuyen(huyen);
                        diaChi.setXa(xa);
                        diaChi.setNgayTao(new Date());
                        diaChi.setTrangThai(true);
                        diaChi.setMoTa(null);

                        diaChiVanChuyenRepository.save(diaChi);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Lỗi khi đồng bộ dữ liệu: " + e.getMessage(), e);
            throw e; // Re-throw to ensure the error is propagated
        }
    }

    // Hàm gọi API bên ngoài và parse dữ liệu JSON
    private List<Map<String, Object>> fetchDataFromApi(String url) {
        try {
            // Gọi API và nhận phản hồi
            ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);

            // Kiểm tra nếu dữ liệu trả về là rỗng hoặc null
            if (response.getBody() == null || response.getBody().isEmpty()) {
                logger.error("Dữ liệu trả về từ API là rỗng hoặc không hợp lệ từ URL: " + url);
                return null;
            }

            return response.getBody();
        } catch (Exception e) {
            logger.error("Lỗi khi gọi API: " + url, e);
            throw new RuntimeException("Lỗi khi gọi API: " + url, e);
        }
    }
}
