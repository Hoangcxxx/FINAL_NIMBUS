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
  [ma_nguoi_dung] NVARCHAR(50) NOT NULL UNIQUE,
  [ten_nguoi_dung] NVARCHAR(100),
  [email] NVARCHAR(255),
  [sdt] NVARCHAR(15),
  [ngay_sinh] DATE,
  [dia_chi] NVARCHAR(255),
  [gioi_tinh] NVARCHAR(10),
  [mat_khau] NVARCHAR(255),
  [anh_dai_dien] NVARCHAR(255),
  [trang_thai] BIT DEFAULT 1,
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [id_vai_tro] INT,
  CONSTRAINT [FK_nguoi_dung_id_vai_tro]
    FOREIGN KEY ([id_vai_tro])
      REFERENCES [vai_tro]([Id_vai_tro])
);
go
CREATE TABLE loai_thong_bao (
    [Id_loai_thong_bao] INT PRIMARY KEY IDENTITY(1,1),
    [ten_loai_thong_bao] NVARCHAR(225),
    [ngay_tao] DATETIME DEFAULT GETDATE(),
    [ngay_cap_nhat] DATETIME DEFAULT GETDATE()
);
go
CREATE TABLE thong_bao (
    Id_thong_bao INT PRIMARY KEY IDENTITY(1,1),
    id_nguoi_dung INT,
    id_loai_thong_bao INT,
    noi_dung NVARCHAR(MAX),
    trang_thai BIT DEFAULT 1,
    ngay_gui DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_thong_bao_id_nguoi_dung
        FOREIGN KEY (id_nguoi_dung)
        REFERENCES nguoi_dung(Id_nguoi_dung),
    CONSTRAINT FK_thong_bao_id_loai_thong_bao
        FOREIGN KEY (id_loai_thong_bao)
        REFERENCES loai_thong_bao(Id_loai_thong_bao)
);
go
CREATE TABLE [trang_thai_giam_gia] (
  [Id_trang_thai_giam_gia] INT PRIMARY KEY IDENTITY(1,1),
  [ten_trang_thai_giam_gia] NVARCHAR(100) NOT NULL,
  [mo_ta] NVARCHAR(MAX),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE()
);

go
CREATE TABLE [voucher] (
  [Id_voucher] INT PRIMARY KEY IDENTITY(1,1),
  [ma_voucher] NVARCHAR(50) NOT NULL UNIQUE,
  [ten_voucher] NVARCHAR(50),
  [gia_tri_giam_gia] DECIMAL(18),
  [kieu_giam_gia] BIT DEFAULT 1,
  [so_luong] INT,
  [gia_tri_toi_da] DECIMAL(18),
  [so_tien_toi_thieu] DECIMAL(18),
  [mo_ta] NVARCHAR(MAX),
  [ngay_bat_dau] DATETIME,
  [ngay_ket_thuc] DATETIME,
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [id_trang_thai_giam_gia] int,
	  CONSTRAINT [FK_trang_thai_voucher_id_trang_thai_giam_gia]
    FOREIGN KEY ([id_trang_thai_giam_gia])
      REFERENCES [trang_thai_giam_gia]([Id_trang_thai_giam_gia])
);
go
CREATE TABLE voucher_nguoi_dung (
  Id_voucher_nguoi_dung INT PRIMARY KEY IDENTITY(1,1),
  id_voucher INT,
  id_nguoi_dung INT,
  FOREIGN KEY (id_voucher) REFERENCES voucher(id_voucher),
  FOREIGN KEY (id_nguoi_dung) REFERENCES nguoi_dung(Id_nguoi_dung)
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
  [ma_san_pham] VARCHAR(15) NOT NULL UNIQUE,
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
  [kieu_giam_gia] BIT DEFAULT 1,
  [gia_tri_giam_gia] DECIMAL(18),
  [mo_ta] NVARCHAR(MAX),
  [ngay_bat_dau] DATETIME DEFAULT GETDATE(),
  [ngay_ket_thuc] DATETIME DEFAULT GETDATE(),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [id_trang_thai_giam_gia] INT,
	  CONSTRAINT [FK_trang_thai_giam_gia_id_trang_thai_giam_gia]
    FOREIGN KEY ([id_trang_thai_giam_gia])
      REFERENCES [trang_thai_giam_gia]([Id_trang_thai_giam_gia])
);
go
CREATE TABLE [giam_gia_san_pham] (
  [Id_giam_gia_san_pham] INT PRIMARY KEY IDENTITY(1,1),
  [gia_khuyen_mai] DECIMAL(18),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [id_dot_giam_gia] INT,
  [id_san_pham] INT,
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
  [Id_chat_lieu_chi_tiet] INT PRIMARY KEY IDENTITY(1,1),
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
  [ma_san_pham_chi_tiet] VARCHAR(15),
  [so_luong] INT NULL,
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [trang_thai] BIT DEFAULT 1,
  [id_kich_thuoc_chi_tiet] INT,
  [id_mau_sac_chi_tiet] INT,
  [id_chat_lieu_chi_tiet] INT,
  [id_san_pham] INT,
  CONSTRAINT [FK_san_pham_chi_tiet_id_chat_lieu_chi_chi_tiet]
    FOREIGN KEY ([id_chat_lieu_chi_tiet])
      REFERENCES [chat_lieu_chi_tiet]([Id_chat_lieu_chi_tiet]),
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
GO
CREATE TABLE hoa_don (
    [Id_hoa_don] INT PRIMARY KEY IDENTITY(1,1),
    [ma_hoa_don] NVARCHAR(50) NOT NULL UNIQUE,
    [id_nguoi_dung] INT,
    [id_nhan_vien] INT,
    [id_voucher] INT,
    [loai] BIT DEFAULT 1,
    [id_dia_chi_van_chuyen] INT,
    [ten_nguoi_nhan] NVARCHAR(100) NOT NULL,
    [phi_ship] DECIMAL(18),
    [dia_chi] NVARCHAR(255),
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
    CONSTRAINT [FK_hoa_don_id_voucher]
        FOREIGN KEY ([id_voucher])
            REFERENCES voucher([id_voucher])
);
GO
CREATE TABLE loai_trang_thai (
    [Id_loai_trang_thai] INT PRIMARY KEY IDENTITY(1,1),
    [ten_loai_trang_thai] NVARCHAR(100) NOT NULL,
    [mo_ta] NVARCHAR(MAX),
    [ngay_tao] DATETIME DEFAULT GETDATE(),
    [ngay_cap_nhat] DATETIME DEFAULT GETDATE()
);
GO
CREATE TABLE trang_thai_hoa_don (
    [Id_trang_thai_hoa_don] INT PRIMARY KEY IDENTITY(1,1),
    [mo_ta] NVARCHAR(MAX),
    [ngay_tao] DATETIME DEFAULT GETDATE(),
    [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
    [id_loai_trang_thai] INT,
    [id_nhan_vien] INT,
    [id_hoa_don] INT,
    CONSTRAINT [FK_trang_thai_hoa_don_id_loai_trang_thai]
        FOREIGN KEY ([id_loai_trang_thai])
            REFERENCES loai_trang_thai([Id_loai_trang_thai]),
    CONSTRAINT [FK_hoa_don_id_hoa_don]
        FOREIGN KEY ([id_hoa_don])
            REFERENCES hoa_don([Id_hoa_don])
);
go
CREATE TABLE tinh  (
    [Id_tinh] INT PRIMARY KEY IDENTITY(1,1),
    [ma_tinh] NVARCHAR(100),
    [ten_tinh] NVARCHAR(100),
    [ngay_tao] DATETIME DEFAULT GETDATE(),
    [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
);
go
CREATE TABLE huyen  (
    [Id_huyen] INT PRIMARY KEY IDENTITY(1,1),
    [ma_huyen] NVARCHAR(100),
    [ten_huyen] NVARCHAR(100),
	[id_tinh] INT,
	[ngay_tao] DATETIME DEFAULT GETDATE(),
    [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
    CONSTRAINT [FK_huyen_id_tinh]
        FOREIGN KEY ([id_tinh])
            REFERENCES tinh([Id_tinh])
);
go
CREATE TABLE xa  (
    [Id_xa] INT PRIMARY KEY IDENTITY(1,1),
    [ma_xa] NVARCHAR(100),
    [ten_xa] NVARCHAR(100),
	[id_huyen] INT,
	[ngay_tao] DATETIME DEFAULT GETDATE(),
    [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
    CONSTRAINT [FK_xa_id_xa]
        FOREIGN KEY ([id_huyen])
            REFERENCES huyen([Id_huyen])
);
GO
CREATE TABLE dia_chi_van_chuyen (
    [Id_dia_chi_van_chuyen] INT PRIMARY KEY IDENTITY(1,1),
    [id_tinh] INT,
    [id_huyen] INT,
    [id_xa] INT ,
    [dia_chi_cu_the] NVARCHAR(100),
    [ngay_tao] DATETIME DEFAULT GETDATE(),
    [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
    [trang_thai] BIT DEFAULT 1,
    [mo_ta] NVARCHAR(MAX),
	[id_nguoi_dung] INT,
    CONSTRAINT [FK_dia_chi_van_chuyen_id_nguoi_dung]
        FOREIGN KEY ([id_nguoi_dung])
            REFERENCES nguoi_dung([Id_nguoi_dung]),
    CONSTRAINT [FK_dia_chi_van_chuyen_id_tinh]
        FOREIGN KEY ([id_tinh])
            REFERENCES tinh([Id_tinh]),
    CONSTRAINT [FK_dia_chi_van_chuyen_id_huyen]
        FOREIGN KEY ([id_huyen])
            REFERENCES huyen([Id_huyen]),
    CONSTRAINT [FK_dia_chi_van_chuyen_id_xa]
        FOREIGN KEY ([id_xa])
            REFERENCES xa([Id_xa])
);
GO

CREATE TABLE phi_van_chuyen (
    [Id_phi_van_chuyen] INT PRIMARY KEY IDENTITY(1,1),
    [id_dia_chi_van_chuyen] INT,
    [so_tien_van_chuyen] DECIMAL(18),
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
  [ngay_giao_dich] DATETIME DEFAULT GETDATE(),
  [id_nguoi_dung] INT,
  CONSTRAINT [FK_lich_su_hoa_don_id_nguoi_dung]
    FOREIGN KEY ([id_nguoi_dung])
      REFERENCES [nguoi_dung]([Id_nguoi_dung])
);
go
CREATE TABLE [lich_su_thanh_toan] (
  [Id_lich_su_thanh_toan] INT PRIMARY KEY IDENTITY(1,1),
  [so_tien_thanh_toan] DECIMAL(18),
  [ngay_giao_dich] DATETIME DEFAULT GETDATE(),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [trang_thai_thanh_toan] BIT DEFAULT 1,
  [mo_ta] NVARCHAR(MAX),
  [id_nhan_vien] INT,
  [id_nguoi_dung] INT,
  [id_hoa_don] INT,
  CONSTRAINT [FK_lich_su_thanh_toan_id_nguoi_dung]
    FOREIGN KEY ([id_nguoi_dung])
      REFERENCES [nguoi_dung]([Id_nguoi_dung]),
	  CONSTRAINT [FK_lich_su_thanh_toan_id_hoa_don]
    FOREIGN KEY ([id_hoa_don])
      REFERENCES [hoa_don]([Id_hoa_don])
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
(N'Khách hàng lẽ', N'Khách hàng lẽ mua hàng tại quầy'),
(N'Nhân viên bán hàng', N'Nhân viên hỗ trợ bán hàng'),
(N'Nhân viên giao hàng', N'Người giao hàng đến tay khách hàng'),
(N'Quản lý kho', N'Người quản lý tồn kho');
go
-- Insert data for nguoi_dung
INSERT INTO nguoi_dung (ten_nguoi_dung, ma_nguoi_dung, email, sdt, ngay_sinh, dia_chi, gioi_tinh, mat_khau,id_vai_tro) VALUES 
(N'Phạm Thùy Dương', 'user001', 'duongpt@gmail.com', '0918829273', '2004-01-02', N'Hà Nội', N'Nữ', '$2a$10$KBFTerXFW6vJ4IDXgln38ulJg1cjq1ZTNBS/cN0HzLsyicNc76aKG',1),
(N'Lê Khả Hoàng', 'user002', 'hoanglk@gmail.com', '0912353678', '2004-01-03', N'Hà Nội', N'Nam', '$2a$10$KBFTerXFW6vJ4IDXgln38ulJg1cjq1ZTNBS/cN0HzLsyicNc76aKG',2),
(N'Nguyễn Trung Hiếu', 'user003', 'hieunt@gmail.com', '0916789535', '2004-01-04', N'Hà Nội', 'Nam', '$2a$10$KBFTerXFW6vJ4IDXgln38ulJg1cjq1ZTNBS/cN0HzLsyicNc76aKG',3),
(N'Lê Đình Linh', 'user004', 'linhld@gmail.com', '0912679346', '2004-01-05', N'Hà Nội', N'Nam', '$2a$10$KBFTerXFW6vJ4IDXgln38ulJg1cjq1ZTNBS/cN0HzLsyicNc76aKG',4),
(N'Hoàng Văn Hà', 'user005', 'hahv@gmail.com', '0918934754', '2004-01-06', N'Hà Nội', N'Nam', '$2a$10$KBFTerXFW6vJ4IDXgln38ulJg1cjq1ZTNBS/cN0HzLsyicNc76aKG',5);
go	
INSERT INTO loai_thong_bao (ten_loai_thong_bao)
VALUES
--Thông báo về Đơn hàng
(N'Đơn hàng được tạo thành công'),
(N'Đơn hàng đã được xác nhận'),
(N'Đơn hàng đã được giao'),
(N'Đơn hàng đã hoàn tất'),
(N'Đơn hàng bị hủy'),
--Thông báo về Khuyến mãi và Voucher
(N'Khuyến mãi mới'),
(N'Voucher sử dụng thành công'),
(N'Voucher được thêm vào tài khoản'),
--Thông báo về Sản phẩm
(N'Sản phẩm đã được thêm vào giỏ hàng'),
(N'Sản phẩm hết hàng'),
(N'Giảm giá sản phẩm'),
--Thông báo về Tình trạng thanh toán
(N'Thanh toán thành công'),
(N'Thanh toán thất bại'),
(N'Thanh toán đang chờ xử lý'),
--Thông báo về Vận chuyển và Giao hàng
(N'Đơn hàng đã được giao'),
(N'Giao hàng thành công'),
--Thông báo về Tài khoản và Bảo mật
(N'Đăng ký tài khoản thành công'),
(N'Đổi mật khẩu thành công'),
--Thông báo về Khách hàng hoặc Đánh giá
(N'Đánh giá sản phẩm'),
(N'Khách hàng thân thiết'),
--Thông báo về địa chỉ giao hàng
(N'Địa chỉ giao hàng đã được thêm mới');
go
INSERT INTO [trang_thai_giam_gia] (ten_trang_thai_giam_gia, mo_ta) VALUES
(N'Đang phát hành', N'Giảm giá đã được phát hành và có thể sử dụng.'),
(N'Đã sử dụng', N'Giảm giá đã được sử dụng và không còn giá trị.'),
(N'Hết hạn', N'Giảm giá không còn giá trị do đã hết hạn sử dụng.'),
(N'Chưa phát hành', N'Giảm giá đã được tạo nhưng chưa được phát hành cho người dùng.'),
(N'Bị xóa', N'Giảm giá đã bị xóa và không còn hiệu lực.'),
(N'Số lượng voucher đã hết', N'Số lượng giảm giá đã hết vui lòng thử lại vào dịp sau .');
go
-- Insert data for voucher
INSERT INTO voucher (ma_voucher,ten_voucher,kieu_giam_gia, gia_tri_giam_gia, so_luong, gia_tri_toi_da, so_tien_toi_thieu, mo_ta, ngay_bat_dau, ngay_ket_thuc,id_trang_thai_giam_gia) VALUES
(N'KM10',N'Giảm giá cho đơn hàng', 0,10, 100, 500000,2000000, N'Giảm 10% cho đơn hàng từ 50k', '2024-01-01', '2024-01-31', 3),
(N'KM20K',N'Giảm giá cho đơn hàng',1, 20000, 50,500000,2000000, N'Giảm 20.000đ cho đơn hàng từ 100k', '2024-02-01', '2024-02-28', 3),
(N'FREE_SHIP',N'Miễn phí vận chuyển cho đơn hàng',1, 0, 200,500000,2000000, N'Miễn phí vận chuyển cho đơn hàng từ 150k', '2024-03-01', '2024-03-31', 3),
(N'KM30', N'Giảm giá cho đơn hàng', 0, 30, 80, 500000, 1500000, N'Giảm 30% cho đơn hàng từ 100k', '2024-04-01', '2024-04-30', 3),
(N'BONUS50K', N'Giảm giá cho đơn hàng', 1, 50000, 60, 1000000, 3000000, N'Giảm 50.000đ cho đơn hàng từ 200k', '2024-05-01', '2024-05-31', 3);


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
/*
INSERT INTO lich_su_hoa_don (so_tien_thanh_toan, id_nguoi_dung) VALUES 
(500, 4),
(750, 2),
(300, 1),
(1200, 3),
(150, 5);
*/
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
(5),
(6);
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

-- Insert data for hoa_don
/*
INSERT INTO hoa_don (ma_hoa_don, id_nguoi_dung,id_nhan_vien, id_voucher, id_dia_chi_van_chuyen, ten_nguoi_nhan, phi_ship, dia_chi, sdt_nguoi_nhan, thanh_tien, mo_ta, id_pt_thanh_toan_hoa_don,loai) VALUES 
('HD001', 2,1, 1, 1,  N'Trần Văn A', 30.00, N'Số 1, Đường A, Quận 1', N'0123456789', 500.00, N'Hoa đơn cho sản phẩm A', 1,1),
('HD002', 2,1, 2, 2,  N'Nguyễn Thị B', 20.00, N'Số 2, Đường B, Quận 2', N'0123456788', 750.00, N'Hoa đơn cho sản phẩm B', 2,1),
('HD003', 2,1, 3, 1,N'Lê Văn C', 15.00, N'Số 3, Đường C, Quận 3', N'0123456787', 300.00, N'Hoa đơn cho sản phẩm C', 3,0),
('HD004', 3,1,4, 2,  N'Trần Thị D', 25.00, N'Số 4, Đường D, Quận 4', N'0123456786', 1200.00, N'Hoa đơn cho sản phẩm D', 4,0),
('HD005', 2,1, 5, 1,  N'Nguyễn Văn E', 10.00, N'Số 5, Đường E, Quận 5', N'0123456785', 150.00, N'Hoa đơn cho sản phẩm E', 5,0),
('HD006', 2,1, 5, 1,  N'Nguyễn Văn E', 10.00, N'Số 5, Đường E, Quận 5', N'0123456785', 150.00, N'Hoa đơn cho sản phẩm E', 5,1),
('HD007', 2,1, 5, 1, N'Nguyễn Văn E', 10.00, N'Số 5, Đường E, Quận 5', N'0123456785', 150.00, N'Hoa đơn cho sản phẩm E', 5,0),
('HD008', 3,1, 5, 1,  N'Nguyễn Văn E', 10.00, N'Số 5, Đường E, Quận 5', N'0123456785', 150.00, N'Hoa đơn cho sản phẩm E', 5,1),
('HD009', 2,1, 5, 1, N'Nguyễn Văn E', 10.00, N'Số 5, Đường E, Quận 5', N'0123456785', 150.00, N'Hoa đơn cho sản phẩm E', 5,1);
*/
go
-- Thêm các loại trạng thái vào bảng loai_trang_thai
INSERT INTO loai_trang_thai (ten_loai_trang_thai, mo_ta)
VALUES
(N'Tạo đơn hàng', N'Khách hàng đã tạo đơn hàng.'),
(N'Chờ xác nhận', N'Đơn hàng đang chờ xác nhận từ người bán hoặc hệ thống.'),
(N'Xác nhận đơn hàng', N'Người bán đã xác nhận đơn hàng.'),
(N'Chờ giao hàng', N'Đơn hàng đã được thanh toán thành công và đang chờ giao đến khách hàng.'),
(N'Đang giao hàng', N'Đơn hàng đang được vận chuyển và trên đường giao đến khách hàng.'),
(N'Chờ thanh toán', N'Đơn hàng đã được vận chuyển nhưng chưa thanh toán.'),
(N'Đã thanh toán thành công', N'Khách hàng đã thanh toán thành công.'),
(N'Hoàn thành', N'Đơn hàng đã được giao đến khách hàng thành công.'),
(N'Đã hủy', N'Đơn hàng bị hủy bỏ.'),
(N'Đã hoàn tiền', N'Đơn hàng đã bị hủy và tiền đã được hoàn trả cho khách hàng.');

-- Insert data for trang_thai_hoa_don
-- Thêm trạng thái cho hóa đơn với Id_hoa_don là 1
/*
INSERT INTO trang_thai_hoa_don (mo_ta,id_nhan_vien, id_loai_trang_thai, id_hoa_don)
VALUES
(N'Khách hàng đã tạo đơn hàng.', 1, 1, 1),  -- Trạng thái "Đặt hàng"
(N'Đơn hàng đang chờ xác nhận từ người bán.', 1, 2, 1),  -- Trạng thái "Chờ xác nhận"
(N'Người bán đã xác nhận đơn hàng.', 1, 3, 1),  -- Trạng thái "Xác nhận đơn hàng"
(N'Đơn hàng đang chờ vận chuyển.', 1, 4, 1),  -- Trạng thái "Đang vận chuyển"
(N'Đơn hàng đang được vận chuyển.', 1, 5, 1),  -- Trạng thái "Đang vận chuyển"
(N'Đơn hàng đang chờ thanh toán.', 1, 6, 1),  -- Trạng thái "Chờ thanh toán"
(N'Khách hàng đã thanh toán thành công.', 1, 7, 1),  -- Trạng thái "Đang xử lý thanh toán"
(N'Đơn hàng đã được giao thành công.', 1, 8, 1);  -- Trạng thái "Đã giao"
-- Insert data for trang_thai_hoa_don
-- Thêm trạng thái cho hóa đơn với Id_hoa_don là 1
INSERT INTO trang_thai_hoa_don (mo_ta,id_nhan_vien, id_loai_trang_thai, id_hoa_don)
VALUES
(N'Khách hàng đã tạo đơn hàng.', 1, 1, 7),  -- Trạng thái "Đặt hàng"
(N'Đơn hàng đang chờ xác nhận từ người bán.', 1, 2, 7),  -- Trạng thái "Chờ xác nhận"
(N'Người bán đã xác nhận đơn hàng.', 1, 3, 7),  -- Trạng thái "Xác nhận đơn hàng"
(N'Đơn hàng đang chờ vận chuyển.', 1, 4, 7),  -- Trạng thái "Đang vận chuyển"
(N'Đơn hàng đang được vận chuyển.', 1, 5, 7),  -- Trạng thái "Đang vận chuyển"
(N'Đơn hàng đang chờ thanh toán.', 1, 6, 7),  -- Trạng thái "Chờ thanh toán"
(N'Khách hàng đã thanh toán thành công.', 1, 7, 7),  -- Trạng thái "Đang xử lý thanh toán"
(N'Đơn hàng đã được giao thành công.', 1, 8, 7);  -- Trạng thái "Đã giao"
-- Thêm trạng thái cho hóa đơn với Id_hoa_don là 2
INSERT INTO trang_thai_hoa_don (mo_ta,id_nhan_vien, id_loai_trang_thai, id_hoa_don)
VALUES
(N'Khách hàng đã tạo đơn hàng.', 1,1, 2),  -- Trạng thái "Đặt hàng"
(N'Đơn hàng đang chờ xác nhận từ người bán.', 1,2, 2),  -- Trạng thái "Chờ xác nhận"
(N'Người bán đã xác nhận đơn hàng.', 1,3, 2),  -- Trạng thái "Xác nhận đơn hàng"
(N'Đơn hàng đang chờ vận chuyển.',1, 4, 7),  -- Trạng thái "Đang vận chuyển"
(N'Đơn hàng đang được vận chuyển.',1, 5, 7),  -- Trạng thái "Đang vận chuyển"
(N'Đơn hàng đang chờ thanh toán.', 1,6, 2),  -- Trạng thái "Chờ thanh toán"
(N'Đơn hàng đã bị hủy.',1, 9, 2);  -- Trạng thái "Đã hủy"

-- Thêm trạng thái cho hóa đơn với Id_hoa_don là 3
INSERT INTO trang_thai_hoa_don (mo_ta,id_nhan_vien, id_loai_trang_thai, id_hoa_don)
VALUES
(N'Khách hàng đã tạo đơn hàng.',1, 1, 3),  -- Trạng thái "Đặt hàng"
(N'Đơn hàng đang chờ xác nhận từ người bán.',1, 2, 3),  -- Trạng thái "Chờ xác nhận"
(N'Người bán đã xác nhận đơn hàng.',1, 3, 3),  -- Trạng thái "Xác nhận đơn hàng"
(N'Đơn hàng đang chờ vận chuyển.',1, 4, 3),  -- Trạng thái "Đang vận chuyển"
(N'Đơn hàng đang được vận chuyển.', 1,5, 3),  -- Trạng thái "Đang vận chuyển"
(N'Đơn hàng đang chờ thanh toán.',1, 6, 3),  -- Trạng thái "Chờ thanh toán"
(N'Khách hàng đã thanh toán thành công.',1, 7, 3),  -- Trạng thái "Đang xử lý thanh toán"
(N'Đơn hàng đã được giao thành công.',1, 8, 3);  -- Trạng thái "Đã giao"

-- Thêm trạng thái cho hóa đơn với Id_hoa_don là 4
INSERT INTO trang_thai_hoa_don (mo_ta, id_nhan_vien,id_loai_trang_thai, id_hoa_don)
VALUES
(N'Khách hàng đã tạo đơn hàng.', 1,1, 4),  -- Trạng thái "Đặt hàng"
(N'Đơn hàng đang chờ xác nhận từ người bán.', 1,2, 4),  -- Trạng thái "Chờ xác nhận"
(N'Đơn hàng đang chờ vận chuyển.', 1,4, 3),  -- Trạng thái "Đang vận chuyển"
(N'Đơn hàng đang được vận chuyển.', 1,5, 3),  -- Trạng thái "Đang vận chuyển"
(N'Người bán đã xác nhận đơn hàng.',1, 6, 4),  -- Trạng thái "Xác nhận đơn hàng"
(N'Đơn hàng đang chờ thanh toán.', 1,7, 4),  -- Trạng thái "Chờ thanh toán"
(N'Đơn hàng đã bị hủy và tiền đã được hoàn trả.',1, 10, 4);  -- Trạng thái "Đã hoàn tiền"
go
	-- Thêm trạng thái cho hóa đơn với Id_hoa_don là 1
INSERT INTO trang_thai_hoa_don (mo_ta,id_nhan_vien, id_loai_trang_thai, id_hoa_don)
VALUES
(N'Khách hàng đã tạo đơn hàng.', 1,1, 5),  -- Trạng thái "Đặt hàng"
(N'Đơn hàng đang chờ xác nhận từ người bán.',1, 2, 5);  -- Trạng thái "Đã giao"
go
	-- Thêm trạng thái cho hóa đơn với Id_hoa_don là 1
INSERT INTO trang_thai_hoa_don (mo_ta,id_nhan_vien, id_loai_trang_thai, id_hoa_don)
VALUES
(N'Khách hàng đã tạo đơn hàng.',1, 1, 6),  -- Trạng thái "Đặt hàng"
(N'Đơn hàng đang chờ xác nhận từ người bán.',1, 2, 6);  -- Trạng thái "Đã giao"
go
	-- Thêm trạng thái cho hóa đơn với Id_hoa_don là 1
INSERT INTO trang_thai_hoa_don (mo_ta, id_nhan_vien,id_loai_trang_thai, id_hoa_don)
VALUES
(N'Khách hàng đã tạo đơn hàng.',1, 1, 8),  -- Trạng thái "Đặt hàng"
(N'Đơn hàng đang chờ xác nhận từ người bán.',1, 2, 8);  -- Trạng thái "Đã giao"
go
	-- Thêm trạng thái cho hóa đơn với Id_hoa_don là 1
INSERT INTO trang_thai_hoa_don (mo_ta,id_nhan_vien, id_loai_trang_thai, id_hoa_don)
VALUES
(N'Khách hàng đã tạo đơn hàng.',1, 1, 9),  -- Trạng thái "Đặt hàng"
(N'Đơn hàng đang chờ xác nhận từ người bán.',1, 2, 9);  -- Trạng thái "Đã giao"
*/
/*
go
INSERT INTO tinh ( [ten_tinh]) VALUES
( N'Hà Nội'),
( N'Hồ Chí Minh'),
( N'Đà Nẵng'),
( N'Hải Phòng'),
( N'Cần Thơ'),
( N'An Giang'),
( N'Bình Dương'),
( N'Đắk Lắk'),
( N'Lâm Đồng'),
( N'Thanh Hóa');
INSERT INTO huyen ([ten_huyen], [id_tinh]) VALUES
-- Tỉnh Hà Nội
(N'Ba Đình', 1),
(N'Hoàn Kiếm', 1),
(N'Tây Hồ', 1),
(N'Cầu Giấy', 1),
(N'Đống Đa', 1),
-- Tỉnh Hồ Chí Minh
(N'Củ Chi', 2),
(N'Bình Thạnh', 2),
(N'Tân Bình', 2),
(N'Quận 1', 2),
(N'Quận 2', 2),
-- Tỉnh Đà Nẵng
(N'Hải Châu', 3),
(N'Cẩm Lệ', 3),
(N'Liên Chiểu', 3),
(N'Ngũ Hành Sơn', 3),
(N'Sơn Trà', 3),
-- Tỉnh Hải Phòng
(N'Hồng Bàng', 4),
(N'Lê Chân', 4),
(N'Ngô Quyền', 4),
(N'Kiến An', 4),
(N'Đồ Sơn', 4),
-- Tỉnh Cần Thơ
(N'Ninh Kiều', 5),
(N'Cái Răng', 5),
(N'Bình Thủy', 5),
(N'Ô Môn', 5),
(N'Thốt Nốt', 5),
-- Tỉnh An Giang
(N'Châu Đốc', 6),
(N'Long Xuyên', 6),
(N'Tân Châu', 6),
(N'Châu Phú', 6),
-- Tỉnh Bình Dương
(N'Thủ Dầu Một', 7),
(N'Dĩ An', 7),
(N'Bến Cát', 7),
(N'Tân Uyên', 7),
-- Tỉnh Đắk Lắk
(N'Buôn Ma Thuột', 8),
(N'Krông Pắk', 8),
(N'Ea H’Leo', 8),
(N'M’Đrắk', 8),
-- Tỉnh Lâm Đồng
(N'Đà Lạt', 9),
(N'Bảo Lộc', 9),
(N'Lâm Hà', 9),
(N'Di Linh', 9),
-- Tỉnh Thanh Hóa
(N'Thanh Hóa', 10),
(N'Sầm Sơn', 10),
(N'Bỉm Sơn', 10),
(N'Hà Trung', 10);
INSERT INTO xa ([ten_xa], [id_huyen]) VALUES
-- Hà Nội
(N'Phúc Xá', 1),
(N'Trúc Bạch', 1),
(N'Yên Phụ', 1),
(N'Mai Dịch', 2),
(N'Dịch Vọng', 2),
(N'Ngã Tư Sở', 3),
(N'Kim Liên', 4),
(N'Văn Quán', 4),
-- Hồ Chí Minh
(N'An Phú', 6),
(N'Đa Kao', 6),
(N'Bình An', 6),
(N'Tân An', 7),
(N'Phú Hòa', 7),
(N'Bình Chánh', 8),
(N'Nhà Bè', 8),
-- Đà Nẵng
(N'Tam Thuận', 10),
(N'Vĩnh Niệm', 10),
(N'Hòa Khánh', 11),
(N'An Hải Bắc', 12),
(N'Mỹ An', 13),
-- Hải Phòng
(N'Vạn Mỹ', 14),
(N'Cát Bi', 14),
(N'Lạc Viên', 15),
(N'Tràng Cát', 15),
-- Cần Thơ
(N'An Khánh', 16),
(N'Bình Thủy', 16),
(N'Thới Lai', 17),
(N'Phong Điền', 17),
-- An Giang
(N'Long Xuyên', 18),
(N'Châu Đốc', 18),
(N'Châu Phú', 19),
(N'Tân Châu', 19),
-- Bình Dương
(N'Bình An', 20),
(N'Dĩ An', 20),
(N'Tân Uyên', 21),
-- Đắk Lắk
(N'Buôn Ma Thuột', 22),
(N'Krông Pắk', 22),
-- Lâm Đồng
(N'Đà Lạt', 23),
(N'Bảo Lộc', 23),
(N'Di Linh', 23),
-- Thanh Hóa
(N'Sầm Sơn', 24),
(N'Bỉm Sơn', 24),
(N'Thọ Xuân', 24);


-- Dữ liệu bảng dia_chi_van_chuyen (Địa chỉ vận chuyển)
INSERT INTO dia_chi_van_chuyen ([id_tinh], [id_huyen], [id_xa], [dia_chi_cu_the], [id_nguoi_dung]) VALUES
(1, 1, 1, N'Số 10 Phố Phúc Xá, Hà Nội', 1),
(2, 2, 4, N'Số 15 Đường An Phú, Hồ Chí Minh', 2),
(3, 3, 7, N'Số 20 Đường Tam Thuận, Đà Nẵng', 3),
(4, 4, 8, N'Số 30 Đường Vĩnh Niệm, Hải Phòng', 4),
(5, 5, 9, N'Số 5 Đường Cái Khế, Cần Thơ', 5);
*/
go
-- Insert data for pt_thanh_toan
INSERT INTO pt_thanh_toan (ma_thanh_toan, ten_phuong_thuc, mo_ta) VALUES 
(N'TT001', N'Tiền mặt', N'Transfer qua ngân hàng cho đơn hàng.'),
(N'TT002', N'Ví Điện Tử Vnpay', N'Sử dụng ví điện tử để thanh toán.'),
(N'TT003', N'MBBank', N'Sử dụng thẻ tín dụng để thanh toán.'),
(N'TT004', N'Thu Tiền Tận Nơi', N'Nhân viên sẽ đến thu tiền tại địa chỉ giao hàng.'),
(N'TT005', N'Thanh Toán Trực Tiếp', N'Khách hàng thanh toán trực tiếp tại cửa hàng.');
-- Insert data for phi_van_chuyen
/*
INSERT INTO phi_van_chuyen (id_dia_chi_van_chuyen,so_tien_van_chuyen,id_hoa_don, mo_ta) VALUES 
(1,10000,1, N'Phí vận chuyển cho đơn hàng nội tỉnh.'),
(2,10000,2, N'Phí vận chuyển cho đơn hàng liên tỉnh.'),
(3,10000,3, N'Phí vận chuyển cho đơn hàng dưới 1kg.'),
(4,10000,4, N'Phí vận chuyển cho đơn hàng trên 1kg.'),
(5,10000,5, N'Phí vận chuyển cho đơn hàng đặc biệt.');
go
*/
-- Insert data for pt_thanh_toan_hoa_don
/*
INSERT INTO pt_thanh_toan_hoa_don (id_pt_thanh_toan, ngay_giao_dich, mo_ta, trang_thai, noi_dung_thanh_toan, id_hoa_don) VALUES 
(1, GETDATE(), N'Thanh toán đơn hàng 001', N'Hoàn Thành', N'Thanh toán đơn hàng 001', 1),
(2, GETDATE(), N'Thanh toán đơn hàng 002', N'Hoàn Thành', N'Thanh toán đơn hàng 002', 2),
(3, GETDATE(), N'Thanh toán đơn hàng 003', N'Hoàn Thành', N'Thanh toán đơn hàng 003', 3),
(1, GETDATE(), N'Thanh toán đơn hàng 004', N'Hoàn Thành', N'Thanh toán đơn hàng 004', 4),
(2, GETDATE(), N'Thanh toán đơn hàng 005', N'Hoàn Thành', N'Thanh toán đơn hàng 005', 5);
*/

go
INSERT INTO xac_thuc (ma_xac_thuc, id_nguoi_dung, mo_ta) VALUES 
(N'XAC001', 1, N'Xác thực đăng ký tài khoản'),
(N'XAC002', 2, N'Xác thực email'),
(N'XAC003', 1, N'Xác thực số điện thoại'),
(N'XAC004', 3, N'Xác thực khôi phục mật khẩu'),
(N'XAC005', 2, N'Xác thực đăng nhập');
go


-- Insert data for san_pham
INSERT INTO san_pham (ma_san_pham,ten_san_pham, gia_ban, mo_ta, id_danh_muc,trang_thai) VALUES 
('SP001',N'Áo phông hình bàn tay',120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
('SP002',N'Áo phông butterfly',  120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1,1),
('SP003',N'Áo phông cotton',  120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
('SP004',N'Áo phông ENJOYABLE',120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
('SP005',N'Áo phông loang',  120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
('SP006',N'Áo phông holiday 1961', 120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
('SP007',N'Áo phông nam nữ 1984', 120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
('SP008',N'Áo phông nam nữ oversize',120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
('SP009',N'Áo phông tay lỡ',  120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
('SP0010',N'Áo phông thể thao',  120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),
('SP0011',N'Áo phông SPORT FASHION', 120000 , N'Áo phông cotton mềm mại, thiết kế cổ tròn với tay ngắn', 1, 1),

('SP0012',N'Áo sơ mi đũi nơ thắt eo', 120000 , N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 1),
('SP0013',N'Áo sơ mi nam kẻ sọc', 120000 , N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 1),
('SP0014',N'Áo sơ mi lụa công sở', 120000 , N'Áo sơ mi được làm từ lụa mềm mại, thoáng khí', 2, 1),
('SP0015',N'Áo sơ mi nam loang', 120000 , N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 1),
('SP0016',N'Áo sơ mi ngắn siết eo',  120000 , N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 1),
('SP0017',N'Áo sơ mi tay ngắn túi hộp', 120000 , N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 1),
('SP0018',N'Áo sơ mi thắt cà vạt', 120000 , N'Áo sơ mi được làm theo phong cách Nhật', 2,1),
('SP0019',N'Áo sơ mi sọc đơn giản',  120000 , N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 1),
('SP0020',N'Áo sơ mi tay ngắn', 120000 , N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2,1),
('SP0021',N'Áo sơ mi trơn',  120000 , N'Áo sơ mi được làm từ 100% cotton mềm mại, thoáng khí', 2, 1),

('SP0022',N'Áo ấm lông cừu', 120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 1),
('SP0023',N'Áo béo buộc nơ', 120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 1),
('SP0024',N'Áo phao bông',  120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3,1),
('SP0025',N'Áo phao cài khuy', 120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 1),
('SP0026',N'Áo phao gile', 120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 1),
('SP0027',N'Áo phao cài khuy cổ', 120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3,1),
('SP0028',N'Áo phao cài lửng thời trang', 120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3, 1),
('SP0029',N'Áo phao nhung cừu', 120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3,1),
('SP0030',N'Áo phao cài NIKE',  120000 , N'Áo phao dày dặn, giúp giữ ấm hiệu quả trong những ngày đông lạnh giá', 3,1),

('SP0031',N'Áo chống nắng viền tròn', 120000 , N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 1),
('SP0032',N'Áo chống nắng toàn thân',  120000 , N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 1),
('SP0033',N'Áo chống nắng thông hơi', 120000 , N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4,1),
('SP0034',N'Áo chống nắng thời trang', 120000 , N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 1),
('SP0035',N'Áo chống nắng thể thao', 120000 , N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 1),
('SP0036',N'Áo chống nắng LV',  120000 , N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 1),
('SP0037',N'Áo chống nắng dài xoe',  120000 , N'Áo chống nắng chất liệu vải thoáng mát, nhẹ nhàng và có khả năng chống tia UV', 4, 1),


('SP0038',N'Quần baggy kaki',  120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5,1),
('SP0039',N'Quần đũi dài nam',  120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 1),
('SP0040',N'Quần đũi dài nữ', 120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 1),
('SP0041',N'Quần ống rộng cạp cao',  120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 1),
('SP0042',N'Quần ống rộng nam',  120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 1),
('SP0043',N'Quần đũi rộng túi hộp', 120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 1),
('SP0044',N'Quần suông đơn giản', 120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 1),
('SP0045',N'Quần suông rộng', 120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 5, 1),

('SP0046',N'Quần ống rộng suông',  120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6, 1),
('SP0047',N'Quần sort nữ cá tính', 120000 , N'Quần sort chất liệu kaki mềm mại, co giãn nhẹ', 6, 1),
('SP0048',N'Quần sort nữ đũi', 120000 , N'Quần sort chất liệu đũi mềm mại, co giãn nhẹ', 6, 1),
('SP0049',N'Quần đùi túi hộp đứng', 120000 , N'Quần dài dáng suông, chất liệu đũi mềm mại, co giãn nhẹ', 6, 1),
('SP0050',N'Quần jean cạp trễ',  120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6,1),
('SP0051',N'Quần jean thời trang', 120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6, 1),
('SP0052',N'Quần sort bò rộng',  120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6,1),
('SP0053',N'Quần sort tây nam', 120000 , N'Quần sort chất liệu kaki mềm mại, co giãn nhẹ', 6, 1),
('SP0054',N'Quần suông dài basic', 120000 , N'Quần dài dáng suông, chất liệu kaki mềm mại, co giãn nhẹ', 6,1);
go
INSERT INTO danh_gia (id_nguoi_dung, id_san_pham, noi_dung, diem) VALUES 
(1, 1, N'Sản phẩm đẹp như trên mô tả', 5),
(2, 2, N'Sản phẩm chất lượng', 4),
(3, 3, N'Sản phẩm tốt, phù hợp giá tiền', 5),
(4, 4, N'Chất vải tốt, mặc rất thoải mái', 4),
(5, 5, N'Áo chất lượng, đáng tiền', 4);
go
/*
-- Giả sử bạn đã có các bản ghi trong bảng dot_giam_gia
INSERT INTO dot_giam_gia (ten_dot_giam_gia, gia_tri_giam_gia, mo_ta, ngay_bat_dau, ngay_ket_thuc,id_trang_thai_giam_gia)
VALUES 
(N'Khuyến mãi mùa hè', 20000, N'Giảm giá cho các sản phẩm mùa hè', GETDATE(), DATEADD(DAY, 30, GETDATE()),1);

-- Lấy Id_dot_giam_gia vừa tạo
DECLARE @Id_dot_giam_gia INT = SCOPE_IDENTITY();

-- Chèn dữ liệu vào bảng giam_gia_san_pham
INSERT INTO giam_gia_san_pham (Id_dot_giam_gia, id_san_pham,gia_khuyen_mai)
VALUES
(@Id_dot_giam_gia, 1,100000),
(@Id_dot_giam_gia, 2,100000),
(@Id_dot_giam_gia, 3,100000),
(@Id_dot_giam_gia, 4,100000),
(@Id_dot_giam_gia, 5,100000),
( @Id_dot_giam_gia, 6,100000);
go
*/
-- Insert data for san_pham_chi_tiet
INSERT INTO san_pham_chi_tiet (ma_san_pham_chi_tiet,so_luong, id_kich_thuoc_chi_tiet, id_mau_sac_chi_tiet, id_chat_lieu_chi_tiet, id_san_pham)
VALUES 
/* Áo phông */
('SPCT001',3,  1, 3, 1, 1),
('SPCT002',100, 2, 3, 1,  1),
('SPCT003',100,  3, 3, 1,  1),
('SPCT004',100,  4, 3, 1, 1),
('SPCT005',100,  1, 6, 1,  1),
('SPCT006',100,  2, 6, 1,  1),
('SPCT007',100,  3, 6, 1,  1),
('SPCT008',100,  4, 6, 1,  1),
('SPCT009',100,  1, 4, 1,  1),
('SPCT0010',100,  2, 4, 1,  1),
('SPCT0011',100,  3, 4, 1,1),
('SPCT0012',100,  4, 4, 1, 1),
('SPCT0013',100,  1, 2, 1, 2),
('SPCT0014',100,  2, 2, 1, 2),
('SPCT0015',100,  3, 2, 1,  2),
('SPCT0016',100,  4, 2, 1,  2),
('SPCT0017',100,  1, 13, 1, 2),
('SPCT0018',100,  2, 13, 1, 2),
('SPCT0019',100,  3, 13, 1, 2),
('SPCT0020',100,  4, 13, 1,  2),
('SPCT0021',100,  1, 6, 1,  2),
('SPCT0022',100,  2, 6, 1,  2),
('SPCT0023',100,  3, 6, 1,  2),
('SPCT0024',100,  4, 6, 1,  2),
('SPCT0025',100,  1, 9, 1,  3),
('SPCT0026',100,  2, 9, 1,  3),
('SPCT0027',100,  3, 9, 1,  3),
('SPCT0028',100,  4, 9, 1,  3),
('SPCT0029',100,  1, 5, 1,  3),
('SPCT0030',100,  2, 5, 1,  3),
('SPCT0031',100,  3, 5, 1,  3),
('SPCT0032',100,  4, 5, 1,  3),
('SPCT0033',100,  1, 4, 1,  3),
('SPCT0034',100,  2, 4, 1,  3),
('SPCT0035',100,  3, 4, 1,  3),
('SPCT0036',100,  4, 4, 1,  3),
('SPCT0037',100,  1, 10, 1,  4),
('SPCT0038',100,  2, 10, 1,  4),
('SPCT0039',100,  3, 10, 1,  4),
('SPCT0040',100,  4, 10, 1,  4),
('SPCT0041',100,  1, 11, 1,  4),
('SPCT0042',100,  2, 11, 1,  4),
('SPCT0043',100,  3, 11, 1,  4),
('SPCT0044',100,  4, 11, 1,  4),
('SPCT0045',100,  1, 4, 1,  4),
('SPCT0046',100,  2, 4, 1,  4),
('SPCT0047',100,  3, 4, 1,  4),
('SPCT0048',100,  4, 4, 1,  4),
('SPCT0049',100,  1, 8, 1,  5),
('SPCT0050',100,  2, 8, 1,  5),
('SPCT0051',100,  3, 8, 1,  5),
('SPCT0052',100,  4, 8, 1,  5),
('SPCT0053',100,  1, 10, 1,  5),
('SPCT0054',100,  2, 10, 1,  5),
('SPCT0055',100,  3, 10, 1,  5),
('SPCT0056',100,  4, 10, 1,  5),
('SPCT0057',100,  1, 1, 1,  6),
('SPCT0058',100,  2, 1, 1,  6),
('SPCT0059',100,  3, 1, 1,  6),
('SPCT0060',100,  4, 1, 1,  6),
('SPCT0061',100,  1, 10, 1,  6),
('SPCT0062',100,  2, 10, 1,  6),
('SPCT0063',100,  3, 10, 1,  6),
('SPCT0064',100,  4, 10, 1,  6),
('SPCT0065',100,  1, 12, 1,  6),
('SPCT0066',100,  2, 12, 1,  6),
('SPCT0067',100,  3, 12, 1,  6),
('SPCT0068',100,  4, 12, 1,  6),
('SPCT0069',100,  1, 1, 1,  7),
('SPCT0070',100,  2, 1, 1,  7),
('SPCT0071',100,  3, 1, 1,  7),
('SPCT0072',100,  4, 1, 1,  7),
('SPCT0073',100,  1, 10, 1,  7),
('SPCT0074',100,  2, 10, 1,  7),
('SPCT0075',100,  3, 10, 1,  7),
('SPCT0076',100,  4, 10, 1,  7),
('SPCT0077',100,  1, 9, 1,  7),
('SPCT0078',100,  2, 9, 1,  7),
('SPCT0079',100,  3, 9, 1,  7),
('SPCT0080',100,  4, 9, 1,  7),
('SPCT0081',100,  1, 9, 1,  8),
('SPCT0082',100,  2, 9, 1,  8),
('SPCT0083',100,  3, 9, 1,  8),
('SPCT0084',100,  4, 9, 1,  8),
('SPCT0085',100,  1, 10, 1,  8),
('SPCT0086',100,  2, 10, 1,  8),
('SPCT0087',100,  3, 10, 1,  8),
('SPCT0088',100,  4, 10, 1,  8),
('SPCT0089',100,  1, 9, 1,  9),
('SPCT0090',100,  2, 9, 1,  9),
('SPCT0091',100,  3, 9, 1,  9),
('SPCT0092',100,  4, 9, 1,  9),
('SPCT0093',100,  1, 8, 1,  9),
('SPCT0094',100,  2, 8, 1,  9),
('SPCT0095',100,  3, 8, 1,  9),
('SPCT0096',100,  4, 8, 1,  9),
('SPCT0097',100,  1, 4, 1,  9),
('SPCT0098',100,  2, 4, 1,  9),
('SPCT0099',100,  3, 4, 1,  9),
('SPCT0100',100,  4, 4, 1,  9),
('SPCT0101',100,  1, 1, 1, 10),
('SPCT0102',100,  2, 1, 1, 10),
('SPCT0103',100,  3, 1, 1, 10),
('SPCT0104',100,  4, 1, 1, 10),
('SPCT0105',100,  1, 3, 1, 10),
('SPCT0106',100,  2, 3, 1, 10),
('SPCT0107',100,  3, 3, 1, 10),
('SPCT0108',100,  4, 3, 1, 10),
('SPCT0109',100,  1, 6, 1, 10),
('SPCT0110',100,  2, 6, 1, 10),
('SPCT0111',100,  3, 6, 1, 10),
('SPCT0112',100,  4, 6, 1, 10),
('SPCT0113',100,  1, 1, 1, 11),
('SPCT0114',100,  2, 1, 1, 11),
('SPCT0115',100,  3, 1, 1, 11),
('SPCT0116',100,  4, 1, 1, 11),
('SPCT0117',100,  1, 6, 1, 11),
('SPCT0118',100,  2, 6, 1, 11),
('SPCT0119',100,  3, 6, 1, 11),
('SPCT0120',100,  4, 6, 1, 11),
('SPCT0121',100,  1, 9, 1, 11),
('SPCT0122',100,  2, 9, 1, 11),
('SPCT0123',100,  3, 9, 1, 11),
('SPCT0124',100,  4, 9, 1, 11),
/* Áo sơ mi */
('SPCT0125',100,   1, 11, 1,  12),
('SPCT0126',100,   2, 11, 1,  12),
('SPCT0127',100,   3, 11, 1, 12),
('SPCT0128',100,   4, 11, 1,  12),
('SPCT0129',100,    1, 10, 1,  12),
('SPCT0130',100,    2, 10, 1,  12),
('SPCT0131',100,    3, 10, 1,  12),
('SPCT0132',100,    4, 10, 1,  12),
('SPCT0133',100,   1, 2, 1,  13),
('SPCT0134',100,   2, 2, 1,  13),
('SPCT0135',100,   3, 2, 1,  13),
('SPCT0136',100,   4, 2, 1,  13),
('SPCT0137',100,   1, 1, 1,  13),
('SPCT0138',100,   2, 1, 1,  13),
('SPCT0139',100,   3, 1, 1,  13),
('SPCT0140',100,   4, 1, 1,  13),

('SPCT0141', 100, 3, 1, 1, 13),
('SPCT0142', 100, 4, 1, 1, 13),
('SPCT0143', 100, 1, 2, 2, 14),
('SPCT0144', 100, 2, 2, 2, 14),
('SPCT0145', 100, 3, 2, 2, 14),
('SPCT0146', 100, 4, 2, 2, 14),
('SPCT0147', 100, 1, 3, 2, 14),
('SPCT0148', 100, 2, 3, 2, 14),
('SPCT0149', 100, 3, 3, 2, 14),
('SPCT0150', 100, 4, 3, 2, 14),
('SPCT0151', 100, 1, 2, 1, 15),
('SPCT0152', 100, 2, 2, 1, 15),
('SPCT0153', 100, 3, 2, 1, 15),
('SPCT0154', 100, 4, 2, 1, 15),
('SPCT0155', 100, 1, 1, 1, 15),
('SPCT0156', 100, 2, 1, 1, 15),
('SPCT0157', 100, 3, 1, 1, 15),
('SPCT0158', 100, 4, 1, 1, 15),
('SPCT0159', 100, 1, 2, 1, 16),
('SPCT0160', 100, 2, 2, 1, 16),
('SPCT0161', 100, 3, 2, 1, 16),
('SPCT0162', 100, 4, 2, 1, 16),
('SPCT0163', 100, 1, 3, 1, 16),
('SPCT0164', 100, 2, 3, 1, 16),
('SPCT0165', 100, 3, 3, 1, 16),
('SPCT0166', 100, 4, 3, 1, 16),
('SPCT0167', 100, 1, 2, 1, 17),
('SPCT0168', 100, 2, 2, 1, 17),
('SPCT0169', 100, 3, 2, 1, 17),
('SPCT0170', 100, 4, 2, 1, 17),
('SPCT0171', 100, 1, 10, 1, 17),
('SPCT0172', 100, 2, 10, 1, 17),
('SPCT0173', 100, 3, 10, 1, 17),
('SPCT0174', 100, 4, 10, 1, 17),
('SPCT0175', 100, 1, 7, 1, 17),
('SPCT0176', 100, 2, 7, 1, 17),
('SPCT0177', 100, 3, 7, 1, 17),
('SPCT0178', 100, 4, 7, 1, 17),
('SPCT0179', 100, 1, 9, 1, 18),
('SPCT0180', 100, 2, 9, 1, 18),
('SPCT0181', 100, 3, 9, 1, 18),
('SPCT0182', 100, 4, 9, 1, 18),
('SPCT0183', 100, 1, 12, 1, 18),
('SPCT0184', 100, 2, 12, 1, 18),
('SPCT0185', 100, 3, 12, 1, 18),
('SPCT0186', 100, 4, 12, 1, 18),
('SPCT0187', 100, 1, 3, 1, 18),
('SPCT0188', 100, 2, 3, 1, 18),
('SPCT0189', 100, 3, 3, 1, 18),
('SPCT0190', 100, 4, 3, 1, 18),
('SPCT0191', 100, 1, 3, 1, 19),
('SPCT0192', 100, 2, 3, 1, 19),
('SPCT0193', 100, 3, 3, 1, 19),
('SPCT0194', 100, 4, 3, 1, 19),
('SPCT0195', 100, 1, 8, 1, 19),
('SPCT0196', 100, 2, 8, 1, 19),
('SPCT0197', 100, 3, 8, 1, 19),
('SPCT0198', 100, 4, 8, 1, 19),
('SPCT0199', 100, 1, 2, 1, 20),
('SPCT0200', 100, 2, 2, 1, 20),
('SPCT0201', 100, 3, 2, 1, 20),
('SPCT0202', 100, 4, 2, 1, 20),
('SPCT0203', 100, 1, 7, 1, 20),
('SPCT0204', 100, 2, 7, 1, 20),
('SPCT0205', 100, 3, 7, 1, 20),
('SPCT0206', 100, 4, 7, 1, 20),
('SPCT0207', 100, 1, 4, 1, 21),
('SPCT0208', 100, 2, 4, 1, 21),
('SPCT0209', 100, 3, 4, 1, 21),
('SPCT0210', 100, 4, 4, 1, 21),
('SPCT0211', 100, 1, 8, 1, 21),
('SPCT0212', 100, 2, 8, 1, 21),
('SPCT0213', 100, 3, 8, 1, 21),
('SPCT0214', 100, 4, 8, 1, 21),
('SPCT0215', 100, 1, 3, 1, 21),
('SPCT0216', 100, 2, 3, 1, 21),
('SPCT0217', 100, 3, 3, 1, 21),
('SPCT0218', 100, 4, 3, 1, 21),


('SPCT0219', 100, 1, 12, 1, 22),
('SPCT0220', 100, 2, 12, 1, 22),
('SPCT0221', 100, 3, 12, 1, 22),
('SPCT0222', 100, 4, 12, 1, 22),
('SPCT0223', 100, 1, 8, 1, 22),
('SPCT0224', 100, 2, 8, 1, 22),
('SPCT0225', 100, 3, 8, 1, 22),
('SPCT0226', 100, 4, 8, 1, 22),
('SPCT0227', 100, 1, 1, 1, 22),
('SPCT0228', 100, 2, 1, 1, 22),
('SPCT0229', 100, 3, 1, 1, 22),
('SPCT0230', 100, 4, 1, 1, 22),
('SPCT0231', 100, 1, 1, 1, 23),
('SPCT0232', 100, 2, 1, 1, 23),
('SPCT0233', 100, 3, 1, 1, 23),
('SPCT0234', 100, 4, 1, 1, 23),
('SPCT0235', 100, 1, 9, 1, 23),
('SPCT0236', 100, 2, 9, 1, 23),
('SPCT0237', 100, 3, 9, 1, 23),
('SPCT0238', 100, 4, 9, 1, 23),
('SPCT0239', 100, 1, 12, 1, 23),
('SPCT0240', 100, 2, 12, 1, 23),
('SPCT0241', 100, 3, 12, 1, 23),
('SPCT0242', 100, 4, 12, 1, 23),
('SPCT0243', 100, 1, 1, 1, 24),
('SPCT0244', 100, 2, 1, 1, 24),
('SPCT0245', 100, 3, 1, 1, 24),
('SPCT0246', 100, 4, 1, 1, 24),
('SPCT0247', 100, 1, 8, 1, 24),
('SPCT0248', 100, 2, 8, 1, 24),
('SPCT0249', 100, 3, 8, 1, 24),
('SPCT0250', 100, 4, 8, 1, 24),
('SPCT0251', 100, 1, 2, 1, 24),
('SPCT0252', 100, 2, 2, 1, 24),
('SPCT0253', 100, 3, 2, 1, 24),
('SPCT0254', 100, 4, 2, 1, 24),
('SPCT0255', 100, 1, 1, 1, 25),
('SPCT0256', 100, 2, 1, 1, 25),
('SPCT0257', 100, 3, 1, 1, 25),
('SPCT0258', 100, 4, 1, 1, 25),
('SPCT0259', 100, 1, 7, 1, 25),
('SPCT0260', 100, 2, 7, 1, 25),
('SPCT0261', 100, 3, 7, 1, 25),
('SPCT0262', 100, 4, 7, 1, 25),
('SPCT0263', 100, 1, 12, 1, 25),
('SPCT0264', 100, 2, 12, 1, 25),
('SPCT0265', 100, 3, 12, 1, 25),
('SPCT0266', 100, 4, 12, 1, 25),
('SPCT0267', 100, 1, 1, 1, 26),
('SPCT0268', 100, 2, 1, 1, 26),
('SPCT0269', 100, 3, 1, 1, 26),
('SPCT0270', 100, 4, 1, 1, 26),
('SPCT0271', 100, 2, 5, 1, 26),
('SPCT0272', 100, 2, 5, 1, 26),
('SPCT0273', 100, 2, 5, 1, 26),
('SPCT0274', 100, 2, 5, 1, 26),
('SPCT0275', 100, 1, 2, 1, 26),
('SPCT0276', 100, 2, 2, 1, 26),
('SPCT0277', 100, 3, 2, 1, 26),
('SPCT0278', 100, 4, 2, 1, 26),
('SPCT0279', 100, 1, 8, 1, 27),
('SPCT0280', 100, 2, 8, 1, 27),
('SPCT0281', 100, 3, 8, 1, 27),
('SPCT0282', 100, 4, 8, 1, 27),
('SPCT0283', 100, 1, 7, 1, 27),
('SPCT0284', 100, 2, 7, 1, 27),
('SPCT0285', 100, 3, 7, 1, 27),
('SPCT0286', 100, 4, 7, 1, 27),
('SPCT0287', 100, 1, 1, 1, 27),
('SPCT0288', 100, 2, 1, 1, 27),
('SPCT0289', 100, 3, 1, 1, 27),
('SPCT0290', 100, 4, 1, 1, 27),
('SPCT0291', 100, 1, 2, 1, 28),
('SPCT0292', 100, 2, 2, 1, 28),
('SPCT0293', 100, 3, 2, 1, 28),
('SPCT0294', 100, 4, 2, 1, 28),
('SPCT0295', 100, 1, 9, 1, 28),
('SPCT0296', 100, 2, 9, 1, 28),
('SPCT0297', 100, 3, 9, 1, 28),
('SPCT0298', 100, 4, 9, 1, 28),
('SPCT0299', 100, 1, 1, 1, 28),
('SPCT0300', 100, 2, 1, 1, 28),
('SPCT0301', 100, 3, 1, 1, 28),
('SPCT0302', 100, 4, 1, 1, 28),

('SPCT0303', 100, 1, 1, 1, 29),
('SPCT0304', 100, 2, 1, 1, 29),
('SPCT0305', 100, 3, 1, 1, 29),
('SPCT0306', 100, 4, 1, 1, 29),
('SPCT0307', 100, 1, 1, 1, 29),
('SPCT0308', 100, 2, 1, 1, 29),
('SPCT0309', 100, 3, 1, 1, 29),
('SPCT0310', 100, 4, 1, 1, 29),
('SPCT0311', 100, 1, 1, 1, 29),
('SPCT0312', 100, 2, 1, 1, 29),
('SPCT0313', 100, 3, 1, 1, 29),
('SPCT0314', 100, 4, 1, 1, 29),
('SPCT0315', 100, 1, 1, 1, 30),
('SPCT0316', 100, 2, 1, 1, 30),
('SPCT0317', 100, 3, 1, 1, 30),
('SPCT0318', 100, 4, 1, 1, 30),
('SPCT0319', 100, 1, 14, 1, 30),
('SPCT0320', 100, 2, 14, 1, 30),
('SPCT0321', 100, 3, 14, 1, 30),
('SPCT0322', 100, 4, 14, 1, 30),
('SPCT0323', 100, 1, 2, 1, 30),
('SPCT0324', 100, 2, 2, 1, 30),
('SPCT0325', 100, 3, 2, 1, 30),
('SPCT0326', 100, 4, 2, 1, 30),


('SPCT0327', 100, 1, 8, 2, 31),
('SPCT0328', 100, 2, 8, 2, 31),
('SPCT0329', 100, 3, 8, 2, 31),
('SPCT0330', 100, 4, 8, 2, 31),
('SPCT0331', 100, 1, 1, 2, 31),
('SPCT0332', 100, 2, 1, 2, 31),
('SPCT0333', 100, 3, 1, 2, 31),
('SPCT0334', 100, 4, 1, 2, 31),
('SPCT0335', 100, 1, 10, 2, 31),
('SPCT0336', 100, 2, 10, 2, 31),
('SPCT0337', 100, 3, 10, 2, 31),
('SPCT0338', 100, 4, 10, 2, 31),
('SPCT0339', 100, 1, 3, 2, 32),
('SPCT0340', 100, 2, 3, 2, 32),
('SPCT0341', 100, 3, 3, 2, 32),
('SPCT0342', 100, 4, 3, 2, 32),
('SPCT0343', 100, 1, 10, 2, 32),
('SPCT0344', 100, 2, 10, 2, 32),
('SPCT0345', 100, 3, 10, 2, 32),
('SPCT0346', 100, 4, 10, 2, 32),
('SPCT0347', 100, 1, 2, 2, 32),
('SPCT0348', 100, 2, 2, 2, 32),
('SPCT0349', 100, 3, 2, 2, 32),
('SPCT0350', 100, 4, 2, 2, 32),
('SPCT0351', 100, 1, 8, 2, 33),
('SPCT0352', 100, 2, 8, 2, 33),
('SPCT0353', 100, 3, 8, 2, 33),
('SPCT0354', 100, 4, 8, 2, 33),
('SPCT0355', 100, 1, 3, 2, 33),
('SPCT0356', 100, 2, 3, 2, 33),
('SPCT0357', 100, 3, 3, 2, 33),
('SPCT0358', 100, 4, 3, 2, 33),
('SPCT0359', 100, 1, 1, 2, 33),
('SPCT0360', 100, 2, 1, 2, 33),
('SPCT0361', 100, 3, 1, 2, 33),
('SPCT0362', 100, 4, 1, 2, 33),
('SPCT0363', 100, 1, 2, 2, 34),
('SPCT0364', 100, 2, 2, 2, 34),
('SPCT0365', 100, 3, 2, 2, 34),
('SPCT0366', 100, 4, 2, 2, 34),
('SPCT0367', 100, 1, 8, 2, 34),
('SPCT0368', 100, 2, 8, 2, 34),
('SPCT0369', 100, 3, 8, 2, 34),
('SPCT0370', 100, 4, 8, 2, 34),
('SPCT0371', 100, 1, 10, 2, 35),
('SPCT0372', 100, 2, 10, 2, 35),
('SPCT0373', 100, 3, 10, 2, 35),
('SPCT0374', 100, 4, 10, 2, 35),
('SPCT0375', 100, 1, 1, 2, 35),
('SPCT0376', 100, 2, 1, 2, 35),
('SPCT0377', 100, 3, 1, 2, 35),
('SPCT0378', 100, 4, 1, 2, 35),
('SPCT0379', 100, 1, 11, 2, 36),
('SPCT0380', 100, 2, 11, 2, 36),
('SPCT0381', 100, 3, 11, 2, 36),
('SPCT0382', 100, 4, 11, 2, 36),
('SPCT0383', 100, 1, 10, 2, 36),
('SPCT0384', 100, 2, 10, 2, 36),
('SPCT0385', 100, 3, 10, 2, 36),
('SPCT0386', 100, 4, 10, 2, 36),
('SPCT0387', 100, 1, 1, 2, 36),
('SPCT0388', 100, 2, 1, 2, 36),
('SPCT0389', 100, 3, 1, 2, 36),
('SPCT0390', 100, 4, 1, 2, 36),
('SPCT0391', 100, 1, 8, 2, 37),
('SPCT0392', 100, 2, 8, 2, 37),
('SPCT0393', 100, 3, 8, 2, 37),
('SPCT0394', 100, 4, 8, 2, 37),
('SPCT0395', 100, 1, 14, 2, 37),
('SPCT0396', 100, 2, 14, 2, 37),
('SPCT0397', 100, 3, 14, 2, 37),
('SPCT0398', 100, 4, 14, 2, 37),
('SPCT0399', 100, 1, 1, 2, 37),
('SPCT0400', 100, 2, 1, 2, 37),
('SPCT0401', 100, 3, 1, 2, 37),
('SPCT0402', 100, 4, 1, 2, 37),


('SPCT0405', 100, 1, 7, 2, 38),
('SPCT0406', 100, 2, 7, 2, 38),
('SPCT0407', 100, 3, 7, 2, 38),
('SPCT0408', 100, 4, 7, 2, 38),
('SPCT0409', 100, 1, 1, 2, 38),
('SPCT0410', 100, 2, 1, 2, 38),
('SPCT0411', 100, 3, 1, 2, 38),
('SPCT0412', 100, 4, 1, 2, 38),
('SPCT0413', 100, 1, 10, 2, 38),
('SPCT0414', 100, 2, 10, 2, 38),
('SPCT0415', 100, 3, 10, 2, 38),
('SPCT0416', 100, 4, 10, 2, 38),
('SPCT0417', 100, 1, 1, 2, 39),
('SPCT0418', 100, 2, 1, 2, 39),
('SPCT0419', 100, 3, 1, 2, 39),
('SPCT0420', 100, 4, 1, 2, 39),
('SPCT0421', 100, 1, 12, 2, 39),
('SPCT0422', 100, 2, 12, 2, 39),
('SPCT0423', 100, 3, 12, 2, 39),
('SPCT0424', 100, 4, 12, 2, 39),
('SPCT0425', 100, 1, 3, 2, 40),
('SPCT0426', 100, 2, 3, 2, 40),
('SPCT0427', 100, 3, 3, 2, 40),
('SPCT0428', 100, 4, 3, 2, 40),
('SPCT0429', 100, 1, 1, 2, 40),
('SPCT0430', 100, 2, 1, 2, 40),
('SPCT0431', 100, 3, 1, 2, 40),
('SPCT0432', 100, 4, 1, 2, 40),
('SPCT0433', 100, 1, 8, 2, 40),
('SPCT0434', 100, 2, 8, 2, 40),
('SPCT0435', 100, 3, 8, 2, 40),
('SPCT0436', 100, 4, 8, 2, 40),
('SPCT0437', 100, 1, 2, 2, 41),
('SPCT0438', 100, 2, 2, 2, 41),
('SPCT0439', 100, 3, 2, 2, 41),
('SPCT0440', 100, 4, 2, 2, 41),
('SPCT0441', 100, 1, 1, 2, 41),
('SPCT0442', 100, 2, 1, 2, 41),
('SPCT0443', 100, 3, 1, 2, 41),
('SPCT0444', 100, 4, 1, 2, 41),
('SPCT0445', 100, 1, 12, 2, 41),
('SPCT0446', 100, 2, 12, 2, 41),
('SPCT0447', 100, 3, 12, 2, 41),
('SPCT0448', 100, 4, 12, 2, 41),
('SPCT0449', 100, 1, 1, 2, 42),
('SPCT0450', 100, 2, 1, 2, 42),
('SPCT0451', 100, 3, 1, 2, 42),
('SPCT0452', 100, 4, 1, 2, 42),
('SPCT0453', 100, 1, 12, 2, 42),
('SPCT0454', 100, 2, 12, 2, 42),
('SPCT0455', 100, 3, 12, 2, 42),
('SPCT0456', 100, 4, 12, 2, 42),

('SPCT0457', 100, 1, 5, 2, 43),
('SPCT0458', 100, 1, 5, 2, 43),
('SPCT0459', 100, 1, 5, 2, 43),
('SPCT0460', 100, 1, 5, 2, 43),
('SPCT0461', 100, 1, 1, 2, 43),
('SPCT0462', 100, 2, 1, 2, 43),
('SPCT0463', 100, 3, 1, 2, 43),
('SPCT0464', 100, 4, 1, 2, 43),
('SPCT0465', 100, 1, 2, 2, 43),
('SPCT0466', 100, 2, 2, 2, 43),
('SPCT0467', 100, 3, 2, 2, 43),
('SPCT0468', 100, 4, 2, 2, 43),

('SPCT0469', 100, 1, 1, 2, 44),
('SPCT0470', 100, 2, 1, 2, 44),
('SPCT0471', 100, 3, 1, 2, 44),
('SPCT0472', 100, 4, 1, 2, 44),
('SPCT0473', 100, 1, 12, 2, 44),
('SPCT0474', 100, 2, 12, 2, 44),
('SPCT0475', 100, 3, 12, 2, 44),
('SPCT0476', 100, 4, 12, 2, 44),
('SPCT0477', 100, 1, 2, 2, 44),
('SPCT0478', 100, 2, 2, 2, 44),
('SPCT0479', 100, 3, 2, 2, 44),
('SPCT0480', 100, 4, 2, 2, 44),
('SPCT0481', 100, 1, 9, 2, 45),
('SPCT0482', 100, 2, 9, 2, 45),
('SPCT0483', 100, 3, 9, 2, 45),
('SPCT0484', 100, 4, 9, 2, 45),
('SPCT0485', 100, 1, 7, 2, 45),
('SPCT0486', 100, 2, 7, 2, 45),
('SPCT0487', 100, 3, 7, 2, 45),
('SPCT0488', 100, 4, 7, 2, 45),

('SPCT0489', 100, 1, 7, 2, 46),
('SPCT0490', 100, 2, 7, 2, 46),
('SPCT0491', 100, 3, 7, 2, 46),
('SPCT0492', 100, 4, 7, 2, 46),
('SPCT0493', 100, 1, 11, 2, 46),
('SPCT0494', 100, 2, 11, 2, 46),
('SPCT0495', 100, 3, 11, 2, 46),
('SPCT0496', 100, 4, 11, 2, 46),
('SPCT0497', 100, 1, 10, 2, 46),
('SPCT0498', 100, 2, 10, 2, 46),
('SPCT0499', 100, 3, 10, 2, 46),
('SPCT0500', 100, 4, 10, 2, 46),

('SPCT0501', 100, 1, 9, 2, 47),
('SPCT0502', 100, 2, 9, 2, 47),
('SPCT0503', 100, 3, 9, 2, 47),
('SPCT0504', 100, 4, 9, 2, 47),
('SPCT0505', 100, 1, 1, 2, 47),
('SPCT0506', 100, 2, 1, 2, 47),
('SPCT0507', 100, 3, 1, 2, 47),
('SPCT0508', 100, 4, 1, 2, 47),
('SPCT0509', 100, 1, 2, 3, 48),
('SPCT0510', 100, 2, 2, 3, 48),
('SPCT0511', 100, 3, 2, 3, 48),
('SPCT0512', 100, 4, 2, 3, 48),
('SPCT0513', 100, 1, 1, 3, 48),
('SPCT0514', 100, 2, 1, 3, 48),
('SPCT0515', 100, 3, 1, 3, 48),
('SPCT0516', 100, 4, 1, 3, 48),
('SPCT0517', 100, 1, 8, 3, 48),
('SPCT0518', 100, 2, 8, 3, 48),
('SPCT0519', 100, 3, 8, 3, 48),
('SPCT0520', 100, 4, 8, 3, 48),
('SPCT0521', 100, 1, 5, 3, 49),
('SPCT0522', 100, 2, 5, 3, 49),
('SPCT0523', 100, 3, 5, 3, 49),
('SPCT0524', 100, 4, 5, 3, 49),
('SPCT0525', 100, 1, 2, 3, 49),
('SPCT0526', 100, 2, 2, 3, 49),
('SPCT0527', 100, 3, 2, 3, 49),
('SPCT0528', 100, 4, 2, 3, 49),
('SPCT0529', 100, 1, 3, 2, 50),
('SPCT0530', 100, 2, 3, 2, 50),
('SPCT0531', 100, 3, 3, 2, 50),
('SPCT0532', 100, 4, 3, 2, 50),
('SPCT0533', 100, 1, 10, 2, 50),
('SPCT0534', 100, 2, 10, 2, 50),
('SPCT0535', 100, 3, 10, 2, 50),
('SPCT0536', 100, 4, 10, 2, 50),
('SPCT0537', 100, 1, 8, 2, 50),
('SPCT0538', 100, 2, 8, 2, 50),
('SPCT0539', 100, 3, 8, 2, 50),
('SPCT0540', 100, 4, 8, 2, 50),
('SPCT0541', 100, 1, 8, 2, 51),
('SPCT0542', 100, 2, 8, 2, 51),
('SPCT0543', 100, 3, 8, 2, 51),
('SPCT0544', 100, 4, 8, 2, 51),
('SPCT0545', 100, 1, 9, 2, 51),
('SPCT0546', 100, 2, 9, 2, 51),
('SPCT0547', 100, 3, 9, 2, 51),
('SPCT0548', 100, 4, 9, 2, 51),
('SPCT0549', 100, 1, 1, 2, 51),
('SPCT0550', 100, 2, 1, 2, 51),
('SPCT0551', 100, 3, 1, 2, 51),
('SPCT0552', 100, 4, 1, 2, 51),
('SPCT0553', 100, 1, 1, 2, 52),
('SPCT0554', 100, 2, 1, 2, 52),
('SPCT0555', 100, 3, 1, 2, 52),
('SPCT0556', 100, 4, 1, 2, 52),
('SPCT0557', 100, 1, 1, 2, 52),
('SPCT0558', 100, 2, 1, 2, 52),
('SPCT0559', 100, 3, 1, 2, 52),
('SPCT0560', 100, 4, 1, 2, 52),
('SPCT0561', 100, 1, 12, 2, 53),
('SPCT0562', 100, 2, 12, 2, 53),
('SPCT0563', 100, 3, 12, 2, 53),
('SPCT0564', 100, 4, 12, 2, 53),
('SPCT0565', 100, 1, 9, 2, 53),
('SPCT0566', 100, 2, 9, 2, 53),
('SPCT0567', 100, 3, 9, 2, 53),
('SPCT0568', 100, 4, 9, 2, 53),
('SPCT0569', 100, 1, 1, 1, 53),
('SPCT0570', 100, 2, 1, 1, 53),
('SPCT0571', 100, 3, 1, 1, 53),
('SPCT0572', 100, 4, 1, 1, 53),
('SPCT0573', 100, 1, 1, 1, 54),
('SPCT0574', 100, 2, 1, 1, 54),
('SPCT0575', 100, 3, 1, 1, 54),
('SPCT0576', 100, 4, 1, 1, 54),
('SPCT0577', 100, 1, 2, 1, 54),
('SPCT0578', 100, 2, 2, 1, 54),
('SPCT0579', 100, 3, 2, 1, 54),
('SPCT0580', 100, 4, 2, 1, 54),
('SPCT0581', 100, 1, 3, 1, 54),
('SPCT0582', 100, 2, 3, 1, 54),
('SPCT0583', 100, 3, 3, 1, 54),
('SPCT0584', 100, 4, 3, 1, 54);

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
/*
INSERT INTO hoa_don_chi_tiet (id_san_pham_chi_tiet, id_hoa_don, so_luong, tong_tien,so_tien_thanh_toan,tien_tra_lai) VALUES 
(1, 1, 2, 1000.00, 1000.00,0),
(2, 1, 1, 500,600,100),
(3, 2, 1, 750, 750,0),
(4, 3, 5, 1500, 1500,0),
(5, 4, 3, 3600,4000,400),
(27, 6, 5, 1500, 1500,0),
(25, 7, 3, 3600,4000,400),
(53, 8, 5, 1500, 1500,0),
(54, 9, 3, 3600,4000,400);
go
*/
select * from vai_tro
select * from nguoi_dung
select * from voucher_nguoi_dung
select * from nguoi_dung
select * from voucher
select * from nguoi_dung
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
select * from tinh
select * from huyen
select * from xa
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
select * from gio_hang_chi_tiet
select * from gio_hang
select * from hinh_anh_san_pham
select * from dot_giam_gia
select * from giam_gia_san_pham
select * from trang_thai_giam_gia



