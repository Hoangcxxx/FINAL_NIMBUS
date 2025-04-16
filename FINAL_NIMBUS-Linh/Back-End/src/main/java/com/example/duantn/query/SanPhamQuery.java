package com.example.duantn.query;

public class SanPhamQuery {
    public static final String BASE_QUERY = "SELECT \n" +
            "    sp.Id_san_pham,\n" +
            "    sp.ma_san_pham,\n" +
            "    sp.ten_san_pham,\n" +
            "    sp.gia_ban,\n" +
            "    sp.mo_ta ,\n" +
            "    dm.ten_danh_muc,\n" +
            "    dgg.ten_dot_giam_gia,\n" +
            "    ggsp.gia_khuyen_mai,\n" +
            "    dgg.gia_tri_giam_gia,\n" +
            "    dgg.kieu_giam_gia,\n" +
            "    dgg.ngay_bat_dau,\n" +
            "    dgg.ngay_ket_thuc,\n" +
            "\tha.url_anh,\n" +
            "\tha.thu_tu\n" +
            "FROM \n" +
            "    san_pham sp\n" +
            "LEFT JOIN \n" +
            "    danh_muc dm ON sp.id_danh_muc = dm.Id_danh_muc\n" +
            "LEFT JOIN \n" +
            "    giam_gia_san_pham ggsp ON sp.Id_san_pham = ggsp.id_san_pham\n" +
            "LEFT JOIN \n" +
            "    dot_giam_gia dgg ON ggsp.id_dot_giam_gia = dgg.Id_dot_giam_gia\n" +
            "LEFT JOIN \n" +
            "    hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham\n" +
            "WHERE \n" +
            "    sp.trang_thai = 1 and ha.thu_tu = 1\n" +
            "    AND (dgg.ngay_ket_thuc >= GETDATE() OR dgg.ngay_ket_thuc IS NULL)\n" +
            "ORDER BY \n" +
            "    CASE \n" +
            "        WHEN ggsp.id_san_pham IS NOT NULL THEN 0 -- Sản phẩm có khuyến mãi sẽ xuất hiện đầu tiên\n" +
            "        ELSE 1\n" +
            "    END,\n" +
            "    sp.ngay_cap_nhat DESC, -- Sắp xếp theo tên sản phẩm (A-Z)\n" +
            "    sp.gia_ban ASC; -- Sắp xếp theo giá (tăng dần)";


    public static final String GET_SAN_PHAM_BY_DANH_MUC = "\n" +
            "SELECT \n" +
            "    sp.Id_san_pham,\n" +
            "    sp.ma_san_pham,\n" +
            "    sp.ten_san_pham,\n" +
            "    sp.gia_ban,\n" +
            "    sp.mo_ta,\n" +
            "    dm.ten_danh_muc,\n" +
            "    dgg.ten_dot_giam_gia,\n" +
            "    ggsp.gia_khuyen_mai,\n" +
            "    dgg.gia_tri_giam_gia,\n" +
            "    dgg.kieu_giam_gia,\n" +
            "    dgg.ngay_bat_dau,\n" +
            "    dgg.ngay_ket_thuc,\n" +
            "\tha.url_anh,\n" +
            "\tha.thu_tu\n" +
            "FROM \n" +
            "    san_pham sp\n" +
            "LEFT JOIN \n" +
            "    danh_muc dm ON sp.id_danh_muc = dm.Id_danh_muc\n" +
            "LEFT JOIN \n" +
            "    giam_gia_san_pham ggsp ON sp.Id_san_pham = ggsp.id_san_pham\n" +
            "LEFT JOIN \n" +
            "    dot_giam_gia dgg ON ggsp.id_dot_giam_gia = dgg.Id_dot_giam_gia\n" +
            "LEFT JOIN \n" +
            "    hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham\n" +
            "WHERE \n" +
            "    sp.trang_thai = 1 and ha.thu_tu = 1\n" +
            "    AND (dgg.ngay_ket_thuc >= GETDATE() OR dgg.ngay_ket_thuc IS NULL)\n" +
            "    AND sp.id_danh_muc = :idDanhMuc -- Thêm điều kiện lọc theo id_danh_muc\n" +
            "ORDER BY \n" +
            "    CASE \n" +
            "        WHEN ggsp.id_san_pham IS NOT NULL THEN 0 -- Sản phẩm có khuyến mãi sẽ xuất hiện đầu tiên\n" +
            "        ELSE 1\n" +
            "    END,\n" +
            "    sp.ngay_cap_nhat DESC, -- Sắp xếp theo tên sản phẩm (A-Z)\n" +
            "    sp.gia_ban ASC; -- Sắp xếp theo giá (tăng dần)";
    public static final String GET_SAN_PHAM_BY_ID_DOT_GIAM_GIA = "\n" +
            "SELECT \n" +
            "    sp.Id_san_pham,\n" +
            "    sp.ma_san_pham,\n" +
            "    sp.ten_san_pham,\n" +
            "    sp.gia_ban,\n" +
            "    ggs.gia_khuyen_mai,\n" +
            "    dgg.gia_tri_giam_gia,\n" +
            "    dgg.kieu_giam_gia,\n" +
            "    dgg.ten_dot_giam_gia,\n" +
            "    dgg.ngay_bat_dau,\n" +
            "    dgg.ngay_ket_thuc,\n" +
            "    sp.mo_ta,\n" +
            "    dc.ten_danh_muc,\n" +
            "    hl.url_anh\n" +
            "FROM \n" +
            "    san_pham sp\n" +
            "JOIN \n" +
            "    giam_gia_san_pham ggs ON ggs.id_san_pham = sp.Id_san_pham\n" +
            "LEFT JOIN \n" +
            "    dot_giam_gia AS dgg ON ggs.id_dot_giam_gia = dgg.Id_dot_giam_gia\n" +
            "LEFT JOIN \n" +
            "    trang_thai_giam_gia AS tt ON dgg.id_trang_thai_giam_gia = tt.Id_trang_thai_giam_gia\n" +
            "LEFT JOIN \n" +
            "    danh_muc AS dc ON sp.id_danh_muc = dc.Id_danh_muc\n" +
            "LEFT JOIN \n" +
            "    hinh_anh_san_pham AS hl ON sp.Id_san_pham = hl.id_san_pham\n" +
            "WHERE \n" +
            "    ggs.id_dot_giam_gia = :idDotGiamGia\n" +
            "    AND tt.id_trang_thai_giam_gia = 1\n" +
            "    AND sp.trang_thai = 1 AND hl.thu_tu = 1\n" +
            "GROUP BY \n" +
            "    sp.Id_san_pham,\n" +
            "    sp.ma_san_pham,\n" +
            "    sp.ten_san_pham,\n" +
            "    sp.gia_ban,\n" +
            "    ggs.gia_khuyen_mai,\n" +
            "    dgg.gia_tri_giam_gia, \n" +
            "    dgg.kieu_giam_gia, \n" +
            "    dgg.ten_dot_giam_gia, \n" +
            "    dgg.ngay_bat_dau,\n" +
            "    dgg.ngay_ket_thuc, \n" +
            "    sp.mo_ta, \n" +
            "    hl.url_anh, \n" +
            "    dc.ten_danh_muc; \n";

    public static final String ADD_SAN_PHAM_AD =
            "INSERT INTO san_pham (ma_san_pham,ten_san_pham, gia_ban, mo_ta, id_danh_muc, ngay_tao, ngay_cap_nhat, trang_thai) " +
                    "VALUES (:maSanPham,:tenSanPham, :giaBan, :moTa, :idDanhMuc, :ngayTao, :ngayCapNhat, :trangThai); " +
                    "SELECT SCOPE_IDENTITY();";



    public static final String ADD_HINH_ANH_SAN_PHAM_AD = "INSERT INTO hinh_anh_san_pham (id_san_pham, url_anh, thu_tu, loai_hinh_anh) VALUES (:idSanPham, :urlAnh, :thuTu, :loaiHinhAnh);";


    public static final String GET_SAN_PHAM_AD = "SELECT \n" +
            "    sp.id_san_pham AS idSanPham,\n" +
            "    sp.ma_san_pham AS maSanPham,\n" +
            "    sp.ten_san_pham AS tenSanPham,\n" +
            "    sp.gia_ban AS giaBan,\n" +
            "    sp.mo_ta AS moTa,\n" +
            "    dm.ten_danh_muc AS tenDanhMuc,\n" +
            "    sp.trang_thai AS trangThai\n" +
            "FROM \n" +
            "    san_pham sp\n" +
            "JOIN \n" +
            "    danh_muc dm ON sp.id_danh_muc = dm.Id_danh_muc\n" +
            "ORDER BY \n" +
            "    sp.ngay_cap_nhat DESC;";


    public static final String GET_SAN_PHAM_GIAM_GIA = "SELECT \n" +
            "    sp.Id_san_pham,\n" +
            "    sp.ma_san_pham,\n" +
            "    sp.ten_san_pham,\n" +
            "    sp.gia_ban,\n" +
            "    ggs.gia_khuyen_mai,\n" +
            "    dgg.gia_tri_giam_gia,\n" +
            "    dgg.kieu_giam_gia,\n" +
            "    dgg.ten_dot_giam_gia,\n" +
            "    dgg.ngay_bat_dau,\n" +
            "    dgg.ngay_ket_thuc,\n" +
            "    sp.mo_ta,\n" +
            "    dc.ten_danh_muc,\n" +
            "    ha.url_anh\n" +
            "FROM \n" +
            "    san_pham AS sp\n" +
            "LEFT JOIN giam_gia_san_pham AS ggs ON sp.Id_san_pham = ggs.id_san_pham\n" +
            "LEFT JOIN dot_giam_gia AS dgg ON ggs.id_dot_giam_gia = dgg.Id_dot_giam_gia\n" +
            "LEFT JOIN trang_thai_giam_gia AS tt ON dgg.id_trang_thai_giam_gia = tt.Id_trang_thai_giam_gia\n" +
            "LEFT JOIN danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc \n" +
            "LEFT JOIN hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham\n" +
            "WHERE \n" +
            "    sp.trang_thai = 1\n" +
            "    AND ha.thu_tu = 1\n" +
            "    AND tt.id_trang_thai_giam_gia = 1\n" +
            "ORDER BY \n" +
            "    sp.ten_san_pham;";



    public static final String GET_SAN_PHAM_BAN_HANG = "SELECT \n" +
            "    sp.Id_san_pham,\n" +
            "    sp.ma_san_pham,\n" +
            "    sp.ten_san_pham,\n" +
            "    sp.gia_ban,\n" +
            "    sp.mo_ta,\n" +
            "    dm.ten_danh_muc,\n" +
            "    dgg.ten_dot_giam_gia,\n" +
            "    ggsp.gia_khuyen_mai,\n" +
            "    dgg.gia_tri_giam_gia,\n" +
            "    dgg.kieu_giam_gia,\n" +
            "    dgg.ngay_bat_dau,\n" +
            "    dgg.ngay_ket_thuc,\n" +
            "    ha.url_anh,\n" +
            "    ha.thu_tu\n" +
            "FROM \n" +
            "    san_pham sp\n" +
            "LEFT JOIN \n" +
            "    danh_muc dm ON sp.id_danh_muc = dm.Id_danh_muc\n" +
            "LEFT JOIN \n" +
            "    giam_gia_san_pham ggsp ON sp.Id_san_pham = ggsp.id_san_pham\n" +
            "LEFT JOIN \n" +
            "    dot_giam_gia dgg ON ggsp.id_dot_giam_gia = dgg.Id_dot_giam_gia\n" +
            "LEFT JOIN \n" +
            "    hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham\n" +
            "WHERE \n" +
            "    sp.trang_thai = 1 \n" +
            "    AND ha.thu_tu = 1\n" +
            "    AND (dgg.ngay_ket_thuc >= GETDATE() OR dgg.ngay_ket_thuc IS NULL)\n" +
            "ORDER BY \n" +
            "    CASE \n" +
            "        WHEN ggsp.id_san_pham IS NOT NULL THEN 0 -- Sản phẩm có khuyến mãi xuất hiện trước\n" +
            "        ELSE 1\n" +
            "    END\n" ;

    public static final String GET_SAN_PHAM_CHI_TIET =
            "SELECT \n" +
                    "    spc.id_san_pham_chi_tiet, \n" +
                    "    spc.ma_san_pham_chi_tiet, \n" +
                    "    sp.id_san_pham, \n" +
                    "    sp.ma_san_pham, \n" +
                    "    sp.ten_san_pham, \n" +
                    "    spc.so_luong, \n" +
                    "    cl.ten_chat_lieu, \n" +
                    "    ms.ten_mau_sac, \n" +
                    "    kc.ten_kich_thuoc, \n" +
                    "    sp.mo_ta, \n" +
                    "    dgg.ten_dot_giam_gia, \n" +
                    "    ggsp.gia_khuyen_mai, \n" +
                    "    dgg.gia_tri_giam_gia, \n" +
                    "    dgg.kieu_giam_gia, \n" +
                    "    dgg.ngay_bat_dau, \n" +
                    "    dgg.ngay_ket_thuc, \n" +
                    "    sp.gia_ban  -- Thêm giá bán của sản phẩm \n" +
                    "FROM \n" +
                    "    san_pham sp \n" +
                    "JOIN \n" +
                    "    san_pham_chi_tiet spc ON sp.id_san_pham = spc.id_san_pham \n" +
                    "JOIN \n" +
                    "    mau_sac_chi_tiet msc ON spc.id_mau_sac_chi_tiet = msc.id_mau_sac_chi_tiet \n" +
                    "JOIN \n" +
                    "    mau_sac ms ON msc.id_mau_sac = ms.id_mau_sac \n" +
                    "JOIN \n" +
                    "    chat_lieu_chi_tiet clc ON spc.id_chat_lieu_chi_tiet = clc.id_chat_lieu_chi_tiet \n" +
                    "JOIN \n" +
                    "    chat_lieu cl ON clc.id_chat_lieu = cl.id_chat_lieu \n" +
                    "JOIN \n" +
                    "    kich_thuoc_chi_tiet kcc ON spc.id_kich_thuoc_chi_tiet = kcc.id_kich_thuoc_chi_tiet \n" +
                    "JOIN \n" +
                    "    kich_thuoc kc ON kcc.id_kich_thuoc = kc.id_kich_thuoc \n" +
                    "LEFT JOIN \n" +
                    "    giam_gia_san_pham ggsp ON sp.id_san_pham = ggsp.id_san_pham \n" +
                    "LEFT JOIN \n" +
                    "    dot_giam_gia dgg ON ggsp.id_dot_giam_gia = dgg.id_dot_giam_gia \n" +
                    "WHERE \n" +
                    "    sp.id_san_pham = ? \n" +
                    "    AND spc.so_luong > 0;";

}
