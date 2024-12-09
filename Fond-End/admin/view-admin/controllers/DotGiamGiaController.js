window.DotGiamGiaController = function ($scope, $http) {
    $scope.dsDotKhuyenMai = [];
    $scope.dsTrangThaiGiamGia = [];
    $scope.tenDotGiamGia = '';
    $scope.kieuGiamGia = '';
    // Function to fetch data
    $scope.fetchData = function (url, target, logMessage) {
        $http.get(url).then(function (response) {
            $scope[target] = response.data;
            console.log(logMessage, response.data);
        }, function (error) {
            console.error('Error fetching data:', error);
        });
    };
    // Function to fetch data with search filters
    $scope.filterVoucherByDiscountType = function () {
        var discountType = $scope.selectedDiscountType;

        if (discountType === '') {
            // Nếu kiểu giảm giá không được chọn, lấy toàn bộ danh sách
            $scope.fetchData('http://localhost:8080/api/admin/dot_giam_gia', 'dsDotKhuyenMai', 'Fetched Voucher:');
        } else {
            // Nếu kiểu giảm giá được chọn, gọi API tìm kiếm theo kiểu giảm giá
            $http.get('http://localhost:8080/api/admin/dot_giam_gia/search/kieuGiamGia', {
                params: { kieuGiamGia: discountType }
            })
                .then(function (response) {
                    $scope.dsDotKhuyenMai = response.data;
                    console.log('Voucher filtered by discount type:', response.data);
                })
                .catch(function (error) {
                    console.error('Error fetching filtered vouchers:', error);
                });
        }
    };
    // Fetch initial data
    $scope.fetchData('http://localhost:8080/api/admin/dot_giam_gia', 'dsDotKhuyenMai', 'Fetched Voucher:');
    $scope.fetchData('http://localhost:8080/api/admin/trang_thai_giam_gia', 'dsTrangThaiGiamGia', 'Fetched trạng thái:');

    // Function to format currency
    $scope.formatCurrency = function (value) {
        if (value == null) return '';
        return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') + ' VNĐ';
    };

    // Function to delete discount
    $scope.deleteDotGiamGia = function (id) {
        if (confirm('Bạn có chắc chắn muốn xóa đợt giảm giá này?')) {
            $http.delete('http://localhost:8080/api/admin/dot_giam_gia/' + id).then(function (response) {
                console.log('Deleted successfully:', response.data.message); // Xử lý thông báo từ server
                alert("Xóa đợt giảm giá thành công!");
                $scope.fetchData('http://localhost:8080/api/admin/dot_giam_gia', 'dsDotKhuyenMai', 'Fetched Voucher:');
            }, function (error) {
                console.error('Error deleting:', error);
            });
        }
    };
};
