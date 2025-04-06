window.detailHoaDonController = function ($scope, $http, $routeParams) {
    var idHoaDon = $routeParams.idHoaDon;
    $scope.trangThaiHoaDon = [];
    $scope.hoaDon = [];
    $scope.doiTra = [];
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
        $http.get('http://localhost:8080/api/admin/doi_tra/' + idHoaDon)
            .then(function (response) {
                if (Array.isArray(response.data)) {
                    $scope.doiTra = response.data;
                } else {
                    console.error('Dữ liệu trả về từ API không phải mảng:', response.data);
                }
            })
            .catch(function (error) {
                console.error('Error fetching status data:', error);
            });
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

                                // Kiểm tra và đảm bảo discountAmount không lớn hơn totalBeforeDiscount
                                if ($scope.discountAmount > $scope.totalBeforeDiscount) {
                                    $scope.discountAmount = $scope.totalBeforeDiscount;
                                }

                                // Cập nhật lại tổng thanh toán sau giảm giá
                                $scope.finalAmount = $scope.totalBeforeDiscount - $scope.discountAmount;

                                // Kiểm tra và đảm bảo finalAmount không âm
                                if ($scope.finalAmount < 0) {
                                    $scope.finalAmount = 0;
                                }

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
            case 5:
                $scope.modalButtonText = 'Giao hàng thành công';
                break;
            // case 6:
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
    $scope.getStatus8 = function () {
        return getMaxStatus([8]);
    };
    $scope.getStatus8 = function () {
        return getMaxStatus([10]);
    };
    $scope.getStatus8 = function () {
        return getMaxStatus([11]);
    };
    $scope.getStatus13 = function () {
        return getMaxStatus([13]);
    };
    $scope.getStatus14 = function () {
        return getMaxStatus([14]);
    };
    $scope.getStatus15 = function () {
        return getMaxStatus([15]);
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
                case 2:
                    nextStatusId = 3;
                    break;
                case 3:
                    nextStatusId = 4;
                    break;
                case 4:
                    nextStatusId = 5;
                    break;
                case 5:
                    nextStatusId = 6;
                    break;
                case 6:
                    nextStatusId = 7;
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
    $scope.isPaymentButtonVisible = function () {
        // Kiểm tra nếu trạng thái cuối cùng của hóa đơn là "Chờ thanh toán" (trạng thái 6)
        if ($scope.trangThaiHoaDon.length > 0) {
            var currentStatus = $scope.trangThaiHoaDon[$scope.trangThaiHoaDon.length - 1];
            // Trả về true nếu trạng thái là 6 (Chờ thanh toán)
            return currentStatus.idLoaiTrangThaiHoaDon === 6;
        }
        return false; // Nếu không có trạng thái hoặc trạng thái không phải là 6, ẩn nút
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
                    fetchStatusAndInvoiceData();
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

    $scope.canCancelOrder = function () {
        // Kiểm tra nếu có trạng thái hóa đơn
        if ($scope.trangThaiHoaDon && $scope.trangThaiHoaDon.length > 0) {
            var currentStatus = $scope.trangThaiHoaDon[$scope.trangThaiHoaDon.length - 1]; // Trạng thái hiện tại của hóa đơn

            // Kiểm tra nếu trạng thái là 5 (Đang giao hàng) hoặc 6 (Chờ thanh toán), thì không cho phép hủy
            if (currentStatus.idLoaiTrangThaiHoaDon === 1 || currentStatus.idLoaiTrangThaiHoaDon === 2 || currentStatus.idLoaiTrangThaiHoaDon === 3 || currentStatus.idLoaiTrangThaiHoaDon === 4) {
                return true; // Không thể hủy nếu đang ở trạng thái 5 hoặc 6
            }

            // Nếu trạng thái là "Đã hủy" (ID = 7), ẩn nút hủy
            if (currentStatus.idLoaiTrangThaiHoaDon !== 1 || currentStatus.idLoaiTrangThaiHoaDon !== 2 || currentStatus.idLoaiTrangThaiHoaDon !== 3 || currentStatus.idLoaiTrangThaiHoaDon !== 4) {
                return false; // Đã hủy, không cho phép hủy nữa
            }
        }

        return true; // Nếu không phải trạng thái 5, 6 hoặc 7, cho phép hủy
    };
    $scope.huyOrder = function () {
        // Kiểm tra nếu có trạng thái hóa đơn
        if ($scope.trangThaiHoaDon && $scope.trangThaiHoaDon.length > 0) {
            var currentStatus = $scope.trangThaiHoaDon[$scope.trangThaiHoaDon.length - 1]; // Trạng thái hiện tại của hóa đơn

            // Kiểm tra nếu trạng thái là 5 (Đang giao hàng) hoặc 6 (Chờ thanh toán), thì không cho phép hủy
            if (currentStatus.idLoaiTrangThaiHoaDon !== 15) {
                return false; // Không thể hủy nếu đang ở trạng thái 5 hoặc 6
            }

            // Nếu trạng thái là "Đã hủy" (ID = 7), ẩn nút hủy
            if (currentStatus.idLoaiTrangThaiHoaDon === 15) {
                return true; // Đã hủy, không cho phép hủy nữa
            }
        }

        return true; // Nếu không phải trạng thái 5, 6 hoặc 7, cho phép hủy
    };
    $scope.cancelOrder = function () {
        if (idHoaDon) {
            // Kiểm tra trước khi hủy đơn hàng
            const currentStatus = $scope.trangThaiHoaDon[$scope.trangThaiHoaDon.length - 1];

            // Kiểm tra trạng thái hiện tại có phải là "Đang giao hàng" (5) hoặc "Chờ thanh toán" (6)
            if (currentStatus.idLoaiTrangThaiHoaDon === 5 || currentStatus.idLoaiTrangThaiHoaDon === 6) {
                Swal.fire({
                    icon: 'warning',
                    title: 'Không thể hủy đơn hàng',
                    text: 'Đơn hàng hiện tại đang trong quá trình giao hàng hoặc chờ thanh toán, không thể hủy!'
                });
                return;
            }

            // Cập nhật trạng thái hủy đơn hàng (ID = 7)
            updateStatusInBackend(7);
        } else {
            Swal.fire({
                icon: 'warning',
                title: 'Vui lòng kiểm tra thông tin',
                text: 'Không có mã hóa đơn để hủy!'
            });
        }
    };
    $scope.huyDonHang = function () {
        if (idHoaDon) {
            // Kiểm tra trước khi hủy đơn hàng
            const currentStatus = $scope.trangThaiHoaDon[$scope.trangThaiHoaDon.length - 1];

            if (currentStatus.idLoaiTrangThaiHoaDon === 6) {
                updateStatusInBackend(12);
            }
        } else {
            Swal.fire({
                icon: 'warning',
                title: 'Vui lòng kiểm tra thông tin',
                text: 'Không có mã hóa đơn để hủy!'
            });
        }
    };
    $scope.hoanTra = function () {
        // Kiểm tra nếu có trạng thái hóa đơn
        if ($scope.trangThaiHoaDon && $scope.trangThaiHoaDon.length > 0) {
            var currentStatus = $scope.trangThaiHoaDon[$scope.trangThaiHoaDon.length - 1]; // Trạng thái hiện tại của hóa đơn

            // Kiểm tra nếu trạng thái là 5 (Đang giao hàng) hoặc 6 (Chờ thanh toán), thì không cho phép hủy
            if (currentStatus.idLoaiTrangThaiHoaDon !== 9) {
                return false; // Không thể hủy nếu đang ở trạng thái 5 hoặc 6
            }

            // Nếu trạng thái là "Đã hủy" (ID = 7), ẩn nút hủy
            if (currentStatus.idLoaiTrangThaiHoaDon === 9) {
                return true; // Đã hủy, không cho phép hủy nữa
            }
        }

        return true; // Nếu không phải trạng thái 5, 6 hoặc 7, cho phép hủy
    };
    $scope.sanPhamHoanTra = function () {
        // Kiểm tra nếu có trạng thái hóa đơn
        if ($scope.trangThaiHoaDon && $scope.trangThaiHoaDon.length > 0) {
            var currentStatus = $scope.trangThaiHoaDon[$scope.trangThaiHoaDon.length - 1];
            
            // Kiểm tra nếu idLoaiTrangThaiHoaDon không phải là 9, 8 hoặc 10
            if (![9, 8, 10,11].includes(currentStatus.idLoaiTrangThaiHoaDon)) {
                return false;
            }
    
            return true;
        }
    
        return true;
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
    // Khi người dùng mở modal, tự động điền thông tin vào modal
    $('#xacNhanModal').on('show.bs.modal', function () {
        $scope.openPaymentModal();
    });
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
                            // Dùng AngularJS để thao tác với DOM
                            var modalElement = angular.element(document.getElementById('xacNhanModal'));
                            if (modalElement) {
                                console.log('Modal exists in DOM, hiding modal...');

                                // Loại bỏ class 'show' nếu modal đang hiển thị
                                modalElement.removeClass('show');

                                // Đặt lại style để chắc chắn modal bị ẩn
                                modalElement.css('display', 'none');

                                // Xóa backdrop nếu có
                                var backdrop = angular.element(document.querySelector('.modal-backdrop'));
                                if (backdrop) {
                                    backdrop.remove(); // Loại bỏ backdrop nếu có
                                }

                                // Kiểm tra lại xem modal đã bị ẩn chưa
                                console.log('Modal visibility after hide:', modalElement.css('display'));
                            } else {
                                console.error('Modal with ID "xacNhanModal" does not exist in the DOM.');
                            }
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



    $scope.confirmReturn = function () {
        if (idHoaDon) {
            // Log ra idHoaDon để kiểm tra xem có giá trị hay không
            console.log('idHoaDon:', idHoaDon);

            // Log ra trạng thái hóa đơn hiện tại để kiểm tra trạng thái
            var currentStatus = $scope.trangThaiHoaDon[$scope.trangThaiHoaDon.length - 1];
            console.log('Trạng thái hóa đơn hiện tại:', currentStatus);

            // Lấy ID nhân viên từ thông tin người dùng
            var userInfo = localStorage.getItem('user');
            var userId = null;
            if (userInfo) {
                userInfo = JSON.parse(userInfo);
                userId = userInfo.idNguoiDung;  // Lấy ID nhân viên từ thông tin người dùng
            }

            // Tạo confirmation dialog với các lựa chọn
            Swal.fire({
                title: 'Chọn hành động',
                text: 'Bạn muốn xác nhận hoàn trả hay hủy hoàn trả?',
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: 'Xác nhận hoàn trả',
                cancelButtonText: 'Hủy hoàn trả',
                reverseButtons: true, // Đảo vị trí của nút "Xác nhận" và "Hủy"
            }).then((result) => {
                var idLoaiTrangThaiHoaDon;
                // Kiểm tra kết quả từ dialog
                if (result.isConfirmed) {
                    // Nếu người dùng chọn "Xác nhận hoàn trả" (trạng thái 11)
                    idLoaiTrangThaiHoaDon = 11;
                } else if (result.dismiss === Swal.DismissReason.cancel) {
                    // Nếu người dùng chọn "Hủy hoàn trả" (trạng thái 10)
                    idLoaiTrangThaiHoaDon = 10;
                }

                if (idLoaiTrangThaiHoaDon) {
                    // Tiến hành gọi API cập nhật trạng thái với idLoaiTrangThaiHoaDon mới và ID nhân viên
                    $http.put('http://localhost:8080/api/admin/doi_tra/cap-nhat-trang-thai/' + idHoaDon + '/' + idLoaiTrangThaiHoaDon + '/' + userId, {
                    })
                        .then(function (response) {
                            console.log('Dữ liệu trả về từ API:', response.data); // Log dữ liệu trả về từ API
                            Swal.fire({
                                icon: 'success',
                                title: 'Cập nhật thành công',
                                text: 'Trạng thái hóa đơn đã được cập nhật thành công!'
                            });
                            // Tải lại dữ liệu sau khi cập nhật
                            fetchStatusAndInvoiceData();
                        })
                        .catch(function (error) {
                            console.error('Lỗi khi gọi API:', error);
                            Swal.fire({
                                icon: 'error',
                                title: 'Cập nhật thất bại',
                                text: 'Có lỗi khi cập nhật trạng thái hoàn trả.'
                            });
                        });
                }
            });
        } else {
            Swal.fire({
                icon: 'warning',
                title: 'Vui lòng kiểm tra thông tin',
                text: 'Không có mã hóa đơn để xác nhận hoàn trả!'
            });
        }
    };

    $scope.canConfirmReturn = function () {
        // Log ra trạng thái hóa đơn để kiểm tra giá trị của trạng thái
        var currentStatus = $scope.trangThaiHoaDon[$scope.trangThaiHoaDon.length - 1];
        console.log('Trạng thái hóa đơn hiện tại:', currentStatus);

        // Kiểm tra trạng thái có thể hoàn trả không
        // Giả sử trạng thái có thể hoàn trả là trạng thái "Chờ xác nhận" (idLoaiTrangThaiHoaDon = 9)
        var canReturn = currentStatus.idLoaiTrangThaiHoaDon === 9;  // Thay đổi theo trạng thái hoàn trả của bạn
        console.log('Có thể hoàn trả không?', canReturn);  // Log ra kết quả kiểm tra

        // Kiểm tra ID nhân viên có hợp lệ không
        var userInfo = localStorage.getItem('user');
        if (userInfo) {
            userInfo = JSON.parse(userInfo);
            var userId = userInfo.idNguoiDung;
            console.log('ID Nhân viên:', userId);  // Log ID nhân viên
            return canReturn && userId;  // Kiểm tra có thể hoàn trả và có ID nhân viên
        }

        return false;  // Nếu không có thông tin người dùng, không cho phép hoàn trả
    };


};