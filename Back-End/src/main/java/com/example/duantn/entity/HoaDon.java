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
@Table(name = "hoa_don")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_hoa_don")
    private int idHoaDon;

    @Column(name = "ma_hoa_don", nullable = false, unique = true, length = 50)
    private String maHoaDon;

    @ManyToOne
    @JoinColumn(name = "id_nguoi_dung", referencedColumnName = "Id_nguoi_dung")
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "id_trang_thai_hoa_don", referencedColumnName = "Id_trang_thai_hoa_don")
    private TrangThaiHoaDon trangThaiHoaDon;

    @Column(name = "ten_nguoi_nhan")
    private String tenNguoiNhan;

    @Column(name = "phi_ship")
    private BigDecimal phiShip;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "sdt_nguoi_nhan", length = 15)
    private String sdtNguoiNhan;

    @Column(name = "thanh_tien")
    private BigDecimal thanhTien;
    @Column(name = "ngay_tao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "mo_ta", columnDefinition = "NVARCHAR(MAX)")
    private String moTa;

    @Column(name = "trang_thai")
    private boolean trangThai;

    @Column(name = "ngay_thanh_toan")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayThanhToan;
    @Column(name = "loai")
    private String loai;

    @ManyToOne
    @JoinColumn(name = "id_pt_thanh_toan_hoa_don")
    private PtThanhToanHoaDon ptThanhToanHoaDon;
    @OneToMany(mappedBy = "hoaDon")
    private List<HoaDonChiTiet> hoaDonChiTietList;
}
