package com.example.duantn.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "nguoi_dung")
public class NguoiDung implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_nguoi_dung")
    private Integer idNguoiDung;

    @Column(name = "ten_nguoi_dung", nullable = false)
    private String tenNguoiDung;
    @Column(name = "ma_nguoi_dung", nullable = false)
    private String maNguoiDung;
    @Column(name = "sdt_nguoi_dung", nullable = false)
    private String sdtNguoiDung;
    @Column(name = "Dia_Chi", nullable = false)
    private String diaChi;
    @Column(name = "Gioi_Tinh", nullable = false)
    private String gioiTinh;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @Column(name = "trang_thai")
    private Boolean trangThai;
    @ManyToOne
    @JoinColumn(name = "id_vai_tro", referencedColumnName = "Id_vai_tro")
    private VaiTro vaiTro;

/*@Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;*/

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(vaiTro.getTen()));
    }

    @Override
    public String getPassword() {
        return matKhau;
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
