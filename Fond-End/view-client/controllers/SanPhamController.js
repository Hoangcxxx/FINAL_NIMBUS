window.SanPhamController = function ($scope, $http, $location, $routeParams) {
    $scope.dsSanPham = [];
    $scope.dsDanhMuc = [];
    $scope.selectedCategoryId = null;
    $scope.selectedProduct = null;

    $scope.fetchData = function (url, target, logMessage) {
        $http.get(url).then(function (response) {
            $scope[target] = response.data;
            console.log(logMessage, response.data);
        }, function (error) {
            console.error('Error fetching data:', error);
        });
    };
    
    $scope.onclickDanhMuc = function (id) {
        console.log("Danh mục được click: " + id);
        $scope.selectedCategoryId = id;
        $scope.fetchData('http://localhost:8080/api/san_pham/findDanhMuc/' + id, 'dsSanPham', "Dữ liệu API trả về:");
    };
    
    $scope.onclickSanPham = function (id) {
        $scope.selectedProduct = id;
        console.log("Sản phẩm được click: " + id);
        $scope.fetchData('http://localhost:8080/api/san_pham/findSanPham/' + id, 'dsSanPham', "Dữ liệu API trả về:");
    };
    
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
    fetchData('http://localhost:8080/api/danh_muc', 'dsDanhMuc');

};
