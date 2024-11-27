package com.example.duantn.service;

import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        String url = "https://provinces.open-api.vn/api/?depth=1";
        return restTemplate.getForObject(url, List.class);
    }

    // 2. Hiển thị danh sách Huyện dựa trên Tỉnh
    public List<Map<String, Object>> getDistricts(String cityCode) {
        String url = "https://provinces.open-api.vn/api/p/" + cityCode + "?depth=2";
        Map<String, Object> cityDetails = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) cityDetails.get("districts");
    }

    // 3. Hiển thị danh sách Xã dựa trên Huyện
    public List<Map<String, Object>> getWards(String districtCode) {
        String url = "https://provinces.open-api.vn/api/d/" + districtCode + "?depth=2";
        Map<String, Object> districtDetails = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) districtDetails.get("wards");
    }

    // 4. Lưu dữ liệu vào DB
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
}
