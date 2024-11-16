window.ThanhCongController = function ($scope, $http) {
    // Hàm xử lý khi thanh toán thành công
    function handlePaymentSuccess(response) {
        const maHoaDon = response.data.maHoaDon; // Lấy mã hóa đơn từ response
        getOrderDetails(maHoaDon); // Lấy chi tiết đơn hàng
    }

    // Hàm lấy thông tin chi tiết đơn hàng
    function getOrderDetails(maHoaDon) {
        const apiUrl = `http://localhost:8080/api/hoa-don/${maHoaDon}`;
        $http.get(apiUrl)
            .then(function (response) {
                const data = response.data;

                if (data && Array.isArray(data) && data.length > 0) {
                    const hoaDon = data[0];
                    $scope.orderData = {
                        maHoaDon: hoaDon.maHoaDon,
                        tenPhuongThucThanhToan: hoaDon.tenPhuongThucThanhToan,
                        tenNguoiNhan: hoaDon.tenNguoiNhan,
                        diaChi: hoaDon.diaChi,
                        sdtNguoiNhan: hoaDon.sdtNguoiNhan,
                        ghiChu: hoaDon.ghiChu,
                        thanhTien: hoaDon.thanhTien,
                        phiShip: hoaDon.phiShip,
                        tinh: hoaDon.tinh,
                        huyen: hoaDon.huyen,
                        xa: hoaDon.xa
                    };

                    // Cập nhật thông tin chi tiết sản phẩm
                    if (hoaDon.listSanPhamChiTiet && hoaDon.listSanPhamChiTiet.length > 0) {
                        $scope.orderDetails = {
                            listSanPhamChiTiet: hoaDon.listSanPhamChiTiet,
                            thanhTien: hoaDon.thanhTien,
                            phiShip: hoaDon.phiShip
                        };

                        // Lấy hình ảnh cho từng sản phẩm
                        $scope.orderDetails.listSanPhamChiTiet.forEach((item) => {
                            $http.get(`http://localhost:8080/api/hinh_anh/${item.idSanPham}`)
                                .then(function (response) {
                                    item.urlAnh = response.data[0]?.urlAnh || ''; // Lấy url hình ảnh
                                })
                                .catch(function (error) {
                                    console.error("Lỗi khi lấy hình ảnh sản phẩm:", error);
                                });
                        });
                    }
                } else {
                    console.error("Dữ liệu hóa đơn không hợp lệ.");
                }
            })
            .catch(function (error) {
                console.error("Lỗi khi lấy thông tin hóa đơn:", error);
            });
    }

    // Giả sử khi thanh toán thành công, bạn sẽ gọi handlePaymentSuccess như sau:
    // handlePaymentSuccess(response); // response sẽ là dữ liệu trả về từ API thanh toán
};
