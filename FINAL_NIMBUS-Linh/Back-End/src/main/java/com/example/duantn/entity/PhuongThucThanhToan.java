    package com.example.duantn.entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.math.BigDecimal;
    import java.util.Date;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "pt_thanh_toan")
    public class PhuongThucThanhToan {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "Id_pt_thanh_toan")
        private Integer id;

        @Column(name = "ma_thanh_toan", nullable = false, unique = true)
        private String maThanhToan;

        @Column(name = "ten_phuong_thuc", nullable = false)
        private String tenPhuongThuc;

        @Column(name = "ngay_tao")
        @Temporal(TemporalType.TIMESTAMP)
        private Date ngayTao;

        @Column(name = "ngay_cap_nhat")
        @Temporal(TemporalType.TIMESTAMP)
        private Date ngayCapNhat;

        @Column(name = "trang_thai")
        private boolean trangThai;

        @Column(name = "mo_ta", columnDefinition = "NVARCHAR(MAX)")
        private String moTa;
    }
