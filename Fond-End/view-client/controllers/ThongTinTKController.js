window.ThongTinTKController = function ($scope, $http) {
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
        var updatedCustomer = {
            tenNguoiDung: $scope.tempCustomerName,
            email: $scope.tempCustomerEmail,
            diaChi: $scope.tempCustomerAddress,
            thanhPho: $scope.tempCustomerCity,
            sdtNguoiDung: $scope.tempCustomerPhone
        };

        $http.put('http://localhost:8080/api/auth/update/' + iduser, updatedCustomer)
            .then(function (response) {
                // Cập nhật thông tin sau khi lưu thành công
                $scope.customerName = updatedCustomer.tenNguoiDung;
                $scope.customerEmail = updatedCustomer.email;
                $scope.customerAddress = updatedCustomer.diaChi;
                $scope.customerCity = updatedCustomer.thanhPho;
                $scope.customerPhone = updatedCustomer.sdtNguoiDung;

                alert('Cập nhật thông tin khách hàng thành công!');
                $('#editCustomerModal').modal('hide'); // Đóng modal
            }, function (error) {
                console.error('Lỗi khi cập nhật thông tin khách hàng:', error);
                alert('Cập nhật không thành công!'); // Thông báo lỗi
            });
    };
};
