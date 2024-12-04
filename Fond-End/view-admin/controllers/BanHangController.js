window.BanHangController = function ($scope, $http, $window, $location) {
    $scope.selectedPhuongThucThanhToan = 1;
    $scope.invoiceToDelete = null;
    $scope.currentPage = 1;
    $scope.pageChanged = function (newPage) {
        $scope.currentPage = newPage;
    };

    $scope.getUsers = function () {
        console.log("Người dùng đã chọn:", $scope.selectedUser);
        $http.get('http://localhost:8080/api/admin/khach_hang')
            .then(function (response) {
                console.log('Dữ liệu người dùng:', response.data);
                $scope.users = response.data;
                $scope.filteredUsers = $scope.users; // Khởi tạo danh sách đã lọc
                $scope.searchTerm = "";
                $scope.getCartItems();
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy dữ liệu người dùng:', error);
            });
    };
    $scope.filterUsers = function () {
        if (!$scope.searchTerm) {
            $scope.filteredUsers = [];
            $scope.showDropdown = false;
        } else {
            // Lọc danh sách người dùng theo từ khóa
            $scope.filteredUsers = $scope.users.filter(function (user) {
                const sdt = user.sdt || ''; // Nếu sdt là null hoặc undefined, gán giá trị mặc định là chuỗi rỗng
                return user.tenNguoiDung.toLowerCase().includes($scope.searchTerm.toLowerCase()) ||
                    sdt.includes($scope.searchTerm); // Kiểm tra trên sdt đã được đảm bảo là chuỗi
            });
            $scope.showDropdown = true;
        }
        $scope.getCartItems();
    };


    $scope.selectUser = function (user) {
        $scope.searchTerm = user.tenNguoiDung; // Hiển thị tên người dùng vào ô tìm kiếm
        $scope.selectedUser = user;
        $scope.showDropdown = false; // Ẩn dropdown khi người dùng đã chọn
        $scope.getCartItems();
    };

    $scope.nvs = [];
    $scope.getNV = function () {
        console.log("Người dùng đã chọn:", $scope.selectedUser);
        $http.get('http://localhost:8080/api/admin/nhan_vien')
            .then(function (response) {
                $scope.selectedEmployeeName = $scope.nvs.tenNguoiDung;
                console.log('Dữ liệu người dùng:', response.data);
                $scope.nvs = response.data;
            })

            .catch(function (error) {
                console.error('Lỗi khi lấy dữ liệu người dùng:', error);
            });
    };
    $scope.newCustomer = {
        tenNguoiDung: '',
        sdt: ''
    };
    $scope.openAddCustomerModal = function () {
        $scope.isAddCustomerModalOpen = true;
    };
    $scope.closeAddCustomerModal = function () {
        $scope.isAddCustomerModalOpen = false;
        $scope.newCustomer.tenNguoiDung = '';
        $scope.newCustomer.sdt = '';
    };
    $scope.addCustomer = function () {
        if (!$scope.newCustomer.tenNguoiDung || $scope.newCustomer.tenNguoiDung.trim() === '') {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng nhập tên khách hàng!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return;
        }

        if (!$scope.newCustomer.sdt || $scope.newCustomer.sdt.trim() === '') {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng nhập số điện thoại!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra định dạng số điện thoại (ví dụ: chỉ cho phép số từ 10-11 chữ số)
        const phoneRegex = /^[0-9]{10,11}$/;
        if (!phoneRegex.test($scope.newCustomer.sdt)) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Số điện thoại không hợp lệ! Vui lòng nhập số từ 10-11 chữ số.',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return;
        }
        let isPhoneDuplicate = $scope.users.some(user => user.sdt === $scope.newCustomer.sdt.trim());
        if (isPhoneDuplicate) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Số điện thoại này đã tồn tại. Vui lòng nhập số điện thoại khác!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return;
        }
        // Dữ liệu hợp lệ, tiến hành gọi API
        let customerData = {
            tenNguoiDung: $scope.newCustomer.tenNguoiDung.trim(),
            sdt: $scope.newCustomer.sdt.trim(),
        };

        $http.post('http://localhost:8080/api/admin/khach_hang/add', customerData)
            .then(function (response) {
                console.log('Khách hàng đã được thêm:', response.data);
                $scope.getUsers();
                $scope.closeAddCustomerModal();
                Swal.fire({
                    title: 'Thành công!',
                    text: 'Khách hàng đã được thêm thành công!',
                    icon: 'success',
                    timer: 1500,
                    showConfirmButton: false
                });
            })
            .catch(function (error) {
                console.error('Đã xảy ra lỗi:', error);
                Swal.fire({
                    title: 'Lỗi!',
                    text: 'Có lỗi xảy ra trong quá trình thêm khách hàng. Vui lòng thử lại!',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            });
    };

    $scope.getUnpaidInvoices = function () {
        $http.get('http://localhost:8080/api/admin/hoa_don_ban_hang/chua-thanh-toan')
            .then(function (response) {
                console.log('Danh sách hóa đơn chưa thanh toán:', response.data);
                $scope.unpaidInvoices = response.data;
                if ($scope.unpaidInvoices.length > 0) {
                    $scope.selectInvoice($scope.unpaidInvoices[0]);
                }
                if ($scope.unpaidInvoices.length >= 5) {
                    Swal.fire({
                        title: 'Thông báo!',
                        text: 'Bạn đã có 5 hóa đơn chưa thanh toán. Không thể tạo thêm hóa đơn!',
                        icon: 'warning',
                        confirmButtonText: 'OK'
                    });
                }
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy danh sách hóa đơn chưa thanh toán:', error);
            });
    };

    $scope.createInvoice = function () {
        console.log("Selected User:", $scope.selectedUser);

        // Kiểm tra xem đã chọn người dùng chưa
        if (!$scope.selectedUser || !$scope.selectedUser.idNguoiDung) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng chọn người dùng trước khi tạo hóa đơn.',
                icon: 'error',
                confirmButtonText: 'Đóng'
            });
            return;
        }

        // Gọi API kiểm tra hóa đơn chưa thanh toán
        $http.get('http://localhost:8080/api/admin/hoa_don_ban_hang/chua-thanh-toan')
            .then(function (response) {
                const allUnpaidInvoices = response.data;

                // Lọc hóa đơn của người dùng đã chọn
                const userUnpaidInvoices = allUnpaidInvoices.filter(invoice =>
                    invoice.nguoiDung &&
                    invoice.nguoiDung.idNguoiDung === $scope.selectedUser.idNguoiDung
                );

                // Kiểm tra số lượng hóa đơn chưa thanh toán
                if (userUnpaidInvoices.length > 0) {
                    Swal.fire({
                        title: 'Thông báo!',
                        text: `Khách hàng này đã có ${userUnpaidInvoices.length} hóa đơn chưa thanh toán. Vui lòng thanh toán trước khi tạo hóa đơn mới!`,
                        icon: 'warning',
                        confirmButtonText: 'OK'
                    });
                    return; // Dừng lại nếu có hóa đơn chưa thanh toán
                }

                // Nếu không có hóa đơn chưa thanh toán, tiếp tục tạo hóa đơn
                let newInvoice = {
                    tenNguoiNhan: $scope.selectedUser.tenNguoiDung,
                    nguoiDung: { idNguoiDung: $scope.selectedUser.idNguoiDung },
                    nhanVien: { idNguoiDung: $scope.nvs.idNguoiDung },
                    ngayTao: new Date()
                };

                $http.post('http://localhost:8080/api/admin/hoa_don_ban_hang/create', newInvoice)
                    .then(function (response) {
                        console.log('Hóa đơn được tạo:', response.data);

                        Swal.fire({
                            title: 'Thành công!',
                            text: 'Hóa đơn đã được tạo thành công!',
                            icon: 'success',
                            timer: 1500,
                            showConfirmButton: false
                        });

                        // Tải lại danh sách hóa đơn chưa thanh toán
                        $scope.getUnpaidInvoices();
                    })
                    .catch(function (error) {
                        console.error('Lỗi khi tạo hóa đơn:', error);
                        Swal.fire({
                            title: 'Lỗi!',
                            text: 'Có lỗi xảy ra khi tạo hóa đơn!',
                            icon: 'error',
                            confirmButtonText: 'OK'
                        });
                    });

            })
            .catch(function (error) {
                console.error('Lỗi khi kiểm tra hóa đơn chưa thanh toán:', error);
                Swal.fire({
                    title: 'Lỗi!',
                    text: 'Không thể kiểm tra hóa đơn chưa thanh toán!',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            });
    };

    $scope.openDeleteModal = function (invoiceId) {
        $scope.invoiceToDelete = invoiceId;
    };

    $scope.confirmDelete = function () {
        if ($scope.invoiceToDelete) {
            $scope.deleteInvoice($scope.invoiceToDelete);
            $scope.invoiceToDelete = null;
        }
    };
    $scope.deleteInvoice = function (invoiceId) {
        console.log('ID hóa đơn cần xóa:', invoiceId);
        if (invoiceId) {
            $http.delete(' http://localhost:8080/api/admin/hoa_don_ban_hang/delete/' + invoiceId)
                .then(function (response) {
                    console.log('Hóa đơn đã được xóa:', response.data);
                    $scope.getUnpaidInvoices();
                    Swal.fire({
                        title: 'Thành công!',
                        text: 'Xóa hóa đơn thành công!',
                        icon: 'success',
                        timer: 1500,
                        showConfirmButton: false
                    });
                })
            // .catch(function (error) {
            //     console.error('Lỗi khi xóa hóa đơn:', error);
            // });
        }
    };
    $scope.selectedProductDetails = [];
    $scope.showProductDetails = function (sp) {
        const idSanPham = sp.idSanPham; // Lấy ID sản phẩm từ sản phẩm đã click
        $http.get(`http://localhost:8080/api/admin/ban_hang/chi_tiet?id_san_pham=${idSanPham}`)
            .then(function (response) {
                $scope.selectedProductDetails = response.data; // Gán dữ liệu chi tiết vào scope
                console.log('Chi tiết sản phẩm:', $scope.selectedProductDetails);

                // Hiển thị modal hoặc chi tiết sản phẩm
                $('#productDetailsModal').modal('show'); // Hiển thị modal (nếu sử dụng Bootstrap)
            })
            .catch(function (error) {
                console.error("Có lỗi xảy ra khi gọi API chi tiết sản phẩm:", error);
            });
    };
    $scope.colorOptions = ["Đỏ", "Xanh", "Vàng", "Trắng"]; // Các màu sắc có sẵn
    $scope.sizeOptions = ["S", "M", "L", "XL"];  // Các kích thước có sẵn
    $scope.materialOptions = ["Cotton", "Len", "Da", "Vải tổng hợp"]; // Các chất liệu có sẵn

    $scope.searchColor = ''; // Màu sắc tìm kiếm
    $scope.searchSize = '';  // Kích thước tìm kiếm
    $scope.searchMaterial = ''; // Chất liệu tìm kiếm

    // Hàm lọc sản phẩm dựa trên các tiêu chí màu sắc, kích thước và chất liệu
    $scope.filteredProductDetails = function () {
        return $scope.selectedProductDetails.filter(function (detail) {
            return (!$scope.searchColor || detail.ten_mau_sac.toLowerCase().includes($scope.searchColor.toLowerCase())) &&
                (!$scope.searchSize || detail.ten_kich_thuoc.toLowerCase().includes($scope.searchSize.toLowerCase())) &&
                (!$scope.searchMaterial || detail.ten_chat_lieu.toLowerCase().includes($scope.searchMaterial.toLowerCase()));
        });
    };

    $scope.getProductDetails = function () {
        $http.get('http://localhost:8080/api/admin/ban_hang/all_quay')
            .then(function (response) {
                $scope.sanPhamChiTietList = response.data;
                console.log('Danh sách sản phẩm chi tiết:', $scope.sanPhamChiTietList);
            })
            .catch(function (error) {
                console.error("Có lỗi xảy ra khi gọi API sản phẩm chi tiết:", error);
            });
    };
    $scope.selectInvoice = function (invoice) {
        console.log("Hóa đơn đã chọn:", invoice);
        $scope.selectedInvoice = invoice;

        if (!invoice || !invoice.idHoaDon) {
            console.error('Hóa đơn không hợp lệ:', invoice);
            alert("Hóa đơn không hợp lệ!");
            return;
        }

        $http.get('http://localhost:8080/api/admin/hoa_don_ban_hang/' + invoice.idHoaDon)
            .then(function (response) {
                console.log('Chi tiết hóa đơn:', response.data);
                $scope.selectedInvoice = response.data;
                $scope.selectedInvoice.maHoaDon = response.data.maHoaDon;
                $scope.selectedUser = $scope.selectedInvoice.nguoiDung;
                $scope.searchTerm = $scope.selectedUser.tenNguoiDung;

                // Lưu chi tiết hóa đơn vào localStorage
                localStorage.setItem('selectedInvoice', JSON.stringify($scope.selectedInvoice));

                $scope.getCartItems(); // Gọi để lấy thông tin giỏ hàng
            })
        // .catch(function (error) {
        //     console.error('Lỗi khi lấy chi tiết hóa đơn:', error);
        // });
    };
    $scope.calculateChange = function () {
        var tienKhachDua = $scope.tienKhachDua ? $scope.tienKhachDua.replace(/,/g, '') : '0';
        tienKhachDua = parseFloat(tienKhachDua);
        var totalPrice = parseFloat($scope.totalPrice) || 0;
        if (isNaN(tienKhachDua) || tienKhachDua < 0) {
            $scope.tienKhachDua = 0;
            $scope.tienThua = 0;
            alert("Vui lòng nhập một số tiền hợp lệ.");
            return;
        }
        if (isNaN(totalPrice) || totalPrice < 0) {
            $scope.totalPrice = 0;
            alert("Tổng tiền thanh toán không hợp lệ.");
            return;
        }
        $scope.tienThua = tienKhachDua - totalPrice;
        if ($scope.tienThua < 0) {
            $scope.tienThua = 0;
        }
        console.log("Tiền khách đưa:", tienKhachDua);
        console.log("Tổng tiền thanh toán:", totalPrice);
        console.log("Tiền thừa:", $scope.tienThua);
    };
    $scope.addToCart = function (idSanPhamChiTiet) {
        if (!$scope.selectedUser || !$scope.selectedUser.idNguoiDung) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng chọn người dùng trước khi thêm sản phẩm vào giỏ hàng.',
                icon: 'error',
                confirmButtonText: 'Đóng'
            });
            return;
        }
        $scope.addToCart = function (detail) {
            let donGia = detail.gia_khuyen_mai || detail.gia_ban;

            // Tính thành tiền (giả sử số lượng là 1)
            let soLuong = detail.soLuong || 1;
            let thanhTien = donGia * soLuong;

            let cartItem = {
                idSanPhamChiTiet: detail.id_san_pham_chi_tiet, // ID sản phẩm chi tiết
                soLuong: 1,
                donGia: donGia,
                thanhTien: thanhTien
            };

            // Gửi yêu cầu thêm sản phẩm vào giỏ hàng
            $http.post('http://localhost:8080/api/admin/gio_hang/them/' + $scope.selectedUser.idNguoiDung, cartItem)
                .then(function (response) {
                    console.log('Sản phẩm đã được thêm vào giỏ hàng:', response.data);
                    Swal.fire({
                        title: 'Thành công!',
                        text: 'Thêm giỏ hàng thành công!',
                        icon: 'success',
                        timer: 1500,
                        showConfirmButton: false
                    });
                    $scope.showProductDetails({ idSanPham: detail.id_san_pham });
                    return $scope.getCartItems();

                })
                .then(function () {
                    $scope.calculateTotalPrice(); // Tính toán tổng giá
                })
                .catch(function (error) {
                    console.error('Lỗi khi thêm sản phẩm vào giỏ hàng:', error);
                });
        };
    }

    $scope.getCartItems = function () {
        if (!$scope.selectedUser || !$scope.selectedUser.idNguoiDung) {
            console.log("Không có ID người dùng.");
            return;
        }
        $http.get('http://localhost:8080/api/admin/gio_hang/lay/' + $scope.selectedUser.idNguoiDung)
            .then(function (response) {
                console.log('Danh sách sản phẩm trong giỏ hàng:', response.data);
                $scope.cartItems = response.data;
                $scope.isCartEmpty = $scope.cartItems.length === 0;
                $scope.calculateTotalPrice();
                $scope.getProductDetails() // sau khi lấy thêm giỏ hàng thì load lại spct nhé kkk

            })
            .catch(function (error) {
                console.error('Lỗi khi lấy giỏ hàng:', error);
                alert("Có lỗi xảy ra khi lấy giỏ hàng!");
            });
    };


    $scope.voucherCode = '';
    $scope.selectedVoucher = null;
    $scope.voucherError = '';
    $scope.$watch('totalPrice', function (newVal, oldVal) {
        if (newVal !== oldVal) {
            console.log("Tổng tiền đã thay đổi:", newVal);
            $scope.dsvoucher(); // Gọi lại API danh sách voucher
        }
    });
    $scope.calculateTotalPrice = function () {
        // Tính tổng tiền chưa giảm giá
        let originalTotalPrice = 0;
        let discount = 0;

        if ($scope.cartItems && Array.isArray($scope.cartItems)) {
            $scope.cartItems.forEach(function (item) {
                if (item && item.soLuong && item.giaBan) {
                    originalTotalPrice += item.soLuong * item.giaBan;
                }
            });
        } else {
            console.error('Giỏ hàng không hợp lệ:', $scope.cartItems);
        }

        // Kiểm tra tổng tiền hợp lệ
        if (isNaN(originalTotalPrice) || originalTotalPrice <= 0) {
            console.error('Tổng tiền không hợp lệ:', originalTotalPrice);
            $scope.totalPrice = 0;
            $scope.selectedVoucher = null;
            return;
        }

        // Đặt lại tổng tiền gốc
        $scope.totalPrice = originalTotalPrice;

        // Kiểm tra và áp dụng voucher nếu có
        if ($scope.selectedVoucher) {
            const minValue = $scope.selectedVoucher.soTienToiThieu;
            const maxValue = $scope.selectedVoucher.giaTriToiDa;

            // Kiểm tra voucher có hợp lệ với tổng tiền chưa giảm giá
            if ($scope.totalPrice < minValue || $scope.totalPrice > maxValue) {
                $scope.selectedVoucher = null;
            } else {
                if ($scope.selectedVoucher.kieuGiamGia === false) {
                    discount = $scope.totalPrice * ($scope.selectedVoucher.giaTriGiamGia / 100);  // Giảm theo tỷ lệ phần trăm
                } else {
                    discount = $scope.selectedVoucher.giaTriGiamGia;  // Giảm theo giá trị cố định
                }

                // Áp dụng giảm giá
                $scope.totalPrice -= discount;
                $scope.totalPrice = Math.max($scope.totalPrice, 0);  // Đảm bảo tổng tiền không âm
            }
        }
    };
    $scope.getVoucher = function () {
        if ($scope.voucherCode) {
            // Tính tổng tiền chưa giảm giá (gốc)
            let originalTotalPrice = 0;
            if ($scope.cartItems && Array.isArray($scope.cartItems)) {
                $scope.cartItems.forEach(function (item) {
                    if (item && item.soLuong && item.giaBan) {
                        originalTotalPrice += item.soLuong * item.giaBan;
                    }
                });
            } else {
                console.error('Giỏ hàng không hợp lệ:', $scope.cartItems);
            }

            // Kiểm tra tổng tiền hợp lệ
            if (!originalTotalPrice || originalTotalPrice <= 0) {
                $scope.voucherError = 'Tổng tiền phải lớn hơn 0 để áp dụng voucher!';
                return;
            }

            // Đảm bảo dữ liệu gửi đi là một đối tượng JSON hợp lệ
            let requestData = { totalPrice: originalTotalPrice };

            // Gửi yêu cầu API kiểm tra voucher
            $http.post('http://localhost:8080/api/admin/ban_hang/use/' + $scope.voucherCode, requestData)
                .then(function (response) {
                    $scope.selectedVoucher = response.data; // Nhận voucher đã chọn từ phản hồi API
                    console.log("Voucher đã áp dụng:", $scope.selectedVoucher);

                    // Kiểm tra giá trị tối thiểu và tối đa của voucher
                    if (originalTotalPrice < $scope.selectedVoucher.soTienToiThieu) {
                        $scope.voucherError = 'Tổng tiền không đủ để áp dụng voucher này. Bạn cần ít nhất ' + $scope.selectedVoucher.soTienToiThieu + ' để áp dụng.';
                        return;
                    }

                    if (originalTotalPrice > $scope.selectedVoucher.giaTriToiDa) {
                        $scope.voucherError = 'Tổng tiền vượt quá giá trị tối đa của voucher này. Bạn chỉ có thể sử dụng voucher khi tổng tiền từ ' + $scope.selectedVoucher.soTienToiThieu + ' đến ' + $scope.selectedVoucher.giaTriToiDa + '.';
                        return;
                    }

                    // Nếu tổng tiền hợp lệ với voucher, reset lỗi và tính lại tổng tiền
                    $scope.voucherError = '';
                    $scope.calculateTotalPrice(); // Tính lại tổng tiền sau khi áp dụng voucher
                })
                .catch(function (error) {
                    console.error('Lỗi khi áp dụng voucher:', error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi khi áp dụng voucher!',
                        text: error.data || 'Mã voucher không hợp lệ hoặc không thể sử dụng.',
                        confirmButtonText: 'Thử lại'
                    });
                    $scope.voucherError = error.data || 'Mã voucher không hợp lệ hoặc không thể sử dụng!';
                });
        }
    };

    $scope.dsvoucher = function () {
        // Tính tổng tiền chưa giảm giá (gốc)
        let originalTotalPrice = 0;
        if ($scope.cartItems && Array.isArray($scope.cartItems)) {
            $scope.cartItems.forEach(function (item) {
                if (item && item.soLuong && item.giaBan) {
                    originalTotalPrice += item.soLuong * item.giaBan;
                }
            });
        } else {
            console.error('Giỏ hàng không hợp lệ:', $scope.cartItems);
        }

        if (originalTotalPrice <= 0) {
            console.error('Tổng tiền chưa giảm giá không hợp lệ:', originalTotalPrice);
            return;
        }

        // Gửi yêu cầu API với tổng tiền chưa giảm giá
        $http.get('http://localhost:8080/api/admin/ban_hang/allvoucher/' + originalTotalPrice)
            .then(function (response) {
                $scope.availableVouchers = response.data || [];
                console.log("Danh sách voucher khả dụng:", $scope.availableVouchers);

                // Lọc các voucher hợp lệ (dựa trên tổng tiền chưa giảm giá)
                $scope.availableVouchers = $scope.availableVouchers.filter(function (voucher) {
                    return originalTotalPrice >= voucher.soTienToiThieu && originalTotalPrice <= voucher.giaTriToiDa;
                });

                // Không tự động áp dụng voucher nào
                $scope.selectedVoucher = null;
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy danh sách voucher:', error);
                $scope.availableVouchers = [];
            });
    };


    $scope.confirmVoucher = function () {
        if ($scope.selectedVoucher) {
            // Hiển thị thông báo thành công
            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: `Áp dụng thành công mã voucher: ${$scope.selectedVoucher.maVoucher}`,
                confirmButtonText: 'OK',
                customClass: {
                    confirmButton: 'btn btn-success'
                },
                buttonsStyling: false
            }).then((result) => {
                if (result.isConfirmed) {
                    // Đóng modal
                    $('#voucherModal').modal('hide'); // Dùng jQuery để đóng modal
                }
            });
        } else {
            // Hiển thị lỗi nếu chưa chọn voucher
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Vui lòng chọn một voucher trước khi xác nhận.',
                confirmButtonText: 'OK',
                customClass: {
                    confirmButton: 'btn btn-danger'
                },
                buttonsStyling: false
            });
        }
    };
    $scope.selectVoucher = function (voucher) {
        // Kiểm tra điều kiện sử dụng voucher
        if (voucher.isUsable) {
            $scope.selectedVoucher = voucher;
            $scope.voucherCode = voucher.maVoucher;  // Gán mã voucher vào input (nếu cần)
            $scope.voucherError = '';  // Reset lỗi
            $scope.calculateTotalPrice();
        } else {
            $scope.voucherError = 'Voucher này không thể sử dụng.';
        }
    };


    $scope.deleteCartItem = function (productId) {
        const userId = $scope.selectedUser.idNguoiDung;
        $http.delete('http://localhost:8080/api/admin/gio_hang/xoa-san-pham/' + userId + '/' + productId)
            .then(function (response) {
                console.log('Sản phẩm đã được xóa khỏi giỏ hàng:', response.data);
                $scope.getCartItems(); // Load lại giỏ hàng
                $scope.getProductDetails(); // Load lại sản phẩm
                $scope.calculateTotalPrice(); // Tính toán lại tổng tiền sau khi xóa sản phẩm

                Swal.fire({
                    title: 'Thành công!',
                    text: 'Xóa giỏ hàng thành công!',
                    icon: 'success',
                    timer: 1500,  // Đóng sau 2 giây
                    showConfirmButton: false
                });

            })
            .catch(function (error) {
                console.error('Lỗi khi xóa sản phẩm khỏi giỏ hàng:', error);
                alert("Có lỗi xảy ra khi xóa sản phẩm khỏi giỏ hàng!");
            });
    };

    $scope.deleteCart = function (productId) {
        const userId = $scope.selectedUser.idNguoiDung;
        $http.delete('http://localhost:8080/api/admin/gio_hang/xoa-tat-ca/' + userId)
    };
    $scope.updateQuantity = function (item) {
        if (!$scope.selectedUser || !$scope.selectedUser.idNguoiDung) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng chọn người dùng trước khi thực hiện thao tác.',
                icon: 'error',
                confirmButtonText: 'Đóng'
            });
            return;
        }

        // Lấy userId từ selectedUser
        const userId = $scope.selectedUser.idNguoiDung;

        // Kiểm tra số lượng hợp lệ
        if (item.soLuong < 1) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Số lượng phải lớn hơn hoặc bằng 1.',
                icon: 'error',
                confirmButtonText: 'Đóng'
            });
            item.soLuong = 1;
            return;
        }

        if (item.soLuong > item.soLuongTon) {
            Swal.fire({
                title: 'Lỗi!',
                text: `Số lượng không thể vượt quá tồn kho (${item.soLuongTon}).`,
                icon: 'error',
                confirmButtonText: 'Đóng'
            });
            item.soLuong = item.soLuongTon;
            return;
        }

        const updatedQuantity = { soLuong: item.soLuong };

        $http.put('http://localhost:8080/api/admin/gio_hang/cap-nhat/' + userId + '/' + item.idSanPhamChiTiet, updatedQuantity)
            .then(function (response) {
                console.log('Cập nhật số lượng thành công:', response.data);

                $scope.getProductDetails(); // Load lại sản phẩm
                $scope.calculateTotalPrice(); // Tính toán lại tổng tiền

            })
            .catch(function (error) {
                console.error('Lỗi khi cập nhật số lượng:', error);
                Swal.fire({
                    title: 'Lỗi!',
                    text: 'Số lượng không thể vượt quá tồn kho.',
                    icon: 'error',
                    confirmButtonText: 'Đóng'
                });
            });
    };
    $scope.getPhuongThucThanhToan = function () {
        $http.get('http://localhost:8080/api/admin/phuong_thuc_thanh_toan/ten-phuong-thuc')
            .then(function (response) {
                console.log('Dữ liệu phương thức thanh toán:', response.data);
                $scope.phuongThucThanhToan = response.data;
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy dữ liệu phương thức thanh toán:', error);
            });
    };

    $scope.selectPhuongThucThanhToan = function (id) {
        $scope.selectedPhuongThucThanhToan = id;

        // Tìm tên phương thức thanh toán từ dữ liệu đã lấy và gán vào biến phuongThuc
        var selectedMethod = $scope.phuongThucThanhToan.find(function (pt) {
            return pt.id === id;
        });

        $scope.phuongThuc = selectedMethod ? selectedMethod.tenPhuongThuc : '';
        console.log('Phương thức thanh toán đã chọn:', $scope.phuongThuc);
    };
    $scope.TEST = function () {
        if ($scope.selectedPhuongThucThanhToan === 1) {
            console.log('Phương thức thanh toán là ID 1, không chạy VNPAY');
            return;
        }

        if ($scope.selectedPhuongThucThanhToan === 2) {
            const paymentMethod = 'VNPAY';
            const amount = $scope.totalPrice;

            const url = `http://localhost:8080/api/admin/payment/creat_payment?amount=${amount}&paymentMethod=${paymentMethod}`;
            $http.post(url)
                .then(function (response) {
                    console.log('Dữ liệu trả về từ VNPAY:', response.data);

                    const paymentUrlMatch = response.data.match(/window\.location\.href='([^']+)'/);

                    if (paymentUrlMatch && paymentUrlMatch[1]) {
                        const paymentUrl = paymentUrlMatch[1];
                        console.log('Điều hướng đến URL thanh toán:', paymentUrl);
                        window.location.href = paymentUrl;
                    } else {
                        alert('Không thể tạo thanh toán. Vui lòng thử lại.');
                    }
                })
                .catch(function (error) {
                    console.error('Lỗi xảy ra khi gọi VNPAY API:', error);
                });
        }

        // Thêm điều kiện cho PayOS (phương thức thanh toán 3)
        if ($scope.selectedPhuongThucThanhToan === 3) {
            const paymentMethod = 'PayOS';
            const amount = $scope.totalPrice;
            const description = 'Thanh toán cho hóa đơn';
            const returnUrl = 'http://127.0.0.1:5500/admin.html#!/ban_hang?message=Thanh%20toan%20thanh%20cong';
            const cancelUrl = 'http://127.0.0.1:5500/admin.html#!/ban_hang?message=Thanh%20toan%20that%20bai';
            $http.post('http://localhost:8080/payos/create-payment-link', {
                description: description,
                returnUrl: returnUrl,
                cancelUrl: cancelUrl,
                price: amount
            })
                .then(function (response) {
                    console.log('Dữ liệu trả về từ PayOS:', response.data);

                    if (response.data.error === 0 && response.data.data && response.data.data.checkoutUrl) {
                        const paymentUrl = response.data.data.checkoutUrl;  // Lấy URL thanh toán từ PayOS
                        console.log('Điều hướng đến URL thanh toán PayOS:', paymentUrl);
                        window.location.href = paymentUrl;
                    } else {
                        alert('Không thể tạo liên kết thanh toán PayOS. Vui lòng thử lại.');
                    }
                })
        }
    };


    $scope.validateInvoiceCreation = function () {
        if ($scope.isCartEmpty) {
            Swal.fire({
                title: 'Thông báo!',
                text: 'Giỏ hàng trống. Vui lòng thêm sản phẩm trước khi tạo hóa đơn.',
                icon: 'warning',
                confirmButtonText: 'OK'
            });
            return false;
        }
        if (!$scope.selectedInvoice || !$scope.selectedInvoice.idHoaDon) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng tạo hóa đơn trước khi thanh toán!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return false;
        }
        return true;
    };


    $scope.createHoaDonChiTiet = async function () {
        try {
            // Kiểm tra các điều kiện trước khi tạo hóa đơn chi tiết
            if (!$scope.validateInvoiceCreation()) return;
            await $scope.updateInvoiceStatus();
            // Chạy các tác vụ không phụ thuộc đồng thời
            await Promise.all([
                // Cập nhật trạng thái hóa đơn
                $scope.deleteGioHangChiTietByUserId(), // Xóa chi tiết giỏ hàng theo người dùng
                $scope.createPaymentMethod(),         // Tạo phương thức thanh toán
                $scope.deleteCart(),                  // Xóa toàn bộ giỏ hàng
            ]);

            // Xử lý sản phẩm trong giỏ hàng và hóa đơn chi tiết
            await $scope.processCartItems();
            await $scope.TEST(); // Thực hiện kiểm tra hoặc thao tác khác

            // Gửi API tạo hóa đơn chi tiết
            const response = await $http.post(
                "http://localhost:8080/api/admin/hoa_don_chi_tiet/create",
                $scope.hoaDonChiTietDTO,
                { params: { userId: $scope.selectedUser.idNguoiDung } }
            );
            console.log("Hóa đơn chi tiết:", response.data);

            // Nếu có voucher được chọn, trừ số lượng của voucher
            if ($scope.selectedVoucher) {
                await $scope.deductVoucherQuantity();
            }

            // Reset dữ liệu sau khi tạo hóa đơn
            $scope.resetAfterInvoiceCreation();

        } catch (error) {
            console.error("Lỗi xảy ra:", error);
            Swal.fire({
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra trong quá trình tạo hóa đơn.',
                icon: 'error',
                confirmButtonText: 'OK'
            });
        }
    };


    $scope.processCartItems = async function () {
        $scope.hoaDonChiTietDTO = [];
        const isChuyenKhoan = $scope.selectedPhuongThucThanhToan === 2;
        const isPayOS = $scope.selectedPhuongThucThanhToan === 3;

        $scope.cartItems.forEach(function (item) {
            const detail = {
                idHoaDon: $scope.selectedInvoice.idHoaDon,
                idSanPhamChiTiet: item.idSanPhamChiTiet,
                soLuong: item.soLuong,
                tongTien: $scope.totalPrice,
                soTienThanhToan: isChuyenKhoan || isPayOS
                    ? $scope.totalPrice
                    : parseFloat($scope.tienKhachDua),
                tienTraLai: isChuyenKhoan || isPayOS
                    ? 0
                    : $scope.tienThua,
                moTa: item.moTa || ''
            };
            $scope.hoaDonChiTietDTO.push(detail);
        });
    };

    $scope.deductVoucherQuantity = async function () {
        try {
            // Gửi yêu cầu trừ số lượng voucher
            const response = await $http.post(
                `http://localhost:8080/api/admin/ban_hang/use/${$scope.selectedVoucher.maVoucher}`,
                $scope.totalPrice
            );
            console.log("Số lượng voucher đã được trừ.");

            // Cập nhật lại số lượng voucher trong giao diện người dùng
            $scope.selectedVoucher.soLuong--;
        } catch (error) {
            console.error('Lỗi khi trừ số lượng voucher:', error);

        }
    };

    // Hàm reset dữ liệu sau khi tạo hóa đơn
    $scope.resetAfterInvoiceCreation = function () {
        $scope.getUnpaidInvoices();
        $scope.getCartItems();
        $scope.description = "";
        $scope.selectedInvoice = { maHoaDon: 0 };
        $scope.selectednv = null;
        $scope.searchTerm = "";
        $scope.tienKhachDua = "";
        $scope.tienThua = "";
        $scope.selectedVoucher = null;
        $scope.voucherCode = "";
    };

    $scope.deleteGioHangChiTietByUserId = function () {
        const userId = $scope.selectedUser.idNguoiDung;
        $http.delete("http://localhost:8080/api/admin/gio_hang/delete/user/" + userId)
            .then(function (response) {
                $scope.getCartItems();
            })
            .catch(function (error) {
                console.error("Lỗi khi xóa chi tiết giỏ hàng:", error);
            });
    };


    $scope.updateInvoiceStatus = function () {
        $http.put("http://localhost:8080/api/admin/hoa_don_ban_hang/" + $scope.selectedInvoice.idHoaDon + "/update-status", { trangThai: true })
            .then(function (response) {
                console.log("Kết quả cập nhật:", response.data);
            })
    };
    $scope.createPaymentMethod = function () {
        var paymentMethod = {
            phuongThucThanhToan: { idPTThanhToan: $scope.selectedPhuongThucThanhToan },
            soTienThanhToan: Number($scope.customerPaid),
            ngayGiaoDich: new Date().toISOString(),
            ghiChu: "Thanh toán hóa đơn " + $scope.selectedInvoice.maHoaDon
        };

        // Tạo phương thức thanh toán
        $http.post("http://localhost:8080/api/admin/phuong_thuc_thanh_toan_hoa_don/" + $scope.selectedInvoice.idHoaDon + "/thanh-toan", paymentMethod)
            .then(function (response) {
                console.log("Phản hồi từ POST:", response.data);
                if (response.data && response.data.idThanhToanHoaDon) {
                    // Phương thức thanh toán đã được tạo thành công, tiếp tục cập nhật hóa đơn
                    var updatedInvoice = {
                        phiShip: 0,
                        ngayThanhToan: new Date().toISOString(),
                        thanhTien: $scope.totalPrice,
                        idThanhToanHoaDon: response.data.idThanhToanHoaDon,
                        setSdtNguoiNhan: $scope.selectedUser.sdt,
                        trangThaiHoaDon: $scope.selectedPhuongThucThanhToan === 2 ? 4 : 5,
                        idVoucher: $scope.selectedVoucher ? $scope.selectedVoucher.id_voucher : null
                    };

                    console.log("Cấu trúc của updatedInvoice trước khi gửi:", updatedInvoice);
                    return $http.put("http://localhost:8080/api/admin/hoa_don_ban_hang/cap-nhat/" + $scope.selectedInvoice.idHoaDon, updatedInvoice)
                        .then(function (putResponse) {
                            console.log("Hóa đơn đã được cập nhật thành công:", putResponse.data);
                        })
                        .catch(function (error) {
                            console.error("Lỗi khi cập nhật hóa đơn:", error);
                            alert("Có lỗi xảy ra khi cập nhật hóa đơn.");
                        });
                } else {
                    alert("Có lỗi xảy ra khi tạo phương thức thanh toán.");
                }
            })
            .catch(function (error) {
                console.error("Lỗi khi tạo phương thức thanh toán:", error);
                alert("Có lỗi xảy ra khi tạo phương thức thanh toán.");
            });
    };


    $scope.printInvoice = function () {
        // Tạo một phần tử mới để in hóa đơn
        var printContents = document.getElementById('invoicePrintModal').innerHTML;
        var popupWin = window.open('', '_blank', 'width=600,height=600');
        popupWin.document.open();
        popupWin.document.write(`
                <html>
                    <head>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                color: #333;
                                margin: 20px;
                            }
                            .store-info {
                                text-align: center;
                                margin-bottom: 20px;
                            }
                            .store-info h4 {
                                margin: 0;
                                font-weight: bold;
                            }
                            .store-info p {
                                margin: 5px 0;
                            }
                            h6 {
                                font-weight: bold;
                                margin-bottom: 10px;
                            }
                            table {
                                width: 100%;
                                border-collapse: collapse;
                                margin-bottom: 20px;
                            }
                            th, td {
                                border: 1px solid #333;
                                padding: 10px;
                                text-align: left;
                            }
                            th {
                                background-color: #f2f2f2;
                            }
                            .total-label {
                                font-weight: 600;
                                margin-top: 15px;
                            }
                        </style>
                    </head>
                    <body onload="window.print()">
                        ${printContents}
                    </body>
                </html>
            `);
        popupWin.document.close();
    };
    $scope.openDiscountModal = function () {
        $('#discountModal').modal('show');
    };
    $scope.thanhtoanvain = function () {
        $scope.createHoaDonChiTiet();
        $scope.printInvoice();
    }

    // Hàm đóng modal
    $scope.closeModal = function () {
        console.log('Đóng modal');
        $scope.isGiaoHang = false;
    };
    $scope.customerPaid = null;
    $scope.getProductDetails();
    $scope.getUsers();
    $scope.getNV();
    $scope.getUnpaidInvoices();
    $scope.deleteInvoice();
    $scope.getPhuongThucThanhToan();
    $scope.getVoucher();
    $scope.dsvoucher();
}