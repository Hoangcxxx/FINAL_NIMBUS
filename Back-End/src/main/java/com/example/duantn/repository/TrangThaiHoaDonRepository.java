package com.example.duantn.repository;

import com.example.duantn.entity.GioHangChiTiet;
import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.TrangThaiHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrangThaiHoaDonRepository extends JpaRepository<TrangThaiHoaDon, Integer> {

    // Phương thức để tìm trạng thái hóa đơn theo idHoaDon và idLoaiTrangThai
    List<TrangThaiHoaDon> findByHoaDon_IdHoaDonAndLoaiTrangThai_IdLoaiTrangThai(Integer idHoaDon, Integer idLoaiTrangThai);
    @Modifying
    @Query("DELETE FROM TrangThaiHoaDon t WHERE t.hoaDon.idHoaDon = :hoaDonId")
    void deleteByHoaDonId(@Param("hoaDonId") Integer hoaDonId);
    @Query(value = "select tthd.Id_trang_thai_hoa_don, tthd.mo_ta,tthd.ngay_tao,tthd.ngay_cap_nhat,l.ten_loai_trang_thai,tthd.id_hoa_don\n" +
            "from trang_thai_hoa_don tthd\n" +
            "join loai_trang_thai l on l.Id_loai_trang_thai = tthd.id_loai_trang_thai\n" +
            "where id_hoa_don = :idHoaDon",nativeQuery = true)
    List<Object[]> findAllByidHoaDon(Integer idHoaDon);

}
