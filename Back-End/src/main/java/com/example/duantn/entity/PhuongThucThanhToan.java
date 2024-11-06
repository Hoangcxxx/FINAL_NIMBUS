package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "pt_thanh_toan")
public class PhuongThucThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_pt_thanh_toan")
    private Integer idPTThanhToan;

    @Column(name = "ma_thanh_toan", nullable = false, unique = true)
    private String maThanhToan;

    @Column(name = "ten_phuong_thuc", nullable = false)
    private String tenPhuongThuc;

    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @Column(name = "trang_thai", nullable = false)
    private Boolean trangThai;

    @Column(name = "mo_ta")
    private String moTa;

}
