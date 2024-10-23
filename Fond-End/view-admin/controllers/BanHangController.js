window.BanHangController = function ($scope, $http) {
    $scope.invoiceToDelete = null;
    $scope.currentPage = 1;
    $scope.pageChanged = function (newPage) {
        $scope.currentPage = newPage;
    };

    $scope.getUsers = function () {
        console.log("Người dùng đã chọn:", $scope.selectedUser);
        $http.get('http://localhost:8080/api/nguoi-dung')
            .then(function (response) {
                console.log('Dữ liệu người dùng:', response.data);
                $scope.users = response.data;
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy dữ liệu người dùng:', error);
            });
    };
    $scope.nvs = [];
    $scope.getNV = function () {
        console.log("Người dùng đã chọn:", $scope.selectedUser);
        $http.get('http://localhost:8080/api/nguoi-dung/ban-hang')
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
        sdtNguoiDung: ''
    };
    $scope.openAddCustomerModal = function () {
        $scope.isAddCustomerModalOpen = true;
    };
    $scope.closeAddCustomerModal = function () {
        $scope.isAddCustomerModalOpen = false;
        $scope.newCustomer.tenNguoiDung = '';
        $scope.newCustomer.sdtNguoiDung = '';
    };
    $scope.addCustomer = function () {
        if ($scope.newCustomer.tenNguoiDung && $scope.newCustomer.sdtNguoiDung) {
            let customerData = {
                tenNguoiDung: $scope.newCustomer.tenNguoiDung,
                sdtNguoiDung: $scope.newCustomer.sdtNguoiDung,
            };

            $http.post('http://localhost:8080/api/nguoi-dung/add', customerData)
                .then(function (response) {
                    console.log('Khách hàng đã được thêm:', response.data);
                    $scope.getUsers();
                    $scope.closeAddCustomerModal();
                    alert("Khách hàng đã được thêm thành công!");
                })
                .catch(function (error) {
                    console.error('Đã xảy ra lỗi:', error);
                    alert("Đã xảy ra lỗi khi thêm khách hàng!");
                });
        } else {
            alert("Vui lòng nhập đầy đủ thông tin khách hàng!");
        }
    };
    $scope.getUnpaidInvoices = function () {
        $http.get('http://localhost:8080/api/hoa-don/chua-thanh-toan')
            .then(function (response) {
                console.log('Danh sách hóa đơn chưa thanh toán:', response.data);
                $scope.unpaidInvoices = response.data;
                if ($scope.unpaidInvoices.length > 5) {
                    $scope.unpaidInvoices = $scope.unpaidInvoices.slice(0, 5);
                    alert("Bạn chỉ được phép tạo tối đa 5 hóa đơn chưa thanh toán!");
                }
            })

    };
    $scope.createInvoice = function () {
        console.log("Selected User:", $scope.selectedUser);

        let newInvoice = {
            tenNguoiNhan: $scope.selectedUser.tenNguoiDung,
            nguoiDung: { id: $scope.selectedUser.id },
            nhanVien: { id: $scope.nvs.id },
            ngayTao: new Date()
        };

        $http.post('http://localhost:8080/api/hoa-don/create', newInvoice)
            .then(function (response) {
                console.log('Hóa đơn được tạo:', response.data);
                $scope.getUnpaidInvoices();
                alert("Hóa đơn đã được tạo thành công!");
            })
            .catch(function (error) {
                console.error('Lỗi khi tạo hóa đơn:', error);
                alert("Có lỗi xảy ra khi tạo hóa đơn!");
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
            $http.delete('http://localhost:8080/api/hoa-don/delete/' + invoiceId)
                .then(function (response) {
                    console.log('Hóa đơn đã được xóa:', response.data);
                    $scope.getUnpaidInvoices();
                    alert("Hóa đơn đã được xóa thành công!");
                })
                .catch(function (error) {
                    console.error('Lỗi khi xóa hóa đơn:', error);
                    alert("Có lỗi xảy ra khi xóa hóa đơn!");
                });
        }
    };

    $scope.getProductDetails = function () {
        $http.get('http://localhost:8080/san-pham-chi-tiet/all')
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
        $http.get('http://localhost:8080/api/hoa-don/' + invoice.idHoaDon)
            .then(function (response) {
                console.log('Chi tiết hóa đơn:', response.data);
                $scope.selectedInvoice = response.data;
                $scope.selectedInvoice.maHoaDon = response.data.maHoaDon;
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy chi tiết hóa đơn:', error);
            });
    };
    $scope.calculateTotalPrice = function () {
        $scope.totalPrice = 0;
        $scope.cartItems.forEach(function (item) {
            $scope.totalPrice += item.soLuong * item.giaBan;
        });
        console.log("Tổng tiền hàng:", $scope.totalPrice);
    };
    $scope.calculateChange = function () {
        if ($scope.tienKhachDua !== undefined && !isNaN($scope.tienKhachDua)) {
            $scope.tienThua = Math.max($scope.tienKhachDua - $scope.totalPrice, 0);
        } else {
            $scope.tienThua = 0;
        }
        console.log("Tiền thừa:", $scope.tienThua);
    };
    // add giỏ hàng theo id người dùng
    $scope.addToCart = function (idSanPhamChiTiet) {
        let cartItem = {
            idSanPhamChiTiet: idSanPhamChiTiet,
            soLuong: 1
        };
        $http.post('http://localhost:8080/api/gio-hang/them/' + $scope.selectedUser.id, cartItem)
            .then(function (response) {
                console.log('người dùng đã thêm giỏ hàng: ', $scope.selectedUser.id)
                alert("Sản phẩm đã được thêm vào giỏ hàng!");
                $scope.getCartItems();
                $scope.getProductDetails();
            })

    };
    // lấy danh sách sp theo id người dùng đã thêm vào giỏ hàng
    $scope.getCartItems = function () {
        if (!$scope.selectedUser || !$scope.selectedUser.id) {
            alert("Vui lòng chọn người dùng để xem giỏ hàng.");
            return;
        }
        $http.get('http://localhost:8080/api/gio-hang/lay/' + $scope.selectedUser.id)
            .then(function (response) {
                console.log('Danh sách sản phẩm trong giỏ hàng:', response.data);
                $scope.cartItems = response.data;
                $scope.calculateTotalPrice();
                // $scope.getProductDetails() // sau khi lấy thêm giỏ hàng thì load lại spct nhé kkk
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy giỏ hàng:', error);
                alert("Có lỗi xảy ra khi lấy giỏ hàng!");
            });
    };
    $scope.deleteCartItem = function (productId) {
        const userId = $scope.selectedUser.id;
        $http.delete('http://localhost:8080/api/gio-hang/xoa-san-pham/' + userId + '/' + productId)
            .then(function (response) {
                console.log('Sản phẩm đã được xóa khỏi giỏ hàng:', response.data);
                alert("Sản phẩm đã được xóa thành công khỏi giỏ hàng!");
                $scope.getCartItems(); // Load lại giỏ hàng
                $scope.getProductDetails(); // Load lại sản phẩm
            })
            .catch(function (error) {
                console.error('Lỗi khi xóa sản phẩm khỏi giỏ hàng:', error);
                alert("Có lỗi xảy ra khi xóa sản phẩm khỏi giỏ hàng!");
            });
    };

    $scope.updateQuantity = function (item) {
        const userId = $scope.selectedUser.id;
        const updatedQuantity = { soLuong: item.soLuong };

        $http.put('http://localhost:8080/api/gio-hang/cap-nhat/' + userId + '/' + item.idSanPhamChiTiet, updatedQuantity)
            .then(function (response) {
                console.log('Cập nhật số lượng thành công:', response.data);
                $scope.calculateTotalPrice();
                $scope.getProductDetails(); // Load lại sản phẩm
            })
            .catch(function (error) {
                console.error('Lỗi khi cập nhật số lượng:', error);
                alert("Có lỗi xảy ra khi cập nhật số lượng!");
            });
    };


    $scope.getPhuongThucThanhToan = function () {
        $http.get('http://localhost:8080/api/phuong-thuc-thanh-toan/ten-phuong-thuc')
            .then(function (response) {
                console.log('Dữ liệu phương thức thanh toán:', response.data);
                $scope.phuongThucThanhToan = response.data;
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy dữ liệu phương thức thanh toán:', error);
            });
    };

    $scope.createHoaDonChiTiet = function () {
        $scope.getCartItems();
        $scope.hoaDonChiTietDTO = [];
        $scope.cartItems.forEach(function (item) {
            const detail = {
                idHoaDon: $scope.selectedInvoice.idHoaDon,
                idSanPhamChiTiet: item.idSanPhamChiTiet,
                soLuong: item.soLuong,
                tongTien: item.giaBan * item.soLuong,
                soTienThanhToan: parseFloat($scope.tienKhachDua),
                tienTraLai: $scope.tienThua,
                moTa: item.moTa || ''
            };
            $scope.hoaDonChiTietDTO.push(detail);
        });
        $http.post("http://localhost:8080/api/hoa-don-chi-tiet/create", $scope.hoaDonChiTietDTO,{
            params: { userId: $scope.selectedUser.id }
        })
            .then(function (response) {
                alert("Thanh toán thành công");
                console.log("Hóa đơn chi tiết:", response.data);
                $scope.deleteGioHangChiTietByUserId();
                $scope.updateInvoiceStatus();
                $scope.createPaymentMethod();
            })
            .catch(function (error) {
                console.error("Lỗi khi tạo hóa đơn chi tiết:", error);
                alert("Có lỗi xảy ra. Vui lòng thử lại.");
            });
    };
    $scope.deleteGioHangChiTietByUserId = function () {
        const userId = $scope.selectedUser.id;
        $http.delete("http://localhost:8080/api/gio-hang/delete/user/" + userId)
            .then(function (response) {
                $scope.getCartItems(); 
            })
            .catch(function (error) {
                console.error("Lỗi khi xóa chi tiết giỏ hàng:", error);
            });
    };
    

    $scope.updateInvoiceStatus = function () {
        if (!$scope.selectedInvoice || !$scope.selectedInvoice.idHoaDon) {
            alert("Vui lòng chọn hóa đơn cần cập nhật!");
            return;
        }
        $http.put("http://localhost:8080/api/hoa-don/" + $scope.selectedInvoice.idHoaDon + "/update-status", { trangThai: true })
            .then(function (response) {
                alert("trạng thái hóa đơn thành công!");
                console.log("Kết quả cập nhật:", response.data);
            })
    };
        $scope.createPaymentMethod = function () {
            var paymentMethod = {
                phuongThucThanhToan: { id: $scope.selectedPhuongThucThanhToan },
                soTienThanhToan: Number($scope.customerPaid),
                ngayGiaoDich: new Date().toISOString(),
                ghiChu: "Thanh toán hóa đơn " + $scope.selectedInvoice.maHoaDon
            };
            $http.post("http://localhost:8080/api/hoa-don/" + $scope.selectedInvoice.idHoaDon + "/thanh-toan", paymentMethod)
                .then(function (response) {
                    console.log("Phản hồi từ POST:", response.data);
                    if (response.data && response.data.id) {
                        var updatedInvoice = {
                            phiShip: 0,
                            ngayThanhToan: new Date().toISOString(),
                            thanhTien: Number($scope.totalPrice),
                            idPtThanhToanHoaDon: response.data.id
                        };
                        console.log("Cấu trúc của updatedInvoice trước khi gửi:", updatedInvoice);
                        return $http.put("http://localhost:8080/api/hoa-don/cap-nhat/" + $scope.selectedInvoice.idHoaDon, updatedInvoice)
                            .then(function (putResponse) {
                                console.log("Hóa đơn đã được cập nhật thành công:", putResponse.data);
                                alert("Hóa đơn đã được cập nhật thành công!");
                            })
                            .catch(function (error) {
                                console.error("Có lỗi xảy ra khi cập nhật hóa đơn:", error);
                                alert("Có lỗi xảy ra khi cập nhật hóa đơn. Vui lòng thử lại.");
                            });
                    } else {
                        console.error("Không tìm thấy ID từ phản hồi thanh toán.");
                        alert("Có lỗi xảy ra khi tạo phương thức thanh toán. Vui lòng thử lại.");
                    }
                })

        };
    $scope.customerPaid = null;
    $scope.getProductDetails();
    $scope.getUsers();
    $scope.getNV();
    $scope.getUnpaidInvoices();
    $scope.deleteInvoice();
    $scope.getPhuongThucThanhToan();
}