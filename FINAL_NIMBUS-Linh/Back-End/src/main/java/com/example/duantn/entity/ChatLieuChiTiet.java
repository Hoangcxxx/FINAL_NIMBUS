package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "chat_lieu_chi_tiet")
public class ChatLieuChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_chat_lieu_chi_tiet")
    private Integer idChatLieuChiTiet;

    @ManyToOne
    @JoinColumn(name = "Id_chat_lieu")
    private ChatLieu chatLieu;

    @Column(name = "ngay_tao", updatable = false)
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    private Date ngayCapNhat;
}
