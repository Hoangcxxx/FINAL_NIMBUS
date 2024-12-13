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
@Table(name = "lich_su_thanh_toan")
public class LichSuThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_lich_su_thanh_toan")
    private Integer idLichSuHoaDon;
    @Column(name = "so_tien_thanh_toan")
    private BigDecimal soTienThanhToan;
    @Column(name = "ngay_tao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;
    @Column(name = "ngay_giao_dich")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayGiaoDich;
    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;
    @Column(name = "trang_thai_thanh_toan")
    private Boolean trangThaiThanhToan;
    @ManyToOne
    @JoinColumn(name = "id_hoa_don")
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;
}
