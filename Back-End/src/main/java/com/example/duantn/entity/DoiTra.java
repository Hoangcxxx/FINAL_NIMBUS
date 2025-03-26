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
@Table(name = "doi_tra")
public class DoiTra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_doi_tra")
    private Integer idDoiTra;

    @ManyToOne
    @JoinColumn(name = "id_hoa_don", referencedColumnName = "Id_hoa_don", nullable = false)
    private HoaDon hoaDon; // Tham chiếu đến bảng hoa_don

    @ManyToOne
    @JoinColumn(name = "id_san_pham_chi_tiet", referencedColumnName = "Id_san_pham_chi_tiet", nullable = false)
    private SanPhamChiTiet sanPhamChiTiet; // Tham chiếu đến bảng san_pham_chi_tiet

    @Column(name = "so_luong")
    private Integer soLuong;
    @Column(name = "tong_tien")
    private BigDecimal tongTien;

    @Column(name = "ly_do", length = 225)
    private String lyDo;

    @Column(name = "trang_thai", columnDefinition = "BIT DEFAULT 1")
    private Boolean trangThai;

    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

}
