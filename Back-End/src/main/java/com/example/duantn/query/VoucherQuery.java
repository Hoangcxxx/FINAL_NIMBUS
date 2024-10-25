package com.example.duantn.query;

public class VoucherQuery {
    public static final String GET_VOUCHER = "SELECT \n" +
            "    v.id_voucher,\n" +
            "    v.ma_voucher,\n" +
            "    lv.ten_loai_voucher,\n" +
            "    v.gia_tri_giam_gia,\n" +
            "    v.so_luong,\n" +
            "    v.ngay_bat_dau,\n" +
            "    v.ngay_ket_thuc,\n" +
            "    v.ngay_tao,\n" +
            "    v.ngay_cap_nhat,\n" +
            "    v.trang_thai\n" +
            "FROM \n" +
            "    voucher v\n" +
            "JOIN \n" +
            "    loai_voucher lv ON v.id_loai_voucher = lv.Id_loai_voucher";


}
