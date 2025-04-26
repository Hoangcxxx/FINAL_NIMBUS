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
    $scope.selectedMBBANK = "mbbank";
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
        if (voucher.isUsable) {
            $scope.voucherCode = voucher.maVoucher; // Gán mã voucher vào input (nếu cần)
            $scope.voucherError = ''; // Reset lỗi
            console.log("Voucher được chọn:", voucher, "ID Voucher:", voucher.idVoucher);
            console.log("Trang thái giảm giá:", voucher.trangThaiGiamGia.idTrangThaiGiamGia); // Log thêm thông tin trạng thái giảm giá

            // Lưu idVoucher và idTrangThaiGiamGia vào localStorage
            localStorage.setItem('idVoucher', voucher.idVoucher);
            localStorage.setItem('idTrangThaiGiamGia', voucher.trangThaiGiamGia.idTrangThaiGiamGia);

            // Kiểm tra voucher sau khi chọn
            checkVoucher(); // Gọi hàm kiểm tra voucher
        } else {
            $scope.voucherError = 'Voucher này không thể sử dụng.';
        }
    };


    // Hàm checkVoucher sẽ lấy idVoucher từ localStorage
    function checkVoucher() {
        const idVoucher = localStorage.getItem('idVoucher');
        console.log("idVoucher từ localStorage:", idVoucher); // Log idVoucher lấy từ localStorage

        // Kiểm tra nếu có idVoucher
        if (idVoucher) {
            console.log("Kiểm tra voucher với idvoucher: ", idVoucher); // Log idVoucher

            return $http.get(`http://localhost:8080/api/nguoi_dung/vouchers/vouchers/${idVoucher}`)
                .then(function (response) {
                    const voucher = response.data;
                    console.log("Voucher nhận được từ API: ", voucher); // Log thông tin voucher nhận được từ API

                    // Kiểm tra trạng thái giảm giá của voucher (ví dụ: trạng thái là 4 có thể là 'không hợp lệ')
                    if (voucher && voucher.idTrangThaiGiamGia === 4) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Voucher không hợp lệ!',
                            text: 'Voucher này không hợp lệ và không thể sử dụng để thanh toán.',
                            confirmButtonText: 'Đồng ý'
                        });
                        throw new Error('Voucher không hợp lệ.');
                    }
                })
                .catch(function (error) {
                    console.error('Lỗi khi kiểm tra voucher:', error);
                    throw error;
                });
        } else {
            console.log("Không có idVoucher trong localStorage để kiểm tra."); // Log nếu không có idVoucher trong localStorage
        }
    }


    $scope.removeVoucher = function () {
        // Xóa voucher đã chọn
        $scope.selectedVoucher = null;
        $scope.voucherCode = '';  // Xóa mã voucher (nếu có)

        // Đặt lại giá trị giảm giá và tổng tiền về giá trị ban đầu
        $scope.discount = 0;

        // Tính toán lại tổng tiền
        $scope.calculateTotal();

        // Xóa thông tin liên quan đến voucher khỏi localStorage
        localStorage.removeItem('selectedVoucher'); // Xóa voucher đã lưu trong localStorage
        localStorage.setItem('totalAmount', $scope.totalAmount); // Cập nhật lại tổng tiền
        localStorage.removeItem('idVoucher');
        localStorage.removeItem('idTrangThaiGiamGia');
        console.log('Voucher đã bị xóa và tổng tiền đã được tính toán lại.');
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









    $scope.formatCurrency = function (amount) {
        return new Intl.NumberFormat('vi-VN', {
            style: 'decimal',
            minimumFractionDigits: 0,
            maximumFractionDigits: 0
        }).format(amount) + ' VNĐ';  // Sử dụng 'VNĐ' thay vì '₫'
    };




    $scope.applyVoucher = function () {
        if ($scope.voucherCode) {
            // Tìm voucher từ danh sách bằng mã nhập vào hoặc đã chọn
            const voucher = $scope.Voucher.find(v => v.maVoucher === $scope.voucherCode);

            if (!voucher || !voucher.isUsable) {
                // Nếu voucher không tồn tại hoặc không hợp lệ
                $scope.voucherError = 'Voucher không hợp lệ hoặc không thể sử dụng.';
                Swal.fire({
                    icon: 'error',
                    title: 'Voucher không hợp lệ',
                    text: $scope.voucherError,
                    confirmButtonText: 'OK'
                });
                return;
            }

            // Kiểm tra giá trị tối thiểu của hóa đơn
            if ($scope.totalAmount < voucher.soTienToiThieu) {
                $scope.voucherError = 'Tổng tiền hóa đơn không đủ điều kiện áp dụng voucher. Bạn cần ít nhất ' + $scope.formatCurrency(voucher.soTienToiThieu) + ' để áp dụng.';
                Swal.fire({
                    icon: 'error',
                    title: 'Voucher không hợp lệ',
                    text: $scope.voucherError,
                    confirmButtonText: 'OK'
                });
                return;
            }

            // Kiểm tra giá trị tối đa của hóa đơn (nếu có)
            if (voucher.giaTriToiDa !== null && $scope.totalAmount > voucher.giaTriToiDa) {
                $scope.voucherError = 'Tổng tiền hóa đơn vượt quá giá trị tối đa của voucher. Voucher này chỉ có thể áp dụng khi tổng tiền từ ' + $scope.formatCurrency(voucher.soTienToiThieu) + ' đến ' + $scope.formatCurrency(voucher.giaTriToiDa) + '.';
                Swal.fire({
                    icon: 'error',
                    title: 'Voucher không hợp lệ',
                    text: $scope.voucherError,
                    confirmButtonText: 'OK'
                });
                return;
            }

            // Xóa lỗi và áp dụng voucher
            $scope.voucherError = '';
            $scope.selectedVoucher = voucher;

            // Tính toán giảm giá
            let discount = 0;
            if (voucher.kieuGiamGia === false) {
                // Giảm theo phần trăm
                discount = $scope.totalAmount * (voucher.giaTriGiamGia / 100);
                discount = Math.min(discount, voucher.giaTriToiDa || discount); // Giới hạn giảm tối đa nếu có
            } else if (voucher.kieuGiamGia === true) {
                // Giảm theo số tiền cố định
                discount = voucher.giaTriGiamGia;
            }

            // Cập nhật tổng tiền sau giảm giá
            $scope.totalAmount -= discount;
            $scope.totalAmount = Math.max($scope.totalAmount, 0); // Đảm bảo không âm

            // Tính lại tổng tiền để đảm bảo giá trị chính xác trong UI
            $scope.calculateTotal();

            // Hiển thị thông báo thành công
            Swal.fire({
                icon: 'success',
                title: 'Voucher đã được áp dụng!',
                text: 'Voucher mã ' + voucher.maVoucher + ' đã thành công. Tổng tiền sau khi giảm giá: ' + $scope.formatCurrency($scope.totalAmount),
                confirmButtonText: 'OK'
            });

            // Lưu voucher và tổng tiền vào localStorage
            localStorage.setItem('selectedVoucher', JSON.stringify(voucher));
            localStorage.setItem('totalAmount', $scope.totalAmount);

            // Ẩn modal
            $('#voucherModal').modal('hide');
        } else {
            // Nếu không có mã voucher
            $scope.voucherError = 'Vui lòng nhập hoặc chọn một mã voucher.';
        }
    };


    $scope.calculateDiscountAmount = function (voucher) {
        let discount = 0;

        // Sử dụng totalProductPrice để tính toán giảm giá
        let amount = $scope.totalProductPrice;

        // Nếu kiểu giảm giá là phần trăm
        if (!voucher.kieuGiamGia) {
            // Tính số tiền giảm giá từ số tiền tạm tính (totalProductPrice)
            discount = amount * (voucher.giaTriGiamGia / 100);
            discount = Math.min(discount, voucher.giaTriToiDa || discount); // Giới hạn giảm tối đa nếu có
        } else if (voucher.kieuGiamGia) {
            // Nếu kiểu giảm giá là số tiền cố định
            discount = voucher.giaTriGiamGia;
        }

        return discount;
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
            let donGia = item.giaKhuyenMai !== null ? item.giaKhuyenMai : item.giaBan;
            return total + (item.soLuongGioHang * donGia);
        }, 0);

        let discount = 0;

        // Áp dụng voucher giảm giá (nếu có)
        if ($scope.selectedVoucher) {
            if (!$scope.selectedVoucher.kieuGiamGia) {
                // Giảm theo phần trăm
                discount = totalProductPrice * ($scope.selectedVoucher.giaTriGiamGia / 100);
                // Nếu có giới hạn giảm tối đa, lấy giá trị nhỏ hơn
                discount = Math.min(discount, $scope.selectedVoucher.giaTriToiDa || discount);
            } else {
                // Giảm theo giá trị cố định
                discount = $scope.selectedVoucher.giaTriGiamGia;
            }
            // Giới hạn số tiền giảm giá không vượt quá tổng tiền sản phẩm
            discount = Math.min(discount, totalProductPrice);
        }

        // Tính số tiền sau giảm giá (chỉ tính voucher, chưa cộng phí ship)
        let totalAfterDiscount = totalProductPrice - discount;
        totalAfterDiscount = Math.max(totalAfterDiscount, 0);  // đảm bảo không âm

        // Cộng phí vận chuyển (nếu có)
        let shippingFee = $scope.shippingFee || 0;
        let totalDiscountedPrice = totalAfterDiscount + shippingFee;

        // Đảm bảo tổng tiền cuối cùng không âm
        $scope.totalAmount = Math.max(totalDiscountedPrice, 0);

        // Cập nhật các giá trị hiển thị trong UI
        $scope.totalProductPrice = totalProductPrice;
        $scope.discountAmount = discount;
        $scope.totalDiscountedPrice = totalDiscountedPrice;

        // Lưu tổng tiền vào localStorage
        localStorage.setItem('totalAmount', $scope.totalAmount);

        console.log("Tổng tiền sản phẩm:", totalProductPrice);
        console.log("Giảm giá:", discount);
        console.log("Tổng tiền sau khi áp dụng voucher và phí ship:", totalDiscountedPrice);
    };


    $scope.validateUserInfo = function () {
        if (!$scope.userInfo.tenNguoiDung || $scope.userInfo.tenNguoiDung.trim() === "") {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Tên người dùng không được để trống!',
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
        } else {
            // Loại bỏ khoảng trắng trước và sau số điện thoại
            let phoneNumber = $scope.userInfo.sdt.trim();

            // Kiểm tra nếu số điện thoại chỉ chứa số và có độ dài 10 hoặc 11 chữ số
            if (!/^\d{10,11}$/.test(phoneNumber)) {
                // Kiểm tra nếu có bất kỳ ký tự chữ nào trong số điện thoại
                if (/[a-zA-Z]/.test(phoneNumber)) {
                    Swal.fire({
                        title: 'Lỗi!',
                        text: 'Số điện thoại chỉ được phép chứa các chữ số!',
                        icon: 'error',
                        confirmButtonText: 'OK'
                    });
                    return false;
                }

                Swal.fire({
                    title: 'Lỗi!',
                    text: 'Số điện thoại phải chứa từ 10 đến 11 chữ số!',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
                return false;
            }
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
            Swal.fire({
                title: 'Lỗi!',
                text: 'Giỏ hàng của bạn đang trống!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return false;
        }
        if (!$scope.selectedPaymentMethod) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng chọn phương thức thanh toán!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            swal("Lỗi!", "Vui lòng chọn phương thức thanh toán!", "error");
            return false;
        }
        return true;
    };

    // Thêm lớp overlay khi đang xử lý
    $scope.showOverlay = function () {
        var overlayElement = document.querySelector('.modal-overlay');
        if (overlayElement) {
            overlayElement.style.display = 'block';
        }
    };

    // Xóa lớp overlay khi xử lý xong
    $scope.hideOverlay = function () {
        var overlayElement = document.querySelector('.modal-overlay');
        if (overlayElement) {
            overlayElement.style.display = 'none';
        }
    };

    $scope.placeOrder = function () {


        // Kiểm tra trạng thái người dùng
        function checkUserStatus() {
            return $http.get(`http://localhost:8080/api/admin/nguoi_dung/check_trang_thai/${$scope.idNguoiDung}`)
                .then(function (response) {
                    if (response.data.trangThai === true) {
                        return true; // Trạng thái hợp lệ
                    } else {
                        // Hiển thị thông báo tài khoản bị khóa
                        Swal.fire({
                            title: 'Tài khoản của bạn đã bị khóa!',
                            text: 'Rất tiếc, tài khoản của bạn đã bị tạm khóa do phát hiện hoạt động bất thường hoặc vi phạm chính sách sử dụng. Vui lòng liên hệ Quản trị viên để được hỗ trợ.',
                            icon: 'error',
                            confirmButtonText: 'Đồng ý'
                        });
                        return false;
                    }
                })
                .catch(function (error) {
                    console.error('Lỗi khi kiểm tra trạng thái người dùng:', error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi!',
                        text: 'Đã xảy ra lỗi khi kiểm tra trạng thái người dùng. Vui lòng thử lại sau.',
                        confirmButtonText: 'Đồng ý'
                    });
                    return false;
                });
        }

        function checkVoucher() {
            // Lấy idVoucher và idTrangThaiGiamGia từ localStorage
            const idVoucher = localStorage.getItem('idVoucher');
            const idTrangThaiGiamGia = localStorage.getItem('idTrangThaiGiamGia');

            console.log("idVoucher từ localStorage:", idVoucher); // Log idVoucher lấy từ localStorage
            console.log("idTrangThaiGiamGia từ localStorage:", idTrangThaiGiamGia); // Log idTrangThaiGiamGia lấy từ localStorage

            // Kiểm tra nếu có idVoucher
            if (idVoucher) {
                console.log("Kiểm tra voucher với idvoucher: ", idVoucher); // Log idVoucher

                return $http.get(`http://localhost:8080/api/nguoi_dung/vouchers/vouchers/${idVoucher}`)
                    .then(function (response) {
                        const voucher = response.data;
                        console.log("Voucher nhận được từ API: ", voucher); // Log thông tin voucher nhận được từ API

                        // Kiểm tra trạng thái giảm giá của voucher
                        if (voucher && voucher.trangThaiGiamGiaId === 5) {
                            Swal.fire({
                                icon: 'warning',
                                title: 'Voucher đã bị xóa!',
                                text: 'Voucher này đã bị xóa bởi người bán và không thể sử dụng để thanh toán.',
                                confirmButtonText: 'Đồng ý'
                            }).then(function () {
                                // Sau khi người dùng đóng thông báo lỗi, chỉ xóa idVoucher và idTrangThaiGiamGia nếu có voucher mới
                                const newVoucherId = localStorage.getItem('idVoucher');
                                if (newVoucherId !== idVoucher) { // Kiểm tra nếu voucher mới khác voucher cũ
                                    localStorage.removeItem('idVoucher');
                                    localStorage.removeItem('idTrangThaiGiamGia');
                                }
                            });
                            throw new Error('Voucher không hợp lệ.');
                        }

                        // Kiểm tra số lượng của voucher
                        if (voucher && voucher.soLuong === 0) {
                            Swal.fire({
                                icon: 'error',
                                title: 'Voucher không còn khả dụng!',
                                text: 'Rất tiếc, voucher này đã hết số lượng và không thể sử dụng nữa.',
                                confirmButtonText: 'Đồng ý'
                            })
                                .then(function () {
                                    // Sau khi người dùng đóng thông báo lỗi, chỉ xóa idVoucher và idTrangThaiGiamGia nếu có voucher mới
                                    const newVoucherId = localStorage.getItem('idVoucher');
                                    if (newVoucherId !== idVoucher) { // Kiểm tra nếu voucher mới khác voucher cũ
                                        localStorage.removeItem('idVoucher');
                                        localStorage.removeItem('idTrangThaiGiamGia');
                                    }

                                });
                            throw new Error('Voucher đã hết số lượng.');
                        }

                        // Nếu voucher hợp lệ, xử lý tiếp (nếu cần)
                        console.log("Voucher hợp lệ và có thể sử dụng.");
                    })
                    .catch(function (error) {
                        console.error('Lỗi khi kiểm tra voucher:', error);
                        throw error;
                    });
            } else {
                console.log("Không có idVoucher trong localStorage để kiểm tra."); // Log nếu không có idVoucher trong localStorage
            }
        }
        function checkProductPrices() {
            const promises = $scope.cart.map(item => {
                console.log("Kiểm tra sản phẩm trong giỏ hàng:", item);  // Debug thông tin item
                return $http.get(`http://localhost:8080/api/nguoi_dung/san_pham/kiem-tra-gia/${item.idSanPham}`)
                    .then(response => {
                        const giaBanMoi = response.data.giaBan;

                        if (giaBanMoi <= 0) {
                            Swal.fire({
                                icon: 'error',
                                title: "Giá sản phẩm không hợp lệ!",
                                text: `Giá của sản phẩm "${item.tenSanPham || 'Không xác định'}" không hợp lệ. Vui lòng kiểm tra lại.`,
                                confirmButtonText: 'Đồng ý'
                            });
                            throw new Error(`Giá sản phẩm "${item.tenSanPham || 'Không xác định'}" không hợp lệ.`);
                        }

                        // Kiểm tra nếu giá sản phẩm thay đổi
                        if (item.giaBan !== giaBanMoi) {
                            return Swal.fire({
                                icon: 'warning',
                                title: "Cập nhật giá sản phẩm",
                                text: `Shop vừa cập nhật giá của sản phẩm "${item.tenSanPham}". Giá mới là ${giaBanMoi.toLocaleString()} VNĐ (trước đó ${item.giaBan.toLocaleString()} VNĐ). Bạn có muốn tiếp tục đặt hàng không?`,
                                showCancelButton: true,
                                confirmButtonText: 'Tiếp tục mua',
                                cancelButtonText: 'Hủy'
                            }).then(result => {
                                if (result.isConfirmed) {
                                    item.giaBan = giaBanMoi; // Cập nhật giá sản phẩm
                                } else {
                                    throw new Error(`Người dùng từ chối mua sản phẩm "${item.tenSanPham}" với giá mới.`);
                                }
                            });
                        }
                    })
                    .catch(error => {
                        console.error(`Lỗi kiểm tra giá sản phẩm "${item.tenSanPham || 'Không xác định'}":`, error.message);
                        throw error; // Tiếp tục đẩy lỗi để Promise.all xử lý
                    });
            });

            return Promise.all(promises)
                .then(() => {
                    console.log("Tất cả sản phẩm đã được kiểm tra giá thành công.");
                })
                .catch(error => {
                    console.error("Lỗi trong quá trình kiểm tra giá sản phẩm:", error.message);
                });
        }




        // Kiểm tra tồn kho sản phẩm và khớp số lượng giỏ hàng
        function checkProductStock() {
            const promises = $scope.cart.map(item => {
                return $http.get(`http://localhost:8080/api/nguoi_dung/san_pham_chi_tiet/check-so-luong/${item.idSanPhamCT}?soLuongGioHang=${item.soLuongGioHang}`)
                    .then(function (response) {
                        const message = response.data.message;

                        // Kiểm tra thông báo từ backend
                        if (message.includes('hết hàng')) {
                            Swal.fire({
                                icon: 'error',
                                title: 'Sản phẩm hết hàng!',
                                text: `Sản phẩm "${item.tenSanPham}" đã hết hàng.`,
                                confirmButtonText: 'Đồng ý'
                            });
                            throw new Error(`Sản phẩm "${item.tenSanPham}" hết hàng.`);
                        } else if (message.includes('không khớp')) {
                            const [systemQuantity] = message.match(/Hệ thống: (\d+)/).slice(1); // Lấy số lượng tồn kho từ thông báo
                            Swal.fire({
                                icon: 'warning',
                                title: 'Số lượng không đủ!',
                                text: `Sản phẩm "${item.tenSanPham}" hiện không đủ số lượng trong kho. Chúng tôi chỉ có ${systemQuantity} sản phẩm, nhưng bạn muốn mua ${item.soLuongGioHang} sản phẩm.`,
                                confirmButtonText: 'Đồng ý'
                            });
                            throw new Error(`Sản phẩm "${item.tenSanPham}" không đủ số lượng trong kho.`);
                        }
                    })
                    .catch(function (error) {
                        console.error(`Lỗi kiểm tra tồn kho cho sản phẩm "${item.tenSanPham}":`, error);
                        throw error;
                    });
            });

            // Đợi tất cả kết quả của lời hứa
            return Promise.allSettled(promises).then(results => {
                const hasError = results.some(result => result.status === 'rejected');
                if (hasError) {
                    throw new Error('Có sản phẩm hết hàng hoặc không khớp số lượng trong giỏ.');
                }
            });
        }

        // Kiểm tra trạng thái sản phẩm trong giỏ hàng
        function checkProductStatus() {
            const promises = $scope.cart.map(item => {
                return $http.get(`http://localhost:8080/api/nguoi_dung/san_pham/${item.idSanPham}/trang-thai`)
                    .then(function (response) {
                        if (!response.data || response.data.trangThai === false) {
                            Swal.fire({
                                icon: 'error',
                                title: "Sản phẩm đã ngừng bán!",
                                text: `Sản phẩm "${item.tenSanPham}" không còn bán hoặc đã ngừng bán.`,
                                confirmButtonText: 'Đồng ý'
                            });
                            throw new Error(`Sản phẩm "${item.tenSanPham}" không hợp lệ.`);
                        }
                    })
                    .catch(function (error) {
                        console.error('Lỗi khi kiểm tra trạng thái sản phẩm:', error);
                        throw error;
                    });
            });


            return Promise.allSettled(promises).then(results => {
                const hasError = results.some(result => result.status === 'rejected');
                if (hasError) {
                    throw new Error('Có sản phẩm không hợp lệ trong giỏ hàng.');
                }
            });
        }
        // Hàm chính xử lý đặt hàng
        checkUserStatus()
            .then(function (isValidUser) {
                if (!isValidUser) {
                    throw new Error('Tài khoản bị khóa.');
                }
                return checkVoucher();  // Kiểm tra voucher
            })
            // .then(function () {
            //     return checkupdategiatienpai(); // Kiểm tra và cập nhật giá tiền
            // })
            .then(function () {
                return checkProductStock();  // Kiểm tra tồn kho
            })
            .then(function () {
                return checkProductStatus();  // Kiểm tra trạng thái sản phẩm
            })
            .then(function () {
                return checkProductPrices()
            })
            .then(function () {
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


                // Tạo danh sách sản phẩm từ giỏ hàng
                const listSanPhamChiTiet = $scope.cart.map(item => ({
                    idspct: item.idSanPhamCT,
                    soLuong: item.soLuongGioHang,
                    giaTien: item.giaKhuyenMai !== null ? item.giaKhuyenMai : item.giaBan
                }));

                // Chuẩn bị dữ liệu hóa đơn
                const orderData = {
                    cartId: $scope.idNguoiDung,
                    idNguoiDung: $scope.userInfo.idNguoiDung,
                    tinh: $scope.selectedCity.id,
                    huyen: $scope.selectedDistrict.id,
                    xa: $scope.selectedWard.id,
                    email: $scope.userInfo.email,
                    tenNguoiNhan: $scope.userInfo.tenNguoiDung,
                    diaChi: $scope.userInfo.diaChi,
                    sdtNguoiNhan: $scope.userInfo.sdt,
                    ghiChu: $scope.shippingInfo.note,
                    tenPhuongThucThanhToan: $scope.selectedPaymentMethod,
                    listSanPhamChiTiet: listSanPhamChiTiet,
                    thanhTien: $scope.totalDiscountedPrice,
                    idvoucher: $scope.selectedVoucher?.idVoucher || null
                };

                // Lưu thông tin vào localStorage
                localStorage.setItem("orderData", JSON.stringify(orderData));

                if ($scope.selectedPaymentMethod === "vnpay") {
                    // Xử lý thanh toán VNPay
                    const paymentUrl = `http://localhost:8080/api/nguoi_dung/vnpays/create_payment?amount=${$scope.totalDiscountedPrice}&paymentMethod=vnpay`;

                    return $http.post(paymentUrl).then(function (response) {
                        const paymentUrlMatch = response.data.match(/window\.location\.href='([^']+)'/);
                        if (paymentUrlMatch && paymentUrlMatch[1]) {
                            // Lưu thông tin thanh toán VNPay vào localStorage
                            localStorage.setItem("paymentMethod", "vnpay");
                            localStorage.setItem("totalAmount", $scope.totalDiscountedPrice);
                            localStorage.setItem("paymentUrl", paymentUrlMatch[1]);

                            // Chuyển hướng đến trang thanh toán VNPay
                            window.location.href = paymentUrlMatch[1];
                        } else {
                            Swal.fire({
                                icon: 'error',
                                title: 'Thanh Toán Thất Bại!',
                                text: 'Thanh Toán không hợp lệ. Vui lòng thử lại.',
                                confirmButtonText: 'Đồng ý'
                            });
                            throw new Error('Thanh toán VNPay thất bại.');
                        }
                    }).catch(function (error) {
                        console.error('Lỗi khi xử lý thanh toán VNPay:', error);
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi Thanh Toán',
                            text: 'Có lỗi xảy ra khi xử lý thanh toán. Vui lòng thử lại.',
                            confirmButtonText: 'Đồng ý'
                        });
                        throw error;
                    });

                } if ($scope.selectedPaymentMethod === "cod") {
                    if ($scope.isProcessing) return;
                
                    Swal.fire({
                        icon: 'info',
                        title: 'Xác nhận thanh toán khi nhận hàng',
                        text: 'Bạn sẽ thanh toán khi nhận hàng. Vui lòng xác nhận!',
                        showCancelButton: true,
                        confirmButtonText: 'Xác nhận',
                        cancelButtonText: 'Hủy bỏ'
                    }).then(async (result) => {
                        if (!result.isConfirmed) return;
                
                        $scope.isProcessing = true;
                
                        try {
                            // 🔍 Chuỗi kiểm tra dữ liệu
                            const isValidUser = await checkUserStatus();
                            if (!isValidUser) {
                                throw new Error('Tài khoản bị khóa.');
                            }
                
                            await checkVoucher();             // Kiểm tra mã giảm giá
                            // await checkupdategiatienpai(); // Nếu cần cập nhật giá tạm thời
                            await checkProductStock();        // Kiểm tra tồn kho
                            await checkProductStatus();       // Kiểm tra trạng thái sản phẩm
                            await checkProductPrices();       // Kiểm tra giá sản phẩm có thay đổi không
                
                            // ✅ Tất cả kiểm tra ok → Gửi đơn hàng
                            const response = await $http.post("http://localhost:8080/api/nguoi_dung/hoa_don/them_thong_tin_nhan_hang", orderData);
                            const data = response.data;
                
                            if (!data || data.error || !data.maHoaDon || !data.idHoaDon) {
                                throw new Error(data?.error || "Dữ liệu trả về không hợp lệ.");
                            }
                
                            localStorage.setItem("maHoaDon", data.maHoaDon);
                            localStorage.setItem("idHoaDon", data.idHoaDon);
                
                            Swal.fire({
                                icon: 'info',
                                title: 'Đang xử lý thanh toán...',
                                text: 'Vui lòng chờ trong giây lát.',
                                timer: 3000,
                                timerProgressBar: true,
                                showConfirmButton: false,
                                allowOutsideClick: false,
                                didOpen: () => Swal.showLoading(),
                                willClose: () => {
                                    $window.location.href = "/#!Thanhcong";
                                    $scope.cart = [];
                                    $scope.isOverlayVisible = false;
                
                                    $http.post(`http://localhost:8080/api/nguoi_dung/email/send?recipientEmail=${$scope.userInfo.email}`, orderData)
                                        .then(() => console.log("Email đã gửi"))
                                        .catch(err => console.error("Lỗi gửi email:", err));
                                }
                            });
                
                        } catch (error) {
                            console.error("Lỗi xử lý:", error);
                            Swal.fire({
                                icon: 'error',
                                title: 'Lỗi',
                                text: error.message || 'Đã xảy ra lỗi. Vui lòng thử lại!',
                                confirmButtonText: 'Đồng ý'
                            });
                        } finally {
                            $scope.isProcessing = false;
                        }
                    });
                } else {
                    // Nếu không chọn phương thức thanh toán, báo lỗi
                    Swal.fire({
                        icon: 'warning',
                        title: 'Chưa chọn phương thức thanh toán',
                        text: 'Vui lòng chọn phương thức thanh toán để tiếp tục!',
                        confirmButtonText: 'OK'
                    });
                }





            })
    }




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
    // Hàm gọi API lấy danh sách tỉnh thành
    function getCities() {
        if ($scope.selectedCity && $scope.selectedCity.id) {
            console.log('Province ID:', $scope.selectedCity.id);
        } else {
            console.warn('Chưa chọn tỉnh thành.');
        }

        $http.get("http://127.0.0.1:8080/api/nguoi_dung/address/provinces")
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

        if ($scope.selectedCity && $scope.selectedCity.id) {
            console.log('ID tỉnh vừa chọn:', $scope.selectedCity.id);
            localStorage.setItem("idTinh", JSON.stringify($scope.selectedCity.id));
            $http.get("http://127.0.0.1:8080/api/nguoi_dung/address/districts/" + $scope.selectedCity.id)
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

        if ($scope.selectedDistrict && $scope.selectedDistrict.id) {
            console.log('ID huyện vừa chọn:', $scope.selectedDistrict.id);
            localStorage.setItem("idHuyen", JSON.stringify($scope.selectedDistrict.id));
            $http.get("http://127.0.0.1:8080/api/nguoi_dung/address/wards/" + $scope.selectedDistrict.id)
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
        localStorage.setItem("idXa", JSON.stringify($scope.selectedWard.id));
        // Lấy thông tin cần thiết
        const cityCode = $scope.selectedCity.id;      // Mã tỉnh/thành phố
        const districtCode = $scope.selectedDistrict.id; // Mã quận/huyện
        const wardCode = $scope.selectedWard.id;      // Mã phường/xã

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
    // Hàm lưu tự động
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

    // Tự động tải dữ liệu từ localStorage khi trang load
    $scope.loadShippingData = function () {
        const storedData = JSON.parse(localStorage.getItem("shippingData"));
        if (storedData) {
            $scope.userInfo = {
                email: storedData.email || '',
                tenNguoiDung: storedData.name || '',
                sdt: storedData.phone || '',
                diaChi: storedData.address || '',
            };
            console.log("Đã tải thông tin từ localStorage:", $scope.userInfo);
        }
    };

    // Gọi hàm load dữ liệu ngay khi trang load
    $scope.loadShippingData();

    // Theo dõi mọi thay đổi trong userInfo và tự động lưu
    $scope.$watch('userInfo', function (newVal, oldVal) {
        if (newVal !== oldVal) {
            $scope.autoSave();
        }
    }, true);


    // Gọi API lấy danh sách tỉnh thành khi trang được tải
    getCities();
    $scope.dsvoucher();
    $scope.getVoucher();

    if (user) $scope.getUserInfo(iduser); $scope.getCartItems();

};