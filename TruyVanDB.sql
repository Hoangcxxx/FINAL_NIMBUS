USE testdatn2;
GO
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
    spct.so_luong
ORDER BY 
    sp.Id_san_pham ASC;  -- Sắp xếp theo idSanPham từ nhỏ đến lớn






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
    sp.Id_san_pham = 1 
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

/* Lấy ra danh sách sản phẩm chi tiết mới nhất */






