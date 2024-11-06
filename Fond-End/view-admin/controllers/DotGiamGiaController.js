window.DotGiamGiaController = function ($scope, $http) {
    $scope.dsDotKhuyenMai = [];

    // Function to fetch data
    $scope.fetchData = function (url, target, logMessage) {
        $http.get(url).then(function (response) {
            $scope[target] = response.data;
            console.log(logMessage, response.data);
        }, function (error) {
            console.error('Error fetching data:', error);
        });
    };

    // Fetch initial data
    $scope.fetchData('http://localhost:8080/api/ad_dot_giam_gia', 'dsDotKhuyenMai', 'Fetched Voucher:');

    // Function to format currency
    $scope.formatCurrency = function (value) {
        if (value == null) return '';
        return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') + ' VNĐ';
    };

    // Function to delete discount
    $scope.deleteDotGiamGia = function (id) {
        if (confirm('Bạn có chắc chắn muốn xóa đợt giảm giá này?')) {
            $http.delete('http://localhost:8080/api/ad_dot_giam_gia/' + id).then(function (response) {
                console.log('Deleted successfully:', response.data.message); // Xử lý thông báo từ server
                alert("Xóa đợt giảm giá thành công!");
                $scope.fetchData('http://localhost:8080/api/ad_dot_giam_gia', 'dsDotKhuyenMai', 'Fetched Voucher:');
            }, function (error) {
                console.error('Error deleting:', error);
            });
        }
    };
};
