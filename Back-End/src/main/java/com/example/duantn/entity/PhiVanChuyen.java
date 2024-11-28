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
@Table(name = "phi_van_chuyen")
public class PhiVanChuyen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_phi_van_chuyen")
    private Integer idPhiVanChuyen;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "so_tien_van_chuyen")
    private BigDecimal soTienVanChuyen; // Đảm bảo sử dụng BigDecimal cho tiền

    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @Column(name = "mo_ta")
    private String moTa; // Mô tả thêm cho phí vận chuyển

    // Quan hệ với DiaChiVanChuyen
    @ManyToOne
    @JoinColumn(name = "id_dia_chi_van_chuyen") // Liên kết với bảng DiaChiVanChuyen
    private DiaChiVanChuyen diaChiVanChuyen;

    // Quan hệ với HoaDon (Nếu cần)
    @ManyToOne
    @JoinColumn(name = "id_hoa_don") // Liên kết với bảng HoaDon
    private HoaDon hoaDon;

    // Khởi tạo ngày tạo nếu chưa có
    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) {
            ngayTao = new Date(); // Gán ngày tạo là thời gian hiện tại nếu chưa có
        }
        if (ngayCapNhat == null) {
            ngayCapNhat = new Date(); // Gán ngày cập nhật là thời gian hiện tại
        }
    }

    // Khởi tạo ngày cập nhật mỗi khi có thay đổi
    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = new Date(); // Cập nhật thời gian mỗi khi thay đổi
    }
}
