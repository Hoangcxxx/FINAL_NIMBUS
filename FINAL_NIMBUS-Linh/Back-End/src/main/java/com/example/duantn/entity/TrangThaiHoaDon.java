package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trang_thai_hoa_don")
public class TrangThaiHoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_trang_thai_hoa_don")
    private Integer id;

    @Column(name = "mo_ta", columnDefinition = "NVARCHAR(MAX)")
    private String moTa;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ngay_tao", updatable = false)
    private Date ngayTao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ngay_cap_nhat")
    private Date ngayCapNhat;

    @ManyToOne
    @JoinColumn(name = "id_loai_trang_thai", referencedColumnName = "Id_loai_trang_thai")
    private LoaiTrangThai loaiTrangThai;

    @ManyToOne
    @JoinColumn(name = "id_hoa_don", referencedColumnName = "Id_hoa_don")
    private HoaDon hoaDon;
}
