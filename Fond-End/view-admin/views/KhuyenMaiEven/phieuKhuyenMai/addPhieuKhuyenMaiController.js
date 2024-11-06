window.addPhieuKhuyenMaiController = function ($scope, $http) {
    $scope.dsLoaiVoucher = [];

    $scope.fetchData = function (url, target, logMessage) {
        $http.get(url).then(function (response) {
            $scope[target] = response.data;
            console.log(logMessage, response.data);
        }, function (error) {
            console.error('Error fetching data:', error);
        });
    };

    // Thêm hàm tạo voucher mới
    $scope.createVoucher = function () {
        const voucherData = {
            maVoucher: document.getElementById("maPhieu").value || null, // Tự động tạo nếu bỏ trống
            tenVoucher: document.getElementById("tenPhieu").value,
            giaTriGiamGia: document.getElementById("chietKhau").value,
            soLuong: document.getElementById("luotSuDung").value,
            giaTriToiDa: document.getElementById("giaTriToiDa").value,
            soTienToiThieu: document.getElementById("soTienToiThieu").value,
            trangThai: true,
            ngayBatDau: document.getElementById("ngayBatDau").value,
            ngayKetThuc: document.getElementById("ngayKetThuc").value,
            idLoaiVoucher: $scope.selectedLoaiVoucherId // Giả sử bạn đã lưu id loại voucher
        };

        $http.post('http://localhost:8080/api/ad_vouchers', voucherData).then(function (response) {
            alert("Voucher đã được tạo thành công!");
            console.log("Created Voucher:", response.data);
            // Có thể thêm logic để làm mới form hoặc điều hướng
        }, function (error) {
            console.error('Error creating voucher:', error);
            alert("Có lỗi xảy ra khi tạo voucher.");
        });
    };

    $scope.fetchData('http://localhost:8080/api/ad_loai_vouchers', 'dsLoaiVoucher', 'Fetched LoaiVoucher:');
};
