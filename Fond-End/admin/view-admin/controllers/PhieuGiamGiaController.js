window.PhieuGiamGiaController = function ($scope, $http, $timeout) {
    $scope.dsKhuyenMai = [];
    $scope.dsTrangThaiGiamGia = [];
    $scope.searchText = ''; // Giá trị tìm kiếm
    $scope.searchMaVoucher = ''; // Giá trị tìm kiếm mã voucher
    $scope.selectedTrangThaiGiamGia = ''; // Trạng thái tìm kiếm
    $scope.selectedDiscountType = ''; // Kiểu giảm giá tìm kiếm
    // Function to fetch data
    $scope.fetchData = function (url, target, logMessage) {
        $http.get(url).then(function (response) {
            $scope[target] = response.data;
            console.log(logMessage, response.data);
        }, function (error) {
            console.error('Error fetching data:', error);
        });
    };

    $scope.fetchData('http://localhost:8080/api/admin/vouchers', 'dsKhuyenMai', 'Fetched Voucher:');
    $scope.fetchData('http://localhost:8080/api/admin/trang_thai_giam_gia', 'dsTrangThaiGiamGia', 'Fetched trạng thái:');
    // Hàm kiểm tra nếu nhấn phím Enter thì gọi tìm kiếm
    $scope.checkEnterKey = function (event) {
        if (event.key === 'Enter') {  // Kiểm tra xem có phải phím Enter không
            $scope.searchVoucher();
        }
    };
    $scope.filterVoucherByDiscountType = function () {
        var discountType = $scope.selectedDiscountType;

        if (discountType === '') {
            // Nếu kiểu giảm giá không được chọn, lấy toàn bộ danh sách
            $scope.fetchData('http://localhost:8080/api/admin/vouchers', 'dsKhuyenMai', 'Fetched Voucher:');
        } else {
            // Nếu kiểu giảm giá được chọn, gọi API tìm kiếm theo kiểu giảm giá
            $http.get('http://localhost:8080/api/admin/vouchers/search/kieuGiamGia', {
                params: { kieuGiamGia: discountType }
            })
                .then(function (response) {
                    $scope.dsKhuyenMai = response.data;
                    console.log('Voucher filtered by discount type:', response.data);
                })
                .catch(function (error) {
                    console.error('Error fetching filtered vouchers:', error);
                });
        }
    };

    // Hàm tìm kiếm và lọc voucher theo kiểu giảm giá
    $scope.deleteVoucher = function (id) {
        if (confirm("Bạn có chắc chắn muốn xóa voucher này không?")) {
            $http.delete('http://localhost:8080/api/admin/vouchers/' + id)
                .then(function (response) {
                    Swal.fire({
                        title: 'Thành công!',
                        text: 'Voucher đã được xóa thành công.',
                        icon: 'success',
                        confirmButtonText: 'OK'
                    }).then(() => {
                        // Cập nhật lại danh sách vouchers
                        $scope.fetchData('http://localhost:8080/api/admin/vouchers', 'dsKhuyenMai', 'Fetched KhuyenMai:');
                    });
                }, function (error) {
                    console.error('Error deleting voucher:', error);
                    Swal.fire({
                        title: 'Lỗi!',
                        text: 'Có lỗi xảy ra khi xóa voucher.',
                        icon: 'error',
                        confirmButtonText: 'OK'
                    });
                });
        }
    };

    // Function to update product status
    $scope.updateTrangThai = function (idVoucher, newStatus) {
        $http.put('http://localhost:8080/api/admin/vouchers/update_status/' + idVoucher, { trangThai: newStatus })
            .then(function (response) {
                Swal.fire({
                    title: 'Cập nhật thành công!',
                    text: 'Trạng thái voucher đã được cập nhật.',
                    icon: 'success',
                    confirmButtonText: 'OK'
                });
            })
            .catch(function (error) {
                console.error('Error updating product status:', error);
                Swal.fire({
                    title: 'Lỗi!',
                    text: 'Có lỗi xảy ra khi cập nhật trạng thái.',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            });
    };

    $scope.getRowClass = function (trangThai) {
        console.log('Trang thái voucher:', trangThai); // Log trạng thái voucher

        switch (trangThai) {
            case 'Đã phát hành':
                return 'bg-success text-white'; // Màu xanh
            case 'Đã sử dụng':
                return 'bg-secondary text-white'; // Màu xám
            case 'Hết hạn':
                return 'bg-danger text-white'; // Màu đỏ
            case 'Chưa phát hành':
                return 'bg-warning text-dark'; // Màu vàng
            case 'Bị hủy':
                return 'bg-dark text-white'; // Màu đen
            case 'Đang diễn ra':
                return 'bg-info text-white'; // Màu xanh nhạt cho trạng thái đang diễn ra
            default:
                return ''; // Trả về lớp rỗng nếu không có trạng thái nào khớp
        }
    };
    $scope.formatCurrency = function (value) {
        if (value == null) return '';
        return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') + ' VNĐ';
    };
    $scope.onSearchMaVoucherChange = function () {
        // Kiểm tra nếu ô nhập liệu mã voucher trống thì gọi lại hàm fetchData để lấy tất cả voucher
        if ($scope.searchMaVoucher.trim() === '') {
            $scope.fetchData('http://localhost:8080/api/admin/vouchers', 'dsKhuyenMai', 'Fetched KhuyenMai:');
        }
    };
    // Function to search vouchers based on selected conditions
    $scope.searchVoucherNangCao = function () {
        let searchParams = {};

        // Add search params based on available fields
        if ($scope.searchMaVoucher) {
            searchParams.maVoucher = $scope.searchMaVoucher;
        }
        if ($scope.selectedTrangThaiGiamGia) {
            searchParams.trangThaiId = $scope.selectedTrangThaiGiamGia;
        }
        if ($scope.selectedDiscountType !== '') {
            searchParams.kieuGiamGia = ($scope.selectedDiscountType === 'true');
        }

        // Send the search request to the backend
        let searchUrl = 'http://localhost:8080/api/admin/vouchers/search?';

        // Dynamically add search params to URL
        Object.keys(searchParams).forEach(function (key, index) {
            searchUrl += key + '=' + searchParams[key];
            if (index < Object.keys(searchParams).length - 1) {
                searchUrl += '&';
            }
        });

        // Execute the search request
        $http.get(searchUrl).then(function (response) {
            if (response.data.length > 0) {
                $scope.dsKhuyenMai = response.data;
            } else {
                $scope.dsKhuyenMai = [];
                alert('Không tìm thấy kết quả phù hợp với điều kiện tìm kiếm.');
            }
        }, function (error) {
            console.error('Error during search:', error);
        });
    };

    // Function to check if the Enter key is pressed for search
    $scope.checkEnterKey = function (event) {
        if (event.keyCode === 13) { // Enter key
            $scope.searchVoucher();
        }
    };
    // Hàm tìm kiếm voucher
    // Hàm tìm kiếm và lọc voucher theo kiểu giảm giá
    $scope.searchVoucher = function () {
        var searchQueryTenVoucher = $scope.searchText.trim();  // Tìm kiếm theo tên voucher
        if (searchQueryTenVoucher === '') {
            $scope.fetchData('http://localhost:8080/api/admin/vouchers', 'dsKhuyenMai', 'Fetched KhuyenMai:');
            return;
        }
        // Bước 1: Tìm kiếm theo tên voucher
        if (searchQueryTenVoucher) {
            $scope.searchVoucherByName(searchQueryTenVoucher);
        }
    };

    // Hàm xóa dữ liệu trong các trường tìm kiếm
    $scope.clearSearch = function (field) {
        if (field === 'maVoucher') {
            $scope.searchMaVoucher = '';
        } else if (field === 'trangThai') {
            $scope.selectedTrangThaiGiamGia = '';
        } else if (field === 'discountType') {
            $scope.selectedDiscountType = '';
        }
        $scope.searchVoucher(); // Gọi lại hàm tìm kiếm sau khi xóa
    };


    $scope.searchVoucherByCode = function (maVoucher) {
        maVoucher = maVoucher.trim();
        if (maVoucher === '') {
            // Nếu mã voucher trống, gọi lại tất cả voucher
            $scope.fetchData('http://localhost:8080/api/admin/vouchers', 'dsKhuyenMai', 'Fetched KhuyenMai:');
            return;
        }

        // Gọi API để tìm kiếm theo mã voucher
        $http.get('http://localhost:8080/api/admin/vouchers/search/maVoucher', {
            params: { maVoucher: maVoucher }
        })
            .then(function (response) {
                if (response.data && response.data.length > 0) {
                    // Cập nhật danh sách voucher tìm được
                    $scope.dsKhuyenMai = response.data;
                } else {
                    $scope.dsKhuyenMai = [];
                }
            })
            .catch(function (error) {
                console.error('Error during search by code:', error);
                $scope.dsKhuyenMai = [];
            });
    };

    $scope.searchVoucherByTrangThai = function (trangThaiId) {
        if (!trangThaiId) {
            return;
        }

        $http.get('http://localhost:8080/api/admin/vouchers/search/trangThaiVoucher', {
            params: { trangThaiId: trangThaiId }
        })
            .then(function (response) {
                if (response.data && response.data.length > 0) {
                    $scope.dsKhuyenMai = response.data;
                } else {
                    $scope.dsKhuyenMai = [];
                    alert("Không tìm thấy voucher với trạng thái này.");
                }
            })
            .catch(function (error) {
                console.error('Error during search by status:', error);
                $scope.dsKhuyenMai = [];
            });
    };
    $scope.searchVoucherByName = function (tenVoucher) {
        tenVoucher = tenVoucher.trim().replace(/\s+/g, ' ');;
        if (tenVoucher === '') {
            // Nếu mã voucher trống, gọi lại tất cả voucher
            $scope.fetchData('http://localhost:8080/api/admin/vouchers', 'dsKhuyenMai', 'Fetched KhuyenMai:');
            return;
        }

        // Gọi API để tìm kiếm theo mã voucher
        $http.get('http://localhost:8080/api/admin/vouchers/search/tenVoucher', {
            params: { tenVoucher: tenVoucher }
        })
            .then(function (response) {
                if (response.data && response.data.length > 0) {
                    // Cập nhật danh sách voucher tìm được
                    $scope.dsKhuyenMai = response.data;
                } else {
                    $scope.dsKhuyenMai = [];
                }
            })
            .catch(function (error) {
                console.error('Error during search by code:', error);
                $scope.dsKhuyenMai = [];
            });
    };



}
