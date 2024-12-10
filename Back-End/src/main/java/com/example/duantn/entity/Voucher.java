package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

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

    @Column(name = "ma_voucher", unique = true, nullable = false)
    private String maVoucher;
    @Column(name = "ten_voucher")
    private String  tenVoucher;

    @Column(name = "gia_tri_giam_gia")
    private BigDecimal giaTriGiamGia;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "gia_tri_toi_da")
    private BigDecimal giaTriToiDa;

    @Column(name = "kieu_giam_gia")
    private Boolean kieuGiamGia;

    @Column(name = "so_tien_toi_thieu")
    private BigDecimal soTienToiThieu;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "ngay_bat_dau")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayKetThuc;

    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao = new Date();

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat = new Date();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_trang_thai_giam_gia")
    private TrangThaiGiamGia trangThaiGiamGia;

    @PrePersist
    protected void onCreate() {
        ngayTao = new Date();
        ngayCapNhat = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = new Date();
    }
    @Transient
    private boolean isUsable;
    public boolean getIsUsable() {
        return isUsable;
    }

    public void setIsUsable(boolean isUsable) {
        this.isUsable = isUsable;
    }
    @Transient
    private boolean kiemtravoucher;
}