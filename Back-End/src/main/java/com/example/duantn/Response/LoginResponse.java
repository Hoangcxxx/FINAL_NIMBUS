package com.example.duantn.Response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class LoginResponse {
    private Integer idNguoiDung;  // ID người dùng
    private String tennguoidung;
    private String accessToken;     // Token truy cập
    private String refreshToken;    // Token làm mới


}
