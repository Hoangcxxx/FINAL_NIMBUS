package com.example.duantn.service;

import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class AddressService {

    private final RestTemplate restTemplate;
    private final DiaChiVanChuyenRepository diaChiVanChuyenRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final TinhRepository tinhRepository;
    private final HuyenRepository huyenRepository;
    private final XaRepository xaRepository;

    private final String apiUrl = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data"; // Base URL for GHN API
    private final String token = "337cc41f-844d-11ef-8e53-0a00184fe694"; // Token from your config

    // Constructor injection for dependencies
    public AddressService(RestTemplate restTemplate,
                          DiaChiVanChuyenRepository diaChiVanChuyenRepository,
                          NguoiDungRepository nguoiDungRepository,
                          TinhRepository tinhRepository,
                          HuyenRepository huyenRepository,
                          XaRepository xaRepository) {
        this.restTemplate = restTemplate;
        this.diaChiVanChuyenRepository = diaChiVanChuyenRepository;
        this.nguoiDungRepository = nguoiDungRepository;
        this.tinhRepository = tinhRepository;
        this.huyenRepository = huyenRepository;
        this.xaRepository = xaRepository;
    }

    // Method to get list of provinces from GHN
    public ResponseEntity<String> getProvinces() {
        String url = apiUrl + "/province";
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    // Method to get list of districts by province ID from GHN
    public ResponseEntity<String> getDistricts(int provinceId) {
        String url = apiUrl + "/district?province_id=" + provinceId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    // Method to get list of wards by district ID from GHN
    public ResponseEntity<String> getWards(int districtId) {
        String url = apiUrl + "/ward?district_id=" + districtId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    // Save city, district, ward to the DB for a user
    public void saveCityDistrictWardToDB(Integer userId, String cityCode, String districtCode, String wardCode) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId must not be null");
        }

        // Get user information
        NguoiDung nguoiDung = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch or save City, District, Ward
        Tinh selectedCity = tinhRepository.findByMaTinh(cityCode)
                .orElseGet(() -> tinhRepository.save(fetchCityInfo(cityCode)));
        Huyen selectedDistrict = huyenRepository.findByMaHuyen(districtCode)
                .orElseGet(() -> huyenRepository.save(fetchDistrictInfo(districtCode)));
        Xa selectedWard = xaRepository.findByMaXa(wardCode)
                .orElseGet(() -> xaRepository.save(fetchWardInfo(wardCode)));

        // Save shipping address
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

    // Fetch city information from API
    private Tinh fetchCityInfo(String cityCode) {
        String url = apiUrl + cityCode;
        Map<String, Object> cityData = restTemplate.getForObject(url, Map.class);
        if (cityData != null && cityData.containsKey("code") && cityData.containsKey("name")) {
            Tinh tinh = new Tinh();
            tinh.setIdTinh(Integer.parseInt(cityData.get("code").toString()));
            tinh.setTenTinh(cityData.get("name").toString());
            return tinh;
        }
        throw new RuntimeException("Cannot fetch city data from API");
    }

    // Fetch district information from API
    private Huyen fetchDistrictInfo(String districtCode) {
        String url = apiUrl + districtCode;
        Map<String, Object> districtData = restTemplate.getForObject(url, Map.class);
        if (districtData != null && districtData.containsKey("code") && districtData.containsKey("name")) {
            Huyen huyen = new Huyen();
            huyen.setIdHuyen(Integer.parseInt(districtData.get("code").toString()));
            huyen.setTenHuyen(districtData.get("name").toString());
            return huyen;
        }
        throw new RuntimeException("Cannot fetch district data from API");
    }

    // Fetch ward information from API
    private Xa fetchWardInfo(String wardCode) {
        String url = apiUrl + wardCode;
        Map<String, Object> wardData = restTemplate.getForObject(url, Map.class);
        if (wardData != null && wardData.containsKey("code") && wardData.containsKey("name")) {
            Xa xa = new Xa();
            xa.setIdXa(Integer.parseInt(wardData.get("code").toString()));
            xa.setTenXa(wardData.get("name").toString());
            return xa;
        }
        throw new RuntimeException("Cannot fetch ward data from API");
    }
}
