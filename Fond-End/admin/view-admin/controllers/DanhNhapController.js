window.DanhNhapController = function ($scope, $http, $location,$window) {

    // Hàm đăng nhập
    $scope.dangNhap = function () {
        // Gửi yêu cầu đăng nhập đến API
        $http.post('http://localhost:8080/api/admin/dang_nhap', {
            email: $scope.email,
            matKhau: $scope.matKhau
        })
            .then(function (response) {
                // Kiểm tra kết quả từ API
                if (response.data) {
                    // Lưu thông tin người dùng vào localStorage
                    localStorage.setItem('user', JSON.stringify(response.data));

                    // Lưu trạng thái đăng nhập
                    $scope.infoUser = response.data;
                    $scope.tenNguoiDung = response.data.tenNguoiDung;
                    $scope.vaiTro = response.data.vaiTro.ten;
                    $scope.isAdmin = response.data.vaiTro.ten === 'Quản trị viên';

                    $window.location.href = "#/";
                    $window.location.reload(); // Tải lại trang để áp dụng trạng thái đăng nhập
                    // Thông báo đăng nhập thành công
                    alert('Đăng nhập thành công!');
                } else {
                    // Nếu đăng nhập không thành công
                    alert('Email hoặc mật khẩu không đúng!');
                }
            })
            .catch(function (error) {
                // Xử lý lỗi khi gửi yêu cầu đăng nhập
                console.error('Đăng nhập thất bại', error);
                alert('Đăng nhập thất bại, vui lòng thử lại!');
            });
    };


    // Gửi yêu cầu đăng xuất đến API


};
