package com.example.duantn.reponse;

import lombok.Data;

@Data
public class ResponseDTO {
    private int total;
    private Object data; // Dùng Object để chứa dữ liệu linh hoạt từ API
}
