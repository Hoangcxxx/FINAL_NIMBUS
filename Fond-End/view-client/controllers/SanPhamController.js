window.SanPhamController = function ($scope, $http) {
    $scope.dsSanPham = [];
    $scope.dsDanhMuc = [];
    $scope.selectedCategoryId = null;
    $scope.selectedProduct = null;

    // Hàm lấy dữ liệu từ API
    $scope.fetchData = function (url, target, logMessage) {
        $http.get(url).then(function (response) {
            $scope[target] = response.data;
            console.log(logMessage, response.data);
        }, function (error) {
            console.error('Error fetching data:', error);
        });
    };

    // Hàm click vào danh mục
    $scope.onclickDanhMuc = function (idDanhMuc) {
        console.log("Danh mục được click: " + idDanhMuc);
        $scope.selectedCategoryId = idDanhMuc;
        // Lấy danh sách sản phẩm theo danh mục đã chọn
        $scope.fetchData('http://localhost:8080/api/san_pham/findDanhMuc/' + idDanhMuc, 'dsSanPham', "Dữ liệu API trả về:");
    };

    // Hàm click vào sản phẩm
    $scope.onclickSanPham = function (idSanPham) {
        console.log('ID sản phẩm:', idSanPham);
        // Lấy chi tiết sản phẩm đã chọn
        $http.get('http://localhost:8080/api/san_pham_chi_tiet/' + idSanPham).then(function (response) {
            $scope.dsSanPham = response.data; // Gán dữ liệu sản phẩm chi tiết vào biến selectedProduct
            console.log("Dữ liệu sản phẩm chi tiết:", response.data);
            window.location.href = '#!/san_pham_ct/' + idSanPham;
        }, function (error) {
            console.error('Error fetching product details:', error);
        });
    };


    // Hàm lấy dữ liệu sản phẩm và danh mục khi khởi tạo controller
    function initializeData() {
        // Lấy danh sách sản phẩm
        $scope.fetchData('http://localhost:8080/api/san_pham', 'dsSanPham');
        // Lấy danh sách danh mục
        $scope.fetchData('http://localhost:8080/api/danh_muc', 'dsDanhMuc');
    }

    // Gọi hàm khởi tạo
    initializeData();
};
