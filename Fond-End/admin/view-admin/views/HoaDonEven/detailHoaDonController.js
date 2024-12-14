window.detailHoaDonController = function ($scope, $http, $routeParams) {
    var idHoaDon = $routeParams.idHoaDon;
    $scope.trangThaiHoaDon = [];
    $scope.hoaDon = [];
    $scope.sanPhamCT = [];
    $scope.lichSuThanhToan = [];
    $scope.voucher = [];
    $scope.ghiChu = { trangThaiId: null, moTa: "" };
    $scope.modalButtonText = '';  // Khởi tạo modalButtonText
    $scope.showEmailAndAddress = true; // Mặc định là true (hiển thị)
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
                $scope.totalAmount = $scope.hoaDon.thanhTien;  // Gán giá trị thanhTien vào totalAmount
                // Kiểm tra vai trò khách hàng để quyết định ẩn/hiển thị email và địa chỉ
                if ($scope.hoaDon.vaiTro.idVaiTro === 3) {
                    // Nếu vai trò là 3 (không phải khách hàng), ẩn email và địa chỉ
                    $scope.showEmailAndAddress = false;
                } else {
                    // Nếu vai trò là 2 (Khách hàng), hiển thị đầy đủ thông tin
                    $scope.showEmailAndAddress = true;
                }

                // Kiểm tra voucher
                if ($scope.hoaDon.idVoucher === null) {
                    $scope.voucherInfo = 'Không sử dụng voucher';
                } else {
                    // Lấy thông tin voucher từ API nếu có
                    $http.get('http://localhost:8080/api/admin/hoa_don/findVoucherHoaDon/' + idHoaDon)
                        .then(function (voucherResponse) {
                            $scope.voucher = voucherResponse.data;
                            if ($scope.voucher.length > 0) {
                                // Cấu trúc lại thông tin voucher để hiển thị như giao diện yêu cầu
                                $scope.voucherInfo = {
                                    maVoucher: $scope.voucher[0].maVoucher,
                                    tenVoucher: $scope.voucher[0].tenVoucher,
                                    giaTriGiamGia: $scope.voucher[0].giaTriGiamGia,
                                    kieuGiamGia: $scope.voucher[0].kieuGiamGia
                                };
                            }
                        })
                        .catch(function (error) {
                            console.error('Lỗi khi lấy thông tin voucher:', error);
                        });
                }
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy dữ liệu hóa đơn:', error);
            });
        $http.get('http://localhost:8080/api/admin/hoa_don/findSanPhamCTHoaDon/' + idHoaDon)
            .then(function (response) {
                console.log('Dữ liệu trả về từ API hóa đơn:', response.data);
                $scope.sanPhamCT = response.data;
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy dữ liệu hóa đơn:', error);
            });
        $http.get('http://localhost:8080/api/admin/hoa_don/findVoucherHoaDon/' + idHoaDon)
            .then(function (response) {
                console.log('Dữ liệu trả về từ API voucher:', response.data);
                $scope.voucher = response.data;
                calculateDiscount();
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy dữ liệu hóa đơn:', error);
            });

    }
    // Hàm tính toán tổng giảm giá
    function calculateDiscount() {
        $scope.total_discount = 0; // Khởi tạo lại giá trị tổng giảm giá
        if ($scope.voucher.length > 0) {
            let voucher = $scope.voucher[0]; // Giả sử voucher chỉ có 1 phần tử
            if (voucher.kieuGiamGia === true) {
                // Giảm giá theo giá trị tiền
                $scope.total_discount = voucher.giaTriGiamGia;
            } else {
                // Giảm giá theo phần trăm (giả sử tổng giá trị đơn hàng là totalAmount)
                if ($scope.totalAmount) {
                    $scope.total_discount = (voucher.giaTriGiamGia / 100) * $scope.totalAmount;
                }
            }
        }
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
            case 4: // Trạng thái "Chờ giao hàng"
                $scope.modalButtonText = 'Giao hàng';
                break;
            case 5: // Trạng thái "Chờ giao hàng"
                $scope.modalButtonText = 'Chờ thanh toán';
                break;
            case 6: // Trạng thái "Chờ thanh toán"
                $scope.modalButtonText = 'Xác nhận thanh toán';
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
                return [3, 5, 6, 7, 8, 9].includes(item.idLoaiTrangThai);
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
    $scope.getStatus6 = function () {
        return getMaxStatus([6]);
    };
    $scope.getStatus7 = function () {
        return getMaxStatus([7]);
    };
    var userInfo = localStorage.getItem('user');

    if (userInfo) {
        // Nếu có thông tin người dùng, chuyển thành đối tượng và lấy ID người dùng
        userInfo = JSON.parse(userInfo);  // Parse thông tin người dùng từ JSON
        $scope.userId = $scope.infoUser.idNguoiDung;  // Lấy ID người dùng từ thông tin đã lưu
    } else {
        // Nếu không có thông tin người dùng (người dùng chưa đăng nhập), gán userId là null
        $scope.userId = null;
        // Xóa idGioHang trong localStorage nếu người dùng chưa đăng nhập
        localStorage.removeItem("userInfo");
    }
    // Hàm cập nhật trạng thái hóa đơn
    $scope.updateTrangThaiHoaDon = function () {
        console.log("Trang thái ID:", $scope.ghiChu.trangThaiId);  // Kiểm tra giá trị

        if ($scope.ghiChu.trangThaiId && idHoaDon) {
            // Thực hiện API update
            $http.post('http://localhost:8080/api/admin/hoa_don/updateLoaiTrangThai?idHoaDon=' + idHoaDon + '&idLoaiTrangThai=' + $scope.ghiChu.trangThaiId + '&idNhanVien=' + $scope.userId)
                .then(function (response) {
                    // Kiểm tra nếu backend trả về thành công
                    if (response.data.success) {
                        console.log("Cập nhật thành công");
                        alert("Cập nhật trạng thái hóa đơn thành công");
                        fetchStatusAndInvoiceData();  // Tải lại dữ liệu
                        $('#ghiChuModal').modal('hide'); // Đóng modal
                        $scope.resetModal();
                    } else {
                        // Nếu không thành công, hiển thị thông báo lỗi từ backend
                        console.error('Error updating status:', response.data.message);
                        alert(response.data.message);  // Hiển thị thông báo lỗi từ backend
                    }
                })
                .catch(function (error) {
                    // Nếu có lỗi khi gọi API
                    console.error('Error calling API:', error);
                    alert("Không thể cập nhật trạng thái 'Hoàn thành' vì hóa đơn chưa xác nhận thanh toán.");
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

    // Lấy dữ liệu từ API
    $http.get('http://localhost:8080/api/admin/hoa_don/findTrangThaiHoaDon/' + idHoaDon)
        .then(function (response) {
            // Dữ liệu trả về từ API
            $scope.statuses = response.data;

            // Lọc ra các trạng thái cần thiết: 1, 3, 5, 6, 7, 8
            $scope.filteredStatuses = $scope.statuses.filter(function (status) {
                return [1, 3, 5, 6, 7, 8].includes(status.idLoaiTrangThaiHoaDon);
            });
        })
        .catch(function (error) {
            console.error("Lỗi khi lấy dữ liệu:", error);
        });



    // Lấy lịch sử thanh toán
    $http.get('http://localhost:8080/api/admin/lich_su_thanh_toan/' + idHoaDon)
        .then(function (response) {
            // Gán dữ liệu từ API vào biến lichSuThanhToan
            $scope.lichSuThanhToan = response.data;

            // Kiểm tra xem hóa đơn đã được xác nhận thanh toán hay chưa
            $scope.isInvoiceConfirmed = $scope.lichSuThanhToan.some(function (paymentHistory) {
                return paymentHistory.idNhanVien !== null;  // Kiểm tra nếu có nhân viên xác nhận
            });
        })
        .catch(function (error) {
            console.error('Lỗi khi lấy dữ liệu:', error);
        });


    $scope.showPaymentButton = function () {
        return $scope.lichSuThanhToan.some(function (item) {
            return item.tenNhanVien === 'N/A';
        });
    };



    // Mở modal xác nhận thanh toán và điền thông tin vào modal
    $scope.openPaymentModal = function () {
        // Gán giá trị vào biến scope khi mở modal
        $scope.amountGiven = $scope.totalAmount;
        $scope.notes = '';  // Reset ghi chú
    };

    $scope.updatePaymentHistory = function () {
        var amountGiven = document.getElementById("amountGiven").value;
        var notes = document.getElementById("notes").value;

        if (amountGiven && notes) {
            // Tạo đối tượng dữ liệu thanh toán
            var paymentData = {
                soTienThanhToan: parseInt(amountGiven),  // Chuyển số tiền thành số nguyên
                ngayGiaoDich: new Date().toISOString(), // Định dạng ngày giao dịch
                idNhanVien: $scope.userId,  // Lấy ID nhân viên từ $scope
                moTa: notes,  // Lấy ghi chú
                trangThaiThanhToan: true  // Trạng thái thanh toán
            };

            // Gửi yêu cầu API để cập nhật lịch sử thanh toán
            $http.put('http://localhost:8080/api/admin/lich_su_thanh_toan/update/' + idHoaDon, paymentData)
                .then(function (response) {
                    // Kiểm tra nếu có thông báo thành công
                    if (response.data.message) {
                        console.log(response.data.message);  // Log thông báo thành công
                        alert(response.data.message);  // Hiển thị thông báo thành công
                    } else {
                        console.error('Lỗi khi cập nhật thanh toán:', response.data.message);
                        alert(response.data.message);  // Hiển thị lỗi nếu có
                    }

                    // Gọi lại API để tải lại dữ liệu lịch sử thanh toán sau khi cập nhật
                    $http.get('http://localhost:8080/api/admin/lich_su_thanh_toan/' + idHoaDon)
                        .then(function (response) {
                            $scope.lichSuThanhToan = response.data; // Cập nhật dữ liệu
                        })
                        .catch(function (error) {
                            console.error('Lỗi khi tải lại dữ liệu lịch sử thanh toán:', error);
                        });
                })
                .catch(function (error) {
                    console.error('Lỗi khi gọi API cập nhật lịch sử thanh toán:', error);
                    alert("Không thể cập nhật lịch sử thanh toán.");
                });
        } else {
            alert('Vui lòng nhập đủ thông tin thanh toán!');
        }
    };


    // Khi người dùng mở modal, tự động điền thông tin vào modal
    $('#xacNhanModal').on('show.bs.modal', function () {
        $scope.openPaymentModal();
    });



};
