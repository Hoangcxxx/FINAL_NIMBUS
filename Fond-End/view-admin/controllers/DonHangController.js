window.DonHangController = function ($scope, $http) {
    $scope.dsHoaDon = [];
    $scope.selectedTab = 'tat-ca'; // Mặc định chọn tab "Tất cả"

    // Đếm số lượng đơn hàng theo trạng thái
    $scope.countOrders = function (status) {
        let count = 0;

        // Kiểm tra nếu trạng thái là trống (tất cả đơn hàng)
        if (status === '') {
            count = $scope.dsHoaDon.length; // Trả về tất cả các đơn hàng khi trạng thái là 'Tất cả'
        } else {
            count = $scope.dsHoaDon.filter(function(item) {
                // Kiểm tra và so sánh trạng thái đơn hàng (chú ý chữ hoa và chữ thường)
                return item.trangThaiHoaDon.trim() === status.trim();
            }).length;
        }

        // Log số lượng cho từng trạng thái
        console.log('Số lượng đơn hàng trạng thái "' + status + '": ' + count);

        return count;
    };

    function fetchdsHoaDon() {
        $http.get('http://localhost:8080/api/admin/hoa_don')
            .then(function (response) {
                if (response.data.length > 0) {
                    $scope.dsHoaDon = response.data;
                    $scope.$evalAsync();  // Buộc AngularJS nhận biết sự thay đổi dữ liệu mà không gây lỗi
                    console.log("Dữ liệu đơn hàng:", $scope.dsHoaDon);
                } else {
                    console.log("Không tìm thấy đơn hàng.");
                }
            }, function (error) {
                console.error('Lỗi khi lấy dữ liệu đơn hàng:', error);
            });
    }
    fetchdsHoaDon();

    // Define tabs and their associated status
    $scope.tabs = [
        { id: 'tat-ca', label: 'Tất cả', status: '' },
        { id: 'chua-xu-ly', label: 'Chờ xử lý', status: 'Chờ Xử Lý' },  // Chỉnh lại giá trị trạng thái
        { id: 'chao-giao', label: 'Chờ giao hàng', status: 'Chờ Giao Hàng' },      // Chỉnh lại giá trị trạng thái
        { id: 'hoan-thanh', label: 'Hoàn thành', status: 'Hoàn Thành' },  // Chỉnh lại giá trị trạng thái
        { id: 'da-huy', label: 'Đã hủy', status: 'Đã Hủy' }               // Chỉnh lại giá trị trạng thái
    ];

    // Function to switch the selected tab
    $scope.selectTab = function (tabId) {
        $scope.selectedTab = tabId;
    };
};
