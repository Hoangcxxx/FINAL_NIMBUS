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
@Table(name = "dia_chi_van_chuyen")
public class DiaChiVanChuyen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_dia_chi_van_chuyen")
    private Integer idDiaChiVanChuyen;

    @Column(name = "tinh")
    private String tinh;

    @Column(name = "huyen")
    private String huyen;

    @Column(name = "xa")
    private String xa;



    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @Column(name = "trang_thai")
    private Boolean trangThai; // Chỉnh sửa kiểu dữ liệu cho trang_thai

    @Column(name = "mo_ta")
    private String moTa;

    // Nếu cần thiết, thêm mối quan hệ với phi_van_chuyen
    @ManyToOne
    @JoinColumn(name = "id_phi_van_chuyen")
    private PhiVanChuyen phiVanChuyen;
    @ManyToOne
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;
}
