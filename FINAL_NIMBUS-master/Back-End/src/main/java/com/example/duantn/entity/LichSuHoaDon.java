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
@Table(name = "lich_su_hoa_don")
public class LichSuHoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_lich_su_hoa_don")
    private Integer idLichSuHoaDon;

    @Column(name = "so_tien_thanh_toan")
    private BigDecimal soTienThanhToan;
    @Column(name = "ngay_giao_dich")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayGiaoDich;
    @ManyToOne
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;
}
