window.TrangchuController = function ($scope, $http) {
    $scope.dsSanPham = [];

    // Hàm lấy dữ liệu sản phẩm từ API
    function fetchData(url, target) {
        $http({
            method: 'GET',
            url: url
        }).then(function (response) {
            $scope[target] = response.data;
            console.log($scope.dsSanPham);

        }, function (error) {
            console.error('Error fetching data:', error);
        });
    }
    fetchData('http://localhost:8080/api/san_pham', 'dsSanPham');
};
   