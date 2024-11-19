window.GiohangController = function ($scope, $http, $window, $rootScope) {
    $scope.cart = [];
    $scope.cartItemCount = 0;
    $scope.totalAmount = 0;

    // Lấy thông tin người dùng từ localStorage
    const user = JSON.parse(localStorage.getItem("user"));
    const userId = user ? user.idNguoiDung : null;

    if (!userId) {
        alert("Vui lòng đăng nhập để xem giỏ hàng!");
        return;
    }

    // Lấy danh sách sản phẩm trong giỏ hàng
    $scope.getCartItems = function () {
        $http.get(`http://localhost:8080/api/giohang/user/${userId}/giohang`)
            .then(function (response) {
                $scope.cart = response.data;
                $scope.cartItemCount = $scope.cart.length;

                // Lấy hình ảnh cho từng sản phẩm
                $scope.cart.forEach((item) => {
                    $http.get(`http://localhost:8080/api/hinh_anh/${item.idSanPham}`)
                        .then(function (res) {
                            item.urlAnh = res.data[0]?.urlAnh || "default.jpg"; // Ảnh mặc định nếu không có
                        })
                        .catch(function (err) {
                            console.error("Lỗi khi lấy hình ảnh sản phẩm:", err);
                        });
                });

                // Tính tổng tiền ngay khi load giỏ hàng
                $scope.getTotal();
            })
            .catch(function (error) {
                console.error("Lỗi khi lấy giỏ hàng:", error);
          
            });
    };

    // Xóa sản phẩm khỏi giỏ hàng
    $scope.removeFromCart = function (idSanPhamChiTiet) {
        $http.delete(`http://localhost:8080/api/giohang/delete?idNguoiDung=${userId}&idSanPhamChiTiet=${idSanPhamChiTiet}`)
            .then(function (response) {
                alert(response.data.message || "Sản phẩm đã được xóa khỏi giỏ hàng!");
                $scope.getCartItems(); // Cập nhật giỏ hàng sau khi xóa
            })
            .catch(function (error) {
                console.error("Lỗi xóa sản phẩm:", error);
                alert("Không thể xóa sản phẩm, vui lòng thử lại!");
            });
    };

    $scope.updateCart = function (item) {
        // Tạo đối tượng value chứa thông tin cần cập nhật
        let value = {
            soLuong: item.soLuong,
            donGia: item.giaTien,
            idSanPhamChiTiet: item.idspct,
            thanhTien: item.soLuong * item.giaTien,
        };
    
        // Gửi yêu cầu PUT để cập nhật sản phẩm trong giỏ hàng
        $http.put(`http://localhost:8080/api/giohang/update?idNguoiDung=${userId}`, value, {
            headers: {
                "Content-Type": "application/json",
            }
        })
        
        .then(function (response) {
            console.log("Cập nhật giỏ hàng thành công:", response.data);
            // Cập nhật lại giỏ hàng hoặc làm gì đó sau khi cập nhật thành công
        })
        .catch(function (error) {
            console.error("Lỗi cập nhật sản phẩm trong giỏ hàng:", error);
        });
    };
    

    $scope.getTotal = function () {
        return $scope.cart.reduce((total, item) => total + (item.soLuong * item.giaTien), 0);
    };
    // Chuyển sang trang thanh toán
    $scope.checkout = function () {
        if ($scope.cart.length === 0) {
            alert("Giỏ hàng rỗng, vui lòng thêm sản phẩm trước khi thanh toán!");
            return;
        }
        $window.location.href = "/#!thanh_toan"; // Chuyển hướng đến trang thanh toán
    };

    // Gọi hàm lấy giỏ hàng khi controller được khởi tạo
    $scope.getCartItems();
};
