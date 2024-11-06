package com.example.duantn.repository;

import com.example.duantn.entity.DanhMuc;
import com.example.duantn.entity.KhoHang;
import com.example.duantn.query.KhoHangQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface KhoHangRepository extends JpaRepository<KhoHang, Integer> {

    @Query(value = KhoHangQuery.GET_SAN_PHAM_CT_BY_ALL, nativeQuery = true)
    List<Object[]> getSanPhamCTByAll(
            @Param("idSanPham") Integer idSanPham,
            @Param("idChatLieu") Integer idChatLieu,
            @Param("idMauSac") Integer idMauSac,
            @Param("idKichThuoc") Integer idKichThuoc
    );
    // Phương thức để lấy tất cả danh mục
    @Query(value = KhoHangQuery.GET_SAN_PHAM_CT_BY_DANH_MUC, nativeQuery = true)
    List<Object[]> getSanPhamCTByDanhMuc(
            @Param("idDanhMuc") Integer idDanhMuc
    );

    @Query(value = KhoHangQuery.GET_CHAT_LIEU_BY_ID_SAN_PHAM_CT, nativeQuery = true)
    List<Object[]> getChatLieuByIdSanPhamCT(
            @Param("idSanPhamCT") Integer idSanPhamCT
    );

    @Query(value = KhoHangQuery.GET_MAU_SAC_BY_ID_SAN_PHAM_CT_AND_CHAT_LIEU, nativeQuery = true)
    List<Object[]> getMauSacByIdSanPhamCTAndIdChatLieu(
            @Param("idSanPhamCT") Integer idSanPhamCT,
            @Param("idChatLieu") Integer idChatLieu
    );
    @Query(value = KhoHangQuery.GET_KICH_THUOC_BY_ID_SAN_PHAM_CT_AND_CHAT_LIEU_AND_MAU_SAC, nativeQuery = true)
    List<Object[]> getKichThuocByIdSanPhamCTAndIdChatLieuAndMauSac(
            @Param("idSanPhamCT") Integer idSanPhamCT,
            @Param("idChatLieu") Integer idChatLieu,
            @Param("idMauSac") Integer idMauSac
    );

    @Modifying
    @Transactional
    @Query(value = "UPDATE san_pham_chi_tiet SET so_luong = so_luong + :soLuongThem WHERE id_san_pham_chi_tiet = :idSanPhamChiTiet", nativeQuery = true)
    void updateStock(
            @Param("idSanPhamChiTiet") Integer idSanPhamChiTiet,
            @Param("soLuongThem") Integer soLuongThem
    );

    @Query(value = "SELECT so_luong FROM san_pham_chi_tiet WHERE id_san_pham_chi_tiet = :idSanPhamChiTiet", nativeQuery = true)
    Integer findQuantityById(@Param("idSanPhamChiTiet") Integer idSanPhamChiTiet);

    @Modifying
    @Query(value = "UPDATE san_pham_chi_tiet " +
            "SET Trang_thai = CASE WHEN Trang_thai = 1 THEN 0 ELSE 1 END, " +
            "ngay_cap_nhat = GETDATE() " +
            "WHERE Id_san_pham_chi_tiet = :idSanPhamCT", nativeQuery = true)
    void updateStatusByIdSanPhamCT(@Param("idSanPhamCT") Integer idSanPhamCT);
}
