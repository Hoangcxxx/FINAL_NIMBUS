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
@Table(name = "giam_gia_san_pham")
public class GiamGiaSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_giam_gia_san_pham")
    private Integer idGiamGiaSanPham;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @ManyToOne
    @JoinColumn(name = "Id_dot_giam_gia")
    private DotGiamGia dotGiamGia;

    @ManyToOne
    @JoinColumn(name = "id_san_pham")
    private SanPham sanPham;  // Thêm entity SanPham nếu chưa có
}
