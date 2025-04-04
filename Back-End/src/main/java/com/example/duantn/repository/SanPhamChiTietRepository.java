package com.example.duantn.repository;

import com.example.duantn.entity.GioHangChiTiet;
import com.example.duantn.entity.SanPhamChiTiet;
import com.example.duantn.query.SanPhamChiTietQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {

	@Query(value = SanPhamChiTietQuery.GET_SAN_PHAM_BY_ID, nativeQuery = true)
	List<Object[]> getSanPhamById(@Param("idSanPhamCT") Integer idSanPhamCT);

	Optional<SanPhamChiTiet> findById(Integer id);

	@Query(value = SanPhamChiTietQuery.GET_MAU_SAC_BY_ID_SAN_PHAM, nativeQuery = true)
	List<Object[]> getMauSacByIdSanPham(@Param("idSanPhamCT") Integer idSanPhamCT);

	@Query(value = SanPhamChiTietQuery.GET_KICH_THUOC_BY_ID_SAN_PHAM, nativeQuery = true)
	List<Object[]> getKichThuocByIdSanPham(@Param("idSanPhamCT") Integer idSanPhamCT);

	@Query(value = SanPhamChiTietQuery.GET_CHAT_LIEU_BY_ID_SAN_PHAM, nativeQuery = true)
	List<Object[]> getChatLieuByIdSanPham(@Param("idSanPhamCT") Integer idSanPhamCT);

	@Query(value = SanPhamChiTietQuery.GET_SAN_PHAM_CT_BY_ID_SAN_PHAM, nativeQuery = true)
	List<Object[]> getSanPhamCTByIdSanPham(@Param("idSanPhamCT") Integer idSanPhamCT);

	@Query(value = "SELECT * FROM san_pham_chi_tiet " + "WHERE id_mau_sac_chi_tiet = :idMauSac "
			+ "AND id_kich_thuoc_chi_tiet = :idKichThuoc " + "AND id_chat_lieu_chi_tiet = :idChatLieu "
			+ "AND id_san_pham = :idSanPham", nativeQuery = true)
	Optional<SanPhamChiTiet> findByAttributes(@Param("idMauSac") Integer idMauSac,
			@Param("idKichThuoc") Integer idKichThuoc, @Param("idChatLieu") Integer idChatLieu,
			@Param("idSanPham") Integer idSanPham);

	// Get material by product ID
	@Modifying
	@Transactional
	@Query("DELETE FROM SanPhamChiTiet s WHERE s.idSanPhamChiTiet = :idSanPhamCT")
	void deleteById(@Param("idSanPhamCT") Integer idSanPhamCT);

	@Modifying
	@Transactional
	@Query(value = "UPDATE san_pham_chi_tiet SET so_luong = :soLuong WHERE Id_san_pham_chi_tiet = :idSanPhamCT", nativeQuery = true)
	void updateSoLuongSanPhamCT(@Param("soLuong") Integer soLuong, @Param("idSanPhamCT") Integer idSanPhamCT);

	@Query(value = "SELECT so_luong FROM san_pham_chi_tiet WHERE Id_san_pham_chi_tiet = :idSanPhamCT", nativeQuery = true)
	Integer findQuantityById(@Param("idSanPhamCT") Integer idSanPhamCT);

//    List<SanPhamChiTiet> findAllByGioHangChiTiet(GioHangChiTiet gioHangChiTiet);

}
