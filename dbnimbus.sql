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
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [id_vai_tro] INT,
  CONSTRAINT [FK_nguoi_dung_id_vai_tro]
    FOREIGN KEY ([id_vai_tro])
      REFERENCES [vai_tro]([Id_vai_tro])
);
-- Insert data for vai_tro
go
CREATE TABLE [loai_voucher] (
  [Id_loai_voucher] INT PRIMARY KEY IDENTITY(1,1),
  [ten_loai_voucher] NVARCHAR(100) NOT NULL,
  [mo_ta] NVARCHAR(MAX),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE()
);
go
CREATE TABLE [voucher] (
  [Id_voucher] INT PRIMARY KEY IDENTITY(1,1),
  [ma_voucher] NVARCHAR(50) NOT NULL UNIQUE,
  [gia_tri_giam_gia] DECIMAL(18),
  [so_luong] INT,
  [gia_tri_toi_da] DECIMAL(18),
  [so_tien_toi_thieu] DECIMAL(18),
  [trang_thai] BIT DEFAULT 1,
  [mo_ta] NVARCHAR(MAX),
  [ngay_bat_dau] DATETIME,
  [ngay_ket_thuc] DATETIME,
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [id_loai_voucher] int,
  CONSTRAINT [FK_loai_voucher_id_loai_voucher]
    FOREIGN KEY ([id_loai_voucher])
      REFERENCES [loai_voucher]([Id_loai_voucher])
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
  [gia_ban] DECIMAL(18) NOT NULL,
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [mo_ta] NVARCHAR(MAX),
  [trang_thai] BIT DEFAULT 1,
  [id_danh_muc] INT,
  CONSTRAINT [FK_san_pham_id_danh_muc]
    FOREIGN KEY ([id_danh_muc])
      REFERENCES [danh_muc]([Id_danh_muc])
);
go

CREATE TABLE [dot_giam_gia] (
  [Id_dot_giam_gia] INT PRIMARY KEY IDENTITY(1,1),
  [ten_dot_giam_gia] NVARCHAR(100) NOT NULL,
  [gia_tri_giam_gia] DECIMAL(18),
  [trang_thai] BIT DEFAULT 1,
  [mo_ta] NVARCHAR(MAX),
  [ngay_bat_dau] DATETIME DEFAULT GETDATE(),
  [ngay_ket_thuc] DATETIME DEFAULT GETDATE(),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE()
);
go
CREATE TABLE [giam_gia_san_pham] (
  [Id_giam_gia_san_pham] INT PRIMARY KEY IDENTITY(1,1),
  [mo_ta] NVARCHAR(MAX),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [Id_dot_giam_gia] INT,
  [id_san_pham] INT, -- Thêm id_san_pham vào loai_voucher
  CONSTRAINT [FK_giam_gia_san_pham_id_dot_giam_gia]
    FOREIGN KEY ([id_dot_giam_gia])
      REFERENCES [dot_giam_gia]([Id_dot_giam_gia]),
  CONSTRAINT [FK_giam_gia_san_pham_id_san_pham]
    FOREIGN KEY ([id_san_pham])
      REFERENCES [san_pham]([Id_san_pham])
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
CREATE TABLE [san_pham_chi_tiet] (
  [Id_san_pham_chi_tiet] INT PRIMARY KEY IDENTITY(1,1),
  [so_luong] INT NULL,
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [trang_thai] BIT DEFAULT 1,
  [id_kich_thuoc_chi_tiet] INT,
  [id_mau_sac_chi_tiet] INT,
  [id_chat_lieu_chi_tiet] INT,
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
      REFERENCES [mau_sac_chi_tiet]([Id_mau_sac_chi_tiet])
);
go
CREATE TABLE [gio_hang_chi_tiet] (
  [Id_gio_hang_chi_tiet] INT PRIMARY KEY IDENTITY(1,1),
  [id_san_pham_chi_tiet] INT ,
  [id_gio_hang] INT,
  [so_luong] INT,
  [don_gia] DECIMAL(18),
  [thanh_tien] DECIMAL(18),
  [trang_thai] BIT DEFAULT 1,
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  CONSTRAINT [FK_gio_hang_chi_tiet_id_gio_hang]
    FOREIGN KEY ([id_gio_hang])
      REFERENCES [gio_hang]([Id_gio_hang]),
	  CONSTRAINT [FK_san_pham_chi_tiet_id_san_pham_chi_tiet]
    FOREIGN KEY ([id_san_pham_chi_tiet])
      REFERENCES [san_pham_chi_tiet]([Id_san_pham_chi_tiet])
);
go
CREATE TABLE kho_hang (
    [Id_kho_hang] INT PRIMARY KEY IDENTITY(1,1),
    [so_luong_them] INT,
    [ngay_cap_nhat] DATETIME DEFAULT GETDATE() -- Đổi kiểu dữ liệu từ INT sang DATETIME
);
GO
CREATE TABLE trang_thai_hoa_don (
    [Id_trang_thai_hoa_don] INT PRIMARY KEY IDENTITY(1,1),
    [ten_trang_thai] NVARCHAR(100) NOT NULL,
    [mo_ta] NVARCHAR(MAX),
    [ngay_tao] DATETIME DEFAULT GETDATE(),
    [ngay_cap_nhat] DATETIME DEFAULT GETDATE()
);
GO

CREATE TABLE hoa_don (
    [Id_hoa_don] INT PRIMARY KEY IDENTITY(1,1),
    [ma_hoa_don] NVARCHAR(50) NOT NULL UNIQUE,
    [id_nguoi_dung] INT,
    [id_voucher] INT,
    [id_dia_chi_van_chuyen] INT,
    [id_trang_thai_hoa_don] INT,
    [ten_nguoi_nhan] NVARCHAR(100) NOT NULL,
    [phi_ship] DECIMAL(18),
    [dia_chi] NVARCHAR(255) NOT NULL,
    [sdt_nguoi_nhan] NVARCHAR(15),
    [thanh_tien] DECIMAL(18),
    [ngay_tao] DATETIME DEFAULT GETDATE(),
    [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
    [mo_ta] NVARCHAR(MAX),
    [trang_thai] BIT DEFAULT 1,
    [ngay_thanh_toan] DATETIME,
    [id_pt_thanh_toan_hoa_don] INT,
    CONSTRAINT [FK_hoa_don_id_nguoi_dung]
        FOREIGN KEY ([id_nguoi_dung])
            REFERENCES nguoi_dung([id_nguoi_dung]),
    CONSTRAINT [FK_hoa_don_id_trang_thai_hoa_don]
        FOREIGN KEY ([id_trang_thai_hoa_don])
            REFERENCES trang_thai_hoa_don([Id_trang_thai_hoa_don]),
    CONSTRAINT [FK_hoa_don_id_voucher]
        FOREIGN KEY ([id_voucher])
            REFERENCES voucher([id_voucher])
);
GO
CREATE TABLE dia_chi_van_chuyen (
    [Id_dia_chi_van_chuyen] INT PRIMARY KEY IDENTITY(1,1),
    [tinh] NVARCHAR(100),
    [huyen] NVARCHAR(100),
    [xa] NVARCHAR(100),
    [so_tien_van_chuyen] DECIMAL(18),
    [ngay_tao] DATETIME DEFAULT GETDATE(),
    [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
    [trang_thai] BIT DEFAULT 1,
    [mo_ta] NVARCHAR(MAX)
);
GO

CREATE TABLE phi_van_chuyen (
    [Id_phi_van_chuyen] INT PRIMARY KEY IDENTITY(1,1),
    [id_dia_chi_van_chuyen] INT,
    [id_hoa_don] INT,
    [ngay_tao] DATETIME DEFAULT GETDATE(),
    [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
    [trang_thai] BIT DEFAULT 1,
    [mo_ta] NVARCHAR(MAX),
    CONSTRAINT [FK_phi_van_chuyen_id_dia_chi_van_chuyen]
        FOREIGN KEY ([id_dia_chi_van_chuyen])
            REFERENCES dia_chi_van_chuyen([Id_dia_chi_van_chuyen]),
    CONSTRAINT [FK_phi_van_chuyen_id_hoa_don]
        FOREIGN KEY ([id_hoa_don])
            REFERENCES hoa_don([Id_hoa_don])
);
GO
CREATE TABLE pt_thanh_toan (
    [Id_pt_thanh_toan] INT PRIMARY KEY IDENTITY(1,1),
    [ma_thanh_toan] NVARCHAR(50) NOT NULL UNIQUE,
    [ten_phuong_thuc] NVARCHAR(100) NOT NULL,
    [ngay_tao] DATETIME DEFAULT GETDATE(),
    [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
    [trang_thai] BIT DEFAULT 1,
    [mo_ta] NVARCHAR(MAX)
);
GO

CREATE TABLE pt_thanh_toan_hoa_don (
    [Id_thanh_toan_hoa_don] INT PRIMARY KEY IDENTITY(1,1),
    [id_pt_thanh_toan] INT,
    [ngay_giao_dich] DATETIME,
    [mo_ta] NVARCHAR(MAX),
    [trang_thai] NVARCHAR(MAX),
    [noi_dung_thanh_toan] NVARCHAR(MAX),
    [id_hoa_don] INT,
    CONSTRAINT [FK_pt_thanh_toan_hoa_don_id_pt_thanh_toan]
        FOREIGN KEY ([id_pt_thanh_toan])
            REFERENCES pt_thanh_toan([Id_pt_thanh_toan]),
    CONSTRAINT [FK_pt_thanh_toan_hoa_don_id_hoa_don]
        FOREIGN KEY ([id_hoa_don])
            REFERENCES hoa_don([Id_hoa_don])
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
INSERT INTO loai_voucher (ten_loai_voucher, mo_ta) VALUES
(N'Giảm giá theo phần trăm', N'Giảm giá theo tỷ lệ phần trăm của giá sản phẩm.'),
(N'Giảm giá theo số tiền', N'Giảm giá một số tiền cụ thể cho sản phẩm.'),
(N'Miễn phí vận chuyển', N'Miễn phí vận chuyển cho đơn hàng trên một mức giá nhất định.'),
(N'Quà tặng kèm', N'Tặng kèm sản phẩm miễn phí khi mua sản phẩm khác.'),
(N'Khuyến mãi theo gói', N'Giảm giá khi mua theo gói sản phẩm.'),
(N'Khuyến mãi theo mùa', N'Giảm giá đặc biệt cho các dịp lễ tết.');

-- Insert data for voucher
INSERT INTO voucher (ma_voucher, gia_tri_giam_gia, so_luong, gia_tri_toi_da, so_tien_toi_thieu, mo_ta, ngay_bat_dau, ngay_ket_thuc, id_loai_voucher) VALUES
(N'KM10', 10, 100, 500000,2000000, N'Giảm 10% cho đơn hàng từ 50k', '2024-01-01', '2024-01-31', 1),
(N'KM20K', 20000, 50,500000,2000000, N'Giảm 20.000đ cho đơn hàng từ 100k', '2024-02-01', '2024-02-28', 2),
(N'FREE_SHIP', 0, 200,500000,2000000, N'Miễn phí vận chuyển cho đơn hàng từ 150k', '2024-03-01', '2024-03-31', 3),
(N'GIFT1', 0, 100, 500000,2000000, N'Tặng kèm sản phẩm A khi mua sản phẩm B', '2024-04-01', '2024-04-30', 4),
(N'PACKAGE5', 50, 30, 500000,2000000, N'Giảm 50.000đ khi mua gói 5 sản phẩm', '2024-05-01', '2024-05-31', 5),
(N'SUMMER2024', 30, 150, 500000,2000000, N'Giảm 30% cho các sản phẩm mùa hè', '2024-06-01', '2024-06-30', 6);

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
INSERT INTO hoa_don (ma_hoa_don, id_nguoi_dung, id_voucher, Id_dia_chi_van_chuyen, id_trang_thai_hoa_don, ten_nguoi_nhan, phi_ship, dia_chi, sdt_nguoi_nhan, thanh_tien, mo_ta, id_pt_thanh_toan_hoa_don) VALUES 
('HD001', 1, 1, 1, 1, N'Trần Văn A', 30.00, N'Số 1, Đường A, Quận 1', N'0123456789', 500.00, N'Hoa đơn cho sản phẩm A', 1),
('HD002', 2, 2, 2, 1, N'Nguyễn Thị B', 20.00, N'Số 2, Đường B, Quận 2', N'0123456788', 750.00, N'Hoa đơn cho sản phẩm B', 2),
('HD003', 1, 3, 1, 1, N'Lê Văn C', 15.00, N'Số 3, Đường C, Quận 3', N'0123456787', 300.00, N'Hoa đơn cho sản phẩm C', 3),
('HD004', 3, 4, 2, 1, N'Trần Thị D', 25.00, N'Số 4, Đường D, Quận 4', N'0123456786', 1200.00, N'Hoa đơn cho sản phẩm D', 4),
('HD005', 2, 5, 1, 1, N'Nguyễn Văn E', 10.00, N'Số 5, Đường E, Quận 5', N'0123456785', 150.00, N'Hoa đơn cho sản phẩm E', 5);
go


-- Insert data for dia_chi_van_chuyen
INSERT INTO dia_chi_van_chuyen ( so_tien_van_chuyen, tinh, huyen, xa, trang_thai, mo_ta) VALUES 
(30000, N'Hà Nội', N'Hoàn Kiếm', N'Phan Chu Trinh',1, N'Địa chỉ giao hàng tại Hà Nội.'),
(50000, N'TP. Hồ Chí Minh', N'Quận 1', N'Nguyễn Thái Bình',1, N'Địa chỉ giao hàng tại TP. Hồ Chí Minh.'),
(20000, N'Hà Nội', N'Ba Đình', N'Trần Phú',1, N'Địa chỉ giao hàng cho các đơn hàng nhỏ.'),
(10000, N'Đà Nẵng', N'Hải Châu', N'Hải Châu 1',1, N'Địa chỉ giao hàng tại Đà Nẵng.'),
(100000, N'Bắc Ninh', N'Thuận Thành', N'Thị trấn Hồ',1, N'Địa chỉ giao hàng tại Bắc Ninh.');

go
-- Insert data for pt_thanh_toan
INSERT INTO pt_thanh_toan (ma_thanh_toan, ten_phuong_thuc, mo_ta) VALUES 
(N'TT001', N'Transfer Ngân Hàng', N'Transfer qua ngân hàng cho đơn hàng.'),
(N'TT002', N'Thanh Toán Qua Thẻ', N'Sử dụng thẻ tín dụng để thanh toán.'),
(N'TT003', N'Ví Điện Tử', N'Sử dụng ví điện tử để thanh toán.'),
(N'TT004', N'Thu Tiền Tận Nơi', N'Nhân viên sẽ đến thu tiền tại địa chỉ giao hàng.'),
(N'TT005', N'Thanh Toán Trực Tiếp', N'Khách hàng thanh toán trực tiếp tại cửa hàng.');
-- Insert data for phi_van_chuyen
INSERT INTO phi_van_chuyen (id_dia_chi_van_chuyen,id_hoa_don, mo_ta) VALUES 
(1,1, N'Phí vận chuyển cho đơn hàng nội tỉnh.'),
(2,2, N'Phí vận chuyển cho đơn hàng liên tỉnh.'),
(3,3, N'Phí vận chuyển cho đơn hàng dưới 1kg.'),
(4,4, N'Phí vận chuyển cho đơn hàng trên 1kg.'),
(5,5, N'Phí vận chuyển cho đơn hàng đặc biệt.');
go

-- Insert data for pt_thanh_toan_hoa_don
INSERT INTO pt_thanh_toan_hoa_don (id_pt_thanh_toan, ngay_giao_dich, mo_ta, trang_thai, noi_dung_thanh_toan, id_hoa_don) VALUES 
(1, GETDATE(), N'Thanh toán đơn hàng 001', N'Hoàn Thành', N'Thanh toán đơn hàng 001', 1),
(2, GETDATE(), N'Thanh toán đơn hàng 002', N'Hoàn Thành', N'Thanh toán đơn hàng 002', 2),
(3, GETDATE(), N'Thanh toán đơn hàng 003', N'Hoàn Thành', N'Thanh toán đơn hàng 003', 3),
(4, GETDATE(), N'Thanh toán đơn hàng 004', N'Hoàn Thành', N'Thanh toán đơn hàng 004', 4),
(5, GETDATE(), N'Thanh toán đơn hàng 005', N'Hoàn Thành', N'Thanh toán đơn hàng 005', 5);

go
INSERT INTO xac_thuc (ma_xac_thuc, id_nguoi_dung, mo_ta) VALUES 
(N'XAC001', 1, N'Xác thực đăng ký tài khoản'),
(N'XAC002', 2, N'Xác thực email'),
(N'XAC003', 1, N'Xác thực số điện thoại'),
(N'XAC004', 3, N'Xác thực khôi phục mật khẩu'),
(N'XAC005', 2, N'Xác thực đăng nhập');
go


-- Insert data for san_pham
INSERT INTO san_pham (ten_san_pham, gia_ban, mo_ta, id_danh_muc,trang_thai) VALUES 
(N'Áo phông hình bàn tay',120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
(N'Áo phông butterfly',  120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1,1),
(N'Áo phông cotton',  120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
(N'Áo phông ENJOYABLE',120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
(N'Áo phông loang',  120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
(N'Áo phông holiday 1961', 120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
(N'Áo phông nam nữ 1984', 120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
(N'Áo phông nam nữ oversize',120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
(N'Áo phông tay lỡ',  120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
(N'Áo phông thể thao',  120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
(N'Áo phông SPORT FASHION', 120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),

(N'Áo sơ mi đũi nơ thắt eo', 120000 , N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 1),
(N'Áo sơ mi nam kẻ sọc', 120000 , N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 1),
(N'Áo sơ mi lụa công sở', 120000 , N'Áo sơ mi được làm từ lụa mềm mại, thoáng khí', 2, 1),
(N'Áo sơ mi nam loang', 120000 , N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 1),
(N'Áo sơ mi ngắn siết eo',  120000 , N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 1),
(N'Áo sơ mi tay ngắn túi hộp', 120000 , N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 1),
(N'Áo sơ mi thắt cà vạt', 120000 , N'Áo sơ mi được làm theo phong cách Nhật', 2,1),
(N'Áo sơ mi sọc đơn giản',  120000 , N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 1),
(N'Áo sơ mi tay ngắn', 120000 , N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2,1),
(N'Áo sơ mi trơn',  120000 , N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 1),

(N'Áo ấm lông cừu', 120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 1),
(N'Áo béo buộc nơ', 120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 1),
(N'Áo phao bông',  120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3,1),
(N'Áo phao cài khuy', 120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 1),
(N'Áo phao gile', 120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 1),
(N'Áo phao cài khuy cổ', 120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3,1),
(N'Áo phao cài lửng thời trang', 120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 1),
(N'Áo phao nhung cừu', 120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3,1),
(N'Áo phao cài NIKE',  120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3,1),

(N'Áo chống nắng viền tròn', 120000 , N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 1),
(N'Áo chống nắng toàn thân',  120000 , N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 1),
(N'Áo chống nắng thông hơi', 120000 , N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4,1),
(N'Áo chống nắng thời trang', 120000 , N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 1),
(N'Áo chống nắng thể thao', 120000 , N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 1),
(N'Áo chống nắng LV',  120000 , N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 1),
(N'Áo chống nắng dài xoe',  120000 , N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 1),


(N'Quần baggy kaki',  120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5,1),
(N'Quần đũi dài nam',  120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 1),
(N'Quần đũi dài nữ', 120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 1),
(N'Quần ống rộng cạp cao',  120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 1),
(N'Quần ống rộng nam',  120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 1),
(N'Quần đũi rộng túi hộp', 120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 1),
(N'Quần suông đơn giản', 120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 1),
(N'Quần suông rộng', 120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 1),

(N'Quần ống rộng suông',  120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6, 1),
(N'Quần sort nữ cá tính', 120000 , N'Quần sort chất liệu kaki mềm mại, co giãn nhẹ', 6, 1),
(N'Quần sort nữ đũi', 120000 , N'Quần sort chất liệu đũi mềm mại, co giãn nhẹ', 6, 1),
(N'Quần đùi túi hộp đứng', 120000 , N'Quần dài dáng suông, chất liệu đũi mềm mại, co giãn nhẹ', 6, 1),
(N'Quần jean cạp trễ',  120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6,1),
(N'Quần jean thời trang', 120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6, 1),
(N'Quần sort bò rộng',  120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6,1),
(N'Quần sort tây nam', 120000 , N'Quần sort chất liệu kaki mềm mại, co giãn nhẹ', 6, 1),
(N'Quần suông dài basic', 120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6,1);
go
INSERT INTO danh_gia (id_nguoi_dung, id_san_pham, noi_dung, diem) VALUES 
(1, 1, N'Sản phẩm đẹp như trên mô tả', 5),
(2, 2, N'Sản phẩm chất lượng', 4),
(3, 3, N'Sản phẩm tốt, phù hợp giá tiền', 5),
(4, 4, N'Chất vải tốt, mặc rất thoải mái', 4),
(5, 5, N'Áo chất lượng, đáng tiền', 4);
go
-- Giả sử bạn đã có các bản ghi trong bảng dot_giam_gia
INSERT INTO dot_giam_gia (ten_dot_giam_gia, gia_tri_giam_gia, mo_ta, ngay_bat_dau, ngay_ket_thuc)
VALUES 
(N'Khuyến mãi mùa hè', 20000, N'Giảm giá cho các sản phẩm mùa hè', GETDATE(), DATEADD(DAY, 30, GETDATE()));

-- Lấy Id_dot_giam_gia vừa tạo
DECLARE @Id_dot_giam_gia INT = SCOPE_IDENTITY();

-- Chèn dữ liệu vào bảng giam_gia_san_pham
INSERT INTO giam_gia_san_pham (mo_ta, Id_dot_giam_gia, id_san_pham)
VALUES
(N'Áo thun nam được giảm giá 10%', @Id_dot_giam_gia, 1),
(N'Smartphone XYZ áp dụng giảm 20.000đ', @Id_dot_giam_gia, 2),
(N'Miễn phí vận chuyển cho Bình nước giữ nhiệt', @Id_dot_giam_gia, 3),
(N'Sách hay được tặng kèm sản phẩm khác', @Id_dot_giam_gia, 4),
(N'Giảm giá cho Bánh kẹo nhập khẩu', @Id_dot_giam_gia, 5),
(N'Son môi ABC giảm giá 50.000đ', @Id_dot_giam_gia, 6);
go
-- Insert data for san_pham_chi_tiet
INSERT INTO san_pham_chi_tiet (so_luong, id_kich_thuoc_chi_tiet, id_mau_sac_chi_tiet, id_chat_lieu_chi_tiet, id_san_pham)
VALUES 
/* Áo phông */
(100,  1, 3, 1, 1),
(100, 2, 3, 1,  1),
(100,  3, 3, 1,  1),
(100,  4, 3, 1, 1),
(100,  1, 6, 1,  1),
(100,  2, 6, 1,  1),
(100,  3, 6, 1,  1),
(100,  4, 6, 1,  1),
(100,  1, 4, 1,  1),
(100,  2, 4, 1,  1),
(100,  3, 4, 1,1),
(100,  4, 4, 1, 1),
(100,  1, 2, 1, 2),
(100,  2, 2, 1, 2),
(100,  3, 2, 1,  2),
(100,  4, 2, 1,  2),
(100,  1, 13, 1, 2),
(100,  2, 13, 1, 2),
(100,  3, 13, 1, 2),
(100,  4, 13, 1,  2),
(100,   1, 6, 1,  2),
(100,  2, 6, 1,  2),
(100,  3, 6, 1,  2),
(100,  4, 6, 1,  2),
(100,   1, 9, 1,  3),
(100,  2, 9, 1,  3),
(100,   3, 9, 1,  3),
(100,   4, 9, 1,  3),
(100,   1, 5, 1,  3),
(100,  2, 5, 1,  3),
(100, 3, 5, 1,  3),
(100,   4, 5, 1,  3),
(100,    1, 4, 1, 3),
(100,   2, 4, 1, 3),
(100,    3, 4, 1, 3),
(100,   4, 4, 1, 3),
(100,    1, 10, 1, 4),
(100,    2, 10, 1, 4),
(100,    3, 10, 1, 4),
(100,    4, 10, 1, 4),
(100,  1, 11, 1, 4),
(100,    2, 11, 1, 4),
(100,    3, 11, 1, 4),
(100,    4, 11, 1, 4),
(100,   1, 4, 1, 4),
(100,   2, 4, 1, 4),
(100,   3, 4, 1, 4),
(100,   4, 4, 1, 4),
(100,   1, 8, 1, 5),
(100,   2, 8, 1, 5),
(100,   3, 8, 1, 5),
(100,  4, 8, 1, 5),
(100,    1, 10, 1, 5),
(100,    2, 10, 1, 5),
(100,    3, 10, 1, 5),
(100,    4, 10, 1, 5),
(100,   1, 1, 1, 6),
(100,   2, 1, 1, 6),
(100,   3, 1, 1, 6),
(100,   4, 1, 1, 6),
(100,   1, 10, 1, 6),
(100,   2, 10, 1, 6),
(100,   3, 10, 1, 6),
(100,   4, 10, 1, 6),
(100,    1, 12, 1, 6),
(100,    2, 12, 1, 6),
(100,    3, 12, 1, 6),
(100,    4, 12, 1, 6),
(100,    1, 1, 1, 7),
(100,    2, 1, 1, 7),
(100,    3, 1, 1, 7),
(100,    4, 1, 1, 7),
(100,    1, 10, 1, 7),
(100,    2, 10, 1, 7),
(100,    3, 10, 1, 7),
(100,    4, 10, 1, 7),
(100,  1,9, 1,  7),
(100,  2,9, 1,  7),
(100,  3,9, 1,  7),
(100,  4,9, 1, 7),
(100,   1, 9, 1,  8),
(100,  2, 9, 1, 8),
(100,   3, 9, 1, 8),
(100,   4, 9, 1,  8),
(100,    1, 10, 1,  8),
(100,    2, 10, 1,  8),
(100,  3, 10, 1,  8),
(100,    4, 10, 1, 8),
(100,   1, 9, 1,  9),
(100,   2, 9, 1, 9),
(100,   3, 9, 1,  9),
(100,   4, 9, 1,  9),
(100,    1, 8, 1,  9),
(100,    2, 8, 1, 9),
(100,    3, 8, 1,  9),
(100,    4, 8, 1,  9),
(100,   1, 4, 1,  9),
(100,   2, 4, 1,  9),
(100,   3, 4, 1,  9),
(100,   4, 4, 1, 9),
(100,   1, 1, 1, 10),
(100,   2, 1, 1,  10),
(100,   3, 1, 1,  10),
(100,   4, 1, 1, 10),
(100,   1, 3, 1,  10),
(100,   2, 3, 1, 10),
(100,   3, 3, 1, 10),
(100,   4, 3, 1,  10),
(100,  1, 6, 1,  10),
(100,  2, 6, 1,  10),
(100,  3, 6, 1,  10),
(100,  4, 6, 1,  10),
(100,   1, 1, 1, 11),
(100,   2, 1, 1,  11),
(100,   3, 1, 1, 11),
(100,   4, 1, 1, 11),
(100,  1, 6, 1,  11),
(100,  2, 6, 1,  11),
(100,  3, 6, 1, 11),
(100,  4, 6, 1,  11),
(100,  1, 9, 1,  11),
(100,  2, 9, 1,  11),
(100,  3, 9, 1, 11),
(100,  4, 9, 1,  11),
/* Áo sơ mi */
(100,   1, 11, 1,  12),
(100,   2, 11, 1,  12),
(100,   3, 11, 1, 12),
(100,   4, 11, 1,  12),
(100,    1, 10, 1,  12),
(100,    2, 10, 1,  12),
(100,    3, 10, 1,  12),
(100,    4, 10, 1,  12),
(100,   1, 2, 1,  13),
(100,   2, 2, 1,  13),
(100,   3, 2, 1,  13),
(100,   4, 2, 1,  13),
(100,   1, 1, 1,  13),
(100,   2, 1, 1,  13),
(100,   3, 1, 1,  13),
(100,   4, 1, 1,  13),

(100,  1, 2, 2,  14),
(100,   2, 2, 2,  14),
(100,   3, 2, 2,  14),
(100,   4, 2, 2, 14),
(100,    1, 3, 2,  14),
(100,    2, 3, 2, 14),
(100,  3, 3, 2,  14),
(100,4, 3, 2,  14),
(100,1, 2, 1,  15),
(100,2, 2, 1,  15),
(100,3, 2, 1,  15),
(100,4, 2, 1,  15),
(100, 1, 1, 1, 15),
(100, 2, 1, 1,  15),
(100, 3, 1, 1, 15),
(100, 4, 1, 1,  15),
(100, 1, 2, 1, 16),
(100, 2, 2, 1, 16),
(100, 3, 2, 1,  16),
(100, 4, 2, 1,  16),
(100, 1, 3, 1,  16),
(100, 2, 3, 1,  16),
(100, 3, 3, 1,  16),
(100, 4, 3, 1,  16),
(100,1, 2, 1, 17),
(100,2, 2, 1, 17),
(100,3, 2, 1, 17),
(100,4, 2, 1,  17),
(100,1, 10, 1,  17),
(100,2, 10, 1,  17),
(100,3, 10, 1,  17),
(100,4, 10, 1,  17),
(100,1, 7, 1,  17),
(100,2, 7, 1,  17),
(100,3, 7, 1, 17),
(100,4, 7, 1,  17),
(100, 1, 9, 1,  18),
(100, 2, 9, 1,  18),
(100, 3, 9, 1, 18),
(100, 4, 9, 1,  18),
(100,1, 12, 1,  18),
(100,2, 12, 1,  18),
(100,3, 12, 1, 18),
(100,4, 12, 1,  18),
(100, 1, 3, 1,  18),
(100, 2, 3, 1,  18),
(100, 3, 3, 1,18),
(100, 4, 3, 1, 18),
(100,1, 3, 1, 19),
(100,2, 3, 1,  19),
(100,3, 3, 1, 19),
(100,4, 3, 1, 19),
(100, 1,8, 1,  19),
(100, 2,8, 1,  19),
(100, 3,8, 1,  19),
(100, 4,8, 1,   19),
(100, 1,2, 1,  20),
(100, 2,2, 1,  20),
(100, 3,2, 1, 20),
(100, 4,2, 1, 20),
(100,1,7, 1, 20),
(100,2,7, 1, 20),
(100,3,7, 1, 20),
(100,4,7, 1, 20),
(100,1,4, 1, 21),
(100,2,4, 1, 21),
(100,3,4, 1, 21),
(100,4,4, 1, 21),
(100,1,8, 1, 21),
(100,2,8, 1, 21),
(100,3,8, 1, 21),
(100,4,8, 1, 21),
(100, 1,3, 1, 21),
(100, 2,3, 1, 21),
(100, 3,3, 1, 21),
(100, 4,3, 1, 21),

(100,1, 12, 1, 22),
(100,2, 12, 1, 22),
(100,3, 12, 1, 22),
(100,4, 12, 1, 22),
(100,  1, 8, 1, 22),
(100,  2, 8, 1, 22),
(100,  3, 8, 1, 22),
(100,  4, 8, 1, 22),
(100, 1, 1, 1, 22),
(100, 2, 1, 1, 22),
(100, 3, 1, 1, 22),
(100, 4, 1, 1, 22),

(100,  1, 1, 1,  23),
(100,  2, 1, 1,  23),
(100,  3, 1, 1,  23),
(100,  4, 1, 1,  23),
(100,1, 9, 1, 23),
(100,2, 9, 1, 23),
(100,3, 9, 1, 23),
(100,4, 9, 1, 23),
(100,1, 12, 1, 23),
(100,2, 12, 1, 23),
(100,3, 12, 1, 23),
(100,4, 12, 1, 23),
(100,1, 1, 1, 24),
(100,2, 1, 1, 24),
(100,3, 1, 1, 24),
(100,4, 1, 1, 24),
(100,  1, 8, 1, 24),
(100,  2, 8, 1, 24),
(100,  3, 8, 1, 24),
(100,  4, 8, 1, 24),
(100,  1, 2, 1, 24),
(100,  2, 2, 1, 24),
(100,  3, 2, 1, 24),
(100,  4, 2, 1, 24),
(100,  1, 1, 1, 25),
(100,  2, 1, 1, 25),
(100,  3, 1, 1, 25),
(100,  4, 1, 1, 25),
(100,  1, 7, 1, 25),
(100,  2, 7, 1, 25),
(100,  3, 7, 1, 25),
(100,  4, 7, 1, 25),
(100, 1, 12, 1, 25),
(100, 2, 12, 1, 25),
(100, 3, 12, 1, 25),
(100, 4, 12, 1, 25),
(100, 1, 1, 1, 26),
(100, 2, 1, 1, 26),
(100, 3, 1, 1, 26),
(100, 4, 1, 1, 26),
(100, 2, 5, 1, 26),
(100, 2, 5, 1, 26),
(100, 2, 5, 1, 26),
(100, 2, 5, 1, 26),
(100,  1, 2, 1, 26),
(100,  2, 2, 1, 26),
(100,  3, 2, 1, 26),
(100,  4, 2, 1, 26),
(100,  1, 8, 1, 27),
(100,  2, 8, 1, 27),
(100,  3, 8, 1, 27),
(100,  4, 8, 1, 27),
(100, 1, 7, 1,  27),
(100, 2, 7, 1,  27),
(100, 3, 7, 1,  27),
(100, 4, 7, 1,  27),
(100, 1, 1, 1,  27),
(100, 2, 1, 1,  27),
(100, 3, 1, 1,  27),
(100, 4, 1, 1,  27),
(100, 1, 2, 1,  28),
(100, 2, 2, 1,  28),
(100, 3, 2, 1,  28),
(100, 4, 2, 1,  28),
(100,1, 9, 1, 28),
(100,2, 9, 1, 28),
(100,3, 9, 1, 28),
(100,4, 9, 1, 28),
(100, 1, 1, 1,  28),
(100, 2, 1, 1,  28),
(100, 3, 1, 1,  28),
(100, 4, 1, 1,  28),

(100,1, 1, 1, 29),
(100,2, 1, 1, 29),
(100,3, 1, 1, 29),
(100,4, 1, 1, 29),
(100, 1, 1, 1, 29),
(100, 2, 1, 1, 29),
(100, 3, 1, 1, 29),
(100, 4, 1, 1, 29),
(100, 1, 1, 1, 29),
(100, 2, 1, 1, 29),
(100, 3, 1, 1, 29),
(100, 4, 1, 1, 29),
(100,1, 1, 1,  30),
(100,2, 1, 1,  30),
(100,3, 1, 1,  30),
(100,4, 1, 1,  30),
(100, 1, 14, 1, 30),
(100, 2, 14, 1, 30),
(100, 3, 14, 1, 30),
(100, 4, 14, 1, 30),
(100,1, 2, 1, 30),
(100,2, 2, 1, 30),
(100,3, 2, 1, 30),
(100,4, 2, 1, 30),

(100,  1, 8, 2, 31),
(100,  2, 8, 2, 31),
(100,  3, 8, 2, 31),
(100,  4, 8, 2, 31),
(100, 1, 1, 2, 31),
(100, 2, 1, 2, 31),
(100, 3, 1, 2, 31),
(100, 4, 1, 2, 31),
(100,1, 10, 2, 31),
(100,2, 10, 2, 31),
(100,3, 10, 2, 31),
(100,4, 10, 2, 31),
(100, 1, 3, 2, 32),
(100, 2, 3, 2, 32),
(100, 3, 3, 2, 32),
(100, 4, 3, 2, 32),
(100,  1, 10, 2, 32),
(100,  2, 10, 2, 32),
(100,  3, 10, 2, 32),
(100,  4, 10, 2, 32),
(100,1, 2, 2, 32),
(100,2, 2, 2, 32),
(100,3, 2, 2, 32),
(100,4, 2, 2, 32),
(100, 1, 8, 2, 33),
(100, 2, 8, 2, 33),
(100, 3, 8, 2, 33),
(100, 4, 8, 2, 33),
(100, 1, 3, 2, 33),
(100, 2, 3, 2, 33),
(100, 3, 3, 2, 33),
(100, 4, 3, 2, 33),
(100, 1, 1, 2, 33),
(100, 2, 1, 2, 33),
(100, 3, 1, 2, 33),
(100, 4, 1, 2, 33),
(100, 1, 2, 2, 34),
(100, 2, 2, 2, 34),
(100, 3, 2, 2, 34),
(100, 4, 2, 2, 34),
(100,1, 8, 2, 34),
(100,2, 8, 2, 34),
(100,3, 8, 2, 34),
(100,4, 8, 2, 34),
(100,  1, 10, 2, 35),
(100,  2, 10, 2, 35),
(100,  3, 10, 2, 35),
(100,  4, 10, 2, 35),
(100, 1, 1, 2, 35),
(100, 2, 1, 2, 35),
(100, 3, 1, 2, 35),
(100, 4, 1, 2, 35),
(100, 1, 11, 2, 36),
(100, 2, 11, 2, 36),
(100, 3, 11, 2, 36),
(100, 4, 11, 2, 36),
(100,1, 10, 2,  36),
(100,2, 10, 2,  36),
(100,3, 10, 2,  36),
(100,4, 10, 2,  36),
(100,1, 1, 2, 36),
(100,2, 1, 2, 36),
(100,3, 1, 2, 36),
(100,4, 1, 2, 36),
(100,1, 8, 2, 37),
(100,2, 8, 2, 37),
(100,3, 8, 2, 37),
(100,4, 8, 2, 37),
(100, 1, 14, 2,  37),
(100, 2, 14, 2,  37),
(100, 3, 14, 2,  37),
(100, 4, 14, 2,  37),
(100,1, 1, 2, 37),
(100,2, 1, 2, 37),
(100,3, 1, 2, 37),
(100,4, 1, 2, 37),


(100,  1, 7, 2, 38),
(100,2, 7, 2, 38),
(100,  3, 7, 2, 38),
(100,  4, 7, 2, 38),
(100,  1, 1, 2, 38),
(100,  2, 1, 2, 38),
(100,  3, 1, 2, 38),
(100,  4, 1, 2, 38),
(100,1, 10, 2, 38),
(100,2, 10, 2, 38),
(100,3, 10, 2, 38),
(100,4, 10, 2, 38),
(100, 1, 1, 2, 39),
(100, 2, 1, 2, 39),
(100, 3, 1, 2, 39),
(100, 4, 1, 2, 39),
(100,1, 12, 2, 39),
(100,2, 12, 2, 39),
(100,3, 12, 2, 39),
(100,4, 12, 2, 39),
(100,  1, 3, 2, 40),
(100,  2, 3, 2, 40),
(100,  3, 3, 2, 40),
(100,  4, 3, 2, 40),
(100,1, 1, 2, 40),
(100,2, 1, 2, 40),
(100,3, 1, 2, 40),
(100,4, 1, 2, 40),
(100, 1, 8, 2, 40),
(100, 2, 8, 2, 40),
(100, 3, 8, 2, 40),
(100, 4, 8, 2, 40),
(100,1, 2, 2, 41),
(100,2, 2, 2, 41),
(100,3, 2, 2, 41),
(100,4, 2, 2, 41),
(100,1, 1, 2, 41),
(100,2, 1, 2, 41),
(100,3, 1, 2, 41),
(100,4, 1, 2, 41),
(100,1, 12, 2, 41),
(100,2, 12, 2, 41),
(100,3, 12, 2, 41),
(100,4, 12, 2, 41),
(100,1, 1, 2, 42),
(100,2, 1, 2, 42),
(100,3, 1, 2, 42),
(100,4, 1, 2, 42),
(100,1, 12, 2, 42),
(100,2, 12, 2, 42),
(100,3, 12, 2, 42),
(100,4, 12, 2, 42),

(100,1, 5, 2, 43),
(100,1, 5, 2, 43),
(100,1, 5, 2, 43),
(100,1, 5, 2, 43),
(100,1,1, 2, 43),
(100,2,1, 2, 43),
(100,3,1, 2, 43),
(100,4,1, 2, 43),
(100,  1,2, 2, 43),
(100,  2,2, 2, 43),
(100,  3,2, 2, 43),
(100,  4,2, 2, 43),

(100,1,1, 2, 44),
(100,2,1, 2, 44),
(100,3,1, 2, 44),
(100,4,1, 2, 44),
(100,1,12, 2, 44),
(100,2,12, 2, 44),
(100,3,12, 2, 44),
(100,4,12, 2, 44),
(100,1,2, 2, 44),
(100,2,2, 2, 44),
(100,3,2, 2, 44),
(100,4,2, 2, 44),
(100,1,9, 2, 45),
(100,2,9, 2, 45),
(100,3,9, 2, 45),
(100,4,9, 2, 45),
(100,1,7, 2, 45),
(100,2,7, 2, 45),
(100,3,7, 2, 45),
(100,4,7, 2, 45),


(100, 1, 7, 2,46),
(100, 2, 7, 2,46),
(100, 3, 7, 2,46),
(100, 4, 7, 2,46),
(100, 1, 11, 2, 46),
(100, 2, 11, 2 ,46),
(100, 3, 11, 2, 46),
(100, 4, 11, 2 ,46),
(100,1, 10, 2,46),
(100,2, 10, 2,46),
(100,3, 10, 2,46),
(100,4, 10, 2,46),

(100, 1, 9, 2, 47),
(100, 2, 9, 2, 47),
(100, 3, 9, 2, 47),
(100, 4, 9, 2, 47),
(100,  1, 1, 2, 47),
(100,  2, 1, 2, 47),
(100,  3, 1, 2, 47),
(100,  4, 1, 2, 47),
(100,1, 2, 3,  48),
(100,2, 2, 3, 48),
(100,3, 2, 3, 48),
(100,4, 2, 3, 48),
(100, 1, 1, 3, 48),
(100, 2, 1, 3, 48),
(100, 3, 1, 3, 48),
(100, 4, 1, 3, 48),
(100,1, 8, 3, 48),
(100,2, 8, 3, 48),
(100,3, 8, 3, 48),
(100,4, 8, 3, 48),
(100, 1, 5, 3, 49),
(100, 2, 5, 3, 49),
(100, 3, 5, 3, 49),
(100, 4, 5, 3, 49),
(100,1, 2, 3, 49),
(100,2, 2, 3, 49),
(100,3, 2, 3, 49),
(100,4, 2, 3, 49),
(100, 1, 3, 2, 50),
(100, 2, 3, 2, 50),
(100, 3, 3, 2, 50),
(100, 4, 3, 2, 50),
(100, 1, 10, 2, 50),
(100, 2, 10, 2, 50),
(100, 3, 10, 2, 50),
(100, 4, 10, 2, 50),
(100, 1, 8, 2, 50),
(100, 2, 8, 2, 50),
(100, 3, 8, 2, 50),
(100, 4, 8, 2, 50),
(100, 1, 8, 2, 51),
(100, 2, 8, 2, 51),
(100, 3, 8, 2, 51),
(100, 4, 8, 2, 51),
(100,1, 9, 2, 51),
(100,2, 9, 2, 51),
(100,3, 9, 2, 51),
(100,4, 9, 2, 51),
(100,1, 1, 2, 51),
(100,2, 1, 2, 51),
(100,3, 1, 2, 51),
(100,4, 1, 2, 51),
(100,1, 1, 2, 52),
(100,2, 1, 2, 52),
(100,3, 1, 2, 52),
(100,4, 1, 2, 52),
(100, 1, 1, 2, 52),
(100, 2, 1, 2, 52),
(100, 3, 1, 2, 52),
(100, 4, 1, 2, 52),
(100,1,12,2, 53),
(100,2,12,2, 53),
(100,3,12,2, 53),
(100,4,12,2, 53),
(100, 1,9,2, 53),
(100, 2,9,2, 53),
(100, 3,9,2, 53),
(100, 4,9,2, 53),
(100,1,1,1, 53),
(100,2,1,1, 53),
(100,3,1,1, 53),
(100,4,1,1, 53),
(100,1,1,1,54),
(100,2,1,1,54),
(100,3,1,1,54),
(100,4,1,1,54),
(100, 1,2,1, 54),
(100, 2,2,1, 54),
(100, 3,2,1, 54),
(100, 4,2,1, 54),
(100, 1,3,1, 54),
(100, 2,3,1,54),
(100, 3,3,1,54),
(100, 4,3,1,54);
go
-- Insert data for gio_hang_chi_tiet
INSERT INTO gio_hang_chi_tiet (id_san_pham_chi_tiet, id_gio_hang, so_luong, don_gia, thanh_tien) VALUES 
(1, 1, 1, 200000, 200000),
(2, 2, 2, 300000, 600000),
(3, 1, 1, 500000, 500000),
(4, 1, 1, 100000, 100000),
(5, 1, 1, 1500000, 1500000);
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
(1, 1, 2, 1000.00, 1000.00,0),
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
select * from gio_hang_chi_tiet
select * from gio_hang
select * from hinh_anh_san_pham
select * from dot_giam_gia
select * from giam_gia_san_pham
