package com.example.duantn.repository;

import com.example.duantn.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    @Query(value = "SELECT " +
            "    sp.id_san_pham AS idSanPham, " +
            "    sp.ten_san_pham AS tenSanPham, " +
            "    sct.so_luong AS soLuong, " +
            "    sp.gia_ban AS giaBan, " +
            "    sp.mo_ta AS moTa, " +
            "    dm.ten_danh_muc AS tenDanhMuc, " +
            "    lv.ten_loai_voucher AS tenLoaiVoucher, " +
            "    sct.trang_thai AS trangThai, " +
            "    msc.ten_mau_sac AS tenMauSac, " +   // Màu sắc
            "    kt.ten_kich_thuoc AS tenKichThuoc, " + // Kích thước
            "    cl.ten_chat_lieu AS tenChatLieu, " + // Chất liệu
            "    hasp.url_anh AS urlAnh " + // Thêm hình ảnh sản phẩm
            "FROM " +
            "    san_pham sp " +
            "JOIN " +
            "    danh_muc dm ON sp.id_danh_muc = dm.Id_danh_muc " +
            "LEFT JOIN " +
            "    loai_voucher lv ON sp.id_loai_voucher = lv.Id_loai_voucher " +
            "LEFT JOIN " +
            "    san_pham_chi_tiet sct ON sp.Id_san_pham = sct.id_san_pham " +
            "LEFT JOIN " +
            "    mau_sac_chi_tiet mst ON sct.id_mau_sac_chi_tiet = mst.Id_mau_sac_chi_tiet " +
            "LEFT JOIN " +
            "    mau_sac msc ON mst.id_mau_sac = msc.Id_mau_sac " +
            "LEFT JOIN " +
            "    kich_thuoc_chi_tiet kct ON sct.id_kich_thuoc_chi_tiet = kct.Id_kich_thuoc_chi_tiet " +
            "LEFT JOIN " +
            "    kich_thuoc kt ON kct.id_kich_thuoc = kt.Id_kich_thuoc " +
            "LEFT JOIN " +
            "    chat_lieu_chi_tiet clt ON sct.id_chat_lieu_chi_tiet = clt.Id_chat_lieu_tiet " +
            "LEFT JOIN " +
            "    chat_lieu cl ON clt.id_chat_lieu = cl.Id_chat_lieu " +
            "LEFT JOIN " + // Thêm join với bảng hình ảnh
            "    hinh_anh_san_pham hasp ON sp.Id_san_pham = hasp.id_san_pham",
            nativeQuery = true)
    List<Object[]> getAllSanPham();


    @Query(value = "SELECT " +
            "    sp.ten_san_pham AS tenSanPham, " +
            "    sct.so_luong AS soLuong, " +
            "    sp.gia_ban AS giaBan, " +
            "    sp.mo_ta AS moTa, " +
            "    dm.ten_danh_muc AS tenDanhMuc, " +
            "    lv.ten_loai_voucher AS tenLoaiVoucher, " +
            "    sct.trang_thai AS trangThai, " +
            "    msc.ten_mau_sac AS tenMauSac, " +   // Màu sắc
            "    kt.ten_kich_thuoc AS tenKichThuoc, " + // Kích thước
            "    cl.ten_chat_lieu AS tenChatLieu, " + // Chất liệu
            "    hasp.url_anh AS urlAnh " + // Thêm hình ảnh sản phẩm
            "FROM " +
            "    san_pham sp " +
            "JOIN " +
            "    danh_muc dm ON sp.id_danh_muc = dm.Id_danh_muc " +
            "LEFT JOIN " +
            "    loai_voucher lv ON sp.id_loai_voucher = lv.Id_loai_voucher " +
            "LEFT JOIN " +
            "    san_pham_chi_tiet sct ON sp.Id_san_pham = sct.id_san_pham " +
            "LEFT JOIN " +
            "    mau_sac_chi_tiet mst ON sct.id_mau_sac_chi_tiet = mst.Id_mau_sac_chi_tiet " +
            "LEFT JOIN " +
            "    mau_sac msc ON mst.id_mau_sac = msc.Id_mau_sac " +
            "LEFT JOIN " +
            "    kich_thuoc_chi_tiet kct ON sct.id_kich_thuoc_chi_tiet = kct.Id_kich_thuoc_chi_tiet " +
            "LEFT JOIN " +
            "    kich_thuoc kt ON kct.id_kich_thuoc = kt.Id_kich_thuoc " +
            "LEFT JOIN " +
            "    chat_lieu_chi_tiet clt ON sct.id_chat_lieu_chi_tiet = clt.Id_chat_lieu_tiet " +
            "LEFT JOIN " +
            "    chat_lieu cl ON clt.id_chat_lieu = cl.Id_chat_lieu " +
            "LEFT JOIN " + // Thêm join với bảng hình ảnh
            "    hinh_anh_san_pham hasp ON sp.Id_san_pham = hasp.id_san_pham " +
            "WHERE sp.id_danh_muc = :idDanhMuc", // Thêm điều kiện WHERE
            nativeQuery = true)
    List<Object[]> getSanPhamByDanhMuc(@Param("idDanhMuc") int idDanhMuc);


    @Query("SELECT s FROM SanPham s WHERE s.danhMuc.idDanhMuc = :idDanhMuc")
    List<SanPham> findByDanhMucId(@Param("idDanhMuc") Integer idDanhMuc);

    @Query(value = "SELECT " +
            "    sp.id_san_pham AS idSanPham, " +
            "    sp.ten_san_pham AS tenSanPham, " +
            "    sct.so_luong AS soLuong, " +
            "    sp.gia_ban AS giaBan, " +
            "    sp.mo_ta AS moTa, " +
            "    dm.ten_danh_muc AS tenDanhMuc, " +
            "    lv.ten_loai_voucher AS tenLoaiVoucher, " +
            "    sct.trang_thai AS trangThai, " +
            "    msc.ten_mau_sac AS tenMauSac, " +
            "    kt.ten_kich_thuoc AS tenKichThuoc, " +
            "    cl.ten_chat_lieu AS tenChatLieu, " +
            "    hasp.url_anh AS urlAnh " +
            "FROM " +
            "    san_pham sp " +
            "JOIN " +
            "    danh_muc dm ON sp.id_danh_muc = dm.Id_danh_muc " +
            "LEFT JOIN " +
            "    loai_voucher lv ON sp.id_loai_voucher = lv.Id_loai_voucher " +
            "LEFT JOIN " +
            "    san_pham_chi_tiet sct ON sp.Id_san_pham = sct.id_san_pham " +
            "LEFT JOIN " +
            "    mau_sac_chi_tiet mst ON sct.id_mau_sac_chi_tiet = mst.Id_mau_sac_chi_tiet " +
            "LEFT JOIN " +
            "    mau_sac msc ON mst.id_mau_sac = msc.Id_mau_sac " +
            "LEFT JOIN " +
            "    kich_thuoc_chi_tiet kct ON sct.id_kich_thuoc_chi_tiet = kct.Id_kich_thuoc_chi_tiet " +
            "LEFT JOIN " +
            "    kich_thuoc kt ON kct.id_kich_thuoc = kt.Id_kich_thuoc " +
            "LEFT JOIN " +
            "    chat_lieu_chi_tiet clt ON sct.id_chat_lieu_chi_tiet = clt.Id_chat_lieu_tiet " +
            "LEFT JOIN " +
            "    chat_lieu cl ON clt.id_chat_lieu = cl.Id_chat_lieu " +
            "LEFT JOIN " +
            "    hinh_anh_san_pham hasp ON sp.Id_san_pham = hasp.id_san_pham " +
            "WHERE sp.id_san_pham = :idSanPham", // Thêm điều kiện WHERE để lọc theo ma_san_pham
            nativeQuery = true)
    List<Object[]> getSanPhamById(@Param("idSanPham") String idSanPham);
}
