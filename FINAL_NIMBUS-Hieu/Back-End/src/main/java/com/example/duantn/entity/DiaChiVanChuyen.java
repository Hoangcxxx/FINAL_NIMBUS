package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dia_chi_van_chuyen")
public class DiaChiVanChuyen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dia_chi_van_chuyen")
    private Integer idDiaChiVanChuyen;

    @Column(name = "tinh", nullable = false)
    private String tinh;

    @Column(name = "huyen", nullable = false)
    private String huyen;

    @Column(name = "xa", nullable = false)
    private String xa;

    @Column(name = "so_tien_van_chuyen", nullable = false)
    private BigDecimal soTienVanChuyen;

    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "trang_thai", nullable = false)
    private Boolean trangThai;

    @Column(name = "mo_ta")
    private String moTa;
}
