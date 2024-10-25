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
@Table(name = "loai_voucher")
public class LoaiVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_loai_voucher")
    private Integer idLoaiVoucher;

    @Column(name = "ten_loai_voucher", nullable = false)
    private String tenLoaiVoucher;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;
}
