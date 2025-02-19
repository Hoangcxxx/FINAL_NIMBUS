window.ThanhCongttController = function ($scope, $http, $window) {
    // Lấy mã hóa đơn từ URL hoặc localStorage nếu có
    var maHoaDon = new URLSearchParams(window.location.search).get("maHoaDon") || localStorage.getItem("maHoaDon");

    if (!maHoaDon) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi',
            text: 'Không tìm thấy mã hóa đơn. Vui lòng thử lại!',
            confirmButtonText: 'OK'
        });
        return;
    }
    
    // Hiển thị thông báo thành công khi có mã hóa đơn
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
                        giaTriMavoucher: hoaDon.giaTriMavoucher, // Giá trị giảm (là % hoặc số tiền)
                        kieuGiamGia: hoaDon.kieuGiamGia, // false: giảm theo %, true: giảm theo tiền
                        idlichsuhoadon: hoaDon.idlichsuhoadon || []
                    };

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

    // Tính tổng giá trị sản phẩm (Tạm tính)
    $scope.getTotalProductPrice = function () {
        let total = 0;
        if ($scope.orderDetails && Array.isArray($scope.orderDetails.listSanPhamChiTiet)) {
            angular.forEach($scope.orderDetails.listSanPhamChiTiet, function (item) {
                total += item.tongtien || 0;
            });
        }
        return total;
    };

    // Tính số tiền giảm giá (Discount)
    $scope.calculateDiscount = function () {
        let total = $scope.getTotalProductPrice();
        let discount = 0;
        if ($scope.orderData) {
            if ($scope.orderData.kieuGiamGia === false) {
                // Giảm theo phần trăm
                discount = (total * $scope.orderData.giaTriMavoucher) / 100;
            } else if ($scope.orderData.kieuGiamGia === true) {
                // Giảm trực tiếp theo số tiền
                discount = $scope.orderData.giaTriMavoucher;
            }
        }
        // Giới hạn giảm giá không vượt quá tổng tiền sản phẩm
        return Math.min(discount, total);
    };

    // Tính tổng tiền sau giảm giá (chưa cộng phí ship)
    $scope.calculateTotalAfterDiscount = function () {
        const total = $scope.getTotalProductPrice();
        const discount = $scope.calculateDiscount();
        return Math.max(total - discount, 0);
    };

    // Gọi API để lấy chi tiết đơn hàng
    getOrderDetails(maHoaDon);
};
