package com.example.duantn.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class SanPhamctDTO {
    private Integer idSanPhamCT; // ID sản phẩm chi tiết
    private Integer soLuong; // Số lượng sản phẩm
    private String tenMauSac; // Tên màu sắc
    private String tenKichThuoc; // Tên kích thước
    private String tenChatLieu; // Tên chất liệu
}
