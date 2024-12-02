package com.example.duantn.service;

import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TestDemoService {

    private final RestTemplate restTemplate;
    private final DiaChiVanChuyenRepository diaChiVanChuyenRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final TinhRepository tinhRepository;
    private final HuyenRepository huyenRepository;
    private final XaRepository xaRepository;

    private static final String GHN_API_BASE_URL = "https://online-gateway.ghn.vn/shiip/public-api/v2";
    private static final String TOKEN = "347f8e0e-981c-11ef-a905-420459bb4727";
    private static final String SHOP_ID = "5427817";

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

    // Tạo headers
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", TOKEN);
        headers.set("ShopId", SHOP_ID);
        return headers;
    }

    public Map<String, Object> calculateShippingFee(
            int toDistrictId,
            String toWardCode
    ) {
        String url = GHN_API_BASE_URL + "/shipping-order/fee";

        // Giá trị cố định
        int serviceId = 53321;
        int fromDistrictId = 1452; // Set cứng giá trị fromDistrictId, ví dụ là 1452

        // Tạo request body
        Map<String, Object> requestBody = Map.of(
                "service_id", serviceId,
                "from_district_id", fromDistrictId,
                "to_district_id", toDistrictId,
                "to_ward_code", toWardCode,
                "height", 20,
                "length", 30,
                "weight", 1000,
                "width", 15
        );

        HttpHeaders headers = createHeaders();
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                log.error("Không thể tính phí ship, mã lỗi: {}", response.getStatusCode());
                throw new RuntimeException("Không thể tính phí ship: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Lỗi khi gọi API tính phí ship: {}", e.getMessage());
            throw new RuntimeException("Lỗi khi gọi API tính phí ship", e);
        }
    }

    // Lấy danh sách các dịch vụ vận chuyển có sẵn
    public Map<String, Object> getAvailableServices(int fromDistrictId, int toDistrictId) {
        String url = GHN_API_BASE_URL + "/shipping-order/available-services";

        Map<String, Object> requestBody = Map.of(
                "shop_id", Integer.parseInt(SHOP_ID),
                "from_district", fromDistrictId,
                "to_district", toDistrictId
        );

        HttpHeaders headers = createHeaders();
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); // Trả về danh sách dịch vụ
        } else {
            throw new RuntimeException("Không thể lấy danh sách dịch vụ: " + response.getStatusCode());
        }
    }

    // Lấy danh sách các tỉnh từ Open API
    public List<Map<String, Object>> getCities() {
        String url = "https://provinces.open-api.vn/api/?depth=1";
        return restTemplate.getForObject(url, List.class);
    }

    // Lấy danh sách các quận của tỉnh từ Open API
    public List<Map<String, Object>> getDistricts(String cityCode) {
        String url = "https://provinces.open-api.vn/api/p/" + cityCode + "?depth=2";
        Map<String, Object> cityDetails = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) cityDetails.get("districts");
    }

    // Lấy danh sách các xã từ Open API
    public List<Map<String, Object>> getWards(String districtCode) {
        String url = "https://provinces.open-api.vn/api/d/" + districtCode + "?depth=2";
        Map<String, Object> districtDetails = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) districtDetails.get("wards");
    }

    // Lưu dữ liệu vào DB
    public void saveCityDistrictWardToDB(Integer userId, String cityCode, String districtCode, String wardCode) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId must not be null");
        }

        // Lấy thông tin người dùng
        NguoiDung nguoiDung = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Lấy hoặc lưu Tỉnh
        Tinh selectedCity = tinhRepository.findById(Integer.parseInt(cityCode))
                .orElseGet(() -> tinhRepository.save(fetchCityInfo(cityCode)));

        // Lấy hoặc lưu Huyện
        Huyen selectedDistrict = huyenRepository.findById(Integer.parseInt(districtCode))
                .orElseGet(() -> huyenRepository.save(fetchDistrictInfo(districtCode)));

        // Lấy hoặc lưu Xã
        Xa selectedWard = xaRepository.findById(Integer.parseInt(wardCode))
                .orElseGet(() -> xaRepository.save(fetchWardInfo(wardCode)));

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

    // Lấy thông tin Tỉnh từ Open API
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

    // Lấy thông tin Huyện từ Open API
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

    // Lấy thông tin Xã từ Open API
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
}
