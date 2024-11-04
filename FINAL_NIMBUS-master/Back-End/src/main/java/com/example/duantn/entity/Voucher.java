package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;
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
    private int id_voucher;

    @Column(name = "ma_voucher", nullable = false, unique = true, length = 50)
    private String maVoucher;

    @Column(name = "gia_tri_giam_gia", precision = 18, scale = 2)
    private BigDecimal gia_tri_giam_gia;

    @Column(name = "so_luong")
    private int so_luong;

    @Column(name = "gia_tri_toi_da", precision = 18, scale = 2)
    private BigDecimal giaTriToiDa;

    @Column(name = "so_tien_toi_thieu", precision = 18, scale = 2)
    private BigDecimal soTienToiThieu;

    @Column(name = "trang_thai", columnDefinition = "BIT DEFAULT 1")
    private boolean trangThai;

    @Column(name = "mo_ta", columnDefinition = "NVARCHAR(MAX)")
    private String mo_ta;

    @Column(name = "ngay_bat_dau")
    private Date ngay_bat_dau;

    @Column(name = "ngay_ket_thuc")
    private Date ngayKetThuc;

    @Column(name = "ngay_tao", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private Date ngay_tao;

    @Column(name = "ngay_cap_nhat", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private Date ngay_cap_nhat;

    @ManyToOne
    @JoinColumn(name = "id_loai_voucher", nullable = false)
    private LoaiVoucher loaiVoucher;
}
