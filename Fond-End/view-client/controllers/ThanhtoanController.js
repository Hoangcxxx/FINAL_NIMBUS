window.ThanhtoanController = function ($scope, $http, $window) {

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
        $http.get('http://localhost:8080/api/auth/user/' + userId)
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
        $http.get(`http://localhost:8080/api/giohang/user/${iduser}/giohang`)
            .then(response => {
                $scope.cart = response.data;
                if ($scope.cart.length === 0) {
                    alert("Giỏ hàng của bạn đang trống!");
                    return;
                }
                $scope.cart.forEach(item => {
                    // Lấy ảnh sản phẩm từ API
                    $http.get("http://localhost:8080/api/hinh_anh/" + item.idSanPham)
                        .then(res => item.urlAnh = res.data[0]?.urlAnh || '')
                        .catch(error => console.error("Lỗi khi lấy ảnh sản phẩm:", error));
                });
                $scope.calculateTotal(); // Tính tổng tiền
            })
            .catch(error => console.error("Lỗi khi lấy sản phẩm trong giỏ hàng:", error));
    };

    // Tính tổng tiền của giỏ hàng, áp dụng mã giảm giá nếu có
    $scope.calculateTotal = function () {
        $scope.totalAmount = $scope.cart.reduce((total, item) => total + item.soLuong * item.giaTien, 0);
        if ($scope.selectedVoucher && $scope.selectedVoucher.discount) {
            $scope.totalAmount *= (1 - $scope.selectedVoucher.discount / 100); // Giảm giá theo voucher
        }
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
            idNguoiDung: $scope.userInfo.id,    
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
            const paymentUrl = `http://localhost:8080/api/payment/creat_payment?amount=${$scope.totalAmount}&paymentMethod=vnpay`;

            $http.post(paymentUrl)
                .then(function (response) {
                    const paymentUrlMatch = response.data.match(/window\.location\.href='([^']+)'/);

                    if (paymentUrlMatch && paymentUrlMatch[1]) {
                        const paymentRedirectUrl = paymentUrlMatch[1];

                        $http.post("http://localhost:8080/api/hoa-don/them_thong_tin_nhan_hang", orderData)
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
        $http.post("http://localhost:8080/api/hoa-don/them_thong_tin_nhan_hang", orderData)
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
                $http.post(`http://localhost:8080/api/email/send?recipientEmail=${$scope.userInfo.email}`, orderData)
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
        $http.get("http://localhost:8080/api/dia-chi/all")
            .then(response => $scope.provinces = response.data)
            .catch(error => console.error("Lỗi khi lấy tỉnh thành:", error));
    };

    // Lấy danh sách quận huyện khi chọn tỉnh thành
    $scope.$watch("shippingInfo.province", function (newProvince) {
        if (newProvince) {
            $http.get(`http://localhost:8080/api/dia-chi/tinh/${newProvince}`)
                .then(response => $scope.districts = response.data)
                .catch(error => console.error("Lỗi khi lấy quận huyện:", error));
        }
    });

    // Lấy danh sách phường xã khi chọn quận huyện
    $scope.$watch("shippingInfo.district", function (newDistrict) {
        if (newDistrict) {
            $http.get(`http://localhost:8080/api/dia-chi/huyen/${newDistrict}`)
                .then(response => $scope.wards = response.data)
                .catch(error => console.error("Lỗi khi lấy phường xã:", error));
        }
    });
    // $scope.getShippingFee = function () {
    //     const { province, district, ward } = $scope.shippingInfo;
    //     if (province && district && ward) {
    //         $http.get(`http://localhost:8080/api/dia-chi/shipping-fee/${province}/${district}/${ward}`)
    //             .then(response => {
    //                 if (response.data && !isNaN(response.data)) {
    //                     $scope.shippingFee = response.data;
    //                     $scope.calculateTotal();
    //                 } else {
    //                     $scope.shippingFee = 0;
    //                     alert("Không thể lấy được phí vận chuyển.");
    //                 }
    //             })
    //             .catch(error => {
    //                 console.error("Lỗi khi lấy phí ship:", error);
    //                 alert("Có lỗi xảy ra khi tính phí vận chuyển.");
    //             });
    //     } else {
    //         $scope.shippingFee = 0;
    //     }
    // };

    $scope.totalonline = function () {
        return $scope.totalAmount + ($scope.shippingFee || 0);
    };

    // $scope.getShippingFee = function () {
    //     const { province, district, ward } = $scope.shippingInfo;
    //     if (province && district && ward) {
    //         $http.get(`http://localhost:8080/api/dia-chi/shipping-fee/${province}/${district}/${ward}`)
    //             .then(response => {
    //                 if (response.data && !isNaN(response.data)) {
    //                     $scope.shippingFee = response.data;
    //                     $scope.calculateTotal();
    //                 } else {
    //                     // Nếu không có phí vận chuyển hoặc dữ liệu không hợp lệ
    //                     $scope.shippingFee = 0;
    //                     alert("Không thể lấy được phí vận chuyển.");
    //                 }
    //             })
    //             .catch(error => {
    //                 console.error("Lỗi khi lấy phí ship:", error);
    //                 alert("Có lỗi xảy ra khi tính phí vận chuyển.");
    //             });
    //     } else {
    //         $scope.shippingFee = 0; // Đảm bảo rằng khi chưa có tỉnh, huyện, xã thì phí vận chuyển là 0
    //     }
    // };
    // $scope.getPhuongThucThanhToan = function () {
    //     $http.get('http://localhost:8080/api/phuong-thuc-thanh-toan/ten-phuong-thuc')
    //         .then(function (response) {
    //             console.log('Dữ liệu phương thức thanh toán:', response.data);
    //             $scope.phuongThucThanhToan = response.data;
    //         })
    //         .catch(function (error) {
    //             console.error('Lỗi khi lấy dữ liệu phương thức thanh toán:', error);
    //         });
    // };

    // $scope.selectPhuongThucThanhToan = function (id) {
    //     $scope.selectedPhuongThucThanhToan = id;

    //     // Tìm tên phương thức thanh toán từ dữ liệu đã lấy và gán vào biến phuongThuc
    //     var selectedMethod = $scope.phuongThucThanhToan.find(function (pt) {
    //         return pt.id === id;
    //     });

    //     $scope.phuongThuc = selectedMethod ? selectedMethod.tenPhuongThuc : '';
    //     console.log('Phương thức thanh toán đã chọn:', $scope.phuongThuc);
    // };
    // $scope.TEST = function () {
    //     if ($scope.selectedPhuongThucThanhToan === 1) {
    //         console.log('Phương thức thanh toán là ID 1, không chạy VNPAY');
    //         return;
    //     }
    //     if ($scope.selectedPhuongThucThanhToan === 2) {
    //         const paymentMethod = 'VNPAY';
    //         const amount = $scope.totalPrice;

    //         const url = `http://localhost:8080/api/payment/creat_payment?amount=${amount}&paymentMethod=${paymentMethod}`;
    //         $http.post(url)
    //             .then(function (response) {
    //                 console.log('Dữ liệu trả về từ VNPAY:', response.data);

    //                 const paymentUrlMatch = response.data.match(/window\.location\.href='([^']+)'/);

    //                 if (paymentUrlMatch && paymentUrlMatch[1]) {
    //                     const paymentUrl = paymentUrlMatch[1];
    //                     console.log('Điều hướng đến URL thanh toán:', paymentUrl);
    //                     window.location.href = paymentUrl;
    //                 } else {
    //                     alert('Không thể tạo thanh toán. Vui lòng thử lại.');
    //                 }
    //             })

    //     }
    // };
    // $scope.selectPhuongThucThanhToan = function () {
    //     // If VNPAY is selected
    //     if ($scope.selectedPaymentMethod === 'vnpay') {
    //         const amount = $scope.totalAmount; // Total amount to pay
    //         const paymentMethod = 'vnpay'; // Payment method (VNPAY)

    //         // Make a POST request to create payment
    //         const paymentUrl = `http://localhost:8080/api/payment/creat_payment?amount=${amount}&paymentMethod=${paymentMethod}`;

    //         $http.post(paymentUrl)
    //             .then(function (response) {
    //                 const paymentUrlMatch = response.data.match(/window\.location\.href='([^']+)'/);
    //                 if (paymentUrlMatch && paymentUrlMatch[1]) {
    //                     const paymentUrl = paymentUrlMatch[1];
    //                     console.log('Redirecting to payment URL:', paymentUrl);
    //                     window.location.href = paymentUrl; // Redirect to payment gateway
    //                 } else {
    //                     alert('Unable to create payment. Please try again.');
    //                 }
    //             })
    //             .catch(function (error) {
    //                 console.error('Error creating payment:', error);
    //                 alert('Error creating payment. Please try again.');
    //             });
    //     }
    // };



    // Khởi tạo thông tin ban đầu

    $scope.getProvinces(); if (user) $scope.getUserInfo(iduser); $scope.getCartItems();





};
