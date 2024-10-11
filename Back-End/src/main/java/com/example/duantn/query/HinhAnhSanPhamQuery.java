package com.example.duantn.query;

public class HinhAnhSanPhamQuery {
    public static final String GET_HINH_ANA_SAN_PHAM_BY_ID_SAN_PHAM = "SELECT \n" +
            "    ha.url_anh,\n" +
            "    ha.thu_tu\n" +
            "FROM \n" +
            "    hinh_anh_san_pham ha JOIN san_pham sp ON ha.id_san_pham = sp.Id_san_pham\n" +
            "WHERE \n" +
            "    ha.id_san_pham = :idSanPham";
}
