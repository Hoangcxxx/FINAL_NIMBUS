window.ThongtinListController = function ($scope, $http) {
    var iduser = 1; // Giả lập ID người dùng

    // Lấy dữ liệu người dùng từ API
    $http.get('http://localhost:8080/api/auth/user/' + iduser)
        .then(function (response) {
            var user = response.data;
            $scope.customerName = user.tenNguoiDung;
            $scope.customerEmail = user.email;
            $scope.customerAddress = user.diaChi;
            $scope.customerCity = user.thanhPho;
            $scope.customerPhone = user.sdtNguoiDung;
            // $scope.customerRegistrationDate = new Date(user.ngayTao); // Sửa lại biến ngayTao

            // Dữ liệu tạm thời dùng trong modal chỉnh sửa
            $scope.tempCustomerName = $scope.customerName;
            $scope.tempCustomerEmail = $scope.customerEmail;
            $scope.tempCustomerAddress = $scope.customerAddress;
            $scope.tempCustomerCity = $scope.customerCity;
            $scope.tempCustomerPhone = $scope.customerPhone;
        }, function (error) {
            console.error('Lỗi khi lấy dữ liệu người dùng:', error);
        });

    // Mở modal chỉnh sửa thông tin cá nhân
    $scope.openEditModal = function () {
        $scope.tempCustomerName = $scope.customerName;
        $scope.tempCustomerEmail = $scope.customerEmail;
        $scope.tempCustomerAddress = $scope.customerAddress;
        $scope.tempCustomerCity = $scope.customerCity;
        $scope.tempCustomerPhone = $scope.customerPhone;
        $('#editCustomerModal').modal('show'); // Mở modal chỉnh sửa
    };

    // Lưu thông tin cá nhân đã chỉnh sửa
    $scope.saveCustomerDetails = function () {
        $scope.customerName = $scope.tempCustomerName;
        $scope.customerEmail = $scope.tempCustomerEmail;
        $scope.customerAddress = $scope.tempCustomerAddress;
        $scope.customerCity = $scope.tempCustomerCity;
        $scope.customerPhone = $scope.tempCustomerPhone;

        var updatedCustomer = {
            tenNguoiDung: $scope.customerName,
            email: $scope.customerEmail,
            diaChi: $scope.customerAddress,
            thanhPho: $scope.customerCity,
            sdtNguoiDung: $scope.customerPhone
        };

        $http.put('http://localhost:8080/api/auth/update/' + iduser, updatedCustomer)
            .then(function (response) {
                alert('Cập nhật thông tin khách hàng thành công!');
                $('#editCustomerModal').modal('hide'); // Close modal on success
            }, function (error) {
                console.error('Lỗi khi cập nhật thông tin khách hàng:', error);
            });

    };
};
