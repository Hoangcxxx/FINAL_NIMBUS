window.registetController = function($scope, $http) {
    $scope.user = {}; // Bind form fields here
    
    $scope.registerUser = function() {
        $http.post('/api/auth/register', $scope.user)
            .then(function(response) {
                alert("Registration successful");
                $scope.user = {}; // Reset form after success
            }, function(error) {
                alert("Registration failed");
            });
    };
};
