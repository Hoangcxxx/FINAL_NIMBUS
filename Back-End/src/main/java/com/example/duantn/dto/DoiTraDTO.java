package com.example.duantn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoiTraDTO {
    private Integer idHoaDon;           // ID hóa đơn liên kết
    private Integer idSanPhamChiTiet;   // ID sản phẩm chi tiết cần đổi trả
    private Integer soLuong;            // Số lượng sản phẩm đổi trả
    private String lyDo;                // Lý do đổi trả
    private Boolean trangThai;          // Trạng thái đổi trả
    private BigDecimal tongTien;        // Đảm bảo BigDecimal nhận giá trị từ JSON
}
