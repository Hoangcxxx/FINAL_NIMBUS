package com.example.duantn.query;

public class DoiTraQuery {
    public static final String GET_SAN_PHAM_DOI_TRA_BY_ID_HOA_DON = "SELECT \n" +
            "    sp.ten_san_pham,\n" +
            "    cl.ten_chat_lieu,\n" +
            "    ms.ten_mau_sac ,\n" +
            "    kt.ten_kich_thuoc ,\n" +
            "    dt.so_luong,\n" +
            "    dt.tong_tien,\n" +
            "    dt.ly_do,\n" +
            "    dt.trang_thai,\n" +
            "    dt.ngay_cap_nhat,\n" +
            "    dt.ngay_tao \n" +
            "FROM doi_tra dt\n" +
            "JOIN san_pham_chi_tiet spct ON dt.id_san_pham_chi_tiet = spct.Id_san_pham_chi_tiet\n" +
            "JOIN san_pham sp ON spct.id_san_pham = sp.Id_san_pham\n" +
            "JOIN kich_thuoc_chi_tiet ktct ON spct.id_kich_thuoc_chi_tiet = ktct.Id_kich_thuoc_chi_tiet\n" +
            "JOIN kich_thuoc kt ON kt.id_kich_thuoc = ktct.Id_kich_thuoc\n" +
            "JOIN mau_sac_chi_tiet mstct ON spct.id_mau_sac_chi_tiet = mstct.Id_mau_sac_chi_tiet\n" +
            "JOIN mau_sac ms ON ms.id_mau_sac = mstct.Id_mau_sac\n" +
            "JOIN chat_lieu_chi_tiet clct ON spct.id_chat_lieu_chi_tiet = clct.Id_chat_lieu_chi_tiet\n" +
            "JOIN chat_lieu cl ON cl.id_chat_lieu = clct.Id_chat_lieu\n" +
            "WHERE dt.id_hoa_don = :idHoaDon;";
}
