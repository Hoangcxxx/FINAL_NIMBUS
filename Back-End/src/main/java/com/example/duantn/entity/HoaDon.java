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
@Entity
@Table(name = "hoa_don")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_hoa_don")
    private Integer idHoaDon;

    @Column(name = "ma_hoa_don", nullable = false, unique = true)
    private String maHoaDon;

    @ManyToOne
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "id_voucher")
    private Voucher voucher;

    @ManyToOne
    @JoinColumn(name = "Id_dia_chi_van_chuyen")
    private DiaChiVanChuyen diaChiVanChuyen;

    @Column(name = "ten_nguoi_nhan", nullable = false)
    private String tenNguoiNhan;

    @Column(name = "phi_ship")
    private BigDecimal phiShip;

    @Column(name = "dia_chi", nullable = false)
    private String diaChi;

    @Column(name = "sdt_nguoi_nhan")
    private String sdtNguoiNhan;

    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "mo_ta")
    private String moTa;
    @Column(name = "thanh_tien")
    private BigDecimal thanhTien;
    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "ngay_thanh_toan")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayThanhToan;

    @ManyToOne
    @JoinColumn(name = "id_pt_thanh_toan_hoa_don")
    private PhuongThucThanhToanHoaDon phuongThucThanhToanHoaDon;
    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HoaDonChiTiet> hoaDonChiTiets;

}
