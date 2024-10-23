package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "gio_hang")
public class GioHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_gio_hang")
    private Integer idGioHang;
    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;
    @Column(name = "id_nguoi_dung", nullable = false)
    private Integer idNguoiDung;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;
    @OneToMany(mappedBy = "gioHang", cascade = CascadeType.ALL)
    private List<GioHangChiTiet> gioHangChiTietList;
}
