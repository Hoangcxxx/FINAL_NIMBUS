window.detailHoaDonController = function ($scope, $http, $routeParams) {
    var idHoaDon = $routeParams.idHoaDon;
    $scope.trangThaiHoaDon = [];
    $scope.hoaDon = [];
    $scope.ghiChu = { trangThaiId: null, moTa: "" };
    $scope.modalButtonText = '';  // Khởi tạo modalButtonText

    if (!idHoaDon) {
        console.error('idHoaDon không hợp lệ');
        return;
    }

    console.log('idHoaDon:', idHoaDon);  // Log ra idHoaDon

    // Hàm gọi API chung cho việc lấy trạng thái và thông tin hóa đơn
    function fetchStatusAndInvoiceData() {
        $http.get('http://localhost:8080/api/admin/hoa_don/findTrangThaiHoaDon/' + idHoaDon)
            .then(function (response) {
                if (Array.isArray(response.data)) {
                    $scope.trangThaiHoaDon = response.data;
                    updateCurrentStatus();
                } else {
                    console.error('Dữ liệu trả về từ API không phải mảng:', response.data);
                }
            })
            .catch(function (error) {
                console.error('Error fetching status data:', error);
            });

        $http.get('http://localhost:8080/api/admin/hoa_don/findHoaDonCT/' + idHoaDon)
            .then(function (response) {
                console.log('Dữ liệu trả về từ API hóa đơn:', response.data);
                $scope.hoaDon = response.data;
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy dữ liệu hóa đơn:', error);
            });
    }

    // Cập nhật trạng thái hiện tại vào ghi chú và modalButtonText
    function updateCurrentStatus() {
        if ($scope.trangThaiHoaDon.length > 0) {
            var currentStatus = $scope.trangThaiHoaDon[$scope.trangThaiHoaDon.length - 1];
            $scope.ghiChu.trangThaiId = currentStatus.idLoaiTrangThaiHoaDon;
            $scope.ghiChu.moTa = currentStatus.moTa;
            updateModalButtonText(currentStatus.idLoaiTrangThaiHoaDon);
        }
    }

    // Cập nhật modalButtonText dựa trên trạng thái hóa đơn
    function updateModalButtonText(statusId) {
        switch (statusId) {
            case 2: // Trạng thái "Chờ xác nhận"
                $scope.modalButtonText = 'Xác nhận đơn hàng';
                break;
            case 4: // Trạng thái "Chờ thanh toán"
                $scope.modalButtonText = 'Xác nhận thanh toán';
                break;
            case 6: // Trạng thái "Chờ giao hàng"
                $scope.modalButtonText = 'Giao hàng';
                break;
            case 7: // Trạng thái "Đang vận chuyển"
                $scope.modalButtonText = 'Hoàn thành';
                break;
            case 8: // Trạng thái "Đã hủy"
                $scope.modalButtonText = 'Hủy';
                break;
            default:
                $scope.modalButtonText = ''; // Nếu không có trạng thái nào hợp lệ
        }
    }

    // Gọi API để lấy trạng thái của hóa đơn và thông tin hóa đơn
    fetchStatusAndInvoiceData();

    // Lấy danh sách các loại trạng thái hợp lệ
    $http.get('http://localhost:8080/api/admin/hoa_don/loai_trang_thai')
        .then(function (response) {
            $scope.trangThaiList = response.data;
            $scope.filteredTrangThaiList = $scope.trangThaiList.filter(function (item) {
                return [3, 5, 7, 8, 9].includes(item.idLoaiTrangThai);
            });
        })
        .catch(function (error) {
            console.error('Error fetching status types:', error);
        });

    // Kiểm tra trạng thái có tồn tại không theo id
    $scope.isStatusAvailableById = function (statusId) {
        return $scope.trangThaiHoaDon.some(function (status) {
            return status.idLoaiTrangThaiHoaDon === statusId;
        });
    };

    // Lấy mô tả trạng thái và ngày tạo theo id
    function getStatusById(statusId) {
        return $scope.trangThaiHoaDon.find(function (status) {
            return status.idLoaiTrangThaiHoaDon === statusId;
        });
    }

    $scope.getStatusDescriptionById = function (statusId) {
        var status = getStatusById(statusId);
        return status ? status.moTa : '';
    };

    $scope.getStatusDateById = function (statusId) {
        var status = getStatusById(statusId);
        return status ? status.ngayTao : '';
    };

    // Hàm lọc trạng thái lớn nhất
    function getMaxStatus(statusOrder) {
        return statusOrder.find(function (statusId) {
            return $scope.isStatusAvailableById(statusId);
        }) || 0; // Trả về 0 nếu không tìm thấy trạng thái hợp lệ
    }

    $scope.getStatus2 = function () {
        return getMaxStatus([1, 2, 3]);
    };

    $scope.getStatus4 = function () {
        return getMaxStatus([4, 5]);
    };

    // Hàm cập nhật trạng thái hóa đơn
    $scope.updateTrangThaiHoaDon = function () {
        console.log("Trang thái ID:", $scope.ghiChu.trangThaiId);  // Kiểm tra giá trị
        if ($scope.ghiChu.trangThaiId && idHoaDon) {
            // Thực hiện API update
            $http.post('http://localhost:8080/api/admin/hoa_don/updateLoaiTrangThai?idHoaDon=' + idHoaDon + '&idLoaiTrangThai=' + $scope.ghiChu.trangThaiId)
                .then(function (response) {
                    if (response.data.success) {
                        console.log("Cập nhật thành công");
                        alert("Cập nhật trạng thái hóa đơn thành công");
                        fetchStatusAndInvoiceData();  // Tải lại dữ liệu
                        $('#ghiChuModal').modal('hide'); // Đóng modal
                        $scope.resetModal();
                    } else {
                        console.error('Error updating status:', response.data.message);
                    }
                })
                .catch(function (error) {
                    console.error('Error calling API:', error);
                });
        } else {
            alert('Vui lòng chọn trạng thái trước khi cập nhật!');
        }
    };
    
    

    // Reset modal trạng thái
    $scope.resetModal = function () {
        $scope.ghiChu = { trangThaiId: null, moTa: "" };
    };

    // Khi người dùng mở modal, hiển thị trạng thái hiện tại
    $scope.openModal = function () {
        console.log('Trạng thái hiện tại: ', $scope.ghiChu.trangThaiId);
    };

};
