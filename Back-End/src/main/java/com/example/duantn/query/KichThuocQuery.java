package com.example.duantn.query;

public class KichThuocQuery {
    public static final String GET_ALL_KICH_THUOC = "SELECT \n" +
            "    ms.ten_kich_thuoc AS \"tenKichThuoc\",\n" +
            "    ms.mo_ta AS \"moTa\",\n" +
            "    ms.ngay_tao AS \"ngayTao\",\n" +
            "    ms.ngay_cap_nhat AS \"ngayCapNhat\"\n" +
            "FROM \n" +
            "    kich_thuoc ms\n" +
            "ORDER BY \n" +
            "    ms.ngay_cap_nhat DESC;"; // Sắp xếp theo ngày cập nhật
}
