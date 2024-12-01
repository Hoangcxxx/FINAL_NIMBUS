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
    private int id_loai_voucher;

    @Column(name = "ten_loai_voucher", nullable = false)
    private String tenLoaiVoucher;
    private String mo_ta;

    @Column(columnDefinition = "DATETIME DEFAULT GETDATE()")
    private Date ngay_tao;

    @Column(columnDefinition = "DATETIME DEFAULT GETDATE()")
    private Date ngay_cap_nhat;
}
