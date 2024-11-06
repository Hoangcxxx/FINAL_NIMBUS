package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "kho_hang")
public class KhoHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_kho_hang")
    private Integer idKhoHang;
    @ManyToOne
    @JoinColumn(name = "id_san_pham_chi_tiet", nullable = false)
    private SanPhamChiTiet sanPhamChiTiet;

    @Column(name = "so_luong_them", nullable = false)
    private Integer soLuongThem;

    @Column(name = "ngay_cap_nhat", nullable = false)
    private LocalDateTime ngayCapNhat;

}
