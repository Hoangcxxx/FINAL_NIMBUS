package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_voucher") // Update to match the actual column name
    private Integer idVoucher;

    @Column(name = "ma_voucher", nullable = false, unique = true)
    private String maVoucher;

    @Column(name = "gia_tri_giam_gia")
    private Double phanTramGiamGia;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "trang_thai", columnDefinition = "BIT DEFAULT 1")
    private Boolean trangThai;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "ngay_bat_dau")
    private LocalDateTime ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDateTime ngayKetThuc;

    @Column(name = "ngay_tao", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayCapNhat;

    @Column(name = "id_loai_voucher") // Added field for id_loai_voucher based on SQL query
    private Integer idLoaiVoucher; // Assuming this is an Integer type, adjust as necessary
}
