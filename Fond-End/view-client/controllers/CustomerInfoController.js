window.CustomerInfoController = function ($scope) {
    // Retrieve user information from localStorage
    $scope.tenNguoiDung = localStorage.getItem('tenNguoiDung') || '';
    $scope.email = localStorage.getItem('email') || ''; // Add more fields as necessary

    // You can fetch more detailed user data from your API if needed
    $scope.loadUserData = function () {
        if ($scope.tenNguoiDung) {
            // Example: Fetch user data based on their email
            $http.get(`http://localhost:8080/api/user/${$scope.email}`)
                .then(function (response) {
                    if (response.data.success) {
                        // Assuming response.data contains the user info
                        $scope.userData = response.data.user; 
                    } else {
                        $scope.errorMessage = response.data.message;
                    }
                })
                .catch(function () {
                    $scope.errorMessage = 'Không thể tải thông tin người dùng.';
                });
        }
    };

    // Call loadUserData to fetch user info
    $scope.loadUserData();
};
