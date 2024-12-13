window.UserController = function ($scope, $http, $route, $window) {
    // Đăng nhập
    $scope.dangNhap = function () {
        var loginData = {
            email: $scope.email,
            matKhau: $scope.matKhau
        };
    
        $http.post("http://localhost:8080/api/nguoi_dung/dang_nhap", loginData)
        .then(function (response) {
            // Lưu thông tin người dùng vào scope
            $scope.infoUser = response.data;
    
            // Kiểm tra trạng thái tài khoản
            if (!response.data.trangThai) { // Nếu tài khoản bị khóa
                Swal.fire({
                    title: 'Tài khoản bị khóa',
                    text: 'Tài khoản của bạn đã bị khóa, không thể đăng nhập.',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
                return; // Dừng lại nếu tài khoản bị khóa
            }
    
            // Hiển thị thông báo đăng nhập thành công
            Swal.fire({
                icon: 'success',
                title: 'Đăng nhập thành công!',
                html: `<p>Xin chào, <b>${response.data.tenNguoiDung}</b>!</p>`,
                confirmButtonText: 'Về trang chủ'
            }).then(() => {
                // Lưu thông tin người dùng vào localStorage
                localStorage.setItem("user", JSON.stringify($scope.infoUser));
    
                // Cập nhật giao diện hiển thị tên người dùng
                $scope.tenNguoiDung = $scope.infoUser.tenNguoiDung;
    
                // Reset form đăng nhập
                $scope.email = "";
                $scope.matKhau = "";
    
                // Chuyển hướng về trang chủ hoặc trang khác sau khi đăng nhập thành công
                $window.location.href = "#/"; // Hoặc thay đổi thành đường dẫn mong muốn
                $window.location.reload(); // Tải lại trang để áp dụng trạng thái đăng nhập
            });
        }, function (error) {
            // Xử lý lỗi khi đăng nhập thất bại
            Swal.fire({
                icon: 'error',
                title: 'Đăng nhập thất bại!',
                text: error.data && error.data.message 
                    ? `Lỗi: ${error.data.message}` 
                    : 'Đã xảy ra lỗi không xác định. Vui lòng thử lại!',
                confirmButtonText: 'Thử lại'
            });
        });    
    };
    
    // Đăng ký tài khoản
    $scope.dangKy = function () {
        var registerData = {
            tenNguoiDung: $scope.tenNguoiDung,
            email: $scope.email,
            matKhau: $scope.matKhau,
            vaiTro: { idVaiTro: 2 } // Vai trò mặc định là "Khách hàng"
        };
    
        // Gửi yêu cầu đăng ký
        $http.post("http://localhost:8080/api/nguoi_dung/dang_ky", registerData)
            .then(function (response) {
                // Thông báo khi đăng ký thành công
                Swal.fire({
                    icon: 'success',
                    title: 'Đăng ký thành công!',
                    html: '<p>Bạn đã tạo tài khoản thành công. Hãy đăng nhập để bắt đầu trải nghiệm!</p>',
                    confirmButtonText: 'Đăng nhập ngay'
                }).then(() => {
                    // Chuyển hướng người dùng đến trang đăng nhập
                    $window.location.href = "#!user";
                });
            }, function (error) {
                // Xử lý lỗi đăng ký
                Swal.fire({
                    icon: 'error',
                    title: 'Đăng ký thất bại!',
                    text: error.data && error.data.message 
                        ? `Lỗi: ${error.data.message}` 
                        : 'Đã xảy ra lỗi không xác định. Vui lòng thử lại!',
                    confirmButtonText: 'Thử lại'
                });
            });
    };
    
};
