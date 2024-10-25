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

    $scope.fetchData('http://localhost:8080/api/ad_loai_voucher', 'dsLoaiVoucher', 'Fetched LoaiVoucher:');
};