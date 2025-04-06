package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "voucher_nguoi_dung")
public class VoucherNguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_voucher_nguoi_dung")
    private Integer idVoucherNguoiDung;
    @ManyToOne
    @JoinColumn(name = "id_voucher")
    private Voucher voucher;
    @ManyToOne
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

}
