window.TrangchuController = function ($scope, $http, $window) {
    // Khởi tạo các biến trong scope
    $scope.dsSanPham = [];
    $scope.email = '';
    $scope.password = '';
    $scope.successMessage = '';
    $scope.errorMessage = '';
    $scope.forgotPasswordEmail = '';
    $scope.tenNguoiDung = localStorage.getItem('tenNguoiDung') || '';

    // Lấy dữ liệu sản phẩm từ API
    function fetchData(url, target) {
        $http({
            method: 'GET',
            url: url
        }).then(function (response) {
            $scope[target] = response.data;
        }).catch(function (error) {
            $scope.errorMessage = 'Không thể tải dữ liệu sản phẩm.';
        });
    }

    // Gọi hàm lấy dữ liệu sản phẩm
    fetchData('http://localhost:8080/api/san_pham', 'dsSanPham');

    // Mở modal đăng nhập/đăng ký
    $scope.openAuthModal = function () {
        if (localStorage.getItem('jwtToken')) {
            alert('Bạn đã đăng nhập.');
            return;
        }
        $scope.email = '';
        $scope.password = '';
        $('#authModal').modal('show');
    };

    $scope.login = function () {
        var userCredentials = {
            email: $scope.email,
            matKhau: $scope.password
        };

        $http.post('http://localhost:8080/api/auth/login', userCredentials)
            .then(function (response) {
                var token = response.data.accessToken;

                if (response.data.tenNguoiDung) {
                    $scope.tenNguoiDung = response.data.tenNguoiDung; // Cập nhật tên người dùng trong $scope
                    localStorage.setItem('tenNguoiDung', $scope.tenNguoiDung);
                } else {
                    $scope.tenNguoiDung = '';
                    localStorage.removeItem('tenNguoiDung');
                }

                localStorage.setItem('jwtToken', token);
                alert('Đăng nhập thành công!');
                $scope.email = '';
                $scope.password = '';
                $window.location.href = '/'; // Điều hướng về trang chủ
            })
            .catch(function (error) {
                $scope.errorMessage = 'Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.';
                $scope.tenNguoiDung = '';
                localStorage.removeItem('tenNguoiDung');
            });
    };

    // Đăng ký
    $scope.register = function () {
        var newUser = {
            tenNguoiDung: $scope.tenNguoiDung,
            email: $scope.registerEmail,
            matKhau: $scope.registerPassword,
            sdtNguoiDung: $scope.sdtNguoiDung,
            diaChi: $scope.diaChi,
            gioiTinh: $scope.gioiTinh
        };

        $http.post('http://localhost:8080/api/auth/register', newUser)
            .then(function (response) {
                $scope.successMessage = 'Đăng ký thành công! Vui lòng kiểm tra email để xác thực.';
                $scope.tenNguoiDung = '';
                $scope.registerEmail = '';
                $scope.registerPassword = '';
                $scope.sdtNguoiDung = '';
                $scope.diaChi = '';
                $scope.gioiTinh = '';
            }).catch(function (error) {
                $scope.errorMessage = error.data?.message || 'Đăng ký thất bại.';
            });
    };

    // Quên mật khẩu
    $scope.forgotPassword = function () {
        var email = $scope.forgotPasswordEmail;

        $http.post('http://localhost:8080/api/auth/forgot-password', { email: email })
            .then(function (response) {
                $scope.successMessage = 'Kiểm tra email để khôi phục mật khẩu.';
                $scope.forgotPasswordEmail = '';
            }).catch(function (error) {
                $scope.errorMessage = error.data?.message || 'Có lỗi khi gửi yêu cầu khôi phục mật khẩu.';
            });
    };
    // dang xuất
    $scope.logout = function () {
        localStorage.removeItem('tenNguoiDung');
        localStorage.removeItem('jwtToken');
        $scope.tenNguoiDung = ''; // Cập nhật lại trong $scope
        alert('Đăng xuất thành công!');
        $window.location.href = '/'; // Điều hướng về trang chủ
    };


};
