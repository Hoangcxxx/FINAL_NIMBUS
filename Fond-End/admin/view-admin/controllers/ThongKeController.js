window.ThongKeController = function ($scope, $http) {
    $scope.tongSoLuongBanRa = 0; // Biến để lưu tổng số lượng bán ra
    $scope.dsThongKeTrangThai = [];
    $scope.dsSanPhamBanChay = [];
    // Lấy dữ liệu từ API
    $http.get('http://localhost:8080/api/admin/thong_ke/getAllThongKe')
        .then(function (response) {
            // Dữ liệu trả về từ API
            $scope.dsSanPhamBanChay = response.data;

        })
        .catch(function (error) {
            console.error("Lỗi khi lấy dữ liệu:", error);
        });
    // Lấy dữ liệu từ API
    $http.get('http://localhost:8080/api/admin/thong_ke/tong_so_luong_ban_ra_thanh_nay')
        .then(function (response) {
            // Log toàn bộ dữ liệu trả về để kiểm tra cấu trúc
            console.log("Dữ liệu trả về từ API:", response.data);

            // Kiểm tra xem `tongSoLuongBanRa` có tồn tại không
            if (response.data && response.data[0] && response.data[0].tongSoLuongBanRa !== undefined) {
                $scope.tongSoLuongBanRa = response.data[0].tongSoLuongBanRa; // Lưu giá trị trả về vào biến
            } else {
                console.error("Không tìm thấy trường tongSoLuongBanRa trong dữ liệu trả về");
            }
        })
        .catch(function (error) {
            console.error("Lỗi khi lấy tổng số lượng bán ra:", error);
        });
    $http.get('http://localhost:8080/api/admin/thong_ke/san_pham_ban_ra_hom_nay')
        .then(function (response) {
            // Log toàn bộ dữ liệu trả về để kiểm tra cấu trúc
            console.log("Dữ liệu trả về từ API:", response.data);

            // Kiểm tra xem `tongSoLuongBanRa` có tồn tại không
            if (response.data && response.data[0] && response.data[0].sanPhamBanRa !== undefined) {
                $scope.sanPhamBanRa = response.data[0].sanPhamBanRa; // Lưu giá trị trả về vào biến
            } else {
                console.error("Không tìm thấy trường tongSoLuongBanRa trong dữ liệu trả về");
            }
        })
        .catch(function (error) {
            console.error("Lỗi khi lấy tổng số lượng bán ra:", error);
        });
    $http.get('http://localhost:8080/api/admin/thong_ke/tong_hoa_don_thang_nay')
        .then(function (response) {
            // Log toàn bộ dữ liệu trả về để kiểm tra cấu trúc
            console.log("Dữ liệu trả về từ API:", response.data);

            // Kiểm tra xem `tongSoLuongBanRa` có tồn tại không
            if (response.data && response.data[0] && response.data[0].tongHoaDonThangNay !== undefined) {
                $scope.tongHoaDonThangNay = response.data[0].tongHoaDonThangNay; // Lưu giá trị trả về vào biến
            } else {
                console.error("Không tìm thấy trường tongSoLuongBanRa trong dữ liệu trả về");
            }
        })
        .catch(function (error) {
            console.error("Lỗi khi lấy tổng số lượng bán ra:", error);
        });
    $http.get('http://localhost:8080/api/admin/thong_ke/tong_hoa_don_hom_nay')
        .then(function (response) {
            // Log toàn bộ dữ liệu trả về để kiểm tra cấu trúc
            console.log("Dữ liệu trả về từ API:", response.data);

            // Kiểm tra xem `tongSoLuongBanRa` có tồn tại không
            if (response.data && response.data[0] && response.data[0].tongHoaDonHomNay !== undefined) {
                $scope.tongHoaDonHomNay = response.data[0].tongHoaDonHomNay; // Lưu giá trị trả về vào biến
            } else {
                console.error("Không tìm thấy trường tongSoLuongBanRa trong dữ liệu trả về");
            }
        })
        .catch(function (error) {
            console.error("Lỗi khi lấy tổng số lượng bán ra:", error);
        });
    $http.get('http://localhost:8080/api/admin/thong_ke/tong_san_pham_trong_thang_nay')
        .then(function (response) {
            // Log toàn bộ dữ liệu trả về để kiểm tra cấu trúc
            console.log("Dữ liệu trả về từ API:", response.data);

            // Kiểm tra xem `tongSoLuongBanRa` có tồn tại không
            if (response.data && response.data[0] && response.data[0].tongSanPhamTrongThangNay !== undefined) {
                $scope.tongSanPhamTrongThangNay = response.data[0].tongSanPhamTrongThangNay; // Lưu giá trị trả về vào biến
            } else {
                console.error("Không tìm thấy trường tongSoLuongBanRa trong dữ liệu trả về");
            }
        })
        .catch(function (error) {
            console.error("Lỗi khi lấy tổng số lượng bán ra:", error);
        });

    // Lấy dữ liệu từ API tổng hợp số lượng trạng thái hóa đơn
    $http.get('http://localhost:8080/api/admin/thong_ke/tong_so_luong_trang_thai_hoa_don')
        .then(function (response) {
            // Lưu dữ liệu trả về vào biến $scope.dsThongKeTrangThai
            $scope.dsThongKeTrangThai = response.data;

            // Dữ liệu cho biểu đồ
            const labels = [];
            const data = [];
            const backgroundColor = ['#FFCE56', '#4BC0C0', '#FF6384', '#FF5733', '#8E44AD', '#20c997']; // Màu sắc cho từng trạng thái

            // Lặp qua dữ liệu và chuẩn bị labels và data cho biểu đồ
            $scope.dsThongKeTrangThai.forEach(item => {
                labels.push(item.tenTrangThai);
                data.push(item.soLuongHoaDon);
            });

            // Lấy biểu đồ canvas và vẽ biểu đồ
            const ctx = document.getElementById('orderChart').getContext('2d');
            const orderChart = new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: labels,
                    datasets: [{
                        data: data,
                        backgroundColor: backgroundColor
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'bottom',
                        }
                    }
                }
            });
        })
        .catch(function (error) {
            console.error("Lỗi khi lấy dữ liệu thống kê trạng thái hóa đơn:", error);
        });


};