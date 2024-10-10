package com.example.duantn.repository;

import com.example.duantn.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {

    @Query(value = "SELECT \n" +
            "    sp.id_san_pham,\n" +
            "\tsp.ten_san_pham,\n" +
            "\tsp.gia_ban,\n" +
            "\tsp.mo_ta\n" +
            "FROM \n" +
            "    san_pham sp \n" +
            "WHERE \n" +
            "    sp.Id_san_pham = :idSanPhamCT;", nativeQuery = true)
    List<Object[]> getSanPhamById(@Param("idSanPhamCT") Integer idSanPhamCT);



    @Query(value = "SELECT \n" +
            "    sp.Id_san_pham,\n" +
            "    ms.ten_mau_sac\n" +
            "FROM \n" +
            "    san_pham sp\n" +
            "JOIN \n" +
            "    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham\n" +
            "LEFT JOIN \n" +
            "    mau_sac_chi_tiet mct ON spct.id_mau_sac_chi_tiet = mct.id_mau_sac_chi_tiet\n" +
            "LEFT JOIN \n" +
            "    mau_sac ms ON mct.id_mau_sac = ms.Id_mau_sac\n" +
            "WHERE \n" +
            "    sp.Id_san_pham = :idSanPhamCT\n" +
            "GROUP BY \n" +
            "    sp.Id_san_pham, ms.ten_mau_sac;", nativeQuery = true)
    List<Object[]> getMauSacByIdSanPham(@Param("idSanPhamCT") Integer idSanPhamCT);

}
