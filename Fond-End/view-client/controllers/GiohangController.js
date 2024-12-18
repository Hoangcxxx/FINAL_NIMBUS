window.GiohangController = function ($scope, $http, $window) {
    $scope.cart = []; // Khởi tạo mảng giỏ hàng trống
    const userId = 11; // Thay thế bằng ID người dùng thực tế
    const cartId = 3; // Thay thế bằng ID giỏ hàng thực tế
    $scope.cartItemCount = 0;

    $scope.getCartItems = function () {
        $http.get(`http://localhost:8080/api/giohang/${cartId}`)
            .then(function (response) {
                $scope.cart = response.data;
                $scope.cartItemCount = $scope.cart.length; // Cập nhật số lượng sản phẩm trong giỏ hàng

                // Lấy hình ảnh cho từng sản phẩm
                $scope.cart.forEach((element) => {
                    $http.get(`http://localhost:8080/api/hinh_anh/${element.idSanPham}`)
                        .then(function (response) {
                            element.urlAnh = response.data[0]?.urlAnh || ''; // Lấy url hình ảnh
                        })
                        .catch(function (error) {
                            console.error("Lỗi khi lấy hình ảnh sản phẩm:", error);
                        });
                });

                // Kiểm tra xem có sản phẩm mới nào trong localStorage không
                const newCartItem = localStorage.getItem("cartItem");
                if (newCartItem) {
                    const item = JSON.parse(newCartItem);
                    $scope.cart.push(item);
                    $scope.cartItemCount++; // Tăng số lượng sản phẩm khi thêm sản phẩm mới
                    localStorage.removeItem("cartItem");
                    alert(`${item.tenSanPham} đã được thêm vào giỏ hàng!`);
                }
            })
            .catch(function (error) {
                console.error("Error fetching cart items:", error);
            });
    };

    $scope.removeFromCart = function (idSanPhamChiTiet) {
        $http.delete(`http://localhost:8080/api/giohang/delete?idGioHang=${cartId}&idSanPhamChiTiet=${idSanPhamChiTiet}`)
            .then(function (response) {
                alert(response.data.message);
                $scope.getCartItems(); // Lấy lại giỏ hàng sau khi xóa
            })
            .catch(function (error) {
                console.error("Lỗi xóa sản phẩm khỏi giỏ hàng:", error);
            });
    };

    $scope.updateCart = function (item) {
        let value = {
            soLuong: item.soLuong,
            donGia: item.giaTien,
            idSanPhamChiTiet: item.idspct,
            thanhTien: item.soLuong * item.giaTien,
        };
        $http({
            method: "PUT",
            url: `http://localhost:8080/api/giohang/update?idGioHang=${cartId}`,
            data: value,
            headers: {
                "Content-Type": "application/json",
            },
        })
        .then(function (response) {
            console.log("Cập nhật giỏ hàng thành công:", response.data);
        })
        .catch(function (error) {
            console.error("Lỗi cập nhật sản phẩm trong giỏ hàng:", error);
        });
    };

    $scope.getTotal = function () {
        return $scope.cart.reduce((total, item) => total + (item.soLuong * item.giaTien), 0);
    };

    $scope.checkout = function () {
        $window.location.href = "/#!thanh_toan"; // Chuyển hướng sang trang thanh toán
    };

    // Lấy dữ liệu giỏ hàng ban đầu
    $scope.getCartItems(); 
};
