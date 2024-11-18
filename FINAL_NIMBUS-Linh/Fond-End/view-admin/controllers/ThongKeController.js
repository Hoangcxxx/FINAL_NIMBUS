window.ThongKeController = function ($scope, $http) {
    $scope.searchTerm = '';
    $scope.thongKeData = [];
    $scope.filteredData = [];
    $scope.selectedCategory = '';
    $scope.getThongKeDoanhThuSanPham = function () {
        $http.get('http://localhost:8080/api/thong-ke/doanh-thu-san-pham')
            .then(function (response) {
                $scope.thongKeData = response.data;
                $scope.filteredData = $scope.thongKeData; 
                console.log($scope.thongKeData);
            })
            .catch(function (error) {
                console.error('Có lỗi xảy ra khi lấy thống kê doanh thu sản phẩm:', error);
            });
    };

    $scope.getThongKeTongDoanhThu = function () {
        $http.get('http://localhost:8080/api/thong-ke/tong-doanh-thu')
            .then(function (response) {
                const data = response.data;
                $scope.soSanPhamBanRa = data.soSanPhamBanRa || 0;
                $scope.tongSoLuongBanRa = data.tongSoLuongBanRa || 0;
                $scope.tongDoanhThu = data.tongDoanhThu ? data.tongDoanhThu.toLocaleString() + ' ₫' : '0 ₫';
            })
            .catch(function (error) {
                console.error('Có lỗi xảy ra khi lấy thống kê tổng doanh thu:', error);
            });
    };
$scope.getDoanhThuTheoDanhMuc = function () {
    var tenDanhMuc = $scope.selectedCategory;

    if (tenDanhMuc === "") {
        $scope.filteredData = $scope.thongKeData; 
    } else {
        $http.get('http://localhost:8080/api/thong-ke/doanh-thu-theo-danh-muc', {
            params: { tenDanhMuc: tenDanhMuc }
        })
        .then(function (response) {
            $scope.filteredData = response.data; 
            console.log("Dữ liệu doanh thu theo danh mục:", $scope.filteredData);
        })
        .catch(function (error) {
            console.error('Có lỗi xảy ra khi lấy thống kê doanh thu theo danh mục:', error);
            $scope.filteredData = []; 
        });
    }
};

    $scope.sortByRevenueAsc = function () {
        $scope.filteredData.sort(function (a, b) {
            return parseFloat(a.tongDoanhThu) - parseFloat(b.tongDoanhThu);
        });
    };
    
    $scope.sortByRevenueDesc = function () {
        $scope.filteredData.sort(function (a, b) {
            return parseFloat(b.tongDoanhThu) - parseFloat(a.tongDoanhThu); 
        });
    };

    $scope.filterResults = function () {
        if ($scope.searchTerm) {
            $scope.filteredData = $scope.thongKeData.filter(function(item) {
                return item.tenSanPham.toLowerCase().includes($scope.searchTerm.toLowerCase());
            });
        } else {
            $scope.filteredData = $scope.thongKeData; 
        }
    };
    $scope.thongKe = function () {
        var fromDate = document.getElementById('time-start-tk').value;
        var toDate = document.getElementById('time-end-tk').value;

        if (fromDate && toDate) {
            $http.get('http://localhost:8080/api/thong-ke/doanh-thu', { 
                params: { 
                    fromDate: fromDate, 
                    toDate: toDate 
                } 
            })
            .then(function (response) {
                console.log("Dữ liệu thống kê:", response.data);
                $scope.thongKeData = response.data; 
                $scope.filteredData = $scope.thongKeData; 
            })
            .catch(function (error) {
                console.error('Có lỗi xảy ra khi lấy thống kê doanh thu:', error.response ? error.response.data : error);
            });
        } else {
            console.error('Vui lòng chọn cả ngày bắt đầu và ngày kết thúc.');
        }
    };
    $scope.getThongKeDoanhThuSanPham();
    $scope.getThongKeTongDoanhThu();
};
