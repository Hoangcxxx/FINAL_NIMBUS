window.TrangchuController = function ($scope, $http, $window) {
    // Initialize variables in scope
    $scope.dsSanPham = [];
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

    // Call the function to fetch product data
    fetchData('http://localhost:8080/api/san_pham', 'dsSanPham');

    // Open authentication modal
    $scope.openAuthModal = function () {
        if (localStorage.getItem('jwtToken')) {
            alert('Bạn đã đăng nhập.');
            return; // If already logged in, do not open modal
        }
        // Reset fields
        $scope.email = '';
        $scope.password = '';
        $('#authModal').modal('show');
    };

    $scope.login = function () {
        const userCredentials = {
            email: $scope.email,
            matKhau: $scope.password
        };
    
        $http.post('http://localhost:8080/api/auth/login', userCredentials)
            .then(function (response) {
                const token = response.data.accessToken;
                $scope.tenNguoiDung = response.data.tenNguoiDung || '';
                localStorage.setItem('tenNguoiDung', $scope.tenNguoiDung);
                localStorage.setItem('jwtToken', token);
                alert('Đăng nhập thành công!');
                console.log("toekn:", token)
                return $http.get('http://localhost:8080/api/auth/user/' + response.data.userId, {
                    headers: { 'Authorization': 'Bearer ' + token }
                });                
            })
            .then(function (response) {
                $scope.userInfo = response.data; // Store the user info
                console.log('Thông tin người dùng:', $scope.userInfo); // For debugging
                // Optionally set more user info to be displayed
                $scope.tenNguoiDung = $scope.userInfo.tenNguoiDung || ''; // Update name to display
                $scope.email = ''; // Clear email field
                $scope.password = ''; // Clear password field
                $window.location.href = '/'; // Redirect to home or desired page
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
