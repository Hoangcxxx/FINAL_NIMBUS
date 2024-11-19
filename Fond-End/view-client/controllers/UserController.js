window.UserController = function ($scope, $http, $location, $rootScope) {
    $scope.login = function () {
        const data = {
            email: $scope.email,
            matKhau: $scope.password
        };

        $http.post("http://localhost:8080/api/auth/login", data)
            .then(function (response) {
                const userData = response.data;

                if (userData && userData.tenNguoiDung) {
                    // Lưu thông tin người dùng và token vào localStorage
                    $rootScope.username = userData.tenNguoiDung;
                    $rootScope.infoUser = true; // Cập nhật trạng thái đăng nhập
                    localStorage.setItem("user", JSON.stringify(userData));

                    if (userData.accessToken) {
                        localStorage.setItem("accessToken", userData.accessToken);
                    }

                    // Hiển thị thông báo và chuyển hướng
                    Swal.fire({
                        icon: "success",
                        title: "Đăng nhập thành công",
                        showConfirmButton: false,
                        timer: 1500
                    }).then(() => {
                        $location.path("/thong-tin-tai-khoan");
                        $scope.$apply(); // Cập nhật $location
                    });
                } else {
                    // Xử lý trường hợp thiếu dữ liệu trong phản hồi
                    Swal.fire({
                        icon: "error",
                        title: "Đăng nhập thất bại",
                        text: "Vui lòng thử lại."
                    });
                }
            })
            .catch(function (error) {
                // Xử lý lỗi từ API
                let errorMessage = "Có lỗi xảy ra. Vui lòng thử lại.";
                if (error.status === 401) {
                    errorMessage = "Sai tên đăng nhập hoặc mật khẩu.";
                }
                Swal.fire({
                    icon: "error",
                    title: "Đăng nhập thất bại",
                    text: errorMessage
                });
            });
    };

    // Xử lý đăng xuất
    $scope.dangXuat = function () {
        $rootScope.username = null;
        $rootScope.infoUser = false;
        localStorage.removeItem("user");
        localStorage.removeItem("accessToken");

        Swal.fire({
            icon: "success",
            title: "Đăng xuất thành công",
            showConfirmButton: false,
            timer: 1500
        }).then(() => {
            $location.path("/");
            $scope.$apply(); // Cập nhật $location
        });
    };
};
