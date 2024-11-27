window.addPhieuKhuyenMaiController = function ($scope, $http, $routeParams, $window, $location) {
    $scope.dsLoaiVoucher = [];
    $scope.dsKhachHang = [];
    $scope.selectedVoucher = {
        idVoucher: null,
        maVoucher: '',
        tenVoucher: '',
        kieuGiamGia: true,
        giaTriGiamGia: '',
        soLuong: '',
        giaTriToiDa: '',
        soTienToiThieu: '',
        ngayBatDau: null,
        ngayKetThuc: null,
        loaiVoucher: {}
    };

    $scope.fetchData = function (url, target, logMessage) {
        $http.get(url).then(function (response) {
            $scope[target] = response.data;
            console.log(logMessage, response.data);
        }, function (error) {
            console.error('Error fetching data:', error);
        });
    };

    // Lấy voucher theo ID nếu có
    if ($routeParams.id) {
        $http.get('http://localhost:8080/api/admin/vouchers/' + $routeParams.id).then(function (response) {
            $scope.selectedVoucher = response.data;
            $scope.selectedLoaiVoucher = response.data.loaiVoucher.idLoaiVoucher;

            // Chuyển đổi ngày
            $scope.selectedVoucher.ngayBatDau = new Date(response.data.ngayBatDau);
            $scope.selectedVoucher.ngayKetThuc = new Date(response.data.ngayKetThuc);
        }, function (error) {
            console.error('Error fetching voucher details:', error);
        });
    }

    // Hàm tạo mới hoặc cập nhật voucher
    $scope.createOrUpdateVoucher = function () {
        const voucherData = {
            maVoucher: $scope.selectedVoucher.maVoucher || null,
            tenVoucher: $scope.selectedVoucher.tenVoucher,
            giaTriGiamGia: $scope.selectedVoucher.giaTriGiamGia,
            soLuong: $scope.selectedVoucher.soLuong,
            kieuGiamGia: $scope.selectedVoucher.kieuGiamGia,
            giaTriToiDa: $scope.selectedVoucher.giaTriToiDa,
            soTienToiThieu: $scope.selectedVoucher.soTienToiThieu,
            moTa: "Voucher giảm giá cho đơn hàng từ 50k",
            ngayBatDau: $scope.selectedVoucher.ngayBatDau.toISOString(),
            ngayKetThuc: $scope.selectedVoucher.ngayKetThuc.toISOString()
        };

        // Lấy danh sách khách hàng đã chọn
        const selectedCustomers = $scope.getSelectedCustomers();
        if (selectedCustomers.length === 0) {
            alert("Vui lòng chọn khách hàng để gửi voucher.");
            return;
        }

        // Lấy danh sách các ID khách hàng đã chọn
        const customerIds = selectedCustomers.map(function (customer) {
            return customer.idNguoiDung;
        });

        // Gửi request để thêm voucher cho nhiều khách hàng
        $http.post('http://localhost:8080/api/admin/vouchers/bulk/' + customerIds.join(','), voucherData).then(function (response) {
            alert("Voucher đã được gửi cho khách hàng thành công!");
            $location.path('/phieu_giam_gia'); // Chuyển hướng về trang voucher
        }, function (error) {
            console.error('Error sending voucher to customers:', error);
            alert("Có lỗi xảy ra khi gửi voucher.");
        });
    };

    $scope.getSelectedCustomers = function () {
        // Lọc các khách hàng đã được chọn
        return $scope.dsKhachHang.filter(function (item) {
            return item.selected;
        });
    };
    $scope.selectAllCustomers = function () {
        angular.forEach($scope.dsKhachHang, function (customer) {
            customer.selected = $scope.selectAll;
        });
    };

    // Fetch the types of vouchers
    $scope.fetchData('http://localhost:8080/api/admin/loai_vouchers', 'dsLoaiVoucher', 'Fetched LoaiVoucher:');
    $scope.fetchData('http://localhost:8080/api/admin/nguoi_dung/list', 'dsKhachHang', 'Fetched dsKhachHang:');
};
