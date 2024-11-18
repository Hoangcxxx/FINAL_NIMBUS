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

    private String ten_loai_voucher;
    private String mo_ta;

    @Column(columnDefinition = "DATETIME DEFAULT GETDATE()")
    private Date ngay_tao;

    @Column(columnDefinition = "DATETIME DEFAULT GETDATE()")
    private Date ngay_cap_nhat;
}
