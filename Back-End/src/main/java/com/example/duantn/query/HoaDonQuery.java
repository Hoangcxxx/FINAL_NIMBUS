package com.example.duantn.query;

public class HoaDonQuery {

    public static final String GET_ALL_HOA_DON ="SELECT \n" +
            "    h.ma_hoa_don,\n" +
            "    u.ten_nguoi_dung,\n" +
            "    h.ngay_tao,\n" +
            "    h.thanh_tien,\n" +
            "    ts.ten_trang_thai\n" +
            "FROM \n" +
            "    hoa_don h\n" +
            "JOIN \n" +
            "    nguoi_dung u ON h.id_nguoi_dung = u.id_nguoi_dung\n" +
            "JOIN \n" +
            "    trang_thai_hoa_don ts ON h.id_trang_thai_hoa_don = ts.Id_trang_thai_hoa_don";
}
