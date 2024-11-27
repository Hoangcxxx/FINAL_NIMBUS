package com.example.duantn.query;

public class HoaDonQuery {

    public static final String GET_ALL_HOA_DON = "WITH LatestStatus AS (\n" +
            "    SELECT \n" +
            "        h.Id_hoa_don, \n" +
            "        h.ma_hoa_don, \n" +
            "        u.ten_nguoi_dung,\n" +
            "        u.sdt,\n" +
            "        h.thanh_tien, \n" +
            "        h.loai, \n" +
            "        t.ngay_tao,\n" +
            "        l.ten_loai_trang_thai,\n" +
            "        ROW_NUMBER() OVER (PARTITION BY h.Id_hoa_don ORDER BY t.id_loai_trang_thai DESC) AS rn\n" +
            "    FROM \n" +
            "        hoa_don h\n" +
            "    JOIN \n" +
            "        trang_thai_hoa_don t ON h.Id_hoa_don = t.id_hoa_don\n" +
            "    JOIN \n" +
            "        loai_trang_thai l ON l.id_loai_trang_thai = t.id_loai_trang_thai\n" +
            "    JOIN \n" +
            "        nguoi_dung u ON h.id_nguoi_dung = u.id_nguoi_dung\n" +
            ")\n" +
            "SELECT \n" +
            "    ls.Id_hoa_don, \n" +
            "    ls.ma_hoa_don, \n" +
            "    ls.ten_nguoi_dung,\n" +
            "    ls.sdt, \n" +
            "    ls.thanh_tien, \n" +
            "    ls.loai, \n" +
            "    ls.ngay_tao,\n" +
            "    ls.ten_loai_trang_thai\n" +
            "FROM \n" +
            "    LatestStatus ls\n" +
            "WHERE \n" +
            "    ls.rn = 1\n" +
            "ORDER BY \n" +
            "    ls.Id_hoa_don;";
    public static final String GET_TRANG_THAI_HOA_DON_BY_ID_HOA_DON = "SELECT \n" +
            "    h.ma_hoa_don, \n" +
            "    tthd.mo_ta,\n" +
            "    ltth.id_loai_trang_thai,\n" +
            "    ltth.ten_loai_trang_thai,\n" +
            "    tthd.ngay_tao,\n" +
            "    tthd.ngay_cap_nhat\n" +
            "FROM \n" +
            "    hoa_don h\n" +
            "JOIN \n" +
            "    trang_thai_hoa_don tthd ON h.Id_hoa_don = tthd.id_hoa_don\n" +
            "JOIN \n" +
            "    loai_trang_thai ltth ON tthd.id_loai_trang_thai = ltth.Id_loai_trang_thai\n" +
            "where h.Id_hoa_don = :idHoaDon";
}
