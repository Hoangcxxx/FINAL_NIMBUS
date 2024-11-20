window.ThongTinNguoiDungController = function ($scope, $http, $window) {
    // Lấy thông tin người dùng từ localStorage
    var userInfo = localStorage.getItem('user');

    if (userInfo) {
        // Nếu có thông tin người dùng, parse và gán vào scope để hiển thị
        $scope.infoUser = JSON.parse(userInfo);
        $scope.idNguoiDung = $scope.infoUser.idNguoiDung;
        $scope.tenNguoiDung = $scope.infoUser.tenNguoiDung;
        $scope.emailNguoiDung = $scope.infoUser.email;
        $scope.diaChiNguoiDung = $scope.infoUser.diaChi;
        $scope.sdtNguoiDung = $scope.infoUser.sdt;
        $scope.ngaySinhNguoiDung = $scope.infoUser.ngaySinh;
        $scope.gioiTinhNguoiDung = $scope.infoUser.gioiTinh;
    } else {
        // Nếu không có thông tin người dùng trong localStorage
        $scope.infoUser = null;
    }

    // Mở modal chỉnh sửa thông tin
    $scope.openEditModal = function () {
        // Thông tin sẽ tự động điền vào modal khi mở
        $scope.tenNguoiDung = $scope.infoUser.tenNguoiDung;
        $scope.emailNguoiDung = $scope.infoUser.email;
        $scope.diaChiNguoiDung = $scope.infoUser.diaChi;
        $scope.sdtNguoiDung = $scope.infoUser.sdt;
        $scope.ngaySinhNguoiDung = $scope.infoUser.ngaySinh;
        $scope.gioiTinh = $scope.infoUser.gioiTinh;
        console.log('Ngày sinh trong modal sau khi chuyển định dạng: ', $scope.ngaySinhNguoiDung);
        // Log thông tin người dùng hiện tại khi mở modal
        console.log('Dữ liệu người dùng khi mở modal: ', $scope.infoUser);
    };

    // Hàm cập nhật thông tin người dùng
    $scope.saveCustomerDetails = function () {
        // Chuyển đổi ngày sinh từ đối tượng Date thành định dạng dd-MM-yyyy
        var ngaySinhFormat = $scope.ngaySinhNguoiDung;
        if (ngaySinhFormat instanceof Date) {
            var day = ngaySinhFormat.getDate().toString().padStart(2, '0');  // Lấy ngày và đảm bảo có 2 chữ số
            var month = (ngaySinhFormat.getMonth() + 1).toString().padStart(2, '0'); // Lấy tháng và đảm bảo có 2 chữ số
            var year = ngaySinhFormat.getFullYear();  // Lấy năm

            // Định dạng lại ngày sinh theo dd-MM-yyyy
            $scope.ngaySinhNguoiDung = day + '-' + month + '-' + year;
        }
        // Chuẩn bị dữ liệu người dùng mới để gửi đi
        var updatedUser = {
            tenNguoiDung: $scope.tenNguoiDung,
            email: $scope.emailNguoiDung,
            sdt: $scope.sdtNguoiDung,
            ngaySinh: $scope.ngaySinhNguoiDung,
            diaChi: $scope.diaChiNguoiDung,
            gioiTinh: $scope.gioiTinh,
        };
        // Log dữ liệu đã chỉnh sửa
        console.log('Dữ liệu người dùng sau khi chỉnh sửa: ', updatedUser);
        // Gửi yêu cầu PUT đến API cập nhật người dùng
        $http.put('http://localhost:8080/api/nguoi_dung/' + $scope.infoUser.idNguoiDung, updatedUser)
            .then(function (response) {
                // Cập nhật thông tin trong localStorage sau khi thành công
                localStorage.setItem('user', JSON.stringify(response.data));

                // Cập nhật lại thông tin trong scope
                $scope.infoUser = response.data;
                $scope.tenNguoiDung = response.data.tenNguoiDung;
                $scope.emailNguoiDung = response.data.email;
                $scope.diaChiNguoiDung = response.data.diaChi;
                $scope.sdtNguoiDung = response.data.sdt;
                $scope.ngaySinhNguoiDung = response.data.ngaySinh;
                $scope.gioiTinh = response.data.gioiTinh;

                // Đóng modal
                $('#editCustomerModal').modal('hide');
                alert('Thông tin đã được cập nhật thành công!');
                $window.location.href = "/#!thong_tin_nguoi_dung"; // Chuyển hướng sang trang thanh toán
            })
            .catch(function (error) {
                console.log('Cập nhật thông tin thất bại:', error);
                alert('Có lỗi xảy ra khi cập nhật thông tin!');
            });
    };
    $scope.formatDate = function () {
        let date = $scope.ngaySinhNguoiDung;

        // Loại bỏ mọi ký tự không phải là số (bao gồm cả dấu gạch nối)
        date = date.replace(/[^0-9]/g, "");

        // Nếu có ít hơn 8 ký tự, xử lý thêm dấu gạch nối theo từng bước
        if (date.length >= 2) {
            // Nếu có 2 ký tự, thêm dấu gạch nối sau ngày
            date = date.substring(0, 2) + (date.length >= 2 ? '-' : '') + date.substring(2);
        }

        if (date.length >= 5) {
            // Nếu có 5 ký tự, thêm dấu gạch nối sau tháng
            date = date.substring(0, 5) + (date.length >= 5 ? '-' : '') + date.substring(5);
        }

        // Hiển thị lại giá trị đã định dạng
        $scope.ngaySinhNguoiDung = date;

        // Nếu đủ 10 ký tự, không thay đổi gì thêm
        if (date.length === 10) {
            return;
        }
    };

    function getOrderDetails() {
        const apiUrl = `http://localhost:8080/api/nguoi_dung/hoa_don/user/${$scope.idNguoiDung}`;

        $http.get(apiUrl)
            .then(function (response) {
                // Kiểm tra và gán dữ liệu đơn hàng
                const hoaDonList = response.data.hoaDon && Array.isArray(response.data.hoaDon)
                    ? response.data.hoaDon
                    : [];

                if (hoaDonList.length > 0) {
                    // Gán danh sách hóa đơn vào $scope
                    $scope.orderData = hoaDonList;
                    console.log($scope.orderData);
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
    getOrderDetails();



};
