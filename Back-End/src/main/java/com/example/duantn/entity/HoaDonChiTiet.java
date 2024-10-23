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
@Table(name = "hoa_don_chi_tiet")
public class HoaDonChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_hoa_don_chi_tiet")
    private int idHoaDonChiTiet;

    @ManyToOne
    @JoinColumn(name = "id_san_pham_chi_tiet", referencedColumnName = "Id_san_pham_chi_tiet")
    private SanPhamChiTiet sanPhamChiTiet;
    @ManyToOne
    @JoinColumn(name = "id_lich_su_hoa_don")
    private LichSuHoaDon lichSuHoaDon;
    @ManyToOne
    @JoinColumn(name = "id_hoa_don")
    private HoaDon hoaDon;

    @Column(name = "so_luong", nullable = false)
    private int soLuong;

    @Column(name = "tong_tien", precision = 18, scale = 2)
    private BigDecimal tongTien;
    @Column(name = "so_tien_thanh_toan", precision = 18, scale = 2)
    private BigDecimal soTienThanhToan;
    @Column(name = "tien_tra_lai", precision = 18, scale = 2)
    private BigDecimal tienTraLai;

    @Column(name = "ngay_tao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @Column(name = "trang_thai")
    private boolean trangThai;

    @Column(name = "mo_ta", columnDefinition = "NVARCHAR(MAX)")
    private String moTa;
}