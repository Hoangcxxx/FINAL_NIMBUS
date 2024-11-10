package com.example.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRequest {
    private String hoTen;
    private String soDienThoai;    // Số điện thoại
    private String email;          // Email
    private String diaChi;         // Địa chỉ
    private String tinh;           // Tỉnh
    private String quan;           // Quận
    private String phuong;
}
