package com.example.duantn.repository;

import com.example.duantn.entity.NguoiDung;
import com.example.duantn.query.NguoiDungQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    @Query(value = "SELECT * FROM nguoi_dung nd WHERE nd.id_vai_tro = 2", nativeQuery = true)
    List<NguoiDung> getSanPhamById();
    List<NguoiDung> findByVaiTro_IdVaiTro(Integer idVaiTro);

    Optional<NguoiDung> findByEmail(String email);

    Optional<NguoiDung> findBySdt(String sdt);
    boolean existsByMaNguoiDung(String maNguoiDung);
    boolean existsByEmail(String email);
    @Query(value = NguoiDungQuery.GET_TIM_KIEM_SDT_KH, nativeQuery = true)
    List<Object[]> getSearchKhachHang(String phonePrefix);

}
