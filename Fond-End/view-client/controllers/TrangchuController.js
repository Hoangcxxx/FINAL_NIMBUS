window.TrangchuController = function ($scope, $http, $window) {
    // Khởi tạo biến trong scope
    $scope.dsSanPham = [];
    $scope.tenNguoiDung = localStorage.getItem('tenNguoiDung') || '';
    
    // Lấy dữ liệu sản phẩm từ API
    function fetchData(url, target) {
        $http.get(url)
            .then(function (response) {
                $scope[target] = response.data;
            })
            .catch(function (error) {
                $scope.errorMessage = 'Không thể tải dữ liệu sản phẩm.';
            });
    }

    // Gọi hàm để lấy sản phẩm
    fetchData('http://localhost:8080/api/san_pham', 'dsSanPham');

    // Mở modal đăng nhập/đăng ký
    $scope.openAuthModal = function () {
        if ($scope.tenNguoiDung) {
            alert('Bạn đã đăng nhập.');
            return;
        }
        $('#authModal').modal('show');
    };

    // Đăng nhập người dùng
    $scope.login = function () {
        const userCredentials = {
            email: $scope.email,
            matKhau: $scope.password
        };

        $http.post('http://localhost:8080/api/auth/login', userCredentials)
            .then(function (response) {
                const token = response.data.accessToken;

                if (response.data.tenNguoiDung) {
                    $scope.tenNguoiDung = response.data.tenNguoiDung; // Cập nhật tên người dùng trong $scope
                    localStorage.setItem('tenNguoiDung', $scope.tenNguoiDung);
                }

                localStorage.setItem('jwtToken', token);
                alert('Đăng nhập thành công!');
                $scope.email = '';
                $scope.password = '';
                $('#authModal').modal('hide'); // Ẩn modal sau khi đăng nhập
            })
            .catch(function (error) {
                $scope.errorMessage = 'Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.';
                $scope.tenNguoiDung = '';
                localStorage.removeItem('tenNguoiDung');
            });
    };

    // Xử lý quên mật khẩu
    $scope.forgotPassword = function () {
        const forgotCredentials = {
            email: $scope.forgotPasswordEmail
        };

        $http.post('http://localhost:8080/api/auth/forgot-password', forgotCredentials)
            .then(function (response) {
                $scope.successMessage = 'Một email đã được gửi để khôi phục mật khẩu của bạn.';
                $scope.forgotPasswordEmail = '';
            })
            .catch(function (error) {
                $scope.errorMessage = 'Có lỗi xảy ra. Vui lòng thử lại.';
            });
    };

    // Đăng xuất người dùng
    $scope.logout = function () {
        localStorage.removeItem('tenNguoiDung');
        localStorage.removeItem('jwtToken');
        $scope.tenNguoiDung = ''; // Cập nhật trong $scope
        alert('Đăng xuất thành công!');
        $window.location.href = '/'; // Chuyển hướng về trang chính
    };
};
