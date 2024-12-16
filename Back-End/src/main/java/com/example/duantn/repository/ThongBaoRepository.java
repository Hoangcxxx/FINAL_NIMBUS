package com.example.duantn.repository;

import com.example.duantn.entity.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Integer> {
    // Tìm thông báo theo id người dùng và loại thông báo, sắp xếp theo ngày gửi mới nhất
    @Query("SELECT t FROM ThongBao t " +
            "WHERE t.nguoiDung.idNguoiDung = :idNguoiDung " +
            "AND t.ngayGui = (SELECT MAX(t2.ngayGui) FROM ThongBao t2 " +
            "WHERE t2.nguoiDung.idNguoiDung = :idNguoiDung " +
            "AND t2.loaiThongBao.idLoaiThongBao = t.loaiThongBao.idLoaiThongBao) " +
            "ORDER BY t.ngayGui DESC")
    List<ThongBao> findByNguoiDung_IdNguoiDungOrderByNgayGuiDesc(Integer idNguoiDung);
}
