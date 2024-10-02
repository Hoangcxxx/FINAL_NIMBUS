package com.example.duantn.repository;

import com.example.duantn.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    @Query(value = "SELECT " +
            "    sp.ten_san_pham AS tenSanPham, " +
            "    sct.so_luong AS soLuong, " +
            "    dm.ten_danh_muc AS tenDanhMuc, " +
            "    lv.ten_loai_voucher AS tenLoaiVoucher, " +
            "    sct.trang_thai AS trangThai, " +
            "    msc.ten_mau_sac AS tenMauSac, " +   // Màu sắc
            "    kt.ten_kich_thuoc AS tenKichThuoc, " + // Kích thước
            "    cl.ten_chat_lieu AS tenChatLieu " + // Chất liệu
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
            "    chat_lieu cl ON clt.id_chat_lieu = cl.Id_chat_lieu",
            nativeQuery = true)
    List<Object[]> getAllSanPham();


}
