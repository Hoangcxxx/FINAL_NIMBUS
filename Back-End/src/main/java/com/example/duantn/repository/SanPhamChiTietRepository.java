package com.example.duantn.repository;

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


    @Query(value = SanPhamChiTietQuery.GET_MAU_SAC_BY_ID_SAN_PHAM, nativeQuery = true)
    List<Object[]> getMauSacByIdSanPham(@Param("idSanPhamCT") Integer idSanPhamCT);

    @Query(value = SanPhamChiTietQuery.GET_KICH_THUOC_BY_ID_SAN_PHAM, nativeQuery = true)
    List<Object[]> getKichThuocByIdSanPham(@Param("idSanPhamCT") Integer idSanPhamCT);

    @Query(value = SanPhamChiTietQuery.GET_CHAT_LIEU_BY_ID_SAN_PHAM, nativeQuery = true)
    List<Object[]> getChatLieuByIdSanPham(@Param("idSanPhamCT") Integer idSanPhamCT);

    @Query(value = SanPhamChiTietQuery.GET_SAN_PHAM_CT_BY_ID_SAN_PHAM, nativeQuery = true)
    List<Object[]> getAllSanPhamByIdSanPham(@Param("idSanPham") Integer idSanPham);
    @Query(value = SanPhamChiTietQuery.GET_SAN_PHAM_CT_BY_ID_SAN_PHAM_AND_SL_LON_HON_0, nativeQuery = true)
    List<Object[]> getSanPhamCTByIdSanPhamLonHon0(@Param("idSanPhamCT") Integer idSanPhamCT);
    @Modifying
    @Transactional
    @Query("DELETE FROM SanPhamChiTiet s WHERE s.idSanPhamChiTiet IN :idSanPhamCTs")
    void deleteByIds(@Param("idSanPhamCTs") List<Integer> idSanPhamCTs);

    void deleteSanPhamChiTietByIdSanPhamChiTiet(Integer idSanPhamCT);

    @Modifying
    @Transactional
    @Query(value = "UPDATE san_pham_chi_tiet SET so_luong = :soLuong WHERE Id_san_pham_chi_tiet = :idSanPhamCT", nativeQuery = true)
    void updateSoLuongSanPhamCT(@Param("soLuong") Integer soLuong, @Param("idSanPhamCT") Integer idSanPhamCT);


    @Query(value = "SELECT so_luong FROM san_pham_chi_tiet WHERE Id_san_pham_chi_tiet = :idSanPhamCT", nativeQuery = true)
    Integer findQuantityById(@Param("idSanPhamCT") Integer idSanPhamCT);

    @Query(value = "SELECT * FROM san_pham_chi_tiet " + "WHERE id_mau_sac_chi_tiet = :idMauSac "
            + "AND id_kich_thuoc_chi_tiet = :idKichThuoc " + "AND id_chat_lieu_chi_tiet = :idChatLieu "
            + "AND id_san_pham = :idSanPham", nativeQuery = true)
    Optional<SanPhamChiTiet> findByAttributes(@Param("idMauSac") Integer idMauSac,
                                              @Param("idKichThuoc") Integer idKichThuoc, @Param("idChatLieu") Integer idChatLieu,
                                              @Param("idSanPham") Integer idSanPham);

    // Tìm kiếm sản phẩm chi tiết theo id sản phẩm, màu sắc, chất liệu và kích thước
    @Query("SELECT s FROM SanPhamChiTiet s " +
            "WHERE s.sanPham.idSanPham = :idSanPham " +
            "AND (:idChatLieu IS NULL OR s.chatLieuChiTiet.idChatLieuChiTiet = :idChatLieu) " +
            "AND (:idMauSac IS NULL OR s.mauSacChiTiet.idMauSacChiTiet = :idMauSac) " +
            "AND (:idKichThuoc IS NULL OR s.kichThuocChiTiet.idKichThuocChiTiet = :idKichThuoc)")
    List<SanPhamChiTiet> findByMauSacChatLieuKichThuocSanPham(
            @Param("idSanPham") Integer idSanPham,
            @Param("idChatLieu") Integer idChatLieu,
            @Param("idMauSac") Integer idMauSac,
            @Param("idKichThuoc") Integer idKichThuoc
    );
}
