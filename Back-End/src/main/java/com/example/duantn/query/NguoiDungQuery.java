package com.example.duantn.query;

public class NguoiDungQuery {
    public static final String GET_ALL_NGUOI_DUNG_BY_ID_VAI_TRO= "select * from nguoi_dung nd where nd.id_vai_tro = 2";
    public static final String GET_TIM_KIEM_SDT_KH =
            "SELECT id_nguoi_dung, ten_nguoi_dung, sdt " +
                    "FROM [nguoi_dung] " +
                    "WHERE sdt LIKE ? " +
                    "AND id_vai_tro = 2";
}
