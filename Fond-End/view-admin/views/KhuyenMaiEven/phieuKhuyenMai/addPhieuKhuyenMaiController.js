window.addPhieuKhuyenMaiController = function ($scope, $http, $routeParams, $window, $location) {
    $scope.dsLoaiVoucher = [];
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
        $http.get('http://localhost:8080/api/ad_vouchers/' + $routeParams.id).then(function (response) {
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
            idVoucher: $scope.selectedVoucher.idVoucher || null,
            maVoucher: $scope.selectedVoucher.maVoucher || null,
            tenVoucher: $scope.selectedVoucher.tenVoucher,
            giaTriGiamGia: $scope.selectedVoucher.giaTriGiamGia,
            soLuong: $scope.selectedVoucher.soLuong,
            kieuGiamGia: $scope.selectedVoucher.kieuGiamGia,
            giaTriToiDa: $scope.selectedVoucher.giaTriToiDa,
            soTienToiThieu: $scope.selectedVoucher.soTienToiThieu,
            trangThai: true,
            moTa: "Giảm giá cho đơn hàng trên 500.000đ",
            ngayBatDau: $scope.selectedVoucher.ngayBatDau.toISOString(),
            ngayKetThuc: $scope.selectedVoucher.ngayKetThuc.toISOString(),
            loaiVoucher: { idLoaiVoucher: $scope.selectedLoaiVoucher }
        };

        // Log dữ liệu voucher để kiểm tra
        console.log("Dữ liệu voucher trước khi cập nhật:", voucherData);

        // Kiểm tra loại voucher
        if (!$scope.selectedLoaiVoucher) {
            alert("Vui lòng chọn loại voucher.");
            return;
        }

        // Kiểm tra tính hợp lệ của ngày
        const ngayBatDau = new Date(voucherData.ngayBatDau);
        const ngayKetThuc = new Date(voucherData.ngayKetThuc);
        if (isNaN(ngayBatDau.getTime()) || isNaN(ngayKetThuc.getTime())) {
            alert("Ngày bắt đầu hoặc ngày kết thúc không hợp lệ. Vui lòng kiểm tra lại.");
            return;
        }

        // Nếu có idVoucher, thực hiện cập nhật, nếu không thì tạo mới
        if ($scope.selectedVoucher.idVoucher) {
            $http.put('http://localhost:8080/api/ad_vouchers/' + $scope.selectedVoucher.idVoucher, voucherData).then(function (response) {
                alert("Voucher đã được cập nhật thành công!");
                $location.path('/phieu_giam_gia'); // Chuyển hướng về trang voucher
            }, function (error) {
                console.error('Error updating voucher:', error);
                alert("Có lỗi xảy ra khi cập nhật voucher.");
            });
        } else {
            $http.post('http://localhost:8080/api/ad_vouchers', voucherData).then(function (response) {
                alert("Voucher đã được tạo thành công!");
                $location.path('/phieu_giam_gia'); // Chuyển hướng về trang voucher
            }, function (error) {
                console.error('Error creating voucher:', error);
                alert("Có lỗi xảy ra khi tạo voucher.");
            });
        }
    };

    // Fetch the types of vouchers
    $scope.fetchData('http://localhost:8080/api/ad_loai_vouchers', 'dsLoaiVoucher', 'Fetched LoaiVoucher:');
};
