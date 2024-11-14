window.MauSacController = function ($scope, $http) {
    $scope.dsMauSac = [];

    // Hàm để lấy danh sách chất liệu
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

    $scope.onRefresh = function () {
        fetchData('http://localhost:8080/api/ad_mau_sac', 'dsMauSac');
    };

    $scope.MauSac = {
        tenMauSac: "",
        moTa: ""
    };

    $scope.chinhSuaMauSac = function (item) {
        $scope.MauSac = angular.copy(item); // Sao chép dữ liệu để sửa
        $('#addMauSacModal').modal('show'); // Hiển thị modal
        $scope.isEditing = true; // Đánh dấu là đang chỉnh sửa
    };

    $scope.onCreate = function () {
        $http({
            method: 'POST',
            url: "http://localhost:8080/api/ad_mau_sac",
            data: $scope.MauSac
        }).then(function (response) {
            alert('Chúc mừng bạn tạo mới thành công');
            $scope.onRefresh(); // Refresh the data
            $scope.resetModal(); // Reset the modal
        });
    };

    $scope.onUpdate = function () {
        $http({
            method: 'PUT',
            url: `http://localhost:8080/api/ad_mau_sac/${$scope.MauSac.id}`,
            data: $scope.MauSac
        }).then(function (response) {
            alert('Chất liệu đã được cập nhật thành công');
            $scope.onRefresh(); // Refresh the data
            $scope.resetModal(); // Reset the modal
        });
    };



    $scope.resetModal = function () {
        $scope.MauSac = {
            tenMauSac: "",
            moTa: ""
        };
        $scope.isEditing = false; // Reset editing state
        $('#addMauSacModal').modal('hide'); // Hide the modal
    };



    $scope.onDelete = function (idMauSac) {
        if (confirm('Bạn có chắc chắn muốn xóa chất liệu này không?')) {
            $http({
                method: 'DELETE',
                url: `http://localhost:8080/api/ad_mau_sac/${idMauSac}`
            }).then(function (response) {
                alert('Chất liệu đã được xóa thành công');
                location.reload();
            });
        }
    };
    fetchData('http://localhost:8080/api/ad_mau_sac', 'dsMauSac');


    $scope.onSearch = function () {
        const query = $scope.tenMauSac; // Lấy tên màu sắc từ ô nhập
        const url = `http://localhost:8080/api/ad_mau_sac/${query}`; // URL tìm kiếm theo tên

        if (query) {
            $http.get(url).then(function (response) {
                $scope.dsMauSac = response.data; // Cập nhật danh sách màu sắc
            }).catch(function (error) {
                console.error('Error searching colors:', error);
            });
        } else {
            $scope.onRefresh(); // Nếu ô tìm kiếm rỗng, làm mới danh sách
        }
    };

};
