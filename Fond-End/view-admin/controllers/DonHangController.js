window.DonHangController = function ($scope, $http) {
    $scope.dsHoaDon = [];
    function fetchdsHoaDon() {
        $http.get('http://localhost:8080/api/ad_hoa_don')
            .then(function (response) {
                if (response.data.length > 0) {
                    $scope.dsHoaDon = response.data;
                    console.log("Ảnh sản phẩm:", $scope.dsHoaDon);
                } else {
                    console.log("Không tìm thấy hình ảnh cho sản phẩm.");
                }
            }, function (error) {
                console.error('Error fetching product images:', error);
            });
    }
    fetchdsHoaDon();

}