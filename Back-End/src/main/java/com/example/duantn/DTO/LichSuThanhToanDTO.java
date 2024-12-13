package com.example.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LichSuThanhToanDTO {
    private Integer id;
    private Boolean trangThaiThanhToan;
    // Các trường khác nếu cần
}
