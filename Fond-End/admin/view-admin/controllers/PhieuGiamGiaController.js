window.PhieuGiamGiaController = function ($scope, $http) {
    $scope.dsKhuyenMai = [];
    $scope.dsTrangThaiGiamGia = [];
    $scope.searchText = ''; // Giá trị tìm kiếm

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
    // Hàm tìm kiếm voucher
    $scope.searchVoucher = function () {
        var searchQueryMaVoucher = $scope.searchMaVoucher.trim();  // Lấy giá trị mã voucher
        var searchQueryTenVoucher = $scope.searchText.trim();  // Lấy giá trị tên voucher

        // Nếu không có gì trong phần mã voucher, gọi lại hàm fetchData để lấy tất cả các voucher
        if (searchQueryMaVoucher === '' && searchQueryTenVoucher === '') {
            $scope.fetchData('http://localhost:8080/api/admin/vouchers', 'dsKhuyenMai', 'Fetched KhuyenMai:');
        } else {
            // Kiểm tra nếu có giá trị mã voucher thì tìm kiếm theo mã trước
            if (searchQueryMaVoucher) {
                $scope.searchVoucherByCode(searchQueryMaVoucher);
            }
            if (searchQueryTenVoucher) {
                // Nếu không có mã voucher, tìm kiếm theo tên voucher
                $scope.searchVoucherByName(searchQueryTenVoucher);
            }
        }
    };


    $scope.searchVoucherByCode = function (maVoucher) {
        maVoucher = maVoucher.trim(); // Loại bỏ khoảng trắng đầu và cuối

        if (maVoucher === '') {
            return;
        }

        console.log('Searching voucher by code:', maVoucher);

        $http.get('http://localhost:8080/api/admin/vouchers/search/maVoucher', {
            params: { maVoucher: maVoucher }
        })
            .then(function (response) {
                console.log('Voucher search result:', response.data);

                if (!response.data || response.data.length === 0) {
                    // Nếu không có voucher nào được tìm thấy
                    Swal.fire({
                        title: 'Không tìm thấy mã voucher!',
                        text: 'Không có voucher nào khớp với mã bạn nhập.',
                        icon: 'warning',
                        confirmButtonText: 'OK'
                    });

                    $scope.dsKhuyenMai = []; // Xóa danh sách voucher hiển thị
                } else {
                    // Kiểm tra và xử lý voucher bị xóa
                    if (response.data.trangThaiGiamGia.tenTrangThaiGiamGia === 'Bị xóa') {
                        $scope.dsKhuyenMai = []; // Không hiển thị voucher đã bị xóa
                        Swal.fire({
                            title: 'Thông báo!',
                            text: 'Voucher đã bị xóa và không còn hiệu lực.',
                            icon: 'warning',
                            confirmButtonText: 'OK'
                        });
                    } else {
                        $scope.dsKhuyenMai = [response.data]; // Hiển thị voucher nếu không bị xóa
                        Swal.fire({
                            title: 'Tìm kiếm thành công!',
                            text: 'Đã tìm thấy voucher với mã: ' + maVoucher,
                            icon: 'success',
                            confirmButtonText: 'OK'
                        });
                    }
                }
            })
            .catch(function (error) {
                console.error('Error during search by code:', error);
                $scope.dsKhuyenMai = []; // Nếu có lỗi, xóa danh sách hiển thị
                Swal.fire({
                    title: 'Lỗi!',
                    text: 'Có lỗi xảy ra trong quá trình tìm kiếm.',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            });
    };





    // Hàm tìm kiếm theo tên voucher
    $scope.searchVoucherByName = function (tenVoucher) {
        if (tenVoucher.trim() === '') {
            return;  // Nếu không có tên voucher, dừng hàm
        }

        console.log('Searching voucher by name:', tenVoucher); // Log tên voucher đang tìm kiếm

        $http.get('http://localhost:8080/api/admin/vouchers/search/tenVoucher', {
            params: { tenVoucher: tenVoucher }
        })
            .then(function (response) {
                console.log('Voucher search result:', response.data); // Log kết quả tìm kiếm

                // Kiểm tra dữ liệu trả về từ API và hiển thị
                if (response.data && response.data.length > 0) {
                    $scope.dsKhuyenMai = response.data;
                } else {
                    // Nếu không có kết quả nào, thông báo cho người dùng
                    $scope.dsKhuyenMai = [];
                    alert("Không tìm thấy voucher với tên: " + tenVoucher);
                }
            })
            .catch(function (error) {
                console.error('Lỗi khi tìm kiếm theo tên voucher:', error);
                $scope.dsKhuyenMai = [];  // Nếu có lỗi, xóa danh sách hiển thị
            });
    };




}
