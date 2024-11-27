package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "loai_trang_thai")
public class LoaiTrangThai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_loai_trang_thai")
    private Integer idLoaiTrangThai;

//    @Column(name = "ten_trang_thai", nullable = false)
//    private String tenTrangThai; // Ví dụ: "Đang xử lý", "Đã giao", "Đã hủy"
}
