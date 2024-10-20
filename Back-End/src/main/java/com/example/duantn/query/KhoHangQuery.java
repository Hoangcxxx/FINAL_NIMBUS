package com.example.duantn.query;

public class KhoHangQuery {

    private static final String SELECT_FIELDS =
            "spct.Id_san_pham_chi_tiet, " +
                    "sp.Id_san_pham, " +
                    "sp.ten_san_pham, " +
                    "spct.so_luong, " + // Sum the quantities for grouping
                    "dm.id_danh_muc, " +
                    "dm.ten_danh_muc, " +
                    "cl.id_chat_lieu, " +
                    "cl.ten_chat_lieu, " +
                    "ms.id_mau_sac, " +
                    "ms.ten_mau_sac, " +
                    "kt.id_kich_thuoc, " +
                    "kt.ten_kich_thuoc, " +
                    "spct.trang_thai";

    private static final String FROM_CLAUSE =
            "FROM san_pham AS sp " +
                    "JOIN san_pham_chi_tiet AS spct ON sp.Id_san_pham = spct.id_san_pham " +
                    "JOIN chat_lieu_chi_tiet AS clt ON spct.id_chat_lieu_chi_tiet = clt.Id_chat_lieu_tiet " +
                    "JOIN chat_lieu AS cl ON clt.id_chat_lieu = cl.Id_chat_lieu " +
                    "JOIN mau_sac_chi_tiet AS msc ON spct.id_mau_sac_chi_tiet = msc.Id_mau_sac_chi_tiet " +
                    "JOIN mau_sac AS ms ON msc.id_mau_sac = ms.Id_mau_sac " +
                    "JOIN kich_thuoc_chi_tiet AS kct ON spct.id_kich_thuoc_chi_tiet = kct.Id_kich_thuoc_chi_tiet " +
                    "JOIN kich_thuoc AS kt ON kct.id_kich_thuoc = kt.Id_kich_thuoc " +
                    "JOIN danh_muc AS dm ON sp.id_danh_muc = dm.Id_danh_muc ";

    public static final String GET_SAN_PHAM_CT_BY_ALL =
            "SELECT " + SELECT_FIELDS + " " + FROM_CLAUSE +
                    "WHERE (:idSanPham IS NULL OR sp.Id_san_pham = :idSanPham) " +
                    "AND (:idChatLieu IS NULL OR cl.Id_chat_lieu = :idChatLieu) " +
                    "AND (:idMauSac IS NULL OR ms.Id_mau_sac = :idMauSac) " +
                    "AND (:idKichThuoc IS NULL OR kt.Id_kich_thuoc = :idKichThuoc) " +
                    "GROUP BY spct.Id_san_pham_chi_tiet, sp.Id_san_pham, sp.ten_san_pham, spct.so_luong," + // Group by the necessary fields
                    "dm.id_danh_muc, dm.ten_danh_muc, " +
                    "cl.id_chat_lieu, cl.ten_chat_lieu, " +
                    "ms.id_mau_sac, ms.ten_mau_sac, " +
                    "kt.id_kich_thuoc, kt.ten_kich_thuoc, " +
                    "spct.trang_thai";
    public static final String GET_SAN_PHAM_CT_BY_DANH_MUC =
            "SELECT \n" +
                    "    sp.id_san_pham, \n" +
                    "    sp.ten_san_pham \n" +
                    "FROM \n" +
                    "    danh_muc dm\n" +
                    "LEFT JOIN \n" +
                    "    san_pham sp ON dm.Id_danh_muc = sp.id_danh_muc\n" +
                    "WHERE \n" +
                    "    dm.id_danh_muc = :idDanhMuc\n" +
                    "ORDER BY \n" +
                    "    sp.ngay_tao DESC; -- Sắp xếp theo ngày tạo mới nhất\n";
    public static final String GET_CHAT_LIEU_BY_ID_SAN_PHAM_CT =
            "SELECT \n" +
                    "    cl.id_chat_lieu,\n" +
                    "    cl.ten_chat_lieu\n" +
                    "FROM \n" +
                    "    san_pham_chi_tiet spct\n" +
                    "JOIN \n" +
                    "    chat_lieu_chi_tiet clct ON spct.id_chat_lieu_chi_tiet = clct.Id_chat_lieu_tiet\n" +
                    "JOIN \n" +
                    "    chat_lieu cl ON clct.id_chat_lieu = cl.Id_chat_lieu\n" +
                    "WHERE \n" +
                    "    spct.id_san_pham = :idSanPhamCT -- Thay 1 bằng giá trị ID sản phẩm cần truy vấn\n" +
                    "GROUP BY \n" +
                    "     cl.id_chat_lieu,\n" +
                    "    cl.ten_chat_lieu";


    public static final String GET_MAU_SAC_BY_ID_SAN_PHAM_CT_AND_CHAT_LIEU =
            "SELECT \n" +
                    "    ms.id_mau_sac,\n" +
                    "    ms.ten_mau_sac\n" +
                    "FROM \n" +
                    "    san_pham_chi_tiet spct\n" +
                    "JOIN \n" +
                    "    chat_lieu_chi_tiet clct ON spct.id_chat_lieu_chi_tiet = clct.Id_chat_lieu_tiet\n" +
                    "JOIN \n" +
                    "    chat_lieu cl ON clct.id_chat_lieu = cl.Id_chat_lieu\n" +
                    "JOIN \n" +
                    "    mau_sac_chi_tiet mscct ON spct.id_mau_sac_chi_tiet = mscct.Id_mau_sac_chi_tiet\n" +
                    "JOIN \n" +
                    "    mau_sac ms ON mscct.id_mau_sac = ms.Id_mau_sac\n" +
                    "WHERE \n" +
                    "    spct.id_san_pham = :idSanPhamCT -- Thay đổi ID sản phẩm tại đây\n" +
                    "    AND cl.Id_chat_lieu = :idChatLieu -- Thay đổi ID chất liệu tại đây\n" +
                    "GROUP BY \n" +
                    "    ms.id_mau_sac, ms.ten_mau_sac";


    public static final String GET_KICH_THUOC_BY_ID_SAN_PHAM_CT_AND_CHAT_LIEU_AND_MAU_SAC =
            "SELECT \n" +
                    "    kc.id_kich_thuoc,\n" +
                    "    kc.ten_kich_thuoc\n" +
                    "FROM \n" +
                    "    san_pham_chi_tiet spct\n" +
                    "    LEFT JOIN \n" +
                    "    kich_thuoc_chi_tiet kcct ON spct.id_kich_thuoc_chi_tiet = kcct.Id_kich_thuoc_chi_tiet\n" +
                    "    LEFT JOIN \n" +
                    "    kich_thuoc kc ON kc.id_kich_thuoc = kcct.Id_kich_thuoc\n" +
                    "    JOIN \n" +
                    "    chat_lieu_chi_tiet clct ON spct.id_chat_lieu_chi_tiet = clct.Id_chat_lieu_tiet\n" +
                    "    JOIN \n" +
                    "    chat_lieu cl ON clct.id_chat_lieu = cl.Id_chat_lieu\n" +
                    "    JOIN \n" +
                    "    mau_sac_chi_tiet mscct ON spct.id_mau_sac_chi_tiet = mscct.Id_mau_sac_chi_tiet\n" +
                    "    JOIN \n" +
                    "    mau_sac ms ON mscct.id_mau_sac = ms.Id_mau_sac\n" +
                    "WHERE \n" +
                    "    spct.id_san_pham = :idSanPhamCT-- Thay đổi ID sản phẩm tại đây\n" +
                    "    AND cl.Id_chat_lieu = :idChatLieu -- Thay đổi ID chất liệu tại đây\n" +
                    "    AND ms.Id_mau_sac = :idMauSac -- Thay đổi ID màu sắc tại đây\n" +
                    "GROUP BY \n" +
                    "    kc.id_kich_thuoc,\n" +
                    "    kc.ten_kich_thuoc";
}
