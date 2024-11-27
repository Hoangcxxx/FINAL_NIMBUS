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
@Table(name = "trang_thai_hoa_don")
public class TrangThaiHoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_trang_thai_hoa_don")
    private Integer idTrangThaiHoaDon;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    // Quan hệ với loại trạng thái, giả sử bạn có entity "LoaiTrangThai"
    @ManyToOne
    @JoinColumn(name = "id_loai_trang_thai")
    private LoaiTrangThai loaiTrangThai;

    // Quan hệ với hóa đơn
    @ManyToOne
    @JoinColumn(name = "id_hoa_don")
    private HoaDon hoaDon;
}
