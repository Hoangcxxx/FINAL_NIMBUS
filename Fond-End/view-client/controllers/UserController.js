window.UserController = function ($scope, $http, $location, $rootScope) {
    // Khởi tạo username và userId từ localStorage (nếu có)
    $scope.username = localStorage.getItem("username") || null;
    $rootScope.username = $scope.username;
    const userId = localStorage.getItem("userId");  

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
                    // Lưu tên người dùng và id vào localStorage
                    $scope.username = response.data.tenNguoiDung;
                    $scope.userId = response.data.id;  // Lưu userId
                    localStorage.setItem("username", $scope.username);
                    localStorage.setItem("userId", $scope.userId); // Lưu userId
                    alert("Đăng nhập thành công!");

                    $rootScope.username = $scope.username;

                    // Lấy thông tin người dùng sau khi đăng nhập thành công
                    $http.get('http://localhost:8080/api/auth/user/' + $scope.userId)
                        .then(function (userResponse) {
                            $rootScope.userDetails = userResponse.data;
                            console.log('Thông tin người dùng:', userResponse.data);
                            $location.path('/thongtinkhachhang');
                        })
                        .catch(function (error) {
                            console.error("Lỗi khi lấy thông tin người dùng:", error);
                        });
                } else {
                    alert("Đăng nhập thất bại.");
                }
            })
            .catch(function (error) {
                if (error.status === 401) {
                    alert("Tên người dùng hoặc mật khẩu không đúng.");
                } else {
                    alert("Đã xảy ra lỗi. Vui lòng thử lại sau.");
                }
            });
    };

    // Kiểm tra nếu đã đăng nhập
    $scope.isLoggedIn = function () {
        return !!$scope.username; // Trả về true nếu username tồn tại
    };

    // Hàm đăng xuất
    $scope.logout = function () {
        $scope.username = null;
        localStorage.removeItem("username");
        localStorage.removeItem("userId");  // Xóa userId khi đăng xuất
        alert("Đăng xuất thành công!");
        $location.path('/#!user'); // Điều chỉnh lại đường dẫn nếu cần
    };
};
