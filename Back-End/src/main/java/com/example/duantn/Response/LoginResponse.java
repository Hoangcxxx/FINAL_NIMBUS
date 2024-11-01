package com.example.duantn.Response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class LoginResponse {
    private Integer idNguoiDung;  // ID người dùng
    private String tenNguoiDung;   // Thay đổi tên thuộc tính ở đây
    private String accessToken;     // Token truy cập

}
