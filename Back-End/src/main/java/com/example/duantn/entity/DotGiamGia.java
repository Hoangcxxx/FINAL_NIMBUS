package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "dot_giam_gia")
public class DotGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_dot_giam_gia")
    private Integer idDotGiamGia;

    @Column(name = "ten_dot_giam_gia", nullable = false)
    private String tenDotGiamGia;

    @Column(name = "gia_tri_giam_gia")
    private BigDecimal giaTriGiamGia;

    @Column(name = "trang_thai")
    private Boolean trangThai;

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
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @OneToMany(mappedBy = "dotGiamGia")
    private List<GiamGiaSanPham> giamGiaSanPhams;
}
