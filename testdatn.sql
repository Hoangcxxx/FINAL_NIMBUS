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
  [ten_loai_voucher] NVARCHAR(100) NOT NULL, -- Tên loại voucher (ví dụ: Giảm giá phần trăm, Giảm giá theo số tiền)
  [mo_ta] NVARCHAR(MAX), -- Mô tả chi tiết
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE()
);
go
CREATE TABLE [voucher] (
  [Id_voucher] INT PRIMARY KEY IDENTITY(1,1),
  [ma_voucher] NVARCHAR(50) NOT NULL UNIQUE,
  [gia_tri_giam_gia] DECIMAL(18), -- Số tiền giảm giá cho sản phẩm hoặc hóa đơn
  [so_luong] INT, -- Số lượng voucher có sẵn
  [gia_toi_thieu] DECIMAL(18), -- Giá tối thiểu để áp dụng voucher cho hóa đơn
  [trang_thai] BIT DEFAULT 1, -- Trạng thái của voucher (còn sử dụng hay không)
  [mo_ta] NVARCHAR(MAX), -- Mô tả chi tiết
  [ngay_bat_dau] DATETIME, -- Ngày bắt đầu áp dụng voucher
  [ngay_ket_thuc] DATETIME, -- Ngày kết thúc áp dụng voucher
  [ngay_tao] DATETIME DEFAULT GETDATE(), -- Ngày tạo
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(), -- Ngày cập nhật
  [id_loai_voucher] INT, -- Liên kết với bảng loại voucher
  CONSTRAINT [FK_voucher_id_loai_voucher]
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
  [gia_khuyen_mai] DECIMAL(18),
  [ngay_tao] DATETIME DEFAULT GETDATE(),
  [ngay_cap_nhat] DATETIME DEFAULT GETDATE(),
  [mo_ta] NVARCHAR(MAX),
  [Trang_thai] BIT DEFAULT 1,
  [id_danh_muc] INT,
  [id_voucher] INT, -- Thay đổi để liên kết với bảng voucher
  CONSTRAINT [FK_san_pham_id_voucher]
    FOREIGN KEY ([id_voucher])
      REFERENCES [voucher]([Id_voucher]),
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