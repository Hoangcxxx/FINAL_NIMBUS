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
@Table(name = "gio_hang_chi_tiet")
public class GioHangChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_gio_hang_chi_tiet")
    private Integer idGioHangChiTiet;
    @ManyToOne
    @JoinColumn(name = "id_san_pham_chi_tiet", referencedColumnName = "Id_san_pham_chi_tiet")
    private SanPhamChiTiet sanPhamChiTiet;
    @ManyToOne
    @JoinColumn(name = "id_gio_hang")
    private GioHang gioHang;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "don_gia")
    private Double donGia;

    @Column(name = "thanh_tien")
    private Double thanhTien;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

}
