package com.example.duantn.query;

public class DotGiamGiaQuery {
    public static final String GET_SAN_PHAM_CHUA_GIAM_GIA = "SELECT \n" +
            "    sp.Id_san_pham,\n" +
            "    sp.ten_san_pham,\n" +
            "    sp.gia_ban,\n" +
            "    sp.mo_ta,\n" +
            "    dc.ten_danh_muc,\n" +
            "    ha.url_anh\n" +
            "FROM san_pham sp\n" +
            "LEFT JOIN giam_gia_san_pham ggs ON sp.Id_san_pham = ggs.id_san_pham\n" +
            "LEFT JOIN danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc \n" +
            "LEFT JOIN hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham\n" +
            "WHERE \n" +
            "    ha.thu_tu = 1 AND -- Chọn hình ảnh đầu tiên\n" +
            "    ggs.Id_giam_gia_san_pham IS NULL;";
    public static final String GET_SAN_PHAM_CHUA_GIAM_GIA_BY_DANH_MUC = "SELECT \n" +
            "    sp.Id_san_pham,\n" +
            "    sp.ten_san_pham,\n" +
            "    sp.gia_ban,\n" +
            "    sp.mo_ta,\n" +
            "    dc.ten_danh_muc,\n" +
            "    ha.url_anh\n" +
            "FROM san_pham sp\n" +
            "LEFT JOIN giam_gia_san_pham ggs ON sp.Id_san_pham = ggs.id_san_pham\n" +
            "LEFT JOIN danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc \n" +
            "LEFT JOIN hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham\n" +
            "WHERE \n" +
            "    ha.thu_tu = 1 AND sp.id_danh_muc = :idDanhMuc AND-- Chọn hình ảnh đầu tiên\n" +
            "    ggs.Id_giam_gia_san_pham IS NULL;";
}
