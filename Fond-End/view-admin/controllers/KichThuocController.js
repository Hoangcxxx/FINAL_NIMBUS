window.KichThuocController = function ($scope, $http, $routeParams, $location) {
    $scope.dsKichThuoc = [];
    
    // Hàm để lấy danh sách kích thước
    function fetchData(url, target) {
        $http({
            method: 'GET',
            url: url
        }).then(function (response) {
            $scope[target] = response.data;
        }, function (error) {
            console.error('Error fetching data:', error);
        });
    }

    // Hàm để lấy chi tiết kích thước
    $scope.getKichThuocById = function (id) {
        console.log("Fetching KichThuoc with ID:", id); // Log ID
        $http({
            method: 'GET',
            url: 'http://localhost:8080/api/kich_thuoc/' + id
        }).then(function (response) {
            $scope.KichThuoc = response.data;
            $('#updateKichThuocModal').modal('show');
        }, function (error) {
            console.error('Error fetching KichThuoc:', error.data);
        });
    };

    $scope.KichThuoc = {
        tenKichThuoc: "",
        moTa: ""
    };
    
    $scope.onCreate = function () {
        $http({
            method: 'POST',
            url: "http://localhost:8080/api/kich_thuoc",
            data: $scope.KichThuoc
        }).then(function (response) {
            alert('Chúc mừng bạn tạo mới thành công');
            location.reload();
        });
    };

    $scope.onUpdate = function () {
        $http({
            method: 'PUT',
            url: 'http://localhost:8080/api/kich_thuoc/' + $scope.KichThuoc.id, // Sử dụng ID từ KichThuoc
            data: $scope.KichThuoc
        }).then(function (response) {
            alert('Chúc mừng bạn chỉnh sửa thành công');
            location.reload(); // Refresh trang sau khi chỉnh sửa
        });
    };

    // Gọi hàm lấy dữ liệu khi controller được khởi tạo
    fetchData('http://localhost:8080/api/kich_thuoc', 'dsKichThuoc');
}
