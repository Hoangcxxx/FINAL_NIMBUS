package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pt_thanh_toan_hoa_don")
public class PhuongThucThanhToanHoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_thanh_toan_hoa_don")
    private Integer idThanhToanHoaDon;
    @ManyToOne
    @JoinColumn(name = "id_pt_thanh_toan")
    private PhuongThucThanhToan phuongThucThanhToan;
    @ManyToOne
    @JoinColumn(name = "id_hoa_don")
    private HoaDon hoaDon; // Giả sử bạn có một entity HoaDon
    @Column(name = "ngay_giao_dich")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayGiaoDich;
    @Column(name = "mo_ta")
    private String moTa; // Đổi tên trường thành moTa cho phù hợp với bảng
    @Column(name = "trang_thai")
    private String trangThai; // Chỉnh sửa kiểu dữ liệu cho phù hợp với bảng
    @Column(name = "noi_dung_thanh_toan")
    private String noiDungThanhToan; // Đổi tên trường cho phù hợp với bảng

}
