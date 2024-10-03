window.SanPhamController = function ($scope, $http) {
    $scope.products = [];
    $scope.dsDanhMuc = [];
    $scope.selectedCategoryId = null; // Lưu ID danh mục đã chọn

    $scope.onclickDanhMuc = function (id) {
        console.log("Danh mục được click: " + id);
        $scope.selectedCategoryId = id; // Cập nhật ID danh mục đã chọn

        // Xây dựng URL với ID danh mục
        var url = 'http://localhost:8080/api/san_pham/findDanhMuc/' + id;

        $http({
            method: 'GET',
            url: url // Sử dụng URL đã xây dựng
        }).then(function (response) {
            $scope.products = response.data;
            console.log("Dữ liệu API trả về: ", response.data);
        }, function (error) {
            console.error('Error fetching data:', error);
        });
    };

    // Hàm lấy dữ liệu sản phẩm từ API
    function fetchData(url, target) {
        $http({
            method: 'GET',
            url: url
        }).then(function (response) {
            $scope[target] = response.data;
            console.log($scope.products);
        }, function (error) {
            console.error('Error fetching data:', error);
        });
    }

    fetchData('http://localhost:8080/api/san_pham', 'products');
    fetchData('http://localhost:8080/api/danh_muc', 'dsDanhMuc');
};
