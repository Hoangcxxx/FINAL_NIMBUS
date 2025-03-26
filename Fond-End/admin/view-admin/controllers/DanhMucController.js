window.DanhMucController = function ($scope, $http) {
    $scope.dsDanhMuc = [];

    // Hàm để lấy danh sách danh mục
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
        fetchData('http://localhost:8080/api/admin/danh_muc', 'dsDanhMuc');
    };

    $scope.DanhMuc = {
        tenDanhMuc: "",
        moTa: ""
    };

    $scope.chinhSuaDanhMuc = function (item) {
        $scope.DanhMuc = angular.copy(item); // Sao chép dữ liệu để sửa
        $('#addDanhMucModal').modal('show'); // Hiển thị modal
        $scope.isEditing = true; // Đánh dấu là đang chỉnh sửa
    };

    $scope.onCreate = function () {
        $http({
            method: 'POST',
            url: "http://localhost:8080/api/admin/danh_muc",
            data: $scope.DanhMuc
        }).then(function (response) {
            alert('Chúc mừng bạn tạo mới thành công');
            location.reload(); // Reload the page
        });
    };

    $scope.onUpdate = function () {
        $http({
            method: 'PUT',
            url: `http://localhost:8080/api/admin/danh_muc/${$scope.DanhMuc.idDanhMuc}`,
            data: $scope.DanhMuc
        }).then(function (response) {
            alert('Danh mục đã được cập nhật thành công');
            location.reload(); // Reload the page
        });
    };

    $scope.resetModal = function () {
        $scope.DanhMuc = {
            tenDanhMuc: "",
            moTa: ""
        };
        $scope.isEditing = false; // Reset editing state
        $('#addDanhMucModal').modal('hide'); // Hide the modal
    };

    $scope.onDelete = function (idDanhMuc) {
        if (confirm('Bạn có chắc chắn muốn xóa danh mục này không?')) {
            $http({
                method: 'DELETE',
                url: `http://localhost:8080/api/admin/danh_muc/${idDanhMuc}`
            }).then(function (response) {
                alert('Danh mục đã được xóa thành công');
                location.reload(); // Reload the page
            });
        }
    };

    // Gọi hàm lấy dữ liệu khi controller được khởi tạo
    fetchData('http://localhost:8080/api/admin/danh_muc', 'dsDanhMuc');


    $scope.onSearch = function () {
        const query = $scope.tenDanhMuc; // Lấy tên màu sắc từ ô nhập
        const url = `http://localhost:8080/api/admin/danh_muc/${query}`; // URL tìm kiếm theo tên

        if (query) {
            $http.get(url).then(function (response) {
                $scope.dsDanhMuc = response.data; // Cập nhật danh sách màu sắc
            }).catch(function (error) {
                console.error('Error searching colors:', error);
            });
        } else {
            $scope.onRefresh(); // Nếu ô tìm kiếm rỗng, làm mới danh sách
        }
    };
};
