package com.example.duantn.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResponseDTO {
    private String code;
    private String message;
    private BigDecimal fee; // Phí vận chuyển
}
