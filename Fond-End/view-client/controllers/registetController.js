window.registerController = function($scope, $http) {
    $scope.user = {}; 

    $scope.registerUser = function() {
      
        if (!$scope.user.agreeTerms) {
            alert("Please agree to the terms & conditions");
            return;
        }

        $http.post('http://localhost:8080/api/auth/register', $scope.user)
            .then(function(response) {
                alert("Registration successful");
                $scope.user = {}; // Xóa dữ liệu form sau khi đăng ký thành công
            }, function(error) {
                alert("Registration failed: " + (error.data.message || "Unknown error"));
                $window.location.href = "/#!user";
            });
         
    };
};
