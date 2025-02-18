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

            // Kiểm tra nếu loaiVoucher tồn tại
            if (response.data.loaiVoucher) {
                $scope.selectedLoaiVoucher = response.data.loaiVoucher.idLoaiVoucher;
            } else {
                // Xử lý nếu không có loaiVoucher
                console.warn('Loại voucher không tồn tại.');
            }

            // Chuyển đổi ngày
            $scope.selectedVoucher.ngayBatDau = new Date(response.data.ngayBatDau);
            $scope.selectedVoucher.ngayKetThuc = new Date(response.data.ngayKetThuc);
        }, function (error) {
            console.error('Error fetching voucher details:', error);
        });
    }


    // Hàm tạo mới hoặc cập nhật voucher
    $scope.createOrUpdateVoucher = function () {
        // Kiểm tra các trường bắt buộc
        if (!$scope.selectedVoucher.tenVoucher) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Mã voucher và tên voucher là bắt buộc.',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra giá trị giảm giá hợp lệ
        if (!$scope.selectedVoucher.giaTriGiamGia || isNaN($scope.selectedVoucher.giaTriGiamGia) || parseFloat($scope.selectedVoucher.giaTriGiamGia) <= 0) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Giá trị giảm giá phải là số dương và không được để trống.',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra nếu kiểu giảm giá là phần trăm (kieuGiamGia === false)
        if ($scope.selectedVoucher.kieuGiamGia === false) {
            if (parseFloat($scope.selectedVoucher.giaTriGiamGia) > 100) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: 'Giảm giá theo phần trăm không thể vượt quá 100%.',
                    confirmButtonText: 'OK'
                });
                return;
            }

            // Kiểm tra giá trị tối đa nếu kiểu giảm giá là phần trăm
            if (!$scope.selectedVoucher.giaTriToiDa || isNaN($scope.selectedVoucher.giaTriToiDa) || parseFloat($scope.selectedVoucher.giaTriToiDa) <= 0) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: 'Giá trị tối đa phải là số dương và không được để trống.',
                    confirmButtonText: 'OK'
                });
                return;
            }
        }

        // Kiểm tra số lượng voucher hợp lệ
        if (!$scope.selectedVoucher.soLuong || isNaN($scope.selectedVoucher.soLuong) || parseInt($scope.selectedVoucher.soLuong) <= 0) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Số lượng voucher phải là số nguyên dương và không được để trống.',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra ngày bắt đầu và ngày kết thúc hợp lệ
        if (!$scope.selectedVoucher.ngayBatDau || !$scope.selectedVoucher.ngayKetThuc) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Ngày bắt đầu và ngày kết thúc là bắt buộc.',
                confirmButtonText: 'OK'
            });
            return;
        }

        if ($scope.selectedVoucher.ngayBatDau > $scope.selectedVoucher.ngayKetThuc) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Ngày bắt đầu phải trước ngày kết thúc.',
                confirmButtonText: 'OK'
            });
            return;
        }

        const voucherData = {
            maVoucher: $scope.selectedVoucher.maVoucher || null,
            tenVoucher: $scope.selectedVoucher.tenVoucher,
            giaTriGiamGia: $scope.selectedVoucher.giaTriGiamGia,
            soLuong: $scope.selectedVoucher.soLuong,
            kieuGiamGia: $scope.selectedVoucher.kieuGiamGia,
            giaTriToiDa: $scope.selectedVoucher.kieuGiamGia === false ? $scope.selectedVoucher.giaTriToiDa : null,
            soTienToiThieu: $scope.selectedVoucher.soTienToiThieu,
            moTa: "Voucher giảm giá cho đơn hàng từ 50k",
            // Sử dụng toLocaleDateString để giữ nguyên ngày
            ngayBatDau: $scope.selectedVoucher.ngayBatDau.toLocaleDateString('en-CA'),  // Format yyyy-MM-dd
            ngayKetThuc: $scope.selectedVoucher.ngayKetThuc.toLocaleDateString('en-CA'),  // Format yyyy-MM-dd
        };

        // Kiểm tra nếu voucher đã có id (cập nhật voucher)
        if ($scope.selectedVoucher.idVoucher) {
            // Gửi yêu cầu PUT để cập nhật voucher
            $http.put('http://localhost:8080/api/admin/vouchers/' + $scope.selectedVoucher.idVoucher, voucherData)
                .then(function (response) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Cập nhật thành công!',
                        text: 'Voucher đã được cập nhật thành công!',
                        confirmButtonText: 'OK'
                    });
                    $location.path('/phieu_giam_gia'); // Chuyển hướng về trang voucher
                }, function (error) {
                    console.error('Error updating voucher:', error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Có lỗi xảy ra!',
                        text: 'Có lỗi xảy ra khi cập nhật voucher.',
                        confirmButtonText: 'OK'
                    });
                });
        } else {
            // Gửi yêu cầu POST để tạo mới voucher
            $http.post('http://localhost:8080/api/admin/vouchers', voucherData)
                .then(function (response) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công!',
                        text: 'Voucher đã được gửi cho khách hàng thành công!',
                        confirmButtonText: 'OK'
                    });
                    $location.path('/phieu_giam_gia'); // Chuyển hướng về trang voucher
                }, function (error) {
                    console.error('Error sending voucher to customers:', error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Có lỗi xảy ra!',
                        text: 'Có lỗi xảy ra khi gửi voucher.',
                        confirmButtonText: 'OK'
                    });
                });
        }
    };




    $scope.onKieuGiamGiaChange = function () {
        console.log('Kiểu Giảm Giá đã thay đổi:', $scope.selectedVoucher.kieuGiamGia);

        // Kiểm tra nếu giá trị là kiểu string, chuyển đổi sang boolean
        if ($scope.selectedVoucher.kieuGiamGia === 'true') {
            $scope.selectedVoucher.kieuGiamGia = true;
        } else if ($scope.selectedVoucher.kieuGiamGia === 'false') {
            $scope.selectedVoucher.kieuGiamGia = false;
        }

        // Kiểm tra xem nếu kiểu giảm giá là % thì hiển thị trường "Giá Trị Tối Đa"
        if ($scope.selectedVoucher.kieuGiamGia === false) {
            console.log('Hiển thị trường "Giá Trị Tối Đa"');
        } else {
            console.log('Ẩn trường "Giá Trị Tối Đa"');
        }
    };


};
