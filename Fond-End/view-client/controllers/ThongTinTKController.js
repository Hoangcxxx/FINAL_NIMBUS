window.ThongTinTKController = function ($scope, $http) {
    // Lấy thông tin người dùng từ localStorage
    var user = JSON.parse(localStorage.getItem("user"));
    if (user) {
        var iduser = user.idNguoiDung;  // Giả sử 'idNguoiDung' là ID người dùng trong phản hồi từ API
    }
    // Lấy thông tin người dùng từ API
    $http.get('http://localhost:8080/api/auth/user/' + iduser)
        .then(function (response) {
            var user = response.data;
            $scope.tenKhachHang = user.tenNguoiDung;
            $scope.emailKhachHang = user.email;
            $scope.diaChiKhachHang = user.diaChi;
            $scope.sdtKhachHang = user.sdtNguoiDung;
        }, function (error) {
            console.error('Lỗi khi lấy dữ liệu người dùng:', error);
        });

    // Mở modal chỉnh sửa thông tin
    $scope.openEditModal = function () {
        $scope.tempTenKhachHang = $scope.tenKhachHang;
        $scope.tempEmailKhachHang = $scope.emailKhachHang;
        $scope.tempDiaChiKhachHang = $scope.diaChiKhachHang;
        $scope.tempSdtKhachHang = $scope.sdtKhachHang;

        // Mở modal
        $('#editCustomerModal').modal('show');
    };

    // Cập nhật thông tin khách hàng
    $scope.saveCustomerDetails = function () {
        var updatedCustomer = {
            tenNguoiDung: $scope.tempTenKhachHang,
            email: $scope.tempEmailKhachHang,
            diaChi: $scope.tempDiaChiKhachHang,
            sdtNguoiDung: $scope.tempSdtKhachHang,
            MatKhau: $scope.MatKhau
        };

        // Cập nhật thông tin người dùng qua API
        $http.put('http://localhost:8080/api/auth/update/' + iduser, updatedCustomer)
            .then(function (response) {
                // Cập nhật thông tin người dùng trên giao diện
                $scope.tenKhachHang = updatedCustomer.tenNguoiDung;
                $scope.emailKhachHang = updatedCustomer.email;
                $scope.diaChiKhachHang = updatedCustomer.diaChi;
                $scope.sdtKhachHang = updatedCustomer.sdtNguoiDung;
                $scope.MatKhau = updatedCustomer.MatKhau;

                // Lưu lại thông tin vào localStorage
                var updatedUser = { ...user, ...updatedCustomer }; // Cập nhật thông tin người dùng trong localStorage
                localStorage.setItem("user", JSON.stringify(updatedUser));

                // Thông báo cập nhật thành công
                alert('Cập nhật thông tin thành công!');
                $('#editCustomerModal').modal('hide');
            }, function (error) {
                console.error('Lỗi khi cập nhật dữ liệu người dùng:', error);
                alert('Cập nhật không thành công! Vui lòng thử lại.');
            });
    };

    function getOrderDetails(iduser) {
        const apiUrl = `http://localhost:8080/api/hoa-don/user/${iduser}`;

        $http.get(apiUrl)
            .then(function (response) {
                // Kiểm tra và gán dữ liệu đơn hàng
                const hoaDonList = response.data.hoaDon && Array.isArray(response.data.hoaDon)
                    ? response.data.hoaDon
                    : [];

                if (hoaDonList.length > 0) {
                    // Gán danh sách hóa đơn vào $scope
                    $scope.orderData = hoaDonList;
                } else {
                    console.warn("Không tìm thấy đơn hàng nào.");
                    $scope.orderData = [];
                }
            })
            .catch(function (error) {
                console.error("Lỗi khi lấy thông tin hóa đơn:", error);
                $scope.orderData = []; // Gán rỗng nếu lỗi
            });
    }
    // Gọi API để lấy chi tiết đơn hàng
    getOrderDetails(iduser);
}
