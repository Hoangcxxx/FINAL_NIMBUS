package com.example.duantn.repository;

import com.example.duantn.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    String baseQuery = "SELECT sp.Id_san_pham AS idSanPham, sp.ten_san_pham AS tenSanPham, " +
            "spct.so_luong AS soLuong, sp.gia_ban AS giaBan, sp.mo_ta AS moTa, " +
            "dc.ten_danh_muc AS tenDanhMuc, lv.ten_loai_voucher AS tenLoaiVoucher, " +
            "hl.trang_thai AS trangThai, ms.ten_mau_sac AS mauSac, " +
            "kc.ten_kich_thuoc AS kichThuoc, cl.ten_chat_lieu AS chatLieu, " +
            "hl.url_anh AS urlAnh, hl.thu_tu AS thuTu " +
            "FROM san_pham sp " +
            "JOIN san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham " +
            "LEFT JOIN hinh_anh_san_pham hl ON spct.Id_san_pham_chi_tiet = hl.id_san_pham_chi_tiet " +
            "LEFT JOIN chat_lieu_chi_tiet clt ON spct.id_chat_lieu_chi_tiet = clt.Id_chat_lieu_tiet " +
            "LEFT JOIN chat_lieu cl ON clt.id_chat_lieu = cl.Id_chat_lieu " +
            "LEFT JOIN kich_thuoc_chi_tiet kct ON spct.id_kich_thuoc_chi_tiet = kct.Id_kich_thuoc_chi_tiet " +
            "LEFT JOIN kich_thuoc kc ON kct.id_kich_thuoc = kc.Id_kich_thuoc " +
            "LEFT JOIN mau_sac_chi_tiet mct ON spct.id_mau_sac_chi_tiet = mct.id_mau_sac_chi_tiet " +
            "LEFT JOIN mau_sac ms ON mct.id_mau_sac = ms.Id_mau_sac " +
            "LEFT JOIN danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc " +
            "LEFT JOIN loai_voucher lv ON sp.id_loai_voucher = lv.id_loai_voucher " +
            "WHERE hl.thu_tu = 1";  // Chỉ lấy hình ảnh có thứ tự là 1

    @Query(value = baseQuery, nativeQuery = true)
    List<Object[]> getAllSanPham();

    @Query(value = baseQuery + " AND sp.id_danh_muc = :idDanhMuc", nativeQuery = true)
    List<Object[]> getSanPhamByDanhMuc(@Param("idDanhMuc") int idDanhMuc);

    @Query("SELECT s FROM SanPham s WHERE s.danhMuc.idDanhMuc = :idDanhMuc")
    List<SanPham> findByDanhMucId(@Param("idDanhMuc") Integer idDanhMuc);

    @Query(value = "SELECT " +
            "    sp.Id_san_pham AS idSanPham, " +
            "    sp.ten_san_pham AS tenSanPham, " +
            "    spct.so_luong AS soLuong, " +
            "    spct.mo_ta AS mo_ta_spct, " +
            "    sp.gia_ban AS giaBan, " +
            "    dc.ten_danh_muc AS tenDanhMuc, " +
            "    lv.ten_loai_voucher AS tenLoaiVoucher, " +
            "    hl.trang_thai AS trangThai, " +
            "    ms.ten_mau_sac AS tenMauSac, " +
            "    kc.ten_kich_thuoc AS tenKichThuoc, " +
            "    cl.ten_chat_lieu AS tenChatLieu, " +
            "    hl.url_anh AS urlAnh, " +
            "    hl.thu_tu AS thuTu " +
            "FROM san_pham sp " +
            "JOIN san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham " +
            "LEFT JOIN hinh_anh_san_pham hl ON spct.Id_san_pham_chi_tiet = hl.id_san_pham_chi_tiet " +
            "LEFT JOIN chat_lieu_chi_tiet clt ON spct.id_chat_lieu_chi_tiet = clt.Id_chat_lieu_tiet " +
            "LEFT JOIN chat_lieu cl ON clt.id_chat_lieu = cl.Id_chat_lieu " +
            "LEFT JOIN kich_thuoc_chi_tiet kct ON spct.id_kich_thuoc_chi_tiet = kct.Id_kich_thuoc_chi_tiet " +
            "LEFT JOIN kich_thuoc kc ON kct.id_kich_thuoc = kc.Id_kich_thuoc " +
            "LEFT JOIN mau_sac_chi_tiet mct ON spct.id_mau_sac_chi_tiet = mct.id_mau_sac_chi_tiet " +
            "LEFT JOIN mau_sac ms ON mct.id_mau_sac = ms.Id_mau_sac " +
            "LEFT JOIN danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc " +
            "LEFT JOIN loai_voucher lv ON sp.id_loai_voucher = lv.id_loai_voucher " +
            "WHERE " +
            "    sp.Id_san_pham = :idSanPham", nativeQuery = true)
    List<Object[]> getSanPhamById(@Param("idSanPham") String idSanPham);

}
