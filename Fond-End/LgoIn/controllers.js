var app = angular.module('loginApp', []);

app.controller('LoginController', function ($scope, $http, $window) {
    $scope.login = function () {
        var userCredentials = {
            email: $scope.email,
            matKhau: $scope.password,
         
        };

        $http.post('http://localhost:8080/api/auth/login', userCredentials)
            .then(function (response) {
                // Lưu JWT token và tên người dùng vào localStorage
                localStorage.setItem('jwtToken', response.data.accessToken);
                localStorage.setItem('tenNguoiDung', response.data.tenNguoiDung);
                alert('Đăng nhập thành công!');

                // Lấy và in ra token và tên người dùng từ localStorage
                var token = localStorage.getItem('jwtToken');
                var tenNguoiDung = localStorage.getItem('tenNguoiDung');

                console.log('Token:', token);
                console.log('Tên người dùng:', tenNguoiDung);
                console.log("Loi")
                // Chuyển hướng đến trang chủ
                // $window.location.href = '/index.html';
            })
            .catch(function () {
                $scope.errorMessage = 'Đăng nhập thất bại.';
            });
    };
});
