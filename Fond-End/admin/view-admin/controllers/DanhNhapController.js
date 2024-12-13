window.DanhNhapController = function ($scope, $http, $location, $window) {

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

                    // Hiển thị thông báo đăng nhập thành công và tải lại trang sau khi nhấn OK
                    Swal.fire({
                        icon: 'success',
                        title: 'Đăng nhập thành công!',
                        text: 'Chào mừng bạn đến với hệ thống!',
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            $window.location.href = "#/";
                            $window.location.reload(); // Tải lại trang sau khi nhấn OK
                        }
                    });
                } else {
                    // Nếu đăng nhập không thành công
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi đăng nhập',
                        text: 'Email hoặc mật khẩu không đúng!',
                        confirmButtonText: 'Thử lại'
                    });
                }
            })
            .catch(function (error) {
                // Xử lý lỗi khi gửi yêu cầu đăng nhập
                console.error('Đăng nhập thất bại', error);
                Swal.fire({
                    icon: 'error',
                    title: 'Đăng nhập thất bại',
                    text: 'Có lỗi xảy ra, vui lòng thử lại!',
                    confirmButtonText: 'Thử lại'
                });
            });
    };

    // Gửi yêu cầu đăng xuất đến API (nếu cần)

};
