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
    @Column(name = "ten_loai_voucher")
    private String tenLoaiVoucher; // Tên loại voucher
    @Column(name = "mo_ta")
    private String moTa; // Mô tả chi tiết
}
