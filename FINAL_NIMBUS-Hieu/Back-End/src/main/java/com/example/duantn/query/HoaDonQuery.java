package com.example.duantn.query;

public class HoaDonQuery {

    public static final String GET_HOADON_BY_MA_QUERY  = "SELECT h.id_hoa_don, h.ma_hoa_don, h.ten_nguoi_nhan, h.phi_ship, "
            + "h.dia_chi, h.sdt_nguoi_nhan, d.tinh, d.huyen, d.xa, u.email "
            + "FROM hoa_don h JOIN dia_chi_van_chuyen d ON h.id_dia_chi_van_chuyen = d.id_dia_chi_van_chuyen "
            + "JOIN nguoi_dung u ON h.id_nguoi_dung = u.id_nguoi_dung "
            + "WHERE h.trang_thai = 1 AND h.ma_hoa_don = ?";

}
