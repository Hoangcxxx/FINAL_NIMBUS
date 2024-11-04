package com.example.duantn.query;

public class MauSacQuery {
    public static final String GET_ALL_MAU_SAC = "SELECT \n" +
            "    ms.ten_mau_sac AS \"Tên màu sắc\",\n" +
            "    ms.mo_ta AS \"Mô tả\",\n" +
            "    ms.ngay_tao AS \"Ngày tạo\",\n" +
            "    ms.ngay_cap_nhat AS \"Ngày cập nhật\"\n" +
            "FROM \n" +
            "    mau_sac ms;";
}
