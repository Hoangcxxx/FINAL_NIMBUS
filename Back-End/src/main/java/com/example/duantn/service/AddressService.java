package com.example.duantn.service;

import com.example.duantn.dto.ApiResponse;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class AddressService {

    @Autowired
    private TinhRepository tinhRepository;

    @Autowired
    private XaRepository xaRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private HuyenRepository huyenRepository;

    @Autowired
    private DiaChiVanChuyenRepository diaChiVanChuyenRepository;

    private final String apiUrl = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data";
    private final String token = "337cc41f-844d-11ef-8e53-0a00184fe694"; // Token của bạn
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();  // ObjectMapper để chuyển đổi JSON

    public AddressService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Map<String, Object>> getProvinces() {
        String url = apiUrl + "/province";
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token); // Đảm bảo token đã được set đúng
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Kiểm tra token trước khi gửi yêu cầu
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token không hợp lệ.");
        }

        // Gửi yêu cầu GET tới API và nhận kết quả trả về dạng ApiResponse
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // Kiểm tra mã phản hồi từ API
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                // Sử dụng ObjectMapper để chuyển đổi String thành ApiResponse
                ObjectMapper objectMapper = new ObjectMapper();
                ApiResponse apiResponse = objectMapper.readValue(response.getBody(), ApiResponse.class);

                // Trả về danh sách tỉnh (data) từ ApiResponse
                return apiResponse.getData();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error parsing response data.");
            }
        } else {
            throw new RuntimeException("Error fetching provinces: " + response.getStatusCode());
        }
    }

    public List<Map<String, Object>> getDistricts(int provinceId) {
        String url = apiUrl + "/district?province_id=" + provinceId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token); // Đảm bảo token đã được set đúng
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Kiểm tra token trước khi gửi yêu cầu
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token không hợp lệ.");
        }

        // Gửi yêu cầu GET tới API và nhận kết quả trả về dạng ApiResponse
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // Kiểm tra mã phản hồi từ API
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                // Sử dụng ObjectMapper để chuyển đổi String thành ApiResponse
                ObjectMapper objectMapper = new ObjectMapper();
                ApiResponse apiResponse = objectMapper.readValue(response.getBody(), ApiResponse.class);

                // Trả về danh sách tỉnh (data) từ ApiResponse
                return apiResponse.getData();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error parsing response data.");
            }
        } else {
            throw new RuntimeException("Error fetching provinces: " + response.getStatusCode());
        }
    }

    public List<Map<String, Object>> getWards ( int districtId){
        String url = apiUrl + "/ward?district_id=" + districtId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token); // Đảm bảo token đã được set đúng
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Kiểm tra token trước khi gửi yêu cầu
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token không hợp lệ.");
        }

        // Gửi yêu cầu GET tới API và nhận kết quả trả về dạng ApiResponse
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // Kiểm tra mã phản hồi từ API
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                // Sử dụng ObjectMapper để chuyển đổi String thành ApiResponse
                ObjectMapper objectMapper = new ObjectMapper();
                ApiResponse apiResponse = objectMapper.readValue(response.getBody(), ApiResponse.class);

                // Trả về danh sách tỉnh (data) từ ApiResponse
                return apiResponse.getData();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error parsing response data.");
            }
        } else {
            throw new RuntimeException("Error fetching provinces: " + response.getStatusCode());
        }
    }


    // 4. Lưu dữ liệu vào DB
    public void saveCityDistrictWardToDB(Integer userId, String cityCode, String districtCode, String wardCode) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId must not be null");
        }

        // Lấy thông tin người dùng
        NguoiDung nguoiDung = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Lấy hoặc lưu Tỉnh theo mã
        Tinh selectedCity = tinhRepository.findByMaTinh(cityCode)
                .orElseGet(() -> {
                    Tinh city = fetchCityInfoFromGHN(cityCode); // Gọi API GHN nếu không có trong DB
                    return tinhRepository.save(city);          // Lưu vào DB
                });

// Lấy hoặc lưu Huyện theo mã
        Huyen selectedDistrict = huyenRepository.findByMaHuyen(districtCode)
                .orElseGet(() -> {
                    Huyen district = fetchDistrictInfoFromGHN(districtCode); // Gọi API GHN nếu không có trong DB
                    district.setTinh(selectedCity);                         // Gán tỉnh liên quan
                    return huyenRepository.save(district);                  // Lưu vào DB
                });

// Lấy hoặc lưu Xã theo mã
        Xa selectedWard = xaRepository.findByMaXa(wardCode)
                .orElseGet(() -> {
                    // Gọi API GHN để lấy thông tin xã
                    Xa ward = fetchWardInfoFromGHN(districtCode, wardCode); // Truyền mã huyện và mã xã
                    ward.setHuyen(selectedDistrict);                       // Gán huyện liên quan
                    return xaRepository.save(ward);                        // Lưu vào DB
                });


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
        diaChi.setDiaChiCuThe(diaChi.getDiaChiCuThe());
        diaChi.setNgayTao(new Date());
        diaChi.setNgayCapNhat(new Date());

        diaChiVanChuyenRepository.save(diaChi);
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






}
