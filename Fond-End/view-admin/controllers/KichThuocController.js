window.KichThuocController = function ($scope, $http) {
    $scope.dsKichThuoc = [];

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
        fetchData('http://localhost:8080/api/ad_kich_thuoc', 'dsKichThuoc');
    };

    $scope.KichThuoc = {
        tenKichThuoc: "",
        moTa: ""
    };

    $scope.chinhSuaKichThuoc = function (item) {
        $scope.KichThuoc = angular.copy(item); // Sao chép dữ liệu để sửa
        $('#addKichThuocModal').modal('show'); // Hiển thị modal
        $scope.isEditing = true; // Đánh dấu là đang chỉnh sửa
    };

    $scope.onCreate = function () {
        $http({
            method: 'POST',
            url: "http://localhost:8080/api/ad_kich_thuoc",
            data: $scope.KichThuoc
        }).then(function (response) {
            alert('Chúc mừng bạn tạo mới thành công');
            $scope.onRefresh(); // Refresh the data
            $scope.resetModal(); // Reset the modal
        });
    };

    $scope.onUpdate = function () {
        $http({
            method: 'PUT',
            url: `http://localhost:8080/api/ad_kich_thuoc/${$scope.KichThuoc.idKichThuoc}`,
            data: $scope.KichThuoc
        }).then(function (response) {
            alert('Chất liệu đã được cập nhật thành công');
            $scope.onRefresh(); // Refresh the data
            $scope.resetModal(); // Reset the modal
        });
    };



    $scope.resetModal = function () {
        $scope.KichThuoc = {
            tenKichThuoc: "",
            moTa: ""
        };
        $scope.isEditing = false; // Reset editing state
        $('#addKichThuocModal').modal('hide'); // Hide the modal
    };



    $scope.onDelete = function (idKichThuoc) {
        if (confirm('Bạn có chắc chắn muốn xóa chất liệu này không?')) {
            $http({
                method: 'DELETE',
                url: `http://localhost:8080/api/ad_kich_thuoc/${idKichThuoc}`
            }).then(function (response) {
                alert('Chất liệu đã được xóa thành công');
                location.reload();
            });
        }
    };

    // Gọi hàm lấy dữ liệu khi controller được khởi tạo
    fetchData('http://localhost:8080/api/ad_kich_thuoc', 'dsKichThuoc');

    $scope.onSearch = function () {
        const query = $scope.tenKichThuoc; // Lấy tên màu sắc từ ô nhập
        const url = `http://localhost:8080/api/ad_kich_thuoc/${query}`; // URL tìm kiếm theo tên

        if (query) {
            $http.get(url).then(function (response) {
                $scope.dsKichThuoc = response.data; // Cập nhật danh sách màu sắc
            }).catch(function (error) {
                console.error('Error searching colors:', error);
            });
        } else {
            $scope.onRefresh(); // Nếu ô tìm kiếm rỗng, làm mới danh sách
        }
    };
};
