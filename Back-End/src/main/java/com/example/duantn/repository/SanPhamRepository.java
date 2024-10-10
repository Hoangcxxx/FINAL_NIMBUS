package com.example.duantn.repository;

import com.example.duantn.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    String baseQuery = "SELECT \n" +
            "    sp.Id_san_pham AS idSanPham, \n" +
            "    sp.ten_san_pham AS tenSanPham, \n" +
            "    sp.trang_thai AS trangThai, \n" +
            "    AVG(sp.gia_ban) AS giaBan,       -- Giá bán trung bình\n" +
            "    MAX(sp.mo_ta) AS moTa,           -- Mô tả sản phẩm\n" +
            "    dc.ten_danh_muc AS tenDanhMuc, \n" +
            "    MAX(hl.url_anh) AS urlAnh,       -- Lấy URL ảnh\n" +
            "    MAX(hl.thu_tu) AS thuTu           -- Lấy thứ tự ảnh tối đa\n" +
            "FROM \n" +
            "    san_pham sp \n" +
            "JOIN \n" +
            "    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham \n" +
            "LEFT JOIN \n" +
            "    hinh_anh_san_pham hl ON spct.Id_san_pham = hl.id_san_pham \n" +
            "LEFT JOIN \n" +
            "    danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc \n" +
            "WHERE \n" +
            "    hl.thu_tu = 1 \n" +
            "GROUP BY \n" +
            "    sp.Id_san_pham, \n" +
            "    sp.ten_san_pham, \n" +
            "    sp.trang_thai,  -- Đảm bảo trường này có trong GROUP BY\n" +
            "    dc.ten_danh_muc\n" +
            "ORDER BY \n" +
            "    sp.Id_san_pham ASC;  -- Sắp xếp theo idSanPham từ nhỏ đến lớn\n";  // Chỉ lấy hình ảnh có thứ tự là 1

    @Query(value = baseQuery, nativeQuery = true)
    List<Object[]> getAllSanPham();
    @Query(value = "SELECT \n" +
            "    sp.Id_san_pham AS idSanPham, \n" +
            "    sp.ten_san_pham AS tenSanPham, \n" +
            "    sp.trang_thai AS trangThai, \n" +
            "    AVG(sp.gia_ban) AS giaBan,       -- Giá bán trung bình\n" +
            "    MAX(sp.mo_ta) AS moTa,           -- Mô tả sản phẩm\n" +
            "    dc.ten_danh_muc AS tenDanhMuc, \n" +
            "    MAX(hl.url_anh) AS urlAnh,       -- Lấy URL ảnh\n" +
            "    MAX(hl.thu_tu) AS thuTu           -- Lấy thứ tự ảnh tối đa\n" +
            "FROM \n" +
            "    san_pham sp \n" +
            "JOIN \n" +
            "    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham \n" +
            "LEFT JOIN \n" +
            "    hinh_anh_san_pham hl ON spct.Id_san_pham = hl.id_san_pham \n" +
            "LEFT JOIN \n" +
            "    danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc \n" +
            "WHERE \n" +
            "    hl.thu_tu = 1 and dc.Id_danh_muc = :idDanhMuc\n" +
            "GROUP BY \n" +
            "    sp.Id_san_pham, \n" +
            "    sp.ten_san_pham, \n" +
            "    sp.trang_thai,  -- Đảm bảo trường này có trong GROUP BY\n" +
            "    dc.ten_danh_muc", nativeQuery = true)
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


    @Query(value = """
                SELECT 
                    sp.Id_san_pham,
                    sp.ten_san_pham,
                    sp.gia_ban,
                    SUM(spct.so_luong) AS tong_so_luong,
                    sp.mo_ta,
                    MAX(ms.ten_mau_sac) AS mau_sac,
                    cl.ten_chat_lieu,
                    STRING_AGG(kc.ten_kich_thuoc, ', ') AS danh_sach_kich_thuoc
                FROM 
                    san_pham sp
                JOIN 
                    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham
                LEFT JOIN 
                    chat_lieu_chi_tiet clt ON spct.id_chat_lieu_chi_tiet = clt.Id_chat_lieu_tiet
                LEFT JOIN 
                    chat_lieu cl ON clt.id_chat_lieu = cl.Id_chat_lieu
                LEFT JOIN 
                    kich_thuoc_chi_tiet kct ON spct.id_kich_thuoc_chi_tiet = kct.id_kich_thuoc_chi_tiet
                LEFT JOIN 
                    kich_thuoc kc ON kct.id_kich_thuoc = kc.Id_kich_thuoc
                LEFT JOIN 
                    mau_sac_chi_tiet mct ON spct.id_mau_sac_chi_tiet = mct.id_mau_sac_chi_tiet
                LEFT JOIN 
                    mau_sac ms ON mct.id_mau_sac = ms.Id_mau_sac
                WHERE 
                    sp.Id_san_pham = :idSanPham AND ms.Id_mau_sac = :idMauSac
                GROUP BY 
                    sp.Id_san_pham,
                    sp.ten_san_pham,
                    sp.gia_ban,
                    sp.mo_ta,
                    cl.ten_chat_lieu
            """, nativeQuery = true)
    List<Object[]> findKichThuocBySanPhamAndMauSac(Integer idSanPham, Integer idMauSac);


}
