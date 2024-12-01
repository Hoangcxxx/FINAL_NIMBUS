package com.example.duantn.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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

    @Column(name = "ma_nguoi_dung", unique = true, nullable = false)
    private String maNguoiDung;

    @Column(name = "ten_nguoi_dung", nullable = false)
    private String tenNguoiDung;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @Column(name = "sdt", nullable = false)
    private String sdt;

    @Column(name = "ngay_sinh", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate ngaySinh;

    @Column(name = "dia_chi", nullable = false)
    private String diaChi;

    @Column(name = "gioi_tinh", nullable = false)
    private String gioiTinh;

    @Column(name = "anh_dai_dien", nullable = false)
    private String anhDaiDien;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "ngay_tao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao; // Changed to LocalDateTime

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat; // Changed to LocalDateTime

    @ManyToOne
    @JoinColumn(name = "id_vai_tro")
    private VaiTro vaiTro;
}
