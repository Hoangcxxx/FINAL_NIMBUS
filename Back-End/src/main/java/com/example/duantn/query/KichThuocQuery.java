package com.example.duantn.query;

public class KichThuocQuery {
    public static final String GET_ALL_KICH_THUOC = "SELECT \n" +
            "    ms.ten_kich_thuoc AS \"Tên kích thước\",\n" +
            "    ms.mo_ta AS \"Mô tả\",\n" +
            "    ms.ngay_tao AS \"Ngày tạo\",\n" +
            "    ms.ngay_cap_nhat AS \"Ngày cập nhật\"\n" +
            "FROM \n" +
            "    kich_thuoc ms;";
}
