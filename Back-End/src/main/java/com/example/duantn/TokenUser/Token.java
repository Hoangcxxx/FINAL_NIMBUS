package com.example.duantn.TokenUser;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Token {
    private String accessToken;
    private String refreshToken;
}
