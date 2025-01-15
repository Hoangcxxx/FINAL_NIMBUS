window.UserController = function ($scope, $http, $route, $window) {
    // Đăng nhập
    $scope.dangNhap = function () {
        if (!$scope.email || !$scope.matKhau) {
            let message = !$scope.email && !$scope.matKhau 
                ? 'Email và mật khẩu không được để trống.' 
                : !$scope.email 
                ? 'Email không được để trống.' 
                : 'Mật khẩu không được để trống.';
        
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: message,
                confirmButtonText: 'OK'
            });
            return; // Dừng lại nếu các trường không hợp lệ
        }
        

        // Kiểm tra định dạng email (tùy chỉnh theo yêu cầu)
        var emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
        if (!emailRegex.test($scope.email)) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Email không hợp lệ. Vui lòng nhập lại.',
                confirmButtonText: 'OK'
            });
            return; // Dừng lại nếu email không hợp lệ
        }

        var loginData = {
            email: $scope.email,
            matKhau: $scope.matKhau
        };

        $http.post("http://localhost:8080/api/nguoi_dung/dang_nhap", loginData)
            .then(function (response) {
                // Xử lý khi đăng nhập thành công
                $scope.infoUser = response.data;
                // Kiểm tra trạng thái tài khoản
                if (!response.data.trangThai) {
                    Swal.fire({
                        title: 'Tài khoản bị khóa',
                        text: 'Tài khoản của bạn đã bị khóa, không thể đăng nhập.',
                        icon: 'error',
                        confirmButtonText: 'OK'
                    });
                    return;
                }

                Swal.fire({
                    icon: 'success',
                    title: 'Đăng nhập thành công!',
                    html: `<p>Xin chào, <b>${response.data.tenNguoiDung}</b>!</p>`,
                    confirmButtonText: 'Về trang chủ'
                }).then(() => {
                    localStorage.setItem("user", JSON.stringify($scope.infoUser));
                    $scope.tenNguoiDung = $scope.infoUser.tenNguoiDung;
                    $scope.email = "";
                    $scope.matKhau = "";
                    $window.location.href = "#/";
                    $window.location.reload();
                });
            }, function (error) {
                Swal.fire({
                    icon: 'error',
                    title: 'Đăng nhập thất bại!',
                    text: error.data && error.data.message ? `Lỗi: ${error.data.message}` : 'Tài Khoản Chưa Đăng Ký Hoặc Sai Mật Khẩu!',
                    confirmButtonText: 'Thử lại'
                });
            });
    };


    // Đăng ký tài khoản
    $scope.dangKy = function () {
        if (!$scope.tenNguoiDung || !$scope.email || !$scope.matKhau) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Tên người dùng, email và mật khẩu không được để trống.',
                confirmButtonText: 'OK'
            });
            return; // Dừng lại nếu các trường không hợp lệ
        }

        // Kiểm tra định dạng email
        var emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
        if (!emailRegex.test($scope.email)) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Email không hợp lệ. Vui lòng nhập lại.',
                confirmButtonText: 'OK'
            });
            return; // Dừng lại nếu email không hợp lệ
        }

        var registerData = {
            tenNguoiDung: $scope.tenNguoiDung,
            email: $scope.email,
            matKhau: $scope.matKhau,
            vaiTro: { idVaiTro: 2 } // Vai trò mặc định là "Khách hàng"
        };

        $http.post("http://localhost:8080/api/nguoi_dung/dang_ky", registerData)
            .then(function (response) {
                Swal.fire({
                    icon: 'success',
                    title: 'Đăng ký thành công!',
                    html: '<p>Bạn đã tạo tài khoản thành công. Hãy đăng nhập để bắt đầu trải nghiệm!</p>',
                    confirmButtonText: 'Đăng nhập ngay'
                }).then(() => {
                    $window.location.href = "#!user";
                });
            }, function (error) {
                Swal.fire({
                    icon: 'error',
                    title: 'Đăng ký thất bại!',
                    text: error.data && error.data.message ? `Lỗi: ${error.data.message}` : 'Đã xảy ra lỗi không xác định. Vui lòng thử lại!',
                    confirmButtonText: 'Thử lại'
                });
            });
    };

};
