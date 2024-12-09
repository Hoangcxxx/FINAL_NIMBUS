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

    $scope.getVoucher = function () {
        if ($scope.voucherCode) {
            // Kiểm tra tổng tiền
            if (!$scope.totalAmount || $scope.totalAmount <= 0) {
                $scope.voucherError = 'Tổng tiền phải lớn hơn 0 để áp dụng voucher!';
                return;
            }

            // Gửi yêu cầu API kiểm tra voucher
            $http.post('http://localhost:8080/api/nguoi_dung/vouchers/apma/' + $scope.voucherCode + '/' + $scope.totalAmount)
                .then(function (response) {
                    $scope.selectedVoucher = response.data; // Nhận voucher đã chọn từ phản hồi API
                    console.log("Voucher đã áp dụng:", $scope.selectedVoucher);

                    // Kiểm tra giá trị tối thiểu và tối đa của voucher
                    if ($scope.totalAmount < $scope.selectedVoucher.soTienToiThieu) {
                        $scope.voucherError = 'Tổng tiền không đủ để áp dụng voucher này. Bạn cần ít nhất ' + $scope.selectedVoucher.soTienToiThieu + ' để áp dụng.';
                        return;
                    }

                    if ($scope.totalAmount > $scope.selectedVoucher.giaTriToiDa) {
                        $scope.voucherError = 'Tổng tiền vượt quá giá trị tối đa của voucher này. Bạn chỉ có thể sử dụng voucher khi tổng tiền từ ' + $scope.selectedVoucher.soTienToiThieu + ' đến ' + $scope.selectedVoucher.giaTriToiDa + '.';
                        return;
                    }

                    // Reset lỗi nếu tất cả điều kiện hợp lệ
                    $scope.voucherError = '';
                })
                .catch(function (error) {
                    console.error('Lỗi khi áp dụng voucher:', error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi khi áp dụng voucher!',
                        text: error.data || 'Mã voucher không hợp lệ hoặc không thể sử dụng.',
                        confirmButtonText: 'Thử lại'
                    });

                    $scope.voucherError = error.data || 'Mã voucher không hợp lệ hoặc không thể sử dụng!';
                });
        }
    };

    $scope.applyVoucher = function () {
        if ($scope.selectedVoucher) {
            // Kiểm tra giá trị tối thiểu và tối đa của voucher
            const minValue = $scope.selectedVoucher.soTienToiThieu;
            const maxValue = $scope.selectedVoucher.giaTriToiDa;

            // In ra giá trị tối thiểu và tối đa
            console.log("Giá trị tối thiểu voucher: ", minValue);
            console.log("Giá trị tối đa voucher: ", maxValue);

            // Kiểm tra nếu tổng tiền nhỏ hơn giá trị tối thiểu của voucher, không áp dụng giảm giá
            if ($scope.totalAmount < minValue) {
                $scope.voucherError = 'Tổng tiền không đủ điều kiện áp dụng voucher.';
                return; // Không thực hiện giảm giá nếu tổng tiền nhỏ hơn giá trị tối thiểu
            }

            if (isNaN($scope.selectedVoucher.giaTriGiamGia) || $scope.selectedVoucher.giaTriGiamGia < 0) {
                console.error('Giá trị giảm giá không hợp lệ:', $scope.selectedVoucher.giaTriGiamGia);
                $scope.selectedVoucher.giaTriGiamGia = 0;
            }

            // Áp dụng giảm giá theo loại voucher
            let discount = 0;
            if ($scope.selectedVoucher.kieuGiamGia === false) {
                // Giảm theo phần trăm
                discount = $scope.totalAmount * ($scope.selectedVoucher.giaTriGiamGia / 100);
            } else if ($scope.selectedVoucher.kieuGiamGia === true) {
                // Giảm theo giá trị cố định
                discount = $scope.selectedVoucher.giaTriGiamGia;
            }

            // Trừ giảm giá vào tổng tiền
            $scope.totalAmount -= discount;
            $scope.totalAmount = Math.max($scope.totalAmount, 0); // Đảm bảo tổng tiền không âm

            console.log("Giảm giá: ", discount);
            console.log("Tổng tiền sau giảm: ", $scope.totalAmount);

            // Hiển thị thông báo thành công
            Swal.fire({
                icon: 'success',
                title: 'Voucher đã được áp dụng!',
                text: 'Mã voucher: ' + $scope.selectedVoucher.maVoucher + ' đã thành công.',
                confirmButtonText: 'OK'
            });

            // Tính lại tổng tiền sau khi áp dụng voucher
            $scope.calculateTotal(); // Tính lại tổng tiền sau khi áp dụng voucher
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
            $scope.Voucher = []; // No valid vouchers if there's no total amount
            return;
        }

        // Fetch available vouchers based on the total amount
        $http.get('http://localhost:8080/api/nguoi_dung/vouchers/' + $scope.totalAmount)
            .then(function (response) {
                // Store the available vouchers in $scope.availableVouchers
                $scope.Voucher = response.data;
                console.log("Danh sách voucher khả dụng:", $scope.Voucher);

                // Filter vouchers based on the total amount conditions
                $scope.availableVouchers = $scope.availableVouchers.filter(function (voucher) {
                    // Only include vouchers where the total amount meets the minimum and maximum limits
                    return $scope.totalAmount >= voucher.soTienToiThieu;
                });
            })
            .catch(function (error) {
                // Handle errors in fetching vouchers
                console.error("Error fetching vouchers:", error);
                $scope.availableVouchers = [];
            });
    };



    $scope.calculateTotal = function () {
        let totalProductPrice = $scope.cart.reduce(function (total, item) {
            let donGia = item.giaKhuyenMai != null ? item.giaKhuyenMai : item.giaBan;
            return total + (item.soLuongGioHang * donGia);
        }, 0);

        let totalDiscountedPrice = $scope.cart.reduce(function (total, item) {
            // Tính tổng tiền sản phẩm theo giá khuyến mãi và số lượng
            let price = item.giaKhuyenMai != null ? item.giaKhuyenMai : item.giaBan;  // Giá khuyến mãi nếu có, nếu không lấy giá bán cơ bản
            return total + (item.soLuongGioHang * price);
        }, 0);

        // Tính tổng tiền sau khi áp dụng voucher
        if ($scope.selectedVoucher) {
            let discount = 0;
            if ($scope.selectedVoucher.kieuGiamGia === false) {
                // Giảm theo phần trăm
                discount = totalDiscountedPrice * ($scope.selectedVoucher.giaTriGiamGia / 100);
            } else if ($scope.selectedVoucher.kieuGiamGia === true) {
                // Giảm theo giá trị cố định
                discount = $scope.selectedVoucher.giaTriGiamGia;
            }

            totalDiscountedPrice -= discount; // Trừ giảm giá
        }

        // Nếu có phí vận chuyển, cộng vào tổng tiền
        if ($scope.shippingFee) {
            totalDiscountedPrice += $scope.shippingFee;
        }

        // Đảm bảo tổng tiền không âm
        $scope.totalAmount = Math.max(totalDiscountedPrice, 0);

        // Cập nhật lại các giá trị để hiển thị trong UI
        $scope.totalProductPrice = totalProductPrice;
        $scope.totalDiscountedPrice = totalDiscountedPrice;

        console.log("Tổng tiền sản phẩm (theo giá bán cơ bản): ", totalProductPrice);
        console.log("Tổng tiền sản phẩm và giá khuyến mãi (cộng phí ship): ", totalDiscountedPrice);
    };


    $scope.placeOrder = function () {
        if ($scope.cart.length === 0) {
            alert("Giỏ hàng của bạn đang trống!");
            return;
        }

        // Kiểm tra thông tin người dùng
        if (!$scope.userInfo.tenNguoiDung) {
            alert("Vui lòng nhập tên người dùng!");
            return;
        }
        if (!$scope.userInfo.email) {
            alert("Vui lòng nhập email!");
            return;
        }
        if (!$scope.userInfo.sdt) {
            alert("Vui lòng nhập số điện thoại!");
            return;
        }
        if (!$scope.userInfo.diaChi) {
            alert("Vui lòng nhập địa chỉ!");
            return;
        }

        // Kiểm tra thông tin giao hàng
        if (!$scope.selectedCity || !$scope.selectedDistrict || !$scope.selectedWard) {
            alert("Vui lòng chọn đầy đủ thông tin nhận hàng!");
            return;
        }
        // Tạo danh sách sản phẩm chi tiết từ giỏ hàng
        const listSanPhamChiTiet = $scope.cart.map(item => ({
            idspct: item.idSanPhamCT,  // Dùng idSanPhamCT thay vì idSanPhamCT trực tiếp
            soLuong: item.soLuongGioHang,  // Số lượng của sản phẩm trong giỏ hàng
            giaTien: item.giaKhuyenMai != null ? item.giaKhuyenMai : item.giaBan  // Giá của sản phẩm, nếu có giá khuyến mãi thì dùng, nếu không thì dùng giá bán
        }));

        // Dữ liệu hóa đơn
        const orderData = {
            cartId: iduser,
            idNguoiDung: $scope.userInfo.idNguoiDung,
            tinh: $scope.selectedCity.code, // Lấy mã tỉnh từ selectedCity
            huyen: $scope.selectedDistrict.code, // Lấy mã huyện từ selectedDistrict
            xa: $scope.selectedWard.code, // Lấy mã xã từ selectedWard
            email: $scope.userInfo.email,
            tenNguoiNhan: $scope.userInfo.tenNguoiDung,
            diaChi: $scope.userInfo.diaChi,
            sdtNguoiNhan: $scope.userInfo.sdt,
            ghiChu: $scope.shippingInfo.note,
            tenPhuongThucThanhToan: $scope.selectedPaymentMethod,
            listSanPhamChiTiet: listSanPhamChiTiet, // Đưa vào danh sách sản phẩm chi tiết
            thanhTien: $scope.totalAmount,
            idVoucher: $scope.selectedVoucher ? $scope.selectedVoucher.id : null, // Nếu có voucher thì thêm vào
        };
        console.log("orderData:", orderData);

        // Kiểm tra và tạo thanh toán VNPay
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


    // Lắng nghe sự thay đổi khi chọn tỉnh
    $scope.$watch("shippingInfo.province", function (newProvince) {
        if (newProvince) {
            $http.get(`http://localhost:8080/api/nguoi_dung/test/districts/${newProvince}`)
                .then(response => $scope.districts = response.data)
                .catch(error => console.error("Lỗi khi lấy quận huyện:", error));

            // Tính phí vận chuyển khi tỉnh thay đổi
            $scope.calculateShippingFee();
        }
    });

    // Lắng nghe sự thay đổi khi chọn quận
    $scope.$watch("shippingInfo.district", function (newDistrict) {
        if (newDistrict) {
            $http.get(`http://localhost:8080/api/nguoi_dung/test/wards/${newDistrict}`)
                .then(response => $scope.wards = response.data)
                .catch(error => console.error("Lỗi khi lấy phường xã:", error));

            // Tính phí vận chuyển khi quận thay đổi
            $scope.calculateShippingFee();
        }
    });

    // Lắng nghe sự thay đổi khi chọn xã
    $scope.$watch("shippingInfo.ward", function (newWard) {
        if (newWard) {
            // Tính phí vận chuyển khi xã thay đổi
            $scope.calculateShippingFee();
        }
    });

    $scope.getProvinces(); if (user) $scope.getUserInfo(iduser); $scope.getCartItems();

    // Định nghĩa hàm tính phí ship
    $scope.calculateShippingFee = function () {
        if ($scope.selectedCity && $scope.selectedDistrict && $scope.selectedWard) {
            // Lấy thông tin tỉnh, huyện, xã đã chọn
            const fromProvinceId = 201; // Mã tỉnh gửi hàng (có thể thay đổi theo thực tế)
            const fromDistrictId = 201; // Mã quận gửi hàng (có thể thay đổi theo thực tế)
            const toProvinceId = $scope.selectedCity.code;  // Mã tỉnh nhận hàng
            const toDistrictId = $scope.selectedDistrict.code; // Mã quận nhận hàng
            const toWardCode = $scope.selectedWard.code; // Mã phường nhận hàng

            // Trọng lượng gói hàng (ví dụ 800g)
            const weight = 800;
            // Kích thước gói hàng (ví dụ chiều dài 50cm, chiều rộng 30cm, chiều cao 15cm)
            const length = 50;
            const width = 30;
            const height = 15;
            // ID dịch vụ (ví dụ dịch vụ mặc định)
            const serviceId = 53321;

            // Tạo đối tượng gửi request API
            const requestData = {
                from_province_id: fromProvinceId,
                from_district_id: fromDistrictId,
                to_province_id: toProvinceId,
                to_district_id: toDistrictId,
                to_ward_code: toWardCode,
                weight: weight,
                length: length,
                width: width,
                height: height,
                service_id: serviceId,
                insurance_value: null,
                cod_failed_amount: null,
                coupon: null
            };

            // Gửi yêu cầu API tính phí vận chuyển
            $http.post("http://localhost:8080/api/admin/shipping/get-shipping-fee", requestData)
                .then(function (response) {
                    // Khi nhận được phản hồi thành công, cập nhật phí ship
                    if (response.data.code === 200) {
                        $scope.shippingFee = response.data.data.total;
                        // Log ra số tiền phí vận chuyển
                        console.log("Phí vận chuyển: ", $scope.shippingFee);
                    } else {
                        console.error("Lỗi tính phí vận chuyển:", response.data.message);
                    }
                })
                .catch(function (error) {
                    console.error("Có lỗi xảy ra khi tính phí vận chuyển:", error);
                });
        } else {
            console.log("Vui lòng chọn đầy đủ tỉnh, huyện và xã.");
        }
    };


    // Khởi tạo dữ liệu ban đầu
    $scope.cities = [];
    $scope.districts = [];
    $scope.wards = [];

    // Hàm gọi API lấy danh sách tỉnh thành
    function getCities() {
        $http.get("http://127.0.0.1:8080/api/nguoi_dung/test/cities")
            .then(function (response) {
                $scope.cities = response.data;
            })
            .catch(function (error) {
                console.error('Có lỗi xảy ra khi lấy tỉnh thành:', error);
            });
    }

    // Hàm gọi API lấy danh sách huyện theo mã tỉnh
    $scope.onCityChange = function () {
        if ($scope.selectedCity) {
            $http.get("http://127.0.0.1:8080/api/nguoi_dung/test/districts/" + $scope.selectedCity.code)
                .then(function (response) {
                    $scope.districts = response.data;
                })
                .catch(function (error) {
                    console.error('Có lỗi xảy ra khi lấy huyện:', error);
                });
        } else {
            $scope.districts = [];
            $scope.wards = [];
        }
    };

    // Hàm gọi API lấy danh sách xã theo mã huyện
    $scope.onDistrictChange = function () {
        if ($scope.selectedDistrict) {
            $http.get("http://127.0.0.1:8080/api/nguoi_dung/test/wards/" + $scope.selectedDistrict.code)
                .then(function (response) {
                    $scope.wards = response.data;
                })
                .catch(function (error) {
                    console.error('Có lỗi xảy ra khi lấy xã:', error);
                });
        } else {
            $scope.wards = [];
        }
    };

    // Hàm lưu địa chỉ vào cơ sở dữ liệu
    $scope.submitForm = function () {
        if ($scope.selectedCity && $scope.selectedDistrict && $scope.selectedWard) {
            const userId = $scope.userId;
            const cityCode = $scope.selectedCity.code;
            const districtCode = $scope.selectedDistrict.code;
            const wardCode = $scope.selectedWard.code;
            console.log("1231313312331313212123" + userId + cityCode + districtCode + wardCode);
            const url = `http://127.0.0.1:8080/api/nguoi_dung/test/save-location?userId=${userId}&cityCode=${cityCode}&districtCode=${districtCode}&wardCode=${wardCode}`;
            console.log("1231313312331313212123" + url);
            $http.post(url)
                .then(function (response) {
                    $scope.isSuccess = true;
                })
                .catch(function (error) {
                    console.error('Có lỗi xảy ra khi lưu địa chỉ:', error);
                });
        } else {
            alert("Vui lòng chọn đầy đủ địa chỉ.");
        }
    };
    // Gọi API lấy danh sách tỉnh thành khi trang được tải
    getCities();
    $scope.dsvoucher();
    $scope.getVoucher();

};
