package com.example.duantn.Response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class LoginResponse {
    private Integer idNguoiDung;
    private String tenNguoiDung;
    private String accessToken;

}
