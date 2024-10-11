package com.example.duantn.query;

public class ChatLieuQuery {
    public static final String GET_ALL_CHAT_LIEU = "SELECT \n" +
            "    ms.ten_chat_lieu AS \"Tên chất liệu\",\n" +
            "    ms.mo_ta AS \"Mô tả\",\n" +
            "    ms.ngay_tao AS \"Ngày tạo\",\n" +
            "    ms.ngay_cap_nhat AS \"Ngày cập nhật\"\n" +
            "FROM \n" +
            "    chat_lieu ms;";
}
