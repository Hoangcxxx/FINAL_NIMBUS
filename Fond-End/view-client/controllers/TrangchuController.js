window.TrangchuController = function ($scope, $http) {
    $scope.dsSanPham = [];
    $scope.dsSanPhamGiamGia = [];
    $scope.dsDotGiamGia = [];

    // Hàm lấy dữ liệu sản phẩm từ API
    $scope.fetchData = function (url, target) {
        $http({
            method: 'GET',
            url: url
        }).then(function (response) {
            $scope[target] = response.data;
            console.log("Dữ liệu API trả về:", response.data);

        }, function (error) {
            console.error('Error fetching data:', error);
        });
    }

    // Hàm click vào danh mục đợt giảm giá
    $scope.onclickDotGiamGia = function (idDotGiamGia) {
        console.log("Đợt giảm giá được click: " + idDotGiamGia);

        if (idDotGiamGia === "null" || !idDotGiamGia) {
            // Nếu chọn "Tất cả", lấy tất cả sản phẩm giảm giá
            $scope.fetchData('http://localhost:8080/api/nguoi_dung/san_pham/san_pham_giam_gia', 'dsSanPhamGiamGia');
        } else {
            // Lấy danh sách sản phẩm giảm giá theo đợt giảm giá đã chọn
            $scope.fetchData('http://localhost:8080/api/nguoi_dung/san_pham/findDotGiamGia/' + idDotGiamGia, 'dsSanPhamGiamGia');
            // Cập nhật đợt giảm giá đã chọn
            $scope.selectedDotGiamGia = $scope.dsDotGiamGia.find(dot => dot.idDotGiamGia === idDotGiamGia);
        }
    };

    // Lấy dữ liệu ban đầu
    $scope.fetchData('http://localhost:8080/api/nguoi_dung/san_pham/san_pham_giam_gia', 'dsSanPhamGiamGia');
    $scope.fetchData('http://localhost:8080/api/nguoi_dung/san_pham/dot_giam_gia', 'dsDotGiamGia');
};
