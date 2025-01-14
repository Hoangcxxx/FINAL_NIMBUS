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
            "    SUM(\n" +
            "        CASE \n" +
            "            WHEN hd.loai = 1 THEN hd.thanh_tien - 22000  -- Nếu loại hóa đơn là 1 thì trừ 22000\n" +
            "            ELSE hd.thanh_tien  -- Nếu loại hóa đơn là 0 thì không trừ gì\n" +
            "        END\n" +
            "    ) AS tong_doanh_thu  -- Tính tổng doanh thu sau khi trừ hoặc không trừ\n" +
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
            "    SUM(\n" +
            "        CASE \n" +
            "            WHEN hd.loai = 1 THEN hd.thanh_tien - 22000  -- Trừ 22000 nếu loại hóa đơn = 1\n" +
            "            ELSE hd.thanh_tien  -- Không trừ nếu loại hóa đơn = 0\n" +
            "        END\n" +
            "    ) AS tong_doanh_thu_hom_nay\n" +
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
            "    AND CAST(hd.ngay_tao AS DATE) = CAST(GETDATE() AS DATE)  -- Lọc hóa đơn tạo trong ngày hôm nay";
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
    public static final String GET_ALL_SO_LUONG_LOAI_TRANG_THAI_HOA_DON = "SELECT \n" +
            "    lt.id_loai_trang_thai,\n" +
            "    lt.ten_loai_trang_thai,\n" +
            "    COUNT(DISTINCT hd.Id_hoa_don) AS so_hoa_don\n" +
            "FROM \n" +
            "    loai_trang_thai lt\n" +
            "LEFT JOIN \n" +
            "    trang_thai_hoa_don tshd ON lt.Id_loai_trang_thai = tshd.id_loai_trang_thai\n" +
            "LEFT JOIN \n" +
            "    hoa_don hd ON tshd.id_hoa_don = hd.Id_hoa_don AND tshd.ngay_tao = (\n" +
            "        SELECT MAX(ngay_tao)\n" +
            "        FROM trang_thai_hoa_don\n" +
            "        WHERE id_hoa_don = hd.Id_hoa_don\n" +
            "    )\n" +
            "WHERE \n" +
            "    lt.id_loai_trang_thai BETWEEN 2 AND 8 AND lt.id_loai_trang_thai != 3 \n" +
            "GROUP BY \n" +
            "    lt.id_loai_trang_thai, lt.ten_loai_trang_thai\n" +
            "ORDER BY \n" +
            "    lt.id_loai_trang_thai;";

}