package com.example.duantn.query;

public class ThongKeQuery {
    public static final String QUERY_THONG_KE_SAN_PHAM =
            "SELECT sp.ten_san_pham, SUM(ct.so_luong) AS so_luong_da_ban, SUM(ct.gia_ban * ct.so_luong) AS tong_doanh_thu " +
                    "FROM chi_tiet_don_hang ct " +
                    "JOIN san_pham sp ON ct.id_san_pham = sp.id " +
                    "GROUP BY sp.ten_san_pham " +
                    "ORDER BY tong_doanh_thu DESC;";
}

