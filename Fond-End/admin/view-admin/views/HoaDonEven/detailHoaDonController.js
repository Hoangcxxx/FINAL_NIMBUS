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
    // Hàm gọi API chung cho việc lấy trạng thái và thông tin hóa đơn
    function fetchStatusAndInvoiceData() {
        // Lấy trạng thái hóa đơn
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

        // Khởi tạo biến tổng tiền trước khi giảm giá
        $scope.totalBeforeDiscount = 0;

        // Lấy dữ liệu chi tiết hóa đơn
        $http.get('http://localhost:8080/api/admin/hoa_don/findHoaDonCT/' + idHoaDon)
            .then(function (response) {
                console.log('Dữ liệu trả về từ API hóa đơn:', response.data);
                $scope.hoaDon = response.data;
                $scope.totalAmount = $scope.hoaDon.thanhTien;  // Gán giá trị thanhTien vào totalAmount

                // Kiểm tra và tính tổng tiền trước khi giảm giá từ sanPhamCT (sản phẩm chi tiết hóa đơn)
                if ($scope.sanPhamCT && Array.isArray($scope.sanPhamCT) && $scope.sanPhamCT.length > 0) {
                    $scope.totalBeforeDiscount = $scope.sanPhamCT.reduce(function (accumulator, product) {
                        // Kiểm tra nếu product.tongTien là hợp lệ
                        if (product.tongTien && !isNaN(product.tongTien)) {
                            return accumulator + product.tongTien;  // Cộng giá trị hợp lệ
                        }
                        return accumulator;  // Nếu không hợp lệ, không cộng vào tổng
                    }, 0);
                    console.log('Tổng tiền trước khi giảm giá:', $scope.totalBeforeDiscount);
                } else {
                    console.warn('Dữ liệu sản phẩm chi tiết hóa đơn không hợp lệ hoặc trống:', $scope.sanPhamCT);
                }

                // Kiểm tra vai trò khách hàng để quyết định ẩn/hiển thị email và địa chỉ
                if ($scope.hoaDon.vaiTro.idVaiTro === 3) {
                    $scope.showEmailAndAddress = false;  // Nếu vai trò là 3 (khách hàng lẽ), ẩn email và địa chỉ
                    $scope.isKhachHangLe = true;
                } else {
                    $scope.showEmailAndAddress = true;  // Nếu vai trò là 2 (Khách hàng), hiển thị đầy đủ thông tin
                }

                // Kiểm tra voucher
                if ($scope.hoaDon.idVoucher === null) {
                    $scope.voucherInfo = 'Không sử dụng voucher';
                } else {
                    $http.get('http://localhost:8080/api/admin/hoa_don/findVoucherHoaDon/' + idHoaDon)
                        .then(function (voucherResponse) {
                            $scope.voucher = voucherResponse.data;
                            if ($scope.voucher.length > 0) {
                                var voucher = $scope.voucher[0];
                                $scope.voucherInfo = {
                                    maVoucher: voucher.maVoucher,
                                    tenVoucher: voucher.tenVoucher,
                                    giaTriGiamGia: voucher.giaTriGiamGia,
                                    kieuGiamGia: voucher.kieuGiamGia
                                };

                                // Tính toán giảm giá
                                if (voucher.kieuGiamGia) {
                                    // Giảm giá theo tiền mặt (kieuGiamGia = true)
                                    $scope.discountAmount = voucher.giaTriGiamGia;
                                } else {
                                    // Giảm giá theo phần trăm (kieuGiamGia = false)
                                    $scope.discountAmount = ($scope.totalBeforeDiscount * voucher.giaTriGiamGia) / 100;
                                }

                                // Cập nhật lại tổng thanh toán sau giảm giá
                                $scope.finalAmount = $scope.totalBeforeDiscount - $scope.discountAmount;
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

        // Lấy dữ liệu sản phẩm chi tiết hóa đơn
        $http.get('http://localhost:8080/api/admin/hoa_don/findSanPhamCTHoaDon/' + idHoaDon)
            .then(function (response) {
                console.log('Dữ liệu trả về từ API sản phẩm chi tiết:', response.data);
                // Cập nhật lại dữ liệu sản phẩm chi tiết
                $scope.sanPhamCT = response.data;

                // Sau khi dữ liệu sản phẩm đã được tải, tính toán lại tổng tiền
                if ($scope.sanPhamCT && Array.isArray($scope.sanPhamCT) && $scope.sanPhamCT.length > 0) {
                    $scope.totalBeforeDiscount = $scope.sanPhamCT.reduce(function (accumulator, product) {
                        // Kiểm tra nếu product.tongTien là hợp lệ
                        if (product.tongTien && !isNaN(product.tongTien)) {
                            return accumulator + product.tongTien;  // Cộng giá trị hợp lệ
                        }
                        return accumulator;  // Nếu không hợp lệ, không cộng vào tổng
                    }, 0);
                    console.log('Tổng tiền trước khi giảm giá sau khi tải sản phẩm:', $scope.totalBeforeDiscount);
                } else {
                    console.warn('Sản phẩm chi tiết không hợp lệ hoặc trống:', $scope.sanPhamCT);
                }
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy dữ liệu sản phẩm chi tiết hóa đơn:', error);
            });
    }

    // Gọi hàm để thực thi
    fetchStatusAndInvoiceData();





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
            // case 8: 
            //     $scope.modalButtonText = 'Hủy';
            //     break;
            default:
                $scope.modalButtonText = ''; // Nếu không có trạng thái nào hợp lệ
        }
    }

    // Lấy danh sách các loại trạng thái hợp lệ
    $http.get('http://localhost:8080/api/admin/hoa_don/loai_trang_thai')
        .then(function (response) {
            $scope.trangThaiList = response.data;
            $scope.filteredTrangThaiList = $scope.trangThaiList.filter(function (item) {
                return [3, 5, 6, 7, 8].includes(item.idLoaiTrangThai);
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



    $scope.updateTrangThaiHoaDon = function () {
        console.log("Trang thái hiện tại:", $scope.ghiChu.trangThaiId);  // Kiểm tra giá trị

        if ($scope.ghiChu.trangThaiId && idHoaDon) {  // Kiểm tra nếu có idHoaDon
            let nextStatusId;

            // Kiểm tra trạng thái hiện tại và xác định trạng thái tiếp theo
            switch ($scope.ghiChu.trangThaiId) {
                case 2:  // Chờ xác nhận -> Chuyển sang Chờ giao hàng (3)
                    nextStatusId = 3;
                    break;
                case 3:  // Chờ giao hàng -> Chuyển sang Chờ thanh toán (4)
                    nextStatusId = 4;
                    break;
                case 4:  // Chờ thanh toán -> Chuyển sang Chờ thanh toán (5)
                    nextStatusId = 5;
                    break;
                case 5:  // Chờ thanh toán -> Chuyển sang Đang vận chuyển (6)
                    nextStatusId = 6;
                    break;
                case 6:  // Chờ thanh toán -> Chuyển sang Đang vận chuyển (7)
                    nextStatusId = 7;

                    // Kiểm tra nếu đang chuyển từ trạng thái "Chờ thanh toán" (6) sang "Xác nhận thanh toán"
                    // Kiểm tra xem đã có lịch sử thanh toán với nhân viên xác nhận chưa
                    $http.get('http://localhost:8080/api/admin/lich_su_thanh_toan/' + idHoaDon)
                        .then(function (response) {
                            // Kiểm tra nếu có lịch sử thanh toán và xem có nhân viên xác nhận không
                            const hasPaymentHistory = response.data.some(function (payment) {
                                // Kiểm tra nếu `tenNhanVien` khác "N/A" và không phải `null`
                                return payment.tenNhanVien !== "N/A" && payment.tenNhanVien !== null;
                            });

                            if (!hasPaymentHistory) {
                                // Nếu không có nhân viên xác nhận thanh toán
                                Swal.fire({
                                    icon: 'warning',
                                    title: 'Không thể cập nhật trạng thái',
                                    text: 'Vui lòng xác nhận thanh toán trước khi chuyển trạng thái!'
                                });
                                return; // Dừng cập nhật trạng thái nếu không có xác nhận thanh toán
                            } else {
                                // Nếu đã có nhân viên xác nhận thanh toán, tiếp tục cập nhật trạng thái
                                updateStatusInBackend(nextStatusId);
                            }
                        })
                        .catch(function (error) {
                            // Xử lý lỗi trong quá trình lấy lịch sử thanh toán
                            console.error('Lỗi khi kiểm tra lịch sử thanh toán:', error);
                            Swal.fire({
                                icon: 'error',
                                title: 'Lỗi khi kiểm tra thanh toán',
                                text: "Đã xảy ra lỗi khi kiểm tra lịch sử thanh toán."
                            });
                        });

                    return;  // Dừng việc chuyển trạng thái nếu phải kiểm tra lịch sử thanh toán

                case 7:  // Đang vận chuyển -> Hoàn thành
                    nextStatusId = 8;  // Trạng thái hoàn thành (có thể là trạng thái 8)
                    break;
                default:
                    Swal.fire({
                        icon: 'warning',
                        title: 'Không thể chuyển trạng thái',
                        text: 'Trạng thái hiện tại không hỗ trợ việc chuyển trạng thái tiếp theo.'
                    });
                    return;  // Nếu không phải trạng thái hợp lệ, thoát khỏi hàm
            }

            // Kiểm tra trạng thái cần được cập nhật có hợp lệ hay không
            if (nextStatusId) {
                // Gọi API cập nhật trạng thái nếu không phải trạng thái "Chờ thanh toán"
                updateStatusInBackend(nextStatusId);
            }

        } else {
            // Nếu chưa chọn trạng thái hoặc chưa có idHoaDon
            Swal.fire({
                icon: 'warning',
                title: 'Vui lòng kiểm tra thông tin',
                text: 'Vui lòng chọn trạng thái và hóa đơn trước khi cập nhật!'
            });
        }
    };

    // Hàm cập nhật trạng thái trong backend
    function updateStatusInBackend(nextStatusId) {
        $http.post('http://localhost:8080/api/admin/hoa_don/updateLoaiTrangThai', null, {
            params: {
                idHoaDon: idHoaDon,  // Truyền tham số idHoaDon
                idLoaiTrangThai: nextStatusId,  // Truyền tham số trạng thái
                idNhanVien: $scope.userId  // Truyền ID người dùng
            }
        })
            .then(function (response) {
                // Kiểm tra nếu backend trả về thành công
                if (response.data.success) {
                    console.log("Cập nhật thành công");
                    Swal.fire({
                        icon: 'success',
                        title: 'Cập nhật thành công',
                        text: 'Trạng thái hóa đơn đã được cập nhật thành công!'
                    });

                    // Tải lại dữ liệu trạng thái hóa đơn sau khi cập nhật
                    fetchStatusAndInvoiceData();  // Gọi lại hàm fetchStatusAndInvoiceData để tải lại dữ liệu
                } else {
                    // Nếu không thành công, hiển thị thông báo lỗi từ backend
                    console.error('Lỗi khi cập nhật trạng thái:', response.data.message);
                    Swal.fire({
                        icon: 'error',
                        title: 'Cập nhật thất bại',
                        text: response.data.message || "Không rõ lỗi từ server"
                    });
                }
            })
            .catch(function (error) {
                // Nếu có lỗi khi gọi API
                console.error('Error calling API:', error);
                let errorMessage = error.data && error.data.message ? error.data.message : "Đã xảy ra lỗi trong quá trình cập nhật trạng thái.";
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi khi chuyển trạng thái',
                    text: errorMessage
                });
            });
    }

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

                        // Hiển thị thông báo thành công với Swal
                        Swal.fire({
                            icon: 'success',
                            title: 'Cập nhật thành công!',
                            text: response.data.message
                        }).then(function () {
                            // Đóng modal sau khi hiển thị thông báo
                            $('#xacNhanModal').modal('hide'); // Đóng modal bằng jQuery
                        });
                    } else {
                        console.error('Lỗi khi cập nhật thanh toán:', response.data.message);

                        // Hiển thị lỗi với Swal
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi!',
                            text: response.data.message
                        });
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

                    // Hiển thị lỗi khi gặp sự cố gọi API với Swal
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi!',
                        text: "Không thể cập nhật lịch sử thanh toán."
                    });
                });
        } else {
            // Thông báo khi người dùng chưa nhập đủ thông tin
            Swal.fire({
                icon: 'warning',
                title: 'Thông báo!',
                text: 'Vui lòng nhập đủ thông tin thanh toán!'
            });
        }
    };


    // Khi người dùng mở modal, tự động điền thông tin vào modal
    $('#xacNhanModal').on('show.bs.modal', function () {
        $scope.openPaymentModal();
    });



};