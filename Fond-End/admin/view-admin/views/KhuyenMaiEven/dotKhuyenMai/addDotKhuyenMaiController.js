window.addDotKhuyenMaiController = function ($scope, $http, $location, $routeParams) {
    $scope.dsSanPham = [];
    $scope.dsDaSanPham = [];
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
    // Hàm lấy thông tin đợt giảm giá theo ID
    $scope.fetchSanPhamDaGiamGia = function (id) {
        $http.get(`http://localhost:8080/api/admin/dot_giam_gia/san_pham_da_giam_gia/${id}`)
            .then(function (response) {
                $scope.dsDaSanPham = response.data;
                console.log('Fetched discounted products:', response.data);
            }, function (error) {
                console.error('Error fetching discounted products:', error);
                $scope.message = 'Có lỗi xảy ra khi lấy sản phẩm đã giảm giá.';
            });
    };

    // Kiểm tra nếu có ID để cập nhật
    if ($routeParams.id) {
        $scope.fetchDotGiamGia($routeParams.id);
        $scope.fetchSanPhamDaGiamGia($routeParams.id);
    }

    // Gọi hàm để lấy danh sách sản phẩm ngay khi controller khởi tạo
    $scope.fetchSanPham();
    // Gọi hàm để lấy danh sách danh mục
    $scope.fetchData('http://localhost:8080/api/admin/danh_muc', 'dsDanhMuc', 'Fetched categories:');



    // Hàm lưu đợt giảm giá
    $scope.saveDotGiamGia = function () {
        // Kiểm tra kiểu giảm giá và giá trị giảm
        console.log("Kiểu giảm giá: ", $scope.dotGiamGia.kieuGiamGia);
        console.log("Giá trị giảm: ", $scope.dotGiamGia.giaTriGiamGia);

        if ($scope.dotGiamGia.kieuGiamGia === true) { // Kiểu giảm giá là tiền mặt
            console.log("Kiểu giảm giá là tiền mặt.");
            const selectedSanPham = $scope.dsSanPham.filter(item => item.selected);
            const invalidPrice = selectedSanPham.some(item => $scope.dotGiamGia.giaTriGiamGia > item.giaBan);
            if (invalidPrice) {
                Swal.fire({
                    icon: 'error',
                    title: 'Giá trị giảm không hợp lệ',
                    text: 'Giá trị giảm không thể lớn hơn giá bán của sản phẩm.'
                });
                return;
            }
        } else if ($scope.dotGiamGia.kieuGiamGia === false || $scope.dotGiamGia.kieuGiamGia === 'false') {
            console.log("Kiểu giảm giá là phần trăm.");
            if ($scope.dotGiamGia.giaTriGiamGia > 100) {
                Swal.fire({
                    icon: 'error',
                    title: 'Phần trăm giảm không hợp lệ',
                    text: 'Phần trăm giảm không được vượt quá 100%.'
                });
                return;
            }
        }

        // Kiểm tra thông tin đợt giảm giá
        if (!$scope.dotGiamGia.tenDotGiamGia || !$scope.dotGiamGia.giaTriGiamGia || !$scope.dotGiamGia.ngayBatDau || !$scope.dotGiamGia.ngayKetThuc) {
            Swal.fire({
                icon: 'warning',
                title: 'Thông tin chưa đầy đủ',
                text: 'Vui lòng điền đầy đủ thông tin.'
            });
            return;
        }

        // Kiểm tra ngày bắt đầu và ngày kết thúc hợp lệ
        if (new Date($scope.dotGiamGia.ngayBatDau) >= new Date($scope.dotGiamGia.ngayKetThuc)) {
            Swal.fire({
                icon: 'error',
                title: 'Ngày không hợp lệ',
                text: 'Ngày kết thúc phải sau ngày bắt đầu.'
            });
            return;
        }

        // Nếu không phải là thao tác cập nhật, kiểm tra ít nhất một sản phẩm được chọn
        if (!$routeParams.id) {
            const selectedSanPham = $scope.dsSanPham.filter(item => item.selected);
            if (selectedSanPham.length === 0) {
                Swal.fire({
                    icon: 'warning',
                    title: 'Chưa chọn sản phẩm',
                    text: 'Vui lòng chọn ít nhất một sản phẩm cho đợt giảm giá.'
                });
                return;
            }
            $scope.dotGiamGia.sanPhamList = selectedSanPham.map(item => ({
                sanPham: { idSanPham: item.idSanPham }
            }));
        } else {
            const selectedSanPham = $scope.dsSanPham.filter(item => item.selected);
            const selectedSanPhamIds = selectedSanPham.map(item => item.idSanPham);
            const currentSanPhamIds = $scope.dsDaSanPham.map(item => item.idSanPham);

            if (angular.equals(selectedSanPhamIds.sort(), currentSanPhamIds.sort())) {
                $scope.dotGiamGia.sanPhamList = $scope.dsDaSanPham.map(item => ({
                    sanPham: { idSanPham: item.idSanPham }
                }));
            } else {
                const allSelectedSanPhamIds = [...selectedSanPhamIds, ...currentSanPhamIds];
                $scope.dotGiamGia.sanPhamList = allSelectedSanPhamIds.map(id => ({
                    sanPham: { idSanPham: id }
                }));
            }
        }

        const request = $routeParams.id
            ? $http.put(`http://localhost:8080/api/admin/dot_giam_gia/${$routeParams.id}`, $scope.dotGiamGia)
            : $http.post('http://localhost:8080/api/admin/dot_giam_gia/create_with_san_pham', $scope.dotGiamGia);

        request.then(function (response) {
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: 'Đợt giảm giá đã được lưu thành công!'
            });
            $location.path('/dot_giam_gia');
        }).catch(function (error) {
            console.error('Lỗi khi lưu đợt giảm giá:', error);
            Swal.fire({
                icon: 'error',
                title: 'Có lỗi xảy ra',
                text: error.data.message || 'Vui lòng thử lại.'
            });
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
    $scope.updateProductSelection = function (product) {
        // Đảm bảo rằng sanPhamList đã được khởi tạo trước khi thao tác
        if (!$scope.dotGiamGia.sanPhamList) {
            $scope.dotGiamGia.sanPhamList = [];
        }

        if (product.selected) {
            // Thêm sản phẩm vào danh sách sản phẩm đợt giảm giá
            $scope.dotGiamGia.sanPhamList.push({ sanPham: { idSanPham: product.idSanPham } });
        } else {
            // Loại bỏ sản phẩm khỏi danh sách sản phẩm đợt giảm giá
            const index = $scope.dotGiamGia.sanPhamList.findIndex(item => item.sanPham.idSanPham === product.idSanPham);
            if (index > -1) {
                $scope.dotGiamGia.sanPhamList.splice(index, 1);
            }
        }
    };





    // Hàm xóa sản phẩm giảm giá
    $scope.xoaSanPhamGiamGia = function (idDotGiamGia, idSanPham) {
        Swal.fire({
            title: 'Bạn chắc chắn muốn xóa sản phẩm này?',
            text: "Sản phẩm sẽ bị xóa khỏi đợt giảm giá!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Xóa',
            cancelButtonText: 'Hủy'
        }).then((result) => {
            if (result.isConfirmed) {
                const url = `http://localhost:8080/api/admin/dot_giam_gia/xoa/${idDotGiamGia}/${idSanPham}`;

                $http.delete(url)
                    .then(function (response) {
                        if (response.status === 200) {
                            Swal.fire({
                                icon: 'success',
                                title: 'Xóa thành công',
                                text: 'Sản phẩm đã bị xóa khỏi đợt giảm giá.'
                            });
                            $scope.dsDaSanPham = $scope.dsDaSanPham.filter(function (item) {
                                return item.idSanPham !== idSanPham;
                            });
                            $scope.fetchSanPham();
                        } else {
                            Swal.fire({
                                icon: 'error',
                                title: 'Có lỗi xảy ra',
                                text: 'Không thể xóa sản phẩm khỏi đợt giảm giá.'
                            });
                        }
                    })
                    .catch(function (error) {
                        console.error('Error during API request:', error);
                        if (error.status === 401) {
                            Swal.fire({
                                icon: 'error',
                                title: 'Lỗi xác thực',
                                text: 'Bạn không có quyền thực hiện hành động này. Vui lòng đăng nhập.'
                            });
                        } else {
                            Swal.fire({
                                icon: 'error',
                                title: 'Có lỗi xảy ra',
                                text: 'Không thể xóa sản phẩm khỏi đợt giảm giá.'
                            });
                        }
                    });
            }
        });
    };




};
