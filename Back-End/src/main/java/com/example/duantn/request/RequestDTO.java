package com.example.duantn.request;

import lombok.Data;

@Data
public class RequestDTO {
    private int service_type_id = 2;       // Loại dịch vụ (2: Giao hàng nhanh)
    private int from_district_id;         // ID Quận/huyện gửi
    private String from_ward_code;        // Mã phường/xã gửi
    private int to_district_id;           // ID Quận/huyện nhận
    private String to_ward_code;          // Mã phường/xã nhận
    private int weight;                   // Trọng lượng (gram)
}
