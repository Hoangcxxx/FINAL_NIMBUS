USE testdatn2;
GO
select * from thong_bao where id_nguoi_dung
select * from loai_thong_bao
select * from trang_thai_giam_gia
SELECT * FROM trang_thai_giam_gia WHERE Id_trang_thai_giam_gia = 5;
select * from dot_giam_gia
select * from giam_gia_san_pham
select * from giam_gia_san_pham
select * from voucher 
select * from vai_tro
select * from nguoi_dung 
select * from gio_hang 
select * from loai_trang_thai
select tthd.Id_trang_thai_hoa_don, tthd.mo_ta,tthd.ngay_tao,tthd.ngay_cap_nhat,l.ten_loai_trang_thai,tthd.id_hoa_don
from trang_thai_hoa_don tthd
join loai_trang_thai l on l.Id_loai_trang_thai = tthd.id_loai_trang_thai
where id_hoa_don = 1
select * from gio_hang
select * from hoa_don
select * from hoa_don_chi_tiet
select * from dia_chi_van_chuyen
select * from lich_su_thanh_toan
select * from lich_su_hoa_don
select * from gio_hang where id_nguoi_dung = :idNguoiDung
select * from nguoi_dung
select * from vai_tro
select * from gio_hang
select * from loai_voucher
select * from gio_hang_voucher
select * from voucher_nguoi_dung
select * from voucher
select * from loai_voucher
select * from trang_thai_giam_gia
select * from hoa_don
select * from hoa_don_chi_tiet
select * from san_pham_chi_tiet
select * from phi_van_chuyen
select * from dia_chi_van_chuyen
select * from dia_chi_van_chuyen
select * from tinh
select * from huyen
select * from xa
select * from san_pham s where s.ten_san_pham like '%a'
select * from san_pham_chi_tiet
select * from trang_thai_hoa_don
select * from loai_trang_thai

select * from gio_hang
select * from gio_hang_chi_tiet
SELECT dg.id_dot_giam_gia,dg.ten_dot_giam_gia,dg.kieu_giam_gia,dg.gia_tri_giam_gia, dg.mo_ta,dg.ngay_bat_dau,dg.ngay_ket_thuc,tgg.ten_trang_thai_giam_gia
FROM dot_giam_gia dg
JOIN trang_thai_giam_gia tgg ON dg.id_trang_thai_giam_gia = tgg.Id_trang_thai_giam_gia;
DELETE FROM dot_giam_gia
WHERE Id_dot_giam_gia = 22;

SELECT CASE WHEN COUNT(vn) > 0 THEN TRUE ELSE FALSE END 
           FROM Voucher_Nguoi_Dung vn join voucher v on vn.id_voucher = v.id_voucher
           WHERE vn.id_Voucher = 1 AND vn.id_Nguoi_Dung = 2

		   select * from voucher
SELECT COLUMN_NAME
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'nguoi_dung';





SELECT 
    sp.Id_san_pham,
    sp.ten_san_pham,
    sp.gia_ban,
    sp.mo_ta,
    dc.ten_danh_muc,
    ha.url_anh
FROM san_pham sp
LEFT JOIN giam_gia_san_pham ggs ON sp.Id_san_pham = ggs.id_san_pham
LEFT JOIN danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc 
LEFT JOIN hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham
WHERE 
    ha.thu_tu = 1 AND sp.id_danh_muc = 2 AND-- Chọn hình ảnh đầu tiên
    ggs.Id_giam_gia_san_pham IS NULL;


















DELETE FROM voucher
WHERE Id_voucher = 5;
SELECT * FROM voucher WHERE id_trang_thai_giam_gia = ?



-- Xóa giảm giá sản phẩm liên quan
DELETE FROM giam_gia_san_pham
WHERE id_dot_giam_gia = 10;

-- Sau đó xóa đợt giảm giá
DELETE FROM dot_giam_gia
WHERE Id_dot_giam_gia = 10;

SELECT 
    sp.Id_san_pham,
    sp.ten_san_pham,
    sp.gia_ban,
    sp.mo_ta,
    dc.ten_danh_muc,
    ha.url_anh
FROM san_pham sp
LEFT JOIN giam_gia_san_pham ggs ON sp.Id_san_pham = ggs.id_san_pham
LEFT JOIN danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc 
LEFT JOIN hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham
WHERE 
    ha.thu_tu = 1 AND -- Chọn hình ảnh đầu tiên
    ggs.Id_giam_gia_san_pham IS NULL;




	SELECT 
    sp.Id_san_pham,
    sp.ten_san_pham,
    sp.trang_thai,
    sp.gia_ban,
    sp.mo_ta,
    dc.ten_danh_muc, 
    ha.url_anh,
    ha.thu_tu
FROM 
    san_pham sp
LEFT JOIN 
    hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham
LEFT JOIN 
    danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc 
WHERE 
    ha.thu_tu = 1 -- Chọn hình ảnh đầu tiên
    AND sp.trang_thai = 1 -- Nếu không có hình ảnh nào
ORDER BY 
    sp.Id_san_pham;





SELECT 
    sp.Id_san_pham AS idSanPham, 
    sp.ten_san_pham AS tenSanPham, 
    sp.trang_thai AS trangThai, 
    sp.gia_ban AS giaBan,       
    MAX(sp.mo_ta) AS moTa,           
    dc.ten_danh_muc AS tenDanhMuc, 
    MAX(hl.url_anh) AS urlAnh,       
    MAX(hl.thu_tu) AS thuTu           
FROM 
    san_pham sp 
JOIN 
    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham 
LEFT JOIN 
    hinh_anh_san_pham hl ON spct.Id_san_pham = hl.id_san_pham 
LEFT JOIN 
    danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc 
WHERE 
    hl.thu_tu = 1 
    AND sp.trang_thai = 1 
    AND dc.Id_danh_muc = 1
GROUP BY 
    sp.Id_san_pham, 
    sp.ten_san_pham, 
    sp.trang_thai,  
    sp.gia_ban,  
    dc.ten_danh_muc;








SELECT 
    sp.Id_san_pham AS idSanPham, 
    sp.ten_san_pham AS tenSanPham, 
    sp.trang_thai AS trangThai, 
    sp.gia_ban AS giaBan,       
    sp.mo_ta AS moTa,           
    dc.ten_danh_muc AS tenDanhMuc, 
    hl.url_anh AS urlAnh,       
    hl.thu_tu AS thuTu           
FROM 
    san_pham sp 
JOIN 
    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham 
LEFT JOIN 
    hinh_anh_san_pham hl ON spct.Id_san_pham = hl.id_san_pham 
LEFT JOIN 
    danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc 
LEFT JOIN 
    giam_gia_san_pham ggs ON sp.Id_san_pham = ggs.id_san_pham 
WHERE 
    hl.thu_tu = 1 
    AND sp.trang_thai = 1 
    AND dc.Id_danh_muc = 2
    AND ggs.Id_giam_gia_san_pham IS NULL  -- Kiểm tra không có giảm giá
GROUP BY 
    sp.Id_san_pham, 
    sp.ten_san_pham, 
    sp.mo_ta,           
    sp.trang_thai,  
    sp.gia_ban,  
    hl.url_anh,  
    hl.thu_tu,  
    dc.ten_danh_muc;



SELECT 
    sp.Id_san_pham,
    sp.ten_san_pham,
    sp.trang_thai,
    sp.gia_ban,
    sp.mo_ta,
    dc.ten_danh_muc, 
    ha.url_anh,
    ha.thu_tu
FROM 
    san_pham sp
LEFT JOIN 
    hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham
LEFT JOIN 
    danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc 
LEFT JOIN 
    giam_gia_san_pham ggs ON sp.Id_san_pham = ggs.id_san_pham
WHERE 
    ha.thu_tu = 1 -- Chọn hình ảnh đầu tiên
    AND sp.trang_thai = 1 -- Sản phẩm đang hoạt động
    AND ggs.Id_giam_gia_san_pham IS NULL -- Sản phẩm chưa được giảm giá
ORDER BY 
    sp.Id_san_pham;






	SELECT 
    sp.Id_san_pham,
    sp.ten_san_pham,
    sp.gia_ban,
    ggs.gia_khuyen_mai,
    dgg.ten_dot_giam_gia,
    dgg.ngay_bat_dau,
    dgg.ngay_ket_thuc
FROM 
    san_pham AS sp
LEFT JOIN 
    giam_gia_san_pham AS ggs ON sp.Id_san_pham = ggs.id_san_pham
LEFT JOIN 
    dot_giam_gia AS dgg ON ggs.id_dot_giam_gia = dgg.Id_dot_giam_gia
WHERE 
    sp.trang_thai = 1
ORDER BY 
    sp.ten_san_pham;

-- Hiển thị ra những sản phẩm đang được giảm giá
SELECT 
    sp.Id_san_pham,
    sp.ten_san_pham,
    sp.gia_ban,
    ggs.gia_khuyen_mai,
    dgg.gia_tri_giam_gia,
    dgg.kieu_giam_gia,
    dgg.ten_dot_giam_gia,
    dgg.ngay_bat_dau,
    dgg.ngay_ket_thuc,
	sp.mo_ta,
    dc.ten_danh_muc,
    ha.url_anh
FROM 
    san_pham AS sp
LEFT JOIN giam_gia_san_pham AS ggs ON sp.Id_san_pham = ggs.id_san_pham
LEFT JOIN dot_giam_gia AS dgg ON ggs.id_dot_giam_gia = dgg.Id_dot_giam_gia
LEFT JOIN danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc 
LEFT JOIN hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham
WHERE 
    sp.trang_thai = 1
    AND ggs.gia_khuyen_mai IS NOT NULL AND ha.thu_tu = 1
ORDER BY 
    sp.ten_san_pham;





SELECT 
    sp.Id_san_pham,
    sp.ten_san_pham,
    sp.gia_ban,
    ggs.gia_khuyen_mai,
    dgg.gia_tri_giam_gia,
    dgg.kieu_giam_gia,
    dgg.ten_dot_giam_gia,
    dgg.ngay_bat_dau,
    dgg.ngay_ket_thuc,
    sp.mo_ta,
    dc.ten_danh_muc,
    ha.url_anh
FROM 
    san_pham AS sp
LEFT JOIN giam_gia_san_pham AS ggs ON sp.Id_san_pham = ggs.id_san_pham
LEFT JOIN dot_giam_gia AS dgg ON ggs.id_dot_giam_gia = dgg.Id_dot_giam_gia
LEFT JOIN danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc 
LEFT JOIN hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham
WHERE 
    sp.trang_thai = 1
    AND ggs.gia_khuyen_mai IS NOT NULL
    AND ha.thu_tu = 1
    AND dgg.ngay_bat_dau <= GETDATE()  -- Kiểm tra giảm giá đã bắt đầu
    AND dgg.ngay_ket_thuc >= GETDATE() -- Kiểm tra giảm giá chưa kết thúc
    AND dgg.id_trang_thai_giam_gia = (SELECT Id_trang_thai_giam_gia 
                                       FROM trang_thai_giam_gia 
                                       WHERE ten_trang_thai_giam_gia = N'Đang phát hành')  -- Kiểm tra trạng thái
ORDER BY 
    sp.ten_san_pham;

















SELECT 
    sp.Id_san_pham,
    sp.ten_san_pham,
    sp.mo_ta,
    sp.gia_ban,
    ha.url_anh
FROM 
    san_pham sp
LEFT JOIN 
    hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham
WHERE 
    ha.thu_tu = 1 -- Chọn hình ảnh đầu tiên
    AND sp.trang_thai = 1 -- Nếu không có hình ảnh nào
ORDER BY 
    sp.Id_san_pham;



	SELECT 
    sp.Id_san_pham AS idSanPham, 
    sp.ten_san_pham AS tenSanPham, 
    sp.trang_thai AS trangThai, 
    sp.gia_ban AS giaBan, 
    MAX(sp.mo_ta) AS moTa, 
    dc.ten_danh_muc AS tenDanhMuc, 
    MAX(hl.url_anh) AS urlAnh, 
    MAX(hl.thu_tu) AS thuTu 
FROM 
    san_pham sp 
JOIN 
    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham 
LEFT JOIN 
    (SELECT DISTINCT id_san_pham, url_anh, thu_tu FROM hinh_anh_san_pham) hl ON spct.Id_san_pham = hl.id_san_pham 
LEFT JOIN 
    danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc 
WHERE 
    hl.thu_tu = 1 AND sp.trang_thai = 1 
GROUP BY 
    sp.Id_san_pham, 
    sp.ten_san_pham, 
    sp.trang_thai, 
    dc.ten_danh_muc, 
    sp.gia_ban, 
    sp.mo_ta 
ORDER BY 
    sp.Id_san_pham ASC;

-- Sắp xếp theo tên sản phẩm

select * from san_pham




select * from hinh_anh_san_pham
select * from voucher
select * from dot_giam_gia
select * from giam_gia_san_pham
/*Lấy ra sản phẩm có hình ảnh là 1 và sắp xếp theo thứ tự tăng dần*/
SELECT 
    sp.Id_san_pham AS idSanPham, 
    sp.ten_san_pham AS tenSanPham, 
    sp.trang_thai AS trangThai, 
    sp.gia_ban AS giaBan,       -- Giá bán trung bình
    MAX(sp.mo_ta) AS moTa,           -- Mô tả sản phẩm
    dc.ten_danh_muc AS tenDanhMuc, 
    SUM(spct.so_luong) AS soLuong, 
    MAX(hl.url_anh) AS urlAnh,       -- Lấy URL ảnh
    MAX(hl.thu_tu) AS thuTu           -- Lấy thứ tự ảnh tối đa
FROM 
    san_pham sp 
JOIN 
    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham 
LEFT JOIN 
    hinh_anh_san_pham hl ON spct.Id_san_pham = hl.id_san_pham 
LEFT JOIN 
    danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc 
WHERE 
    hl.thu_tu = 1 
GROUP BY 
    sp.Id_san_pham, 
    sp.ten_san_pham, 
    sp.trang_thai,  -- Đảm bảo trường này có trong GROUP BY
    dc.ten_danh_muc,
    sp.gia_ban,
    sp.mo_ta,
    spct.so_luong
ORDER BY 
    sp.Id_san_pham ASC;  -- Sắp xếp theo idSanPham từ nhỏ đến lớn







SELECT 
    sp.Id_san_pham AS idSanPham, 
    sp.ten_san_pham AS tenSanPham, 
    sp.trang_thai AS trangThai, 
    sp.gia_ban AS giaBan, 
    MAX(sp.mo_ta) AS moTa, 
    dc.ten_danh_muc AS tenDanhMuc, 
    MAX(hl.url_anh) AS urlAnh, 
    MAX(hl.thu_tu) AS thuTu 
FROM 
    san_pham sp 
JOIN 
    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham 
LEFT JOIN 
    (SELECT DISTINCT id_san_pham, url_anh, thu_tu FROM hinh_anh_san_pham) hl ON spct.Id_san_pham = hl.id_san_pham 
LEFT JOIN 
    danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc 
WHERE 
    hl.thu_tu = 1 AND sp.trang_thai = 1
GROUP BY 
    sp.Id_san_pham, 
    sp.ten_san_pham, 
    sp.trang_thai, 
    dc.ten_danh_muc,
    sp.gia_ban,
    sp.mo_ta 
ORDER BY 
    sp.Id_san_pham ASC;







/*Lấy ra sản phẩm có hình ảnh là 1 và sắp xếp theo thứ tự tăng dần và phân trang*/
SELECT 
    sp.Id_san_pham AS idSanPham, 
    sp.ten_san_pham AS tenSanPham, 
    sp.trang_thai AS trangThai, 
    sp.gia_ban AS giaBan, 
    MAX(sp.mo_ta) AS moTa, 
    dc.ten_danh_muc AS tenDanhMuc, 
    SUM(spct.so_luong) AS soLuong, 
    MAX(hl.url_anh) AS urlAnh, 
    MAX(hl.thu_tu) AS thuTu
FROM 
    san_pham sp 
JOIN 
    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham 
LEFT JOIN 
    hinh_anh_san_pham hl ON spct.Id_san_pham = hl.id_san_pham 
LEFT JOIN 
    danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc 
WHERE 
    hl.thu_tu = 1 
GROUP BY 
    sp.Id_san_pham, 
    sp.ten_san_pham, 
    sp.trang_thai, 
    dc.ten_danh_muc,
    sp.gia_ban,
    spct.so_luong
ORDER BY 
    sp.Id_san_pham ASC
OFFSET 2 ROWS 
FETCH NEXT 5 ROWS ONLY;






/*Lấy ra hình ảnh sản phẩm có id là 1*/
SELECT 
    ha.url_anh,
    ha.thu_tu
FROM
    hinh_anh_san_pham ha join san_pham sp on ha.id_san_pham = sp.Id_san_pham
WHERE 
    ha.id_san_pham = 1;

	SELECT * FROM hinh_anh_san_pham ha WHERE ha.san_pham.idSanPham = 1
	SELECT * FROM san_pham;
	SELECT * FROM hinh_anh_san_pham;

	SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'san_pham';


/*Lấy ra sản phẩm chi tiết 1 có kích thước tương ứng với màu sắc*/	
SELECT 
    mct.id_mau_sac_chi_tiet, 
    ms.ten_mau_sac AS mau_sac 
FROM 
    san_pham sp 
JOIN 
    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham 
JOIN 
    mau_sac_chi_tiet mct ON spct.id_mau_sac_chi_tiet = mct.id_mau_sac_chi_tiet 
JOIN 
    mau_sac ms ON mct.id_mau_sac = ms.Id_mau_sac 
WHERE 
    sp.Id_san_pham = 4
GROUP BY 
    sp.Id_san_pham, mct.id_mau_sac_chi_tiet, ms.ten_mau_sac;







/*Lấy ra tất cả kích thước tương ứng với id sản phẩm chi tiết*/	
SELECT 
    kct.id_kich_thuoc_chi_tiet, 
    kt.ten_kich_thuoc AS kich_thuoc 
FROM 
    san_pham sp 
JOIN 
    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham 
JOIN 
    kich_thuoc_chi_tiet kct ON spct.id_kich_thuoc_chi_tiet = kct.id_kich_thuoc_chi_tiet 
JOIN 
    kich_thuoc kt ON kct.id_kich_thuoc = kt.Id_kich_thuoc 
WHERE 
    sp.Id_san_pham = 1 
GROUP BY 
    sp.Id_san_pham, kct.id_kich_thuoc_chi_tiet, kt.ten_kich_thuoc; -- Thay đổi ID nếu cần

	


/* Lấy ra tất cả chất liệu tương ứng với id sản phẩm chi tiết */	
SELECT 
    clct.Id_chat_lieu_tiet, 
    cl.ten_chat_lieu AS chat_lieu 
FROM 
    san_pham sp 
JOIN 
    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham 
JOIN 
    chat_lieu_chi_tiet clct ON spct.id_chat_lieu_chi_tiet = clct.Id_chat_lieu_tiet 
JOIN 
    chat_lieu cl ON clct.id_chat_lieu = cl.Id_chat_lieu 
WHERE 
    sp.Id_san_pham = 1 
GROUP BY 
    sp.Id_san_pham, clct.Id_chat_lieu_tiet, cl.ten_chat_lieu; -- Thay đổi ID nếu cần







SELECT 
    sp.Id_san_pham,
    ms.ten_mau_sac,
    kc.ten_kich_thuoc
FROM 
    san_pham sp
JOIN 
    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham
LEFT JOIN 
    kich_thuoc_chi_tiet kct ON spct.id_kich_thuoc_chi_tiet = kct.id_kich_thuoc_chi_tiet
LEFT JOIN 
    kich_thuoc kc ON kct.id_kich_thuoc = kc.Id_kich_thuoc
LEFT JOIN 
    mau_sac_chi_tiet mct ON spct.id_mau_sac_chi_tiet = mct.id_mau_sac_chi_tiet
LEFT JOIN 
    mau_sac ms ON mct.id_mau_sac = ms.Id_mau_sac
WHERE 
    sp.Id_san_pham = 2 and ms.id_mau_sac = 2;




SELECT  
    sp.Id_san_pham,
    ms.ten_mau_sac,
    kt.ten_kich_thuoc
FROM 
    san_pham sp
JOIN 
    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham
LEFT JOIN 
    mau_sac_chi_tiet mct ON spct.id_mau_sac_chi_tiet = mct.id_mau_sac_chi_tiet
LEFT JOIN 
    mau_sac ms ON mct.id_mau_sac = ms.Id_mau_sac
LEFT JOIN 
    kich_thuoc_chi_tiet kct ON spct.id_kich_thuoc_chi_tiet = kct.id_kich_thuoc_chi_tiet
LEFT JOIN 
    kich_thuoc kt ON kct.id_kich_thuoc = kt.Id_kich_thuoc
WHERE 
    sp.Id_san_pham = 2 and ms.Id_mau_sac = 2
ORDER BY 
    ms.ten_mau_sac, kt.ten_kich_thuoc; -- Thay đổi tiêu chí sắp xếp nếu cần

SELECT 
    sp.id_san_pham, 
    sp.ten_san_pham, 
    spct.gia_ban,
    sp.mo_ta
FROM 
    san_pham sp
JOIN 
    san_pham_chi_tiet spct ON sp.id_san_pham = spct.id_san_pham
WHERE 
    sp.id_san_pham = 1
GROUP BY 
    sp.id_san_pham, 
    sp.ten_san_pham,
	spct.gia_ban,
    sp.mo_ta;







/*Lấy ra tất cả sản phẩm tương ứng với danh mục của id sản phẩm*/	
SELECT 
    sp.Id_san_pham AS idSanPham, 
    sp.ten_san_pham AS tenSanPham, 
    sp.trang_thai AS trangThai, 
    spct.gia_ban AS giaBan,       -- Giá bán trung bình
    MAX(sp.mo_ta) AS moTa,           -- Mô tả sản phẩm
    dc.ten_danh_muc AS tenDanhMuc, 
    MAX(hl.url_anh) AS urlAnh,       -- Lấy URL ảnh
    MAX(hl.thu_tu) AS thuTu           -- Lấy thứ tự ảnh tối đa
FROM 
    san_pham sp 
JOIN 
    san_pham_chi_tiet spct ON sp.Id_san_pham = spct.id_san_pham 
LEFT JOIN 
    hinh_anh_san_pham hl ON spct.Id_san_pham = hl.id_san_pham 
LEFT JOIN 
    danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc 
WHERE 
    hl.thu_tu = 1 and dc.Id_danh_muc = 1
GROUP BY 
    sp.Id_san_pham, 
    sp.ten_san_pham, 
    sp.trang_thai,  -- Đảm bảo trường này có trong GROUP BY
    spct.gia_ban,  -- Đảm bảo trường này có trong GROUP BY
    dc.ten_danh_muc
ORDER BY 
    sp.Id_san_pham ASC;  -- Sắp xếp theo idSanPham từ nhỏ đến lớn




/* Lấy ra tất cả sản phẩm của admin có tên danh mục và mô tả, sắp xếp theo ngày tạo mới nhất */
SELECT 
    sp.ten_san_pham AS tenSanPham,
    sp.mo_ta AS moTa,
    dm.ten_danh_muc AS tenDanhMuc
FROM 
    san_pham sp
JOIN 
    danh_muc dm ON sp.id_danh_muc = dm.Id_danh_muc
WHERE 
    sp.trang_thai = 1 -- Điều kiện để lấy sản phẩm đang hoạt động
ORDER BY 
    sp.ngay_tao DESC; -- Sắp xếp theo ngày tạo mới nhất




/* Lấy ra danh sách danh mục, sắp xếp theo ngày tạo mới nhất */
SELECT 
    dm.ten_danh_muc AS tenDanhMuc,
    dm.mo_ta AS moTa,
    dm.ngay_tao AS ngayTao,
    dm.ngay_cap_nhat AS ngayCapNhat
FROM 
    danh_muc dm
ORDER BY 
    dm.ngay_cap_nhat DESC; -- Sắp xếp theo ngày tạo mới nhất

	select * from san_pham_chi_tiet

/* Lấy ra danh sách sản phẩm chi tiết mới nhất tương ứng với id sản phẩm */
SELECT *
FROM san_pham_chi_tiet
ORDER BY ngay_cap_nhat DESC;


/* Lấy ra danh sách sản phẩm chi tiết mới nhất tương ứng với id sản phẩm */
SELECT spc.*
FROM san_pham_chi_tiet spc
INNER JOIN (
    SELECT id_san_pham, MAX(ngay_cap_nhat) AS max_ngay_cap_nhat
    FROM san_pham_chi_tiet
    GROUP BY id_san_pham
) AS subquery ON spc.id_san_pham = subquery.id_san_pham 
              AND spc.ngay_cap_nhat = subquery.max_ngay_cap_nhat
ORDER BY spc.ngay_cap_nhat DESC;






BEGIN TRANSACTION;

-- Thêm sản phẩm vào bảng san_pham
INSERT INTO san_pham (ten_san_pham, gia_ban, mo_ta, id_danh_muc, id_loai_voucher)
VALUES ('Tên sản phẩm mới', 100000, 'Mô tả sản phẩm', 1, 1);

DECLARE @Id_san_pham INT;
SET @Id_san_pham = SCOPE_IDENTITY();

-- Thêm hình ảnh vào bảng hinh_anh_san_pham
INSERT INTO hinh_anh_san_pham (id_san_pham, url_anh, mo_ta, thu_tu, loai_hinh_anh)
VALUES (@Id_san_pham, 'http://linktoimage.com/hinhsanpham.jpg', 'Mô tả hình ảnh', 1, 'Hình chính');

COMMIT TRANSACTION;





DBCC CHECKIDENT ('san_pham', RESEED);
DECLARE @MaxID INT;


SELECT @MaxID = MAX(id) FROM san_pham;
DBCC CHECKIDENT ('san_pham', RESEED, @MaxID + 1);



select * from hinh_anh_san_pham
select * from san_pham
select * from san_pham_chi_tiet


INSERT INTO san_pham (id_danh_muc, ten_san_pham, gia_ban, mo_ta, ngay_tao, ngay_cap_nhat, trang_thai) 
VALUES (1, '51111', 100000, '124125', GETDATE(), GETDATE(), 1); 
SELECT SCOPE_IDENTITY() AS IdSanPham; -- Lấy ID sản phẩm vừa thêm

INSERT INTO hinh_anh_san_pham (id_san_pham, url_anh, thu_tu, loai_hinh_anh) 
VALUES (:idSanPham, :urlAnh, :thuTu, :loaiHinhAnh);

sp_help san_pham;

SELECT 
    sp.ten_san_pham,
    spct.so_luong,
    spct.so_luong,
    spct.so_luong,
    spct.so_luong,
    sp.mo_ta
FROM 
    san_pham_chi_tiet spct
JOIN 
    san_pham sp ON sp.Id_san_pham = spct.id_san_pham
WHERE 
    spct.id_san_pham = 5;






INSERT INTO san_pham (ten_san_pham, gia_ban, mo_ta, id_danh_muc, ngay_tao, ngay_cap_nhat, trang_thai) 
VALUES (:tenSanPham, :giaBan, :moTa, :idDanhMuc, :ngayTao, :ngayCapNhat, :trangThai);
SELECT SCOPE_IDENTITY();



/* Lấy ra danh sách sản phẩm chi tiết mới nhất tương ứng với id sản phẩm */
select * from san_pham_chi_tiet
SELECT 
FROM san_pham_chi_tiet
WHERE id_san_pham = 5

















SHOW TRIGGERS LIKE 'san_pham_chi_tiet';

SELECT so_luong FROM san_pham_chi_tiet WHERE id_san_pham_chi_tiet = 686;


/* Lấy ra danh sách sản phẩm chi tiết mới nhất tương ứng với id sản phẩm */

SELECT 
    spc.Id_san_pham_chi_tiet,
    sp.ten_san_pham,
    spc.so_luong,
    kc.ten_kich_thuoc,
    ms.ten_mau_sac,
    cl.ten_chat_lieu
FROM 
    san_pham sp
JOIN 
    san_pham_chi_tiet spc ON sp.Id_san_pham = spc.id_san_pham
JOIN 
    mau_sac_chi_tiet msc ON spc.id_mau_sac_chi_tiet = msc.Id_mau_sac_chi_tiet
JOIN 
    mau_sac ms ON msc.id_mau_sac = ms.Id_mau_sac
JOIN 
    chat_lieu_chi_tiet clc ON spc.id_chat_lieu_chi_tiet = clc.Id_chat_lieu_tiet
JOIN 
    chat_lieu cl ON clc.id_chat_lieu = cl.Id_chat_lieu
JOIN 
    kich_thuoc_chi_tiet kcc ON spc.id_kich_thuoc_chi_tiet = kcc.Id_kich_thuoc_chi_tiet
JOIN 
    kich_thuoc kc ON kcc.id_kich_thuoc = kc.Id_kich_thuoc
WHERE 
    sp.Id_san_pham = 64 -- Replace @productId with the actual product ID

	SELECT * FROM san_pham_chi_tiet WHERE Id_san_pham_chi_tiet IN (685, 686, 687, 688, 689, 690, 691, 692);


EXEC sp_help 'san_pham_chi_tiet';



	UPDATE san_pham_chi_tiet
SET so_luong = 10 -- ví dụ số lượng
WHERE id_san_pham_chi_tiet = 685; -- ID cụ thể




	DESCRIBE san_pham_chi_tiet;
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'san_pham_chi_tiet';
select * from san_pham
select * from san_pham_chi_tiet

	select * from danh_muc
	select * from hinh_anh_san_pham
SELECT 
    sp.ten_san_pham,
    spc.so_luong,
    cl.ten_chat_lieu,
    ms.ten_mau_sac,
    kc.ten_kich_thuoc
FROM 
    san_pham sp
JOIN 
    san_pham_chi_tiet spc ON sp.Id_san_pham = spc.id_san_pham
JOIN 
    mau_sac_chi_tiet msc ON spc.id_mau_sac_chi_tiet = msc.Id_mau_sac_chi_tiet
JOIN 
    mau_sac ms ON msc.id_mau_sac = ms.Id_mau_sac
JOIN 
    chat_lieu_chi_tiet clc ON spc.id_chat_lieu_chi_tiet = clc.Id_chat_lieu_tiet
JOIN 
    chat_lieu cl ON clc.id_chat_lieu = cl.Id_chat_lieu
JOIN 
    kich_thuoc_chi_tiet kcc ON spc.id_kich_thuoc_chi_tiet = kcc.Id_kich_thuoc_chi_tiet
JOIN 
    kich_thuoc kc ON kcc.id_kich_thuoc = kc.Id_kich_thuoc
WHERE 
    sp.Id_san_pham = 59 -- Replace 59 with the actual product ID




-- Cập nhật số lượng trong bảng san_pham_chi_tiet
UPDATE sp
SET sp.so_luong = sp.so_luong + tu.so_luong_them,
    sp.ngay_cap_nhat = GETDATE()
FROM san_pham_chi_tiet sp
JOIN #temp_updates tu ON sp.Id_san_pham_chi_tiet = tu.Id_san_pham_chi_tiet;

-- Xóa bảng tạm
DROP TABLE #temp_updates;




select * from san_pham

DELETE FROM san_pham
WHERE Id_san_pham = 62;

select * from san_pham
-- Tắt ràng buộc khóa ngoại
ALTER TABLE san_pham_chi_tiet NOCHECK CONSTRAINT FK_san_pham_chi_tiet_id_san_pham;
DELETE FROM hinh_anh_san_pham
WHERE id_san_pham = 62;

DELETE FROM san_pham_chi_tiet
WHERE Id_san_pham_chi_tiet = 684;
-- Xóa sản phẩm
DELETE FROM san_pham
WHERE Id_san_pham = 62;

-- Bật lại ràng buộc khóa ngoại
select * from san_pham_chi_tiet

SELECT COLUMN_NAME 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'san_pham';



UPDATE san_pham
SET Trang_thai = @new_trang_thai, 
    ngay_cap_nhat = GETDATE()
WHERE Id_san_pham = @id_san_pham;




UPDATE san_pham
SET Trang_thai = 0, 
    ngay_cap_nhat = GETDATE()
WHERE Id_san_pham = 1;



UPDATE san_pham
SET Trang_thai = 0, -- hoặc 1 tùy thuộc vào trạng thái bạn muốn thiết lập
    ngay_cap_nhat = GETDATE() -- Cập nhật thời gian cập nhật
WHERE Id_san_pham = @id_san_pham; 


select * from san_pham



select * from san_pham_chi_tiet

SELECT 
    sp.Id_san_pham,
    sp.ten_san_pham,
    sp.gia_ban,
    spct.so_luong,
    cl.ten_chat_lieu,
    ms.ten_mau_sac,
    kt.ten_kich_thuoc
FROM 
    san_pham AS sp
JOIN 
    san_pham_chi_tiet AS spct ON sp.Id_san_pham = spct.id_san_pham
JOIN 
    chat_lieu_chi_tiet AS clt ON spct.id_chat_lieu_chi_tiet = clt.Id_chat_lieu_tiet
JOIN 
    chat_lieu AS cl ON clt.id_chat_lieu = cl.Id_chat_lieu
JOIN 
    mau_sac_chi_tiet AS msc ON spct.id_mau_sac_chi_tiet = msc.Id_mau_sac_chi_tiet
JOIN 
    mau_sac AS ms ON msc.id_mau_sac = ms.Id_mau_sac
JOIN 
    kich_thuoc_chi_tiet AS kct ON spct.id_kich_thuoc_chi_tiet = kct.Id_kich_thuoc_chi_tiet
JOIN 
    kich_thuoc AS kt ON kct.id_kich_thuoc = kt.Id_kich_thuoc
JOIN 
    danh_muc AS dm ON sp.id_danh_muc = dm.Id_danh_muc  -- Thêm phép nối với bảng danh_muc
WHERE 
    dm.Id_danh_muc = 1  -- Thay thế bằng ID danh mục nếu cần
    AND sp.Id_san_pham = 1  -- Thay thế bằng ID chất liệu nếu cần
    AND cl.Id_chat_lieu = 1  -- Thay thế bằng ID chất liệu nếu cần
    AND ms.Id_mau_sac = 3  -- Thay thế bằng ID màu sắc nếu cần
    AND kt.Id_kich_thuoc = 1;  -- Thay thế bằng ID kích thước nếu cần









SELECT 
    sp.Id_san_pham, 
    sp.ten_san_pham, 
    SUM(spct.so_luong) AS so_luong,
    dm.id_danh_muc, 
    dm.ten_danh_muc, 
    cl.id_chat_lieu, 
    cl.ten_chat_lieu, 
    ms.id_mau_sac, 
    ms.ten_mau_sac, 
    kt.id_kich_thuoc, 
    kt.ten_kich_thuoc, 
    spct.trang_thai
FROM 
    san_pham AS sp
JOIN 
    san_pham_chi_tiet AS spct ON sp.Id_san_pham = spct.id_san_pham
JOIN 
    chat_lieu_chi_tiet AS clt ON spct.id_chat_lieu_chi_tiet = clt.Id_chat_lieu_tiet
JOIN 
    chat_lieu AS cl ON clt.id_chat_lieu = cl.Id_chat_lieu
JOIN 
    mau_sac_chi_tiet AS msc ON spct.id_mau_sac_chi_tiet = msc.Id_mau_sac_chi_tiet
JOIN 
    mau_sac AS ms ON msc.id_mau_sac = ms.Id_mau_sac
JOIN 
    kich_thuoc_chi_tiet AS kct ON spct.id_kich_thuoc_chi_tiet = kct.Id_kich_thuoc_chi_tiet
JOIN 
    kich_thuoc AS kt ON kct.id_kich_thuoc = kt.Id_kich_thuoc
JOIN 
    danh_muc AS dm ON sp.id_danh_muc = dm.Id_danh_muc
WHERE 
    (1 IS NULL OR dm.Id_danh_muc = 1) 
    AND (1 IS NULL OR sp.Id_san_pham = 1) 
    AND (1 IS NULL OR cl.Id_chat_lieu = 1) 
    AND (3 IS NULL OR ms.Id_mau_sac = 3) 
    AND (1 IS NULL OR kt.Id_kich_thuoc = 1)
GROUP BY 
    sp.Id_san_pham, 
    sp.ten_san_pham, 
    dm.id_danh_muc, 
    dm.ten_danh_muc, 
    cl.id_chat_lieu, 
    cl.ten_chat_lieu, 
    ms.id_mau_sac, 
    ms.ten_mau_sac, 
    kt.id_kich_thuoc, 
    kt.ten_kich_thuoc, 
    spct.trang_thai;














/* Lấy ra sản phẩm theo danh mục sắp xếp theo thứ tự mới nhất */
SELECT 
    sp.id_san_pham, 
    sp.ten_san_pham 
FROM 
    danh_muc dm
LEFT JOIN 
    san_pham sp ON dm.Id_danh_muc = sp.id_danh_muc
WHERE 
    dm.id_danh_muc = 2
ORDER BY 
    sp.ngay_tao DESC; -- Sắp xếp theo ngày tạo mới nhất

	
	/* Lấy ra chất liệu tương ứng với id sản phẩm sắp xếp theo thứ tự mới nhất */
SELECT 
    cl.id_chat_lieu,
    cl.ten_chat_lieu
FROM 
    san_pham_chi_tiet spct
JOIN 
    chat_lieu_chi_tiet clct ON spct.id_chat_lieu_chi_tiet = clct.Id_chat_lieu_tiet
JOIN 
    chat_lieu cl ON clct.id_chat_lieu = cl.Id_chat_lieu
WHERE 
    spct.id_san_pham = 70 -- Thay 1 bằng giá trị ID sản phẩm cần truy vấn
GROUP BY 
     cl.id_chat_lieu,
    cl.ten_chat_lieu



select * from san_pham_chi_tiet	


	/* Lấy ra màu sắc tương ứng với id sản phẩm và chất liệu sắp xếp theo thứ tự mới nhất */
SELECT 
    ms.id_mau_sac,
    ms.ten_mau_sac
FROM 
    san_pham_chi_tiet spct
JOIN 
    chat_lieu_chi_tiet clct ON spct.id_chat_lieu_chi_tiet = clct.Id_chat_lieu_tiet
JOIN 
    chat_lieu cl ON clct.id_chat_lieu = cl.Id_chat_lieu
JOIN 
    mau_sac_chi_tiet mscct ON spct.id_mau_sac_chi_tiet = mscct.Id_mau_sac_chi_tiet
JOIN 
    mau_sac ms ON mscct.id_mau_sac = ms.Id_mau_sac
WHERE 
    spct.id_san_pham = 12 -- Thay đổi ID sản phẩm tại đây
    AND cl.Id_chat_lieu = 1 -- Thay đổi ID chất liệu tại đây
GROUP BY 
    ms.id_mau_sac, ms.ten_mau_sac


/* Lấy ra kích thước tương ứng với id sản phẩm, chất liệu, màu sắc sắp xếp theo thứ tự mới nhất */
SELECT 
    kc.id_kich_thuoc,
    kc.ten_kich_thuoc
FROM 
    san_pham_chi_tiet spct
    LEFT JOIN 
    kich_thuoc_chi_tiet kcct ON spct.id_kich_thuoc_chi_tiet = kcct.Id_kich_thuoc_chi_tiet
    LEFT JOIN 
    kich_thuoc kc ON kc.id_kich_thuoc = kcct.Id_kich_thuoc
    JOIN 
    chat_lieu_chi_tiet clct ON spct.id_chat_lieu_chi_tiet = clct.Id_chat_lieu_tiet
    JOIN 
    chat_lieu cl ON clct.id_chat_lieu = cl.Id_chat_lieu
    JOIN 
    mau_sac_chi_tiet mscct ON spct.id_mau_sac_chi_tiet = mscct.Id_mau_sac_chi_tiet
    JOIN 
    mau_sac ms ON mscct.id_mau_sac = ms.Id_mau_sac
WHERE 
    spct.id_san_pham = 12 -- Thay đổi ID sản phẩm tại đây
    AND cl.Id_chat_lieu = 1 -- Thay đổi ID chất liệu tại đây
    AND ms.Id_mau_sac = 10 -- Thay đổi ID màu sắc tại đây
GROUP BY 
    kc.id_kich_thuoc,
    kc.ten_kich_thuoc


	select dm.Id_danh_muc, dm.ten_danh_muc from danh_muc dm

	
/* Lấy ra sản phẩm chi tiết tương ứng với id sản phẩm sắp xếp theo thứ tự mới nhất */
SELECT 
    sp.id_san_pham,
    spct.id_san_pham_chi_tiet,
    sp.ten_san_pham,
    spct.so_luong, -- Giả sử có cột số lượng trong bảng sản phẩm chi tiết
    cl.ten_chat_lieu,
    ms.ten_mau_sac,
    kc.ten_kich_thuoc,
    spct.trang_thai -- Giả sử có cột trạng thái trong bảng sản phẩm chi tiết
FROM 
    san_pham sp
JOIN 
    san_pham_chi_tiet spct ON sp.id_san_pham = spct.id_san_pham
JOIN 
    chat_lieu_chi_tiet clct ON spct.id_chat_lieu_chi_tiet = clct.Id_chat_lieu_tiet
JOIN 
    chat_lieu cl ON clct.id_chat_lieu = cl.Id_chat_lieu
JOIN 
    mau_sac_chi_tiet mscct ON spct.id_mau_sac_chi_tiet = mscct.Id_mau_sac_chi_tiet
JOIN 
    mau_sac ms ON mscct.id_mau_sac = ms.Id_mau_sac
LEFT JOIN 
    kich_thuoc_chi_tiet kcct ON spct.id_kich_thuoc_chi_tiet = kcct.Id_kich_thuoc_chi_tiet
LEFT JOIN 
    kich_thuoc kc ON kc.id_kich_thuoc = kcct.Id_kich_thuoc
WHERE 
    sp.id_san_pham = 12 -- Lọc theo ID danh mục sản phẩm
    AND cl.id_chat_lieu =1 -- Lọc theo ID chất liệu
    AND ms.id_mau_sac = 10 -- Lọc theo ID màu sắc
    AND kc.id_kich_thuoc = 3 -- Lọc theo ID kích thước




	
/* Lấy ra ds voucher */
SELECT 
    v.id_voucher,
    v.ma_voucher,
    lv.id_loai_voucher,
    lv.ten_loai_voucher,
    v.gia_tri_giam_gia,
    v.so_luong,
    v.ngay_bat_dau,
    v.ngay_ket_thuc,
    v.ngay_tao,
    v.ngay_cap_nhat,
    v.trang_thai
FROM 
    voucher v
JOIN 
    loai_voucher lv ON v.id_loai_voucher = lv.Id_loai_voucher






select * from voucher
select * from loai_voucher



SELECT v.ma_voucher, v.gia_tri_giam_gia, v.ngay_bat_dau, v.ngay_ket_thuc
FROM voucher v
JOIN hoa_don h ON v.Id_voucher = h.id_voucher
WHERE h.id_nguoi_dung = 3;









SELECT 
    dcvc.Id_dia_chi_van_chuyen, 
    t.ten_tinh AS Tinh, 
    h.ten_huyen AS Huyen, 
    x.ten_xa AS Xa, 
    dcvc.dia_chi_cu_the AS DiaChiCuThe, 
    dcvc.mo_ta AS MoTa, 
    dcvc.trang_thai AS TrangThai
FROM 
    dia_chi_van_chuyen dcvc
JOIN 
    tinh t ON dcvc.id_tinh = t.Id_tinh
JOIN 
    huyen h ON dcvc.id_huyen = h.Id_huyen
JOIN 
    xa x ON dcvc.id_xa = x.Id_xa
where dcvc.id_nguoi_dung = 3;





SELECT 
    sp.Id_san_pham,
    sp.ma_san_pham,
    sp.ten_san_pham,
    sp.trang_thai,
    sp.gia_ban,
    sp.mo_ta,
    dc.ten_danh_muc, 
    ha.url_anh,
    ha.thu_tu
FROM 
    san_pham sp
LEFT JOIN 
    hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham
LEFT JOIN 
    danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc
LEFT JOIN 
    giam_gia_san_pham ggs ON sp.Id_san_pham = ggs.id_san_pham
WHERE 
    ha.thu_tu = 1 -- Chọn hình ảnh đầu tiên
    AND sp.trang_thai = 1 -- Sản phẩm đang hoạt động
ORDER BY 
    sp.Id_san_pham;










SELECT 
    sp.id_san_pham,
    sp.ma_san_pham,
    sp.ten_san_pham,
    spc.so_luong,
    cl.ten_chat_lieu,
    ms.ten_mau_sac,
    kc.ten_kich_thuoc,
    sp.mo_ta,
    spc.id_san_pham_chi_tiet
FROM san_pham sp
JOIN san_pham_chi_tiet spc 
    ON sp.id_san_pham = spc.id_san_pham
JOIN mau_sac_chi_tiet msc 
    ON spc.id_mau_sac_chi_tiet = msc.id_mau_sac_chi_tiet
JOIN mau_sac ms 
    ON msc.id_mau_sac = ms.id_mau_sac
JOIN chat_lieu_chi_tiet clc 
    ON spc.id_chat_lieu_chi_tiet = clc.id_chat_lieu_chi_tiet
JOIN chat_lieu cl 
    ON clc.id_chat_lieu = cl.id_chat_lieu
JOIN kich_thuoc_chi_tiet kcc 
    ON spc.id_kich_thuoc_chi_tiet = kcc.id_kich_thuoc_chi_tiet
JOIN kich_thuoc kc 
    ON kcc.id_kich_thuoc = kc.id_kich_thuoc
WHERE sp.id_san_pham = 3;













SELECT 
    sp.Id_san_pham,
    sp.ma_san_pham,
    sp.ten_san_pham,
    sp.gia_ban,
    sp.mo_ta AS mo_ta_san_pham,
    sp.trang_thai,
    dm.ten_danh_muc,
    dgg.ten_dot_giam_gia,
    dgg.gia_tri_giam_gia,
    dgg.kieu_giam_gia,
    dgg.ngay_bat_dau,
    dgg.ngay_ket_thuc,
    ggs.gia_khuyen_mai
FROM 
    san_pham sp
JOIN 
    danh_muc dm ON sp.id_danh_muc = dm.Id_danh_muc
JOIN 
    giam_gia_san_pham ggs ON sp.Id_san_pham = ggs.id_san_pham
JOIN 
    dot_giam_gia dgg ON ggs.id_dot_giam_gia = dgg.Id_dot_giam_gia
WHERE 
    GETDATE() BETWEEN dgg.ngay_bat_dau AND dgg.ngay_ket_thuc
    AND sp.trang_thai = 1;  -- Chỉ lấy sản phẩm đang hoạt động (trang_thai = 1)





	SELECT 
    sp.Id_san_pham,
    sp.ma_san_pham,
    sp.ten_san_pham,
    sp.gia_ban,
    sp.mo_ta AS mo_ta_san_pham,
    dm.ten_danh_muc,
    dgg.ten_dot_giam_gia,
    dgg.gia_tri_giam_gia,
    dgg.kieu_giam_gia,
    dgg.ngay_bat_dau,
    dgg.ngay_ket_thuc
FROM 
    san_pham sp
LEFT JOIN 
    danh_muc dm ON sp.id_danh_muc = dm.Id_danh_muc
LEFT JOIN 
    giam_gia_san_pham ggsp ON sp.Id_san_pham = ggsp.id_san_pham
LEFT JOIN 
    dot_giam_gia dgg ON ggsp.id_dot_giam_gia = dgg.Id_dot_giam_gia
WHERE 
    sp.trang_thai = 1 -- Chỉ lấy sản phẩm còn hoạt động (trang_thai = 1)
    AND (dgg.ngay_ket_thuc >= GETDATE() OR dgg.ngay_ket_thuc IS NULL) -- Chỉ lấy các đợt giảm giá chưa hết hạn
ORDER BY 
    sp.ten_san_pham;




select * from dot_giam_gia
SELECT 
    sp.Id_san_pham,
    sp.ma_san_pham,
    sp.ten_san_pham,
    sp.gia_ban,
    sp.mo_ta ,
    dm.ten_danh_muc,
    dgg.ten_dot_giam_gia,
    ggsp.gia_khuyen_mai,
    dgg.gia_tri_giam_gia,
    dgg.kieu_giam_gia,
    dgg.ngay_bat_dau,
    dgg.ngay_ket_thuc
FROM 
    san_pham sp
LEFT JOIN 
    danh_muc dm ON sp.id_danh_muc = dm.Id_danh_muc
LEFT JOIN 
    giam_gia_san_pham ggsp ON sp.Id_san_pham = ggsp.id_san_pham
LEFT JOIN 
    dot_giam_gia dgg ON ggsp.id_dot_giam_gia = dgg.Id_dot_giam_gia
WHERE 
    sp.trang_thai = 1
    AND (dgg.ngay_ket_thuc >= GETDATE() OR dgg.ngay_ket_thuc IS NULL)
ORDER BY 
    sp.ten_san_pham ASC, 
    sp.gia_ban ASC;







SELECT 
    sp.Id_san_pham,
    sp.ma_san_pham,
    sp.ten_san_pham,
    sp.gia_ban,
    sp.mo_ta ,
    dm.ten_danh_muc,
    dgg.ten_dot_giam_gia,
    ggsp.gia_khuyen_mai,
    dgg.gia_tri_giam_gia,
    dgg.kieu_giam_gia,
    dgg.ngay_bat_dau,
    dgg.ngay_ket_thuc,
	ha.url_anh,
	ha.thu_tu
FROM 
    san_pham sp
LEFT JOIN 
    danh_muc dm ON sp.id_danh_muc = dm.Id_danh_muc
LEFT JOIN 
    giam_gia_san_pham ggsp ON sp.Id_san_pham = ggsp.id_san_pham
LEFT JOIN 
    dot_giam_gia dgg ON ggsp.id_dot_giam_gia = dgg.Id_dot_giam_gia
LEFT JOIN 
    hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham
WHERE 
    sp.trang_thai = 1 and ha.thu_tu = 1
    AND (dgg.ngay_ket_thuc >= GETDATE() OR dgg.ngay_ket_thuc IS NULL)
ORDER BY 
    CASE 
        WHEN ggsp.id_san_pham IS NOT NULL THEN 0 -- Sản phẩm có khuyến mãi sẽ xuất hiện đầu tiên
        ELSE 1
    END,
    sp.ten_san_pham ASC, -- Sắp xếp theo tên sản phẩm (A-Z)
    sp.gia_ban ASC; -- Sắp xếp theo giá (tăng dần)











SELECT 
    sp.Id_san_pham,
    sp.ma_san_pham,
    sp.ten_san_pham,
    sp.gia_ban,
    sp.mo_ta,
    dm.ten_danh_muc,
    dgg.ten_dot_giam_gia,
    ggsp.gia_khuyen_mai,
    dgg.gia_tri_giam_gia,
    dgg.kieu_giam_gia,
    dgg.ngay_bat_dau,
    dgg.ngay_ket_thuc
FROM 
    san_pham sp
LEFT JOIN 
    danh_muc dm ON sp.id_danh_muc = dm.Id_danh_muc
LEFT JOIN 
    giam_gia_san_pham ggsp ON sp.Id_san_pham = ggsp.id_san_pham
LEFT JOIN 
    dot_giam_gia dgg ON ggsp.id_dot_giam_gia = dgg.Id_dot_giam_gia
WHERE 
    sp.trang_thai = 1
    AND (dgg.ngay_ket_thuc >= GETDATE() OR dgg.ngay_ket_thuc IS NULL)
    AND sp.id_danh_muc = 1 -- Thêm điều kiện lọc theo id_danh_muc
ORDER BY 
    CASE 
        WHEN ggsp.id_san_pham IS NOT NULL THEN 0 -- Sản phẩm có khuyến mãi sẽ xuất hiện đầu tiên
        ELSE 1
    END,
    sp.ten_san_pham ASC, -- Sắp xếp theo tên sản phẩm (A-Z)
    sp.gia_ban ASC; -- Sắp xếp theo giá (tăng dần)




SELECT 
    h.id_hoa_don,
    h.ma_hoa_don,
    u.ten_nguoi_dung,
    h.ngay_tao,
    h.thanh_tien,
    lt.ten_loai_trang_thai 
FROM 
    hoa_don h
JOIN 
    nguoi_dung u ON h.id_nguoi_dung = u.id_nguoi_dung
JOIN 
    trang_thai_hoa_don ts ON h.id_hoa_don = ts.id_hoa_don
JOIN 
    loai_trang_thai lt ON ts.id_loai_trang_thai = lt.Id_loai_trang_thai
where lt.id_loai_trang_thai = 2







SELECT 
    hd.ma_hoa_don,
    hd.ten_nguoi_nhan,
    hd.dia_chi,
    hd.sdt_nguoi_nhan,
    hd.thanh_tien,
    ltt.ten_loai_trang_thai AS trang_thai,
    ts.mo_ta AS mo_ta_trang_thai
FROM 
    hoa_don hd
JOIN 
    trang_thai_hoa_don ts ON hd.Id_hoa_don = ts.id_hoa_don
JOIN 
    loai_trang_thai ltt ON ts.id_loai_trang_thai = ltt.Id_loai_trang_thai
WHERE 
    ltt.ten_loai_trang_thai IN (N'Hoàn thành', N'Chờ giao hàng', N'Chờ thanh toán', N'Chờ xác nhận')
ORDER BY 
    CASE 
        WHEN ltt.ten_loai_trang_thai = N'Hoàn thành' THEN 1
        WHEN ltt.ten_loai_trang_thai = N'Chờ giao hàng' THEN 2
        WHEN ltt.ten_loai_trang_thai = N'Chờ thanh toán' THEN 3
        WHEN ltt.ten_loai_trang_thai = N'Chờ xác nhận' THEN 4
        ELSE 5
    END;




	WITH LatestStatus AS (
    SELECT 
        hd.ma_hoa_don,
        hd.ten_nguoi_nhan,
        hd.dia_chi,
        hd.sdt_nguoi_nhan,
        hd.thanh_tien,
        ltt.ten_loai_trang_thai AS trang_thai,
        ts.mo_ta AS mo_ta_trang_thai,
        ROW_NUMBER() OVER (PARTITION BY hd.ma_hoa_don ORDER BY ts.ngay_cap_nhat DESC) AS rn
    FROM 
        hoa_don hd
    JOIN 
        trang_thai_hoa_don ts ON hd.Id_hoa_don = ts.id_hoa_don
    JOIN 
        loai_trang_thai ltt ON ts.id_loai_trang_thai = ltt.Id_loai_trang_thai
    WHERE 
        ltt.ten_loai_trang_thai IN (N'Hoàn thành', N'Chờ giao hàng', N'Chờ thanh toán', N'Chờ xác nhận')
)
SELECT 
    ma_hoa_don,
    ten_nguoi_nhan,
    dia_chi,
    sdt_nguoi_nhan,
    thanh_tien,
    trang_thai,
    mo_ta_trang_thai
FROM 
    LatestStatus
WHERE 
    rn = 1
ORDER BY 
    CASE 
        WHEN trang_thai = N'Hoàn thành' THEN 1
        WHEN trang_thai = N'Chờ giao hàng' THEN 2
        WHEN trang_thai = N'Chờ thanh toán' THEN 3
        WHEN trang_thai = N'Chờ xác nhận' THEN 4
        ELSE 5
    END;









SELECT 
    h.Id_hoa_don, 
    h.ma_hoa_don, 
    t.id_trang_thai_hoa_don, 
    t.mo_ta,
    t.ngay_tao
FROM 
    hoa_don h
JOIN 
    trang_thai_hoa_don t ON h.Id_hoa_don = t.id_hoa_don
WHERE 
    t.ngay_tao = (
        SELECT MAX(ngay_tao)
        FROM trang_thai_hoa_don
        WHERE id_hoa_don = h.Id_hoa_don)
ORDER BY 
    h.Id_hoa_don;



SELECT TOP 1 
    h.[Id_hoa_don], 
    h.[ma_hoa_don], 
    t.[mo_ta] AS [trang_thai_moi],
    t.[ngay_tao] AS [ngay_tao_trang_thai]
FROM 
    hoa_don h
JOIN 
    trang_thai_hoa_don t ON h.[Id_hoa_don] = t.[id_hoa_don]
WHERE 
    t.[ngay_tao] = (
        SELECT MAX([ngay_tao])
        FROM trang_thai_hoa_don
        WHERE [id_hoa_don] = h.[Id_hoa_don]
    )
ORDER BY 
    t.[ngay_tao] DESC;




WITH RankedStatus AS (
    SELECT 
        h.Id_hoa_don, 
        h.ma_hoa_don, 
		u.ten_nguoi_dung,
        t.ngay_tao,
        h.thanh_tien, 
        t.id_loai_trang_thai,
        l.ten_loai_trang_thai,
        ROW_NUMBER() OVER (PARTITION BY h.Id_hoa_don ORDER BY t.ngay_tao DESC) AS rn
    FROM 
        hoa_don h
    JOIN 
        trang_thai_hoa_don t ON h.Id_hoa_don = t.id_hoa_don
	JOIN 
        loai_trang_thai l ON l.id_loai_trang_thai = t.id_loai_trang_thai
	JOIN 
        nguoi_dung u ON h.id_nguoi_dung = u.id_nguoi_dung
)
SELECT 
    Id_hoa_don, 
    ma_hoa_don, 
	ten_nguoi_dung,
    ngay_tao,
	thanh_tien, 
	ten_loai_trang_thai
FROM 
    RankedStatus
WHERE 
    rn = 1
ORDER BY 
    Id_hoa_don;


	select * from hoa_don

SELECT 
    h.[ma_hoa_don], 
    tthd.[mo_ta] AS 'mo_ta_trang_thai_hoa_don',
    ltth.[ten_loai_trang_thai] AS 'ten_loai_trang_thai',
    tthd.[ngay_tao] AS 'ngay_tao_trang_thai',
    tthd.[ngay_cap_nhat] AS 'ngay_cap_nhat_trang_thai'
FROM 
    hoa_don h
JOIN 
    trang_thai_hoa_don tthd ON h.Id_hoa_don = tthd.id_hoa_don
JOIN 
    loai_trang_thai ltth ON tthd.id_loai_trang_thai = ltth.Id_loai_trang_thai
ORDER BY 
    h.[ma_hoa_don], tthd.[ngay_tao];




SELECT 
    h.[ma_hoa_don], 
    tthd.[mo_ta] ,
    ltth.[ten_loai_trang_thai] ,
    tthd.[ngay_tao] ,
    tthd.[ngay_cap_nhat]
FROM 
    hoa_don h
JOIN 
    trang_thai_hoa_don tthd ON h.Id_hoa_don = tthd.id_hoa_don
JOIN 
    loai_trang_thai ltth ON tthd.id_loai_trang_thai = ltth.Id_loai_trang_thai
WHERE 
    tthd.id_loai_trang_thai = 2  -- Thay đổi giá trị 2 bằng id_loai_trang_thai bạn cần
ORDER BY 
    h.[ma_hoa_don], tthd.[ngay_tao];





SELECT 
    h.ma_hoa_don, 
    tthd.mo_ta,
    ltth.id_loai_trang_thai,
    ltth.ten_loai_trang_thai,
    tthd.ngay_tao,
    tthd.ngay_cap_nhat
FROM 
    hoa_don h
JOIN 
    trang_thai_hoa_don tthd ON h.Id_hoa_don = tthd.id_hoa_don
JOIN 
    loai_trang_thai ltth ON tthd.id_loai_trang_thai = ltth.Id_loai_trang_thai
where h.Id_hoa_don =3  AND tthd.id_loai_trang_thai IN (1,2,3, 4,5, 6, 7, 8, 9)


SELECT 
    h.ma_hoa_don, 
    tthd.mo_ta,
    ltth.ten_loai_trang_thai,
    tthd.ngay_tao,
    tthd.ngay_cap_nhat
FROM 
    hoa_don h
JOIN 
    trang_thai_hoa_don tthd ON h.Id_hoa_don = tthd.id_hoa_don
JOIN 
    loai_trang_thai ltth ON tthd.id_loai_trang_thai = ltth.Id_loai_trang_thai
where h.Id_hoa_don = 1  AND tthd.id_loai_trang_thai IN (3, 5)



SELECT 
    h.[ma_hoa_don], 
    tthd.[mo_ta] AS 'mo_ta_trang_thai_hoa_don',
    ltth.[ten_loai_trang_thai] AS 'ten_loai_trang_thai',
    tthd.[ngay_tao] AS 'ngay_tao_trang_thai',
    tthd.[ngay_cap_nhat] AS 'ngay_cap_nhat_trang_thai'
FROM 
    hoa_don h
JOIN 
    trang_thai_hoa_don tthd ON h.Id_hoa_don = tthd.id_hoa_don
JOIN 
    loai_trang_thai ltth ON tthd.id_loai_trang_thai = ltth.Id_loai_trang_thai
WHERE 
    ltth.ten_loai_trang_thai IN 
    ('Chờ giao hàng', 
     'Chờ thanh toán', 
     'Chờ xác nhận', 
     'Đang vận chuyển', 
     'Hoàn thành', 
     'Đã hủy')
ORDER BY 
    h.[Id_hoa_don], tthd.[ngay_tao];




	select * from loai_trang_thai
	select * from trang_thai_hoa_don where id_hoa_don = 1 and id_loai_trang_thai = 8
	select * from hoa_don 



SELECT 
    hdct.Id_san_pham_chi_tiet, 
    sp.ma_san_pham ,
    hdct.so_luong, 
    k.ten_kich_thuoc,
    ms.ten_mau_sac ,
    cl.ten_chat_lieu ,
    spct.id_san_pham ,
    sp.mo_ta ,
    sp.ten_san_pham 
FROM 
    hoa_don_chi_tiet hdct
JOIN 
    san_pham_chi_tiet spct ON spct.id_san_pham_chi_tiet = hdct.id_san_pham_chi_tiet
LEFT JOIN 
    kich_thuoc k ON spct.id_kich_thuoc_chi_tiet = k.Id_kich_thuoc
LEFT JOIN 
    mau_sac ms ON spct.id_mau_sac_chi_tiet = ms.Id_mau_sac
LEFT JOIN 
    chat_lieu cl ON spct.id_chat_lieu_chi_tiet = cl.Id_chat_lieu
LEFT JOIN 
    san_pham sp ON spct.id_san_pham = sp.Id_san_pham
WHERE 
    hdct.id_hoa_don = 1;




WITH LatestStatus AS (
    SELECT 
       h.Id_hoa_don, 
        h.ma_hoa_don, 
		u.ten_nguoi_dung,
		u.sdt,
        h.thanh_tien, 
        h.loai,
        t.ngay_tao,
        l.ten_loai_trang_thai,
        ROW_NUMBER() OVER (PARTITION BY  h.Id_hoa_don ORDER BY h.id_trang_thai_hoa_don DESC) AS rn
     FROM 
        hoa_don h
    JOIN 
        trang_thai_hoa_don t ON h.Id_hoa_don = t.id_hoa_don
	JOIN 
        loai_trang_thai l ON l.id_loai_trang_thai = t.id_loai_trang_thai
	JOIN 
        nguoi_dung u ON h.id_nguoi_dung = u.id_nguoi_dung
)
SELECT 
    Id_hoa_don, 
    ma_hoa_don, 
	ten_nguoi_dung,
	sdt,
    ngay_tao,
	thanh_tien, 
	ten_loai_trang_thai
FROM 
        hoa_don h
    JOIN 
        trang_thai_hoa_don t ON h.Id_hoa_don = t.id_hoa_don
	JOIN 
        loai_trang_thai l ON l.id_loai_trang_thai = t.id_loai_trang_thai
	JOIN 
        nguoi_dung u ON h.id_nguoi_dung = u.id_nguoi_dung
WHERE rn = 1
ORDER BY h.id_hoa_don;




WITH RankedStatus AS (
    SELECT 
        h.Id_hoa_don, 
        h.ma_hoa_don, 
		u.ten_nguoi_dung,
        t.ngay_tao,
        h.thanh_tien, 
        t.id_loai_trang_thai,
        l.ten_loai_trang_thai,
        ROW_NUMBER() OVER (PARTITION BY h.Id_hoa_don ORDER BY t.ngay_tao DESC) AS rn
    FROM 
        hoa_don h
    JOIN 
        trang_thai_hoa_don t ON h.Id_hoa_don = t.id_hoa_don
	JOIN 
        loai_trang_thai l ON l.id_loai_trang_thai = t.id_loai_trang_thai
	JOIN 
        nguoi_dung u ON h.id_nguoi_dung = u.id_nguoi_dung
)
SELECT 
    Id_hoa_don, 
    ma_hoa_don, 
	ten_nguoi_dung,
    ngay_tao,
	thanh_tien, 
	ten_loai_trang_thai
FROM 
    RankedStatus
WHERE 
    rn = 1
ORDER BY 
    Id_hoa_don;




WITH LatestStatus AS (
    SELECT 
        h.Id_hoa_don, 
        h.ma_hoa_don, 
        u.ten_nguoi_dung,
        u.sdt,
        h.thanh_tien, 
        h.loai, 
        t.ngay_tao,
        l.ten_loai_trang_thai,
        ROW_NUMBER() OVER (PARTITION BY h.Id_hoa_don ORDER BY t.id_loai_trang_thai DESC) AS rn
    FROM 
        hoa_don h
    JOIN 
        trang_thai_hoa_don t ON h.Id_hoa_don = t.id_hoa_don
    JOIN 
        loai_trang_thai l ON l.id_loai_trang_thai = t.id_loai_trang_thai
    JOIN 
        nguoi_dung u ON h.id_nguoi_dung = u.id_nguoi_dung
)
SELECT 
    ls.Id_hoa_don, 
    ls.ma_hoa_don, 
    ls.ten_nguoi_dung,
    ls.sdt, 
    ls.thanh_tien, 
    ls.loai, 
    ls.ngay_tao,
    ls.ten_loai_trang_thai
FROM 
    LatestStatus ls
WHERE 
    ls.rn = 1
ORDER BY 
    ls.Id_hoa_don;


SELECT 
    sp.id_san_pham, 
    sp.ma_san_pham, 
    sp.ten_san_pham, 
    dc.ten_danh_muc, 
    sp.gia_ban,
    sp.mo_ta,
    sp.ngay_tao,
	dgg.ten_dot_giam_gia,
    ggsp.gia_khuyen_mai,
    dgg.gia_tri_giam_gia,
    dgg.kieu_giam_gia,
    dgg.ngay_bat_dau,
    dgg.ngay_ket_thuc
FROM 
    san_pham sp
LEFT JOIN 
    danh_muc dc ON sp.id_danh_muc = dc.id_danh_muc
LEFT JOIN
    giam_gia_san_pham ggsp ON sp.Id_san_pham = ggsp.id_san_pham
LEFT JOIN 
    dot_giam_gia dgg ON ggsp.id_dot_giam_gia = dgg.Id_dot_giam_gia
WHERE 
    sp.id_san_pham = 1;



	select * from loai_trang_thai
	select * from trang_thai_hoa_don
	select * from gio_hang
	select * from hoa_don

SELECT 
    spct.Id_san_pham_chi_tiet,
    sp.ma_san_pham,
    sp.ten_san_pham,
    sp.gia_ban,
    spct.so_luong,
    spct.trang_thai,
    sp.ten_san_pham,  -- Tên sản phẩm
    ktt.ten_kich_thuoc,  -- Kích thước
    mst.ten_mau_sac,  -- Màu sắc
    clt.ten_chat_lieu,  -- Chất liệu
    ghct.so_luong,
    ghct.don_gia,
    ghct.thanh_tien,
	ha.url_anh,
    ha.thu_tu
FROM 
    nguoi_dung nd
JOIN 
    gio_hang gh ON gh.id_nguoi_dung = nd.Id_nguoi_dung
JOIN 
    gio_hang_chi_tiet ghct ON ghct.id_gio_hang = gh.Id_gio_hang
JOIN 
    san_pham_chi_tiet spct ON spct.Id_san_pham_chi_tiet = ghct.id_san_pham_chi_tiet
JOIN 
    san_pham sp ON sp.Id_san_pham = spct.id_san_pham
LEFT JOIN 
    kich_thuoc_chi_tiet ktt_ct ON spct.id_kich_thuoc_chi_tiet = ktt_ct.Id_kich_thuoc_chi_tiet
LEFT JOIN 
    kich_thuoc ktt ON ktt_ct.id_kich_thuoc = ktt.Id_kich_thuoc
LEFT JOIN 
    mau_sac_chi_tiet mst_ct ON spct.id_mau_sac_chi_tiet = mst_ct.Id_mau_sac_chi_tiet
LEFT JOIN 
    mau_sac mst ON mst_ct.id_mau_sac = mst.Id_mau_sac
LEFT JOIN 
    chat_lieu_chi_tiet clt_ct ON spct.id_chat_lieu_chi_tiet = clt_ct.Id_chat_lieu_chi_tiet
LEFT JOIN 
    chat_lieu clt ON clt_ct.id_chat_lieu = clt.Id_chat_lieu
LEFT JOIN
    hinh_anh_san_pham ha ON sp.Id_san_pham = ha.id_san_pham
WHERE 
    nd.Id_nguoi_dung = 1 and ha.thu_tu = 1;  -- Thay @Id_nguoi_dung bằng id của người dùng bạn muốn truy vấn








SELECT sp.*
FROM SanPham sp
LEFT JOIN DanhMuc dm ON sp.danhMucId = dm.id
LEFT JOIN GiamGiaSanPham ggsp ON sp.id = ggsp.sanPhamId
LEFT JOIN DotGiamGia dgg ON ggsp.dotGiamGiaId = dgg.id
LEFT JOIN HinhAnhSanPham ha ON sp.id = ha.sanPhamId
WHERE sp.trangThai = TRUE
  AND ha.thuTu = 1
  AND (dgg.ngayKetThuc >= CURRENT_DATE OR dgg.ngayKetThuc IS NULL)
ORDER BY 
  CASE WHEN ggsp.idVoucherSanPham IS NOT NULL THEN 0 ELSE 1 END,
  sp.tenSanPham ASC,
  sp.giaBan ASC;














  select * from hoa_don
  select * from hoa_don_chi_tiet

SELECT 
    spct.Id_san_pham_chi_tiet,
    ghct.so_luong,
    spct.trang_thai,
    sp.ma_san_pham,
    sp.ten_san_pham,
    sp.gia_ban,
    ms.ten_mau_sac,
    kt.ten_kich_thuoc,
    cl.ten_chat_lieu,
	 ggs.gia_khuyen_mai,
            dgg.gia_tri_giam_gia,
          dgg.kieu_giam_gia,
              dgg.ten_dot_giam_gia,
             dgg.ngay_bat_dau,
           dgg.ngay_ket_thuc,
FROM 
    hoa_don h
INNER JOIN 
    hoa_don_chi_tiet ghct ON h.Id_hoa_don = ghct.Id_hoa_don
INNER JOIN 
    san_pham_chi_tiet spct ON ghct.id_san_pham_chi_tiet = spct.Id_san_pham_chi_tiet
INNER JOIN 
    san_pham sp ON spct.id_san_pham = sp.Id_san_pham
LEFT JOIN 
    mau_sac_chi_tiet msc ON spct.id_mau_sac_chi_tiet = msc.Id_mau_sac_chi_tiet
LEFT JOIN 
    mau_sac ms ON ms.Id_mau_sac = msc.id_mau_sac
LEFT JOIN 
    kich_thuoc_chi_tiet ktc ON spct.id_kich_thuoc_chi_tiet = ktc.Id_kich_thuoc_chi_tiet
LEFT JOIN 
    kich_thuoc kt ON kt.Id_kich_thuoc = ktc.id_kich_thuoc
LEFT JOIN 
    chat_lieu_chi_tiet clc ON spct.id_chat_lieu_chi_tiet = clc.Id_chat_lieu_chi_tiet
LEFT JOIN 
    chat_lieu cl ON cl.Id_chat_lieu = clc.id_chat_lieu
WHERE 
    h.Id_hoa_don = 17;  -- Thay @id_hoa_don bằng ID của hóa đơn bạn muốn truy vấn






	select * from hoa_don
	select * from hoa_don_chi_tiet
	select * from dot_giam_gia
	select * from giam_gia_san_pham
	select * from voucher
	select * from giam_gia_san_pham


SELECT 
    v.ma_voucher,
    v.ten_voucher,
    v.gia_tri_giam_gia,
    v.kieu_giam_gia
FROM 
    hoa_don h
LEFT JOIN 
   voucher v on v.Id_voucher = h.id_voucher
WHERE 
    h.Id_hoa_don = 1;  -- Thay @id_hoa_don bằng ID của hóa đơn bạn muốn truy vấn






















WITH LatestStatus AS (
    SELECT 
        h.Id_hoa_don, 
        h.ma_hoa_don, 
        u.ten_nguoi_dung,
        u.sdt,
        h.thanh_tien, 
        h.loai, 
        t.ngay_tao,
        l.ten_loai_trang_thai,
        ROW_NUMBER() OVER (PARTITION BY h.Id_hoa_don ORDER BY t.id_loai_trang_thai DESC) AS rn
    FROM 
        hoa_don h
    JOIN 
        trang_thai_hoa_don t ON h.Id_hoa_don = t.id_hoa_don
    JOIN 
        loai_trang_thai l ON l.id_loai_trang_thai = t.id_loai_trang_thai
    JOIN 
        nguoi_dung u ON h.id_nguoi_dung = u.id_nguoi_dung
)
SELECT 
    ls.Id_hoa_don, 
    ls.ma_hoa_don, 
    ls.ten_nguoi_dung,
    ls.sdt, 
    ls.thanh_tien, 
    ls.loai, 
    ls.ngay_tao,
    ls.ten_loai_trang_thai
FROM 
    LatestStatus ls
WHERE 
    ls.rn = 1
ORDER BY 
    ls.Id_hoa_don;









WITH LatestStatus AS (
    SELECT 
        h.Id_hoa_don, 
        h.ma_hoa_don, 
        u.ten_nguoi_dung,
        u.sdt,
        h.thanh_tien, 
        h.loai, 
        t.ngay_tao,
        l.ten_loai_trang_thai,
        ROW_NUMBER() OVER (PARTITION BY h.Id_hoa_don ORDER BY t.id_loai_trang_thai DESC) AS rn
    FROM 
        hoa_don h
    JOIN 
        trang_thai_hoa_don t ON h.Id_hoa_don = t.id_hoa_don
    JOIN 
        loai_trang_thai l ON l.id_loai_trang_thai = t.id_loai_trang_thai
    JOIN 
        nguoi_dung u ON h.id_nguoi_dung = u.id_nguoi_dung
)
SELECT 
    ls.Id_hoa_don, 
    ls.ma_hoa_don, 
    ls.ten_nguoi_dung,
    ls.sdt, 
    ls.thanh_tien, 
    ls.loai, 
    ls.ngay_tao,
    ls.ten_loai_trang_thai
FROM 
    LatestStatus ls
WHERE 
    ls.rn = 1
ORDER BY 
    ls.ngay_tao DESC,   
    ls.Id_hoa_don;    




SELECT 
    h.ma_hoa_don, 
    tthd.mo_ta,
    ltth.id_loai_trang_thai,
    ltth.ten_loai_trang_thai,
    tthd.ngay_tao,
    tthd.ngay_cap_nhat
FROM 
    hoa_don h
JOIN 
    trang_thai_hoa_don tthd ON h.Id_hoa_don = tthd.id_hoa_don
JOIN 
    loai_trang_thai ltth ON tthd.id_loai_trang_thai = ltth.Id_loai_trang_thai
WHERE 
    h.Id_hoa_don = 1
ORDER BY 
    tthd.ngay_tao DESC;  -- Sắp xếp theo ngày tạo của trạng thái hóa đơn, hóa đơn mới nhất ở trên cùng

	select * from hoa_don
	select * from hoa_don_chi_tiet
	select * from trang_thai_hoa_don
	select * from trang_thai_giam_gia
	select * from trang_thai_giam_gia
select * from voucher
select * from nguoi_dung
select * from hoa_don
select * from hoa_don_chi_tiet
select * from san_pham_chi_tiet


SELECT * 
FROM voucher
WHERE ma_voucher LIKE 'f%';
SELECT * 
FROM san_pham
WHERE ten_san_pham LIKE 'a%';








SELECT 
    h.Id_hoa_don,
    h.ma_hoa_don,
    h.id_nguoi_dung,
    h.id_nhan_vien,
    h.ten_nguoi_nhan,
    h.phi_ship,
    h.thanh_tien,
    h.ngay_tao,
    h.ngay_cap_nhat,
    h.mo_ta,
    h.trang_thai,
    h.ngay_thanh_toan,
    h.id_pt_thanh_toan_hoa_don,
    nv.ten_nguoi_dung AS ten_nhan_vien
FROM 
    hoa_don h
JOIN 
    nguoi_dung nv ON h.id_nhan_vien = nv.id_nguoi_dung
WHERE 
    h.id_nhan_vien = 1;  -- Chỉ lấy hóa đơn của khách hàng với id_nguoi_dung = 2

SELECT 
    nguoi_dung.ten_nguoi_dung AS Ten_Nguoi_Dung,
    nv.ten_nguoi_dung AS Ten_Nhan_Vien
FROM 
    hoa_don hd
JOIN 
    nguoi_dung nguoi_dung ON hd.id_nguoi_dung = nguoi_dung.Id_nguoi_dung
JOIN 
    nguoi_dung nv ON hd.id_nhan_vien = nv.Id_nguoi_dung;



	select* from loai_trang_thai	
	select* from trang_thai_hoa_don	
	select* from lich_su_thanh_toan	
	select* from hoa_don	
	select* from lich_su_thanh_toan	
	select* from lich_su_thanh_toan	














