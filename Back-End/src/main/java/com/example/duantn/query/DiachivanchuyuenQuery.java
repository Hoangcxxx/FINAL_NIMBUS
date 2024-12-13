package com.example.duantn.query;

public class DiachivanchuyuenQuery {

    public static final String Set_Cung_Ship
            = "SELECT \n" +
            "    dvc.id_dia_chi_van_chuyen,\n" +
            "    dvc.dia_chi_cu_the,\n" +
            "    dvc.trang_thai AS trang_thai_dia_chi,\n" +
            "    dvc.ngay_tao AS ngay_tao_dia_chi,\n" +
            "    dvc.ngay_cap_nhat AS ngay_cap_nhat_dia_chi,\n" +
            "    dvc.mo_ta AS mo_ta_dia_chi,\n" +
            "    dvc.id_nguoi_dung,\n" +
            "    pvc.id_phi_van_chuyen,\n" +
            "    15000 AS phi_van_chuyen, -- Phí vận chuyển cố định là 15k\n" +
            "    pvc.ngay_tao AS ngay_tao_phi_van_chuyen,\n" +
            "    pvc.ngay_cap_nhat AS ngay_cap_nhat_phi_van_chuyen,\n" +
            "    pvc.mo_ta AS mo_ta_phi_van_chuyen\n" +
            "FROM \n" +
            "    dia_chi_van_chuyen dvc\n" +
            "JOIN \n" +
            "    phi_van_chuyen pvc ON dvc.id_dia_chi_van_chuyen = pvc.id_dia_chi_van_chuyen\n" +
            "WHERE \n" +
            "    pvc.trang_thai = 1;\n";
}
