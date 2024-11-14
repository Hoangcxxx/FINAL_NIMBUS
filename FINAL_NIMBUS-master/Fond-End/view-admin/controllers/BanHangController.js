    window.BanHangController = function ($scope, $http,$window, $location) {
        $scope.selectedPhuongThucThanhToan = 1; 
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
                $scope.filteredUsers = $scope.users; // Khởi tạo danh sách đã lọc
                $scope.searchTerm = "";
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
                return user.tenNguoiDung.toLowerCase().includes($scope.searchTerm.toLowerCase()) ||
                    user.sdtNguoiDung.includes($scope.searchTerm);
            });
            $scope.showDropdown = true; // Hiển thị dropdown khi có từ khóa
        }
    };

    $scope.selectUser = function (user) {
        $scope.searchTerm = user.tenNguoiDung; // Hiển thị tên người dùng vào ô tìm kiếm
        $scope.selectedUser = user;
        $scope.showDropdown = false; // Ẩn dropdown khi người dùng đã chọn
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
        if ($scope.unpaidInvoices && $scope.unpaidInvoices.length >= 5) {
            Swal.fire({
                title: 'Thông báo!',
                text: 'Bạn không thể tạo thêm hóa đơn vì đã có 5 hóa đơn chưa thanh toán!',
                icon: 'warning',
                confirmButtonText: 'OK'
            });
            return;
        }
    
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
    
                Swal.fire({
                    title: 'Thành công!',
                    text: 'Hóa đơn đã được tạo thành công!',
                    icon: 'success',
                    timer: 1500,  
                    showConfirmButton: false
                });
    
            })
            .catch(function (error) {
                console.error('Lỗi khi tạo hóa đơn:', error);
                // Hiển thị thông báo lỗi
                Swal.fire({
                    title: 'Lỗi!',
                    text: 'Có lỗi xảy ra khi tạo hóa đơn!',
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
            $http.delete('http://localhost:8080/api/hoa-don/delete/' + invoiceId)
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
                .catch(function (error) {
                    console.error('Lỗi khi xóa hóa đơn:', error);
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
                $scope.selectedUser = $scope.selectedInvoice.nguoiDung;
                $scope.searchTerm = $scope.selectedUser.tenNguoiDung;
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy chi tiết hóa đơn:', error);
            });
    };
    $scope.calculateChange = function () {
        // Kiểm tra và loại bỏ dấu phân cách hàng nghìn nếu có
        var tienKhachDua = $scope.tienKhachDua ? $scope.tienKhachDua.replace(/,/g, '') : '0'; // Loại bỏ dấu phẩy nếu có
        tienKhachDua = parseFloat(tienKhachDua);  // Chuyển đổi thành số
        
        var totalPrice = parseFloat($scope.totalPrice) || 0;  // Tổng tiền cần thanh toán
        
        // Kiểm tra xem tiền khách đưa có phải là số hợp lệ hay không
        if (isNaN(tienKhachDua) || tienKhachDua < 0) {
            $scope.tienKhachDua = 0;  // Nếu không phải số hợp lệ, gán giá trị là 0
            $scope.tienThua = 0;  // Cập nhật tiền thừa về 0
            alert("Vui lòng nhập một số tiền hợp lệ.");
            return;  // Dừng hàm nếu không có số tiền hợp lệ
        }
    
        // Kiểm tra giá trị tổng tiền cần thanh toán
        if (isNaN(totalPrice) || totalPrice < 0) {
            $scope.totalPrice = 0;  // Nếu tổng tiền không hợp lệ, gán về 0
            alert("Tổng tiền thanh toán không hợp lệ.");
            return;
        }
    
        // Tính tiền thừa
        $scope.tienThua = tienKhachDua - totalPrice;
    
        // Nếu tiền khách đưa ít hơn tổng tiền thì tiền thừa = 0
        if ($scope.tienThua < 0) {
            $scope.tienThua = 0;
        }
    
        // Log kết quả tiền thừa để kiểm tra
        console.log("Tiền khách đưa:", tienKhachDua);
        console.log("Tổng tiền thanh toán:", totalPrice);
        console.log("Tiền thừa:", $scope.tienThua);
    };
    
    
    
    

    // add giỏ hàng theo id người dùng
    $scope.addToCart = function (idSanPhamChiTiet) {
        let cartItem = { idSanPhamChiTiet: idSanPhamChiTiet, soLuong: 1 };
        $http.post('http://localhost:8080/api/gio-hang/them/' + $scope.selectedUser.id, cartItem)
            .then(function (response) {
                console.log('Sản phẩm đã được thêm vào giỏ hàng:', response.data);
                Swal.fire({
                    title: 'Thành công!',
                    text: 'Thêm giỏ hàng thành công!',
                    icon: 'success',
                    timer: 1500,  // Đóng sau 2 giây
                    showConfirmButton: false
                });
                return $scope.getCartItems();
            })
            .then(function () {
                $scope.calculateTotalPrice(); // Calculate total price
                $scope.getVoucher(); // Load relevant vouchers
            })
            .catch(function (error) {
                console.error('Lỗi khi thêm sản phẩm vào giỏ hàng:', error);
            });
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
                $scope.isCartEmpty = $scope.cartItems.length === 0;
                $scope.calculateTotalPrice();
                $scope.getProductDetails() // sau khi lấy thêm giỏ hàng thì load lại spct nhé kkk
                $scope.getVoucher();

            })
            .catch(function (error) {
                console.error('Lỗi khi lấy giỏ hàng:', error);
                alert("Có lỗi xảy ra khi lấy giỏ hàng!");
            });
    };


    $scope.calculateTotalPrice = function () {
        $scope.totalPrice = 0;

        // Tính tổng tiền hàng từ giỏ
        if ($scope.cartItems && Array.isArray($scope.cartItems)) {
            $scope.cartItems.forEach(function (item) {
                if (item && item.soLuong && item.giaBan) {
                    $scope.totalPrice += item.soLuong * item.giaBan;
                }
            });
        } else {
            console.error('Giỏ hàng không hợp lệ:', $scope.cartItems);
        }
        // Đảm bảo tổng tiền không âm
        $scope.totalPrice = Math.max($scope.totalPrice, 0);
        $scope.getVoucher();

    };
    $scope.getVoucher = function () {
        $http.get('http://localhost:8080/api/voucher/tuong_ung/' + $scope.totalPrice)
            .then(function (response) {
                if (response.data) {
                    const voucher = response.data;

                    // Kiểm tra điều kiện cho voucher
                    if ($scope.totalPrice < voucher.soTienToiThieu || $scope.totalPrice > voucher.giaTriToiDa) {
                        $scope.selectedVoucher = null; // Không áp dụng voucher
                    } else {
                        $scope.selectedVoucher = voucher; // Áp dụng voucher
                        $scope.calculateTotalPriceAfterDiscount(); // Tính toán lại tổng tiền sau giảm giá
                    }
                } else {
                    alert("Không tìm thấy voucher phù hợp.");
                    $scope.selectedVoucher = null; // Không có voucher
                }
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy voucher:', error);
                $scope.selectedVoucher = null; // Không có voucher
            });
    };

    $scope.calculateTotalPriceAfterDiscount = function () {
        if ($scope.selectedVoucher) {
            let discount = $scope.selectedVoucher.gia_tri_giam_gia; // % giảm giá
            let minAmount = $scope.selectedVoucher.soTienToiThieu; // Số tiền tối thiểu để áp dụng voucher

            if ($scope.totalPrice >= minAmount) {
                $scope.totalPrice -= ($scope.totalPrice * discount / 100);
                // Kiểm tra giá trị tối đa
                if ($scope.totalPrice > $scope.selectedVoucher.giaTriToiDa) {
                    $scope.totalPrice = $scope.selectedVoucher.giaTriToiDa;
                }
            }
        }
    };

    // Khi xóa sản phẩm khỏi giỏ hàng, bạn cũng có thể muốn tính toán lại tổng tiền
    $scope.deleteCartItem = function (productId) {
        const userId = $scope.selectedUser.id;
        $http.delete('http://localhost:8080/api/gio-hang/xoa-san-pham/' + userId + '/' + productId)
            .then(function (response) {
                console.log('Sản phẩm đã được xóa khỏi giỏ hàng:', response.data);
                $scope.getCartItems(); // Load lại giỏ hàng
                $scope.getProductDetails(); // Load lại sản phẩm
                $scope.calculateTotalPrice(); // Tính toán lại tổng tiền sau khi xóa sản phẩm
                $scope.getVoucher();
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
        const userId = $scope.selectedUser.id;
        $http.delete('http://localhost:8080/api/gio-hang/xoa-tat-ca/' + userId)
    };
    $scope.updateQuantity = function (item) {
        const userId = $scope.selectedUser.id;
        const updatedQuantity = { soLuong: item.soLuong };

        $http.put('http://localhost:8080/api/gio-hang/cap-nhat/' + userId + '/' + item.idSanPhamChiTiet, updatedQuantity)
            .then(function (response) {
                console.log('Cập nhật số lượng thành công:', response.data);
                $scope.calculateTotalPrice();
                $scope.getProductDetails(); // Load lại sản phẩm
                $scope.calculateTotalPrice(); // Tính toán lại tổng tiền sau khi xóa giỏ hàng
                $scope.getVoucher();
            })
        // .catch(function (error) {
        //     console.error('Lỗi khi cập nhật số lượng:', error);
        //     alert("Có lỗi xảy ra khi cập nhật số lượng!");
        // });
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

            const url = `http://localhost:8080/api/payment/creat_payment?amount=${amount}&paymentMethod=${paymentMethod}`;
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

        }
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
    
        $http.post("http://localhost:8080/api/hoa-don-chi-tiet/create", $scope.hoaDonChiTietDTO, {
            params: { userId: $scope.selectedUser.id }
        })
        .then(function (response) {
            console.log("Hóa đơn chi tiết:", response.data);
            $scope.deleteGioHangChiTietByUserId();
            $scope.updateInvoiceStatus();
            $scope.createPaymentMethod();
            $scope.deleteCart();
            $scope.TEST();
    
            // Hiển thị thông báo thành công
            Swal.fire({
                title: 'Thành công!',
                text: 'Thanh toán thành công!',
                icon: 'success',
                timer: 1500,  // Đóng sau 2 giây
                showConfirmButton: false
            }).then(function () {
                $scope.getUnpaidInvoices();
                $scope.getCartItems();
                $scope.description = "";
                $scope.selectedInvoice = { maHoaDon: 0 };
                $scope.selectednv = null;
                $scope.searchTerm = "";
            });
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
    $scope.printInvoice = function () {
        // Tạo một phần tử mới để in hóa đơn
        var printContents = document.getElementById('invoicePrintModal').innerHTML;

        // Tạo cửa sổ pop-up mới
        var popupWin = window.open('', '_blank', 'width=600,height=600');
        popupWin.document.open();

        // Ghi nội dung hóa đơn vào cửa sổ pop-up với CSS để định dạng
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
        $scope.closeModal = function() {
            console.log('Đóng modal');
            $scope.isGiaoHang = false;  
        };
        $scope.getProvinces = function () {
            $http.get('http://localhost:8080/api/ghn/provinces').then(function (response) {
                if (response.data && response.data.data) {
                    $scope.provinces = response.data.data;
                } else {
                    console.error('Không lấy được danh sách tỉnh từ API GHN');
                }
            }, function (error) {
                console.error('Lỗi khi gọi API GHN:', error);
            });
        };
    
        // Khi chọn tỉnh, lấy danh sách huyện
        $scope.onProvinceChange = function (provinceId) {
            $http.get('http://localhost:8080/api/ghn/districts/' + provinceId).then(function (response) {
                if (response.data && response.data.data) {
                    $scope.districts = response.data.data;
                } else {
                    console.error('Không lấy được danh sách huyện từ API GHN');
                }
            }, function (error) {
                console.error('Lỗi khi gọi API GHN:', error);
            });
        };
    
        // Khi chọn huyện, lấy danh sách xã
        $scope.onDistrictChange = function (districtId) {
            $http.get('http://localhost:8080/api/ghn/wards/' + districtId).then(function (response) {
                if (response.data && response.data.data) {
                    $scope.wards = response.data.data;
                } else {
                    console.error('Không lấy được danh sách xã từ API GHN');
                }
            }, function (error) {
                console.error('Lỗi khi gọi API GHN:', error);
            });
        };
    
    $scope.customerPaid = null;
    $scope.getProductDetails();
    $scope.getUsers();
    $scope.getNV();
    $scope.getUnpaidInvoices();
    $scope.deleteInvoice();
    $scope.getPhuongThucThanhToan();
    $scope.getVoucher();
    $scope.getProvinces();
}