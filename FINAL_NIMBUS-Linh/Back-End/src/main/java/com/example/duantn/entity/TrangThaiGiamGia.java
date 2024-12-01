package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trang_thai_giam_gia")
public class TrangThaiGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_trang_thai_giam_gia")
    private int idTrangThaiGiamGia;

    @Column(name = "ten_trang_thai_giam_gia", nullable = false, length = 100)
    private String tenTrangThaiGiamGia;

    @Column(name = "mo_ta", columnDefinition = "NVARCHAR(MAX)")
    private String moTa;

    @Column(name = "ngay_tao", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayCapNhat;
}
