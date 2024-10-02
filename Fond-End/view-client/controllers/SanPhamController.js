window.SanPhamController = function ($scope, $http) {
    $scope.products = [];

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
    fetchData('http://localhost:8080/api/san_pham/hien_thi', 'products');
};