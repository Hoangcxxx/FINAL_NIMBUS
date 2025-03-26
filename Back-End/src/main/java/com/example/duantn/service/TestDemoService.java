package com.example.duantn.service;

import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class TestDemoService {

    private final RestTemplate restTemplate;
    private final DiaChiVanChuyenRepository diaChiVanChuyenRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final TinhRepository tinhRepository;
    private final HuyenRepository huyenRepository;
    private final XaRepository xaRepository;

    public TestDemoService(RestTemplate restTemplate, DiaChiVanChuyenRepository diaChiVanChuyenRepository,
                           NguoiDungRepository nguoiDungRepository, TinhRepository tinhRepository,
                           HuyenRepository huyenRepository, XaRepository xaRepository) {
        this.restTemplate = restTemplate;
        this.diaChiVanChuyenRepository = diaChiVanChuyenRepository;
        this.nguoiDungRepository = nguoiDungRepository;
        this.tinhRepository = tinhRepository;
        this.huyenRepository = huyenRepository;
        this.xaRepository = xaRepository;
    }

    // 1. Hiển thị danh sách Tỉnh
    public List<Map<String, Object>> getCities() {
        String url = "https://esgoo.net/api-tinhthanh/1/0.htm";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) response.get("data");
    }

    // 2. Hiển thị danh sách Huyện dựa trên Tỉnh
    public List<Map<String, Object>> getDistricts(String cityId) {
        String url = "https://esgoo.net/api-tinhthanh/2/" + cityId + ".htm";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) response.get("data");
    }

    // 3. Hiển thị danh sách Xã dựa trên Huyện
    public List<Map<String, Object>> getWards(String districtId) {
        String url = "https://esgoo.net/api-tinhthanh/3/" + districtId + ".htm";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) response.get("data");
    }
    // Tính phí vận chuyển
    public Map<String, Object> calculateShippingFee(Integer service_id, Integer districtId, Integer wardId, Integer weight) {
        // Kiểm tra các tham số đầu vào
        if (service_id == null || districtId == null || wardId == null || weight == null || weight <= 0) {
            throw new IllegalArgumentException("Các tham số không hợp lệ. Đảm bảo service_id, districtId, wardId và weight đều hợp lệ.");
        }

        // Định nghĩa URL API
        String url = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";

        // Tạo đối tượng request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("service_id", service_id);  // Mã dịch vụ
        requestBody.put("from_district", 1001);  // Mã quận, huyện nơi gửi (có thể tùy chỉnh theo yêu cầu)
        requestBody.put("to_district", districtId);  // Mã quận, huyện nơi nhận
        requestBody.put("to_ward", wardId);  // Mã xã, phường nơi nhận
        requestBody.put("weight", weight);  // Trọng lượng sản phẩm (gram)
        requestBody.put("length", 10);  // Chiều dài (cm)
        requestBody.put("width", 10);  // Chiều rộng (cm)
        requestBody.put("height", 10);  // Chiều cao (cm)

        // Cấu hình headers cho yêu cầu API
        HttpHeaders headers = prepareHeaders();
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Gửi yêu cầu POST tới API và nhận phản hồi
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            // Kiểm tra nếu phản hồi không rỗng và trả về kết quả
            if (response != null && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Không nhận được phản hồi từ API");
            }
        } catch (Exception e) {
            // Xử lý lỗi khi gửi yêu cầu hoặc phản hồi
            throw new RuntimeException("Lỗi khi tính phí vận chuyển: " + e.getMessage(), e);
        }
    }

    // Cấu hình headers cho các yêu cầu API
    private HttpHeaders prepareHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", "347f8e0e-981c-11ef-a905-420459bb4727");
        headers.set("shop_id", "5427817");  // Mã shop của bạn
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    // 4. Lưu dữ liệu vào DB
    public void saveCityDistrictWardToDB(Integer userId, String cityId, String districtId, String wardId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId must not be null");
        }

        // Lấy thông tin người dùng
        NguoiDung nguoiDung = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Lấy hoặc lưu Tỉnh theo mã
        Tinh selectedCity = tinhRepository.findByMaTinh(cityId)
                .orElseGet(() -> tinhRepository.save(fetchCityInfo(cityId)));

        // Lấy hoặc lưu Huyện theo mã
        Huyen selectedDistrict = huyenRepository.findByMaHuyen(districtId)
                .orElseGet(() -> huyenRepository.save(fetchDistrictInfo(districtId)));

        // Lấy hoặc lưu Xã theo mã
        Xa selectedWard = xaRepository.findByMaXa(wardId)
                .orElseGet(() -> xaRepository.save(fetchWardInfo(wardId)));


        // Lưu địa chỉ vận chuyển
        DiaChiVanChuyen diaChi = new DiaChiVanChuyen();
        diaChi.setNguoiDung(nguoiDung);
        diaChi.setTrangThai(true);
        diaChi.setMoTa(String.format("Địa chỉ: %s, %s, %s",
                selectedWard.getTenXa(),
                selectedDistrict.getTenHuyen(),
                selectedCity.getTenTinh()));
        diaChi.setTinh(selectedCity);
        diaChi.setHuyen(selectedDistrict);
        diaChi.setXa(selectedWard);

        diaChiVanChuyenRepository.save(diaChi);
    }

    // Lấy thông tin Tỉnh từ API
    private Tinh fetchCityInfo(String cityId) {
        String url = "https://esgoo.net/api-tinhthanh/1/0.htm";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("data")) {
            List<Map<String, Object>> cities = (List<Map<String, Object>>) response.get("data");
            for (Map<String, Object> city : cities) {
                if (city.get("id").toString().equals(cityId)) {
                    Tinh tinh = new Tinh();
                    tinh.setIdTinh(Integer.parseInt(city.get("id").toString()));
                    tinh.setTenTinh(city.get("full_name").toString());
                    return tinh;
                }
            }
        }
        throw new RuntimeException("Không thể lấy dữ liệu tỉnh từ API");
    }

    // Lấy thông tin Huyện từ API
    private Huyen fetchDistrictInfo(String districtId) {
        String url = "https://esgoo.net/api-tinhthanh/2/" + districtId + ".htm";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("data")) {
            List<Map<String, Object>> districts = (List<Map<String, Object>>) response.get("data");
            for (Map<String, Object> district : districts) {
                if (district.get("id").toString().equals(districtId)) {
                    Huyen huyen = new Huyen();
                    huyen.setIdHuyen(Integer.parseInt(district.get("id").toString()));
                    huyen.setTenHuyen(district.get("full_name").toString());
                    return huyen;
                }
            }
        }
        throw new RuntimeException("Không thể lấy dữ liệu huyện từ API");
    }

    // Lấy thông tin Xã từ API
    private Xa fetchWardInfo(String wardId) {
        String url = "https://esgoo.net/api-tinhthanh/3/" + wardId + ".htm";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("data")) {
            List<Map<String, Object>> wards = (List<Map<String, Object>>) response.get("data");
            for (Map<String, Object> ward : wards) {
                if (ward.get("id").toString().equals(wardId)) {
                    Xa xa = new Xa();
                    xa.setIdXa(Integer.parseInt(ward.get("id").toString()));
                    xa.setTenXa(ward.get("full_name").toString());
                    return xa;
                }
            }
        }
        throw new RuntimeException("Không thể lấy dữ liệu xã từ API");
    }


}
