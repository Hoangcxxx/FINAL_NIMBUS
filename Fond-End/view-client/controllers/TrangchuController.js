window.TrangchuController = function ($scope, $http) {
    $scope.dsSanPham = [];
    $scope.dsSanPhamGiamGia = [];

    // Hàm lấy dữ liệu sản phẩm từ API
    function fetchData(url, target) {
        $http({
            method: 'GET',
            url: url
        }).then(function (response) {
            $scope[target] = response.data;
            console.log($scope.dsSanPham);

        }, function (error) {
            console.error('Error fetching data:', error);
        });
    }
    fetchData('http://localhost:8080/api/san_pham', 'dsSanPham');
    fetchData('http://localhost:8080/api/san_pham/san_pham_giam_gia', 'dsSanPhamGiamGia');


    $scope.login = function() {
        var userCredentials = {
            email: $scope.email,
            matKhau: $scope.password
        };

        $http.post('http://localhost:8080/api/auth/login', userCredentials)
            .then(function(response) {
                // Lưu JWT token và tên người dùng vào localStorage
                localStorage.setItem('jwtToken', response.data.token); 
                localStorage.setItem('tenNguoiDung', response.data.tenNguoiDung); 
                alert('Đăng nhập thành công!');
                // Chuyển hướng đến trang chủ
                $window.location.href = '/index.html';
            })
            .catch(function() {
                $scope.errorMessage = 'Đăng nhập thất bại.';
            });
    };

};