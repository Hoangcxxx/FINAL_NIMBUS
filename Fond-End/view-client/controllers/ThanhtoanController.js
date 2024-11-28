window.ThanhToanController = function ($scope, $http, $window) {

    $scope.provinces = [];
    $scope.districts = [];
    $scope.wards = [];
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
    if (user) {
        var iduser = user.idNguoiDung;
    }
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
            .catch(error => console.error("Lỗi khi lấy thông tin người dùng:", error));
    };


    $scope.getCartItems = function () {
        $http.get(`http://localhost:8080/api/nguoi_dung/gio_hang/${iduser}`)
            .then(response => {
                $scope.cart = response.data;
                if ($scope.cart.length === 0) {
                    alert("Giỏ hàng của bạn đang trống!");
                    return;
                }
                $scope.cart.forEach(item => {
                    // Lấy ảnh sản phẩm từ API
                    $http.get("http://localhost:8080/api/nguoi_dung/hinh_anh/" + item.idSanPham)
                        .then(res => item.urlAnh = res.data[0]?.urlAnh || '')
                        .catch(error => console.error("Lỗi khi lấy ảnh sản phẩm:", error));
                });
                $scope.calculateTotal(); // Tính tổng tiền
            })
            .catch(error => console.error("Lỗi khi lấy sản phẩm trong giỏ hàng:", error));
    };
    $scope.selectVoucher = function (voucher) {
        // Kiểm tra điều kiện sử dụng voucher
        if (voucher.isUsable) {
            $scope.selectedVoucher = voucher;
            $scope.voucherCode = voucher.maVoucher;  // Gán mã voucher vào input (nếu cần)
            $scope.voucherError = '';  // Reset lỗi

        } else {
            $scope.voucherError = 'Voucher này không thể sử dụng.';
        }
    };
    $scope.dsvoucher = function () {
        // Calculate the total amount from the cart
        $scope.totalAmount = $scope.cart.reduce(function (total, item) {
            // Determine the price based on whether there's a promotional price or not
            let donGia = item.giaKhuyenMai != null ? item.giaKhuyenMai : item.giaBan;
            // Add the total price for the item based on its quantity
            return total + (item.soLuongGioHang * donGia);
        }, 0);

        // Log the total amount for debugging
        console.log("Tổng tiền giỏ hàng: " + $scope.totalAmount);

        // If the totalAmount is 0 or less, don't fetch vouchers
        if ($scope.totalAmount <= 0) {
            $scope.availableVouchers = []; // No valid vouchers if there's no total amount
            return;
        }

        // Fetch available vouchers based on the total amount
        $http.get('http://localhost:8080/api/nguoi_dung/vouchers/' + $scope.totalAmount)
            .then(function (response) {
                // Store the available vouchers in $scope.availableVouchers
                $scope.availableVouchers = response.data || [];
                console.log("Danh sách voucher khả dụng:", $scope.availableVouchers);

                // Filter vouchers based on the total amount conditions
                $scope.availableVouchers = $scope.availableVouchers.filter(function (voucher) {
                    // Only include vouchers where the total amount meets the minimum and maximum limits
                    return $scope.totalAmount >= voucher.soTienToiThieu && $scope.totalAmount <= voucher.giaTriToiDa;
                });

                // If no voucher is selected and valid vouchers exist, select the first one automatically
                if (!$scope.selectedVoucher && $scope.availableVouchers.length > 0) {
                    $scope.selectedVoucher = $scope.availableVouchers[0];
                }
            })
            .catch(function (error) {
                // Handle errors in fetching vouchers
                console.error("Error fetching vouchers:", error);
                $scope.availableVouchers = [];
            });
    };


    $scope.calculateTotal = function () {
        $scope.totalAmount = $scope.cart.reduce((total, item) => {
            // Kiểm tra giá và tính tổng tiền
            let donGia = item.giaKhuyenMai != null ? item.giaKhuyenMai : item.giaBan;
            return total + (item.soLuongGioHang * donGia);
        }, 0);
    };

    // ĐặT Hàng
    $scope.placeOrder = function () {
        if ($scope.cart.length === 0) {
            alert("Giỏ hàng của bạn đang trống!");
            return;
        }

        if (!$scope.userInfo.tenNguoiDung) {
            alert("Vui lòng nhập tên người dùng!");
            return;
        }

        if (!$scope.shippingInfo || !$scope.shippingInfo.province || !$scope.shippingInfo.district || !$scope.shippingInfo.ward) {
            alert("Vui lòng chọn đầy đủ thông tin nhận hàng!");
            return;
        }

        const orderData = {
            cartId: iduser,
            idNguoiDung: $scope.userInfo.idNguoiDung,
            tinh: $scope.shippingInfo.province,
            huyen: $scope.shippingInfo.district,
            xa: $scope.shippingInfo.ward,
            email: $scope.userInfo.email,
            tenNguoiNhan: $scope.userInfo.tenNguoiDung,
            diaChi: $scope.userInfo.diaChi,
            sdtNguoiNhan: $scope.userInfo.sdtNguoiDung,
            ghiChu: $scope.shippingInfo.note,
            tenPhuongThucThanhToan: $scope.selectedPaymentMethod,
            listSanPhamChiTiet: $scope.cart,
            thanhTien: $scope.totalAmount,
            idVoucher: $scope.selectedVoucher ? $scope.selectedVoucher.id : null,
        };

        if ($scope.selectedPaymentMethod === "vnpay") {
            const paymentUrl = `http://localhost:8080/api/nguoi_dung/payment/creat_payment?amount=${$scope.totalAmount}&paymentMethod=vnpay`;

            $http.post(paymentUrl)
                .then(function (response) {
                    const paymentUrlMatch = response.data.match(/window\.location\.href='([^']+)'/);

                    if (paymentUrlMatch && paymentUrlMatch[1]) {
                        const paymentRedirectUrl = paymentUrlMatch[1];
                        console.error("orderData:", orderData);

                        $http.post("http://localhost:8080/api/nguoi_dung/hoa_don/them_thong_tin_nhan_hang", orderData)
                            .then(response => {
                                const maHoaDon = response.data.maHoaDon;

                                // Lưu mã hóa đơn và chuyển hướng tới cổng thanh toán
                                localStorage.setItem("maHoaDon", maHoaDon);
                                window.location.href = paymentRedirectUrl;
                            })
                            .catch(error => {
                                console.error("Lỗi khi đặt hàng:", error);
                                alert("Có lỗi xảy ra khi đặt hàng. Vui lòng thử lại.");
                            });
                    } else {
                        alert('Không thể tạo thanh toán VNPAY. Vui lòng thử lại.');
                    }
                })
                .catch(function (error) {
                    console.error('Lỗi khi tạo thanh toán VNPAY:', error);
                    alert('Có lỗi xảy ra khi tạo thanh toán VNPAY. Vui lòng thử lại.');
                });

            return;
        }

        // Khi API trả về thành công
        $http.post("http://localhost:8080/api/nguoi_dung/hoa_don/them_thong_tin_nhan_hang", orderData)
            .then(response => {
                console.log("Đặt hàng thành công!");

                // Lưu mã hóa đơn vào localStorage
                if (response.data.maHoaDon) {
                    localStorage.setItem("maHoaDon", response.data.maHoaDon);
                }



                // Xóa giỏ hàng sau khi đặt hàng
                $scope.cart = [];

                // Điều hướng đến trang thành công
                $window.location.href = "/#!thanhcong?maHoaDon=" + response.data.maHoaDon;

                // Gửi email xác nhận sau khi đặt hàng thành công
                $http.post(`http://localhost:8080/api/nguoi_dung/email/send?recipientEmail=${$scope.userInfo.email}`, orderData)
                    .then(response => console.log("Email đã được gửi thành công"))
                    .catch(error => console.error("Lỗi khi gửi email:", error));
            })
            .catch(error => {
                console.error("Lỗi khi đặt hàng:", error);
                alert("Có lỗi xảy ra khi đặt hàng. Vui lòng thử lại.");
            });
    };



    // Lấy thông tin tỉnh thành, quận huyện và phường xã
    $scope.getProvinces = function () {
        $http.get("http://localhost:8080/api/nguoi_dung/dia_chi/all")
            .then(response => $scope.provinces = response.data)
            .catch(error => console.error("Lỗi khi lấy tỉnh thành:", error));
    };

    // Lấy danh sách quận huyện khi chọn tỉnh thành
    $scope.$watch("shippingInfo.province", function (newProvince) {
        if (newProvince) {
            $http.get(`http://localhost:8080/api/nguoi_dung/dia_chi/tinh/${newProvince}`)
                .then(response => $scope.districts = response.data)
                .catch(error => console.error("Lỗi khi lấy quận huyện:", error));
        }
    });

    // Lấy danh sách phường xã khi chọn quận huyện
    $scope.$watch("shippingInfo.district", function (newDistrict) {
        if (newDistrict) {
            $http.get(`http://localhost:8080/api/nguoi_dung/dia_chi/huyen/${newDistrict}`)
                .then(response => $scope.wards = response.data)
                .catch(error => console.error("Lỗi khi lấy phường xã:", error));
        }
    });



    // Khởi tạo thông tin ban đầu

    $scope.getProvinces(); if (user) $scope.getUserInfo(iduser); $scope.getCartItems();





};
