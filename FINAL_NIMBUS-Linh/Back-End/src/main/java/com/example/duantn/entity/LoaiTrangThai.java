package com.example.duantn.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "loai_trang_thai")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoaiTrangThai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_loai_trang_thai")
    private Integer idLoaiTrangThai;

    @Column(name = "ten_loai_trang_thai", nullable = false, length = 100)
    private String tenLoaiTrangThai;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
}
