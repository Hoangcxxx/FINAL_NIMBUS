package com.example.duantn.query;

public class SanPhamChiTietQuery {
    public static final String GET_SAN_PHAM_BY_ID = "SELECT \n" +
            "    sp.id_san_pham, \n" +
            "    sp.ten_san_pham, \n" +
            "    sp.gia_ban,\n" +
            "    sp.mo_ta\n" +
            "FROM \n" +
            "    san_pham sp\n" +
            "WHERE \n" +
            "    sp.id_san_pham = :idSanPhamCT\n";

    public static final String GET_MAU_SAC_BY_ID_SAN_PHAM = "SELECT mct.id_mau_sac_chi_tiet, ms.ten_mau_sac AS mau_sac " +
            "FROM san_pham sp " +
            "JOIN san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham " +
            "JOIN mau_sac_chi_tiet mct ON spct.id_mau_sac_chi_tiet = mct.id_mau_sac_chi_tiet " +
            "JOIN mau_sac ms ON mct.id_mau_sac = ms.Id_mau_sac " +
            "WHERE sp.Id_san_pham = :idSanPhamCT GROUP BY sp.Id_san_pham, mct.id_mau_sac_chi_tiet, ms.ten_mau_sac";

    public static final String GET_KICH_THUOC_BY_ID_SAN_PHAM = "SELECT kct.id_kich_thuoc_chi_tiet, kt.ten_kich_thuoc AS kich_thuoc " +
            "FROM san_pham sp " +
            "JOIN san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham " +
            "JOIN kich_thuoc_chi_tiet kct ON spct.id_kich_thuoc_chi_tiet = kct.id_kich_thuoc_chi_tiet " +
            "JOIN kich_thuoc kt ON kct.id_kich_thuoc = kt.Id_kich_thuoc " +
            "WHERE sp.Id_san_pham = :idSanPhamCT GROUP BY sp.Id_san_pham, kct.id_kich_thuoc_chi_tiet, kt.ten_kich_thuoc";

    public static final String GET_CHAT_LIEU_BY_ID_SAN_PHAM = "SELECT clct.Id_chat_lieu_tiet, cl.ten_chat_lieu AS chat_lieu " +
            "FROM san_pham sp " +
            "JOIN san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham " +
            "JOIN chat_lieu_chi_tiet clct ON spct.id_chat_lieu_chi_tiet = clct.Id_chat_lieu_tiet " +
            "JOIN chat_lieu cl ON clct.id_chat_lieu = cl.Id_chat_lieu " +
            "WHERE sp.Id_san_pham = :idSanPhamCT GROUP BY sp.Id_san_pham, clct.Id_chat_lieu_tiet, cl.ten_chat_lieu";


    public static final String GET_SAN_PHAM_CT_BY_ID_SAN_PHAM =
            "SELECT \n" +
                    "    spc.id_san_pham_chi_tiet,\n" +
                    "    sp.id_san_pham,\n" +
                    "    sp.ten_san_pham,\n" +
                    "    spc.so_luong,\n" +
                    "    cl.ten_chat_lieu,\n" +
                    "    ms.ten_mau_sac,\n" +
                    "    kc.ten_kich_thuoc,\n" +
                    "    sp.mo_ta\n" +
                    "FROM \n" +
                    "    san_pham sp\n" +
                    "JOIN \n" +
                    "    san_pham_chi_tiet spc ON sp.Id_san_pham = spc.id_san_pham\n" +
                    "JOIN \n" +
                    "    mau_sac_chi_tiet msc ON spc.id_mau_sac_chi_tiet = msc.Id_mau_sac_chi_tiet\n" +
                    "JOIN \n" +
                    "    mau_sac ms ON msc.id_mau_sac = ms.Id_mau_sac\n" +
                    "JOIN \n" +
                    "    chat_lieu_chi_tiet clc ON spc.id_chat_lieu_chi_tiet = clc.Id_chat_lieu_tiet\n" +
                    "JOIN \n" +
                    "    chat_lieu cl ON clc.id_chat_lieu = cl.Id_chat_lieu\n" +
                    "JOIN \n" +
                    "    kich_thuoc_chi_tiet kcc ON spc.id_kich_thuoc_chi_tiet = kcc.Id_kich_thuoc_chi_tiet\n" +
                    "JOIN \n" +
                    "    kich_thuoc kc ON kcc.id_kich_thuoc = kc.Id_kich_thuoc\n" +
                    "WHERE \n" +
                    "    sp.Id_san_pham = :idSanPhamCT -- Replace @productId with the actual product ID\n" +
                    "    AND spc.so_luong IS NULL";

    public static final String GET_SAN_PHAM_CT_BY_ID = "SELECT \n" +
            "    spct.id_san_pham_chi_tiet,\n" +
            "    spct.so_luong,\n" +
            "    ms.ten_mau_sac,\n" +
            "    kt.ten_kich_thuoc,\n" +
            "    cl.ten_chat_lieu,\n" +
            "    sp.ten_san_pham,\n" +
            "    sp.gia_ban,\n" +
            "    COALESCE(ggsp.gia_khuyen_mai, sp.gia_ban) AS gia_ban_sau_khuyen_mai,\n" +
            "    spct.trang_thai,\n" +
            "    spct.ngay_tao,\n" +
            "    spct.ngay_cap_nhat,\n" +
            "    ha.url_anh,\n" +
            "    dm.ten_danh_muc\n" +
            "FROM \n" +
            "    san_pham_chi_tiet spct\n" +
            "JOIN \n" +
            "    mau_sac_chi_tiet msc ON spct.id_mau_sac_chi_tiet = msc.id_mau_sac_chi_tiet\n" +
            "JOIN \n" +
            "    mau_sac ms ON msc.id_mau_sac = ms.id_mau_sac\n" +
            "JOIN \n" +
            "    kich_thuoc_chi_tiet ktc ON spct.id_kich_thuoc_chi_tiet = ktc.id_kich_thuoc_chi_tiet\n" +
            "JOIN \n" +
            "    kich_thuoc kt ON ktc.id_kich_thuoc = kt.id_kich_thuoc\n" +
            "JOIN \n" +
            "    chat_lieu_chi_tiet clc ON spct.id_chat_lieu_chi_tiet = clc.id_chat_lieu_chi_tiet\n" +
            "JOIN \n" +
            "    chat_lieu cl ON clc.id_chat_lieu = cl.id_chat_lieu\n" +
            "JOIN \n" +
            "    san_pham sp ON spct.id_san_pham = sp.id_san_pham\n" +
            "JOIN \n" +
            "    danh_muc dm ON sp.id_danh_muc = dm.id_danh_muc\n" +
            "LEFT JOIN \n" +
            "    giam_gia_san_pham ggsp ON sp.id_san_pham = ggsp.id_san_pham\n" +
            "LEFT JOIN \n" +
            "    dot_giam_gia dgg ON ggsp.id_dot_giam_gia = dgg.id_dot_giam_gia \n" +
            "    AND dgg.id_trang_thai_giam_gia = 1\n" +
            "    AND (dgg.ngay_ket_thuc IS NULL OR dgg.ngay_ket_thuc >= GETDATE())\n" +
            "JOIN \n" +
            "    hinh_anh_san_pham ha ON sp.id_san_pham = ha.id_san_pham\n" +
            "    AND ha.trang_thai = 1\n" +
            "    AND ha.thu_tu = 1\n" +
            "WHERE \n" +
            "    spct.trang_thai = 1\n" +
            "ORDER BY \n" +
            "    ha.thu_tu ASC;";

}
