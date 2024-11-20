package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gio_hang_voucher")
public class GioHangVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_gio_hang_voucher")
    private Integer idGioHangVoucher;
    @ManyToOne
    @JoinColumn(name = "id_gio_hang")
    private GioHang gioHang;
    @ManyToOne
    @JoinColumn(name = "id_voucher")
    private Voucher voucher;
    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao = new Date();
}
