window.ThanhtoanController = function ($scope, $http) {

    $scope.provinces = [];
    $scope.districts = [];
    $scope.wards = [];
    $scope.cart = [];
    $scope.totalAmount = 0;
    $scope.userInfo = {};
    $scope.vouchers = [];
    $scope.selectedVoucher = null;
    $scope.paymentStatus = ''; // Thông báo trạng thái thanh toán

    // Lấy thông tin người dùng
    $scope.getUserInfo = function (userId) {
        $http.get('http://localhost:8080/api/auth/user/' + userId)
            .then(response => {
                $scope.userInfo = response.data;
                // Gán thông tin người dùng vào phần thông tin nhận hàng
                Object.assign($scope.shippingInfo, {
                    email: $scope.userInfo.email,
                    name: $scope.userInfo.name,
                    phone: $scope.userInfo.phone,
                    address: $scope.userInfo.address
                });
            })
            .catch(error => console.error("Lỗi khi lấy thông tin người dùng:", error));
    };

    // Lấy danh sách sản phẩm trong giỏ hàng
    $scope.getCartItems = function () {
        const cartId = 3; // ID giỏ hàng giả định
        $http.get(`http://localhost:8080/api/giohang/${cartId}`)
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

    // Đặt hàng
    $scope.placeOrder = function () {
        // Kiểm tra giỏ hàng có trống hay không
        if ($scope.cart.length === 0) {
            alert("Giỏ hàng của bạn đang trống!");
            return;
        }

        // Dữ liệu đơn hàng
        const orderData = {
            shippingInfo: $scope.shippingInfo, // Thông tin người nhận
            cartItems: $scope.cart, // Sản phẩm trong giỏ
            totalAmount: $scope.totalAmount, // Tổng tiền
            voucher: $scope.selectedVoucher, // Mã giảm giá
            paymentMethod: $scope.selectedPaymentMethod // Phương thức thanh toán
        };

        // Gửi dữ liệu đơn hàng đến API
        $http.post("http://localhost:8080/api/them_thong_tin_nhan_hang", orderData)
            .then(response => {
                alert("Đặt hàng thành công!");
                $scope.paymentStatus = "Đặt hàng thành công!";
                // Làm sạch giỏ hàng sau khi đặt
                $scope.cart = []; 
            })
            .catch(error => {
                console.error("Lỗi khi đặt hàng:", error);
                $scope.paymentStatus = "Có lỗi xảy ra khi đặt hàng. Vui lòng thử lại.";
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

    // Khởi tạo: Lấy thông tin người dùng, giỏ hàng và tỉnh thành
    $scope.getUserInfo(1); // Thay 1 bằng id người dùng hiện tại
    $scope.getCartItems();
    $scope.getProvinces();
};
