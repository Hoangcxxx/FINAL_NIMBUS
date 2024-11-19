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
            sdtNguoiDung: $scope.tempSdtKhachHang
        };

        // Cập nhật thông tin người dùng qua API
        $http.put('http://localhost:8080/api/auth/update/' + iduser, updatedCustomer)
            .then(function (response) {
                // Cập nhật thông tin người dùng trên giao diện
                $scope.tenKhachHang = updatedCustomer.tenNguoiDung;
                $scope.emailKhachHang = updatedCustomer.email;
                $scope.diaChiKhachHang = updatedCustomer.diaChi;
                $scope.sdtKhachHang = updatedCustomer.sdtNguoiDung;

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

    // Lấy danh sách hóa đơn của người dùng
    $scope.getHoaDonList = function () {
        $http.get('http://localhost:8080/api/hoa-don/san-pham/' + iduser)
            .then(function (response) {
                var hoaDonList = response.data; 
                $scope.hoaDonList = hoaDonList.maHoaDon; 
                $scope.hoaDonList = hoaDonList.ngayTao;
                $scope.hoaDonList = hoaDonList.thanhTien;
                $scope.hoaDonList = hoaDonList.idtrangthaihoadon;
                $scope.hoaDonList = hoaDonList.idDiaChiVanChuyen;
            }, function (error) {
                console.error('Lỗi khi lấy danh sách hóa đơn:', error);
                alert('Không thể lấy danh sách hóa đơn!');
            });
    };

    // Gọi hàm để lấy danh sách hóa đơn khi tải trang
    $scope.getHoaDonList();
}
