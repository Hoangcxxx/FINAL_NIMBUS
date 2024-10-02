window.MauSacController = function ($scope, $http) {
    $scope.dsMauSac = [];
    // Hàm để lấy danh sách nhân viên
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

    $scope.MauSac = {
        tenMauSac: "",
        moTa: ""
    }
    $scope.onCreate = function () {
        $http({
            method: 'POST',
            url: "http://localhost:8080/api/mau_sac",
            data: $scope.MauSac
        }).then(function (response) {
            // Call api thành công
            // Thông thường, chúng ta sẽ hiển thị thông báo tạo mới thành công
            // Lưu ý: Live Server. Sau khi Post, Put, Patch thành công, trình duyệt sẽ tự refresh lại
            // ==> Buộc phải dùng alert của js để báo thành công
            alert('Chúc mừng bạn tạo mới thành công');
            location.reload()
        })
    }
    // Gọi hàm lấy dữ liệu khi controller được khởi tạo
    fetchData('http://localhost:8080/api/mau_sac', 'dsMauSac');
}
