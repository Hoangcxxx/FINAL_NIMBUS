window.UserController = function ($scope, $http, $location, $rootScope) {
    // Khởi tạo username từ localStorage (nếu có)


    // Hàm điều hướng về trang chủ
    $scope.goToHome = function () {
        $location.path('/'); // Điều chỉnh lại đường dẫn nếu cần
    };

    // Hàm đăng nhập
    $scope.login = function () {
        const data = {
            email: $scope.email,
            matKhau: $scope.password
        };

        $http.post("http://localhost:8080/api/auth/login", data)
            .then(function (response) {
                console.log("Dữ liệu đăng nhập:", data);
                console.log("Phản hồi từ server:", response.data);
                if (response.data.tenNguoiDung) {
                    // Cập nhật username
                    $scope.username = response.data.tenNguoiDung;
                    console.log("Tên người dùng:", $scope.username);
                    // Lưu tên người dùng vào localStorage
                    localStorage.setItem("username", $scope.username);
                    alert("Đăng nhập thành công!");
                    $rootScope.username = localStorage.getItem("username");
                    // Điều hướng về trang chính
                    $location.path('/_header'); // Điều chỉnh lại đường dẫn nếu cần
                } else {
                    alert("Đăng nhập thất bại.");
                }
            })
            .catch(function (error) {
                // Xử lý lỗi từ server
                if (error.status === 401) {
                    alert("Tên người dùng hoặc mật khẩu không đúng.");
                } else {
                    alert("Đã xảy ra lỗi. Vui lòng thử lại sau.");
                }
            });
    };

    // Hàm đăng xuất
    $scope.logout = function () {
        $scope.username = null;
        localStorage.removeItem("username");
        alert("Đăng xuất thành công!");
        $location.path('/#!user'); // Điều chỉnh lại đường dẫn nếu cần
    };

    // Kiểm tra nếu đã đăng nhập
    $scope.isLoggedIn = function () {
        $scope.username = localStorage.getItem("username");
        return !!$scope.username; // Trả về true nếu username tồn tại
    };

    // Gọi hàm kiểm tra khi khởi tạo controller
    $scope.isLoggedIn();

};
