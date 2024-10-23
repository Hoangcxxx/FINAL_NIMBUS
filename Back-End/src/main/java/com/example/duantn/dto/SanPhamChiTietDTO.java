package com.example.duantn.dto;

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

    public class SanPhamChiTietDTO {
        private int idSanPhamChiTiet;
        private int soLuong;
        private String danhMuc;
        private String tenMauSac;
        private String tenKichThuoc;
        private String tenChatLieu;
        private String tenSanPham;
        private BigDecimal giaBan;
        private boolean trangThai;
        private Date ngayTao;

        private Date ngayCapNhat;
    }
