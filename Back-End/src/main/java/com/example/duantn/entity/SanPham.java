package com.example.duantn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @Column(name = "ma_san_pham", unique = true, nullable = false)
    private String maSanPham;
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
    @JsonIgnore  // Ẩn các hình ảnh sản phẩm khi serialize thành JSON
    private List<HinhAnhSanPham> hinhAnhSanPham;

    @OneToMany(mappedBy = "sanPham")
    @JsonIgnore  // Ẩn các giảm giá sản phẩm khi serialize thành JSON
    private List<GiamGiaSanPham> giamGiaSanPham;

    public String getUrlAnh() {
        if (hinhAnhSanPham != null && !hinhAnhSanPham.isEmpty()) {
            return hinhAnhSanPham.get(0).getUrlAnh();
        }
        return null;  // Trả về null nếu không có hình ảnh
    }
    public String getAllUrlAnh() {
        if (hinhAnhSanPham != null && !hinhAnhSanPham.isEmpty()) {
            // Duyệt qua danh sách hinhAnhSanPham và ghép các URL lại thành một chuỗi, cách nhau bởi dấu phẩy
            return hinhAnhSanPham.stream()
                    .map(HinhAnhSanPham::getUrlAnh)
                    .collect(Collectors.joining(", "));
        }
        return null; // Trả về null nếu không có hình ảnh
    }

}
