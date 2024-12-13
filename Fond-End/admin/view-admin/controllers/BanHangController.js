window.BanHangController = function ($scope, $http, $window, $location) {
    $scope.selectedPhuongThucThanhToan = 1;
    $scope.invoiceToDelete = null;
    $scope.currentPage = 1;
    $scope.pageChanged = function (newPage) {
        $scope.currentPage = newPage;
    };
    $scope.searchTerm = '';
    $scope.filteredUsers = [];
    $scope.showDropdown = false; // Hiển thị danh sách dropdown
    $scope.openAddCustomerModal = function () {
        $scope.isAddCustomerModalOpen = true;
    };
    $scope.closeAddCustomerModal = function () {
        $scope.isAddCustomerModalOpen = false;
        $scope.newCustomer.tenNguoiDung = '';
        $scope.newCustomer.sdtNguoiDung = '';
    };
    $scope.filterUsers = function () {
        if ($scope.searchTerm.trim().length === 0) {
            $scope.filteredUsers = [];
            $scope.showDropdown = false;
            return;
        }

        $http({
            method: 'GET',
            url: 'http://localhost:8080/api/admin/khach_hang/search',
            params: { phonePrefix: $scope.searchTerm }
        }).then(function (response) {
            $scope.filteredUsers = response.data; // Gán kết quả trả về
            $scope.showDropdown = $scope.filteredUsers.length > 0;
        }).catch(function (error) {
            console.error('Lỗi khi gọi API:', error);
            $scope.filteredUsers = [];
            $scope.showDropdown = false;
        });
    };
    $scope.selectUser = function (user) {
        $scope.selectedUser = user;  // Lưu thông tin người dùng đã chọn vào selectedUser
        $scope.searchTerm = user.tenNguoiDung + ' - ' + user.sdt;  // Hiển thị thông tin khách hàng
        $scope.showDropdown = false;  // Ẩn dropdown

        // Lưu thông tin người dùng vào localStorage
        localStorage.setItem('selectedUser', JSON.stringify(user));
    };

    $scope.newCustomer = {
        tenNguoiDung: '',
        sdt: ''
    };
    // check sdt bị trùng
    $scope.checkDuplicatePhone = function (phone, callback) {
        $http.get('http://localhost:8080/api/admin/khach_hang/check-sdt', { params: { sdt: phone } })
            .then(function (response) {
                callback(response.data); // true nếu trùng, false nếu không
            })
            .catch(function (error) {
                console.error('Lỗi khi kiểm tra số điện thoại:', error);
                callback(false); // Mặc định không trùng nếu lỗi
            });
    };

    $scope.addCustomer = function () {
        // Đảm bảo các biến đã được khởi tạo
        $scope.users = $scope.users || [];
        $scope.newCustomer = $scope.newCustomer || {};

        // Kiểm tra tên khách hàng
        if (!$scope.newCustomer.tenNguoiDung || $scope.newCustomer.tenNguoiDung.trim() === '') {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng nhập tên khách hàng!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra số điện thoại
        if (!$scope.newCustomer.sdt || $scope.newCustomer.sdt.trim() === '') {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng nhập số điện thoại!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra định dạng số điện thoại
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

        // Kiểm tra trùng số điện thoại
        const phone = $scope.newCustomer.sdt.trim();
        $scope.checkDuplicatePhone(phone, function (isDuplicate) {
            if (isDuplicate) {
                Swal.fire({
                    title: 'Lỗi!',
                    text: 'Số điện thoại đã tồn tại!',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
                return;
            }

            // Nếu không trùng, tiến hành thêm khách hàng
            const customerData = {
                tenNguoiDung: $scope.newCustomer.tenNguoiDung.trim(),
                sdt: phone,
            };

            $http.post('http://localhost:8080/api/admin/khach_hang/add', customerData)
                .then(function (response) {
                    // Thêm khách hàng mới vào danh sách hiện tại
                    $scope.users.push(response.data);

                    $('#addCustomerModal').modal('hide'); // Đóng modal
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
        });
    };

    $scope.getUnpaidInvoices = function () {
        $http.get('http://localhost:8080/api/admin/hoa_don_ban_hang/chua-thanh-toan')
            .then(function (response) {
                console.log('Danh sách hóa đơn chưa thanh toán:', response.data);
                $scope.unpaidInvoices = response.data;
                // if ($scope.unpaidInvoices.length > 0) {
                //     $scope.selectInvoice($scope.unpaidInvoices[0]);
                // }
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


        // Nếu không có hóa đơn chưa thanh toán, tiếp tục tạo hóa đơn
        let newInvoice = {
            tenNguoiNhan: $scope.selectedUser.tenNguoiDung,
            nguoiDung: { idNguoiDung: $scope.selectedUser.idNguoiDung },
            // nhanVien: { idNguoiDung: },
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
                $scope.selectedUser = null;
                $scope.searchTerm = ""; // Xóa thông tin tìm kiếm
                $scope.filteredUsers = []; // Xóa danh sách người dùng tìm kiếm
                $scope.showDropdown = false; // Ẩn dropdown
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
    };

    $scope.deleteInvoice = function (idHoaDon) {
        Swal.fire({
            title: 'Bạn có chắc chắn muốn xóa hóa đơn này?',
            text: 'Hành động này không thể hoàn tác!',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Xóa',
            cancelButtonText: 'Hủy'
        }).then((result) => {
            if (result.isConfirmed) {
                $http.delete('http://localhost:8080/api/admin/hoa_don_ban_hang/delete/' + idHoaDon)
                    .then(function (response) {
                        Swal.fire({
                            title: 'Đã xóa!',
                            text: 'Hóa đơn đã được xóa thành công.',
                            icon: 'success',
                            timer: 1500,
                            showConfirmButton: false
                        });
                        // Cập nhật danh sách hóa đơn nếu cần
                        $scope.getUnpaidInvoices();
                        // $scope.deleteCartItem();
                        // $scope.getCartItems();
                    })
                    .catch(function (error) {
                        console.error('Lỗi khi xóa hóa đơn:', error);
                        Swal.fire({
                            title: 'Lỗi!',
                            text: 'Không thể xóa hóa đơn. Vui lòng thử lại!',
                            icon: 'error',
                            confirmButtonText: 'OK'
                        });
                    });
            }
        });
    };

    $scope.selectedProductDetails = [];
    $scope.showProductDetails = function (sp) {
        const idSanPham = sp.idSanPham;
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
    $scope.colorOptions = ["Đỏ", "Xanh", "Vàng", "Trắng"]; // Các màu sắc có sẵn
    $scope.sizeOptions = ["S", "M", "L", "XL"];  // Các kích thước có sẵn
    $scope.materialOptions = ["Cotton", "Len", "Da", "Vải tổng hợp"]; // Các chất liệu có sẵn

    $scope.searchColor = '';
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



    $scope.selectInvoice = function (invoice) {
        console.log("Hóa đơn đã chọn:", invoice);

        if (!invoice || !invoice.idHoaDon) {
            console.error('Hóa đơn không hợp lệ:', invoice);
            Swal.fire({
                title: 'Lỗi!',
                text: 'Hóa đơn không hợp lệ!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Gửi yêu cầu lấy chi tiết hóa đơn
        $http.get('http://localhost:8080/api/admin/hoa_don_ban_hang/' + invoice.idHoaDon)
            .then(function (response) {
                console.log('Chi tiết hóa đơn:', response.data);
                $scope.selectedInvoice = response.data;

                // Kiểm tra xem có thông tin người dùng không
                const nguoiDung = response.data.nguoiDung;
                if (nguoiDung && nguoiDung.idNguoiDung) {
                    // Hiển thị thông tin khách hàng lên ô tìm kiếm
                    $scope.searchTerm = nguoiDung.tenNguoiDung + ' - ' + nguoiDung.sdt;
                    console.log("Người dùng đã chọn:", nguoiDung);

                    // Lưu thông tin người dùng và hóa đơn vào localStorage/sessionStorage
                    localStorage.setItem('selectedInvoice', JSON.stringify(invoice));  // Lưu hóa đơn
                    localStorage.setItem('selectedUser', JSON.stringify(nguoiDung));  // Lưu người dùng
                } else {
                    console.error("Không có thông tin người dùng.");
                    Swal.fire({
                        title: 'Thông báo!',
                        text: 'Không có thông tin người dùng!',
                        icon: 'warning',
                        confirmButtonText: 'OK'
                    });
                }

                $scope.getCartItems();
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy chi tiết hóa đơn:', error);
                Swal.fire({
                    title: 'Lỗi!',
                    text: 'Không thể tải chi tiết hóa đơn.',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            });
    };


    $scope.calculateChange = function () {
        // Kiểm tra và chuẩn hóa số tiền khách đưa
        var tienKhachDua = $scope.tienKhachDua ? $scope.tienKhachDua.replace(/,/g, '') : '0';
        tienKhachDua = parseFloat(tienKhachDua);

        // Kiểm tra tổng tiền thanh toán
        var totalPrice = parseFloat($scope.totalPrice) || 0;

        // Kiểm tra số tiền khách đưa hợp lệ
        if (isNaN(tienKhachDua) || tienKhachDua < 0) {
            $scope.tienKhachDua = 0;
            $scope.tienThua = 0;
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng nhập một số tiền hợp lệ cho số tiền khách đưa.',
                icon: 'error',
                confirmButtonText: 'Đóng'
            });
            return;
        }

        // Kiểm tra tổng tiền thanh toán hợp lệ
        if (isNaN(totalPrice) || totalPrice < 0) {
            $scope.totalPrice = 0;
            $scope.tienThua = 0;
            Swal.fire({
                title: 'Lỗi!',
                text: 'Tổng tiền thanh toán không hợp lệ.',
                icon: 'error',
                confirmButtonText: 'Đóng'
            });
            return;
        }

        // Tính tiền thừa (nếu có)
        $scope.tienThua = tienKhachDua - totalPrice;

        // Hiển thị thông tin tính toán
        console.log("Tiền khách đưa:", tienKhachDua);
        console.log("Tổng tiền thanh toán:", totalPrice);
        console.log("Tiền thừa:", $scope.tienThua);
    };
    $scope.xacNhanThanhToan = function () {
        if ($scope.selectedPhuongThucThanhToan === 3) {
            console.log("Phương thức thanh toán là PayOS, bỏ qua xác nhận thanh toán.");
            return true; // Tiến hành thanh toán trực tiếp mà không cần xác nhận
        }
        var tienKhachDua = $scope.tienKhachDua ? $scope.tienKhachDua.replace(/,/g, '') : '0';
        tienKhachDua = parseFloat(tienKhachDua);
        var totalPrice = parseFloat($scope.totalPrice) || 0;

        // Kiểm tra nếu tiền khách đưa không hợp lệ
        if (isNaN(tienKhachDua) || tienKhachDua < 0) {
            $scope.tienKhachDua = "";
            $scope.tienThua = 0;
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng nhập một số tiền hợp lệ cho số tiền khách đưa.',
                icon: 'error',
                confirmButtonText: 'Đóng'
            });
            return; // Dừng hàm nếu tiền khách đưa không hợp lệ
        }

        // Kiểm tra tổng tiền thanh toán
        if (isNaN(totalPrice) || totalPrice < 0) {
            $scope.totalPrice = 0;
            $scope.tienThua = "";
            Swal.fire({
                title: 'Lỗi!',
                text: 'Tổng tiền thanh toán không hợp lệ.',
                icon: 'error',
                confirmButtonText: 'Đóng'
            });
            return; // Dừng hàm nếu tổng tiền thanh toán không hợp lệ
        }

        // Kiểm tra nếu số tiền khách đưa nhỏ hơn tổng tiền thanh toán
        if (tienKhachDua < totalPrice) {
            $scope.tienKhachDua = ""; // Reset lại ô nhập tiền khách đưa
            $scope.tienThua = 0;
            Swal.fire({
                title: 'Lỗi!',
                text: 'Số tiền khách đưa không đủ để thanh toán. Vui lòng nhập lại.',
                icon: 'error',
                confirmButtonText: 'Đóng'
            });
            return; // Dừng hàm nếu tiền khách đưa không đủ
        }

        // Nếu tất cả điều kiện hợp lệ, tiếp tục thực hiện hành động thanh toán
        console.log("Xác nhận thanh toán với số tiền khách đưa:", tienKhachDua);
        console.log("Tổng tiền thanh toán:", totalPrice);
        return true;
    };

    $scope.addToCart = function (detail) {
        // Lưu thông tin sản phẩm đang chọn để sử dụng sau này
        $scope.selectedProduct = detail;

        // Đóng modal chi tiết sản phẩm
        $('#productDetailsModal').modal('hide');

        // Hiển thị modal nhập số lượng
        $('#quantityModal').modal('show');
    };

    $scope.confirmAddToCart = function () {
        if (!$scope.selectedInvoice || !$scope.selectedInvoice.idHoaDon) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng tạo hoặc chọn hóa đơn trước khi thêm vào giỏ hàng!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return false;
        }
        // Kiểm tra xem số lượng đã được nhập chưa
        if (!$scope.quantity || $scope.quantity <= 0) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng nhập số lượng hợp lệ!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return false;
        }
        if ($scope.quantity > $scope.selectedProduct.soLuong) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Số lượng sản phẩm trong kho không đủ!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return false;
        }

        // Tính toán thông tin giỏ hàng
        let donGia = $scope.selectedProduct.gia_khuyen_mai || $scope.selectedProduct.gia_ban;
        let soLuong = $scope.quantity;
        let thanhTien = donGia * soLuong;

        let cartItem = {
            idSanPhamChiTiet: $scope.selectedProduct.id_san_pham_chi_tiet,
            soLuong: soLuong,
            donGia: donGia,
            thanhTien: thanhTien
        };

        // Lấy thông tin người dùng từ localStorage
        let userFromStorage = JSON.parse(localStorage.getItem('selectedUser'));

        if (!userFromStorage || !userFromStorage.idNguoiDung) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng đăng nhập lại!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return false;
        }

        // Gửi yêu cầu thêm sản phẩm vào giỏ hàng
        $http.post('http://localhost:8080/api/admin/gio_hang/them/' + userFromStorage.idNguoiDung, cartItem)
            .then(function (response) {
                console.log('Sản phẩm đã được thêm vào giỏ hàng:', response.data);
                Swal.fire({
                    title: 'Thành công!',
                    text: 'Thêm giỏ hàng thành công!',
                    icon: 'success',
                    timer: 1500,
                    showConfirmButton: false
                });

                // Đóng modal và cập nhật giỏ hàng
                $('#quantityModal').modal('hide');
                $scope.getCartItems();
                $scope.quantity = ""; // Cập nhật giỏ hàng
            })
            .catch(function (error) {
                console.error('Lỗi khi thêm sản phẩm vào giỏ hàng:', error);
                Swal.fire({
                    title: 'Lỗi!',
                    text: 'Không thể thêm sản phẩm vào giỏ hàng!',
                    icon: 'error',
                    confirmButtonText: 'Đóng'
                });
            });
    };



    $scope.getCartItems = function () {
        const selectedUser = JSON.parse(localStorage.getItem('selectedUser'));
        const selectedInvoice = JSON.parse(localStorage.getItem('selectedInvoice'));

        if (!selectedUser || !selectedUser.idNguoiDung) {
            console.log("Không có ID người dùng.");
            return;
        }

        if (!selectedInvoice || !selectedInvoice.idHoaDon) {
            console.log("Không có ID hóa đơn.");
            return;
        }

        $http.get('http://localhost:8080/api/admin/gio_hang/lay/' + selectedUser.idNguoiDung)
            .then(function (response) {
                console.log('Danh sách sản phẩm trong giỏ hàng:', response.data);
                $scope.cartItems = response.data;
                $scope.isCartEmpty = $scope.cartItems.length === 0;
                $scope.calculateTotalPrice();
                $scope.getProductDetails();
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
            $scope.originalTotalPrice = 0;  // Đảm bảo giá trị gốc cũng được cập nhật
            $scope.discount = 0;  // Đảm bảo giá trị giảm cũng được cập nhật
            return;
        }

        // Đặt lại tổng tiền gốc và tổng tiền giảm
        $scope.originalTotalPrice = originalTotalPrice;
        $scope.discount = discount;

        // Đặt lại tổng tiền
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
        $scope.discount = discount;
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


            if (!originalTotalPrice || originalTotalPrice <= 0) {
                $scope.voucherError = 'Tổng tiền phải lớn hơn 0 để áp dụng voucher!';
                return;
            }


            console.log("Tổng tiền khi áp mã voucher: ", requestData)

            // Gửi yêu cầu API kiểm tra voucher
            $http.post('http://localhost:8080/api/admin/ban_hang/apma' + $scope.voucherCode, originalTotalPrice)
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


                // $scope.selectedVoucher = null;
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
        if (voucher.isUsable) {
            $scope.selectedVoucher = voucher;
            $scope.voucherCode = voucher.maVoucher;  // Gán mã voucher vào input (nếu cần)
            $scope.voucherError = '';  // Reset lỗi
              console.log("Selected Voucher:", $scope.selectedVoucher);
            $scope.calculateTotalPrice();
        } else {
            $scope.voucherError = 'Voucher này không thể sử dụng.';
        }
    };



    $scope.deleteCartItem = function (productId) {
        // Lấy thông tin người dùng từ localStorage
        let userFromStorage = JSON.parse(localStorage.getItem('selectedUser'));

        if (!userFromStorage || !userFromStorage.idNguoiDung) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng chọn người dùng trước khi thực hiện thao tác.',
                icon: 'error',
                confirmButtonText: 'Đóng'
            });
            return;
        }

        const userId = userFromStorage.idNguoiDung;

        Swal.fire({
            title: 'Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?',
            text: 'Hành động này không thể hoàn tác!',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Xóa',
            cancelButtonText: 'Hủy'
        }).then((result) => {
            if (result.isConfirmed) {
                $http.delete('http://localhost:8080/api/admin/gio_hang/xoa-san-pham/' + userId + '/' + productId)
                    .then(function (response) {
                        console.log('Sản phẩm đã được xóa khỏi giỏ hàng:', response.data);
                        $scope.getCartItems(); // Load lại giỏ hàng
                        $scope.getProductDetails(); // Load lại sản phẩm
                        $scope.calculateTotalPrice(); // Tính toán lại tổng tiền sau khi xóa sản phẩm

                        Swal.fire({
                            title: 'Thành công!',
                            text: 'Sản phẩm đã được xóa khỏi giỏ hàng.',
                            icon: 'success',
                            timer: 1500,
                            showConfirmButton: false
                        });
                    })
                    .catch(function (error) {
                        console.error('Lỗi khi xóa sản phẩm khỏi giỏ hàng:', error);
                        Swal.fire({
                            title: 'Lỗi!',
                            text: 'Không thể xóa sản phẩm khỏi giỏ hàng. Vui lòng thử lại!',
                            icon: 'error',
                            confirmButtonText: 'OK'
                        });
                    });
            }
        });
    };


    $scope.deleteCart = function () {
        // Lấy thông tin người dùng từ localStorage
        let userFromStorage = JSON.parse(localStorage.getItem('selectedUser'));

        const userId = userFromStorage.idNguoiDung;

        // Gửi yêu cầu xóa giỏ hàng
        $http.delete('http://localhost:8080/api/admin/gio_hang/xoa-tat-ca/' + userId)
            .then(function (response) {
                console.log('Giỏ hàng đã được xóa:', response.data);
                // Có thể thêm các tác vụ sau khi xóa giỏ hàng nếu cần
            })
            .catch(function (error) {
                console.error('Lỗi khi xóa giỏ hàng:', error);
            });
    };

    $scope.updateQuantity = function (item) {
        // Kiểm tra nếu user chưa được chọn
        let userFromStorage = JSON.parse(localStorage.getItem('selectedUser'));

        if (!userFromStorage || !userFromStorage.idNguoiDung) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng chọn người dùng trước khi thực hiện thao tác.',
                icon: 'error',
                confirmButtonText: 'Đóng'
            });
            return;
        }

        // Lấy userId từ selectedUser
        const userId = userFromStorage.idNguoiDung;

        if (!item.soLuong || item.soLuong < 1) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Số lượng phải lớn hơn hoặc bằng 1.',
                icon: 'error',
                confirmButtonText: 'Đóng'
            });
            item.soLuong = 1; // Reset về giá trị hợp lệ
            return;
        }

        // Lấy số lượng tồn kho của sản phẩm từ API (ví dụ: gọi API lấy thông tin sản phẩm chi tiết)
        $http.get('http://localhost:8080/api/admin/ban_hang/search/' + item.idSanPhamChiTiet)
            .then(function (response) {
                const stockQuantity = response.data.soLuong;

                // Kiểm tra nếu số lượng muốn cập nhật vượt quá số lượng tồn kho
                if (item.soLuong > stockQuantity) {
                    Swal.fire({
                        title: 'Lỗi!',
                        text: `Số lượng sản phẩm trong kho chỉ còn ${stockQuantity} chiếc. Vui lòng chọn số lượng nhỏ hơn hoặc bằng ${stockQuantity}.`,
                        icon: 'error',
                        confirmButtonText: 'Đóng'
                    });
                    return;
                }

                // Nếu số lượng hợp lệ, tiếp tục cập nhật
                const updatedQuantity = { soLuong: item.soLuong };

                // Gọi API để cập nhật số lượng
                $http.put('http://localhost:8080/api/admin/gio_hang/cap-nhat/' + userId + '/' + item.idSanPhamChiTiet, updatedQuantity)
                    .then(function (response) {
                        console.log('Cập nhật số lượng thành công:', response.data);

                        // Load lại danh sách sản phẩm và tính tổng tiền
                        $scope.getProductDetails();
                        $scope.calculateTotalPrice();
                    })
                    .catch(function (error) {
                        console.error('Lỗi khi cập nhật số lượng:', error);
                        Swal.fire({
                            title: 'Lỗi!',
                            text: 'Có lỗi xảy ra khi cập nhật số lượng. Vui lòng thử lại!',
                            icon: 'error',
                            confirmButtonText: 'Đóng'
                        });
                    });
            })
        // .catch(function (error) {
        //     console.error('Lỗi khi lấy thông tin sản phẩm chi tiết:', error);
        //     // Log chi tiết lỗi trả về
        //     if (error.response) {
        //         console.log('Dữ liệu lỗi:', error.response.data);
        //         console.log('Mã lỗi:', error.response.status);
        //     } else {
        //         console.log('Lỗi không xác định:', error);
        //     }
        //     Swal.fire({
        //         title: 'Lỗi!',
        //         text: 'Không thể lấy thông tin sản phẩm. Vui lòng thử lại!',
        //         icon: 'error',
        //         confirmButtonText: 'Đóng'
        //     });
        // });
    };


    $scope.getPhuongThucThanhToan = function () {
        $http.get('http://localhost:8080/api/admin/phuong_thuc_thanh_toan/ten-phuong-thuc')
            .then(function (response) {
                console.log('Dữ liệu phương thức thanh toán:', response.data);
                $scope.phuongThucThanhToan = response.data;
                if ($scope.phuongThucThanhToan && $scope.phuongThucThanhToan.length > 0) {
                    $scope.selectPhuongThucThanhToan($scope.phuongThucThanhToan[0].id);  // Chọn phương thức thanh toán đầu tiên
                }
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
    $scope.createOrderStatus = function () {
        var hoaDonId = JSON.parse(localStorage.getItem('selectedInvoice')).idHoaDon;

        $http.post('http://localhost:8080/api/admin/ban_hang/create-trang-thai/' + hoaDonId)
            .then(function (response) {
                console.log("Trạng thái hóa đơn đã được tạo!");
                // Sau khi tạo trạng thái thành công, mở modal thanh toán
                $('#paymentModal').modal('show');
            })

    };

    $scope.TEST = async function () {
        if ($scope.selectedPhuongThucThanhToan === 1) {
            console.log('Phương thức thanh toán là ID 1, không chạy VNPAY');
            return;
        }

        if ($scope.selectedPhuongThucThanhToan === 3) {
            const paymentMethod = 'PayOS';
            const amount = $scope.totalPrice;
            const invoice = $scope.selectedInvoice;
            const description = 'Thanh toán cho hóa đơn';
            const returnUrl = 'http://127.0.0.1:5500/admin.html#!/ban_hang?message=Thanh%20toan%20thanh%20cong';
            const cancelUrl = 'http://127.0.0.1:5500/admin.html#!/ban_hang?message=Thanh%20toan%20that%20bai';

            try {
                const response = await $http.post('http://localhost:8080/api/admin/payos/create-payment-link', {
                    description: description,
                    returnUrl: returnUrl,
                    cancelUrl: cancelUrl,
                    price: amount
                });

                console.log('Dữ liệu trả về từ PayOS:', response.data);
                if (response.data.error === 0 && response.data.data && response.data.data.checkoutUrl) {
                    const paymentUrl = response.data.data.checkoutUrl; // Lấy URL thanh toán từ PayOS
                    console.log('Điều hướng đến URL thanh toán PayOS:', paymentUrl);
                    window.location.href = paymentUrl;

                    // Chờ người dùng hoàn thành thanh toán, sau đó kiểm tra URL hiện tại
                    const checkUrl = async () => {
                        if (window.location.href === returnUrl) {
                            console.log('Thanh toán thành công, bắt đầu tạo hóa đơn chi tiết.');
                            await $scope.createHoaDonChiTiet();
                        } else {
                            console.log('Chưa hoàn thành thanh toán hoặc URL không khớp.');
                        }
                    };

                    // Liên tục kiểm tra URL mỗi 2 giây
                    const interval = setInterval(async () => {
                        await checkUrl();
                    }, 2000);
                    setTimeout(() => clearInterval(interval), 60000);
                } else {
                    alert('Không thể tạo liên kết thanh toán PayOS. Vui lòng thử lại.');
                }
            } catch (error) {
                console.error('Lỗi trong quá trình gọi PayOS:', error);
                alert('Có lỗi xảy ra khi tạo liên kết thanh toán PayOS.');
            }
        }
    };

    $scope.validateInvoiceCreation = function () {
        if (!$scope.selectedInvoice || !$scope.selectedInvoice.idHoaDon) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng tạo hoặc chọn hóa đơn trước khi thanh toán!',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return false;
        }

        if ($scope.isCartEmpty) {
            Swal.fire({
                title: 'Thông báo!',
                text: 'Giỏ hàng trống. Vui lòng thêm sản phẩm để tiếp tục thanh toán.',
                icon: 'warning',
                confirmButtonText: 'OK'
            });
            return false;
        }
        return true;
    };
    $scope.createPaymentHistory = function (amount) {
        // Lấy thông tin từ localStorage
        const selectedInvoice = JSON.parse(localStorage.getItem('selectedInvoice'));
        const selectedUser = JSON.parse(localStorage.getItem('selectedUser'));
    
        // Kiểm tra tính hợp lệ
        if (!selectedInvoice || !selectedInvoice.idHoaDon || !selectedUser || !selectedUser.idNguoiDung) {
            console.error('Thông tin hóa đơn hoặc người dùng không hợp lệ.');
            Swal.fire({
                title: 'Lỗi!',
                text: 'Thông tin hóa đơn hoặc người dùng không hợp lệ.',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return;
        }
    
        // Dữ liệu gửi lên server
        const paymentData = {
            idHoaDon: selectedInvoice.idHoaDon,
            idNguoiDung: selectedUser.idNguoiDung,
            soTienThanhToan:  $scope.totalPrice,
        };
    
        // Gửi yêu cầu POST tạo lịch sử thanh toán
        $http.post('http://localhost:8080/api/nguoi_dung/lich-su-thanh-toan/create', paymentData)
            .then(function (response) {
                console.log('Tạo lịch sử thanh toán thành công:', response.data);
            })
            .catch(function (error) {
                console.error('Lỗi khi tạo lịch sử thanh toán:', error);
            });
    };
    
    $scope.createHoaDonChiTiet = async function () {
        // Kiểm tra xác nhận thanh toán
        if ($scope.xacNhanThanhToan()) {
            // Nếu phương thức thanh toán là PayOS (ví dụ, phương thức thanh toán 3)
            if ($scope.selectedPhuongThucThanhToan === 3) {
                await $scope.TEST(); // Gọi hàm xử lý PayOS
                return; // Dừng lại nếu là phương thức thanh toán 3
            }

            try {
                // Kiểm tra các điều kiện trước khi tạo hóa đơn chi tiết
                if (!$scope.validateInvoiceCreation()) return;

                // Lấy thông tin người dùng từ localStorage
                let userFromStorage = JSON.parse(localStorage.getItem('selectedUser'));

                if (!userFromStorage || !userFromStorage.idNguoiDung) {
                    Swal.fire({
                        title: 'Lỗi!',
                        text: 'Vui lòng chọn người dùng trước khi tạo hóa đơn.',
                        icon: 'error',
                        confirmButtonText: 'Đóng'
                    });
                    return;
                }

                const userId = userFromStorage.idNguoiDung;

                // Cập nhật trạng thái hóa đơn
                await $scope.updateInvoiceStatus();
                await    $scope.createPaymentHistory();
                // Chạy các tác vụ không phụ thuộc đồng thời
                await Promise.all([
                    // Cập nhật trạng thái hóa đơn
                    $scope.deleteGioHangChiTietByUserId(), // Xóa chi tiết giỏ hàng theo người dùng
                    $scope.createPaymentMethod(),         // Tạo phương thức thanh toán
                    $scope.deleteCart(),                  // Xóa toàn bộ giỏ hàng
                ]);

                // Xử lý sản phẩm trong giỏ hàng và hóa đơn chi tiết
                await $scope.processCartItems();

                // Gửi API tạo hóa đơn chi tiết
                const response = await $http.post(
                    "http://localhost:8080/api/admin/hoa_don_chi_tiet/create",
                    $scope.hoaDonChiTietDTO,
                    { params: { userId: userId } }
                );
                console.log("Hóa đơn chi tiết:", response.data);

                // Nếu có voucher được chọn, trừ số lượng của voucher
                if ($scope.selectedVoucher) {
                    await $scope.deductVoucherQuantity();
                }

                // Reset dữ liệu sau khi tạo hóa đơn
                $scope.resetAfterInvoiceCreation();

                // Thông báo thành công
                Swal.fire({
                    title: 'Thành công!',
                    text: 'Thanh toán thành công!',
                    icon: 'success',
                    timer: 1500,
                    showConfirmButton: false
                });

            } catch (error) {
                console.error("Lỗi xảy ra:", error);
                Swal.fire({
                    title: 'Lỗi!',
                    text: 'Có lỗi xảy ra trong quá trình tạo hóa đơn.',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            }
        } else {
            // Nếu xác nhận thanh toán không hợp lệ, không thực hiện tiếp các bước sau
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng nhập đầy đủ thông tin Thanh toán.',
                icon: 'error',
                confirmButtonText: 'Đóng'
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
        // Lấy thông tin người dùng từ localStorage
        let userFromStorage = JSON.parse(localStorage.getItem('selectedUser'));
        const userId = userFromStorage.idNguoiDung;

        // Gửi yêu cầu xóa chi tiết giỏ hàng
        $http.delete("http://localhost:8080/api/admin/gio_hang/delete/user/" + userId)
            .then(function (response) {
                $scope.getCartItems(); // Tải lại các mục trong giỏ hàng
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
        // Lấy thông tin người dùng từ localStorage
        let userFromStorage = JSON.parse(localStorage.getItem('selectedUser'));

        if (!userFromStorage || !userFromStorage.sdt) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Không tìm thấy thông tin số điện thoại người dùng. Vui lòng chọn người dùng trước khi thực hiện thao tác.',
                icon: 'error',
                confirmButtonText: 'Đóng'
            });
            return;
        }


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
                        setSdtNguoiNhan: userFromStorage.sdt, // Lấy số điện thoại từ localStorage
                        trangThaiHoaDon: $scope.selectedPhuongThucThanhToan === 2 ? 4 : 5,
                        idVoucher: $scope.selectedVoucher ? $scope.selectedVoucher.idVoucher : null
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
    $scope.getUnpaidInvoices();
    $scope.getPhuongThucThanhToan();
    $scope.getVoucher();
    $scope.dsvoucher();
}