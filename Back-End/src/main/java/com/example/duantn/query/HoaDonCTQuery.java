//package com.example.duantn.query;
//
//public class HoaDonCTQuery {
//    public static final String GET_ALL_HOA_DON_CT = "SELECT sp.Id_san_pham_chi_tiet AS idspct, \" +\n" +
//            "        \"sp.so_luong AS soluongsp, \" +\n" +
//            "        \"sp.ngay_tao AS ngaytaosp, \" +\n" +
//            "        \"gh.Id_gio_hang_chi_tiet AS giohangct, \" +\n" +
//            "        \"gh.so_luong AS soluonggiohang, \" +\n" +
//            "        \"gh.don_gia AS dongia, \" +\n" +
//            "        \"gh.thanh_tien AS thanhtien, \" +\n" +
//            "        \"hd.Id_hoa_don_chi_tiet AS idhoadonct, \" +\n" +
//            "        \"hd.so_luong AS soluonghd, \" +\n" +
//            "        \"hd.tong_tien AS tongtien, \" +\n" +
//            "        \"hd.so_tien_thanh_toan AS sotienthanhtoan, \" +\n" +
//            "        \"hd.tien_tra_lai AS tientralai \" +\n" +
//            "        \"FROM san_pham_chi_tiet sp \" +\n" +
//            "        \"JOIN gio_hang_chi_tiet gh ON sp.Id_san_pham_chi_tiet = gh.id_san_pham_chi_tiet \" +\n" +
//            "        \"JOIN hoa_don_chi_tiet hd ON sp.Id_san_pham_chi_tiet = hd.id_san_pham_chi_tiet \" +\n" +
//            "        \"WHERE sp.trang_thai = 1 AND gh.trang_thai = 1 AND hd.trang_thai = 1";
//}
