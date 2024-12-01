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
@Table(name = "nguoi_dung")
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_nguoi_dung")
    private Integer id;

    @Column(name = "ten_nguoi_dung", nullable = false, length = 100)
    private String tenNguoiDung;

    @Column(name = "ma_nguoi_dung")
    private String maNguoiDung;

    @Column(name = "Email")
    private String email;

    @Column(name = "sdt", length = 15)
    private String sdtNguoiDung;

    @Column(name = "Ngay_Sinh")
    @Temporal(TemporalType.DATE)
    private Date ngaySinh;

    @Column(name = "Dia_Chi", length = 255)
    private String diaChi;

    @Column(name = "Gioi_Tinh", length = 10)
    private String gioiTinh;

    @Column(name = "Mat_Khau", nullable = false, length = 255)
    private String matKhau;

    @Column(name = "Anh_Dai_Dien", length = 255)
    private String anhDaiDien;

    @Column(name = "Trang_thai", columnDefinition = "BIT DEFAULT 1")
    private Boolean trangThai;

    @Column(name = "ngay_cap_nhat", columnDefinition = "DATETIME DEFAULT GETDATE()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @ManyToOne
    @JoinColumn(name = "id_vai_tro")
    private VaiTro vaiTro;
}
