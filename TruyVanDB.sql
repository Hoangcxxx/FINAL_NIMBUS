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




-- Thêm dữ liệu vào bảng tạm
INSERT INTO kho_hang (Id_san_pham_chi_tiet, so_luong_them)
VALUES
    (1, 5),  -- Cập nhật sản phẩm chi tiết với ID 1, thêm 5 sản phẩm
    (2, 10); -- Cập nhật sản phẩm chi tiết với ID 2, thêm 10 sản phẩm

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



