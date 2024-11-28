package com.example.duantn.query;

public class GioHangQuery {
    public static final String BASE_QUERY = "\n" +
            "SELECT \n" +
            "    spct.Id_san_pham_chi_tiet,\n" +
            "    sp.id_san_pham,\n" +
            "    sp.ma_san_pham,\n" +
            "    sp.ten_san_pham,\n" +
            "    sp.gia_ban,\n" +
            "    spct.so_luong,\n" +
            "    spct.trang_thai,\n" +
            "    sp.ten_san_pham,  -- Tên sản phẩm\n" +
            "    ktt.ten_kich_thuoc,  -- Kích thước\n" +
            "    mst.ten_mau_sac,  -- Màu sắc\n" +
            "    clt.ten_chat_lieu,  -- Chất liệu\n" +
            "    ghct.so_luong,\n" +
            "    ghct.don_gia,\n" +
            "    ghct.thanh_tien,\n" +
            "\tha.url_anh,\n" +
            "    ha.thu_tu,\n" +
            "\tdgg.ten_dot_giam_gia,\n" +
            "    ggsp.gia_khuyen_mai,\n" +
            "    dgg.gia_tri_giam_gia,\n" +
            "    dgg.kieu_giam_gia,\n" +
            "    dgg.ngay_bat_dau,\n" +
            "    dgg.ngay_ket_thuc\n" +
            "FROM \n" +
            "    nguoi_dung nd\n" +
            "JOIN \n" +
            "    gio_hang gh ON gh.id_nguoi_dung = nd.Id_nguoi_dung\n" +
            "JOIN \n" +
            "    gio_hang_chi_tiet ghct ON ghct.id_gio_hang = gh.Id_gio_hang\n" +
            "JOIN \n" +
            "    san_pham_chi_tiet spct ON spct.Id_san_pham_chi_tiet = ghct.id_san_pham_chi_tiet\n" +
            "JOIN \n" +
            "    san_pham sp ON sp.Id_san_pham = spct.id_san_pham\n" +
            "LEFT JOIN \n" +
            "    kich_thuoc_chi_tiet ktt_ct ON spct.id_kich_thuoc_chi_tiet = ktt_ct.Id_kich_thuoc_chi_tiet\n" +
            "LEFT JOIN \n" +
            "    kich_thuoc ktt ON ktt_ct.id_kich_thuoc = ktt.Id_kich_thuoc\n" +
            "LEFT JOIN \n" +
            "    mau_sac_chi_tiet mst_ct ON spct.id_mau_sac_chi_tiet = mst_ct.Id_mau_sac_chi_tiet\n" +
            "LEFT JOIN \n" +
            "    mau_sac mst ON mst_ct.id_mau_sac = mst.Id_mau_sac\n" +
            "LEFT JOIN \n" +
            "    chat_lieu_chi_tiet clt_ct ON spct.id_chat_lieu_chi_tiet = clt_ct.Id_chat_lieu_chi_tiet\n" +
            "LEFT JOIN \n" +
            "    chat_lieu clt ON clt_ct.id_chat_lieu = clt.Id_chat_lieu\n" +
            "LEFT JOIN\n" +
            "    hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham\n" +
            "LEFT JOIN\n" +
            "    giam_gia_san_pham ggsp ON sp.Id_san_pham = ggsp.id_san_pham\n" +
            "LEFT JOIN \n" +
            "    dot_giam_gia dgg ON ggsp.id_dot_giam_gia = dgg.Id_dot_giam_gia\n" +
            "WHERE \n" +
            "    nd.Id_nguoi_dung = :idNguoiDung and ha.thu_tu = 1;";
}
