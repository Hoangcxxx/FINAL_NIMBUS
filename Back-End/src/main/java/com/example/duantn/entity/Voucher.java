package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_voucher")
    private Integer idVoucher;
    @Column(name = "ma_voucher")
    private String maVoucher;
    @Column(name = "gia_tri_giam_gia")
    private BigDecimal giaTriGiamGia;
    @Column(name = "so_luong")
    private Integer soLuong;
    @Column(name = "gia_toi_thieu")
    private BigDecimal giaToiThieu;
    @Column(name = "trang_thai")
    private Boolean trangThai;
    @Column(name = "mo_ta")
    private String moTa;
    @Column(name = "ngay_bat_dau")
    private LocalDateTime ngayBatDau;
    @Column(name = "ngay_ket_thuc")
    private LocalDateTime ngayKetThuc;
}
