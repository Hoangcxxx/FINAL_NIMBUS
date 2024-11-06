package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "nguoi_dung")
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_nguoi_dung")
    private Integer idNguoiDung;
    @Column(name = "ma_nguoi_dung", unique = true, nullable = false) // Cột ma_nguoi_dung không thể NULL
    private String maNguoiDung;  // Thêm trường này
    @Column(name = "ten_nguoi_dung", nullable = false)
    private String tenNguoiDung;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @Column(name = "sdt", unique = true, nullable = false)
    private String sdt;

    @Column(name = "ngay_sinh", nullable = false)
    private LocalDate ngaySinh; // Changed to LocalDate

    @Column(name = "dia_chi", nullable = false)
    private String diaChi;

    @Column(name = "gioi_tinh", nullable = false)
    private String gioiTinh;

    @Column(name = "anh_dai_dien", nullable = false)
    private String anhDaiDien;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime ngayTao; // Changed to LocalDateTime

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime ngayCapNhat; // Changed to LocalDateTime

    @ManyToOne
    @JoinColumn(name = "id_vai_tro")
    private VaiTro vaiTro;
}
