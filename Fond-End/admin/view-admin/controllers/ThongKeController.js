window.ThongKeController = function ($scope, $http) {
    // Initialize variables
    $scope.quantityProduct = 0;
    $scope.quantityOrder = 0;
    $scope.quantitySale = '0₫';
    $scope.donHangCho = 0;
    $scope.donHangDangGiao = 0;
    $scope.donHangHoanThanh = 0;
    $scope.donHangHuyBo = 0;

    // Hàm thongKe xử lý lọc dữ liệu
    $scope.thongKe = function () {
        // Log giá trị được chọn trong form
        console.log("Start Date:", $scope.startDate);
        console.log("End Date:", $scope.endDate);
        console.log("Month:", $scope.filterMonth);
        console.log("Year:", $scope.filterYear);

        // Kiểm tra và chuyển đổi ngày tháng thành định dạng yyyy-MM-dd
        let startDateFormatted = $scope.startDate ? formatDate($scope.startDate) : null;
        let endDateFormatted = $scope.endDate ? formatDate($scope.endDate) : null;

        // Nếu có khoảng ngày
        if (startDateFormatted && endDateFormatted) {
            console.log("Tìm kiếm theo khoảng ngày:", startDateFormatted, endDateFormatted);
            $scope.fetchOrderStatusData(startDateFormatted, endDateFormatted);
        }
        // Nếu có tháng và năm
        else if ($scope.filterMonth && $scope.filterYear) {
            console.log("Tìm kiếm theo tháng và năm:", $scope.filterMonth, $scope.filterYear);
            $scope.fetchDoanhThuTheoThangNam($scope.filterMonth, $scope.filterYear);
        }
        // Nếu không có gì, tải dữ liệu mặc định
        else {
            console.log("Không có filter nào được chọn, tải dữ liệu mặc định");
            $scope.fetchOrderStatusData();
        }
    };

    // Hàm chuyển đổi đối tượng Date thành định dạng yyyy-MM-dd
    function formatDate(date) {
        let d = new Date(date);
        let day = ('0' + d.getDate()).slice(-2); // Đảm bảo ngày luôn có 2 chữ số
        let month = ('0' + (d.getMonth() + 1)).slice(-2); // Đảm bảo tháng luôn có 2 chữ số
        let year = d.getFullYear();
        return year + '-' + month + '-' + day;
    }

    $scope.fetchDoanhThuTheoThangNam = function (month, year) {
        console.log("Fetching doanh thu theo tháng và năm...");
        let url = `http://localhost:8080/api/admin/thong_ke/doanh_thu_theo_thang_nam?month=${month}&year=${year}`;

        // Log URL yêu cầu
        console.log("Request URL for monthly revenue:", url);

        $http.get(url)
            .then(function (response) {
                // Log dữ liệu trả về
                console.log("Doanh thu theo tháng và năm:", response.data);

                // Kiểm tra và cập nhật giá trị doanh thu vào scope
                $scope.quantitySale = $scope.formatCurrency(response.data || 0);

                // Log doanh thu đã được định dạng
                console.log("Formatted monthly revenue:", $scope.quantitySale);
            }, function (error) {
                // Log lỗi khi không thể lấy dữ liệu
                console.error('Error fetching monthly revenue data:', error);
            });
    };


    // Fetch thống kê các đơn hàng theo trạng thái (với khoảng ngày nếu có)
    $scope.fetchOrderStatusData = function (startDate, endDate) {
        let urls = [
            {url: 'don_hang_cho', key: 'donHangCho', description: 'Đơn hàng chờ xử lý'},
            {url: 'don_hang_dang_giao', key: 'donHangDangGiao', description: 'Đơn hàng đang giao'},
            {url: 'don_hang_hoan_thanh', key: 'donHangHoanThanh', description: 'Đơn hàng đã hoàn thành'},
            {url: 'don_hang_huy_bo', key: 'donHangHuyBo', description: 'Đơn hàng bị hủy'}
        ];
    
        urls.forEach(function(item) {
            let url = `http://localhost:8080/api/admin/thong_ke/${item.url}`;
            if (startDate && endDate) {
                url += `?startDate=${startDate}&endDate=${endDate}`;
            }
    
            // Log URL và mô tả loại trạng thái
            console.log(`Request URL for ${item.description}:`, url);
    
            $http.get(url)
                .then(function (response) {
                    // Log dữ liệu trả về
                    console.log(`Response for ${item.description}:`, response.data);
    
                    // Cập nhật dữ liệu của từng trạng thái đơn hàng vào scope
                    if (response.data) {
                        let key = item.key;
                        let quantityKey = Object.keys(response.data)[0];  // Lấy key đầu tiên trong response.data (ví dụ: soLuongDonHangHuyBo)
    
                        $scope[key] = response.data[quantityKey] || 0;
                        
                        // Log giá trị thống kê của mỗi trạng thái
                        console.log(`${item.description} count:`, $scope[key]);
                    } else {
                        console.log(`No data received for ${item.description}`);
                    }
                }, function (error) {
                    // Log lỗi khi không thể lấy dữ liệu
                    console.error(`Error fetching ${item.url}:`, error);
                });
        });
    };
    


    // Function to format currency
    $scope.formatCurrency = function (value) {
        if (value == null) return '';
        return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') + '₫';
    };

    $scope.fetchThongKeData = function () {
        console.log("Fetching initial statistics...");

        // Fetch product sales data
        $http.get('http://localhost:8080/api/admin/thong_ke/san_pham_ban_ra')
            .then(function (response) {
                // Log dữ liệu về sản phẩm bán ra
                console.log("Product sales data:", response.data);
                $scope.quantityProduct = response.data[0].sanPhamBanRa || 0;
            }, function (error) {
                console.error('Error fetching product data:', error);
            });

        // Fetch total quantity sold
        $http.get('http://localhost:8080/api/admin/thong_ke/tong_so_luong_ban_ra')
            .then(function (response) {
                // Log dữ liệu về số lượng bán ra
                console.log("Total quantity sold:", response.data);
                $scope.quantityOrder = response.data[0].tongSoLuongBanRa || 0;
            }, function (error) {
                console.error('Error fetching quantity sold data:', error);
            });

        // Fetch total revenue
        $http.get('http://localhost:8080/api/admin/thong_ke/tong_doanh_thu')
            .then(function (response) {
                // Log dữ liệu về tổng doanh thu
                console.log("Total revenue:", response.data);
                $scope.quantitySale = $scope.formatCurrency(response.data[0].tongDoanhThu);
            }, function (error) {
                console.error('Error fetching revenue data:', error);
            });

        // Fetch order status data (with default filter)
        $scope.fetchOrderStatusData();
    };


    // Fetch dữ liệu khi trang được tải
    $scope.fetchThongKeData();
};
