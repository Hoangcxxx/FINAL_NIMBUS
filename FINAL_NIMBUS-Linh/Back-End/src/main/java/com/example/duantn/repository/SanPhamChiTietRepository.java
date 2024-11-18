package com.example.duantn.repository;

import com.example.duantn.dto.SanPhamChiTietDTO;
import com.example.duantn.entity.SanPhamChiTiet;
import com.example.duantn.query.SanPhamChiTietQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    List<Object[]> getSanPhamCTByIdSanPham(@Param("idSanPhamCT") Integer idSanPhamCT);
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

    @Query("SELECT new com.example.duantn.dto.SanPhamChiTietDTO(" +
            "spct.idSanPhamChiTiet, " +
            "spct.soLuong, " +
            "sp.danhMuc.tenDanhMuc, " +
            "m.mauSac.tenMauSac, " +
            "kkt.tenKichThuoc, " +
            "c.chatLieu.tenChatLieu, " +
            "sp.tenSanPham, " +
            "sp.giaBan, " +
            "spct.trangThai, " +
            "spct.ngayTao, " +
            "spct.ngayCapNhat, " +
            "ha.urlAnh) " +
            "FROM SanPhamChiTiet spct " +
            "JOIN spct.mauSacChiTiet m " +
            "JOIN spct.kichThuocChiTiet k " +
            "JOIN k.kichThuoc kkt " +
            "JOIN spct.chatLieuChiTiet c " +
            "JOIN spct.sanPham sp " +
            "JOIN sp.hinhAnhSanPham ha " +
            "WHERE ha.trangThai = true " + // Lọc hình ảnh có trạng thái active
            "AND ha.thuTu = 1 " + // Chỉ lấy hình ảnh đầu tiên
            "ORDER BY ha.thuTu ASC" // Nếu cần sắp xếp theo thứ tự
    )
    List<SanPhamChiTietDTO> findAllSanPhamChiTietDetails();
    @Modifying
    @Transactional
    @Query("UPDATE SanPhamChiTiet spct SET spct.soLuong = spct.soLuong - :soLuong WHERE spct.idSanPhamChiTiet = :idSanPhamChiTiet AND spct.soLuong >= :soLuong")
    int updateSoLuong(@Param("idSanPhamChiTiet") Integer idSanPhamChiTiet, @Param("soLuong") Integer soLuong);

}
