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

                // Check if shippingInfo is empty, if so, initialize it
                if (!Object.keys($scope.shippingInfo).length) {
                    $scope.shippingInfo = {};
                }

                // Assign user info to shippingInfo
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

                // Nếu giỏ hàng trống, thông báo và thoát
                if ($scope.cart.length === 0) {
                    alert("Giỏ hàng của bạn đang trống!");
                    return;
                }

                // Duyệt qua từng sản phẩm trong giỏ để lấy ảnh sản phẩm
                $scope.cart.forEach(item => {
                    // Lấy ảnh sản phẩm từ API
                    $http.get("http://localhost:8080/api/nguoi_dung/hinh_anh/" + item.idSanPham)
                        .then(res => item.urlAnh = res.data[0]?.urlAnh || '')
                        .catch(error => console.error("Lỗi khi lấy ảnh sản phẩm:", error));
                });

                // Tính tổng tiền (nếu bạn có phương thức tính tổng)
                $scope.calculateTotal();

                // Lưu giỏ hàng vào localStorage
                localStorage.setItem('cart', JSON.stringify($scope.cart));
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

            // Lưu voucher và tổng tiền vào localStorage
            localStorage.setItem('selectedVoucher', JSON.stringify($scope.selectedVoucher));
            localStorage.setItem('totalAmount', $scope.totalAmount);
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
        // Tính tổng tiền sản phẩm theo giá bán cơ bản hoặc giá khuyến mãi
        let totalProductPrice = $scope.cart.reduce(function (total, item) {
            let donGia = item.giaKhuyenMai != null ? item.giaKhuyenMai : item.giaBan;
            return total + (item.soLuongGioHang * donGia);
        }, 0);

        let totalDiscountedPrice = totalProductPrice;

        // Áp dụng voucher giảm giá (nếu có)
        if ($scope.selectedVoucher) {
            let discount = 0;
            if ($scope.selectedVoucher.kieuGiamGia === false) {
                // Giảm theo phần trăm
                discount = totalProductPrice * ($scope.selectedVoucher.giaTriGiamGia / 100);
            } else if ($scope.selectedVoucher.kieuGiamGia === true) {
                // Giảm theo giá trị cố định
                discount = $scope.selectedVoucher.giaTriGiamGia;
            }

            totalDiscountedPrice -= discount; // Trừ giảm giá
        }

        // Cộng thêm phí vận chuyển (nếu có)
        if ($scope.shippingFee) {
            totalDiscountedPrice += $scope.shippingFee;
        }

        // Đảm bảo tổng tiền không âm
        $scope.totalAmount = Math.max(totalDiscountedPrice, 0);

        // Cập nhật các giá trị để hiển thị trong UI
        $scope.totalProductPrice = totalProductPrice;
        $scope.totalDiscountedPrice = totalDiscountedPrice;

        // Lưu tổng tiền vào localStorage
        localStorage.setItem('totalAmount', $scope.totalAmount);

        console.log("Tổng tiền sản phẩm (theo giá bán cơ bản): ", totalProductPrice);
        console.log("Tổng tiền sau khi áp dụng voucher và phí ship: ", totalDiscountedPrice);
    };

    $scope.validateUserInfo = function () {
        if (!$scope.userInfo.tenNguoiDung || $scope.userInfo.tenNguoiDung.trim() === "") {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng nhập tên người dùng hợp lệ!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return false;
        }
        if (!$scope.userInfo.email || $scope.userInfo.email.trim() === "") {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng nhập email.',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return false;
        } else {
            // Loại bỏ dấu cách thừa trước và sau email trước khi kiểm tra
            var email = $scope.userInfo.email.trim();
        
            // Kiểm tra định dạng email
            var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        
            if (!emailPattern.test(email)) {
                Swal.fire({
                    title: 'Lỗi!',
                    text: 'Định dạng email không hợp lệ! Vui lòng kiểm tra lại.',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
                return false;
            }
        }
        
        if (!$scope.userInfo.sdt || $scope.userInfo.sdt.trim() === "") {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng nhập số điện thoại!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return false;
        } else if (!/^\d{10,11}$/.test($scope.userInfo.sdt)) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Số điện thoại phải chứa từ 10 đến 11 chữ số!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return false;
        }
        if (!$scope.userInfo.diaChi || $scope.userInfo.diaChi.trim() === "") {
            Swal.fire({
                title: 'Không Được Bỏ Trống Địa Chỉ',
                text: 'Vui lòng nhập địa chỉ giao hàng!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return false;
        }

        // Nếu tất cả hợp lệ
        return true;
    };


    $scope.validateShippingInfo = function () {
        if (!$scope.selectedCity || !$scope.selectedDistrict || !$scope.selectedWard) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng chọn đầy đủ thông tin nhận hàng!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return false;
        }
        return true;
    };

    $scope.validateOrderDetails = function () {
        if (!$scope.cart || $scope.cart.length === 0) {
            swal("Lỗi!", "Giỏ hàng của bạn đang trống!", "error");
            return false;
        }
        if (!$scope.selectedPaymentMethod) {
            swal("Lỗi!", "Vui lòng chọn phương thức thanh toán!", "error");
            return false;
        }
        return true;
    };

    $scope.placeOrder = function () {

        // Kiểm tra thông tin người dùng
        if (!$scope.validateUserInfo()) {
            return;
        }

        // Kiểm tra thông tin giao hàng
        if (!$scope.validateShippingInfo()) {
            return;
        }

        // Kiểm tra thông tin hóa đơn
        if (!$scope.validateOrderDetails()) {
            return;
        }



        // Tạo danh sách sản phẩm chi tiết từ giỏ hàng
        const listSanPhamChiTiet = $scope.cart.map(item => ({
            idspct: item.idSanPhamCT,
            soLuong: item.soLuongGioHang,
            giaTien: item.giaKhuyenMai != null ? item.giaKhuyenMai : item.giaBan
        }));
        localStorage.setItem("email", JSON.stringify($scope.userInfo.email));
        localStorage.setItem("diachi", JSON.stringify($scope.userInfo.diaChi));
        localStorage.setItem("sdt", JSON.stringify($scope.userInfo.sdt));

        // Dữ liệu hóa đơn
        const orderData = {
            cartId: iduser,
            idNguoiDung: $scope.userInfo.idNguoiDung,
            tinh: $scope.selectedCity.code,
            huyen: $scope.selectedDistrict.code,
            xa: $scope.selectedWard.code,
            email: $scope.userInfo.email,
            tenNguoiNhan: $scope.userInfo.tenNguoiDung,
            diaChi: $scope.userInfo.diaChi,
            sdtNguoiNhan: $scope.userInfo.sdt,
            ghiChu: $scope.shippingInfo.note,
            tenPhuongThucThanhToan: $scope.selectedPaymentMethod,
            listSanPhamChiTiet: listSanPhamChiTiet,
            thanhTien: $scope.totalAmount,
            idvoucher: $scope.selectedVoucher && $scope.selectedVoucher.idVoucher ? $scope.selectedVoucher.idVoucher : null
        };

        console.log("orderData:", orderData);

        // Lưu orderData vào localStorage
        localStorage.setItem("orderData", JSON.stringify(orderData));

        // Kiểm tra và tạo thanh toán VNPay
        if ($scope.selectedPaymentMethod === "vnpay") {
            const paymentUrl = `http://localhost:8080/api/nguoi_dung/vnpays/create_payment?amount=${$scope.totalAmount}&paymentMethod=vnpay`;

            $http.post(paymentUrl)
                .then(function (response) {
                    const paymentUrlMatch = response.data.match(/window\.location\.href='([^']+)'/);

                    if (paymentUrlMatch && paymentUrlMatch[1]) {
                        const paymentRedirectUrl = paymentUrlMatch[1];
                        console.error("orderData:", orderData);
                        window.location.href = paymentRedirectUrl;
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

                if (response.data.idHoaDon) {
                    localStorage.setItem("idHoaDon", response.data.idHoaDon);
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
        $http.get("http://127.0.0.1:8080/api/nguoi_dung/test/cities")
            .then(response => $scope.provinces = response.data)
            .catch(error => console.error("Lỗi khi lấy tỉnh thành:", error));
    };


    $scope.getProvinces(); if (user) $scope.getUserInfo(iduser); $scope.getCartItems();

    // Khởi tạo dữ liệu ban đầu
    $scope.cities = [];
    $scope.districts = [];
    $scope.wards = [];
    $scope.shippingInfo = {
        province: null,
        district: null,
        ward: null,
        address: ''
    };
    $scope.cart = [];
    $scope.shippingFee = 0;
    $scope.totalAmount = 0;

    // Hàm gọi API lấy danh sách tỉnh thành
    function getCities() {
        if ($scope.selectedCity && $scope.selectedCity.code) {
            console.log('Province ID:', $scope.selectedCity.code);
        } else {
            console.warn('Chưa chọn tỉnh thành.');
        }

        $http.get("http://127.0.0.1:8080/api/nguoi_dung/test/cities")
            .then(function (response) {
                if (response.data && Array.isArray(response.data)) {
                    $scope.cities = response.data;


                } else {
                    console.error('Không có dữ liệu tỉnh thành trả về.');
                }
            })
            .catch(function (error) {
                console.error('Có lỗi xảy ra khi lấy tỉnh thành:', error);
            });
    }

    // Hàm gọi API lấy danh sách huyện theo mã tỉnh
    $scope.onCityChange = function () {
        $scope.districts = [];
        $scope.wards = [];
        $scope.shippingInfo.district = null;
        $scope.shippingInfo.ward = null;

        if ($scope.selectedCity && $scope.selectedCity.code) {
            console.log('ID tỉnh vừa chọn:', $scope.selectedCity.code);
            localStorage.setItem("idTinh", JSON.stringify($scope.selectedCity.code));
            $http.get("http://127.0.0.1:8080/api/nguoi_dung/test/districts/" + $scope.selectedCity.code)
                .then(function (response) {
                    if (response.data && Array.isArray(response.data)) {
                        $scope.districts = response.data;

                    } else {
                        console.error('Không có dữ liệu huyện trả về.');
                    }
                })
                .catch(function (error) {
                    console.error('Có lỗi xảy ra khi lấy huyện:', error);
                });
        } else {
            console.warn('Chưa chọn tỉnh thành.');
        }
    };

    // Hàm gọi API lấy danh sách xã theo mã huyện
    $scope.onDistrictChange = function () {
        $scope.wards = [];
        $scope.shippingInfo.ward = null;

        if ($scope.selectedDistrict && $scope.selectedDistrict.code) {
            console.log('ID huyện vừa chọn:', $scope.selectedDistrict.code);
            localStorage.setItem("idHuyen", JSON.stringify($scope.selectedDistrict.code));
            $http.get("http://127.0.0.1:8080/api/nguoi_dung/test/wards/" + $scope.selectedDistrict.code)
                .then(function (response) {
                    if (response.data && Array.isArray(response.data)) {
                        $scope.wards = response.data;


                    } else {
                        console.error('Không có dữ liệu xã trả về.');
                    }
                })
                .catch(function (error) {
                    console.error('Có lỗi xảy ra khi lấy xã:', error);
                });
        } else {
            console.warn('Chưa chọn huyện.');
        }
    };


    $scope.calculateShipping = function () {
        // Kiểm tra nếu chưa chọn đủ thông tin quận/huyện và phường/xã
        if (!$scope.selectedDistrict || !$scope.selectedWard) {
            alert("Vui lòng chọn đầy đủ thông tin quận/huyện và phường/xã!");
            return;
        }
        localStorage.setItem("idXa", JSON.stringify($scope.selectedWard.code));
        // Lấy thông tin cần thiết
        const cityCode = $scope.selectedCity.code;      // Mã tỉnh/thành phố
        const districtCode = $scope.selectedDistrict.code; // Mã quận/huyện
        const wardCode = $scope.selectedWard.code;      // Mã phường/xã

        // Gửi yêu cầu HTTP đến backend để lấy phí vận chuyển
        $http.get("http://127.0.0.1:8080/api/nguoi_dung/test/shipping-fee/"
            + cityCode + "/" + districtCode + "/" + wardCode)
            .then(function (response) {
                if (response.data) {
                    // Cập nhật phí vận chuyển trong scope
                    $scope.shippingFee = response.data;
                    console.log("Phí vận chuyển:", $scope.shippingFee);

                    // Cập nhật lại tổng tiền khi đã có phí vận chuyển
                    $scope.calculateTotal();
                } else {
                    // Nếu không có dữ liệu trả về, vẫn tính tổng tiền
                    $scope.calculateTotal();
                }
            })
            .catch(function (error) {
                console.error("Có lỗi xảy ra khi lấy phí vận chuyển:", error);

                // Xử lý lỗi và cập nhật tổng tiền
                $scope.calculateTotal();
            });
    };



    // Lắng nghe sự thay đổi khi người dùng chọn tỉnh thành, quận huyện, xã phường
    $scope.$watchGroup(['selectedCity', 'selectedDistrict', 'selectedWard'], function (newValues, oldValues) {
        // Kiểm tra khi tất cả các trường được chọn
        if ($scope.selectedCity && $scope.selectedDistrict && $scope.selectedWard) {
            $scope.calculateShipping();
        }
    });


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
    $scope.autoSave = function () {
        const shippingData = {
            email: $scope.userInfo.email || '',
            name: $scope.userInfo.tenNguoiDung || '',
            phone: $scope.userInfo.sdt || '',
            address: $scope.userInfo.diaChi || '',

        };

        // Lưu dữ liệu vào localStorage
        localStorage.setItem("shippingData", JSON.stringify(shippingData));
        console.log("Đã lưu thông tin tự động:", shippingData);
    };

    // Gọi API lấy danh sách tỉnh thành khi trang được tải
    getCities();
    $scope.dsvoucher();
    $scope.getVoucher();

};