window.TrangchuController = function ($scope, $http, $window) {
    // Initialize variables in scope
    $scope.dsSanPham = [];
    var userId  = $scope.userId
    $scope.email = '';
    $scope.password = '';
    $scope.successMessage = '';
    $scope.errorMessage = '';
    $scope.forgotPasswordEmail = '';
    $scope.tenNguoiDung = localStorage.getItem('tenNguoiDung') || '';
    $scope.userInfo = {};
    $scope.registerEmail = '';
    $scope.registerPassword = '';
    $scope.sdtNguoiDung = '';
    $scope.diaChi = '';
    $scope.gioiTinh = '';

    // Fetch product data from API
    function fetchData(url, target) {
        $http.get(url)
            .then(function (response) {
                $scope[target] = response.data;
            })
            .catch(function () {
                $scope.errorMessage = 'Không thể tải dữ liệu sản phẩm.';
            });
    }


    fetchData('http://localhost:8080/api/san_pham', 'dsSanPham');

    // Open authentication modal
    $scope.openAuthModal = function () {
        if (localStorage.getItem('jwtToken')) {
            alert('Bạn đã đăng nhập.');
            return; // If already logged in, do not open modal
        }
   
        $scope.email = '';
        $scope.password = '';
        $('#authModal').modal('show');
    };

    // Login function
    $scope.login = function () {
        const userCredentials = {
            email: $scope.email,
            matKhau: $scope.password,
            userId: $scope.userId
        };
    
        $http.post('http://localhost:8080/api/auth/login', userCredentials)
            .then(function (response) {
                const token = response.data.accessToken;
                $scope.tenNguoiDung = response.data.tenNguoiDung || '';
                localStorage.setItem('tenNguoiDung', $scope.tenNguoiDung);
                localStorage.setItem('jwtToken', token);
                alert('Đăng nhập thành công!');
    
                return $http.get('http://localhost:8080/api/auth/user/' + $scope.userId, {
                    headers: { 'Authorization': 'Bearer ' + token }
                });                
            })
            .then(function (response) {
                $scope.userInfo = response.data; 
                console.log('Thông tin người dùng:', $scope.userInfo);
                $scope.tenNguoiDung = $scope.userInfo.tenNguoiDung || ''; 
                $scope.email = ''; 
                $scope.password = ''; 
                $window.location.href = '/'; 
            })
            .catch(function (error) {
                $scope.errorMessage = 'Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.';
                localStorage.removeItem('tenNguoiDung');
            });
    };
    
    // Register function
    $scope.register = function () {
        const newUser = {
            tenNguoiDung: $scope.tenNguoiDung,
            email: $scope.registerEmail,
            matKhau: $scope.registerPassword,
            sdtNguoiDung: $scope.sdtNguoiDung,
            diaChi: $scope.diaChi,
            gioiTinh: $scope.gioiTinh
        };

        $http.post('http://localhost:8080/api/auth/register', newUser)
            .then(function () {
                $scope.successMessage = 'Đăng ký thành công! Vui lòng kiểm tra email để xác thực.';
                // Reset registration fields
                $scope.tenNguoiDung = '';
                $scope.registerEmail = '';
                $scope.registerPassword = '';
                $scope.sdtNguoiDung = '';
                $scope.diaChi = '';
                $scope.gioiTinh = '';
            })
            .catch(function (error) {
                $scope.errorMessage = error.data?.message || 'Đăng ký thất bại.';
            });
    };

    // Forgot password function
    $scope.forgotPassword = function () {
        const email = $scope.forgotPasswordEmail;

        $http.post('http://localhost:8080/api/auth/forgot-password', { email: email })
            .then(function () {
                $scope.successMessage = 'Kiểm tra email để khôi phục mật khẩu.';
                $scope.forgotPasswordEmail = ''; // Reset email
            })
            .catch(function (error) {
                $scope.errorMessage = error.data?.message || 'Có lỗi khi gửi yêu cầu khôi phục mật khẩu.';
            });
    };

    // Logout function
    $scope.logout = function () {
        localStorage.removeItem('tenNguoiDung');
        localStorage.removeItem('jwtToken');
        $scope.tenNguoiDung = '';
        $scope.userInfo = {};
        alert('Đăng xuất thành công!');
        $window.location.href = '/';
    };
};
