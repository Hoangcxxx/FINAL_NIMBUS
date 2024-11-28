package com.example.duantn.service;

import com.example.duantn.city.District;
import com.example.duantn.city.Province;
import com.example.duantn.city.Ward;
import com.example.duantn.dto.Response;
import com.example.duantn.reponse.ResponseDTO;
import com.example.duantn.request.RequestDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GHNService {

    private final String token = "f7a6a8d9-a552-11ef-a89d-dab02cbaab48";
    private final String API_BASE_URL = "https://online-gateway.ghn.vn/shiip/public-api";

    // Lấy danh sách tỉnh/thành phố
    public List<Province> getProvinces() {
        return getDataFromApi("/master-data/province", new ParameterizedTypeReference<>() {});
    }

    // Lấy danh sách quận/huyện theo tỉnh
    public List<District> getDistricts(int provinceId) {
        String url = "/master-data/district?province_id=" + provinceId;
        return getDataFromApi(url, new ParameterizedTypeReference<>() {});
    }

    // Lấy danh sách phường/xã theo quận/huyện
    public List<Ward> getWards(int districtId) {
        String url = "/master-data/ward?district_id=" + districtId;
        return getDataFromApi(url, new ParameterizedTypeReference<>() {});
    }
    // Hàm tính khoảng cách giữa hai địa điểm dựa trên vĩ độ và kinh độ
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371; // Bán kính Trái Đất tính bằng km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Khoảng cách theo km
    }


    // Tính phí giao hàng theo khoảng cách và trọng lượng
    public ResponseDTO getShippingFee(RequestDTO requestDTO) {
        RestTemplate restTemplate = new RestTemplate();
        String url = API_BASE_URL + "/v2/shipping-order/fee";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token);

        HttpEntity<RequestDTO> entity = new HttpEntity<>(requestDTO, headers);
        try {
            // Gửi yêu cầu POST để tính phí giao hàng
            ResponseEntity<ResponseDTO> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, ResponseDTO.class);

            // Trả về dữ liệu tính phí nếu thành công
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.err.println("Lỗi khi gọi API GHN: " + e.getMessage());
            e.printStackTrace();
            return null; // Hoặc bạn có thể xử lý lỗi cụ thể hơn
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Hàm chung để lấy dữ liệu từ GHN API
    private <T> List<T> getDataFromApi(String endpoint, ParameterizedTypeReference<Response<List<T>>> typeReference) {
        RestTemplate restTemplate = new RestTemplate();
        String url = API_BASE_URL + endpoint;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Response<List<T>>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, typeReference);
            return response.getBody().getData();
        } catch (HttpClientErrorException e) {
            System.err.println("Lỗi khi lấy dữ liệu từ API GHN: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
