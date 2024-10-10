CREATE DATABASE nb;
GO

USE nb;
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
  [phan_tram_giam] DECIMAL(5,2),
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
  [gia_ban] DECIMAL(18,2) NOT NULL,
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
  [don_gia] DECIMAL(18,2),
  [thanh_tien] DECIMAL(18,2),
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
  [so_luong] INT NOT NULL,
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [trang_thai] BIT DEFAULT 1,
  [mo_ta] NVARCHAR(MAX),
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
  [so_tien_van_chuyen] DECIMAL(18,2),
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
  [phi_giao_dich] DECIMAL(18,2),
  [thong_tin_them] NVARCHAR(MAX)
);
go
CREATE TABLE [pt_thanh_toan_hoa_don] (
  [Id_thanh_toan_hoa_don] INT PRIMARY KEY IDENTITY(1,1),
  [id_pt_thanh_toan] INT,
  [so_tien_thanh_toan] DECIMAL(18,2),
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
  [phi_ship] DECIMAL(18,2),
  [dia_chi] NVARCHAR(255) NOT NULL,
  [sdt_nguoi_nhan] NVARCHAR(15),
  [thanh_tien] DECIMAL(18,2),
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
  [so_tien_thanh_toan] DECIMAL(18,2),
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
  [tong_tien] DECIMAL(18,2),
  [so_tien_thanh_toan] DECIMAL(18,2),
  [tien_tra_lai] DECIMAL(18,2),
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
(N'VOUCHER1', 10.00, 100, N'Giảm 10% cho đơn hàng trên 500k'),
(N'VOUCHER2', 15.00, 50, N'Giảm 15% cho khách hàng mới'),
(N'VOUCHER3', 20.00, 30, N'Giảm 20% vào dịp sinh nhật'),
(N'VOUCHER4', 5.00, 200, N'Giảm 5% cho tất cả sản phẩm'),
(N'VOUCHER5', 25.00, 10, N'Giảm 25% cho đơn hàng trên 1 triệu');
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
(500.00, 4),
(750.00, 2),
(300.00, 1),
(1200.00, 3),
(150.00, 5);
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
(N'Ghi', N'Màu ghi là màu trung tính giữa màu đen và trắng, mang ý nghĩa cân bằng, trí tuệ, trang trọng và nghiêm túc');
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
(N'TT001', N'Transfer Ngân Hàng', N'Transfer qua ngân hàng cho đơn hàng.', 0.00, N'Không có phí.'),
(N'TT002', N'Thanh Toán Qua Thẻ', N'Sử dụng thẻ tín dụng để thanh toán.', 1.50, N'Phí giao dịch thẻ tín dụng.'),
(N'TT003', N'Ví Điện Tử', N'Sử dụng ví điện tử để thanh toán.', 0.00, N'Không có phí.'),
(N'TT004', N'Thu Tiền Tận Nơi', N'Nhân viên sẽ đến thu tiền tại địa chỉ giao hàng.', 10.00, N'Phí thu tiền tận nơi.'),
(N'TT005', N'Thanh Toán Trực Tiếp', N'Khách hàng thanh toán trực tiếp tại cửa hàng.', 0.00, N'Không có phí.');
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
('HD001', 1, 1, 1, 1, N'Trần Văn A', 30.00, N'Số 1, Đường A, Quận 1', N'0123456789', 500.00, N'Hoa đơn cho sản phẩm A', 1),
('HD002', 2, 2, 2, 1, N'Nguyễn Thị B', 20.00, N'Số 2, Đường B, Quận 2', N'0123456788', 750.00, N'Hoa đơn cho sản phẩm B', 2),
('HD003', 1, 3, 1, 1, N'Lê Văn C', 15.00, N'Số 3, Đường C, Quận 3', N'0123456787', 300.00, N'Hoa đơn cho sản phẩm C', 3),
('HD004', 3, 4, 2, 1, N'Trần Thị D', 25.00, N'Số 4, Đường D, Quận 4', N'0123456786', 1200.00, N'Hoa đơn cho sản phẩm D', 4),
('HD005', 2, 5, 1, 1, N'Nguyễn Văn E', 10.00, N'Số 5, Đường E, Quận 5', N'0123456785', 150.00, N'Hoa đơn cho sản phẩm E', 5);

go
INSERT INTO xac_thuc (ma_xac_thuc, id_nguoi_dung, mo_ta) VALUES 
(N'XAC001', 1, N'Xác thực đăng ký tài khoản'),
(N'XAC002', 2, N'Xác thực email'),
(N'XAC003', 1, N'Xác thực số điện thoại'),
(N'XAC004', 3, N'Xác thực khôi phục mật khẩu'),
(N'XAC005', 2, N'Xác thực đăng nhập');
go


-- Insert data for san_pham
INSERT INTO san_pham (ten_san_pham, gia_ban, mo_ta, id_danh_muc,id_loai_voucher,trang_thai) VALUES 
(N'Áo phông hình bàn tay',120000, N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông butterfly',  120000, N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông cotton',  120000, N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông ENJOYABLE',120000, N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông loang',  120000, N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông holiday 1961', 120000, N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông nam nữ 1984', 120000, N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông nam nữ oversize',120000, N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông tay lỡ',  120000, N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông thể thao',  120000, N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),
(N'Áo phông SPORT FASHION', 120000, N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 4,1),

(N'Áo sơ mi đũi nơ thắt eo', 120000, N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 4,1),
(N'Áo sơ mi nam kẻ sọc', 120000, N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 4,1),
(N'Áo sơ mi lụa công sở', 120000, N'Áo sơ mi được làm từ lụa mềm mại, thoáng khí', 2, 4,1),
(N'Áo sơ mi nam loang', 120000, N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 4,1),
(N'Áo sơ mi ngắn siết eo',  120000, N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 4,1),
(N'Áo sơ mi tay ngắn túi hộp', 120000, N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 4,1),
(N'Áo sơ mi thắt cà vạt', 120000, N'Áo sơ mi được làm theo phong cách Nhật', 2, 4,1),
(N'Áo sơ mi sọc đơn giản',  120000, N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 4,1),
(N'Áo sơ mi tay ngắn', 120000, N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 4,1),
(N'Áo sơ mi trơn',  120000, N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 4,1),

(N'Áo ấm lông cừu', 120000, N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),
(N'Áo béo buộc nơ', 120000, N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),
(N'Áo phao bông',  120000, N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),
(N'Áo phao cài khuy', 120000, N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),
(N'Áo phao gile', 120000, N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),
(N'Áo phao cài khuy cổ', 120000, N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),
(N'Áo phao cài lửng thời trang', 120000, N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),
(N'Áo phao nhung cừu', 120000, N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),
(N'Áo phao cài NIKE',  120000, N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 4,1),

(N'Áo chống nắng viền tròn', 120000, N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 4,1),
(N'Áo chống nắng toàn thân',  120000, N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 4,1),
(N'Áo chống nắng thông hơi', 120000, N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 4,1),
(N'Áo chống nắng thời trang', 120000, N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 4,1),
(N'Áo chống nắng thể thao', 120000, N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 4,1),
(N'Áo chống nắng LV',  120000, N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 4,1),
(N'Áo chống nắng dài xoe',  120000, N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 4,1),


(N'Quần baggy kaki',  120000, N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 4,1),
(N'Quần đũi dài nam',  120000, N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 4,1),
(N'Quần đũi dài nữ', 120000, N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 4,1),
(N'Quần ống rộng cạp cao',  120000, N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 4,1),
(N'Quần ống rộng nam',  120000, N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 4,1),
(N'Quần đũi rộng túi hộp', 120000, N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 4,1),
(N'Quần suông đơn giản', 120000, N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 4,1),
(N'Quần suông rộng', 120000, N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 4,1),

(N'Quần ống rộng suông',  120000, N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6, 4,1),
(N'Quần sort nữ cá tính', 120000, N'Quần sort chất liệu kaki mềm mại, co giãn nhẹ', 6, 4,1),
(N'Quần sort nữ đũi', 120000, N'Quần sort chất liệu đũi mềm mại, co giãn nhẹ', 6, 4,1),
(N'Quần đùi túi hộp đứng', 120000, N'Quần dài dáng suông, chất liệu đũi mềm mại, co giãn nhẹ', 6, 4,1),
(N'Quần jean cạp trễ',  120000, N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6, 4,1),
(N'Quần jean thời trang', 120000, N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6, 4,1),
(N'Quần sort bò rộng',  120000, N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6, 4,1),
(N'Quần sort tây nam', 120000, N'Quần sort chất liệu kaki mềm mại, co giãn nhẹ', 6, 4,1),
(N'Quần suông dài basic', 120000, N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6, 4,1);
go
INSERT INTO danh_gia (id_nguoi_dung, id_san_pham, noi_dung, diem) VALUES 
(1, 1, N'Sản phẩm đẹp như trên mô tả', 5),
(2, 2, N'Sản phẩm chất lượng', 4),
(3, 3, N'Sản phẩm tốt, phù hợp giá tiền', 5),
(4, 4, N'Chất vải tốt, mặc rất thoải mái', 4),
(5, 5, N'Áo chất lượng, đáng tiền', 4);
go

-- Insert data for san_pham_chi_tiet
INSERT INTO san_pham_chi_tiet (so_luong, mo_ta, id_kich_thuoc_chi_tiet, id_mau_sac_chi_tiet, id_chat_lieu_chi_tiet, id_gio_hang_chi_tiet, id_san_pham)
VALUES 
/* Áo phông */
(100, N'Áo phông hình bàn tay màu xanh dương', 1, 3, 1, 1, 1),
(100, N'Áo phông hình bàn tay màu xanh dương', 2, 3, 1, 1, 1),
(100, N'Áo phông hình bàn tay màu xanh dương', 3, 3, 1, 1, 1),
(100, N'Áo phông hình bàn tay màu xanh dương', 4, 3, 1, 1, 1),
(100, N'Áo phông hình bàn tay màu đỏ', 1, 6, 1, 1, 1),
(100, N'Áo phông hình bàn tay màu đỏ', 2, 6, 1, 1, 1),
(100, N'Áo phông hình bàn tay màu đỏ', 3, 6, 1, 1, 1),
(100, N'Áo phông hình bàn tay màu đỏ', 4, 6, 1, 1, 1),
(100, N'Áo phông hình bàn tay màu xanh lá', 1, 4, 1, 1, 1),
(100, N'Áo phông hình bàn tay màu xanh lá', 2, 4, 1, 1, 1),
(100, N'Áo phông hình bàn tay màu xanh lá', 3, 4, 1, 1, 1),
(100, N'Áo phông hình bàn tay màu xanh lá', 4, 4, 1, 1, 1),
(100, N'Áo phông butterfly màu trắng', 1, 2, 1, 2, 2),
(100, N'Áo phông butterfly màu trắng', 2, 2, 1, 2, 2),
(100, N'Áo phông butterfly màu trắng', 3, 2, 1, 2, 2),
(100, N'Áo phông butterfly màu trắng', 4, 2, 1, 2, 2),
(100, N'Áo phông butterfly màu tím', 1, 13, 1, 2, 2),
(100, N'Áo phông butterfly màu tím', 2, 13, 1, 2, 2),
(100, N'Áo phông butterfly màu tím', 3, 13, 1, 2, 2),
(100, N'Áo phông butterfly màu tím', 4, 13, 1, 2, 2),
(100, N'Áo phông butterfly màu đỏ', 1, 6, 1, 2, 2),
(100, N'Áo phông butterfly màu đỏ', 2, 6, 1, 2, 2),
(100, N'Áo phông butterfly màu đỏ', 3, 6, 1, 2, 2),
(100, N'Áo phông butterfly màu đỏ', 4, 6, 1, 2, 2),
(100, N'Áo phông cotton màu nâu', 1, 9, 1, 3, 3),
(100, N'Áo phông cotton màu nâu', 2, 9, 1, 3, 3),
(100, N'Áo phông cotton màu nâu', 3, 9, 1, 3, 3),
(100, N'Áo phông cotton màu nâu', 4, 9, 1, 3, 3),
(100, N'Áo phông cotton màu xanh rêu', 1, 5, 1, 3, 3),
(100, N'Áo phông cotton màu xanh rêu', 2, 5, 1, 3, 3),
(100, N'Áo phông cotton màu xanh rêu', 3, 5, 1, 3, 3),
(100, N'Áo phông cotton màu xanh rêu', 4, 5, 1, 3, 3),
(100, N'Áo phông cotton màu xanh lá', 1, 4, 1, 3, 3),
(100, N'Áo phông cotton màu xanh lá', 2, 4, 1, 3, 3),
(100, N'Áo phông cotton màu xanh lá', 3, 4, 1, 3, 3),
(100, N'Áo phông cotton màu xanh lá', 4, 4, 1, 3, 3),
(100, N'Áo phông ENJOYABLE màu xám', 1, 10, 1, 4, 4),
(100, N'Áo phông ENJOYABLE màu xám', 2, 10, 1, 4, 4),
(100, N'Áo phông ENJOYABLE màu xám', 3, 10, 1, 4, 4),
(100, N'Áo phông ENJOYABLE màu xám', 4, 10, 1, 4, 4),
(100, N'Áo phông ENJOYABLE xanh than', 1, 11, 1, 4, 4),
(100, N'Áo phông ENJOYABLE xanh than', 2, 11, 1, 4, 4),
(100, N'Áo phông ENJOYABLE xanh than', 3, 11, 1, 4, 4),
(100, N'Áo phông ENJOYABLE xanh than', 4, 11, 1, 4, 4),
(100, N'Áo phông ENJOYABLE xanh lá', 1, 4, 1, 4, 4),
(100, N'Áo phông ENJOYABLE xanh lá', 2, 4, 1, 4, 4),
(100, N'Áo phông ENJOYABLE xanh lá', 3, 4, 1, 4, 4),
(100, N'Áo phông ENJOYABLE xanh lá', 4, 4, 1, 4, 4),
(100, N'Áo phông loang màu hồng', 1, 8, 1, 5, 5),
(100, N'Áo phông loang màu hồng', 2, 8, 1, 5, 5),
(100, N'Áo phông loang màu hồng', 3, 8, 1, 5, 5),
(100, N'Áo phông loang màu hồng', 4, 8, 1, 5, 5),
(100, N'Áo phông loang màu xám', 1, 10, 1, 5, 5),
(100, N'Áo phông loang màu xám', 2, 10, 1, 5, 5),
(100, N'Áo phông loang màu xám', 3, 10, 1, 5, 5),
(100, N'Áo phông loang màu xám', 4, 10, 1, 5, 5),
(100, N'Áo phông hodiday màu đen', 1, 1, 1, 5, 6),
(100, N'Áo phông hodiday màu đen', 2, 1, 1, 5, 6),
(100, N'Áo phông hodiday màu đen', 3, 1, 1, 5, 6),
(100, N'Áo phông hodiday màu đen', 4, 1, 1, 5, 6),
(100, N'Áo phông hodiday màu xám', 1, 10, 1, 5, 6),
(100, N'Áo phông hodiday màu xám', 2, 10, 1, 5, 6),
(100, N'Áo phông hodiday màu xám', 3, 10, 1, 5, 6),
(100, N'Áo phông hodiday màu xám', 4, 10, 1, 5, 6),
(100, N'Áo phông hodiday màu be', 1, 12, 1, 5, 6),
(100, N'Áo phông hodiday màu be', 2, 12, 1, 5, 6),
(100, N'Áo phông hodiday màu be', 3, 12, 1, 5, 6),
(100, N'Áo phông hodiday màu be', 4, 12, 1, 5, 6),
(100, N'Áo phông nam nữ 1984 màu đen', 1, 1, 1, 5, 7),
(100, N'Áo phông nam nữ 1984 màu đen', 2, 1, 1, 5, 7),
(100, N'Áo phông nam nữ 1984 màu đen', 3, 1, 1, 5, 7),
(100, N'Áo phông nam nữ 1984 màu đen', 4, 12, 1, 5, 7),
(100, N'Áo phông nam nữ 1984 màu nâu', 1,9, 1, 5, 7),
(100, N'Áo phông nam nữ 1984 màu nâu', 2,9, 1, 5, 7),
(100, N'Áo phông nam nữ 1984 màu nâu', 3,9, 1, 5, 7),
(100, N'Áo phông nam nữ 1984 màu nâu', 4,9, 1, 5, 7),
(100, N'Áo phông nam nữ 1984 màu xám', 1, 10, 1, 5, 7),
(100, N'Áo phông nam nữ 1984 màu xám', 2, 10, 1, 5, 7),
(100, N'Áo phông nam nữ 1984 màu xám', 3, 10, 1, 5, 7),
(100, N'Áo phông nam nữ 1984 màu xám', 4, 10, 1, 5, 7),
(100, N'Áo phông nam nữ oversize màu nâu', 1, 9, 1, 5, 8),
(100, N'Áo phông nam nữ oversize màu nâu', 2, 9, 1, 5, 8),
(100, N'Áo phông nam nữ oversize màu nâu', 3, 9, 1, 5, 8),
(100, N'Áo phông nam nữ oversize màu nâu', 4, 9, 1, 5, 8),
(100, N'Áo phông nam nữ oversize màu xám', 1, 10, 1, 5, 8),
(100, N'Áo phông nam nữ oversize màu xám', 2, 10, 1, 5, 8),
(100, N'Áo phông nam nữ oversize màu xám', 3, 10, 1, 5, 8),
(100, N'Áo phông nam nữ oversize màu xám', 4, 10, 1, 5, 8),
(100, N'Áo phông tay lỡ màu nâu', 1, 9, 1, 5, 9),
(100, N'Áo phông tay lỡ màu nâu', 2, 9, 1, 5, 9),
(100, N'Áo phông tay lỡ màu nâu', 3, 9, 1, 5, 9),
(100, N'Áo phông tay lỡ màu nâu', 4, 9, 1, 5, 9),
(100, N'Áo phông tay lỡ màu hồng', 1, 8, 1, 5, 9),
(100, N'Áo phông tay lỡ màu hồng', 2, 8, 1, 5, 9),
(100, N'Áo phông tay lỡ màu hồng', 3, 8, 1, 5, 9),
(100, N'Áo phông tay lỡ màu hồng', 4, 8, 1, 5, 9),
(100, N'Áo phông tay lỡ màu xanh lá', 1, 4, 1, 5, 9),
(100, N'Áo phông tay lỡ màu xanh lá', 2, 4, 1, 5, 9),
(100, N'Áo phông tay lỡ màu xanh lá', 3, 4, 1, 5, 9),
(100, N'Áo phông tay lỡ màu xanh lá', 4, 4, 1, 5, 9),
(100, N'Áo phông thể thao màu trắng phối đen', 1, 1, 1, 5, 10),
(100, N'Áo phông thể thao màu trắng phối đen', 2, 1, 1, 5, 10),
(100, N'Áo phông thể thao màu trắng phối đen', 3, 1, 1, 5, 10),
(100, N'Áo phông thể thao màu trắng phối đen', 4, 1, 1, 5, 10),
(100, N'Áo phông thể thao màu trắng phối xanh dương', 1, 3, 1, 5, 10),
(100, N'Áo phông thể thao màu trắng phối xanh dương', 2, 3, 1, 5, 10),
(100, N'Áo phông thể thao màu trắng phối xanh dương', 3, 3, 1, 5, 10),
(100, N'Áo phông thể thao màu trắng phối xanh dương', 4, 3, 1, 5, 10),
(100, N'Áo phông thể thao màu trắng phối đỏ', 1, 6, 1, 5, 10),
(100, N'Áo phông thể thao màu trắng phối đỏ', 2, 6, 1, 5, 10),
(100, N'Áo phông thể thao màu trắng phối đỏ', 3, 6, 1, 5, 10),
(100, N'Áo phông thể thao màu trắng phối đỏ', 4, 6, 1, 5, 10),
(100, N'Áo phông SPORT FASHION màu đen',1, 1, 1, 5, 11),
(100, N'Áo phông SPORT FASHION màu đen',2, 1, 1, 5, 11),
(100, N'Áo phông SPORT FASHION màu đen',3, 1, 1, 5, 11),
(100, N'Áo phông SPORT FASHION màu đen',4, 1, 1, 5, 11),
(100, N'Áo phông SPORT FASHION màu đỏ',1, 6, 1, 5, 11),
(100, N'Áo phông SPORT FASHION màu đỏ',2, 6, 1, 5, 11),
(100, N'Áo phông SPORT FASHION màu đỏ',3, 6, 1, 5, 11),
(100, N'Áo phông SPORT FASHION màu đỏ',4, 6, 1, 5, 11),
(100, N'Áo phông SPORT FASHION màu nâu',1, 9, 1, 5, 11),
(100, N'Áo phông SPORT FASHION màu nâu',2, 9, 1, 5, 11),
(100, N'Áo phông SPORT FASHION màu nâu',3, 9, 1, 5, 11),
(100, N'Áo phông SPORT FASHION màu nâu',4, 9, 1, 5, 11),
/* Áo sơ mi */
(100, N'Áo sơ mi đũi nơ thắt eo màu xanh than', 1, 11, 1, 4, 12),
(100, N'Áo sơ mi đũi nơ thắt eo màu xanh than', 2, 11, 1, 4, 12),
(100, N'Áo sơ mi đũi nơ thắt eo màu xanh than', 3, 11, 1, 4, 12),
(100, N'Áo sơ mi đũi nơ thắt eo màu xanh than', 4, 11, 1, 4, 12),
(100, N'Áo sơ mi đũi nơ thắt eo màu xám', 1, 10, 1, 4, 12),
(100, N'Áo sơ mi đũi nơ thắt eo màu xám', 2, 10, 1, 4, 12),
(100, N'Áo sơ mi đũi nơ thắt eo màu xám', 3, 10, 1, 4, 12),
(100, N'Áo sơ mi đũi nơ thắt eo màu xám', 4, 10, 1, 4, 12),
(100, N'Áo sơ mi nam kẻ sọc màu trắng', 1, 2, 1, 4, 13),
(100, N'Áo sơ mi nam kẻ sọc màu trắng', 2, 2, 1, 4, 13),
(100, N'Áo sơ mi nam kẻ sọc màu trắng', 3, 2, 1, 4, 13),
(100, N'Áo sơ mi nam kẻ sọc màu trắng', 4, 2, 1, 4, 13),
(100, N'Áo sơ mi nam kẻ sọc màu đen', 1, 1, 1, 4, 13),
(100, N'Áo sơ mi nam kẻ sọc màu đen', 2, 1, 1, 4, 13),
(100, N'Áo sơ mi nam kẻ sọc màu đen', 3, 1, 1, 4, 13),
(100, N'Áo sơ mi nam kẻ sọc màu đen', 4, 1, 1, 4, 13),

(100, N'Áo sơ mi lụa công sở màu trắng', 1, 2, 2, 4, 14),
(100, N'Áo sơ mi lụa công sở màu trắng', 2, 2, 2, 4, 14),
(100, N'Áo sơ mi lụa công sở màu trắng', 3, 2, 2, 4, 14),
(100, N'Áo sơ mi lụa công sở màu trắng', 4, 2, 2, 4, 14),
(100, N'Áo sơ mi lụa công sở màu xanh dương', 1, 3, 2, 4, 14),
(100, N'Áo sơ mi lụa công sở màu xanh dương', 2, 3, 2, 4, 14),
(100, N'Áo sơ mi lụa công sở màu xanh dương', 3, 3, 2, 4, 14),
(100, N'Áo sơ mi lụa công sở màu xanh dương', 4, 3, 2, 4, 14),
(100, N'Áo sơ mi nam loang màu trắng', 1, 2, 1, 4, 15),
(100, N'Áo sơ mi nam loang màu trắng', 2, 2, 1, 4, 15),
(100, N'Áo sơ mi nam loang màu trắng', 3, 2, 1, 4, 15),
(100, N'Áo sơ mi nam loang màu trắng', 4, 2, 1, 4, 15),
(100, N'Áo sơ mi nam loang màu đen', 1, 1, 1, 4, 15),
(100, N'Áo sơ mi nam loang màu đen', 2, 1, 1, 4, 15),
(100, N'Áo sơ mi nam loang màu đen', 3, 1, 1, 4, 15),
(100, N'Áo sơ mi nam loang màu đen', 4, 1, 1, 4, 15),
(100, N'Áo sơ mi ngắn siết eo màu trắng', 1, 2, 1, 4, 16),
(100, N'Áo sơ mi ngắn siết eo màu trắng', 2, 2, 1, 4, 16),
(100, N'Áo sơ mi ngắn siết eo màu trắng', 3, 2, 1, 4, 16),
(100, N'Áo sơ mi ngắn siết eo màu trắng', 4, 2, 1, 4, 16),
(100, N'Áo sơ mi ngắn siết eo màu xanh dương', 1, 3, 1, 4, 16),
(100, N'Áo sơ mi ngắn siết eo màu xanh dương', 2, 3, 1, 4, 16),
(100, N'Áo sơ mi ngắn siết eo màu xanh dương', 3, 3, 1, 4, 16),
(100, N'Áo sơ mi ngắn siết eo màu xanh dương', 4, 3, 1, 4, 16),
(100, N'Áo sơ mi ngắn túi hộp màu trắng khói',1, 2, 1, 4, 17),
(100, N'Áo sơ mi ngắn túi hộp màu trắng khói',2, 2, 1, 4, 17),
(100, N'Áo sơ mi ngắn túi hộp màu trắng khói',3, 2, 1, 4, 17),
(100, N'Áo sơ mi ngắn túi hộp màu trắng khói',4, 2, 1, 4, 17),
(100, N'Áo sơ mi ngắn túi hộp màu xám',1, 10, 1, 4, 17),
(100, N'Áo sơ mi ngắn túi hộp màu xám',2, 10, 1, 4, 17),
(100, N'Áo sơ mi ngắn túi hộp màu xám',3, 10, 1, 4, 17),
(100, N'Áo sơ mi ngắn túi hộp màu xám',4, 10, 1, 4, 17),
(100, N'Áo sơ mi ngắn túi hộp màu vàng',1, 7, 1, 4, 17),
(100, N'Áo sơ mi ngắn túi hộp màu vàng',2, 7, 1, 4, 17),
(100, N'Áo sơ mi ngắn túi hộp màu vàng',3, 7, 1, 4, 17),
(100, N'Áo sơ mi ngắn túi hộp màu vàng',4, 7, 1, 4, 17),
(100, N'Áo sơ mi phong cách Nhật màu nâu',1, 9, 1, 4, 18),
(100, N'Áo sơ mi phong cách Nhật màu nâu',2, 9, 1, 4, 18),
(100, N'Áo sơ mi phong cách Nhật màu nâu',3, 9, 1, 4, 18),
(100, N'Áo sơ mi phong cách Nhật màu nâu',4, 9, 1, 4, 18),
(100, N'Áo sơ mi phong cách Nhật màu be',1, 12, 1, 4, 18),
(100, N'Áo sơ mi phong cách Nhật màu be',2, 12, 1, 4, 18),
(100, N'Áo sơ mi phong cách Nhật màu be',3, 12, 1, 4, 18),
(100, N'Áo sơ mi phong cách Nhật màu be',4, 12, 1, 4, 18),
(100, N'Áo sơ mi phong cách Nhật màu xanh dương',1, 3, 1, 4, 18),
(100, N'Áo sơ mi phong cách Nhật màu xanh dương',2, 3, 1, 4, 18),
(100, N'Áo sơ mi phong cách Nhật màu xanh dương',3, 3, 1, 4, 18),
(100, N'Áo sơ mi phong cách Nhật màu xanh dương',4, 3, 1, 4, 18),
(100, N'Áo sơ mi kẻ sọc màu xanh dương',1, 3, 1, 4, 19),
(100, N'Áo sơ mi kẻ sọc màu xanh dương',2, 3, 1, 4, 19),
(100, N'Áo sơ mi kẻ sọc màu xanh dương',3, 3, 1, 4, 19),
(100, N'Áo sơ mi kẻ sọc màu xanh dương',4, 3, 1, 4, 19),
(100, N'Áo sơ mi kẻ sọc màu hồng',1,8, 1, 4, 19),
(100, N'Áo sơ mi kẻ sọc màu hồng',2,8, 1, 4, 19),
(100, N'Áo sơ mi kẻ sọc màu hồng',3,8, 1, 4, 19),
(100, N'Áo sơ mi kẻ sọc màu hồng',4,8, 1, 4, 19),
(100, N'Áo sơ mi tay ngắn màu trắng',1,2, 1, 4, 20),
(100, N'Áo sơ mi tay ngắn màu trắng',2,2, 1, 4, 20),
(100, N'Áo sơ mi tay ngắn màu trắng',3,2, 1, 4, 20),
(100, N'Áo sơ mi tay ngắn màu trắng',4,2, 1, 4, 20),
(100, N'Áo sơ mi tay ngắn màu vàng',1,7, 1, 4, 20),
(100, N'Áo sơ mi tay ngắn màu vàng',2,7, 1, 4, 20),
(100, N'Áo sơ mi tay ngắn màu vàng',3,7, 1, 4, 20),
(100, N'Áo sơ mi tay ngắn màu vàng',4,7, 1, 4, 20),
(100, N'Áo sơ mi trơn màu xanh lá',1,4, 1, 4, 21),
(100, N'Áo sơ mi trơn màu xanh lá',2,4, 1, 4, 21),
(100, N'Áo sơ mi trơn màu xanh lá',3,4, 1, 4, 21),
(100, N'Áo sơ mi trơn màu xanh lá',4,4, 1, 4, 21),
(100, N'Áo sơ mi trơn màu hồng',1,8, 1, 4, 21),
(100, N'Áo sơ mi trơn màu hồng',2,8, 1, 4, 21),
(100, N'Áo sơ mi trơn màu hồng',3,8, 1, 4, 21),
(100, N'Áo sơ mi trơn màu hồng',4,8, 1, 4, 21),
(100, N'Áo sơ mi trơn màu xanh dương',1,3, 1, 4, 21),
(100, N'Áo sơ mi trơn màu xanh dương',2,3, 1, 4, 21),
(100, N'Áo sơ mi trơn màu xanh dương',3,3, 1, 4, 21),
(100, N'Áo sơ mi trơn màu xanh dương',4,3, 1, 4, 21),

(100, N'Áo ấm lông cừu màu be', 1, 12, 1, 4, 22),
(100, N'Áo ấm lông cừu màu be', 2, 12, 1, 4, 22),
(100, N'Áo ấm lông cừu màu be', 3, 12, 1, 4, 22),
(100, N'Áo ấm lông cừu màu be', 4, 12, 1, 4, 22),
(100, N'Áo ấm lông cừu màu hồng', 1, 8, 1, 4, 22),
(100, N'Áo ấm lông cừu màu hồng', 2, 8, 1, 4, 22),
(100, N'Áo ấm lông cừu màu hồng', 3, 8, 1, 4, 22),
(100, N'Áo ấm lông cừu màu hồng', 4, 8, 1, 4, 22),
(100, N'Áo ấm lông cừu màu đen', 1, 1, 1, 4, 22),
(100, N'Áo ấm lông cừu màu đen', 2, 1, 1, 4, 22),
(100, N'Áo ấm lông cừu màu đen', 3, 1, 1, 4, 22),
(100, N'Áo ấm lông cừu màu đen', 4, 1, 1, 4, 22),

(100, N'Áo béo buộc nơ màu đen', 1, 1, 1, 4, 23),
(100, N'Áo béo buộc nơ màu đen', 2, 1, 1, 4, 23),
(100, N'Áo béo buộc nơ màu đen', 3, 1, 1, 4, 23),
(100, N'Áo béo buộc nơ màu đen', 4, 1, 1, 4, 23),
(100, N'Áo béo buộc nơ màu nâu', 1, 9, 1, 4, 23),
(100, N'Áo béo buộc nơ màu nâu', 2, 9, 1, 4, 23),
(100, N'Áo béo buộc nơ màu nâu', 3, 9, 1, 4, 23),
(100, N'Áo béo buộc nơ màu nâu', 4, 9, 1, 4, 23),
(100, N'Áo béo buộc nơ màu be', 1, 12, 1, 4, 23),
(100, N'Áo béo buộc nơ màu be', 2, 12, 1, 4, 23),
(100, N'Áo béo buộc nơ màu be', 3, 12, 1, 4, 23),
(100, N'Áo béo buộc nơ màu be', 4, 12, 1, 4, 23),
(100, N'Áo phao bông màu đen', 1, 1, 1, 4, 24),
(100, N'Áo phao bông màu đen', 2, 1, 1, 4, 24),
(100, N'Áo phao bông màu đen', 3, 1, 1, 4, 24),
(100, N'Áo phao bông màu đen', 4, 1, 1, 4, 24),
(100, N'Áo phao bông màu trắng', 1, 2, 1, 4, 24),
(100, N'Áo phao bông màu trắng', 2, 2, 1, 4, 24),
(100, N'Áo phao bông màu trắng', 3, 2, 1, 4, 24),
(100, N'Áo phao bông màu trắng', 4, 2, 1, 4, 24),
(100, N'Áo phao bông màu hồng', 1, 8, 1, 4, 24),
(100, N'Áo phao bông màu hồng', 2, 8, 1, 4, 24),
(100, N'Áo phao bông màu hồng', 3, 8, 1, 4, 24),
(100, N'Áo phao bông màu hồng', 4, 8, 1, 4, 24),
(100, N'Áo phao cài khuy màu đen', 1, 1, 1, 4, 25),
(100, N'Áo phao cài khuy màu đen', 2, 1, 1, 4, 25),
(100, N'Áo phao cài khuy màu đen', 3, 1, 1, 4, 25),
(100, N'Áo phao cài khuy màu đen', 4, 1, 1, 4, 25),
(100, N'Áo phao cài khuy màu vàng', 1, 7, 1, 4, 25),
(100, N'Áo phao cài khuy màu vàng', 2, 7, 1, 4, 25),
(100, N'Áo phao cài khuy màu vàng', 3, 7, 1, 4, 25),
(100, N'Áo phao cài khuy màu vàng', 4, 7, 1, 4, 25),
(100, N'Áo phao cài khuy màu be', 1, 12, 1, 4, 25),
(100, N'Áo phao cài khuy màu be', 2, 12, 1, 4, 25),
(100, N'Áo phao cài khuy màu be', 3, 12, 1, 4, 25),
(100, N'Áo phao cài khuy màu be', 4, 12, 1, 4, 25),
(100, N'Áo phao gile màu đen', 1, 1, 1, 4, 26),
(100, N'Áo phao gile màu đen', 2, 1, 1, 4, 26),
(100, N'Áo phao gile màu đen', 3, 1, 1, 4, 26),
(100, N'Áo phao gile màu đen', 4, 1, 1, 4, 26),
(100, N'Áo phao gile màu xanh rêu', 2, 5, 1, 4, 26),
(100, N'Áo phao gile màu xanh rêu', 2, 5, 1, 4, 26),
(100, N'Áo phao gile màu xanh rêu', 2, 5, 1, 4, 26),
(100, N'Áo phao gile màu xanh rêu', 2, 5, 1, 4, 26),
(100, N'Áo phao gile màu trắng', 1, 2, 1, 4, 26),
(100, N'Áo phao gile màu trắng', 2, 2, 1, 4, 26),
(100, N'Áo phao gile màu trắng', 3, 2, 1, 4, 26),
(100, N'Áo phao gile màu trắng', 4, 2, 1, 4, 26),
(100, N'Áo phao dáng ngắn khuy cổ màu hồng', 1, 8, 1, 4, 27),
(100, N'Áo phao dáng ngắn khuy cổ màu hồng', 2, 8, 1, 4, 27),
(100, N'Áo phao dáng ngắn khuy cổ màu hồng', 3, 8, 1, 4, 27),
(100, N'Áo phao dáng ngắn khuy cổ màu hồng', 4, 8, 1, 4, 27),
(100, N'Áo phao dáng ngắn khuy cổ màu vàng', 1, 7, 1, 4, 27),
(100, N'Áo phao dáng ngắn khuy cổ màu vàng', 2, 7, 1, 4, 27),
(100, N'Áo phao dáng ngắn khuy cổ màu vàng', 3, 7, 1, 4, 27),
(100, N'Áo phao dáng ngắn khuy cổ màu vàng', 4, 7, 1, 4, 27),
(100, N'Áo phao dáng ngắn khuy cổ màu đen', 1, 1, 1, 4, 27),
(100, N'Áo phao dáng ngắn khuy cổ màu đen', 2, 1, 1, 4, 27),
(100, N'Áo phao dáng ngắn khuy cổ màu đen', 3, 1, 1, 4, 27),
(100, N'Áo phao dáng ngắn khuy cổ màu đen', 4, 1, 1, 4, 27),
(100, N'Áo phao dáng lửng thời trang màu trắng',1, 2, 1, 4, 28),
(100, N'Áo phao dáng lửng thời trang màu trắng',2, 2, 1, 4, 28),
(100, N'Áo phao dáng lửng thời trang màu trắng',3, 2, 1, 4, 28),
(100, N'Áo phao dáng lửng thời trang màu trắng',4, 2, 1, 4, 28),
(100, N'Áo phao dáng lửng thời trang màu nâu',1, 9, 1, 4, 28),
(100, N'Áo phao dáng lửng thời trang màu nâu',2, 9, 1, 4, 28),
(100, N'Áo phao dáng lửng thời trang màu nâu',3, 9, 1, 4, 28),
(100, N'Áo phao dáng lửng thời trang màu nâu',4, 9, 1, 4, 28),
(100, N'Áo phao dáng lửng thời trang màu đen',1, 1, 1, 4, 28),
(100, N'Áo phao dáng lửng thời trang màu đen',2, 1, 1, 4, 28),
(100, N'Áo phao dáng lửng thời trang màu đen',3, 1, 1, 4, 28),
(100, N'Áo phao dáng lửng thời trang màu đen',4, 1, 1, 4, 28),

(100, N'Áo phao béo nhung cừu màu hồng ',1, 1, 1, 4, 29),
(100, N'Áo phao béo nhung cừu màu hồng ',2, 1, 1, 4, 29),
(100, N'Áo phao béo nhung cừu màu hồng ',3, 1, 1, 4, 29),
(100, N'Áo phao béo nhung cừu màu hồng ',4, 1, 1, 4, 29),
(100, N'Áo phao béo nhung cừu màu đen ',1, 1, 1, 4, 29),
(100, N'Áo phao béo nhung cừu màu đen ',2, 1, 1, 4, 29),
(100, N'Áo phao béo nhung cừu màu đen ',3, 1, 1, 4, 29),
(100, N'Áo phao béo nhung cừu màu đen ',4, 1, 1, 4, 29),
(100, N'Áo phao béo nhung cừu màu trắng ',1, 1, 1, 4, 29),
(100, N'Áo phao béo nhung cừu màu trắng ',2, 1, 1, 4, 29),
(100, N'Áo phao béo nhung cừu màu trắng ',3, 1, 1, 4, 29),
(100, N'Áo phao béo nhung cừu màu trắng ',4, 1, 1, 4, 29),
(100, N'Áo phao ấm NIKE màu đen ',1, 1, 1, 4, 30),
(100, N'Áo phao ấm NIKE màu đen ',2, 1, 1, 4, 30),
(100, N'Áo phao ấm NIKE màu đen ',3, 1, 1, 4, 30),
(100, N'Áo phao ấm NIKE màu đen ',4, 1, 1, 4, 30),
(100, N'Áo phao ấm NIKE màu ghi ',1, 14, 1, 4, 30),
(100, N'Áo phao ấm NIKE màu ghi ',2, 14, 1, 4, 30),
(100, N'Áo phao ấm NIKE màu ghi ',3, 14, 1, 4, 30),
(100, N'Áo phao ấm NIKE màu ghi ',4, 14, 1, 4, 30),
(100, N'Áo phao ấm NIKE màu trắng ',1, 2, 1, 4, 30),
(100, N'Áo phao ấm NIKE màu trắng ',2, 2, 1, 4, 30),
(100, N'Áo phao ấm NIKE màu trắng ',3, 2, 1, 4, 30),
(100, N'Áo phao ấm NIKE màu trắng ',4, 2, 1, 4, 30),

(100, N'Áo chống nắng viền tròn màu hồng', 1, 8, 2, 4, 31),
(100, N'Áo chống nắng viền tròn màu hồng', 2, 8, 2, 4, 31),
(100, N'Áo chống nắng viền tròn màu hồng', 3, 8, 2, 4, 31),
(100, N'Áo chống nắng viền tròn màu hồng', 4, 8, 2, 4, 31),
(100, N'Áo chống nắng viền tròn màu đen', 1, 1, 2, 4, 31),
(100, N'Áo chống nắng viền tròn màu đen', 2, 1, 2, 4, 31),
(100, N'Áo chống nắng viền tròn màu đen', 3, 1, 2, 4, 31),
(100, N'Áo chống nắng viền tròn màu đen', 4, 1, 2, 4, 31),
(100, N'Áo chống nắng viền tròn màu xám', 1, 10, 2, 4, 31),
(100, N'Áo chống nắng viền tròn màu xám', 2, 10, 2, 4, 31),
(100, N'Áo chống nắng viền tròn màu xám', 3, 10, 2, 4, 31),
(100, N'Áo chống nắng viền tròn màu xám', 4, 10, 2, 4, 31),
(100, N'Áo chống nắng toàn thân màu xanh dương', 1, 3, 2, 4, 32),
(100, N'Áo chống nắng toàn thân màu xanh dương', 2, 3, 2, 4, 32),
(100, N'Áo chống nắng toàn thân màu xanh dương', 3, 3, 2, 4, 32),
(100, N'Áo chống nắng toàn thân màu xanh dương', 4, 3, 2, 4, 32),
(100, N'Áo chống nắng toàn thân màu xanh xám', 1, 10, 2, 4, 32),
(100, N'Áo chống nắng toàn thân màu xanh xám', 2, 10, 2, 4, 32),
(100, N'Áo chống nắng toàn thân màu xanh xám', 3, 10, 2, 4, 32),
(100, N'Áo chống nắng toàn thân màu xanh xám', 4, 10, 2, 4, 32),
(100, N'Áo chống nắng toàn thân màu xanh đen', 1, 2, 2, 4, 32),
(100, N'Áo chống nắng toàn thân màu xanh đen', 2, 2, 2, 4, 32),
(100, N'Áo chống nắng toàn thân màu xanh đen', 3, 2, 2, 4, 32),
(100, N'Áo chống nắng toàn thân màu xanh đen', 4, 2, 2, 4, 32),
(100, N'Áo chống nắng thông hơi màu hồng', 1, 8, 2, 4, 33),
(100, N'Áo chống nắng thông hơi màu hồng', 2, 8, 2, 4, 33),
(100, N'Áo chống nắng thông hơi màu hồng', 3, 8, 2, 4, 33),
(100, N'Áo chống nắng thông hơi màu hồng', 4, 8, 2, 4, 33),
(100, N'Áo chống nắng thông hơi màu xanh dương', 1, 3, 2, 4, 33),
(100, N'Áo chống nắng thông hơi màu xanh dương', 2, 3, 2, 4, 33),
(100, N'Áo chống nắng thông hơi màu xanh dương', 3, 3, 2, 4, 33),
(100, N'Áo chống nắng thông hơi màu xanh dương', 4, 3, 2, 4, 33),
(100, N'Áo chống nắng thông hơi màu trắng', 1, 1, 2, 4, 33),
(100, N'Áo chống nắng thông hơi màu trắng', 2, 1, 2, 4, 33),
(100, N'Áo chống nắng thông hơi màu trắng', 3, 1, 2, 4, 33),
(100, N'Áo chống nắng thông hơi màu trắng', 4, 1, 2, 4, 33),
(100, N'Áo chống nắng thời trang màu trắng', 1, 2, 2, 4, 34),
(100, N'Áo chống nắng thời trang màu trắng', 2, 2, 2, 4, 34),
(100, N'Áo chống nắng thời trang màu trắng', 3, 2, 2, 4, 34),
(100, N'Áo chống nắng thời trang màu trắng', 4, 2, 2, 4, 34),
(100, N'Áo chống nắng thời trang màu hồng', 1, 8, 2, 4, 34),
(100, N'Áo chống nắng thời trang màu hồng', 2, 8, 2, 4, 34),
(100, N'Áo chống nắng thời trang màu hồng', 3, 8, 2, 4, 34),
(100, N'Áo chống nắng thời trang màu hồng', 4, 8, 2, 4, 34),
(100, N'Áo chống nắng thể thao màu xám', 1, 10, 2, 4, 35),
(100, N'Áo chống nắng thể thao màu xám', 2, 10, 2, 4, 35),
(100, N'Áo chống nắng thể thao màu xám', 3, 10, 2, 4, 35),
(100, N'Áo chống nắng thể thao màu xám', 4, 10, 2, 4, 35),
(100, N'Áo chống nắng thể thao màu xanh đen', 1, 1, 2, 4, 35),
(100, N'Áo chống nắng thể thao màu xanh đen', 2, 1, 2, 4, 35),
(100, N'Áo chống nắng thể thao màu xanh đen', 3, 1, 2, 4, 35),
(100, N'Áo chống nắng thể thao màu xanh đen', 4, 1, 2, 4, 35),
(100, N'Áo chống nắng LV màu xanh than',1, 11, 2, 4, 36),
(100, N'Áo chống nắng LV màu xanh than',2, 11, 2, 4, 36),
(100, N'Áo chống nắng LV màu xanh than',3, 11, 2, 4, 36),
(100, N'Áo chống nắng LV màu xanh than',4, 11, 2, 4, 36),
(100, N'Áo chống nắng LV màu xám',1, 10, 2, 4, 36),
(100, N'Áo chống nắng LV màu xám',2, 10, 2, 4, 36),
(100, N'Áo chống nắng LV màu xám',3, 10, 2, 4, 36),
(100, N'Áo chống nắng LV màu xám',4, 10, 2, 4, 36),
(100, N'Áo chống nắng LV màu đen',1, 1, 2, 4, 36),
(100, N'Áo chống nắng LV màu đen',2, 1, 2, 4, 36),
(100, N'Áo chống nắng LV màu đen',3, 1, 2, 4, 36),
(100, N'Áo chống nắng LV màu đen',4, 1, 2, 4, 36),
(100, N'Áo chống nắng toàn thân dài xòe màu hồng ',1, 8, 2, 4, 37),
(100, N'Áo chống nắng toàn thân dài xòe màu hồng ',2, 8, 2, 4, 37),
(100, N'Áo chống nắng toàn thân dài xòe màu hồng ',3, 8, 2, 4, 37),
(100, N'Áo chống nắng toàn thân dài xòe màu hồng ',4, 8, 2, 4, 37),
(100, N'Áo chống nắng toàn thân dài xòe màu ghi ',1, 14, 2, 4, 37),
(100, N'Áo chống nắng toàn thân dài xòe màu ghi ',2, 14, 2, 4, 37),
(100, N'Áo chống nắng toàn thân dài xòe màu ghi ',3, 14, 2, 4, 37),
(100, N'Áo chống nắng toàn thân dài xòe màu ghi ',4, 14, 2, 4, 37),
(100, N'Áo chống nắng toàn thân dài xòe màu đen ',1, 1, 2, 4, 37),
(100, N'Áo chống nắng toàn thân dài xòe màu đen ',2, 1, 2, 4, 37),
(100, N'Áo chống nắng toàn thân dài xòe màu đen ',3, 1, 2, 4, 37),
(100, N'Áo chống nắng toàn thân dài xòe màu đen ',4, 1, 2, 4, 37),


(100, N'Quần baggy kaki màu vàng', 1, 7, 2, 4, 38),
(100, N'Quần baggy kaki màu vàng', 2, 7, 2, 4, 38),
(100, N'Quần baggy kaki màu vàng', 3, 7, 2, 4, 38),
(100, N'Quần baggy kaki màu vàng', 4, 7, 2, 4, 38),
(100, N'Quần baggy kaki màu đen', 1, 1, 2, 4, 38),
(100, N'Quần baggy kaki màu đen', 2, 1, 2, 4, 38),
(100, N'Quần baggy kaki màu đen', 3, 1, 2, 4, 38),
(100, N'Quần baggy kaki màu đen', 4, 1, 2, 4, 38),
(100, N'Quần baggy kaki màu xám', 1, 10, 2, 4, 38),
(100, N'Quần baggy kaki màu xám', 2, 10, 2, 4, 38),
(100, N'Quần baggy kaki màu xám', 3, 10, 2, 4, 38),
(100, N'Quần baggy kaki màu xám', 4, 10, 2, 4, 38),
(100, N'Quần đũi dài nam màu đen', 1, 1, 2, 4, 39),
(100, N'Quần đũi dài nam màu đen', 2, 1, 2, 4, 39),
(100, N'Quần đũi dài nam màu đen', 3, 1, 2, 4, 39),
(100, N'Quần đũi dài nam màu đen', 4, 1, 2, 4, 39),
(100, N'Quần đũi dài nam màu be', 1, 12, 2, 4, 39),
(100, N'Quần đũi dài nam màu be', 2, 12, 2, 4, 39),
(100, N'Quần đũi dài nam màu be', 3, 12, 2, 4, 39),
(100, N'Quần đũi dài nam màu be', 4, 12, 2, 4, 39),
(100, N'Quần đũi dài nữ màu xanh dương', 1, 3, 2, 4, 40),
(100, N'Quần đũi dài nữ màu xanh dương', 2, 3, 2, 4, 40),
(100, N'Quần đũi dài nữ màu xanh dương', 3, 3, 2, 4, 40),
(100, N'Quần đũi dài nữ màu xanh dương', 4, 3, 2, 4, 40),
(100, N'Quần đũi dài nữ màu đen', 1, 1, 2, 4, 40),
(100, N'Quần đũi dài nữ màu đen', 2, 1, 2, 4, 40),
(100, N'Quần đũi dài nữ màu đen', 3, 1, 2, 4, 40),
(100, N'Quần đũi dài nữ màu đen', 4, 1, 2, 4, 40),
(100, N'Quần đũi dài nữ màu hồng', 1, 8, 2, 4, 40),
(100, N'Quần đũi dài nữ màu hồng', 2, 8, 2, 4, 40),
(100, N'Quần đũi dài nữ màu hồng', 3, 8, 2, 4, 40),
(100, N'Quần đũi dài nữ màu hồng', 4, 8, 2, 4, 40),
(100, N'Quần ống rộng cạp cao màu trắng', 1, 2, 2, 4, 41),
(100, N'Quần ống rộng cạp cao màu trắng', 2, 2, 2, 4, 41),
(100, N'Quần ống rộng cạp cao màu trắng', 3, 2, 2, 4, 41),
(100, N'Quần ống rộng cạp cao màu trắng', 4, 2, 2, 4, 41),
(100, N'Quần ống rộng cạp cao màu đen', 1, 1, 2, 4, 41),
(100, N'Quần ống rộng cạp cao màu đen', 2, 1, 2, 4, 41),
(100, N'Quần ống rộng cạp cao màu đen', 3, 1, 2, 4, 41),
(100, N'Quần ống rộng cạp cao màu đen', 4, 1, 2, 4, 41),
(100, N'Quần ống rộng cạp cao màu be', 1, 12, 2, 4, 41),
(100, N'Quần ống rộng cạp cao màu be', 2, 12, 2, 4, 41),
(100, N'Quần ống rộng cạp cao màu be', 3, 12, 2, 4, 41),
(100, N'Quần ống rộng cạp cao màu be', 4, 12, 2, 4, 41),
(100, N'Quần ống rộng nam đen', 1, 1, 2, 4, 42),
(100, N'Quần ống rộng nam đen', 2, 1, 2, 4, 42),
(100, N'Quần ống rộng nam đen', 3, 1, 2, 4, 42),
(100, N'Quần ống rộng nam đen', 4, 1, 2, 4, 42),
(100, N'Quần ống rộng nam be', 1, 12, 2, 4, 42),
(100, N'Quần ống rộng nam be', 2, 12, 2, 4, 42),
(100, N'Quần ống rộng nam be', 3, 12, 2, 4, 42),
(100, N'Quần ống rộng nam be', 4, 12, 2, 4, 42),

(100, N'Quần ống rộng túi hộp màu xanh rêu', 1, 5, 2, 4, 43),
(100, N'Quần ống rộng túi hộp màu xanh rêu', 1, 5, 2, 4, 43),
(100, N'Quần ống rộng túi hộp màu xanh rêu', 1, 5, 2, 4, 43),
(100, N'Quần ống rộng túi hộp màu xanh rêu', 1, 5, 2, 4, 43),
(100, N'Quần ống rộng túi hộp màu đen', 1,1, 2, 4, 43),
(100, N'Quần ống rộng túi hộp màu đen', 2,1, 2, 4, 43),
(100, N'Quần ống rộng túi hộp màu đen', 3,1, 2, 4, 43),
(100, N'Quần ống rộng túi hộp màu đen', 4,1, 2, 4, 43),
(100, N'Quần ống rộng túi hộp màu trắng', 1,2, 2, 4, 43),
(100, N'Quần ống rộng túi hộp màu trắng', 2,2, 2, 4, 43),
(100, N'Quần ống rộng túi hộp màu trắng', 3,2, 2, 4, 43),
(100, N'Quần ống rộng túi hộp màu trắng', 4,2, 2, 4, 43),

(100, N'Quần ống suông chun basic màu đen',1,2, 2, 4, 44),
(100, N'Quần ống suông chun basic màu đen',2,2, 2, 4, 44),
(100, N'Quần ống suông chun basic màu đen',3,2, 2, 4, 44),
(100, N'Quần ống suông chun basic màu đen',4,2, 2, 4, 44),
(100, N'Quần ống suông chun basic màu be',1,12, 2, 4, 44),
(100, N'Quần ống suông chun basic màu be',2,12, 2, 4, 44),
(100, N'Quần ống suông chun basic màu be',3,12, 2, 4, 44),
(100, N'Quần ống suông chun basic màu be',4,12, 2, 4, 44),
(100, N'Quần ống suông chun basic màu trắng',1,2, 2, 4, 44),
(100, N'Quần ống suông chun basic màu trắng',2,2, 2, 4, 44),
(100, N'Quần ống suông chun basic màu trắng',3,2, 2, 4, 44),
(100, N'Quần ống suông chun basic màu trắng',4,2, 2, 4, 44),
(100, N'Quần ống rộng suông màu nâu',1,9, 2, 4, 45),
(100, N'Quần ống rộng suông màu nâu',2,9, 2, 4, 45),
(100, N'Quần ống rộng suông màu nâu',3,9, 2, 4, 45),
(100, N'Quần ống rộng suông màu nâu',4,9, 2, 4, 45),
(100, N'Quần ống rộng suông màu vàng',1,7, 2, 4, 45),
(100, N'Quần ống rộng suông màu vàng',2,7, 2, 4, 45),
(100, N'Quần ống rộng suông màu vàng',3,7, 2, 4, 45),
(100, N'Quần ống rộng suông màu vàng',4,7, 2, 4, 45),


(100, N'Quần ống rộng suông màu vàng', 1, 7, 2, 4, 46),
(100, N'Quần ống rộng suông màu vàng', 2, 7, 2, 4, 46),
(100, N'Quần ống rộng suông màu vàng', 3, 7, 2, 4, 46),
(100, N'Quần ống rộng suông màu vàng', 4, 7, 2, 4, 46),
(100, N'Quần ống rộng suông màu xanh than', 1, 11, 2, 4, 46),
(100, N'Quần ống rộng suông màu xanh than', 2, 11, 2, 4, 46),
(100, N'Quần ống rộng suông màu xanh than', 3, 11, 2, 4, 46),
(100, N'Quần ống rộng suông màu xanh than', 4, 11, 2, 4, 46),
(100, N'Quần ống rộng suông màu xám', 1, 10, 2, 4, 46),
(100, N'Quần ống rộng suông màu xám', 2, 10, 2, 4, 46),
(100, N'Quần ống rộng suông màu xám', 3, 10, 2, 4, 46),
(100, N'Quần ống rộng suông màu xám', 4, 10, 2, 4, 46),

(100, N'Quần sort nữ cá tính màu nâu', 1, 9, 2, 4, 47),
(100, N'Quần sort nữ cá tính màu nâu', 2, 9, 2, 4, 47),
(100, N'Quần sort nữ cá tính màu nâu', 3, 9, 2, 4, 47),
(100, N'Quần sort nữ cá tính màu nâu', 4, 9, 2, 4, 47),
(100, N'Quần sort nữ cá tính màu đen', 1, 1, 2, 4, 47),
(100, N'Quần sort nữ cá tính màu đen', 2, 1, 2, 4, 47),
(100, N'Quần sort nữ cá tính màu đen', 3, 1, 2, 4, 47),
(100, N'Quần sort nữ cá tính màu đen', 4, 1, 2, 4, 47),
(100, N'Quần sort nữ đũi màu trắng', 1, 2, 3, 4, 48),
(100, N'Quần sort nữ đũi màu trắng', 2, 2, 3, 4, 48),
(100, N'Quần sort nữ đũi màu trắng', 3, 2, 3, 4, 48),
(100, N'Quần sort nữ đũi màu trắng', 4, 2, 3, 4, 48),
(100, N'Quần sort nữ đũi màu đen', 1, 1, 3, 4, 48),
(100, N'Quần sort nữ đũi màu đen', 2, 1, 3, 4, 48),
(100, N'Quần sort nữ đũi màu đen', 3, 1, 3, 4, 48),
(100, N'Quần sort nữ đũi màu đen', 4, 1, 3, 4, 48),
(100, N'Quần sort nữ đũi màu hồng', 1, 8, 3, 4, 48),
(100, N'Quần sort nữ đũi màu hồng', 2, 8, 3, 4, 48),
(100, N'Quần sort nữ đũi màu hồng', 3, 8, 3, 4, 48),
(100, N'Quần sort nữ đũi màu hồng', 4, 8, 3, 4, 48),
(100, N'Quần đùi túi hộp đứng màu xanh rêu', 1, 5, 3, 4, 49),
(100, N'Quần đùi túi hộp đứng màu xanh rêu', 2, 5, 3, 4, 49),
(100, N'Quần đùi túi hộp đứng màu xanh rêu', 3, 5, 3, 4, 49),
(100, N'Quần đùi túi hộp đứng màu xanh rêu', 4, 5, 3, 4, 49),
(100, N'Quần đùi túi hộp đứng màu đen', 1, 2, 3, 4, 49),
(100, N'Quần đùi túi hộp đứng màu đen', 2, 2, 3, 4, 49),
(100, N'Quần đùi túi hộp đứng màu đen', 3, 2, 3, 4, 49),
(100, N'Quần đùi túi hộp đứng màu đen', 4, 2, 3, 4, 49),
(100, N'Quần jean cạp trễ màu xanh dương', 1, 3, 2, 4, 50),
(100, N'Quần jean cạp trễ màu xanh dương', 2, 3, 2, 4, 50),
(100, N'Quần jean cạp trễ màu xanh dương', 3, 3, 2, 4, 50),
(100, N'Quần jean cạp trễ màu xanh dương', 4, 3, 2, 4, 50),
(100, N'Quần jean cạp trễ màu xám', 1, 10, 2, 4, 50),
(100, N'Quần jean cạp trễ màu xám', 2, 10, 2, 4, 50),
(100, N'Quần jean cạp trễ màu xám', 3, 10, 2, 4, 50),
(100, N'Quần jean cạp trễ màu xám', 4, 10, 2, 4, 50),
(100, N'Quần jean cạp trễ màu hồng', 1, 8, 2, 4, 50),
(100, N'Quần jean cạp trễ màu hồng', 2, 8, 2, 4, 50),
(100, N'Quần jean cạp trễ màu hồng', 3, 8, 2, 4, 50),
(100, N'Quần jean cạp trễ màu hồng', 4, 8, 2, 4, 50),
(100, N'Quần jean thời trang màu hồng',1, 8, 2, 4, 51),
(100, N'Quần jean thời trang màu hồng',2, 8, 2, 4, 51),
(100, N'Quần jean thời trang màu hồng',3, 8, 2, 4, 51),
(100, N'Quần jean thời trang màu hồng',4, 8, 2, 4, 51),
(100, N'Quần jean thời trang màu nâu',1, 9, 2, 4, 51),
(100, N'Quần jean thời trang màu nâu',2, 9, 2, 4, 51),
(100, N'Quần jean thời trang màu nâu',3, 9, 2, 4, 51),
(100, N'Quần jean thời trang màu nâu',4, 9, 2, 4, 51),
(100, N'Quần jean thời trang màu đen',1, 1, 2, 4, 51),
(100, N'Quần jean thời trang màu đen',2, 1, 2, 4, 51),
(100, N'Quần jean thời trang màu đen',3, 1, 2, 4, 51),
(100, N'Quần jean thời trang màu đen',4, 1, 2, 4, 51),
(100, N'Quần sort bò ống rộng màu xanh dương',1, 1, 2, 4, 52),
(100, N'Quần sort bò ống rộng màu xanh dương',2, 1, 2, 4, 52),
(100, N'Quần sort bò ống rộng màu xanh dương',3, 1, 2, 4, 52),
(100, N'Quần sort bò ống rộng màu xanh dương',4, 1, 2, 4, 52),
(100, N'Quần sort bò ống rộng màu đen',1, 1, 2, 4, 52),
(100, N'Quần sort bò ống rộng màu đen',2, 1, 2, 4, 52),
(100, N'Quần sort bò ống rộng màu đen',3, 1, 2, 4, 52),
(100, N'Quần sort bò ống rộng màu đen',4, 1, 2, 4, 52),
(100, N'Quần sort tây nam màu be',1,12,2, 4, 53),
(100, N'Quần sort tây nam màu be',2,12,2, 4, 53),
(100, N'Quần sort tây nam màu be',3,12,2, 4, 53),
(100, N'Quần sort tây nam màu be',4,12,2, 4, 53),
(100, N'Quần sort tây nam màu nâu',1,9,2, 4, 53),
(100, N'Quần sort tây nam màu nâu',2,9,2, 4, 53),
(100, N'Quần sort tây nam màu nâu',3,9,2, 4, 53),
(100, N'Quần sort tây nam màu nâu',4,9,2, 4, 53),
(100, N'Quần sort tây nam màu đen',1,1,1, 4, 53),
(100, N'Quần sort tây nam màu đen',2,1,1, 4, 53),
(100, N'Quần sort tây nam màu đen',3,1,1, 4, 53),
(100, N'Quần sort tây nam màu đen',4,1,1, 4, 53),
(100, N'Quần suông nam dài basic màu đen',1,1,1, 4,54),
(100, N'Quần suông nam dài basic màu đen',2,1,1, 4,54),
(100, N'Quần suông nam dài basic màu đen',3,1,1, 4,54),
(100, N'Quần suông nam dài basic màu đen',4,1,1, 4,54),
(100, N'Quần suông nam dài basic màu trắng',1,2,1, 4,54),
(100, N'Quần suông nam dài basic màu trắng',2,2,1, 4,54),
(100, N'Quần suông nam dài basic màu trắng',3,2,1, 4,54),
(100, N'Quần suông nam dài basic màu trắng',4,2,1, 4,54),
(100, N'Quần suông nam dài basic màu xanh dương',1,3,1, 4,54),
(100, N'Quần suông nam dài basic màu xanh dương',2,3,1, 4,54),
(100, N'Quần suông nam dài basic màu xanh dương',3,3,1, 4,54),
(100, N'Quần suông nam dài basic màu xanh dương',4,3,1, 4,54);
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
(1, 1, 2, 1000.00, 1000.00,0.00),
(2, 1, 1, 500.00,600.00,100.00),
(3, 2, 1, 750.00, 750.00,0.00),
(4, 3, 5, 1500.00, 1500.00,0.00),
(5, 4, 3, 3600.00,4000.00,400.00);
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