window.ThanhCongController = function ($scope, $http) {
    // Lấy mã hóa đơn từ URL hoặc localStorage nếu có
    var maHoaDon = new URLSearchParams(window.location.search).get("maHoaDon") || localStorage.getItem("maHoaDon");

    // Nếu không có mã hóa đơn thì thông báo lỗi
    if (!maHoaDon) {
        alert("Mã hóa đơn không hợp lệ!");
        return;
    }

    function getOrderDetails(maHoaDon) {
        const apiUrl = `http://localhost:8080/api/nguoi_dung/hoa_don/${maHoaDon}`;

        // Gọi API để lấy chi tiết hóa đơn
        $http.get(apiUrl)
            .then(function (response) {
                const hoaDon = response.data.hoaDon && Array.isArray(response.data.hoaDon) && response.data.hoaDon.length > 0
                    ? response.data.hoaDon[0]
                    : null;

                if (hoaDon) {
                    // Cập nhật thông tin đơn hàng
                    $scope.orderData = {
                        maHoaDon: hoaDon.maHoaDon,
                        tenPhuongThucThanhToan: hoaDon.tenPhuongThucThanhToan,
                        tenNguoiNhan: hoaDon.tenNguoiNhan,
                        diaChi: hoaDon.diaChi,
                        sdtNguoiNhan: hoaDon.sdtNguoiNhan,
                        ghiChu: hoaDon.ghiChu,
                        thanhTien: hoaDon.thanhTien,
                        // phiShip: hoaDon.phiShip,
                        tinh: hoaDon.tenTinh,
                        huyen: hoaDon.tenHuyen,
                        xa: hoaDon.tenXa
                    };
                    console.log("214141414214", $scope.orderData);
                    // Cập nhật chi tiết sản phẩm
                    if (hoaDon.listSanPhamChiTiet && hoaDon.listSanPhamChiTiet.length > 0) {
                        $scope.orderDetails = {
                            listSanPhamChiTiet: hoaDon.listSanPhamChiTiet,
                            thanhTien: hoaDon.thanhTien,
                            phiShip: hoaDon.phiShip,
                            maSPCT: hoaDon.maSPCT,
                            tenSanPham: hoaDon.tenSanPham,
                            tenmausac: hoaDon.tenmausac, // Đảm bảo trường này tồn tại
                            tenchatlieu: hoaDon.tenchatlieu, // Đảm bảo trường này tồn tại
                            tenkichthuoc: hoaDon.tenkichthuoc, // Đảm bảo trường này tồn tại
                            giaKhuyenMai: hoaDon.giaKhuyenMai
                        };

                        // Lấy hình ảnh cho từng sản phẩm
                        $scope.orderDetails.listSanPhamChiTiet.forEach((item) => {
                            $http.get(`http://localhost:8080/api/nguoi_dung/hinh_anh/${item.idSanPham}`)
                                .then(function (imageResponse) {
                                    // Gán hình ảnh cho sản phẩm nếu có
item.urlAnh = imageResponse.data[0]?.urlAnh || '';
                                })
                                .catch(function (error) {
                                    console.error("Lỗi khi lấy hình ảnh sản phẩm:", error);
                                });
                        });
                    }
                } else {
                    console.error("Dữ liệu hóa đơn không hợp lệ:", response.data);
                }
            })
            .catch(function (error) {
                console.error("Lỗi khi lấy thông tin hóa đơn:", error);
            });
    }

    // Giả sử bạn có một API hoặc sự kiện thanh toán thành công
    function onPaymentSuccess() {
        // Xóa mã hóa đơn khỏi localStorage
        localStorage.removeItem("maHoaDon");
        console.log("Mã hóa đơn đã được xóa khỏi localStorage.");
        alert("Thanh toán thành công!");
        // Bạn có thể điều hướng người dùng tới trang khác sau khi thanh toán thành công
        window.location.href = "/thank-you"; // Ví dụ chuyển hướng tới trang cảm ơn
    }

    // Gọi API để lấy chi tiết đơn hàng
    getOrderDetails(maHoaDon);

}