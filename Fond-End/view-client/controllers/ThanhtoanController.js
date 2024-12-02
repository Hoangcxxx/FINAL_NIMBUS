window.ThanhToanController = function ($scope, $http, $window) {
    $scope.cart = [];
    $scope.totalAmount = 0;
    $scope.userInfo = {};
    $scope.vouchers = [];
    $scope.selectedVoucher = null;
    $scope.paymentStatus = '';
    $scope.selectedPaymentMethod = "cod";
    $scope.selectedVNPay = "vnpay";
    $scope.shippingInfo = {};

    var user = JSON.parse(localStorage.getItem("user"));
    console.log("User data:", user);
    if (user) {
        var iduser = user.idNguoiDung;
    }

    // Lấy thông tin người dùng
    $scope.getUserInfo = function (userId) {
        $http.get('http://localhost:8080/api/nguoi_dung/' + userId)
            .then(response => {
                $scope.userInfo = response.data;
                if (!Object.keys($scope.shippingInfo).length) {
                    $scope.shippingInfo = {};
                }
                Object.assign($scope.shippingInfo, {
                    email: $scope.userInfo.email,
                    name: $scope.userInfo.name,
                    phone: $scope.userInfo.phone,
                    address: $scope.userInfo.address
                });
            })
            .catch(error => console.error("Error fetching user info:", error));
    };

    // Lấy giỏ hàng
    $scope.getCartItems = function () {
        $http.get(`http://localhost:8080/api/nguoi_dung/gio_hang/${iduser}`)
            .then(response => {
                $scope.cart = response.data;
                $scope.cart.forEach(item => {
                    // Lấy ảnh sản phẩm
                    $http.get("http://localhost:8080/api/nguoi_dung/hinh_anh/" + item.idSanPham)
                        .then(res => item.urlAnh = res.data[0]?.urlAnh || '')
                        .catch(error => console.error("Error fetching product images:", error));
                });
                $scope.calculateTotal(); // Tính tổng tiền
            })
            .catch(error => console.error("Error fetching cart items:", error));
    };

    // Tính tổng tiền giỏ hàng
    $scope.calculateTotal = function () {
        $scope.totalAmount = $scope.cart.reduce((total, item) => total + item.soLuong * item.giaTien, 0);
        if ($scope.selectedVoucher && $scope.selectedVoucher.discount) {
            $scope.totalAmount *= (1 - $scope.selectedVoucher.discount / 100); // Áp dụng giảm giá từ voucher
        }
    };

    // Đặt hàng
    $scope.placeOrder = function () {
        // Kiểm tra thông tin người dùng
        if (!$scope.userInfo || !$scope.userInfo.tenNguoiDung) {
            alert("Vui lòng nhập tên người dùng!");
            return;
        }

        // Kiểm tra thông tin địa chỉ (tỉnh, huyện, xã)
        if (!$scope.shippingInfo || !$scope.shippingInfo.province || !$scope.shippingInfo.district || !$scope.shippingInfo.ward) {
            alert("Vui lòng chọn đầy đủ thông tin nhận hàng!");
            return;
        }

        // Tạo đối tượng orderData
        const orderData = {
            cartId: iduser,
            idNguoiDung: $scope.userInfo.idNguoiDung,
            tinh: $scope.shippingInfo.province,
            huyen: $scope.shippingInfo.district,
            xa: $scope.shippingInfo.ward,
            email: $scope.userInfo.email,
            tenNguoiNhan: $scope.userInfo.tenNguoiDung,
            diaChi: $scope.userInfo.diaChi,
            sdtNguoiNhan: $scope.userInfo.sdt,
            ghiChu: $scope.shippingInfo.note,
            tenPhuongThucThanhToan: $scope.selectedPaymentMethod,
            listSanPhamChiTiet: $scope.cart,
            thanhTien: $scope.totalAmount,
            idVoucher: $scope.selectedVoucher ? $scope.selectedVoucher.id : null,
        };

        // Xử lý thanh toán với VNPAY
        if ($scope.selectedPaymentMethod === "vnpay") {
            const paymentUrl = `http://localhost:8080/api/nguoi_dung/payment/creat_payment?amount=${$scope.totalAmount}&paymentMethod=vnpay`;
            $http.post(paymentUrl)
                .then(function (response) {
                    const paymentUrlMatch = response.data.match(/window\.location\.href='([^']+)'/);
                    if (paymentUrlMatch && paymentUrlMatch[1]) {
                        const paymentRedirectUrl = paymentUrlMatch[1];
                        // Gửi dữ liệu đơn hàng vào API
                        $http.post("http://localhost:8080/api/nguoi_dung/hoa_don/them_thong_tin_nhan_hang", orderData)
                            .then(response => {
                                const maHoaDon = response.data.maHoaDon;
                                localStorage.setItem("maHoaDon", maHoaDon);
                                window.location.href = paymentRedirectUrl;
                            })
                            .catch(error => {
                                console.error("Order placement error:", error);
                                alert("Có lỗi xảy ra khi đặt hàng. Vui lòng thử lại.");
                            });
                    } else {
                        alert('Không thể tạo thanh toán VNPAY. Vui lòng thử lại.');
                    }
                })
                .catch(function (error) {
                    console.error('Error creating VNPAY payment:', error);
                    alert('Có lỗi xảy ra khi tạo thanh toán VNPAY. Vui lòng thử lại.');
                });
            return;
        }

        // Đặt hàng bình thường (không sử dụng VNPAY)
        $http.post("http://localhost:8080/api/nguoi_dung/hoa_don/them_thong_tin_nhan_hang", orderData)
            .then(response => {
                console.log("Order placed successfully!");
                localStorage.setItem("maHoaDon", response.data.maHoaDon);
                $scope.cart = [];
                $window.location.href = "/#!thanhcong?maHoaDon=" + response.data.maHoaDon;
                // Gửi email xác nhận sau khi đặt hàng thành công
                $http.post(`http://localhost:8080/api/nguoi_dung/email/send?recipientEmail=${$scope.userInfo.email}`, orderData)
                    .then(response => console.log("Email đã được gửi thành công"))
                    .catch(error => console.error("Lỗi khi gửi email:", error));
            })
            .catch(error => {
                console.error("Order placement error:", error);
                alert("Có lỗi xảy ra khi đặt hàng. Vui lòng thử lại.");
            });
    };

    // Hàm gọi API lấy danh sách tỉnh thành
    $scope.getProvinces = function () {
        $http.get("http://127.0.0.1:8080/api/nguoi_dung/test/cities")
            .then(response => {
                $scope.dsTinh = response.data; // Gán dữ liệu tỉnh thành vào danh sách dsTinh
            })
            .catch(error => {
                console.error("Có lỗi xảy ra khi lấy tỉnh thành:", error);
                alert("Không thể lấy thông tin tỉnh thành. Vui lòng thử lại.");
            });
    };

    // Hàm gọi API lấy danh sách huyện theo mã tỉnh
    $scope.getDistricts = function (cityCode) {
        if (!cityCode) {
            alert("Vui lòng chọn tỉnh trước!");
            return;
        } // Không gọi API nếu mã tỉnh không hợp lệ

        $http.get(`http://127.0.0.1:8080/api/nguoi_dung/test/districts/${cityCode}`)
            .then(response => {
                $scope.dsHuyen = response.data; // Gán dữ liệu huyện vào danh sách dsHuyen
                $scope.shippingInfo.district = null; // Reset lựa chọn huyện
                $scope.dsXa = []; // Xóa dữ liệu xã khi tỉnh thay đổi
                $scope.shippingInfo.ward = null; // Reset lựa chọn xã
            })
            .catch(error => {
                console.error("Có lỗi xảy ra khi lấy huyện:", error);
                alert("Không thể lấy thông tin huyện. Vui lòng thử lại.");
            });
    };

    // Hàm gọi API lấy danh sách xã theo mã huyện
    $scope.getWards = function (districtCode) {
        if (!districtCode) {
            alert("Vui lòng chọn huyện trước!");
            return;
        } // Không gọi API nếu mã huyện không hợp lệ

        $http.get(`http://127.0.0.1:8080/api/nguoi_dung/test/wards/${districtCode}`)
            .then(response => {
                $scope.dsXa = response.data; // Gán dữ liệu xã vào danh sách dsXa
                $scope.shippingInfo.ward = null; // Reset lựa chọn xã
            })
            .catch(error => {
                console.error("Có lỗi xảy ra khi lấy xã:", error);
                alert("Không thể lấy thông tin xã. Vui lòng thử lại.");
            });
    };


    // Khởi tạo thông tin ban đầu
    $scope.getProvinces();
    if (user) $scope.getUserInfo(iduser);
    $scope.getCartItems();

};
