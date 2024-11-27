window.detailSanPhamController = function ($scope, $http, $routeParams) {
    var idSanPham = $routeParams.id;
    $scope.anhSanPham = [];
    $scope.dsSanPhamChiTiet = [];
    $scope.kichThuocChiTiet = [];

    function fetchAnhSanPham() {
        $http.get('http://localhost:8080/api/nguoi_dung/hinh_anh/' + idSanPham)
            .then(function (response) {
                if (response.data.length > 0) {
                    $scope.anhSanPham = response.data;
                    console.log("Ảnh sản phẩm:", $scope.anhSanPham);
                } else {
                    console.log("Không tìm thấy hình ảnh cho sản phẩm.");
                }
            }, function (error) {
                console.error('Error fetching product images:', error);
            });
    }
    console.log("Thông tin sản phẩm:");
    function fetchSanPhamChiTiet() {
        $http.get('http://localhost:8080/api/nguoi_dung/san_pham_chi_tiet/' + idSanPham)
            .then(function (response) {
                if (response.data) {
                    $scope.sanPhamChiTiet = response.data;
                    console.log("Thông tin sản phẩm:", $scope.sanPhamChiTiet);
                } else {
                    console.log("Không tìm thấy sản phẩm.");
                }
            }, function (error) {
                console.error('Error fetching product details:', error);
            });
    }
    console.log("Danh sách sản phẩm:");
    function fetchDSSanPhamChiTiet() {
        $http.get('http://localhost:8080/api/nguoi_dung/san_pham_chi_tiet/findSanPhamCTLonHon0/' + idSanPham)
            .then(function (response) {
                if (response.data) {
                    $scope.dsSanPhamChiTiet = response.data;
                    console.log("Danh sách sản phẩm:", $scope.dsSanPhamChiTiet);
                } else {
                    console.log("Không tìm thấy sản phẩm.");
                }
            }, function (error) {
                console.error('Error fetching product details:', error);
            });
    }
    
    // Fetch functions
    fetchAnhSanPham();
    fetchSanPhamChiTiet();
    fetchDSSanPhamChiTiet();
   

}
