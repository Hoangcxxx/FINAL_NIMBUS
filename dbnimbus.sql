CREATE DATABASE testdatn2;
GO

USE testdatn2;
GO
CREATE TABLE [vai_tro] (
  [Id_vai_tro] INT PRIMARY KEY IDENTITY(1,1),
  [ten] NVARCHAR(100) NOT NULL,
  [mo_ta] NVARCHAR(MAX),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE()
);
go
CREATE TABLE [nguoi_dung] (
  [Id_nguoi_dung] INT PRIMARY KEY IDENTITY(1,1),
  [ten_nguoi_dung] NVARCHAR(100) NOT NULL,
  [ma_nguoi_dung] NVARCHAR(50) NOT NULL UNIQUE,
  [Email] NVARCHAR(255) NOT NULL UNIQUE,
  [sdt_nguoi_dung] NVARCHAR(15),
  [Ngay_Sinh] DATE,
  [Dia_Chi] NVARCHAR(255),
  [Gioi_Tinh] NVARCHAR(10),
  [Mat_Khau] NVARCHAR(255) NOT NULL,
  [Anh_Dai_Dien] NVARCHAR(255),
  [Trang_thai] BIT DEFAULT 1,
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [id_vai_tro] INT,
  CONSTRAINT [FK_nguoi_dung_id_vai_tro]
    FOREIGN KEY ([id_vai_tro])
      REFERENCES [vai_tro]([Id_vai_tro])
);
-- Insert data for vai_tro

CREATE TABLE [voucher] (
  [Id_voucher] INT PRIMARY KEY IDENTITY(1,1),
  [ma_voucher] NVARCHAR(50) NOT NULL UNIQUE,
  [phan_tram_giam] DECIMAL(5),
  [so_luong] INT,
  [trang_thai] BIT DEFAULT 1,
  [mo_ta] NVARCHAR(MAX),
  [ngay_bat_dau] DATETIME,
  [ngay_ket_thuc] DATETIME,
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE()
);
go
CREATE TABLE [loai_voucher] (
  [Id_loai_voucher] INT PRIMARY KEY IDENTITY(1,1),
  [ten_loai_voucher] NVARCHAR(100) NOT NULL,
  [mo_ta] NVARCHAR(MAX),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [id_voucher] INT,
  CONSTRAINT [FK_loai_voucher_id_voucher]
    FOREIGN KEY ([id_voucher])
      REFERENCES [voucher]([Id_voucher])
);
go
CREATE TABLE [danh_muc] (
  [Id_danh_muc] INT PRIMARY KEY IDENTITY(1,1),
  [ten_danh_muc] NVARCHAR(100) NOT NULL,
  [mo_ta] NVARCHAR(MAX),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE()
);
go
CREATE TABLE [san_pham] (
  [Id_san_pham] INT PRIMARY KEY IDENTITY(1,1),
  [ten_san_pham] NVARCHAR(100) NOT NULL,
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [mo_ta] NVARCHAR(MAX),
  [Trang_thai] BIT DEFAULT 1,
  [id_danh_muc] INT,
  [id_loai_voucher] INT,
  CONSTRAINT [FK_san_pham_id_loai_voucher]
    FOREIGN KEY ([id_loai_voucher])
      REFERENCES [loai_voucher]([Id_loai_voucher]),
  CONSTRAINT [FK_san_pham_id_danh_muc]
    FOREIGN KEY ([id_danh_muc])
      REFERENCES [danh_muc]([Id_danh_muc])
);
go
CREATE TABLE [danh_gia] (
  [Id_danh_gia] INT PRIMARY KEY IDENTITY(1,1),
  [id_nguoi_dung] INT,
  [id_san_pham] INT,
  [noi_dung] NVARCHAR(MAX),
  [diem] INT CHECK (diem >= 1 AND diem <= 5),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  CONSTRAINT [FK_danh_gia_id_nguoi_dung]
    FOREIGN KEY ([id_nguoi_dung])
      REFERENCES [nguoi_dung]([Id_nguoi_dung]),
  CONSTRAINT [FK_danh_gia_id_san_pham]
    FOREIGN KEY ([id_san_pham])
      REFERENCES [san_pham]([Id_san_pham])
);
go
CREATE TABLE [chat_lieu] (
  [Id_chat_lieu] INT PRIMARY KEY IDENTITY(1,1),
  [ten_chat_lieu] NVARCHAR(100) NOT NULL,
  [mo_ta] NVARCHAR(MAX),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE()
);
go
CREATE TABLE [chat_lieu_chi_tiet] (
  [Id_chat_lieu_tiet] INT PRIMARY KEY IDENTITY(1,1),
  [id_chat_lieu] INT,
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  CONSTRAINT [FK_chat_lieu_chi_tiet_id_chat_lieu]
    FOREIGN KEY ([id_chat_lieu])
      REFERENCES [chat_lieu]([Id_chat_lieu])
);
go
CREATE TABLE [kich_thuoc] (
  [Id_kich_thuoc] INT PRIMARY KEY IDENTITY(1,1),
  [ten_kich_thuoc] NVARCHAR(100) NOT NULL,
  [mo_ta] NVARCHAR(MAX),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE()
);
go
CREATE TABLE [kich_thuoc_chi_tiet] (
  [Id_kich_thuoc_chi_tiet] INT PRIMARY KEY IDENTITY(1,1),
  [id_kich_thuoc] INT,
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  CONSTRAINT [FK_kich_thuoc_chi_tiet_id_kich_thuoc]
    FOREIGN KEY ([id_kich_thuoc])
      REFERENCES [kich_thuoc]([Id_kich_thuoc])
);
go
CREATE TABLE [mau_sac] (
  [Id_mau_sac] INT PRIMARY KEY IDENTITY(1,1),
  [ten_mau_sac] NVARCHAR(100) NOT NULL,
  [mo_ta] NVARCHAR(MAX),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE()
);
go
CREATE TABLE [mau_sac_chi_tiet] (
  [Id_mau_sac_chi_tiet] INT PRIMARY KEY IDENTITY(1,1),
  [id_mau_sac] INT,
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  CONSTRAINT [FK_mau_sac_chi_tiet_id_mau_sac]
    FOREIGN KEY ([id_mau_sac])
      REFERENCES [mau_sac]([Id_mau_sac])
);
go
CREATE TABLE [gio_hang] (
    [Id_gio_hang] INT PRIMARY KEY IDENTITY(1,1),
    [id_nguoi_dung] INT,
    [ngay_tao] DATETIME DEFAULT GETDATE(),
    [trang_thai] BIT DEFAULT 1,
    [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
    FOREIGN KEY ([id_nguoi_dung]) REFERENCES nguoi_dung(Id_nguoi_dung)
);
go
CREATE TABLE [gio_hang_chi_tiet] (
  [Id_gio_hang_chi_tiet] INT PRIMARY KEY IDENTITY(1,1),
  [id_gio_hang] INT,
  [so_luong] INT,
  [don_gia] DECIMAL(18),
  [thanh_tien] DECIMAL(18),
  [Trang_Thai] BIT DEFAULT 1,
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  CONSTRAINT [FK_gio_hang_chi_tiet_id_gio_hang]
    FOREIGN KEY ([id_gio_hang])
      REFERENCES [gio_hang]([Id_gio_hang])
);
go
CREATE TABLE [san_pham_chi_tiet] (
  [Id_san_pham_chi_tiet] INT PRIMARY KEY IDENTITY(1,1),
  [so_luong] INT NULL,
  [gia_ban] DECIMAL(18),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [trang_thai] BIT DEFAULT 1,
  [id_kich_thuoc_chi_tiet] INT,
  [id_mau_sac_chi_tiet] INT,
  [id_chat_lieu_chi_tiet] INT,
  [id_gio_hang_chi_tiet] INT,
  [id_san_pham] INT,
  CONSTRAINT [FK_san_pham_chi_tiet_id_chat_lieu_chi_tiet]
    FOREIGN KEY ([id_chat_lieu_chi_tiet])
      REFERENCES [chat_lieu_chi_tiet]([Id_chat_lieu_tiet]),
  CONSTRAINT [FK_san_pham_chi_tiet_id_kich_thuoc_chi_tiet]
    FOREIGN KEY ([id_kich_thuoc_chi_tiet])
      REFERENCES [kich_thuoc_chi_tiet]([Id_kich_thuoc_chi_tiet]),
  CONSTRAINT [FK_san_pham_chi_tiet_id_san_pham]
    FOREIGN KEY ([id_san_pham])
      REFERENCES [san_pham]([Id_san_pham]),
  CONSTRAINT [FK_san_pham_chi_tiet_id_mau_sac_chi_tiet]
    FOREIGN KEY ([id_mau_sac_chi_tiet])
      REFERENCES [mau_sac_chi_tiet]([Id_mau_sac_chi_tiet]),
  CONSTRAINT [FK_san_pham_chi_tiet_id_gio_hang_chi_tiet]
    FOREIGN KEY ([id_gio_hang_chi_tiet])
      REFERENCES [gio_hang_chi_tiet]([Id_gio_hang_chi_tiet])
);
go
CREATE TABLE [phi_van_chuyen] (
  [Id_phi_van_chuyen] INT PRIMARY KEY IDENTITY(1,1),
  [so_tien_van_chuyen] DECIMAL(18),
  [mo_ta] NVARCHAR(MAX),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE()
);
go
CREATE TABLE [dia_chi_van_chuyen] (
  [Id_dia_chi_van_chuyen] INT PRIMARY KEY IDENTITY(1,1),
  [id_phi_van_chuyen] INT,
  [tinh] NVARCHAR(100),
  [huyen] NVARCHAR(100),
  [xa] NVARCHAR(100),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [mo_ta] NVARCHAR(MAX),
  CONSTRAINT [FK_dia_chi_van_chuyen_id_phi_van_chuyen]
    FOREIGN KEY ([id_phi_van_chuyen])
      REFERENCES [phi_van_chuyen]([Id_phi_van_chuyen])
);
go
CREATE TABLE [pt_thanh_toan] (
  [Id_pt_thanh_toan] INT PRIMARY KEY IDENTITY(1,1),
  [ma_thanh_toan] NVARCHAR(50) NOT NULL UNIQUE,
  [ten_phuong_thuc] NVARCHAR(100) NOT NULL,
  [noi_dung_thanh_toan] NVARCHAR(MAX),
  [ngay_thanh_toan] DATETIME,
  [trang_thai_thanh_toan] BIT DEFAULT 1,
  [phi_giao_dich] DECIMAL(18),
  [thong_tin_them] NVARCHAR(MAX)
);
go
CREATE TABLE [pt_thanh_toan_hoa_don] (
  [Id_thanh_toan_hoa_don] INT PRIMARY KEY IDENTITY(1,1),
  [id_pt_thanh_toan] INT,
  [so_tien_thanh_toan] DECIMAL(18),
  [ngay_giao_dich] DATETIME,
  [ghi_chu] NVARCHAR(MAX),
  [id_giao_dich_thanh_toan] NVARCHAR(50),
  CONSTRAINT [FK_pt_thanh_toan_hoa_don_id_pt_thanh_toan]
    FOREIGN KEY ([id_pt_thanh_toan])
      REFERENCES [pt_thanh_toan]([Id_pt_thanh_toan])
);
go
CREATE TABLE [trang_thai_hoa_don] (
  [Id_trang_thai_hoa_don] INT PRIMARY KEY IDENTITY(1,1),
  [ten_trang_thai] NVARCHAR(100) NOT NULL,
  [mo_ta] NVARCHAR(MAX),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE()
);
go
CREATE TABLE [hoa_don] (
  [Id_hoa_don] INT PRIMARY KEY IDENTITY(1,1),
  [ma_hoa_don] NVARCHAR(50) NOT NULL UNIQUE,
  [id_nguoi_dung] INT,
  [id_loai_voucher] INT,
  [Id_dia_chi_van_chuyen] INT,
  [id_trang_thai_hoa_don] INT,
  [ten_nguoi_nhan] NVARCHAR(100) NOT NULL,
  [phi_ship] DECIMAL(18),
  [dia_chi] NVARCHAR(255) NOT NULL,
  [sdt_nguoi_nhan] NVARCHAR(15),
  [thanh_tien] DECIMAL(18),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [mo_ta] NVARCHAR(MAX),
  [trang_thai] BIT DEFAULT 1,
  [ngay_thanh_toan] DATETIME,
  [id_pt_thanh_toan_hoa_don] INT,
  CONSTRAINT [FK_hoa_don_id_nguoi_dung]
    FOREIGN KEY ([id_nguoi_dung])
      REFERENCES [nguoi_dung]([Id_nguoi_dung]),
  CONSTRAINT [FK_hoa_don_Id_dia_chi_van_chuyen]
    FOREIGN KEY ([Id_dia_chi_van_chuyen])
      REFERENCES [dia_chi_van_chuyen]([Id_dia_chi_van_chuyen]),
  CONSTRAINT [FK_hoa_don_id_pt_thanh_toan_hoa_don]
    FOREIGN KEY ([id_pt_thanh_toan_hoa_don])
      REFERENCES [pt_thanh_toan_hoa_don]([Id_thanh_toan_hoa_don]),
  CONSTRAINT [FK_hoa_don_id_trang_thai_hoa_don]
    FOREIGN KEY ([id_trang_thai_hoa_don])
      REFERENCES [trang_thai_hoa_don]([Id_trang_thai_hoa_don]),
  CONSTRAINT [FK_hoa_don_id_loai_voucher]
    FOREIGN KEY ([id_loai_voucher])
      REFERENCES [loai_voucher]([Id_loai_voucher])
);

go
CREATE TABLE [hinh_anh_san_pham] (
  [Id_hinh_anh_san_pham] INT PRIMARY KEY IDENTITY(1,1),
  [id_san_pham] INT,
  [url_anh] NVARCHAR(255) NOT NULL,
  [mo_ta] NVARCHAR(MAX),
  [trang_thai] BIT DEFAULT 1,
  [thu_tu] INT,
  [loai_hinh_anh] NVARCHAR(50),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  CONSTRAINT [FK_hinh_anh_san_pham_id_san_pham]
    FOREIGN KEY ([id_san_pham])
      REFERENCES [san_pham]([Id_san_pham])
);
go
CREATE TABLE [xac_thuc] (
  [Id_xac_thuc] INT PRIMARY KEY IDENTITY(1,1),
  [ma_xac_thuc] NVARCHAR(50) NOT NULL UNIQUE,
  [id_nguoi_dung] INT,
  [mo_ta] NVARCHAR(MAX),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  CONSTRAINT [FK_xac_thuc_id_nguoi_dung]
    FOREIGN KEY ([id_nguoi_dung])
      REFERENCES [nguoi_dung]([Id_nguoi_dung])
);
go
CREATE TABLE [lich_su_hoa_don] (
  [Id_lich_su_hoa_don] INT PRIMARY KEY IDENTITY(1,1),
  [so_tien_thanh_toan] DECIMAL(18),
  [ngay_giao_dich] DATETIME,
  [id_nguoi_dung] INT,
  CONSTRAINT [FK_lich_su_hoa_don_id_nguoi_dung]
    FOREIGN KEY ([id_nguoi_dung])
      REFERENCES [nguoi_dung]([Id_nguoi_dung])
);
go

CREATE TABLE [hoa_don_chi_tiet] (
  [Id_hoa_don_chi_tiet] INT PRIMARY KEY IDENTITY(1,1),
  [id_san_pham_chi_tiet] INT,
  [id_lich_su_hoa_don] INT,
  [id_hoa_don] INT,
  [so_luong] INT NOT NULL,
  [tong_tien] DECIMAL(18),
  [so_tien_thanh_toan] DECIMAL(18),
  [tien_tra_lai] DECIMAL(18),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [trang_thai] BIT DEFAULT 1,
  [mo_ta] NVARCHAR(MAX),
  CONSTRAINT [FK_hoa_don_chi_tiet_id_hoa_don]
    FOREIGN KEY ([id_hoa_don])
      REFERENCES [hoa_don]([Id_hoa_don]),
  CONSTRAINT [FK_hoa_don_chi_tiet_id_san_pham_chi_tiet]
    FOREIGN KEY ([id_san_pham_chi_tiet])
      REFERENCES [san_pham_chi_tiet]([Id_san_pham_chi_tiet]),
  CONSTRAINT [FK_hoa_don_chi_tiet_id_lich_su_hoa_don]
    FOREIGN KEY ([id_lich_su_hoa_don])
      REFERENCES [lich_su_hoa_don]([id_lich_su_hoa_don])
);
go
INSERT INTO vai_tro (ten, mo_ta) VALUES 
(N'Quản trị viên', N'Người quản lý toàn bộ hệ thống'),
(N'Khách hàng', N'Người mua hàng trên website'),
(N'Nhân viên bán hàng', N'Nhân viên hỗ trợ bán hàng'),
(N'Nhân viên giao hàng', N'Người giao hàng đến tay khách hàng'),
(N'Quản lý kho', N'Người quản lý tồn kho');
go
-- Insert data for nguoi_dung
INSERT INTO nguoi_dung (ten_nguoi_dung, ma_nguoi_dung, Email, sdt_nguoi_dung, Ngay_Sinh, Dia_Chi, Gioi_Tinh, Mat_Khau,id_vai_tro) VALUES 
(N'Phạm Thùy Dương', 'user001', 'duongpt@gmail.com', '0918829273', '2004-01-02', N'Hà Nội', N'Nữ', '123456',1),
(N'Lê Khả Hoàng', 'user002', 'hoanglk@gmail.com', '0912353678', '2004-01-03', N'Hà Nội', N'Nam', '123456',2),
(N'Nguyễn Trung Hiếu', 'user003', 'hieunt@gmail.com', '0916789535', '2004-01-04', N'Hà Nội', 'Nam', '123456',3),
(N'Lê Đình Linh', 'user004', 'linhld@gmail.com', '0912679346', '2004-01-05', N'Hà Nội', N'Nam', '123456',4),
(N'Hoàng Văn Hà', 'user005', 'hahv@gmail.com', '0918934754', '2004-01-06', N'Hà Nội', N'Nam', '123456',5);
go
-- Insert data for voucher
INSERT INTO voucher (ma_voucher, phan_tram_giam, so_luong, mo_ta) VALUES 
(N'VOUCHER1', 10, 100, N'Giảm 10% cho đơn hàng trên 500k'),
(N'VOUCHER2', 15, 50, N'Giảm 15% cho khách hàng mới'),
(N'VOUCHER3', 20, 30, N'Giảm 20% vào dịp sinh nhật'),
(N'VOUCHER4', 5, 200, N'Giảm 5% cho tất cả sản phẩm'),
(N'VOUCHER5', 25, 10, N'Giảm 25% cho đơn hàng trên 1 triệu');
go
-- Insert data for loai_voucher
INSERT INTO loai_voucher (ten_loai_voucher,mo_ta,id_voucher) VALUES 
(N'Voucher Giảm Giá 10%', N'Áp dụng cho đơn hàng trên 500k', 1),
(N'Voucher Khách Hàng Mới', N'Chỉ dành cho khách hàng mới', 2),
(N'Voucher Sinh Nhật', N'Giảm giá đặc biệt vào dịp sinh nhật',3),
(N'Voucher Toàn Sản Phẩm', N'Giảm giá cho tất cả sản phẩm',4),
(N'Voucher Đơn Hàng Lớn', N'Áp dụng cho đơn hàng trên 1 triệu',5);
go
-- Insert data for danh_muc
INSERT INTO danh_muc (ten_danh_muc, mo_ta) VALUES 
(N'Áo phông', N'Áo phông đa dạng kiểu dành cho nam nữ'),
(N'Áo sơ mi', N'Áo sơ mi đa dạng kiểu dành cho nam nữ'),
(N'Áo phao', N'Áo phao đa dạng kiểu dành cho nam nữ'),
(N'Áo chống nắng', N'Áo chống nắng dành cho nam nữ'),
(N'Quần kaki', N'Quần kaki đa dạng kiểu dành cho nam nữ'),
(N'Quần sort & jean', N'Quần sort và jean đa dạng dành cho nam nữ');
go


-- Insert data for lich_su_hoa_don
INSERT INTO lich_su_hoa_don (so_tien_thanh_toan, id_nguoi_dung) VALUES 
(500, 4),
(750, 2),
(300, 1),
(1200, 3),
(150, 5);
go

-- Insert data for chat_lieu
INSERT INTO chat_lieu (ten_chat_lieu, mo_ta) VALUES 
(N'Cotton', N'Vải cotton thoáng khí, mềm mại, thích hợp cho áo phông và áo sơ mi.'),
(N'Polyester', N'Vải polyester bền, ít nhăn, thích hợp cho áo phao và áo chống nắng.'),
(N'Linen', N'Vải linen thoáng khí, thường dùng cho áo sơ mi vào mùa hè.'),
(N'Tencel', N'Vải Tencel mịn màng, thoải mái, phù hợp cho áo sơ mi.'),
(N'Nylon', N'Vải nylon nhẹ, bền, thường được dùng cho áo phao và áo chống nắng.'),
(N'Denim', N'Vải denim dày dạn, dùng cho quần kaki,sort và jean.');
go
-- Insert data for chat_lieu_chi_tiet
INSERT INTO chat_lieu_chi_tiet (id_chat_lieu) VALUES 
(1),
(2),
(3),
(4),
(5);
go
-- Insert data for kich_thuoc
INSERT INTO kich_thuoc (ten_kich_thuoc, mo_ta) VALUES 
('S', N'Kích thước nhỏ'),
('M', N'Kích thước vừa'),
('L', N'Kích thước lớn'),
('XL', N'Kích thước rất lớn'),
('XXL', N'Kích thước cực lớn');
go
-- Insert data for kich_thuoc_chi_tiet
INSERT INTO kich_thuoc_chi_tiet (id_kich_thuoc) VALUES 
(1),
(2),
(3),
(4),
(5);
go
-- Insert data for mau_sac
INSERT INTO mau_sac (ten_mau_sac, mo_ta) VALUES 
(N'Đen', N'Màu đen cổ điển, dễ phối với nhiều loại trang phục.'),
(N'Trắng', N'Màu trắng tinh khiết, phù hợp cho mọi dịp.'),
(N'Xanh dương', N'Màu xanh dương, mang đến cảm giác tươi mát và trẻ trung.'),
(N'Xanh lá', N'Màu xanh lá cây, biểu trưng cho thiên nhiên và sự tươi mát.'),
(N'Xanh rêu', N'Màu xanh rêu cổ điển, phù hợp cho nhiều loại trang phục.'),
(N'Đỏ', N'Màu đỏ nổi bật, thể hiện sự năng động và cá tính.'),
(N'Vàng', N'Màu vàng tươi sáng, mang lại sự ấm áp và vui tươi.'),
(N'Hồng', N'Màu hồng nhẹ nhàng, phù hợp cho những ai yêu thích sự nữ tính.'),
(N'Nâu', N'Màu nâu ấm áp, thường được dùng trong thời trang thu đông.'),
(N'Xám', N'Màu xám trung tính, dễ dàng kết hợp với các màu khác.'),
(N'Xanh than', N'Màu xanh than, mang lại vẻ lịch lãm và sang trọng.'),
(N'Be', N'Màu be nhẹ nhàng, phù hợp cho trang phục hàng ngày.'),
(N'Tím', N'Màu tím đằm thắm, mang lại sự mông mơ.'),
(N'Ghi', N'Màu ghi là màu trung tính giữa đen và trắng, thể hiện cân bằng và trang trọng.');
go
-- Insert data for mau_sac_chi_tiet
INSERT INTO mau_sac_chi_tiet (id_mau_sac) VALUES 
(1),
(2),
(3),
(4),
(5),
(6),
(7),
(8),
(9),
(10),
(11),
(12),
(13),
(14);


go
-- Insert data for gio_hang
INSERT INTO gio_hang (id_nguoi_dung) VALUES 
(1), (2), (3), (4), (5);
go

-- Insert data for gio_hang_chi_tiet
INSERT INTO gio_hang_chi_tiet (id_gio_hang, so_luong, don_gia, thanh_tien) VALUES 
(1, 1, 200000, 200000),
(2, 2, 300000, 600000),
(3, 1, 500000, 500000),
(4, 1, 100000, 100000),
(5, 1, 1500000, 1500000);

go
-- Insert data for phi_van_chuyen
INSERT INTO phi_van_chuyen (so_tien_van_chuyen, mo_ta) VALUES 
(30000, N'Phí vận chuyển cho đơn hàng nội tỉnh.'),
(50000, N'Phí vận chuyển cho đơn hàng liên tỉnh.'),
(20000, N'Phí vận chuyển cho đơn hàng dưới 1kg.'),
(10000, N'Phí vận chuyển cho đơn hàng trên 1kg.'),
(100000, N'Phí vận chuyển cho đơn hàng đặc biệt.');
go

-- Insert data for dia_chi_van_chuyen
INSERT INTO dia_chi_van_chuyen (id_phi_van_chuyen, tinh, huyen, xa, mo_ta) VALUES 
(1, N'Hà Nội', N'Hoàn Kiếm', N'Phan Chu Trinh', N'Địa chỉ giao hàng tại Hà Nội.'),
(2, N'TP. Hồ Chí Minh', N'Quận 1', N'Nguyễn Thái Bình', N'Địa chỉ giao hàng tại TP. Hồ Chí Minh.'),
(3, N'Hà Nội', N'Ba Đình', N'Trần Phú', N'Địa chỉ giao hàng cho các đơn hàng nhỏ.'),
(4, N'Đà Nẵng', N'Hải Châu', N'Hải Châu 1', N'Địa chỉ giao hàng tại Đà Nẵng.'),
(5, N'Bắc Ninh', N'Thuận Thành', N'Thị trấn Hồ', N'Địa chỉ giao hàng tại Bắc Ninh.');
go

-- Insert data for pt_thanh_toan
INSERT INTO pt_thanh_toan (ma_thanh_toan, ten_phuong_thuc, noi_dung_thanh_toan, phi_giao_dich, thong_tin_them) VALUES 
(N'TT001', N'Transfer Ngân Hàng', N'Transfer qua ngân hàng cho đơn hàng.', 0, N'Không có phí.'),
(N'TT002', N'Thanh Toán Qua Thẻ', N'Sử dụng thẻ tín dụng để thanh toán.', 1, N'Phí giao dịch thẻ tín dụng.'),
(N'TT003', N'Ví Điện Tử', N'Sử dụng ví điện tử để thanh toán.', 0, N'Không có phí.'),
(N'TT004', N'Thu Tiền Tận Nơi', N'Nhân viên sẽ đến thu tiền tại địa chỉ giao hàng.', 10, N'Phí thu tiền tận nơi.'),
(N'TT005', N'Thanh Toán Trực Tiếp', N'Khách hàng thanh toán trực tiếp tại cửa hàng.', 0, N'Không có phí.');
go
-- Insert data for pt_thanh_toan
INSERT INTO pt_thanh_toan_hoa_don (id_pt_thanh_toan, so_tien_thanh_toan, ghi_chu, id_giao_dich_thanh_toan) VALUES 
(1, 500.00, N'Thanh toán đơn hàng 001', N'GD001'),
(2, 750.00, N'Thanh toán đơn hàng 002', N'GD002'),
(3, 300.00, N'Thanh toán đơn hàng 003', N'GD003'),
(4, 1200.00, N'Thanh toán đơn hàng 004', N'GD004'),
(5, 150.00, N'Thanh toán đơn hàng 005', N'GD005');

go
-- Insert data for trang_thai_hoa_don
INSERT INTO trang_thai_hoa_don (ten_trang_thai, mo_ta) VALUES 
(N'Chờ Xử Lý', N'Hoa đơn đang chờ xử lý.'),
(N'Đang Giao Hàng', N'Hoa đơn đang được giao hàng.'),
(N'Hoàn Thành', N'Hoa đơn đã hoàn thành.'),
(N'Hủy Bỏ', N'Hoa đơn đã bị hủy.'),
(N'Thất Bại', N'Hoa đơn không thể hoàn tất.');

go
-- Insert data for hoa_don
INSERT INTO hoa_don (ma_hoa_don, id_nguoi_dung, id_loai_voucher, Id_dia_chi_van_chuyen, id_trang_thai_hoa_don, ten_nguoi_nhan, phi_ship, dia_chi, sdt_nguoi_nhan, thanh_tien, mo_ta, id_pt_thanh_toan_hoa_don) VALUES 
('HD001', 1, 1, 1, 1, N'Trần Văn A', 30.00, N'Số 1, Đường A, Quận 1', N'0123456789', 500, N'Hoa đơn cho sản phẩm A', 1),
('HD002', 2, 2, 2, 1, N'Nguyễn Thị B', 20.00, N'Số 2, Đường B, Quận 2', N'0123456788', 750, N'Hoa đơn cho sản phẩm B', 2),
('HD003', 1, 3, 1, 1, N'Lê Văn C', 15.00, N'Số 3, Đường C, Quận 3', N'0123456787', 300, N'Hoa đơn cho sản phẩm C', 3),
('HD004', 3, 4, 2, 1, N'Trần Thị D', 25.00, N'Số 4, Đường D, Quận 4', N'0123456786', 1200, N'Hoa đơn cho sản phẩm D', 4),
('HD005', 2, 5, 1, 1, N'Nguyễn Văn E', 10.00, N'Số 5, Đường E, Quận 5', N'0123456785', 150, N'Hoa đơn cho sản phẩm E', 5);

go
INSERT INTO xac_thuc (ma_xac_thuc, id_nguoi_dung, mo_ta) VALUES 
(N'XAC001', 1, N'Xác thực đăng ký tài khoản'),
(N'XAC002', 2, N'Xác thực email'),
(N'XAC003', 1, N'Xác thực số điện thoại'),
(N'XAC004', 3, N'Xác thực khôi phục mật khẩu'),
(N'XAC005', 2, N'Xác thực đăng nhập');
go


-- Insert data for san_pham
INSERT INTO san_pham (ten_san_pham, mo_ta, id_danh_muc,id_loai_voucher,trang_thai) VALUES 
(N'Áo phông hình bàn tay', N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông butterfly', N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông cotton', N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông ENJOYABLE', N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông loang', N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông holiday 1961',  N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông nam nữ 1984',  N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông nam nữ oversize', N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông tay lỡ', N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông thể thao', N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông SPORT FASHION',  N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),

(N'Áo sơ mi đũi nơ thắt eo',  N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 4,1),
(N'Áo sơ mi nam kẻ sọc',  N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 4,1),
(N'Áo sơ mi lụa công sở',  N'Áo sơ mi được làm từ lụa mềm mại, thoáng khí', 2, 4,1),
(N'Áo sơ mi nam loang',  N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 4,1),
(N'Áo sơ mi ngắn siết eo', N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 4,1),
(N'Áo sơ mi tay ngắn túi hộp',  N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 4,1),
(N'Áo sơ mi thắt cà vạt',  N'Áo sơ mi được làm theo phong cách Nhật', 2, 4,1),
(N'Áo sơ mi sọc đơn giản', N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 4,1),
(N'Áo sơ mi tay ngắn',  N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 4,1),
(N'Áo sơ mi trơn', N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 4,1),

(N'Áo ấm lông cừu',  N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),
(N'Áo béo buộc nơ',  N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),
(N'Áo phao bông', N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),
(N'Áo phao cài khuy',  N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),
(N'Áo phao gile',  N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),
(N'Áo phao cài khuy cổ',  N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),
(N'Áo phao cài lửng thời trang',  N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),
(N'Áo phao nhung cừu',  N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),
(N'Áo phao cài NIKE', N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),

(N'Áo chống nắng viền tròn',  N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 4,1),
(N'Áo chống nắng toàn thân', N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 4,1),
(N'Áo chống nắng thông hơi',  N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 4,1),
(N'Áo chống nắng thời trang',  N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 4,1),
(N'Áo chống nắng thể thao',  N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 4,1),
(N'Áo chống nắng LV', N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 4,1),
(N'Áo chống nắng dài xoe', N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 4,1),


(N'Quần baggy kaki', N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 4,1),
(N'Quần đũi dài nam', N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 4,1),
(N'Quần đũi dài nữ',  N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 4,1),
(N'Quần ống rộng cạp cao', N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 4,1),
(N'Quần ống rộng nam', N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 4,1),
(N'Quần đũi rộng túi hộp',  N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 4,1),
(N'Quần suông đơn giản',  N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 4,1),
(N'Quần suông rộng',  N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 4,1),

(N'Quần ống rộng suông', N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6, 4,1),
(N'Quần sort nữ cá tính',  N'Quần sort chất liệu kaki mềm mại, co giãn nhẹ', 6, 4,1),
(N'Quần sort nữ đũi',  N'Quần sort chất liệu đũi mềm mại, co giãn nhẹ', 6, 4,1),
(N'Quần đùi túi hộp đứng',  N'Quần dài dáng suông, chất liệu đũi mềm mại, co giãn nhẹ', 6, 4,1),
(N'Quần jean cạp trễ', N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6, 4,1),
(N'Quần jean thời trang',  N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6, 4,1),
(N'Quần sort bò rộng', N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6, 4,1),
(N'Quần sort tây nam',  N'Quần sort chất liệu kaki mềm mại, co giãn nhẹ', 6, 4,1),
(N'Quần suông dài basic',  N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6, 4,1);
go
INSERT INTO danh_gia (id_nguoi_dung, id_san_pham, noi_dung, diem) VALUES 
(1, 1, N'Sản phẩm đẹp như trên mô tả', 5),
(2, 2, N'Sản phẩm chất lượng', 4),
(3, 3, N'Sản phẩm tốt, phù hợp giá tiền', 5),
(4, 4, N'Chất vải tốt, mặc rất thoải mái', 4),
(5, 5, N'Áo chất lượng, đáng tiền', 4);
go

-- Insert data for san_pham_chi_tiet
INSERT INTO san_pham_chi_tiet (so_luong, gia_ban, id_kich_thuoc_chi_tiet, id_mau_sac_chi_tiet, id_chat_lieu_chi_tiet, id_gio_hang_chi_tiet, id_san_pham)
VALUES 
/* Áo phông */
(100,120000,  1, 3, 1, 1, 1),
(100, 120000,2, 3, 1, 1, 1),
(100, 120000, 3, 3, 1, 1, 1),
(100, 120000, 4, 3, 1, 1, 1),
(100, 120000, 1, 6, 1, 1, 1),
(100, 120000, 2, 6, 1, 1, 1),
(100, 120000, 3, 6, 1, 1, 1),
(100, 120000, 4, 6, 1, 1, 1),
(100, 120000, 1, 4, 1, 1, 1),
(100, 120000, 2, 4, 1, 1, 1),
(100, 120000, 3, 4, 1, 1, 1),
(100, 120000, 4, 4, 1, 1, 1),
(100, 120000, 1, 2, 1, 2, 2),
(100, 120000, 2, 2, 1, 2, 2),
(100, 120000, 3, 2, 1, 2, 2),
(100, 120000, 4, 2, 1, 2, 2),
(100, 120000, 1, 13, 1, 2, 2),
(100, 120000, 2, 13, 1, 2, 2),
(100, 120000, 3, 13, 1, 2, 2),
(100, 120000, 4, 13, 1, 2, 2),
(100, 120000,  1, 6, 1, 2, 2),
(100, 120000, 2, 6, 1, 2, 2),
(100, 120000, 3, 6, 1, 2, 2),
(100, 120000, 4, 6, 1, 2, 2),
(100, 120000,  1, 9, 1, 3, 3),
(100, 120000, 2, 9, 1, 3, 3),
(100, 120000,  3, 9, 1, 3, 3),
(100, 120000,  4, 9, 1, 3, 3),
(100,  120000, 1, 5, 1, 3, 3),
(100, 120000, 2, 5, 1, 3, 3),
(100, 120000,3, 5, 1, 3, 3),
(100, 120000,  4, 5, 1, 3, 3),
(100,  120000,  1, 4, 1, 3, 3),
(100, 120000,  2, 4, 1, 3, 3),
(100, 120000,   3, 4, 1, 3, 3),
(100,  120000, 4, 4, 1, 3, 3),
(100,  120000,  1, 10, 1, 4, 4),
(100, 120000,   2, 10, 1, 4, 4),
(100, 120000,   3, 10, 1, 4, 4),
(100,  120000,  4, 10, 1, 4, 4),
(100, 120000, 1, 11, 1, 4, 4),
(100,  120000,  2, 11, 1, 4, 4),
(100,  120000,  3, 11, 1, 4, 4),
(100,  120000,  4, 11, 1, 4, 4),
(100, 120000,  1, 4, 1, 4, 4),
(100,  120000, 2, 4, 1, 4, 4),
(100,  120000, 3, 4, 1, 4, 4),
(100, 120000,  4, 4, 1, 4, 4),
(100, 120000,  1, 8, 1, 5, 5),
(100, 120000,  2, 8, 1, 5, 5),
(100, 120000,  3, 8, 1, 5, 5),
(100, 120000, 4, 8, 1, 5, 5),
(100, 120000,   1, 10, 1, 5, 5),
(100,  120000,  2, 10, 1, 5, 5),
(100, 120000,   3, 10, 1, 5, 5),
(100,  120000,  4, 10, 1, 5, 5),
(100, 120000,  1, 1, 1, 5, 6),
(100, 120000,  2, 1, 1, 5, 6),
(100, 120000,  3, 1, 1, 5, 6),
(100, 120000,  4, 1, 1, 5, 6),
(100, 120000,  1, 10, 1, 5, 6),
(100, 120000,  2, 10, 1, 5, 6),
(100, 120000,  3, 10, 1, 5, 6),
(100, 120000,  4, 10, 1, 5, 6),
(100, 120000,   1, 12, 1, 5, 6),
(100,  120000,  2, 12, 1, 5, 6),
(100, 120000,   3, 12, 1, 5, 6),
(100, 120000,   4, 12, 1, 5, 6),
(100, 120000,   1, 1, 1, 5, 7),
(100,  120000,  2, 1, 1, 5, 7),
(100,  120000,  3, 1, 1, 5, 7),
(100, 120000,   4, 1, 1, 5, 7),
(100,  120000,  1, 10, 1, 5, 7),
(100, 120000,   2, 10, 1, 5, 7),
(100,  120000,  3, 10, 1, 5, 7),
(100,  120000,  4, 10, 1, 5, 7),
(100, 120000, 1,9, 1, 5, 7),
(100, 120000, 2,9, 1, 5, 7),
(100, 120000, 3,9, 1, 5, 7),
(100, 120000, 4,9, 1, 5, 7),
(100, 120000,  1, 9, 1, 5, 8),
(100, 120000, 2, 9, 1, 5, 8),
(100, 120000,  3, 9, 1, 5, 8),
(100, 120000,  4, 9, 1, 5, 8),
(100,  120000,  1, 10, 1, 5, 8),
(100,  120000,  2, 10, 1, 5, 8),
(100, 120000, 3, 10, 1, 5, 8),
(100,  120000,  4, 10, 1, 5, 8),
(100, 120000,  1, 9, 1, 5, 9),
(100, 120000,  2, 9, 1, 5, 9),
(100,  120000, 3, 9, 1, 5, 9),
(100, 120000,  4, 9, 1, 5, 9),
(100, 120000,   1, 8, 1, 5, 9),
(100,  120000,  2, 8, 1, 5, 9),
(100,  120000,  3, 8, 1, 5, 9),
(100,  120000,  4, 8, 1, 5, 9),
(100,  120000, 1, 4, 1, 5, 9),
(100, 120000,  2, 4, 1, 5, 9),
(100, 120000,  3, 4, 1, 5, 9),
(100, 120000,  4, 4, 1, 5, 9),
(100, 120000,  1, 1, 1, 5, 10),
(100,  120000, 2, 1, 1, 5, 10),
(100,  120000, 3, 1, 1, 5, 10),
(100, 120000,  4, 1, 1, 5, 10),
(100,  120000, 1, 3, 1, 5, 10),
(100, 120000,  2, 3, 1, 5, 10),
(100, 120000,  3, 3, 1, 5, 10),
(100, 120000,  4, 3, 1, 5, 10),
(100, 120000, 1, 6, 1, 5, 10),
(100, 120000, 2, 6, 1, 5, 10),
(100, 120000, 3, 6, 1, 5, 10),
(100, 120000, 4, 6, 1, 5, 10),
(100,  120000, 1, 1, 1, 5, 11),
(100, 120000,  2, 1, 1, 5, 11),
(100, 120000,  3, 1, 1, 5, 11),
(100, 120000,  4, 1, 1, 5, 11),
(100, 120000, 1, 6, 1, 5, 11),
(100, 120000, 2, 6, 1, 5, 11),
(100, 120000, 3, 6, 1, 5, 11),
(100, 120000, 4, 6, 1, 5, 11),
(100, 120000, 1, 9, 1, 5, 11),
(100, 120000, 2, 9, 1, 5, 11),
(100, 120000, 3, 9, 1, 5, 11),
(100, 120000, 4, 9, 1, 5, 11),
/* Áo sơ mi */
(100,  120000, 1, 11, 1, 4, 12),
(100,  120000, 2, 11, 1, 4, 12),
(100,  120000, 3, 11, 1, 4, 12),
(100,  120000, 4, 11, 1, 4, 12),
(100,  120000,  1, 10, 1, 4, 12),
(100,  120000,  2, 10, 1, 4, 12),
(100,  120000,  3, 10, 1, 4, 12),
(100,  120000,  4, 10, 1, 4, 12),
(100,  120000, 1, 2, 1, 4, 13),
(100, 120000,  2, 2, 1, 4, 13),
(100, 120000,  3, 2, 1, 4, 13),
(100, 120000,  4, 2, 1, 4, 13),
(100,  120000, 1, 1, 1, 4, 13),
(100, 120000,  2, 1, 1, 4, 13),
(100, 120000,  3, 1, 1, 4, 13),
(100, 120000,  4, 1, 1, 4, 13),

(100, 120000, 1, 2, 2, 4, 14),
(100, 120000,  2, 2, 2, 4, 14),
(100,  120000, 3, 2, 2, 4, 14),
(100,  120000, 4, 2, 2, 4, 14),
(100,  120000,  1, 3, 2, 4, 14),
(100,  120000,  2, 3, 2, 4, 14),
(100, 120000, 3, 3, 2, 4, 14),
(100,120000,4, 3, 2, 4, 14),
(100,120000,1, 2, 1, 4, 15),
(100,120000,2, 2, 1, 4, 15),
(100,120000,3, 2, 1, 4, 15),
(100,120000,4, 2, 1, 4, 15),
(100,120000, 1, 1, 1, 4, 15),
(100,120000, 2, 1, 1, 4, 15),
(100,120000, 3, 1, 1, 4, 15),
(100,120000, 4, 1, 1, 4, 15),
(100,120000, 1, 2, 1, 4, 16),
(100,120000, 2, 2, 1, 4, 16),
(100,120000, 3, 2, 1, 4, 16),
(100,120000, 4, 2, 1, 4, 16),
(100,120000, 1, 3, 1, 4, 16),
(100,120000, 2, 3, 1, 4, 16),
(100,120000, 3, 3, 1, 4, 16),
(100,120000, 4, 3, 1, 4, 16),
(100,120000,1, 2, 1, 4, 17),
(100,120000,2, 2, 1, 4, 17),
(100,120000,3, 2, 1, 4, 17),
(100,120000,4, 2, 1, 4, 17),
(100,120000,1, 10, 1, 4, 17),
(100,120000,2, 10, 1, 4, 17),
(100,120000,3, 10, 1, 4, 17),
(100,120000,4, 10, 1, 4, 17),
(100,120000,1, 7, 1, 4, 17),
(100,120000,2, 7, 1, 4, 17),
(100,120000,3, 7, 1, 4, 17),
(100,120000,4, 7, 1, 4, 17),
(100,120000, 1, 9, 1, 4, 18),
(100,120000, 2, 9, 1, 4, 18),
(100,120000, 3, 9, 1, 4, 18),
(100,120000, 4, 9, 1, 4, 18),
(100,120000,1, 12, 1, 4, 18),
(100,120000,2, 12, 1, 4, 18),
(100,120000,3, 12, 1, 4, 18),
(100,120000,4, 12, 1, 4, 18),
(100,120000, 1, 3, 1, 4, 18),
(100,120000, 2, 3, 1, 4, 18),
(100,120000, 3, 3, 1, 4, 18),
(100,120000, 4, 3, 1, 4, 18),
(100,120000,1, 3, 1, 4, 19),
(100,120000,2, 3, 1, 4, 19),
(100,120000,3, 3, 1, 4, 19),
(100,120000,4, 3, 1, 4, 19),
(100,120000, 1,8, 1, 4, 19),
(100,120000, 2,8, 1, 4, 19),
(100,120000, 3,8, 1, 4, 19),
(100,120000, 4,8, 1, 4, 19),
(100,120000, 1,2, 1, 4, 20),
(100,120000, 2,2, 1, 4, 20),
(100,120000, 3,2, 1, 4, 20),
(100,120000, 4,2, 1, 4, 20),
(100,120000,1,7, 1, 4, 20),
(100,120000,2,7, 1, 4, 20),
(100,120000,3,7, 1, 4, 20),
(100,120000,4,7, 1, 4, 20),
(100,120000,1,4, 1, 4, 21),
(100,120000,2,4, 1, 4, 21),
(100,120000,3,4, 1, 4, 21),
(100,120000,4,4, 1, 4, 21),
(100,120000,1,8, 1, 4, 21),
(100,120000,2,8, 1, 4, 21),
(100,120000,3,8, 1, 4, 21),
(100,120000,4,8, 1, 4, 21),
(100,120000, 1,3, 1, 4, 21),
(100,120000, 2,3, 1, 4, 21),
(100,120000, 3,3, 1, 4, 21),
(100,120000, 4,3, 1, 4, 21),

(100,120000,1, 12, 1, 4, 22),
(100,120000,2, 12, 1, 4, 22),
(100,120000,3, 12, 1, 4, 22),
(100,120000,4, 12, 1, 4, 22),
(100,120000,  1, 8, 1, 4, 22),
(100,120000,  2, 8, 1, 4, 22),
(100,120000,  3, 8, 1, 4, 22),
(100,120000,  4, 8, 1, 4, 22),
(100,120000, 1, 1, 1, 4, 22),
(100,120000, 2, 1, 1, 4, 22),
(100,120000, 3, 1, 1, 4, 22),
(100,120000, 4, 1, 1, 4, 22),

(100,120000,  1, 1, 1, 4, 23),
(100,120000,  2, 1, 1, 4, 23),
(100,120000,  3, 1, 1, 4, 23),
(100,120000,  4, 1, 1, 4, 23),
(100,120000,1, 9, 1, 4, 23),
(100,120000,2, 9, 1, 4, 23),
(100,120000,3, 9, 1, 4, 23),
(100,120000,4, 9, 1, 4, 23),
(100,120000,1, 12, 1, 4, 23),
(100,120000,2, 12, 1, 4, 23),
(100,120000,3, 12, 1, 4, 23),
(100,120000,4, 12, 1, 4, 23),
(100,120000,1, 1, 1, 4, 24),
(100,120000,2, 1, 1, 4, 24),
(100,120000,3, 1, 1, 4, 24),
(100,120000,4, 1, 1, 4, 24),
(100,120000,  1, 8, 1, 4, 24),
(100,120000,  2, 8, 1, 4, 24),
(100, 120000, 3, 8, 1, 4, 24),
(100,120000,  4, 8, 1, 4, 24),
(100,120000,  1, 2, 1, 4, 24),
(100,120000,  2, 2, 1, 4, 24),
(100,120000,  3, 2, 1, 4, 24),
(100, 120000, 4, 2, 1, 4, 24),
(100, 120000, 1, 1, 1, 4, 25),
(100, 120000, 2, 1, 1, 4, 25),
(100, 120000, 3, 1, 1, 4, 25),
(100, 120000, 4, 1, 1, 4, 25),
(100, 120000, 1, 7, 1, 4, 25),
(100, 120000, 2, 7, 1, 4, 25),
(100, 120000, 3, 7, 1, 4, 25),
(100,120000,  4, 7, 1, 4, 25),
(100,120000, 1, 12, 1, 4, 25),
(100, 120000,2, 12, 1, 4, 25),
(100, 120000,3, 12, 1, 4, 25),
(100,120000, 4, 12, 1, 4, 25),
(100,120000, 1, 1, 1, 4, 26),
(100,120000, 2, 1, 1, 4, 26),
(100,120000, 3, 1, 1, 4, 26),
(100,120000, 4, 1, 1, 4, 26),
(100,120000, 2, 5, 1, 4, 26),
(100,120000, 2, 5, 1, 4, 26),
(100,120000, 2, 5, 1, 4, 26),
(100,120000, 2, 5, 1, 4, 26),
(100,120000,  1, 2, 1, 4, 26),
(100, 120000, 2, 2, 1, 4, 26),
(100,120000,  3, 2, 1, 4, 26),
(100,120000,  4, 2, 1, 4, 26),
(100,120000,  1, 8, 1, 4, 27),
(100,120000,  2, 8, 1, 4, 27),
(100, 120000, 3, 8, 1, 4, 27),
(100, 120000, 4, 8, 1, 4, 27),
(100,120000, 1, 7, 1, 4, 27),
(100,120000, 2, 7, 1, 4, 27),
(100,120000, 3, 7, 1, 4, 27),
(100,120000, 4, 7, 1, 4, 27),
(100,120000, 1, 1, 1, 4, 27),
(100,120000, 2, 1, 1, 4, 27),
(100,120000, 3, 1, 1, 4, 27),
(100,120000, 4, 1, 1, 4, 27),
(100,120000, 1, 2, 1, 4, 28),
(100,120000, 2, 2, 1, 4, 28),
(100,120000, 3, 2, 1, 4, 28),
(100,120000, 4, 2, 1, 4, 28),
(100,120000,1, 9, 1, 4, 28),
(100,120000,2, 9, 1, 4, 28),
(100,120000,3, 9, 1, 4, 28),
(100,120000,4, 9, 1, 4, 28),
(100,120000, 1, 1, 1, 4, 28),
(100,120000, 2, 1, 1, 4, 28),
(100,120000, 3, 1, 1, 4, 28),
(100,120000, 4, 1, 1, 4, 28),

(100,120000,1, 1, 1, 4, 29),
(100,120000,2, 1, 1, 4, 29),
(100,120000,3, 1, 1, 4, 29),
(100,120000,4, 1, 1, 4, 29),
(100,120000, 1, 1, 1, 4, 29),
(100,120000, 2, 1, 1, 4, 29),
(100,120000, 3, 1, 1, 4, 29),
(100,120000, 4, 1, 1, 4, 29),
(100,120000, 1, 1, 1, 4, 29),
(100,120000, 2, 1, 1, 4, 29),
(100,120000, 3, 1, 1, 4, 29),
(100,120000, 4, 1, 1, 4, 29),
(100,120000,1, 1, 1, 4, 30),
(100,120000,2, 1, 1, 4, 30),
(100,120000,3, 1, 1, 4, 30),
(100,120000,4, 1, 1, 4, 30),
(100,120000, 1, 14, 1, 4, 30),
(100,120000, 2, 14, 1, 4, 30),
(100,120000, 3, 14, 1, 4, 30),
(100,120000, 4, 14, 1, 4, 30),
(100,120000,1, 2, 1, 4, 30),
(100,120000,2, 2, 1, 4, 30),
(100,120000,3, 2, 1, 4, 30),
(100,120000,4, 2, 1, 4, 30),

(100,120000,  1, 8, 2, 4, 31),
(100,120000,  2, 8, 2, 4, 31),
(100,120000,  3, 8, 2, 4, 31),
(100,120000,  4, 8, 2, 4, 31),
(100,120000, 1, 1, 2, 4, 31),
(100,120000, 2, 1, 2, 4, 31),
(100,120000, 3, 1, 2, 4, 31),
(100,120000, 4, 1, 2, 4, 31),
(100,120000,1, 10, 2, 4, 31),
(100,120000,2, 10, 2, 4, 31),
(100,120000,3, 10, 2, 4, 31),
(100,120000,4, 10, 2, 4, 31),
(100,120000, 1, 3, 2, 4, 32),
(100,120000, 2, 3, 2, 4, 32),
(100,120000, 3, 3, 2, 4, 32),
(100,120000, 4, 3, 2, 4, 32),
(100,120000,  1, 10, 2, 4, 32),
(100,120000,  2, 10, 2, 4, 32),
(100,120000,  3, 10, 2, 4, 32),
(100,120000,  4, 10, 2, 4, 32),
(100,120000,1, 2, 2, 4, 32),
(100,120000,2, 2, 2, 4, 32),
(100,120000,3, 2, 2, 4, 32),
(100,120000,4, 2, 2, 4, 32),
(100,120000, 1, 8, 2, 4, 33),
(100,120000, 2, 8, 2, 4, 33),
(100,120000, 3, 8, 2, 4, 33),
(100,120000, 4, 8, 2, 4, 33),
(100,120000, 1, 3, 2, 4, 33),
(100,120000, 2, 3, 2, 4, 33),
(100,120000, 3, 3, 2, 4, 33),
(100,120000, 4, 3, 2, 4, 33),
(100,120000, 1, 1, 2, 4, 33),
(100,120000, 2, 1, 2, 4, 33),
(100,120000, 3, 1, 2, 4, 33),
(100,120000, 4, 1, 2, 4, 33),
(100,120000, 1, 2, 2, 4, 34),
(100,120000, 2, 2, 2, 4, 34),
(100,120000, 3, 2, 2, 4, 34),
(100,120000, 4, 2, 2, 4, 34),
(100,120000,1, 8, 2, 4, 34),
(100,120000,2, 8, 2, 4, 34),
(100,120000,3, 8, 2, 4, 34),
(100,120000,4, 8, 2, 4, 34),
(100,120000,  1, 10, 2, 4, 35),
(100,120000,  2, 10, 2, 4, 35),
(100, 120000, 3, 10, 2, 4, 35),
(100,120000,  4, 10, 2, 4, 35),
(100,120000, 1, 1, 2, 4, 35),
(100,120000, 2, 1, 2, 4, 35),
(100,120000, 3, 1, 2, 4, 35),
(100,120000, 4, 1, 2, 4, 35),
(100,120000, 1, 11, 2, 4, 36),
(100,120000, 2, 11, 2, 4, 36),
(100,120000, 3, 11, 2, 4, 36),
(100,120000, 4, 11, 2, 4, 36),
(100,120000,1, 10, 2, 4, 36),
(100,120000,2, 10, 2, 4, 36),
(100,120000,3, 10, 2, 4, 36),
(100,120000,4, 10, 2, 4, 36),
(100,120000,1, 1, 2, 4, 36),
(100,120000,2, 1, 2, 4, 36),
(100,120000,3, 1, 2, 4, 36),
(100,120000,4, 1, 2, 4, 36),
(100,120000,1, 8, 2, 4, 37),
(100,120000,2, 8, 2, 4, 37),
(100,120000,3, 8, 2, 4, 37),
(100,120000,4, 8, 2, 4, 37),
(100,120000, 1, 14, 2, 4, 37),
(100,120000, 2, 14, 2, 4, 37),
(100,120000, 3, 14, 2, 4, 37),
(100,120000, 4, 14, 2, 4, 37),
(100,120000,1, 1, 2, 4, 37),
(100,120000,2, 1, 2, 4, 37),
(100,120000,3, 1, 2, 4, 37),
(100,120000,4, 1, 2, 4, 37),


(100, 120000, 1, 7, 2, 4, 38),
(100,120000,2, 7, 2, 4, 38),
(100,120000,  3, 7, 2, 4, 38),
(100,120000,  4, 7, 2, 4, 38),
(100,120000,  1, 1, 2, 4, 38),
(100, 120000, 2, 1, 2, 4, 38),
(100, 120000, 3, 1, 2, 4, 38),
(100, 120000, 4, 1, 2, 4, 38),
(100,120000,1, 10, 2, 4, 38),
(100,120000,2, 10, 2, 4, 38),
(100,120000,3, 10, 2, 4, 38),
(100,120000,4, 10, 2, 4, 38),
(100, 120000,1, 1, 2, 4, 39),
(100,120000, 2, 1, 2, 4, 39),
(100,120000, 3, 1, 2, 4, 39),
(100, 120000,4, 1, 2, 4, 39),
(100,120000,1, 12, 2, 4, 39),
(100,120000,2, 12, 2, 4, 39),
(100,120000,3, 12, 2, 4, 39),
(100,120000,4, 12, 2, 4, 39),
(100,120000,  1, 3, 2, 4, 40),
(100,120000,  2, 3, 2, 4, 40),
(100,120000,  3, 3, 2, 4, 40),
(100,120000,  4, 3, 2, 4, 40),
(100,120000,1, 1, 2, 4, 40),
(100,120000,2, 1, 2, 4, 40),
(100,120000,3, 1, 2, 4, 40),
(100,120000,4, 1, 2, 4, 40),
(100,120000, 1, 8, 2, 4, 40),
(100,120000, 2, 8, 2, 4, 40),
(100,120000, 3, 8, 2, 4, 40),
(100,120000, 4, 8, 2, 4, 40),
(100,120000,1, 2, 2, 4, 41),
(100,120000,2, 2, 2, 4, 41),
(100,120000,3, 2, 2, 4, 41),
(100,120000,4, 2, 2, 4, 41),
(100,120000,1, 1, 2, 4, 41),
(100,120000,2, 1, 2, 4, 41),
(100,120000,3, 1, 2, 4, 41),
(100,120000,4, 1, 2, 4, 41),
(100,120000,1, 12, 2, 4, 41),
(100,120000,2, 12, 2, 4, 41),
(100,120000,3, 12, 2, 4, 41),
(100,120000,4, 12, 2, 4, 41),
(100,120000,1, 1, 2, 4, 42),
(100,120000,2, 1, 2, 4, 42),
(100,120000,3, 1, 2, 4, 42),
(100,120000,4, 1, 2, 4, 42),
(100,120000,1, 12, 2, 4, 42),
(100,120000,2, 12, 2, 4, 42),
(100,120000,3, 12, 2, 4, 42),
(100,120000,4, 12, 2, 4, 42),

(100,120000,1, 5, 2, 4, 43),
(100,120000,1, 5, 2, 4, 43),
(100,120000,1, 5, 2, 4, 43),
(100,120000,1, 5, 2, 4, 43),
(100,120000,1,1, 2, 4, 43),
(100,120000,2,1, 2, 4, 43),
(100,120000,3,1, 2, 4, 43),
(100,120000,4,1, 2, 4, 43),
(100,120000,  1,2, 2, 4, 43),
(100,120000,  2,2, 2, 4, 43),
(100,120000,  3,2, 2, 4, 43),
(100,120000,  4,2, 2, 4, 43),

(100,120000,1,1, 2, 4, 44),
(100,120000,2,1, 2, 4, 44),
(100,120000,3,1, 2, 4, 44),
(100,120000,4,1, 2, 4, 44),
(100,120000,1,12, 2, 4, 44),
(100,120000,2,12, 2, 4, 44),
(100,120000,3,12, 2, 4, 44),
(100,120000,4,12, 2, 4, 44),
(100,120000,1,2, 2, 4, 44),
(100,120000,2,2, 2, 4, 44),
(100,120000,3,2, 2, 4, 44),
(100,120000,4,2, 2, 4, 44),
(100,120000,1,9, 2, 4, 45),
(100,120000,2,9, 2, 4, 45),
(100,120000,3,9, 2, 4, 45),
(100,120000,4,9, 2, 4, 45),
(100,120000,1,7, 2, 4, 45),
(100,120000,2,7, 2, 4, 45),
(100,120000,3,7, 2, 4, 45),
(100,120000,4,7, 2, 4, 45),


(100,120000, 1, 7, 2, 4, 46),
(100,120000, 2, 7, 2, 4, 46),
(100,120000, 3, 7, 2, 4, 46),
(100,120000, 4, 7, 2, 4, 46),
(100,120000, 1, 11, 2, 4, 46),
(100,120000, 2, 11, 2, 4, 46),
(100,120000, 3, 11, 2, 4, 46),
(100,120000, 4, 11, 2, 4, 46),
(100,120000,1, 10, 2, 4, 46),
(100,120000,2, 10, 2, 4, 46),
(100,120000,3, 10, 2, 4, 46),
(100,120000,4, 10, 2, 4, 46),

(100, 120000,1, 9, 2, 4, 47),
(100,120000, 2, 9, 2, 4, 47),
(100,120000, 3, 9, 2, 4, 47),
(100,120000, 4, 9, 2, 4, 47),
(100,120000,  1, 1, 2, 4, 47),
(100, 120000, 2, 1, 2, 4, 47),
(100, 120000, 3, 1, 2, 4, 47),
(100, 120000, 4, 1, 2, 4, 47),
(100,120000,1, 2, 3, 4, 48),
(100,120000,2, 2, 3, 4, 48),
(100,120000,3, 2, 3, 4, 48),
(100,120000,4, 2, 3, 4, 48),
(100,120000, 1, 1, 3, 4, 48),
(100,120000, 2, 1, 3, 4, 48),
(100,120000, 3, 1, 3, 4, 48),
(100,120000, 4, 1, 3, 4, 48),
(100,120000,1, 8, 3, 4, 48),
(100,120000,2, 8, 3, 4, 48),
(100,120000,3, 8, 3, 4, 48),
(100,120000,4, 8, 3, 4, 48),
(100,120000, 1, 5, 3, 4, 49),
(100,120000, 2, 5, 3, 4, 49),
(100,120000, 3, 5, 3, 4, 49),
(100,120000, 4, 5, 3, 4, 49),
(100,120000,1, 2, 3, 4, 49),
(100,120000,2, 2, 3, 4, 49),
(100,120000,3, 2, 3, 4, 49),
(100,120000,4, 2, 3, 4, 49),
(100,120000, 1, 3, 2, 4, 50),
(100,120000, 2, 3, 2, 4, 50),
(100,120000, 3, 3, 2, 4, 50),
(100,120000, 4, 3, 2, 4, 50),
(100,120000, 1, 10, 2, 4, 50),
(100,120000, 2, 10, 2, 4, 50),
(100,120000, 3, 10, 2, 4, 50),
(100,120000, 4, 10, 2, 4, 50),
(100,120000, 1, 8, 2, 4, 50),
(100,120000, 2, 8, 2, 4, 50),
(100,120000, 3, 8, 2, 4, 50),
(100,120000, 4, 8, 2, 4, 50),
(100,120000, 1, 8, 2, 4, 51),
(100,120000, 2, 8, 2, 4, 51),
(100,120000, 3, 8, 2, 4, 51),
(100,120000, 4, 8, 2, 4, 51),
(100,120000,1, 9, 2, 4, 51),
(100,120000,2, 9, 2, 4, 51),
(100,120000,3, 9, 2, 4, 51),
(100,120000,4, 9, 2, 4, 51),
(100,120000,1, 1, 2, 4, 51),
(100,120000,2, 1, 2, 4, 51),
(100,120000,3, 1, 2, 4, 51),
(100,120000,4, 1, 2, 4, 51),
(100,120000,1, 1, 2, 4, 52),
(100,120000,2, 1, 2, 4, 52),
(100,120000,3, 1, 2, 4, 52),
(100,120000,4, 1, 2, 4, 52),
(100,120000, 1, 1, 2, 4, 52),
(100,120000, 2, 1, 2, 4, 52),
(100,120000, 3, 1, 2, 4, 52),
(100,120000, 4, 1, 2, 4, 52),
(100,120000,1,12,2, 4, 53),
(100,120000,2,12,2, 4, 53),
(100,120000,3,12,2, 4, 53),
(100,120000,4,12,2, 4, 53),
(100,120000, 1,9,2, 4, 53),
(100,120000, 2,9,2, 4, 53),
(100,120000, 3,9,2, 4, 53),
(100,120000, 4,9,2, 4, 53),
(100,120000,1,1,1, 4, 53),
(100,120000,2,1,1, 4, 53),
(100,120000,3,1,1, 4, 53),
(100,120000,4,1,1, 4, 53),
(100,120000,1,1,1, 4,54),
(100,120000,2,1,1, 4,54),
(100,120000,3,1,1, 4,54),
(100,120000,4,1,1, 4,54),
(100,120000, 1,2,1, 4,54),
(100,120000, 2,2,1, 4,54),
(100,120000, 3,2,1, 4,54),
(100,120000, 4,2,1, 4,54),
(100,120000, 1,3,1, 4,54),
(4,120000, 2,3,1, 4,54),
(1,120000, 3,3,1, 4,54),
(12,120000, 4,3,1, 4,54);
go
INSERT INTO hinh_anh_san_pham (id_san_pham, url_anh, mo_ta, trang_thai, thu_tu, loai_hinh_anh) VALUES /* Áo phông */
(1, 'images/ao_phong/aophongbantaydep.jpg', N'Hình ảnh áo phông', 1, 1, N'Áo phông'),
(1, 'images/ao_phong/aophongbantaydep(2).jpg', N'Hình ảnh áo phông', 1, 2, N'Áo phông'),
(1, 'images/ao_phong/aophongbantaydep(3).jpg', N'Hình ảnh áo phông', 1, 3, N'Áo phông'),
(2, 'images/ao_phong/aophongbutterfly.jpg', N'Hình ảnh áo phông', 1, 1, N'Áo phông'),
(2, 'images/ao_phong/aophongbutterfly(2).jpg', N'Hình ảnh áo phông', 1, 2, N'Áo phông'),
(2, 'images/ao_phong/aophongbutterfly(3).jpg', N'Hình ảnh áo phông', 1, 3, N'Áo phông'),
(3, 'images/ao_phong/aophongcotton.jpg', N'Hình ảnh áo phông', 1, 1, N'Áo phông'),
(3, 'images/ao_phong/aophongcotton(2).jpg', N'Hình ảnh áo phông', 1, 2, N'Áo phông'),
(3, 'images/ao_phong/aophongcotton(3).jpg', N'Hình ảnh áo phông', 1, 3, N'Áo phông'),
(4, 'images/ao_phong/aophongenjoyable.jpg', N'Hình ảnh áo phông', 1, 1, N'Áo phông'),
(4, 'images/ao_phong/aophongenjoyable(2).jpg', N'Hình ảnh áo phông', 1, 2, N'Áo phông'),
(4, 'images/ao_phong/aophongenjoyable(3).jpg', N'Hình ảnh áo phông', 1, 3, N'Áo phông'),
(5, 'images/ao_phong/aophongloang.jpg', N'Hình ảnh áo phông', 1, 1, N'Áo phông'),
(5, 'images/ao_phong/aophongloang(2).jpg', N'Hình ảnh áo phông', 1, 2, N'Áo phông'),
(6, 'images/ao_phong/aophongmatholiday.jpg', N'Hình ảnh áo phông', 1, 1, N'Áo phông'),
(6, 'images/ao_phong/aophongmatholiday(2).jpg', N'Hình ảnh áo phông', 1, 2, N'Áo phông'),
(6, 'images/ao_phong/aophongmatholiday(3).jpg', N'Hình ảnh áo phông', 1, 3, N'Áo phông'),
(7, 'images/ao_phong/aophongnamnu1984.jpg', N'Hình ảnh áo phông', 1, 1, N'Áo phông'),
(7, 'images/ao_phong/aophongnamnu1984(2).jpg', N'Hình ảnh áo phông', 1, 2, N'Áo phông'),
(7, 'images/ao_phong/aophongnamnu1984(3).jpg', N'Hình ảnh áo phông', 1, 3, N'Áo phông'),
(8, 'images/ao_phong/aophongnamnuoversize.jpg', N'Hình ảnh áo phông', 1, 1, N'Áo phông'),
(8, 'images/ao_phong/aophongnamnuoversize(2).jpg', N'Hình ảnh áo phông', 1, 2, N'Áo phông'),
(9, 'images/ao_phong/aophongtaylo.jpg', N'Hình ảnh áo phông', 1, 1, N'Áo phông'),
(9, 'images/ao_phong/aophongtaylo(2).jpg', N'Hình ảnh áo phông', 1, 2, N'Áo phông'),
(9, 'images/ao_phong/aophongtaylo(3).jpg', N'Hình ảnh áo phông', 1, 3, N'Áo phông'),
(10, 'images/ao_phong/aophongthethao.jpg', N'Hình ảnh áo phông', 1, 1, N'Áo phông'),
(10, 'images/ao_phong/aophongthethao(2).jpg', N'Hình ảnh áo phông', 1, 2, N'Áo phông'),
(10, 'images/ao_phong/aophongthethao(3).jpg', N'Hình ảnh áo phông', 1, 3, N'Áo phông'),
(11, 'images/ao_phong/sportfashion.jpg', N'Hình ảnh áo phông', 1, 1, N'Áo phông'),
(11, 'images/ao_phong/sportfashion(2).jpg', N'Hình ảnh áo phông', 1, 2, N'Áo phông'),
(11, 'images/ao_phong/sportfashion(3).jpg', N'Hình ảnh áo phông', 1, 3, N'Áo phông'),

/* Áo sơ mi */
(12,'images/ao_so_mi/aosomiduinobuoceo.jpg', N'Hình ảnh áo sơ mi',1,1,N'Áo sơ mi'),
(12,'images/ao_so_mi/aosomiduinobuoceo(2).jpg', N'Hình ảnh sơ mi',1,2,N'Áo sơ mi'),
(13,'images/ao_so_mi/aosomikesoc.jpg', N'Hình ảnh áo sơ mi',1,1,N'Áo sơ mi'),
(13,'images/ao_so_mi/aosomikesoc(2).jpg', N'Hình ảnh áo sơ mi',1,2,N'Áo sơ mi'),
(14,'images/ao_so_mi/aosomiluacongso.jpg', N'Hình ảnh sơ mi',1,1,N'Áo sơ mi'),
(14,'images/ao_so_mi/aosomiluacongso(2).jpg', N'Hình ảnh sơ mi',1,2,N'Áo sơ mi'), 
(15,'images/ao_so_mi/aosominamnuloang.jpg', N'Hình ảnh sơ mi',1,1,N'Áo sơ mi'), 
(15,'images/ao_so_mi/aosominamnuloang(2).jpg', N'Hình ảnh sơ mi',1,2,N'Áo sơ mi'),
(16,'images/ao_so_mi/aosomingansieteo.jpg', N'Hình ảnh sơ mi',1,1,N'Áo sơ mi'),
(16,'images/ao_so_mi/aosomingansieteo(2).jpg', N'Hình ảnh sơ mi',1,2,N'Áo sơ mi'),
(17,'images/ao_so_mi/aosomingantaytuihop.jpg', N'Hình ảnh sơ mi',1,1,N'Áo sơ mi'),
(17,'images/ao_so_mi/aosomingantaytuihop(2).jpg', N'Hình ảnh sơ mi',1,2,N'Áo sơ mi'),
(17,'images/ao_so_mi/aosomingantaytuihop(3).jpg', N'Hình ảnh sơ mi',1,3,N'Áo sơ mi'),
(18,'images/ao_so_mi/aosomiphongcachnhat.jpg', N'Hình ảnh sơ mi',1,1,N'Áo sơ mi'),
(18,'images/ao_so_mi/aosomiphongcachnhat(2).jpg', N'Hình ảnh sơ mi ',1,2,N'Áo sơ mi'),
(18,'images/ao_so_mi/aosomiphongcachnhat(3).jpg', N'Hình ảnh sơ mi',1,3,N'Áo sơ mi'),
(19,'images/ao_so_mi/aosomisocdongian.jpg', N'Hình ảnh áo sơ mi',1,1,N'Áo sơ mi'),
(19,'images/ao_so_mi/aosomisocdongian(2).jpg', N'Hình ảnh áo sơ mi',1,2,N'Áo sơ mi'),
(20,'images/ao_so_mi/aosomitayngan.jpg', N'Hình ảnh áo sơ mi',1,1,N'Áo sơ mi'),
(20, 'images/ao_so_mi/aosomitayngan(2).jpg', 'Hình ảnh áo sơ mi', 1, 2, 'Hình ảnh chính'),
(21, 'images/ao_so_mi/aosomitron.jpg', 'Hình ảnh áo sơ mi', 1, 1, 'Hình ảnh chính'),
(21, 'images/ao_so_mi/aosomitron(2).jpg', 'Hình ảnh áo sơ mi', 1, 2, 'Hình ảnh phụ'),
(21, 'images/ao_so_mi/aosomitron(3).jpg', 'Hình ảnh áo sơ mi', 1, 3, 'Hình ảnh phụ'),



(22, 'images/ao_phao/aoamcolong.jpg', 'Hình ảnh áo phao', 1, 1, 'Hình ảnh chính'),
(22, 'images/ao_phao/aoamcolong(2).jpg', 'Hình ảnh áo phao', 1, 2, 'Hình ảnh phụ'),
(22, 'images/ao_phao/aoamcolong(3).jpg', 'Hình ảnh áo phao', 1, 3, 'Hình ảnh phụ'),
(23, 'images/ao_phao/aobeobuocno.jpg', 'Hình ảnh áo phao', 1, 1, 'Hình ảnh chính'),
(23, 'images/ao_phao/aobeobuocno(2).jpg', 'Hình ảnh áo phao', 1, 2, 'Hình ảnh phụ'),
(23, 'images/ao_phao/aobeobuocno(3).jpg', 'Hình ảnh áo phao', 1, 3, 'Hình ảnh phụ'),
(24, 'images/ao_phao/aophaobongngau.jpg', 'Hình ảnh áo phao', 1, 1, 'Hình ảnh chính'),
(24, 'images/ao_phao/aophaobongngau(2).jpg', 'Hình ảnh áo phao', 1, 2, 'Hình ảnh phụ'),
(25, 'images/ao_phao/aophaocaikhuydethuong.jpg', 'Hình ảnh áo phao', 1, 1, 'Hình ảnh chính'),
(25, 'images/ao_phao/aophaocaikhuydethuong(2).jpg', 'Hình ảnh áo phao', 1, 2, 'Hình ảnh phụ'),
(25, 'images/ao_phao/aophaocaikhuydethuong(3).jpg', 'Hình ảnh áo phao', 1, 3, 'Hình ảnh phụ'),
(26, 'images/ao_phao/aophaogile.jpg', 'Hình ảnh áo phao', 1, 1, 'Hình ảnh chính'),
(26, 'images/ao_phao/aophaogile(2).jpg', 'Hình ảnh áo phao', 1, 2, 'Hình ảnh phụ'),
(26, 'images/ao_phao/aophaogile(3).jpg', 'Hình ảnh áo phao', 1, 3, 'Hình ảnh phụ'),
(27, 'images/ao_phao/aophaokhuyco.jpg', 'Hình ảnh áo phao', 1, 1, 'Hình ảnh chính'),
(27, 'images/ao_phao/aophaokhuyco(2).jpg', 'Hình ảnh áo phao', 1, 2, 'Hình ảnh phụ'),
(27, 'images/ao_phao/aophaokhuyco(3).jpg', 'Hình ảnh áo phao', 1, 3, 'Hình ảnh phụ'),
(28, 'images/ao_phao/aophaolungthoitrang.jpg', 'Hình ảnh áo phao', 1, 1, 'Hình ảnh chính'),
(28, 'images/ao_phao/aophaolungthoitrang(2).jpg', 'Hình ảnh áo phao', 1, 2, 'Hình ảnh phụ'),
(28, 'images/ao_phao/aophaolungthoitrang(3).jpg', 'Hình ảnh áo phao', 1, 3, 'Hình ảnh phụ'),
(29, 'images/ao_phao/aophaonhungcuu.jpg', 'Hình ảnh áo phao', 1, 1, 'Hình ảnh chính'),
(29, 'images/ao_phao/aophaonhungcuu(2).jpg', 'Hình ảnh áo phao', 1, 2, 'Hình ảnh phụ'),
(29, 'images/ao_phao/aophaonhungcuu(3).jpg', 'Hình ảnh áo phao', 1, 3, 'Hình ảnh phụ'),
(30, 'images/ao_phao/aophaoNIKE.jpg', 'Hình ảnh áo phao', 1, 1, 'Hình ảnh chính'),
(30, 'images/ao_phao/aophaoNIKE(2).jpg', 'Hình ảnh áo phao', 1, 2, 'Hình ảnh phụ'),
(30, 'images/ao_phao/aophaoNIKE(3).jpg', 'Hình ảnh áo phao', 1, 3, 'Hình ảnh phụ'),




(31, 'images/ao_chong_nang/aochongnangvientron.jpg', 'Hình ảnh áo chống nắng', 1, 1, 'Hình ảnh chính'),
(31, 'images/ao_chong_nang/aochongnangvientron(2).jpg', 'Hình ảnh chống nắng', 1, 2, 'Hình ảnh phụ'),
(31, 'images/ao_chong_nang/aochongnangvientron(3).jpg', 'Hình ảnh chống nắng', 1, 3, 'Hình ảnh phụ'),
(32, 'images/ao_chong_nang/aochongnangtoanthan.jpg', 'Hình ảnh áo chống nắng', 1, 1, 'Hình ảnh chính'),
(32, 'images/ao_chong_nang/aochongnangtoanthan(2).jpg', 'Hình ảnh áo chống nắng', 1, 2, 'Hình ảnh phụ'),
(32, 'images/ao_chong_nang/aochongnangtoanthan(3).jpg', 'Hình ảnh áo chống nắng', 1, 3, 'Hình ảnh phụ'),
(33, 'images/ao_chong_nang/aochongnangthonghoi.jpg', 'Hình ảnh áo chống nắng', 1, 1, 'Hình ảnh chính'),
(33, 'images/ao_chong_nang/aochongnangthonghoi(2).jpg', 'Hình ảnh áo chống nắng', 1, 2, 'Hình ảnh phụ'),
(33, 'images/ao_chong_nang/aochongnangthonghoi(3).jpg', 'Hình ảnh áo chống nắng', 1, 3, 'Hình ảnh phụ'),
(34, 'images/ao_chong_nang/aochongnangthoitrang.jpg', 'Hình ảnh áo chống nắng', 1, 1, 'Hình ảnh chính'),
(34, 'images/ao_chong_nang/aochongnangthoitrang(2).jpg', 'Hình ảnh áo chống nắng', 1, 2, 'Hình ảnh phụ'),
(35, 'images/ao_chong_nang/aochongnangthethao.jpg', 'Hình ảnh áo chống nắng', 1, 1, 'Hình ảnh chính'),
(35, 'images/ao_chong_nang/aochongnangthethao(2).jpg', 'Hình ảnh áo chống nắng', 1, 2, 'Hình ảnh phụ'),
(35, 'images/ao_chong_nang/aochongnangthethao(3).jpg', 'Hình ảnh áo chống nắng', 1, 3, 'Hình ảnh phụ'),
(36, 'images/ao_chong_nang/aochongnangLV.jpg', 'Hình ảnh áo chống nắng', 1, 1, 'Hình ảnh chính'),
(36, 'images/ao_chong_nang/aochongnangLV(2).jpg', 'Hình ảnh áo chống nắng', 1, 2, 'Hình ảnh phụ'),
(36, 'images/ao_chong_nang/aochongnangLV(3).jpg', 'Hình ảnh áo chống nắng', 1, 3, 'Hình ảnh phụ'),
(37, 'images/ao_chong_nang/aochongnangdaixoe.jpg', 'Hình ảnh áo chống nắng', 1, 1, 'Áo chống nắng'),
(37, 'images/ao_chong_nang/aochongnangdaixoe(2).jpg', 'Hình ảnh áo chống nắng', 1, 2, 'Hình ảnh phụ'),
(37, 'images/ao_chong_nang/aochongnangdaixoe(3).jpg', 'Hình ảnh áo chống nắng', 1, 3, 'Hình ảnh phụ'),



(38, 'images/quan_kaki/quanbaggykaki.jpg', 'Hình ảnh quần kaki', 1, 1, 'Quần kaki'),
(38, 'images/quan_kaki/quanbaggykaki(2).jpg', 'Hình ảnh quần kaki', 1, 2, 'Quần kaki'),
(38, 'images/quan_kaki/quanbaggykaki(3).jpg', 'Hình ảnh quần kaki', 1, 3, 'Quần kaki'),
(39, 'images/quan_kaki/quanduidainamnu.jpg', 'Hình ảnh quần kaki', 1, 2, 'Quần kaki'),
(39, 'images/quan_kaki/quanduidainamnu(2).jpg', 'Hình ảnh quần kaki', 1, 2, 'Quần kaki'),
(40, 'images/quan_kaki/quanduithoaimai.jpg', 'Hình ảnh quần kaki', 1, 1, 'Quần kaki'),
(40, 'images/quan_kaki/quanduithoaimai(2).jpg', 'Hình ảnh quần kaki', 1, 3, 'Quần kaki'),
(40, 'images/quan_kaki/quanduithoaimai(3).jpg', 'Hình ảnh quần kaki', 1, 1, 'Quần kaki'),
(41, 'images/quan_kaki/quanongrongcapcao.jpg', 'Hình ảnh quần kaki', 1, 2, 'Quần kaki'),
(41, 'images/quan_kaki/quanongrongcapcao(2).jpg', 'Hình ảnh quần kaki', 1, 3, 'Quần kaki'),
(41, 'images/quan_kaki/quanongrongcapcao(3).jpg', 'Hình ảnh quần kaki', 1, 1, 'Quần kaki'),
(42, 'images/quan_kaki/quanongrongcongso.jpg', 'Hình ảnh quần kaki', 1, 2, 'Quần kaki'),
(42, 'images/quan_kaki/quanongrongcongso(2).jpg', 'Hình ảnh quần kaki', 1, 3, 'Quần kaki'),
(43, 'images/quan_kaki/quanrongtuihopngau.jpg', 'Hình ảnh quần kaki', 1, 1, 'Quần kaki'),
(43, 'images/quan_kaki/quanrongtuihopngau(2).jpg', 'Hình ảnh quần kaki', 1, 2, 'Quần kaki'),
(43, 'images/quan_kaki/quanrongtuihopngau(3).jpg', 'Hình ảnh quần kaki', 1, 3, 'Quần kaki'),
(44, 'images/quan_kaki/quansuongdongian.jpg', 'Hình ảnh quần kaki', 1, 1, 'Quần kaki'),
(44, 'images/quan_kaki/quansuongdongian(2).jpg', 'Hình ảnh quần kaki', 1, 3, 'Quần kaki'),
(44, 'images/quan_kaki/quansuongdongian(3).jpg', 'Hình ảnh quần kaki', 1, 2, 'Quần kaki'),
(45, 'images/quan_kaki/quansuongongrong.jpg', 'Hình ảnh quần kaki', 1, 2, 'Quần kaki'),
(45, 'images/quan_kaki/quansuongongrong(2).jpg', 'Hình ảnh quần kaki', 1, 1, 'Quần kaki'),


(46, 'images/quan_sort_&_jean/quanboongrongsuong.jpg', 'Hình ảnh quần kiểu', 1, 1, 'Quần sort and jean'),
(46, 'images/quan_sort_&_jean/quanboongrongsuong(2).jpg', 'Hình ảnh quần kiểu', 1, 2, 'Quần sort and jean'),
(46, 'images/quan_sort_&_jean/quanboongrongsuong(3).jpg', 'Hình ảnh quần kiểu', 1, 3, 'Quần sort and jean'),
(47, 'images/quan_sort_&_jean/quanduinucatinh.jpg', 'Hình ảnh quần kiểu', 1, 1, 'Quần sort and jean'),
(47, 'images/quan_sort_&_jean/quanduinucatinh(2).jpg', 'Hình ảnh quần kiểu', 1, 2, 'Quần sort and jean'),
(48, 'images/quan_sort_&_jean/quanduinuthoaimai.jpg', 'Hình ảnh quần kiểu', 1, 1, 'Quần sort and jean'),
(48, 'images/quan_sort_&_jean/quanduinuthoaimai(2).jpg', 'Hình ảnh quần kiểu', 1, 2, 'Quần sort and jean'),
(48, 'images/quan_sort_&_jean/quanduinuthoaimai(3).jpg', 'Hình ảnh quần kiểu', 1, 3, 'Quần sort and jean'),
(49, 'images/quan_sort_&_jean/quanduituihopdung.jpg', 'Hình ảnh quần kiểu', 1, 1, 'Quần sort and jean'),
(49, 'images/quan_sort_&_jean/quanduituihopdung(2).jpg', 'Hình ảnh quần kiểu', 1, 2, 'Quần sort and jean'),
(50, 'images/quan_sort_&_jean/quanjeancaptre.jpg', 'Hình ảnh quần kiểu', 1, 1, 'Quần sort and jean'),
(50, 'images/quan_sort_&_jean/quanjeancaptre(2).jpg', 'Hình ảnh quần kiểu', 1, 2, 'Quần sort and jean'),
(50, 'images/quan_sort_&_jean/quanjeancaptre(3).jpg', 'Hình ảnh quần kiểu', 1, 3, 'Quần sort and jean'),
(51, 'images/quan_sort_&_jean/quanjeanthoitrang.jpg', 'Hình ảnh quần kiểu', 1, 1, 'Quần sort and jean'),
(51, 'images/quan_sort_&_jean/quanjeanthoitrang(2).jpg', 'Hình ảnh quần kiểu', 1, 2, 'Quần sort and jean'),
(51, 'images/quan_sort_&_jean/quanjeanthoitrang(3).jpg', 'Hình ảnh quần kiểu', 1, 3, 'Quần sort and jean'),
(52, 'images/quan_sort_&_jean/quanshortborong.jpg', 'Hình ảnh quần kiểu', 1, 1, 'Quần sort and jean'),
(52, 'images/quan_sort_&_jean/quanshortborong(2).jpg', 'Hình ảnh quần kiểu', 1, 2, 'Quần sort and jean'),
(53, 'images/quan_sort_&_jean/quanshorttaynam.jpg', 'Hình ảnh quần kiểu', 1, 1, 'Quần sort and jean'),
(53, 'images/quan_sort_&_jean/quanshorttaynam(2).jpg', 'Hình ảnh quần kiểu', 1, 2, 'Quần sort and jean'),
(53, 'images/quan_sort_&_jean/quanshorttaynam(3).jpg', 'Hình ảnh quần kiểu', 1, 3, 'Quần sort and jean'),
(54, 'images/quan_sort_&_jean/quansuongdaibasic.jpg', 'Hình ảnh quần kiểu', 1, 1, 'Quần sort and jean'),
(54, 'images/quan_sort_&_jean/quansuongdaibasic(2).jpg', 'Hình ảnh quần kiểu', 1, 2, 'Quần sort and jean'),
(54, 'images/quan_sort_&_jean/quansuongdaibasic(3).jpg', 'Hình ảnh quần kiểu', 1, 3, 'Quần sort and jean');
go
--Insert data for hoa_don_chi_tiet
INSERT INTO hoa_don_chi_tiet (id_san_pham_chi_tiet, id_hoa_don, so_luong, tong_tien,so_tien_thanh_toan,tien_tra_lai) VALUES 
(1, 1, 2, 1000, 1000,0),
(2, 1, 1, 500,600,100),
(3, 2, 1, 750, 750,0),
(4, 3, 5, 1500, 1500,0),
(5, 4, 3, 3600,4000,400);
go

-- Insert data for lich_su_hoa_don




select * from vai_tro
select * from nguoi_dung
select * from voucher
select * from loai_voucher
select * from danh_muc
select * from danh_gia
select * from chat_lieu
select * from chat_lieu_chi_tiet
select * from kich_thuoc
select * from kich_thuoc_chi_tiet
select * from mau_sac
select * from mau_sac_chi_tiet
select * from gio_hang
select * from gio_hang_chi_tiet
select * from phi_van_chuyen
select * from dia_chi_van_chuyen
select * from pt_thanh_toan
select * from pt_thanh_toan_hoa_don
select * from trang_thai_hoa_don
select * from hoa_don
select * from hoa_don_chi_tiet
select * from xac_thuc
select * from lich_su_hoa_don
select * from san_pham
select * from san_pham_chi_tiet
select * from hinh_anh_san_pham