package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "loai_trang_thai")
public class LoaiTrangThai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_loai_trang_thai")
    private Integer idLoaiTrangThai;


    @Column(name = "ten_loai_trang_thai")  // Đảm bảo cột này tồn tại trong cơ sở dữ liệu
    private String tenloaitrangthai;

    // Quan hệ với TrangThaiHoaDon
    @OneToMany(mappedBy = "loaiTrangThai", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrangThaiHoaDon> trangThaiHoaDons;
}
