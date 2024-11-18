package com.example.duantn.query;

public class ChatLieuQuery {
    public static final String GET_ALL_CHAT_LIEU = "SELECT \n" +
            "    ms.ten_chat_lieu AS \"tenChatLieu\",\n" +
            "    ms.mo_ta AS \"moTa\",\n" +
            "    ms.ngay_tao AS \"ngayTao\",\n" +
            "    ms.ngay_cap_nhat AS \"ngayCapNhat\"\n" +
            "FROM \n" +
            "    chat_lieu ms\n" +
            "ORDER BY \n" +
            "    ms.ngay_cap_nhat DESC;"; // Sắp xếp theo ngày cập nhật
}
