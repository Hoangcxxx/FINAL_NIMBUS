package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "thong_bao")  // Tên bảng trong cơ sở dữ liệu
public class ThongBao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Tự động tăng giá trị của Id
    @Column(name = "Id_thong_bao")  // Tên cột trong cơ sở dữ liệu
    private Integer idThongBao;

    @ManyToOne  // Nhiều thông báo có thể thuộc về một người dùng
    @JoinColumn(name = "id_nguoi_dung")  // Tên cột khóa ngoại
    private NguoiDung nguoiDung;  // Liên kết đến bảng người dùng
    @ManyToOne  // Nhiều thông báo có thể thuộc về một người dùng
    @JoinColumn(name = "id_loai_thong_bao")  // Tên cột khóa ngoại
    private LoaiThongBao loaiThongBao;  // Liên kết đến bảng người dùng

    @Column(name = "noi_dung", columnDefinition = "NVARCHAR(MAX)")  // Tên cột "noi_dung"
    private String noiDung;  // Nội dung của thông báo

    @Column(name = "trang_thai", columnDefinition = "BIT DEFAULT 1")  // Tên cột và giá trị mặc định
    private Boolean trangThai = true;  // Trạng thái mặc định là true (hiện)

    @Column(name = "ngay_gui", updatable = false)  // Cột này không thể cập nhật sau khi đã tạo
    @Temporal(TemporalType.TIMESTAMP)  // Định dạng thời gian
    private Date ngayGui = new Date();  // Ngày gửi thông báo, mặc định là ngày hiện tại

}
