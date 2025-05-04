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
                    $scope.newCustomer = {};
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
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy danh sách hóa đơn chưa thanh toán:', error);
            });
    };
    $scope.createInvoice = function () {
        // Kiểm tra số hóa đơn chưa thanh toán
        if ($scope.unpaidInvoices && $scope.unpaidInvoices.length >= 5) {
            Swal.fire({
                title: 'Thông báo!',
                text: 'Đã đạt giới hạn tối đa 5 hóa đơn chưa thanh toán. Vui lòng thanh toán trước khi tạo hóa đơn mới.',
                icon: 'warning',
                confirmButtonText: 'Đóng'
            });
            return;
        }

        var user = JSON.parse(localStorage.getItem("user"));
        console.log(user.idNguoiDung);  // ID của nhân viên
        console.log(user.tenNguoiDung); // Tên nhân viên

        // Kiểm tra xem đã chọn người dùng chưa
        if (!$scope.selectedUser || !$scope.selectedUser.idNguoiDung || !$scope.selectedUser.tenNguoiDung) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng chọn người dùng trước khi tạo hóa đơn.',
                icon: 'error',
                confirmButtonText: 'Đóng'
            });
            return;
        }

        if (!$scope.searchTerm || $scope.searchTerm.trim() === "") {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Vui lòng chọn người dùng trước khi tạo hóa đơn.',
                icon: 'error',
                confirmButtonText: 'Đóng'
            });
            return;
        }

        // Nếu số hóa đơn chưa thanh toán hợp lệ, tiếp tục tạo hóa đơn
        let newInvoice = {
            tenNguoiNhan: $scope.selectedUser.tenNguoiDung,
            nguoiDung: { idNguoiDung: $scope.selectedUser.idNguoiDung },
            ngayTao: new Date()
        };

        $http.post('http://localhost:8080/api/admin/hoa_don_ban_hang/create?idNhanVien=' + user.idNguoiDung, newInvoice)
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
                        $scope.searchTerm = "";
                        $scope.selectedInvoice = { maHoaDon: 0 };
                        $scope.deleteCartsl()

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

    $scope.deleteCartsl = function () {
        // Lấy thông tin người dùng từ localStorage
        let userFromStorage = JSON.parse(localStorage.getItem('selectedUser'));

        const userId = userFromStorage.idNguoiDung;

        // Gửi yêu cầu xóa giỏ hàng
        $http.delete('http://localhost:8080/api/admin/gio_hang/xoa/' + userId)
            .then(function (response) {
                console.log('Giỏ hàng đã được xóa:', response.data);
                $scope.getCartItems();
            })
            .catch(function (error) {
                console.error('Lỗi khi xóa giỏ hàng:', error);
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
                $scope.quantity = "";
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
                console.error("Có lỗi xảy ra khi g  ọi API sản phẩm chi tiết:", error);
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

    $scope.kiemTraKhuyenMaiHopLe = function (sp) {
        if (!sp.coKhuyenMai || !sp.ngayBatDauKhuyenMai || !sp.ngayKetThucKhuyenMai) {
            return false;
        }

        const now = new Date();
        const batDau = new Date(sp.ngayBatDauKhuyenMai);
        const ketThuc = new Date(sp.ngayKetThucKhuyenMai);

        return now >= batDau && now <= ketThuc;
    };
    $scope.kiemTraKhuyenMaiDetailHopLe = function (detail) {
        // Chỉ loại null/undefined, cho phép 0
        if (detail.gia_khuyen_mai == null
            || !detail.ngay_bat_dau
            || !detail.ngay_ket_thuc) {
            return false;
        }
        const now = new Date();
        const batDau = new Date(detail.ngay_bat_dau);
        const ketThuc = new Date(detail.ngay_ket_thuc);
        return now >= batDau && now <= ketThuc;
    };
    $scope.getGiaCuoi = function (sp) {
        return $scope.kiemTraKhuyenMaiHopLe(sp)
            ? sp.giaKhuyenMai
            : sp.giaBan;
    };

    // 3) Fetch dữ liệu và sort theo yêu cầu
    $scope.getProductDetails = function () {
        $http.get('http://localhost:8080/api/admin/ban_hang/all_quay')
            .then(function (response) {
                let list = response.data;

                list.sort(function (a, b) {
                    const aPromo = $scope.kiemTraKhuyenMaiHopLe(a);
                    const bPromo = $scope.kiemTraKhuyenMaiHopLe(b);

                    // 1) Cả A và B đều có KM hợp lệ → so sánh giáKhuyenMai tăng dần
                    if (aPromo && bPromo) {
                        return a.giaKhuyenMai - b.giaKhuyenMai;
                    }
                    // 2) Chỉ A có KM → A lên trước
                    if (aPromo && !bPromo) {
                        return -1;
                    }
                    // 3) Chỉ B có KM → B lên trước
                    if (!aPromo && bPromo) {
                        return 1;
                    }
                    // 4) Cả hai không KM hoặc hết hạn → sort theo giá gốc tăng dần
                    return a.giaBan - b.giaBan;
                });

                $scope.sanPhamChiTietList = list;
            })
            .catch(function (err) {
                console.error('Lỗi khi gọi API:', err);
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

        // Lưu thông tin vào sessionStorage
        const paymentInfo = {
            tienKhachDua: $scope.tienKhachDua,
            tienThua: $scope.tienThua,
            totalPrice: $scope.totalPrice
        };

        // Lưu thông tin vào sessionStorage dưới dạng chuỗi JSON
        sessionStorage.setItem('paymentInfo', JSON.stringify(paymentInfo));

        console.log('Thông tin thanh toán đã lưu vào sessionStorage:', paymentInfo);
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
            item.soLuong = 1;
            return false;
        }
        let now = new Date();
        let batDau = new Date($scope.selectedProduct.ngay_bat_dau);
        let ketThuc = new Date($scope.selectedProduct.ngay_ket_thuc);

        let khuyenMaiHopLe = $scope.selectedProduct.gia_khuyen_mai !== null &&
            $scope.selectedProduct.gia_khuyen_mai !== undefined &&
            now >= batDau && now <= ketThuc;

        let donGia = khuyenMaiHopLe ? $scope.selectedProduct.gia_khuyen_mai : $scope.selectedProduct.gia_ban;

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
                    text: 'Số lượng tồn không đủ!',
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

                // Lưu cartItems vào localStorage
                localStorage.setItem('cartItems', JSON.stringify($scope.cartItems));

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
            if ($scope.totalPrice < minValue || ($scope.selectedVoucher.kieuGiamGia === false && $scope.totalPrice > maxValue)) {
                $scope.selectedVoucher = null;
            } else {
                if ($scope.selectedVoucher.kieuGiamGia === false) {
                    // Giảm theo tỷ lệ phần trăm
                    discount = $scope.totalPrice * ($scope.selectedVoucher.giaTriGiamGia / 100);
                } else {
                    // Giảm theo giá trị cố định
                    discount = $scope.selectedVoucher.giaTriGiamGia;
                }
                discount = Math.min(discount, $scope.totalPrice);
                // Áp dụng giảm giá
                $scope.totalPrice -= discount;
                $scope.totalPrice = Math.max($scope.totalPrice, 0);  // Đảm bảo tổng tiền không âm
            }
        }
        $scope.discount = discount;

        // Lưu thông tin vào localStorage
        const totalPriceInfo = {
            originalTotalPrice: $scope.originalTotalPrice,
            discount: $scope.discount,
            totalPrice: $scope.totalPrice
        };

        // Lưu thông tin vào localStorage dưới dạng chuỗi JSON
        localStorage.setItem('totalPriceInfo', JSON.stringify(totalPriceInfo));

        // Lưu riêng originalTotalPrice vào localStorage
        localStorage.setItem('originalTotalPrice', $scope.originalTotalPrice);
        localStorage.setItem('discount', $scope.discount);
        console.log('Thông tin tổng tiền đã lưu vào localStorage:', totalPriceInfo);
    };




    $scope.getVoucher = function () {
        if ($scope.voucherCode) {
            const storedUser = localStorage.getItem('selectedUser');
            const nguoiDung = storedUser ? JSON.parse(storedUser) : null;
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
            console.log("Tổng tiền khi áp mã voucher: ", originalTotalPrice)

            // Gửi yêu cầu API kiểm tra voucher
            $http.post(`http://localhost:8080/api/admin/ban_hang/apma/${$scope.voucherCode}/${originalTotalPrice}`)
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
        const storedUser = localStorage.getItem('selectedUser');
        const nguoiDung = storedUser ? JSON.parse(storedUser) : null;

        // Tính tổng tiền chưa giảm giá (gốc)
        let originalTotalPrice = 0;
        if ($scope.cartItems && Array.isArray($scope.cartItems)) {
            $scope.cartItems.forEach(function (item) {
                if (item && item.soLuong && item.giaBan) {
                    originalTotalPrice += item.soLuong * item.giaBan;
                }
            });
        } else {
            console.log('Giỏ hàng không hợp lệ:', $scope.cartItems);
        }

        if (originalTotalPrice <= 0) {
            console.log('Tổng tiền chưa giảm giá không hợp lệ:', originalTotalPrice);
            return;
        }

        // Gửi yêu cầu API với tổng tiền chưa giảm giá
        $http.get(`http://localhost:8080/api/admin/ban_hang/allvoucher/${originalTotalPrice}`)
            .then(function (response) {
                $scope.availableVouchers = response.data || [];
                console.log("Danh sách voucher trước khi lọc:", $scope.availableVouchers);
                console.log('Tổng tiền chưa giảm giá:', originalTotalPrice);

                console.log("Danh sách voucher sau khi lọc:", $scope.availableVouchers);
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy danh sách voucher:', error);
            });
    };


    $scope.confirmVoucher = function () {
        if ($scope.tempVoucher) {
            const voucher = $scope.tempVoucher;

            // Kiểm tra giá trị tối thiểu
            if ($scope.totalPrice < voucher.soTienToiThieu) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: `Đơn hàng phải có giá trị từ ${voucher.soTienToiThieu.toLocaleString()} VND để áp dụng voucher.`,
                    confirmButtonText: 'OK',
                    customClass: {
                        confirmButton: 'btn btn-danger'
                    },
                    buttonsStyling: false
                });
                return;
            }

            // Kiểm tra giá trị tối đa nếu giảm giá là phần trăm
            if (!voucher.kieuGiamGia && $scope.totalPrice > voucher.giaTriToiDa) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: `Đơn hàng vượt quá mức tối đa mà voucher có thể áp dụng (${voucher.giaTriToiDa.toLocaleString()} VND).`,
                    confirmButtonText: 'OK',
                    customClass: {
                        confirmButton: 'btn btn-danger'
                    },
                    buttonsStyling: false
                });
                return;
            }

            // Áp dụng voucher thành công
            $scope.selectedVoucher = $scope.tempVoucher; // Gán voucher chính thức
            $scope.tempVoucher = null; // Reset voucher tạm
            console.log("Confirmed Voucher:", $scope.selectedVoucher);
            localStorage.setItem('voucherdachon', JSON.stringify($scope.selectedVoucher));
            // Thông báo thành công
            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: `Áp dụng thành công mã voucher: ${voucher.maVoucher}`,
                confirmButtonText: 'OK',
                customClass: {
                    confirmButton: 'btn btn-success'
                },
                buttonsStyling: false
            }).then((result) => {
                if (result.isConfirmed) {
                    // Đóng modal nếu cần
                    $('#voucherModal').modal('hide');
                }
            });

            // Cập nhật tổng giá trị đơn hàng
            $scope.calculateTotalPrice();
        } else {
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

    $scope.removeVoucher = function () {
        // Xóa voucher đã chọn
        $scope.selectedVoucher = null;
        $scope.voucherCode = '';  // Xóa mã voucher (nếu có)

        // Cập nhật lại tổng tiền
        $scope.discount = 0;
        $scope.totalPrice = $scope.originalTotalPrice; // Đặt lại tổng tiền về giá trị gốc

        // Cập nhật lại thông tin trong localStorage
        const totalPriceInfo = {
            originalTotalPrice: $scope.originalTotalPrice,
            discount: $scope.discount,
            totalPrice: $scope.totalPrice
        };

        // Lưu thông tin mới vào localStorage
        localStorage.setItem('totalPriceInfo', JSON.stringify(totalPriceInfo));
        localStorage.removeItem('selectedVoucher'); // Xóa voucher đã lưu trong localStorage
        localStorage.removeItem('voucherdachon');
        console.log('Voucher đã bị xóa và giao diện đã được cập nhật.');
    };

    $scope.tempVoucher = null; // Biến tạm để lưu voucher trước khi xác nhận
    $scope.selectedVoucher = null; // Voucher chính thức sau khi xác nhận
    $scope.selectVoucher = function (voucher) {
        if (voucher.isUsable) {
            $scope.tempVoucher = voucher;

            $scope.voucherCode = voucher.maVoucher;  // Gán mã voucher vào input (nếu cần)
            $scope.voucherError = '';  // Reset lỗi
            console.log("Selected Voucher:", $scope.selectedVoucher);
            // Lưu voucher vào localStorage
            localStorage.setItem('selectedVoucher', JSON.stringify($scope.selectedVoucher)); // Lưu đối tượng voucher vào localStorage
            localStorage.setItem('voucherCode', $scope.voucherCode); // Lưu mã voucher vào localStorage nếu cần
        } else {
            $scope.voucherError = 'Voucher này không thể sử dụng.';
        }
    };



    $scope.saveCartToSessionStorage = function () {
        // Lưu giỏ hàng vào sessionStorage trước khi xóa
        sessionStorage.setItem('giohang', JSON.stringify($scope.cartItems));
    };

    $scope.deleteCartItem = function (productId) {
        $scope.saveCartToSessionStorage(); // Lưu giỏ hàng vào sessionStorage

        // Lấy thông tin người dùng từ sessionStorage
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
                        $scope.tienKhachDua = "";
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
        // Lấy thông tin người dùng từ sessionStorage
        $scope.saveCartToSessionStorage(); // Lưu giỏ hàng vào sessionStorage

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

        // Cập nhật số lượng trước
        const updatedQuantity = { soLuong: item.soLuong };

        // Gọi API để cập nhật số lượng giỏ hàng
        $http.put('http://localhost:8080/api/admin/gio_hang/cap-nhat/' + userId + '/' + item.idSanPhamChiTiet, updatedQuantity)
            .then(function (response) {
                console.log('Cập nhật số lượng thành công:', response.data);

                // Sau khi cập nhật, gọi API để lấy thông tin sản phẩm chi tiết (kiểm tra số lượng tồn kho)
                $http.get('http://localhost:8080/api/admin/ban_hang/search/' + item.idSanPhamChiTiet)
                    .then(function (response) {
                        const stockQuantity = response.data.soLuong;

                        // Kiểm tra nếu số lượng muốn cập nhật vượt quá số lượng tồn kho


                        // Load lại danh sách sản phẩm và tính tổng tiền
                        $scope.getProductDetails();
                        $scope.calculateTotalPrice();
                        $scope.getCartItems();
                    })
                    .catch(function (error) {
                        console.error('Lỗi khi lấy thông tin sản phẩm chi tiết:', error);
                        Swal.fire({
                            title: 'Lỗi!',
                            text: 'Không thể lấy thông tin sản phẩm. Vui lòng thử lại!',
                            icon: 'error',
                            confirmButtonText: 'Đóng'
                        });
                    });
            })
            .catch(function (error) {
                Swal.fire({
                    title: 'Lỗi!',
                    text: 'số lượng vượt quá số lượng tồn!',
                    icon: 'error',
                    confirmButtonText: 'Đóng'
                });
                item.soLuong = 1;
            });
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
        var selectedMethod = $scope.phuongThucThanhToan.find(function (pt) {
            return pt.id === id;
        });

        $scope.phuongThuc = selectedMethod ? selectedMethod.tenPhuongThuc : '';
        console.log('Phương thức thanh toán đã chọn:', $scope.phuongThuc);

        // Lưu phương thức thanh toán vào localStorage
        const paymentMethodData = {
            id: $scope.selectedPhuongThucThanhToan,
            tenPhuongThuc: $scope.phuongThuc
        };

        // Lưu vào localStorage
        localStorage.setItem('selectedPaymentMethod', JSON.stringify(paymentMethodData));
    };

    $scope.createOrderStatus = function () {
        var hoaDonId = JSON.parse(localStorage.getItem('selectedInvoice')).idHoaDon;
        var user = JSON.parse(localStorage.getItem('user'));

        // Lấy danh sách trạng thái đã tạo từ localStorage
        var createdStatuses = JSON.parse(localStorage.getItem('createdOrderStatuses')) || [];

        if (createdStatuses.includes(hoaDonId)) {
            console.log("Trạng thái hóa đơn đã tồn tại!");
            return; // Dừng lại, không gọi API
        }

        // Gửi yêu cầu tạo trạng thái hóa đơn
        $http.post('http://localhost:8080/api/admin/ban_hang/create-trang-thai/' + hoaDonId + '/' + user.idNguoiDung)
            .then(function (response) {
                console.log("Trạng thái hóa đơn đã được tạo!");

                // Lưu trạng thái hóa đơn vào danh sách và cập nhật localStorage
                createdStatuses.push(hoaDonId);
                localStorage.setItem('createdOrderStatuses', JSON.stringify(createdStatuses));

                // Sau khi tạo trạng thái thành công, mở modal thanh toán
                $('#paymentModal').modal('show');
            })
            .catch(function (error) {
                console.error("Lỗi khi tạo trạng thái hóa đơn:", error);
            });
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
            const returnUrl = 'http://127.0.0.1:5501/admin.html#!/ban_hang';
            const cancelUrl = 'http://127.0.0.1:5501/admin.html#!/ban_hang?message=Thanh%20toan%20that%20bai';

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
        const user = JSON.parse(localStorage.getItem('user'));

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
            idNhanVien: user.idNguoiDung,
            soTienThanhToan: $scope.totalPrice,
        };

        // Gửi yêu cầu POST tạo lịch sử thanh toán
        $http.post('http://localhost:8080/api/admin/lich_su_thanh_toan/create', paymentData)
            .then(function (response) {
                console.log('Tạo lịch sử thanh toán thành công:', response.data);
            })
            .catch(function (error) {
                console.error('Lỗi khi tạo lịch sử thanh toán:', error);
            });
    };
    async function checkVoucher(idVoucher) {
        try {
            const response = await fetch(
                `http://localhost:8080/api/admin/ban_hang/vouchers/${idVoucher}`
            );
            if (!response.ok) {
                throw new Error('Không thể lấy thông tin voucher');
            }

            const data = await response.json();

            if (data.soLuong <= 0) {
                await Swal.fire({
                    icon: 'error',
                    title: 'Voucher không hợp lệ',
                    text: 'Rất tiếc, voucher này đã hết số lượng và không thể sử dụng nữa.',
                    confirmButtonText: 'OK'
                });
                localStorage.removeItem('voucherdachon');
                $scope.removeVoucher();
                $scope.$apply();
                return false;
            }
            if (data.idTrangThaiGiamGia === 3) {
                await Swal.fire({
                    icon: 'error',
                    title: 'Voucher không hợp lệ',
                    text: 'Voucher đã hết hạn.',
                    confirmButtonText: 'OK'
                });
                localStorage.removeItem('voucherdachon');
                $scope.removeVoucher();
                $scope.$apply();
                return false;
            }
            if (data.idTrangThaiGiamGia === 4) {
                await Swal.fire({
                    icon: 'error',
                    title: 'Voucher không hợp lệ',
                    text: 'Voucher chưa phát hành.',
                    confirmButtonText: 'OK'
                });
                localStorage.removeItem('voucherdachon');
                $scope.removeVoucher();
                $scope.$apply();
                return false;
            }
            if (data.idTrangThaiGiamGia === 5) {
                await Swal.fire({
                    icon: 'error',
                    title: 'Voucher không hợp lệ',
                    text: 'Voucher này đã bị xóa.',
                    confirmButtonText: 'OK'
                });
                localStorage.removeItem('voucherdachon');
                $scope.removeVoucher();
                $scope.$apply();
                return false;
            }

            // Voucher hợp lệ
            return true;
        } catch (error) {
            console.error('Lỗi khi kiểm tra voucher:', error);
            await Swal.fire({
                icon: 'error',
                title: 'Lỗi hệ thống',
                text: 'Không thể kiểm tra voucher, vui lòng thử lại sau.',
                confirmButtonText: 'OK'
            });
            return false;
        }
    }
    $scope.createHoaDonChiTiet = async function () {

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
                const cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];

                for (const item of cartItems) {
                    const idSanPhamChiTiet = item.idSanPhamChiTiet;
                    const tenSanPham = item.tenSanPham;
                    // Gọi API kiểm tra trạng thái sản phẩm
                    try {
                        const response = await $http.get(`http://localhost:8080/api/admin/ban_hang/check-trang-thai/${idSanPhamChiTiet}`);

                        // Kiểm tra mã trạng thái HTTP
                        if (response.status === 200) {
                            // Sản phẩm đang hoạt động, tiếp tục xử lý
                            console.log(`Sản phẩm ID ${idSanPhamChiTiet} đang hoạt động.`);
                        }
                    } catch (error) {
                        // Nếu có lỗi khi gọi API
                        Swal.fire({
                            icon: 'error',
                            title: 'Sản phẩm ngừng bán',
                            text: `Sản phẩm "${tenSanPham}" đã ngừng bán.!`,
                        });
                        return;
                    }
                }
                // Kiểm tra trạng thái voucher nếu có trong localStorage
                function showVoucherErrorAndRemove(title, message) {
                    Swal.fire({
                        icon: 'error',
                        title: title,
                        text: message
                    }).then(() => {
                     
                        
                        return;

                    });
                }
                
                const voucher = JSON.parse(localStorage.getItem('voucherdachon'));

                if (voucher && voucher.idVoucher != null) {
                    try {
                        const voucherId = voucher.idVoucher;
                        const voucherResponse = await $http.get(`http://localhost:8080/api/admin/ban_hang/vouchers/${voucherId}`);
                        const voucherData = voucherResponse.data;
                        if (voucherData.soLuong <= 0) {
                            Swal.fire({
                                icon: 'error',
                                title: 'Voucher đã hết!',
                                text: 'Số lượng sử dụng voucher này đã hết.',
                                confirmButtonText: 'OK'
                            }).then(() => {
                                localStorage.removeItem('voucherdachon');
                                $scope.removeVoucher(); // Xóa ở giao diện
                                return;
                            });
                            return;
                        }
                        const trangThaiVoucher = voucherData.trangThaiGiamGiaId;
                
                        switch (trangThaiVoucher) {
                            case 1:
                                // OK – đang phát hành
                                break;
                            case 2:
                                showVoucherErrorAndRemove('Voucher đã sử dụng!', 'Voucher này không còn giá trị.');
                                return;
                            case 3:
                                showVoucherErrorAndRemove('Voucher hết hạn!', 'Voucher không còn hiệu lực.');
                                return;
                            case 4:
                                showVoucherErrorAndRemove('Voucher chưa phát hành!', 'Voucher chưa được phát hành.');
                                return;
                            case 5:
                                showVoucherErrorAndRemove('Voucher đã bị xóa!', 'Voucher không còn hiệu lực sử dụng.');
                                return;
                            case 6:
                                showVoucherErrorAndRemove('Số lượng voucher đã hết!', 'Vui lòng thử lại vào dịp sau.');
                                return;
                            default:
                                showVoucherErrorAndRemove('Lỗi trạng thái!', 'Không thể sử dụng voucher này.');
                                return;
                        }
                    } catch (error) {
                        console.error("Lỗi khi kiểm tra trạng thái voucher:", error);
                        showVoucherErrorAndRemove('Lỗi!', 'Không thể kiểm tra thông tin voucher.');
                        return;
                    }
                } else {
                    // Nếu không có voucher nào được chọn
                    console.log("Không sử dụng voucher – bỏ qua kiểm tra.");
                }
                                
               
                await $scope.createPaymentHistory();
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
                // Lấy ra mã hóa đơn vừa tạo
                const selectedInvoice = JSON.parse(localStorage.getItem('selectedInvoice'));

                // Nếu có voucher được chọn => lưu voucher vào hóa đơn
                try {
                    const responseVoucher = await $http.post("http://localhost:8080/api/admin/hoa_don/luu-voucher", {
                        maHoaDon: selectedInvoice.maHoaDon,
                        maVoucherApDung: voucher.maVoucher,
                        tenVoucherApDung: voucher.tenVoucher,
                        giaTriGiamGiaApDung: voucher.giaTriGiamGia,
                        kieuGiamGiaApDung: voucher.kieuGiamGia
                    });
                    console.log("Lưu voucher vào hóa đơn thành công", responseVoucher.data);
                } catch (error) {
                    
                }
                localStorage.removeItem('voucherdachon');
                // Nếu có voucher được chọn, trừ số lượng của voucher
                if ($scope.selectedVoucher) {
                    if (!$scope.selectedVoucher.isUsable) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Voucher không hợp lệ!',
                            text: 'Voucher này đã bị xóa hoặc đã hết hạn.',
                            confirmButtonText: 'Đóng'
                        });
                        return; // Dừng luôn, không gọi deductVoucherQuantity
                    }

                    // Nếu hợp lệ thì mới trừ số lượng
                    await $scope.deductVoucherQuantity();
                }

                // Reset dữ liệu sau khi tạo hóa đơn
                $scope.resetAfterInvoiceCreation();

                Swal.fire({
                    title: 'Thành công!',
                    text: 'Thanh toán thành công!',
                    icon: 'success',
                    timer: 1500,
                    showConfirmButton: false
                }).then(() => {
                    // Sau khi hiển thị thông báo thành công, hỏi người dùng có muốn in hóa đơn không
                    Swal.fire({
                        title: 'Bạn có muốn in hóa đơn không?',
                        icon: 'question',
                        showCancelButton: true,
                        confirmButtonText: 'In Hóa Đơn',
                        cancelButtonText: 'Không'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            // Nếu người dùng chọn "In Hóa Đơn", gọi hàm in hóa đơn
                            $scope.printInvoice();
                        }
                    });
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
            // Gửi yêu cầu trừ số lượng voucher, sử dụng tổng tiền trước giảm giá
            const response = await $http.post(
                `http://localhost:8080/api/admin/ban_hang/use/${$scope.selectedVoucher.maVoucher}`,
                $scope.originalTotalPrice  // Sử dụng tổng tiền chưa giảm giá
            );
            console.log("Số lượng voucher đã được trừ.");

            // Cập nhật lại số lượng voucher trong giao diện người dùng
            $scope.selectedVoucher.soLuong--;
        } catch (error) {
            if (error.response && error.response.status === 400) {
                Swal.fire({
                    icon: 'error',
                    title: 'Voucher không hợp lệ!',
                    text: 'Voucher này không thể được phát hành hoặc đã hết hạn.',
                    confirmButtonText: 'Thử lại'
                });
            }
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

                    var user = JSON.parse(localStorage.getItem("user"));
                    console.log("Cấu trúc của updatedInvoice trước khi gửi:", updatedInvoice);
                    return $http.put("http://localhost:8080/api/admin/hoa_don_ban_hang/cap-nhat/" + $scope.selectedInvoice.idHoaDon + "?idNhanVien=" + user.idNguoiDung, updatedInvoice)
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


    // Khởi tạo các biến từ localStorage hoặc giá trị mặc định
    $scope.cartItems = JSON.parse(localStorage.getItem('giohang')) || [];
    $scope.totalPriceInfo = JSON.parse(localStorage.getItem('totalPriceInfo')) || {};
    $scope.paymentInfo = JSON.parse(sessionStorage.getItem('paymentInfo')) || {};
    $scope.selectedInvoice = JSON.parse(localStorage.getItem('selectedInvoice')) || {};
    $scope.selectedUser = JSON.parse(localStorage.getItem('selectedUser')) || {};


    // Định dạng số thành tiền tệ
    // Định dạng số thành tiền tệ (VNĐ) không có từ 'VND' phía sau
    $scope.formatCurrency = function (value) {
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' })
            .format(value)
            .replace('₫', ' VNĐ'); // Chuyển ký hiệu "₫" thành " VNĐ"
    };

    $scope.printInvoice = function () {
        // Lấy dữ liệu từ localStorage
        const cartItems = JSON.parse(sessionStorage.getItem('giohang')) || [];
        const totalPriceInfo = JSON.parse(localStorage.getItem('totalPriceInfo')) || {};
        const selectedInvoice = JSON.parse(localStorage.getItem('selectedInvoice')) || {};
        const selectedUser = JSON.parse(localStorage.getItem('selectedUser')) || {};
        const user = JSON.parse(localStorage.getItem('user')) || {};
        const selectedPaymentMethod = JSON.parse(localStorage.getItem('selectedPaymentMethod')) || {};
        const paymentInfo = JSON.parse(localStorage.getItem('paymentInfo')) || {};
        const discount = localStorage.getItem('discount') || 0;
        const voucherCode = localStorage.getItem('voucherCode') || 'N/A';
        let formattedDate = 'N/A'; // Mặc định là 'N/A'
        if (selectedInvoice.ngayTao) {
            const dateObj = new Date(selectedInvoice.ngayTao);
            formattedDate = dateObj.getDate().toString().padStart(2, '0') + '/' +
                (dateObj.getMonth() + 1).toString().padStart(2, '0') + '/' +
                dateObj.getFullYear();
        }
        // Tạo HTML hóa đơn từ dữ liệu
        const invoiceHtml = `
       <div style="font-family: 'Roboto', sans-serif; padding: 20px; color: #333; background-color: #fff; width: 100%; max-width: 800px; margin: 0 auto; border: 1px solid #ddd; border-radius: 15px; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);">
    <!-- Thông tin cửa hàng -->
    <div style="text-align: center; margin-bottom: 30px; color: #333;">
        <img src="/view-admin/assets/image/favicon1.png" alt="Logo" style="max-width: 150px; margin-bottom: 10px;">
        <h2 style="font-size: 32px; font-weight: bold; color: #1E3D58; margin-bottom: 5px;">NB Fashion</h2>
        <p style="font-size: 16px; margin: 5px 0; color: #555;">Địa Chỉ: 32 Cổ Nhuế 2 - Bắc Từ Liêm - Hà Nội</p>
        <p style="font-size: 16px; margin: 5px 0; color: #555;">Số Điện Thoại: 0989899999</p>
    </div>
    
    <hr style="border: 1px solid #1E3D58; margin-bottom: 30px;">
    
    <!-- Thông tin khách hàng và nhân viên -->
    <div style="display: flex; justify-content: space-between; font-size: 16px; color: #444; margin-bottom: 20px;">
    <!-- Thông tin khách hàng và nhân viên bên trái -->
    <div style="width: 48%; padding-right: 20px;">
        <p><strong style="color: #1E3D58;">Tên Nhân Viên:</strong> ${user.tenNguoiDung || 'N/A'}</p>
        <p><strong style="color: #1E3D58;">Tên Khách Hàng:</strong> ${selectedUser.tenNguoiDung || 'N/A'}</p>
        <p><strong style="color: #1E3D58;">Số Điện Thoại:</strong> ${selectedUser.sdt || 'N/A'}</p>
        <p><strong style="color: #1E3D58;">Mã Voucher:</strong> ${voucherCode}</p>
    </div>
    
    <!-- Thông tin hóa đơn bên phải -->
    <div style="width: 48%; padding-left: 20px;">
        <p><strong style="color: #1E3D58;">Mã Hóa Đơn:</strong> ${selectedInvoice.maHoaDon || 'N/A'}</p>
        <p><strong style="color: #1E3D58;">Ngày Tạo:</strong> ${formattedDate}</p>
        <p><strong style="color: #1E3D58;">Hình thức thanh toán:</strong> ${selectedPaymentMethod.tenPhuongThuc || 'Chưa rõ'}</p>
       
    </div>
</div>


    <!-- Chi tiết sản phẩm -->
    <h3 style="font-size: 20px; font-weight: bold; color: #1E3D58; text-align: center; margin-top: 30px;">Chi Tiết Sản Phẩm</h3>
    <table style="width: 100%; border-collapse: collapse; margin-top: 20px; background-color: #f9f9f9; border-radius: 8px; overflow: hidden;">
        <thead style="background-color: #1E3D58; color: #fff;">
            <tr>
                <th style="padding: 12px; text-align: center; font-size: 14px;">#</th>
                <th style="padding: 12px; text-align: left; font-size: 14px;">Sản Phẩm</th>
                <th style="padding: 12px; text-align: left; font-size: 14px;">Chất Liệu</th>
                <th style="padding: 12px; text-align: left; font-size: 14px;">Màu Sắc</th>
<th style="padding: 12px; text-align: left; font-size: 14px;">Kích Thước</th>
                <th style="padding: 12px; text-align: center; font-size: 14px;">Số Lượng</th>
                <th style="padding: 12px; text-align: right; font-size: 14px;">Đơn Giá</th>
                <th style="padding: 12px; text-align: right; font-size: 14px;">Tổng Tiền</th>
            </tr>
        </thead>
        <tbody>
            ${cartItems.map((item, index) => `
                <tr style="border-bottom: 1px solid #ddd; text-align: center;">
                    <td style="padding: 10px; font-size: 14px;">${index + 1}</td>
                    <td style="padding: 10px; font-size: 14px;">${item.tenSanPham || 'N/A'}</td>
                    <td style="padding: 10px; font-size: 14px;">${item.chatLieu || 'N/A'}</td>
                    <td style="padding: 10px; font-size: 14px;">${item.mauSac || 'N/A'}</td>
                    <td style="padding: 10px; font-size: 14px;">${item.kichThuoc || 'N/A'}</td>
                    <td style="padding: 10px; text-align: center; font-size: 14px;">${item.soLuong || 0}</td>
                    <td style="padding: 10px; text-align: right; font-size: 14px;">${$scope.formatCurrency(item.giaBan)}</td>
                    <td style="padding: 10px; text-align: right; font-size: 14px;">${$scope.formatCurrency(item.soLuong * item.giaBan)}</td>
                </tr>
            `).join('')}
        </tbody>
    </table>

    <!-- Tổng tiền và các khoản phí -->
    <div style="text-align: right; font-weight: bold; margin-top: 30px; font-size: 18px; color: #1E3D58;">
        <p><strong>Giảm Giá:</strong> ${$scope.formatCurrency(discount)}</p>
        <p><strong>Tổng Cộng:</strong> ${$scope.formatCurrency(totalPriceInfo.totalPrice || 0)}</p>
    </div>

    <!-- Lời cảm ơn -->
    <div style="text-align: center; margin-top: 40px; font-size: 16px; font-style: italic; color: #333;">
        <p>Cảm ơn bạn đã mua sắm tại NB Fashion. Chúc bạn có một ngày tuyệt vời!</p>
    </div>
</div>

    `;

        // Tạo iframe tạm thời để in
        const iframe = document.createElement('iframe');
        iframe.style.position = 'absolute';
        iframe.style.top = '-10000px';
        document.body.appendChild(iframe);

        const iframeDoc = iframe.contentDocument || iframe.contentWindow.document;

        // Ghi nội dung hóa đơn vào iframe
        iframeDoc.open();
        iframeDoc.write(`
            <html>
                <head>
                    <title>In Hóa Đơn</title>
                    <style>
                        @page { 
                            size: A3;
                            margin: 15mm;
                        }
                        body { 
                            margin: 0;
                            font-family: 'Roboto', sans-serif;
                        }
                    </style>
                </head>
                <body>
                    ${invoiceHtml}
                </body>
            </html>
        `);
        iframeDoc.close();

        // Gọi lệnh in từ iframe
        iframe.contentWindow.focus();
        iframe.contentWindow.print();

        // Thông báo in hóa đơn thành công với Swal.fire
        Swal.fire({
            icon: 'success',
            title: 'Hóa đơn đã được in thành công!',
            text: 'Cảm ơn bạn đã mua sắm tại NB Fashion.',
            confirmButtonText: 'Đóng'
        });
        localStorage.removeItem('voucherCode');
        // Xóa giỏ hàng trong sessionStorage sau khi in
        sessionStorage.removeItem('giohang');

        // Xóa iframe sau khi in
        document.body.removeChild(iframe);
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
    $scope.selectedInvoice = { maHoaDon: 0 };
    $scope.customerPaid = null;
    $scope.getProductDetails();
    $scope.getUnpaidInvoices();
    $scope.getPhuongThucThanhToan();
    $scope.getVoucher();
    $scope.dsvoucher();

}