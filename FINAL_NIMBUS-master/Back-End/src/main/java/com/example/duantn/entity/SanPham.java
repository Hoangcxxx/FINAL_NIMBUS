package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "san_pham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_san_pham")
    private Integer idSanPham;
    @Column(name = "ten_san_pham", nullable = false)
    private String tenSanPham;
    @Column(name = "gia_ban", nullable = false)
    private BigDecimal giaBan;
    @Column(name = "mo_ta")
    private String moTa;
    @Column(name = "trang_thai", nullable = false)
    private Boolean trangThai;

    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;


    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @ManyToOne
    @JoinColumn(name = "id_danh_muc")
    private DanhMuc danhMuc;
    @OneToMany(mappedBy = "sanPham")
    private List<HinhAnhSanPham> hinhAnhSanPham;
    public String getUrlAnh() {
        if (hinhAnhSanPham != null && !hinhAnhSanPham.isEmpty()) {
            return hinhAnhSanPham.get(0).getUrlAnh();
        }
        return null;  // Trả về null nếu không có hình ảnh
    }
}