package com.example.duantn.repository;

import com.example.duantn.DTO.HoaDonDTO;
import com.example.duantn.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> {

    // Custom query method to fetch HoaDonChiTiet by HoaDon ID
    @Query("SELECT h FROM HoaDonChiTiet h WHERE h.hoaDon.idHoaDon = :hoaDonId")
    List<HoaDonChiTiet> findByHoaDon_IdHoaDon(@Param("hoaDonId") Integer hoaDonId);

//    @Query("SELECT new com.example.duantn.DTO.HoaDonDTO(" +
//            "gh.cartId, hd.idHoaDon, hd.maHoaDon, hd.tenNguoiNhan, hd.phiShip, hd.diaChi, hd.sdtNguoiNhan, " +
//            "hd.moTa, dvc.tinh, dvc.huyen, dvc.xa, nd.email, " +
//            "dvc.idDiaChiVanChuyen, nd.idNguoiDung, " +
//            "th.idTrangThaiHoaDon, ptt.idThanhToanHoaDon, ptt.phuongThucThanhToan.tenPhuongThuc, " +
//            "hd.thanhTien, " +
//            "listSpt) " +
//            "FROM HoaDon hd " +
//            "JOIN hd.diaChiVanChuyen dvc " +
//            "JOIN hd.nguoiDung nd " +
//            "JOIN hd.voucher v " +
//            "JOIN hd.trangThaiHoaDon th " +
//            "JOIN hd.phuongThucThanhToanHoaDon ptt " +
//            "JOIN hd.gioHangChiTiets gh " +
//            "LEFT JOIN hd.hoaDonChiTiets listSpt")
//    List<HoaDonDTO> findHoaDonDetails();


}
