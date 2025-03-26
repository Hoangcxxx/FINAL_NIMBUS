window.DonHangController = function ($scope, $http) {
    $scope.dsHoaDon = [];
    $scope.selectedTab = 'tat-ca'; // Mặc định chọn tab "Tất cả"
    $scope.searchQuery = ''; // Khởi tạo giá trị tìm kiếm

    // Đếm số lượng đơn hàng theo trạng thái
    $scope.countOrders = function (status) {
        let count = 0;

        if (status === '') {
            count = $scope.dsHoaDon.length; // Trả về tất cả các đơn hàng khi trạng thái là 'Tất cả'
        } else {
            count = $scope.dsHoaDon.filter(function (item) {
                return item.tenLoaiTrangThaiHoaDon.trim() === status.trim();
            }).length;
        }

        return count;
    };

    function fetchdsHoaDon() {
        $http.get('http://localhost:8080/api/admin/hoa_don')
            .then(function (response) {
                if (response.data.length > 0) {
                    $scope.dsHoaDon = response.data;
                    $scope.$evalAsync();  // Buộc AngularJS nhận biết sự thay đổi dữ liệu
                }
            }, function (error) {
                console.error('Lỗi khi lấy dữ liệu đơn hàng:', error);
            });
    }

    // Tìm kiếm hóa đơn theo mã
    $scope.searchHoaDon = function () {
        const searchQuery = $scope.searchQuery.trim();
        if (searchQuery === '') {
            // Nếu không có gì được nhập, hiển thị tất cả đơn hàng
            fetchdsHoaDon();
        } else {
            // Gọi API search theo mã hóa đơn
            $http.get('http://localhost:8080/api/admin/hoa_don/search?maHoaDon=' + searchQuery)
                .then(function (response) {
                    if (response.data.length > 0) {
                        $scope.dsHoaDon = response.data;
                    } else {
                        $scope.dsHoaDon = [];
                    }
                }, function (error) {
                    console.error('Lỗi khi tìm kiếm đơn hàng:', error);
                });
        }
    };

    fetchdsHoaDon();  // Lấy tất cả đơn hàng khi lần đầu tiên vào trang

    // Define tabs and their associated status
    $scope.tabs = [
        { id: 'tat-ca', label: 'Tất cả', status: '' },
        { id: 'chua-xu-ly', label: 'Chờ xác nhận', status: 'Chờ xác nhận' },
        { id: 'chao-giao', label: 'Chờ giao hàng', status: 'Chờ giao hàng' },
        { id: 'cho-giao', label: 'Đang giao hàng', status: 'Đang giao hàng' },
        { id: 'hoan-thanh', label: 'Giao hàng thành công', status: 'Giao hàng thành công' },
        { id: 'da-huy', label: 'Đã hủy', status: 'Đã hủy' },
        { id: 'hoan-tra', label: 'Hoàn trả', status: 'Đổi trả hàng' }
    ];

    // Function to switch the selected tab
    $scope.selectTab = function (tabId) {
        $scope.selectedTab = tabId;
    };
};
