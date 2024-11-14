package com.example.duantn.repository;

import com.example.duantn.entity.GioHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet, Integer> {
	List<GioHangChiTiet> findByGioHang_IdGioHang(Integer idGioHang);

	Optional<GioHangChiTiet> findByGioHang_IdGioHangAndSanPhamChiTiet_IdSanPhamChiTiet(Integer idGioHang,
			Integer idSanPhamChiTiet);


	@Query("SELECT CASE WHEN COUNT(g) > 0 THEN TRUE ELSE FALSE END "
			+ "FROM GioHangChiTiet g WHERE g.gioHang.idGioHang = :idGioHang "
			+ "AND g.sanPhamChiTiet.idSanPhamChiTiet = :idSanPhamChiTiet")
	Boolean existsByIdGioHangAndIdSanPhamChiTiet(@Param("idGioHang") Integer idGioHang,
			@Param("idSanPhamChiTiet") Integer idSanPhamChiTiet);

}
