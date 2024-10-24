window.TrangchuController = function ($scope, $http, $window) {
    // Initialize variables in scope
    $scope.productList = [];
    $scope.email = '';
    $scope.password = '';
    $scope.successMessage = '';
    $scope.errorMessage = '';
    $scope.forgotPasswordEmail = '';
    $scope.username = localStorage.getItem('tenNguoiDung') || '';
    $scope.userInfo = {};
    $scope.registerEmail = '';
    $scope.registerPassword = '';
    $scope.userPhone = '';
    $scope.userAddress = '';
    $scope.userGender = '';

    // Check if user is already logged in
    $scope.isLoggedIn = !!localStorage.getItem('jwtToken');

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
    fetchData('http://localhost:8080/api/san_pham', 'productList');

    // Login function
    $scope.login = function () {
        const userCredentials = {
            email: $scope.email,
            matKhau: $scope.password
        };

        console.log('Đang gửi thông tin đăng nhập:', userCredentials); // Log login credentials

        $http.post('http://localhost:8080/api/auth/login', userCredentials)
            .then(function (response) {
                console.log('Phản hồi từ server:', response.data); // Log server response
                const token = response.data.accessToken;
                $scope.username = response.data.tenNguoiDung || ''; // Update username
                $scope.userId = response.data.idNguoiDung; // Update userId from response

                // Store user information in localStorage
                localStorage.setItem('tenNguoiDung', $scope.username);
                localStorage.setItem('jwtToken', token);
                localStorage.setItem('userId', $scope.userId); // Store userId
                alert('Đăng nhập thành công!');

                window.location.href = ''; 

                // Fetch user information
                return $http.get('http://localhost:8080/api/auth/user/' + $scope.userId, {
                    headers: { 'Authorization': 'Bearer ' + token }
                });
            })
            .then(function (response) {
                $scope.userInfo = response.data;
                console.log('Thông tin người dùng:', $scope.userInfo); // Log user information
                $scope.username = $scope.userInfo.tenNguoiDung || '';
                $scope.email = '';
                $scope.password = '';
                $scope.isLoggedIn = true; // Update logged in status
                
                // Update the view to show the username in the header
                $scope.updateUsername();
            })
            .catch(function (error) {
                console.log('Lỗi đăng nhập:', error); // Log login error
                $scope.errorMessage = error.message || 'Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.';
                localStorage.removeItem('tenNguoiDung');
            });
    };

    // Function to update username display in header
    $scope.updateUsername = function() {
        $scope.username = localStorage.getItem('tenNguoiDung') || '';
    };

    // Logout function
    $scope.logout = function () {
        localStorage.removeItem('tenNguoiDung');
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('userId'); // Clear userId from localStorage
        $scope.username = '';
        $scope.userInfo = {};
        $scope.isLoggedIn = false; // Update logged in status
        alert('Đăng xuất thành công!');
        $window.location.href = '/'; // Redirect after logout
    };
};
