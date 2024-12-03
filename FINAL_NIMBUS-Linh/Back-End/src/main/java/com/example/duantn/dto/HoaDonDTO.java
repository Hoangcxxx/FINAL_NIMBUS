package com.example.duantn.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HoaDonDTO {
        private int idHoaDon;
        private String maHoaDon;
        private String tenNguoiDung;
        private String sdtNguoiDung;
        private Date ngayTao;
        private String tenPhuongThuc;
        private BigDecimal tongTien;
        private int loai;
}
