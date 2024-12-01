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
    @Column(name = "kieu_giam_gia")
    private Boolean kieuGiamGia;

    @Column(name = "gia_tri_giam_gia", precision = 18, scale = 2)
    private BigDecimal giaTriGiamGia;

    @Column(name = "so_luong")
    private int soLuong;

    @Column(name = "gia_tri_toi_da", precision = 18, scale = 2)
    private BigDecimal giaTriToiDa;

    @Column(name = "so_tien_toi_thieu", precision = 18, scale = 2)
    private BigDecimal soTienToiThieu;

    @Column(name = "mo_ta", columnDefinition = "NVARCHAR(MAX)")
    private String mo_ta;

    @Column(name = "ngay_bat_dau")
    private Date ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private Date ngayKetThuc;

    @Column(name = "ngay_tao", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private Date ngay_tao;

    @Column(name = "ngay_cap_nhat", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private Date ngay_cap_nhat;


    @ManyToOne
    @JoinColumn(name = "id_trang_thai_giam_gia", nullable = false)
    private TrangThaiGiamGia trangThaiGiamGia;
    @Transient
    private boolean isUsable;
    public boolean getIsUsable() {
        return isUsable;
    }

    public void setIsUsable(boolean isUsable) {
        this.isUsable = isUsable;
    }
}
