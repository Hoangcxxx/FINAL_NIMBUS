window.UserController = function ($scope, $http, $route, $window) {
    // Đăng nhập
    $scope.dangNhap = function () {
        var loginData = {
            email: $scope.email,
            matKhau: $scope.matKhau
        };

        // Gửi yêu cầu đăng nhập
        $http.post("http://localhost:8080/api/nguoi_dung/dang_nhap", loginData)
            .then(function (response) {
                // Nếu đăng nhập thành công
                $scope.infoUser = response.data;  // Lưu thông tin người dùng vào scope
                alert("Đăng nhập thành công!");

                // Lưu thông tin người dùng vào localStorage để sử dụng trên các trang khác nếu cần
                localStorage.setItem("user", JSON.stringify($scope.infoUser));

                // Hiển thị tên người dùng trong giao diện
                $scope.tenNguoiDung = $scope.infoUser.tenNguoiDung;  // Lấy tên người dùng từ thông tin trả về

                // Reset form đăng nhập
                $scope.email = ""; // Xóa email trong form
                $scope.matKhau = ""; // Xóa mật khẩu trong form

                // Chuyển hướng đến trang chủ sau khi đăng nhập thành công
                $window.location.href = "#/";  // Chuyển hướng về trang chủ
                // Tải lại trang giống như nút reload của Google
                $window.location.reload();  // Reload lại trang hoàn toàn từ server (bỏ cache)
            }, function (error) {
                // Nếu có lỗi
                alert("Đăng nhập thất bại: " + (error.data ? error.data.message : "Lỗi không xác định"));
            });
    };

    // Đăng ký tài khoản
    $scope.dangKy = function () {
        var registerData = {
            tenNguoiDung: $scope.tenNguoiDung,
            email: $scope.email,
            matKhau: $scope.matKhau,
            vaiTro: { idVaiTro: 2 } // Vai trò mặc định là "Khách hàng" (id_vai_tro = 2)
        };

        // Gửi yêu cầu đăng ký đến API
        $http.post("http://localhost:8080/api/nguoi_dung/dang_ky", registerData)
            .then(function (response) {
                // Nếu đăng ký thành công
                alert("Đăng ký thành công! Hãy đăng nhập.");

                // Chuyển hướng người dùng đến trang đăng nhập
                window.location.href = "#!user";
            }, function (error) {
                // Nếu có lỗi trong quá trình đăng ký
                alert("Đăng ký thất bại: " + error.data.message);
            });
    };
};
