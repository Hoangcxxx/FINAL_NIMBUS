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
@Table(name = "phi_van_chuyen")
public class PhiVanChuyen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_phi_van_chuyen")
    private Integer idPhiVanChuyen;
    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @Column(name = "mo_ta")
    private String moTa; // Thêm trường mo_ta

    // Nếu cần, bạn có thể thiết lập quan hệ với DiaChiVanChuyen
    @ManyToOne
    @JoinColumn(name = "id_dia_chi_van_chuyen", insertable = false, updatable = false)
    private DiaChiVanChuyen diaChiVanChuyen;

    // Nếu cần, bạn có thể thiết lập quan hệ với HoaDon
    @ManyToOne
    @JoinColumn(name = "id_hoa_don", insertable = false, updatable = false)
    private HoaDon hoaDon; // Giả sử bạn có một entity HoaDon
}
