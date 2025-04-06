package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "Id_dia_chi_van_chuyen")
    private Integer idDiaChiVanChuyen;

    @ManyToOne
    @JoinColumn(name = "id_tinh")
    private Tinh tinh;

    @ManyToOne
    @JoinColumn(name = "id_huyen")
    private Huyen huyen;

    @ManyToOne
    @JoinColumn(name = "id_xa")
    private Xa xa;

    @Column(name = "dia_chi_cu_the")
    private String diaChiCuThe;

    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "mo_ta")
    private String moTa;

    @ManyToOne
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;
}
