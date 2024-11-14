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
@Table(name = "hinh_anh_san_pham")
public class HinhAnhSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_hinh_anh_san_pham")
    private Integer idHinhAnhSanPham;

    @ManyToOne
    @JoinColumn(name = "id_san_pham", nullable = false)  // Đảm bảo không null
    private SanPham sanPham;  // Giả sử bạn có một entity SanPham

    @Column(name = "url_anh", nullable = false)
    private String urlAnh;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "trang_thai", columnDefinition = "BIT DEFAULT 1")
    private Boolean trangThai = true; // Giá trị mặc định là true

    @Column(name = "thu_tu")
    private Integer thuTu;

    @Column(name = "loai_hinh_anh")
    private String loaiHinhAnh;

    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao = new Date(); // Giá trị mặc định là thời gian hiện tại

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat = new Date(); // Giá trị mặc định là thời gian hiện tại
}
