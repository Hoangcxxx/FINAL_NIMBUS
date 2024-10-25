package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "voucher_san_pham")
public class VoucherSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_voucher_san_pham")
    private Integer idVoucherSanPham;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @ManyToOne
    @JoinColumn(name = "id_voucher")
    private Voucher voucher;

    @ManyToOne
    @JoinColumn(name = "id_san_pham")
    private SanPham sanPham;
}
