package com.example.duantn.query;

public class DanhMucQuery {
    public static final String GET_DANH_MUC_AD = "SELECT dm.ten_danh_muc AS tenDanhMuc, dm.mo_ta AS moTa, dm.ngay_tao AS ngayTao, dm.ngay_cap_nhat AS ngayCapNhat FROM danh_muc dm ORDER BY dm.ngay_cap_nhat DESC;";

    public static final String ADD_DANH_MUC_AD = "INSERT INTO danh_muc (ten_danh_muc, mo_ta) VALUES (:tenDanhMuc, :moTa)";
}
