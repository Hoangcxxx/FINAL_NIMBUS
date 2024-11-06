window.UserController = function ($scope, $http, $location, $rootScope) {
    // Kiểm tra nếu đã có username từ localStorage, nếu có thì sử dụng
    $scope.username = localStorage.getItem("username") || null;

    // Cập nhật $rootScope để chia sẻ trạng thái đăng nhập trên toàn bộ ứng dụng
    $rootScope.username = $scope.username;

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

        // Kiểm tra xem thông tin người dùng đã nhập có đầy đủ không
        if (!$scope.email || !$scope.password) {
            alert("Vui lòng nhập đầy đủ thông tin đăng nhập.");
            return;
        }

        // Gửi yêu cầu đăng nhập
        $http.post("http://localhost:8080/api/auth/login", data)
            .then(function (response) {
                console.log("Dữ liệu đăng nhập:", data);
                console.log("Phản hồi từ server:", response.data);

                if (response.data && response.data.tenNguoiDung) {
                    // Cập nhật username từ phản hồi của server
                    $scope.username = response.data.tenNguoiDung;
                    console.log("Tên người dùng:", $scope.username);

                    // Lưu tên người dùng vào localStorage
                    localStorage.setItem("username", $scope.username);

                    // Cập nhật cho $rootScope để có thể sử dụng ở header
                    $rootScope.username = $scope.username;

                    // Đăng nhập thành công, thông báo và điều hướng về trang chủ
                    alert("Đăng nhập thành công!");
                    $location.path('/'); // Hoặc trang bạn muốn chuyển đến
                } else {
                    alert("Đăng nhập thất bại.");
                }
            })
            .catch(function (error) {
                // Xử lý lỗi từ server
                console.error("Lỗi khi đăng nhập:", error);
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
        // Xóa thông tin người dùng
        $scope.username = null;
        localStorage.removeItem("username");
        $rootScope.username = null; // Cập nhật lại $rootScope khi đăng xuất

        // Thông báo đăng xuất thành công
        alert("Đăng xuất thành công!");

        // Điều hướng về trang login (hoặc trang nào bạn muốn)
        $location.path('/login');
    };
};
