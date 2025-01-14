package com.example.duantn.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter@Setter
public class ProductSearchCriteria {
    private Integer danhMucId;
    private Integer mauSacId;
    private Integer chatLieuId;
    private Integer kichThuocId;
    private BigDecimal giaMin;
    private BigDecimal giaMax;
}
