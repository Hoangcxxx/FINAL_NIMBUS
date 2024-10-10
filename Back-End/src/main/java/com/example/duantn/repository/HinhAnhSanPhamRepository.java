package com.example.duantn.repository;

import com.example.duantn.entity.HinhAnhSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HinhAnhSanPhamRepository extends JpaRepository<HinhAnhSanPham, Integer> {

    @Query(value = "SELECT \n" +
            "    ha.url_anh,\n" +
            "    ha.thu_tu\n" +
            "FROM \n" +
            "    hinh_anh_san_pham ha join san_pham sp on ha.id_san_pham = sp.Id_san_pham\n" +
            "WHERE \n" +
            "    ha.id_san_pham = :idSanPham;", nativeQuery = true)
    List<Object[]> getHinhAnhSanPhamByIdSanPham(@Param("idSanPham") Integer idSanPham);



}
