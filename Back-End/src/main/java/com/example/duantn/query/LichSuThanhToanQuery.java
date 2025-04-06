package com.example.duantn.query;

public class LichSuThanhToanQuery {
    public static final String GET_LICH_SU_THANH_TOAN_BY_ID_HOA_DON = "SELECT \n" +
            "    lstt.id_lich_su_thanh_toan,\n" +
            "    lstt.so_tien_thanh_toan,\n" +
            "    lstt.ngay_giao_dich,\n" +
            "    lstt.trang_thai_thanh_toan,\n" +
            "    lstt.mo_ta,\n" +
            "    CASE \n" +
            "        WHEN lstt.id_nhan_vien IS NOT NULL THEN nv.ten_nguoi_dung\n" +
            "        ELSE 'N/A'\n" +
            "    END AS ten_nhan_vien\n" +
            "FROM \n" +
            "    lich_su_thanh_toan lstt\n" +
            "LEFT JOIN \n" +
            "    nguoi_dung nv ON lstt.id_nhan_vien = nv.Id_nguoi_dung\n" +
            "WHERE \n" +
            "    lstt.id_hoa_don = :idHoaDon;";
}
