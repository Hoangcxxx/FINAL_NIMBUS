package com.example.duantn.repository;

import com.example.duantn.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {

    @Query(value = "SELECT sp.id_san_pham, sp.ten_san_pham, sp.gia_ban, sp.mo_ta " +
            "FROM san_pham sp WHERE sp.Id_san_pham = :idSanPhamCT", nativeQuery = true)
    List<Object[]> getSanPhamById(@Param("idSanPhamCT") Integer idSanPhamCT);

    @Query(value = "SELECT mct.id_mau_sac_chi_tiet, ms.ten_mau_sac AS mau_sac " +
            "FROM san_pham sp " +
            "JOIN san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham " +
            "JOIN mau_sac_chi_tiet mct ON spct.id_mau_sac_chi_tiet = mct.id_mau_sac_chi_tiet " +
            "JOIN mau_sac ms ON mct.id_mau_sac = ms.Id_mau_sac " +
            "WHERE sp.Id_san_pham = :idSanPhamCT GROUP BY sp.Id_san_pham, mct.id_mau_sac_chi_tiet, ms.ten_mau_sac", nativeQuery = true)
    List<Object[]> getMauSacByIdSanPham(@Param("idSanPhamCT") Integer idSanPhamCT);

    @Query(value = "SELECT kct.id_kich_thuoc_chi_tiet, kt.ten_kich_thuoc AS kich_thuoc " +
            "FROM san_pham sp " +
            "JOIN san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham " +
            "JOIN kich_thuoc_chi_tiet kct ON spct.id_kich_thuoc_chi_tiet = kct.id_kich_thuoc_chi_tiet " +
            "JOIN kich_thuoc kt ON kct.id_kich_thuoc = kt.Id_kich_thuoc " +
            "WHERE sp.Id_san_pham = :idSanPhamCT GROUP BY sp.Id_san_pham, kct.id_kich_thuoc_chi_tiet, kt.ten_kich_thuoc", nativeQuery = true)
    List<Object[]> getKichThuocByIdSanPham(@Param("idSanPhamCT") Integer idSanPhamCT);

    @Query(value = "SELECT clct.Id_chat_lieu_tiet, cl.ten_chat_lieu AS chat_lieu " +
            "FROM san_pham sp " +
            "JOIN san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham " +
            "JOIN chat_lieu_chi_tiet clct ON spct.id_chat_lieu_chi_tiet = clct.Id_chat_lieu_tiet " +
            "JOIN chat_lieu cl ON clct.id_chat_lieu = cl.Id_chat_lieu " +
            "WHERE sp.Id_san_pham = :idSanPhamCT GROUP BY sp.Id_san_pham, clct.Id_chat_lieu_tiet, cl.ten_chat_lieu", nativeQuery = true)
    List<Object[]> getChatLieuByIdSanPham(@Param("idSanPhamCT") Integer idSanPhamCT);
}
