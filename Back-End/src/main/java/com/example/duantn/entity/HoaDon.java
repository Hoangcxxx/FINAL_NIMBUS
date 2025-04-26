package com.example.duantn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "id_nhan_vien")
    private Integer idNhanVien;

    @ManyToOne
    @JoinColumn(name = "id_voucher")
    private Voucher voucher;

    @ManyToOne
    @JoinColumn(name = "id_dia_chi_van_chuyen")
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
    @Column(name = "ma_voucher_ap_dung")
    private String maVoucherApDung;
    @Column(name = "ten_voucher_ap_dung")
    private String tenVoucherApDung;
    @Column(name = "gia_tri_giam_gia_ap_dung")
    private BigDecimal giaTriGiamGiaApDung;
    @Column(name = "kieu_giam_gia_ap_dung")
    private Boolean kieuGiamGiaApDung;
    @Column(name = "ngay_thanh_toan")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayThanhToan;
    @Column(name = "loai")
    private int loai;

    @ManyToOne
    @JoinColumn(name = "id_pt_thanh_toan_hoa_don")
    private PhuongThucThanhToanHoaDon phuongThucThanhToanHoaDon;
    @OneToMany(mappedBy = "hoaDon")
    @JsonIgnore // Bỏ qua danh sách trạng thái hóa đơn khi serialize
    private List<TrangThaiHoaDon> trangThaiHoaDons;

    @OneToMany(mappedBy = "hoaDon")
    @JsonIgnore // Bỏ qua danh sách chi tiết hóa đơn khi serialize
    private List<HoaDonChiTiet> hoaDonChiTietList;
    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Bỏ qua danh sách chi tiết hóa đơn khi serialize
    private List<HoaDonChiTiet> hoaDonChiTiets;

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL)
    private List<PhuongThucThanhToanHoaDon> phuongThucThanhToanHoaDons;

}