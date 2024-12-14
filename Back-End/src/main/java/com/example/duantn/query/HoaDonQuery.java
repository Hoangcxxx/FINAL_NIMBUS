package com.example.duantn.query;

public class HoaDonQuery {

    public static final String GET_ALL_HOA_DON = "WITH LatestStatus AS (\n" +
            "    SELECT \n" +
            "        h.Id_hoa_don, \n" +
            "        h.ma_hoa_don, \n" +
            "        u.ten_nguoi_dung,\n" +
            "        h.sdt_nguoi_nhan,\n" +
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
            "    ls.sdt_nguoi_nhan, \n" +
            "    ls.thanh_tien, \n" +
            "    ls.loai, \n" +
            "    ls.ngay_tao,\n" +
            "    ls.ten_loai_trang_thai\n" +
            "FROM \n" +
            "    LatestStatus ls\n" +
            "WHERE \n" +
            "    ls.rn = 1\n" +
            "ORDER BY \n" +
            "    ls.ngay_tao DESC,   \n" +
            "    ls.Id_hoa_don;";
    public static final String GET_TRANG_THAI_HOA_DON_BY_ID_HOA_DON = "SELECT \n" +
            "    h.ma_hoa_don, \n" +
            "    tthd.mo_ta,\n" +
            "    ltth.id_loai_trang_thai,\n" +
            "    ltth.ten_loai_trang_thai,\n" +
            "    tthd.ngay_tao,\n" +
            "    tthd.ngay_cap_nhat,\n" +
            "    nv.ten_nguoi_dung\n" +
            "FROM \n" +
            "    hoa_don h\n" +
            "JOIN \n" +
            "    trang_thai_hoa_don tthd ON h.Id_hoa_don = tthd.id_hoa_don\n" +
            "JOIN \n" +
            "    loai_trang_thai ltth ON tthd.id_loai_trang_thai = ltth.Id_loai_trang_thai\n" +
            "JOIN \n" +
            "    nguoi_dung nv ON tthd.id_nhan_vien = nv.Id_nguoi_dung\n" +
            "WHERE \n" +
            "    h.Id_hoa_don = :idHoaDon;";
    public static final String GET_VOUCHER_HOA_DON_BY_ID_HOA_DON = "SELECT \n" +
            "    v.ma_voucher,\n" +
            "    v.ten_voucher,\n" +
            "    v.gia_tri_giam_gia,\n" +
            "    v.kieu_giam_gia\n" +
            "FROM \n" +
            "    hoa_don h\n" +
            "LEFT JOIN \n" +
            "   voucher v on v.Id_voucher = h.id_voucher\n" +
            "WHERE \n" +
            "    h.Id_hoa_don = :idHoaDon;";
    public static final String GET_SAN_PHAM_CHI_TIET_HOA_DON_BY_ID_HOA_DON = "SELECT \n" +
            "    spct.Id_san_pham_chi_tiet,\n" +
            "    ghct.so_luong,\n" +
            "    spct.trang_thai,\n" +
            "    sp.ma_san_pham,\n" +
            "    sp.ten_san_pham,\n" +
            "    sp.gia_ban,\n" +
            "    cl.ten_chat_lieu,\n" +
            "    ms.ten_mau_sac,\n" +
            "    kt.ten_kich_thuoc,\n" +
            "    ggs.gia_khuyen_mai,           -- Điều chỉnh tên bảng và trường gia_khuyen_mai\n" +
            "    dgg.gia_tri_giam_gia,\n" +
            "    dgg.kieu_giam_gia,\n" +
            "    dgg.ten_dot_giam_gia,\n" +
            "    ghct.tong_tien,\n" +
            "    spct.ma_san_pham_chi_tiet\n" +
            "FROM \n" +
            "    hoa_don h\n" +
            "INNER JOIN \n" +
            "    hoa_don_chi_tiet ghct ON h.Id_hoa_don = ghct.Id_hoa_don\n" +
            "INNER JOIN \n" +
            "    san_pham_chi_tiet spct ON ghct.id_san_pham_chi_tiet = spct.Id_san_pham_chi_tiet\n" +
            "INNER JOIN \n" +
            "    san_pham sp ON spct.id_san_pham = sp.Id_san_pham\n" +
            "LEFT JOIN \n" +
            "    mau_sac_chi_tiet msc ON spct.id_mau_sac_chi_tiet = msc.Id_mau_sac_chi_tiet\n" +
            "LEFT JOIN \n" +
            "    mau_sac ms ON ms.Id_mau_sac = msc.id_mau_sac\n" +
            "LEFT JOIN \n" +
            "    kich_thuoc_chi_tiet ktc ON spct.id_kich_thuoc_chi_tiet = ktc.Id_kich_thuoc_chi_tiet\n" +
            "LEFT JOIN \n" +
            "    kich_thuoc kt ON kt.Id_kich_thuoc = ktc.id_kich_thuoc\n" +
            "LEFT JOIN \n" +
            "    chat_lieu_chi_tiet clc ON spct.id_chat_lieu_chi_tiet = clc.Id_chat_lieu_chi_tiet\n" +
            "LEFT JOIN \n" +
            "    chat_lieu cl ON cl.Id_chat_lieu = clc.id_chat_lieu\n" +
            "LEFT JOIN \n" +
            "    giam_gia_san_pham ggs ON spct.Id_san_pham_chi_tiet = ggs.Id_giam_gia_san_pham  -- Liên kết với bảng khuyến mãi (gia_khuyen_mai)\n" +
            "LEFT JOIN \n" +
            "    dot_giam_gia dgg ON ggs.id_dot_giam_gia = dgg.id_dot_giam_gia  -- Liên kết với bảng dot_giam_gia\n" +
            "WHERE \n" +
            "    h.Id_hoa_don = :idHoaDon;  -- Thay @id_hoa_don bằng ID của hóa đơn bạn muốn truy vấn";


    public static final String GET_MA_HOA_DON =
            "SELECT TOP 1 ma_hoa_don\n" +
                    "FROM hoa_don\n" +
                    "ORDER BY Id_hoa_don DESC;\n";




}
