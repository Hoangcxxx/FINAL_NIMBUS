window.UserController = function ($scope, $http, $location, $rootScope) {
    // Initialize username from localStorage if already logged in
    $scope.username = localStorage.getItem("username") || null;
    $rootScope.username = $scope.username;

    $scope.login = function () {
        const data = {
            email: $scope.email,
            matKhau: $scope.password
        };

        $http.post("http://localhost:8080/api/auth/login", data)
            .then(function (response) {
                if (response.data.tenNguoiDung) {
                    // Save username and userId in localStorage
                    $scope.username = response.data.tenNguoiDung;
                    $scope.userId = response.data.id;
                    localStorage.setItem("username", $scope.username);
                    localStorage.setItem("userId", $scope.userId); // Save userId

                    alert("Login successful!");
                    $rootScope.username = $scope.username; // Update username in root scope
                } else {
                    alert("Login failed.");
                }
            })
            .catch(function (error) {
                if (error.status === 401) {
                    alert("Incorrect username or password.");
                } else {
                    alert("An error occurred. Please try again later.");
                }
            });
    };

    // Check if user is logged in
    $scope.isLoggedIn = function () {
        return !!$scope.username; // Return true if username exists
    };

    // Logout function
    $scope.dangXuat = function () {
        $http.post("http://localhost:8080/api/nguoi_dung/dang_xuat")
            .then(function (response) {
                // Remove user information from localStorage
                localStorage.removeItem("username");
                localStorage.removeItem("userId");

                // Clear user data and update UI
                $scope.username = null;
                $rootScope.username = null;
                alert("Logout successful!");

                // Redirect to homepage after logout
                $location.path('/'); // Redirect to homepage after logout
            }, function (error) {
                alert("Logout failed: " + (error.data ? error.data.message : "Unknown error"));
            });
    };
};
