package com.example.duantn.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoucherDTO {
    private Integer idVoucher;
    private Integer trangThaiGiamGiaId;
    private Integer soLuong;
    private BigDecimal giaTriGiamGia;
}