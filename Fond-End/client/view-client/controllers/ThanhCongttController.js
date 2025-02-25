window.ThanhCongttController = function ($scope, $http, $window) {
    // Lấy mã hóa đơn từ URL hoặc localStorage nếu có
    var maHoaDon = new URLSearchParams(window.location.search).get("maHoaDon") || localStorage.getItem("maHoaDon");

    // Nếu không có mã hóa đơn thì thông báo lỗi
    if (!maHoaDon) {
        // Nếu không có mã hóa đơn, hiển thị thông báo lỗi thay vì thông báo thành công
        Swal.fire({
            icon: 'error',
            title: 'Lỗi',
            text: 'Không tìm thấy mã hóa đơn. Vui lòng thử lại!',
            confirmButtonText: 'OK'
        });
        return;
    }
    

    // Nếu có mã hóa đơn, hiển thị thông báo thanh toán thành công
    Swal.fire({
        icon: 'success',
        title: 'Thanh toán thành công!',
        text: 'Cảm ơn bạn đã tin tưởng và đặt hàng. Mã hóa đơn của bạn là: ' + maHoaDon + '. Chúng tôi sẽ nhanh chóng xử lý đơn hàng của bạn!',
        confirmButtonText: 'OK'
    });

    // Lấy chi tiết hóa đơn từ API
    function getOrderDetails(maHoaDon) {
        const apiUrl = `http://localhost:8080/api/nguoi_dung/hoa_don/${maHoaDon}`;

        $http.get(apiUrl)
            .then(function (response) {
                const hoaDon = response.data.hoaDon && Array.isArray(response.data.hoaDon) && response.data.hoaDon.length > 0
                    ? response.data.hoaDon[0]
                    : null;

                if (hoaDon) {
                    // Cập nhật thông tin đơn hàng vào scope
                    $scope.orderData = {
                        maHoaDon: hoaDon.maHoaDon,
                        tenPhuongThucThanhToan: hoaDon.tenPhuongThucThanhToan,
                        phiShip: hoaDon.phiShip,
                        tenNguoiNhan: hoaDon.tenNguoiNhan,
                        diaChi: hoaDon.diaChi,
                        sdtNguoiNhan: hoaDon.sdtNguoiNhan,
                        ghiChu: hoaDon.ghiChu,
                        thanhTien: hoaDon.thanhTien,
                        tinh: hoaDon.tenTinh,
                        huyen: hoaDon.tenHuyen,
                        xa: hoaDon.tenXa,
                        giaTriMavoucher: hoaDon.giaTriMavoucher,
                        kieuGiamGia: hoaDon.kieuGiamGia,
                        idlichsuhoadon: hoaDon.idlichsuhoadon || []
                    };

                    console.log("Giá trị mã voucher:", $scope.orderData.giaTriMavoucher);
                    console.log("Kiểu Giảm Giá:", $scope.orderData.kieuGiamGia);

                    // Cập nhật chi tiết sản phẩm
                    if (hoaDon.listSanPhamChiTiet && hoaDon.listSanPhamChiTiet.length > 0) {
                        $scope.orderDetails = {
                            listSanPhamChiTiet: hoaDon.listSanPhamChiTiet,
                            thanhTien: hoaDon.thanhTien,
                            giaTien: hoaDon.giaTien,
                            maSPCT: hoaDon.maSPCT,
                            tenSanPham: hoaDon.tenSanPham,
                            tenmausac: hoaDon.tenmausac,
                            tenchatlieu: hoaDon.tenchatlieu,
                            tenkichthuoc: hoaDon.tenkichthuoc,
                            tongtien: hoaDon.tongtien,
                            giaKhuyenMai: hoaDon.giaKhuyenMai
                        };

                        // Lấy hình ảnh cho từng sản phẩm
                        $scope.orderDetails.listSanPhamChiTiet.forEach((item) => {
                            $http.get(`http://localhost:8080/api/nguoi_dung/hinh_anh/${item.idSanPham}`)
                                .then(function (imageResponse) {
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

    // Tính tổng giá trị sản phẩm trong đơn hàng
    $scope.getTotalProductPrice = function () {
        let total = 0;

        // Kiểm tra xem $scope.orderDetails và listSanPhamChiTiet có tồn tại không
        if ($scope.orderDetails && Array.isArray($scope.orderDetails.listSanPhamChiTiet)) {
            angular.forEach($scope.orderDetails.listSanPhamChiTiet, function (item) {
                total += item.tongtien || 0; // Thêm giá của mỗi sản phẩm vào tổng
            });
        } else {
            console.log("orderDetails or listSanPhamChiTiet is undefined or not an array.");
        }

        return total;
    };

    // Tính toán giảm giá
    $scope.calculateDiscount = function () {
        let discount = 0;
        if ($scope.orderData) {
            if ($scope.orderData.kieuGiamGia === false) {
                // Giảm giá theo phần trăm
                discount = ($scope.getTotalProductPrice() * $scope.orderData.giaTriMavoucher) / 100;
            } else if ($scope.orderData.kieuGiamGia === true) {
                // Giảm giá trực tiếp bằng số tiền
                discount = $scope.orderData.giaTriMavoucher;
            }
        }
        return discount;
    };

    // Tính tổng giá trị sau khi trừ giảm giá
    $scope.calculateTotalAfterDiscount = function () {
        const totalPrice = $scope.getTotalProductPrice(); // Tổng tiền sản phẩm
        const discount = $scope.calculateDiscount(); // Số tiền giảm giá
        return totalPrice - discount; // Tổng tiền sau khi trừ giảm giá
    };

    // Gọi API để lấy chi tiết đơn hàng
    getOrderDetails(maHoaDon);
};
