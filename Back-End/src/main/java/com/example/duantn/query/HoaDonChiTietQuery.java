package com.example.duantn.query;

public class HoaDonChiTietQuery {
    public static final String GET_THONG_KE = "SELECT TOP 6\n" +
            "    sp.ten_san_pham,\n" +
            "    sp.gia_ban,\n" +
            "    SUM(hdct.so_luong) AS so_luong_ban_ra,\n" +
            "    ha.url_anh,\n" +
            "\tha.thu_tu\n" +
            "FROM \n" +
            "    hoa_don_chi_tiet hdct\n" +
            "JOIN \n" +
            "    san_pham_chi_tiet spct ON hdct.id_san_pham_chi_tiet = spct.Id_san_pham_chi_tiet\n" +
            "JOIN \n" +
            "    san_pham sp ON spct.id_san_pham = sp.Id_san_pham\n" +
            "JOIN \n" +
            "    hoa_don hd ON hdct.id_hoa_don = hd.Id_hoa_don\n" +
            "JOIN\n" +
            "    trang_thai_hoa_don tthd ON hd.id_hoa_don = tthd.id_hoa_don\n" +
            "JOIN\n" +
            "    loai_trang_thai ltt ON tthd.id_loai_trang_thai = ltt.Id_loai_trang_thai\n" +
            "LEFT JOIN \n" +
            "   hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham\n" +
            "WHERE \n" +
            "    ltt.ten_loai_trang_thai = 'Hoàn thành' and  ha.thu_tu = 1  -- Trạng thái 'Hoàn thành'\n" +
            "GROUP BY \n" +
            "    sp.ten_san_pham,\n" +
            "    sp.gia_ban,\n" +
            "    ha.url_anh,\n" +
            "\tha.thu_tu\n" +
            "ORDER BY \n" +
            "    so_luong_ban_ra DESC;";

    public static final String GET_TONG_SO_LUONG_BAN_RA ="SELECT \n" +
            "    SUM(hd.thanh_tien) AS tong_doanh_thu\n" +
            "FROM \n" +
            "    hoa_don_chi_tiet hdct\n" +
            "JOIN \n" +
            "    hoa_don hd ON hdct.id_hoa_don = hd.Id_hoa_don\n" +
            "JOIN\n" +
            "    trang_thai_hoa_don tthd ON hd.id_hoa_don = tthd.id_hoa_don\n" +
            "JOIN\n" +
            "    loai_trang_thai ltt ON tthd.id_loai_trang_thai = ltt.Id_loai_trang_thai\n" +
            "WHERE \n" +
            "    ltt.ten_loai_trang_thai = 'Hoàn thành'  -- Trạng thái 'Hoàn thành'\n" +
            "    AND CAST(hd.ngay_tao AS DATE) BETWEEN CAST(DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1) AS DATE) \n" +
            "                                        AND CAST(GETDATE() AS DATE)  -- Lọc trong tháng này";
    public static final String GET_TONG_SO_LUONG_BAN_RA_HOM_NAY ="SELECT \n" +
            "    SUM(hd.thanh_tien) AS tong_doanh_thu_hom_nay\n" +
            "FROM \n" +
            "    hoa_don_chi_tiet hdct\n" +
            "JOIN \n" +
            "    hoa_don hd ON hdct.id_hoa_don = hd.Id_hoa_don\n" +
            "JOIN\n" +
            "    trang_thai_hoa_don tthd ON hd.id_hoa_don = tthd.id_hoa_don\n" +
            "JOIN\n" +
            "    loai_trang_thai ltt ON tthd.id_loai_trang_thai = ltt.Id_loai_trang_thai\n" +
            "WHERE \n" +
            "    ltt.ten_loai_trang_thai = 'Hoàn thành'  -- Trạng thái 'Hoàn thành'\n" +
            "    AND CAST(hd.ngay_tao AS DATE) = CAST(GETDATE() AS DATE)  -- Lọc hóa đơn tạo hôm nay";
    public static final String GET_TONG_HOA_DON_THANG_NAY ="SELECT \n" +
            "    COUNT(DISTINCT hd.Id_hoa_don) AS so_hoa_don_hoan_thanh  -- Số lượng hóa đơn hoàn thành\n" +
            "FROM \n" +
            "    hoa_don hd\n" +
            "JOIN \n" +
            "    hoa_don_chi_tiet hdct ON hd.Id_hoa_don = hdct.id_hoa_don\n" +
            "JOIN\n" +
            "    trang_thai_hoa_don tthd ON hd.id_hoa_don = tthd.id_hoa_don\n" +
            "JOIN\n" +
            "    loai_trang_thai ltt ON tthd.id_loai_trang_thai = ltt.Id_loai_trang_thai\n" +
            "WHERE \n" +
            "    ltt.ten_loai_trang_thai = 'Hoàn thành'  -- Trạng thái 'Hoàn thành'\n" +
            "    AND CAST(hd.ngay_tao AS DATE) BETWEEN CAST(DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1) AS DATE) \n" +
            "                                        AND CAST(GETDATE() AS DATE)  -- Lọc trong tháng này";


    public static final String GET_ALL_TONG_HOA_DON_HOM_NAY = "SELECT \n" +
            "    COUNT(DISTINCT hd.Id_hoa_don) AS so_hoa_don_hoan_thanh_hom_nay,  -- Số lượng hóa đơn hoàn thành hôm nay\n" +
            "    SUM(hdct.tong_tien) AS tong_doanh_thu_hom_nay                   -- Tổng doanh thu hôm nay\n" +
            "FROM \n" +
            "    hoa_don hd\n" +
            "JOIN \n" +
            "    hoa_don_chi_tiet hdct ON hd.Id_hoa_don = hdct.id_hoa_don\n" +
            "JOIN\n" +
            "    trang_thai_hoa_don tthd ON hd.id_hoa_don = tthd.id_hoa_don\n" +
            "JOIN\n" +
            "    loai_trang_thai ltt ON tthd.id_loai_trang_thai = ltt.Id_loai_trang_thai\n" +
            "WHERE \n" +
            "    ltt.ten_loai_trang_thai = 'Hoàn thành'  -- Trạng thái 'Hoàn thành'\n" +
            "    AND CAST(hd.ngay_tao AS DATE) = CAST(GETDATE() AS DATE)  -- Lọc theo ngày hôm nay";
    public static final String GET_ALL_TONG_SAN_PHAM_TRONG_THANG = "SELECT \n" +
            "    SUM(hdct.so_luong) AS tong_so_luong_san_pham_da_ban  -- Tổng số lượng sản phẩm đã bán\n" +
            "FROM \n" +
            "    hoa_don_chi_tiet hdct\n" +
            "JOIN \n" +
            "    hoa_don hd ON hdct.id_hoa_don = hd.Id_hoa_don\n" +
            "JOIN\n" +
            "    trang_thai_hoa_don tthd ON hd.id_hoa_don = tthd.id_hoa_don\n" +
            "JOIN\n" +
            "    loai_trang_thai ltt ON tthd.id_loai_trang_thai = ltt.Id_loai_trang_thai\n" +
            "WHERE \n" +
            "    ltt.ten_loai_trang_thai = 'Hoàn thành'  -- Trạng thái 'Hoàn thành'\n" +
            "    AND CAST(hd.ngay_tao AS DATE) BETWEEN CAST(DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1) AS DATE) \n" +
            "                                        AND CAST(GETDATE() AS DATE)  -- Lọc trong tháng này";

}
