window.detailHoaDonController = function ($scope, $http, $routeParams) {
    // Khởi tạo hoaDon chi tiết trong scope
    var idHoaDon = $routeParams.idHoaDon;

    // Log ra idHoaDon để kiểm tra xem có nhận được giá trị hay không
    console.log('idHoaDon:', idHoaDon);  // Log ra idHoaDon

    if (!idHoaDon) {
        console.error('idHoaDon không hợp lệ');
        return; // Nếu không có idHoaDon, dừng việc thực hiện tiếp
    }

    // Khởi tạo hoaDon là một object, không phải mảng
    $scope.hoaDon = [];

    // Gửi yêu cầu GET đến API để lấy thông tin của hóa đơn
    $http.get('http://localhost:8080/api/admin/hoa_don/' + idHoaDon)
        .then(function (response) {
            // Nếu yêu cầu thành công, gán dữ liệu vào $scope.hoaDon
            console.log('Dữ liệu trả về từ API:', response.data);  // Log ra dữ liệu nhận được
            $scope.hoaDon = response.data;
        })
        .catch(function (error) {
            // Nếu có lỗi, log ra lỗi và có thể thông báo lỗi cho người dùng
            console.error('Lỗi khi lấy dữ liệu hóa đơn:', error);
        });
};
