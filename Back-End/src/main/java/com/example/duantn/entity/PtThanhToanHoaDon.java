package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pt_thanh_toan_hoa_don")
public class PtThanhToanHoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_thanh_toan_hoa_don")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_pt_thanh_toan", nullable = false)
    private PhuongThucThanhToan phuongThucThanhToan;

    @ManyToOne
    @JoinColumn(name = "id_hoa_don", referencedColumnName = "Id_hoa_don")
    private HoaDon hoaDon;

    @Column(name = "ngay_giao_dich")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayGiaoDich;

    @Column(name = "mo_ta", columnDefinition = "NVARCHAR(MAX)")
    private String moTa;

    @Column(name = "trang_thai", columnDefinition = "NVARCHAR(MAX)")
    private String trangThai;

    @Column(name = "noi_dung_thanh_toan", columnDefinition = "NVARCHAR(MAX)")
    private String noiDungThanhToan;
}
