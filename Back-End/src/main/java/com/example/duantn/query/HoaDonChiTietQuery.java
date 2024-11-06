package com.example.duantn.query;

public class HoaDonChiTietQuery {
    public static final String GET_THONG_KE = "SELECT \n" +
            "    sp.ten_san_pham,\n" +
            "    SUM(hdct.so_luong) AS so_luong_ban_ra,\n" +
            "    SUM(hdct.tong_tien) AS tong_tien\n" +
            "FROM \n" +
            "    hoa_don_chi_tiet hdct\n" +
            "JOIN \n" +
            "    san_pham_chi_tiet spct ON hdct.id_san_pham_chi_tiet = spct.Id_san_pham_chi_tiet\n" +
            "JOIN \n" +
            "    san_pham sp ON spct.id_san_pham = sp.Id_san_pham\n" +
            "JOIN \n" +
            "    hoa_don hd ON hdct.id_hoa_don = hd.Id_hoa_don\n" +
            "WHERE \n" +
            "    CAST(hd.ngay_tao AS DATE) = CAST(GETDATE() AS DATE)\n" +
            "    AND hd.id_trang_thai_hoa_don = (SELECT Id_trang_thai_hoa_don FROM trang_thai_hoa_don WHERE ten_trang_thai = 'Hoàn thành') -- Thay đổi tên trạng thái nếu cần\n" +
            "GROUP BY \n" +
            "    sp.ten_san_pham\n" +
            "ORDER BY \n" +
            "    so_luong_ban_ra DESC;";

    public static final String GET_TONG_SO_LUONG_BAN_RA ="\tSELECT \n" +
            "    SUM(hdct.so_luong) AS so_luong_san_pham\n" +
            "FROM \n" +
            "    hoa_don_chi_tiet hdct\n" +
            "JOIN \n" +
            "    hoa_don hd ON hdct.id_hoa_don = hd.Id_hoa_don\n" +
            "WHERE \n" +
            "    hd.id_trang_thai_hoa_don = (SELECT Id_trang_thai_hoa_don FROM trang_thai_hoa_don WHERE ten_trang_thai = 'Hoàn thành'); -- Thay đổi tên trạng thái nếu cần";

    public static final String GET_SAN_PHAM_BAN_RA ="SELECT \n" +
            "    COUNT(*) AS so_san_pham_ban_ra\n" +
            "FROM (\n" +
            "    SELECT \n" +
            "        sp.id_san_pham,\n" +
            "        SUM(hdct.so_luong) AS so_luong_ban_ra\n" +
            "    FROM \n" +
            "        hoa_don_chi_tiet hdct\n" +
            "    JOIN \n" +
            "        san_pham_chi_tiet spct ON hdct.id_san_pham_chi_tiet = spct.Id_san_pham_chi_tiet\n" +
            "    JOIN \n" +
            "        san_pham sp ON spct.id_san_pham = sp.Id_san_pham\n" +
            "    JOIN \n" +
            "        hoa_don hd ON hdct.id_hoa_don = hd.Id_hoa_don\n" +
            "    WHERE \n" +
            "        hd.id_trang_thai_hoa_don = (SELECT Id_trang_thai_hoa_don FROM trang_thai_hoa_don WHERE ten_trang_thai = 'Hoàn thành') \n" +
            "    GROUP BY \n" +
            "        sp.id_san_pham\n" +
            "    HAVING \n" +
            "        COUNT(DISTINCT hdct.id_san_pham_chi_tiet) > 0  -- Chỉ lấy những sản phẩm có số lượng bán ra\n" +
            ") AS subquery;\n";
    public static final String GET_TONG_DOANH_THU ="SELECT \n" +
            "    SUM(hdct.tong_tien) AS tong_doanh_thu\n" +
            "FROM \n" +
            "    hoa_don_chi_tiet hdct\n" +
            "JOIN \n" +
            "    hoa_don hd ON hdct.id_hoa_don = hd.Id_hoa_don\n" +
            "WHERE \n" +
            "    hd.id_trang_thai_hoa_don = (SELECT Id_trang_thai_hoa_don FROM trang_thai_hoa_don WHERE ten_trang_thai = 'Hoàn thành');  -- Thay đổi tên trạng thái nếu cần\n";

}
