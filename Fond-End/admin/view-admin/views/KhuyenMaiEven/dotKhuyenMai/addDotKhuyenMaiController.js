window.addDotKhuyenMaiController = function ($scope, $http, $location, $routeParams) {
    $scope.dsSanPham = [];
    $scope.dsDanhMuc = []; // Khởi tạo danh sách danh mục

    // Khởi tạo thông tin đợt giảm giá
    $scope.dotGiamGia = {
        tenDotGiamGia: '',
        giaTriGiamGia: 0,
        moTa: '',
        ngayBatDau: '',
        ngayKetThuc: '',
        kieuGiamGia: true,
        sanPhamList: []
    };
    $scope.fetchSanPhamByDanhMuc = function (idDanhMuc) {
        if (!idDanhMuc) {
            $scope.fetchSanPham(); // Nếu không có danh mục, lấy lại tất cả sản phẩm
            return;
        }
        $http.get(`http://localhost:8080/api/admin/dot_giam_gia/findDanhMuc/${idDanhMuc}`)
            .then(function (response) {
                $scope.dsSanPham = response.data;
                if ($scope.dsSanPham.length === 0) {
                    $scope.message = 'Không có sản phẩm nào trong danh mục này.';
                } else {
                    $scope.message = ''; // Reset thông báo nếu có sản phẩm
                }
            }, function (error) {
                console.error('Error fetching products by category:', error);
                $scope.message = 'Có lỗi xảy ra khi lấy sản phẩm.';
            });
    };

    // Hàm lấy danh sách sản phẩm
    $scope.fetchSanPham = function () {
        $http.get('http://localhost:8080/api/admin/dot_giam_gia/san_pham_chua_giam_gia')
            .then(function (response) {
                $scope.dsSanPham = response.data;
            }, function (error) {
                console.error('Error fetching products:', error);
            });
    };

    // Hàm lấy danh sách danh mục
    $scope.fetchData = function (url, scopeProperty, successMessage) {
        $http.get(url)
            .then(function (response) {
                $scope[scopeProperty] = response.data;
                console.log(successMessage, response.data);
            }, function (error) {
                console.error('Error fetching data:', error);
            });
    };

    // Hàm lấy thông tin đợt giảm giá theo ID
    $scope.fetchDotGiamGia = function (id) {
        $http.get(`http://localhost:8080/api/admin/dot_giam_gia/detail/${id}`)
            .then(function (response) {
                $scope.dotGiamGia = response.data.dotGiamGia;

                // Chuyển đổi kiểu giảm giá về boolean
                $scope.dotGiamGia.kieuGiamGia = $scope.dotGiamGia.kieuGiamGia === true;

                // Chuyển đổi chuỗi ngày thành đối tượng Date
                if ($scope.dotGiamGia.ngayBatDau) {
                    $scope.dotGiamGia.ngayBatDau = new Date($scope.dotGiamGia.ngayBatDau);
                }
                if ($scope.dotGiamGia.ngayKetThuc) {
                    $scope.dotGiamGia.ngayKetThuc = new Date($scope.dotGiamGia.ngayKetThuc);
                }

                // Cập nhật trạng thái sản phẩm đã chọn
                $scope.dsSanPham.forEach(product => {
                    product.selected = response.data.giamGiaSanPhamList.some(voucher => voucher.sanPham.idSanPham === product.idSanPham);
                });
            }, function (error) {
                console.error('Error fetching discount batch:', error);
            });
    };

    // Kiểm tra nếu có ID để cập nhật
    if ($routeParams.id) {
        $scope.fetchDotGiamGia($routeParams.id);
    }

    // Gọi hàm để lấy danh sách sản phẩm ngay khi controller khởi tạo
    $scope.fetchSanPham();
    // Gọi hàm để lấy danh sách danh mục
    $scope.fetchData('http://localhost:8080/api/admin/danh_muc', 'dsDanhMuc', 'Fetched categories:');

    // Hàm thêm hoặc cập nhật đợt giảm giá
    // Hàm lưu đợt giảm giá
    $scope.saveDotGiamGia = function () {
        // Kiểm tra kiểu giảm giá và giá trị giảm
        console.log("Kiểu giảm giá: ", $scope.dotGiamGia.kieuGiamGia);
        console.log("Giá trị giảm: ", $scope.dotGiamGia.giaTriGiamGia);

        // Kiểm tra kiểu giảm giá và giá trị giảm
        if ($scope.dotGiamGia.kieuGiamGia === true) { // Kiểu giảm giá là tiền mặt
            console.log("Kiểu giảm giá là tiền mặt.");
            // Kiểm tra giá trị giảm tiền không lớn hơn giá sản phẩm
            const selectedSanPham = $scope.dsSanPham.filter(item => item.selected);
            const invalidPrice = selectedSanPham.some(item => $scope.dotGiamGia.giaTriGiamGia > item.giaBan);
            if (invalidPrice) {
                alert('Giá trị giảm không thể lớn hơn giá bán của sản phẩm.');
                return;
            }
        } else if ($scope.dotGiamGia.kieuGiamGia === false || $scope.dotGiamGia.kieuGiamGia === 'false') {
            // Kiểu giảm giá là phần trăm
            console.log("Kiểu giảm giá là phần trăm.");
            if ($scope.dotGiamGia.giaTriGiamGia > 100) {
                alert('Phần trăm giảm không được vượt quá 100%.');
                console.log("Giảm giá phần trăm không hợp lệ: ", $scope.dotGiamGia.giaTriGiamGia);
                return;  // Dừng việc lưu đợt giảm giá
            }
        }


        // Kiểm tra thông tin đợt giảm giá
        if (!$scope.dotGiamGia.tenDotGiamGia || !$scope.dotGiamGia.giaTriGiamGia || !$scope.dotGiamGia.ngayBatDau || !$scope.dotGiamGia.ngayKetThuc) {
            alert('Vui lòng điền đầy đủ thông tin.');
            return;
        }

        // Kiểm tra ngày bắt đầu và ngày kết thúc hợp lệ
        if (new Date($scope.dotGiamGia.ngayBatDau) >= new Date($scope.dotGiamGia.ngayKetThuc)) {
            alert('Ngày kết thúc phải sau ngày bắt đầu.');
            return;
        }

        // Kiểm tra ít nhất một sản phẩm được chọn
        const selectedSanPham = $scope.dsSanPham.filter(item => item.selected);
        if (selectedSanPham.length === 0) {
            alert('Vui lòng chọn ít nhất một sản phẩm cho đợt giảm giá.');
            return;
        }

        // Cập nhật danh sách sản phẩm cho đợt giảm giá
        $scope.dotGiamGia.sanPhamList = selectedSanPham.map(item => ({
            sanPham: { idSanPham: item.idSanPham }
        }));

        // Xử lý thêm mới hoặc cập nhật đợt giảm giá
        const request = $routeParams.id
            ? $http.put(`http://localhost:8080/api/admin/dot_giam_gia/${$routeParams.id}`, $scope.dotGiamGia) // Cập nhật
            : $http.post('http://localhost:8080/api/admin/dot_giam_gia/create_with_san_pham', $scope.dotGiamGia); // Thêm mới

        request.then(function (response) {
            alert('Đợt giảm giá đã được lưu thành công!');
            $location.path('/dot_giam_gia');
        }).catch(function (error) {
            console.error('Lỗi khi lưu đợt giảm giá:', error);
            alert('Có lỗi xảy ra: ' + (error.data.message || ''));
        });
    };





    $scope.selectAll = false;

    // Hàm chọn hoặc bỏ chọn tất cả sản phẩm
    $scope.toggleSelectAll = function () {
        angular.forEach($scope.dsSanPham, function (item) {
            item.selected = $scope.selectAll;
        });
    };

    // Hàm cập nhật trạng thái chọn tất cả
    $scope.updateSelectAll = function () {
        $scope.selectAll = $scope.dsSanPham.every(function (item) {
            return item.selected;
        });
    };

    // Hàm cập nhật đơn vị giảm giá khi kiểu giảm giá thay đổi
    $scope.updateUnit = function () {
        // Log giá trị của kieuGiamGia
        console.log("Kiểu giảm giá hiện tại:", $scope.dotGiamGia.kieuGiamGia);

        // Kiểm tra kiểu giảm giá và cập nhật đơn vị
        if ($scope.dotGiamGia.kieuGiamGia === true || $scope.dotGiamGia.kieuGiamGia === 'true') {
            $scope.giamGiaUnit = 'VND'; // Nếu kiểu giảm giá là tiền mặt
        } else {
            $scope.giamGiaUnit = '%'; // Nếu kiểu giảm giá là phần trăm
        }

        // Log ra giá trị đơn vị
        console.log("Đơn vị giảm giá hiện tại:", $scope.giamGiaUnit);
    };

    // Gọi hàm updateUnit mỗi khi kiểu giảm giá thay đổi
    $scope.$watch('dotGiamGia.kieuGiamGia', $scope.updateUnit);

    // Gọi hàm updateUnit ngay khi controller được khởi tạo
    $scope.updateUnit();
};
