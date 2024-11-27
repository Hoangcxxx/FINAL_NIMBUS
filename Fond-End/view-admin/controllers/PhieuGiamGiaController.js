window.PhieuGiamGiaController = function ($scope, $http) {
    $scope.dsKhuyenMai = [];
    // Function to fetch data
    $scope.fetchData = function (url, target, logMessage) {
        $http.get(url).then(function (response) {
            $scope[target] = response.data;
            console.log(logMessage, response.data);
        }, function (error) {
            console.error('Error fetching data:', error);
        });
    };

    $scope.fetchData('http://localhost:8080/api/admin/vouchers', 'dsKhuyenMai', 'Fetched Voucher:');

    $scope.deleteVoucher = function (id) {
        if (confirm("Bạn có chắc chắn muốn xóa voucher này không?")) {
            $http.delete('http://localhost:8080/api/admin/vouchers/' + id)
                .then(function (response) {
                    alert("Voucher đã được xóa thành công!");
                    // Cập nhật lại danh sách vouchers
                    $scope.fetchData('http://localhost:8080/api/admin/vouchers', 'dsKhuyenMai', 'Fetched KhuyenMai:');
                }, function (error) {
                    console.error('Error deleting voucher:', error);
                    alert("Có lỗi xảy ra khi xóa voucher.");
                });
        }
    };
    // Function to update product status
    $scope.updateTrangThai = function (idVoucher, newStatus) {
        $http.put('http://localhost:8080/api/admin/vouchers/update_status/' + idVoucher, { trangThai: newStatus })
            .then(function (response) {
                console.log('Cập nhật trạng thái sản phẩm thành công:', response.data);
            })
            .catch(function (error) {
                console.error('Error updating product status:', error);
                // Log thêm thông tin chi tiết về lỗi
                if (error.status) {
                    console.error('Status:', error.status);
                }
                if (error.data) {
                    console.error('Response data:', error.data);
                }
                // Revert the checkbox state if the update fails
                const voucher = $scope.dsKhuyenMai.find(p => p.idVoucher === idVoucher);
                if (voucher) {
                    voucher.trangThai = !voucher.trangThai; // Toggle back
                }
            });
    };
    $scope.getRowClass = function (trangThai) {
        console.log('Trang thái voucher:', trangThai); // Log trạng thái voucher

        switch (trangThai) {
            case 'Đã phát hành':
                return 'bg-success text-white'; // Màu xanh
            case 'Đã sử dụng':
                return 'bg-secondary text-white'; // Màu xám
            case 'Hết hạn':
                return 'bg-danger text-white'; // Màu đỏ
            case 'Chưa phát hành':
                return 'bg-warning text-dark'; // Màu vàng
            case 'Bị hủy':
                return 'bg-dark text-white'; // Màu đen
            case 'Đang diễn ra':
                return 'bg-info text-white'; // Màu xanh nhạt cho trạng thái đang diễn ra
            default:
                return ''; // Trả về lớp rỗng nếu không có trạng thái nào khớp
        }
    };
    $scope.formatCurrency = function (value) {
        if (value == null) return '';
        return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') + ' VNĐ';
    };

}
