window.ThongtinkhachhangController = function($scope, $http) {
    // Fake customer data
    $scope.customerData = {
        name: "Nguyễn Văn A",
        email: "email@example.com",
        address: "123 Đường ABC",
        city: "Hà Nội"
    };

    // Initialize the form fields with fake data
    $scope.init = function() {
        $scope.customerName = $scope.customerData.name;
        $scope.customerEmail = $scope.customerData.email;
        $scope.customerAddress = $scope.customerData.address;
        $scope.customerCity = $scope.customerData.city;
    };

    // Call the init function to set the initial values
    $scope.init();

    // Function to update customer details (simulating API call)
    $scope.updateCustomerDetails = function() {
        // Here you can perform any validation or API call
        alert("Thông tin đã được cập nhật:\n" +
              "Tên: " + $scope.customerName + "\n" +
              "Email: " + $scope.customerEmail + "\n" +
              "Địa chỉ: " + $scope.customerAddress + "\n" +
              "Thành phố: " + $scope.customerCity);
        
        // Simulate updating the customer data
        $scope.customerData.name = $scope.customerName;
        $scope.customerData.email = $scope.customerEmail;
        $scope.customerData.address = $scope.customerAddress;
        $scope.customerData.city = $scope.customerCity;
    };
};
