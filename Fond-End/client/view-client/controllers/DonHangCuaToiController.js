window.DonHangCuaToiController = function ($scope, $http, $window) {



    // Lấy thông tin người dùng từ localStorage
    var userInfo = localStorage.getItem('user');
    // Lắng nghe sự kiện click trên nút "Tra Cứu"
    document.getElementById("searchOrder").addEventListener("click", searchOrder);

    // Lắng nghe sự kiện nhấn phím Enter trong ô nhập mã đơn hàng
    document.getElementById("orderCode").addEventListener("keydown", function (event) {
        if (event.key === "Enter") {
            searchOrder(); // Thực hiện tra cứu khi nhấn Enter
        }
    });

    function searchOrder() {
        const orderCodeInput = document.getElementById("orderCode");
        const orderCode = orderCodeInput.value.trim();
        const orderInfoDiv = document.getElementById("orderInfo");

        // Ẩn thông tin đơn hàng và trạng thái lỗi trước
        orderInfoDiv.style.display = "none";
        orderCodeInput.classList.remove("is-invalid");

        // Kiểm tra nếu mã đơn hàng không được nhập
        if (!orderCode) {
            showError("Vui lòng nhập mã đơn hàng!");
            return;
        }

        // Kiểm tra mã đơn hàng hợp lệ (chỉ cho phép chữ cái, số, và độ dài từ 5-20 ký tự)
        if (!/^[A-Za-z0-9]{5,20}$/.test(orderCode)) {
            showError("Mã đơn hàng không hợp lệ! Vui lòng nhập đúng định dạng.");
            return;
        }

        // Hiển thị trạng thái tải
        showLoadingState(true);

        // Gửi yêu cầu API để tra cứu đơn hàng
        fetch(`http://127.0.0.1:8080/api/nguoi_dung/hoa_don/${orderCode}`)
            .then(response => {
                if (!response.ok) {
                    if (response.status === 404) {
                        throw new Error("Không tìm thấy đơn hàng với mã này.");
                    } else {
                        throw new Error("Có lỗi xảy ra khi truy vấn API. Vui lòng thử lại.");
                    }
                }
                return response.json();
            })
            .then(data => {
                if (!data || !data.hoaDon || data.hoaDon.length === 0) {
                    throw new Error("Không tìm thấy thông tin đơn hàng!");
                }

                const hoaDon = data.hoaDon[0];

                // Hiển thị thông tin đơn hàng
                document.getElementById("infoOrderCode").textContent = hoaDon.maHoaDon || "N/A";
                document.getElementById("infoCustomerName").textContent = hoaDon.tenNguoiNhan || "N/A";
                const orderDate = hoaDon.ngayTao ? new Date(hoaDon.ngayTao).toLocaleDateString("vi-VN") : "N/A";
                document.getElementById("infoOrderDate").textContent = orderDate;
                const orderStatus = hoaDon.tenTrangThai || "Chưa có trạng thái";
                document.getElementById("infoOrderStatus").textContent = orderStatus;

                // Hiển thị thông tin đơn hàng nếu có
                orderInfoDiv.style.display = "block";
            })
            .catch(error => {
                showError(error.message);
                console.error("Lỗi:", error);
            })
            .finally(() => {
                // Ẩn trạng thái tải
                showLoadingState(false);
            });
    }

    // Hàm hiển thị trạng thái lỗi
    function showError(message) {
        Swal.fire({
            title: 'Lỗi',
            text: message,
            icon: 'error',
            confirmButtonText: 'OK'
        });
    }

    // Hàm hiển thị trạng thái tải
    function showLoadingState(isLoading) {
        const searchButton = document.getElementById("searchOrder");
        if (isLoading) {
            searchButton.disabled = true;
            searchButton.textContent = "Đang tra cứu...";
        } else {
            searchButton.disabled = false;
            searchButton.textContent = "Tra Cứu";
        }
    }

    // Thêm sự kiện click cho nút tra cứu
    document.getElementById("searchOrder").addEventListener("click", searchOrder);


    function getOrderDetailsByMaHoaDon(maHoaDon) {
        const apiUrl = `http://localhost:8080/api/nguoi_dung/hoa_don/${maHoaDon}`;

        // Gọi API để lấy chi tiết hóa đơn
        $http.get(apiUrl)
            .then(function (response) {
                const hoaDon = response.data.hoaDon && Array.isArray(response.data.hoaDon) && response.data.hoaDon.length > 0
                    ? response.data.hoaDon[0]
                    : null;

                if (hoaDon) {
                    // Cập nhật thông tin đơn hàng
                    $scope.orderHieu = {
                        maHoaDon: hoaDon.maHoaDon,
                        idHoaDon: hoaDon.idHoaDon, // Lưu idHoaDon
                        idNguoiDung: hoaDon.idNguoiDung, // Lưu idNguoiDung
                        idLoaiTrangThai: hoaDon.idLoaiTrangThai, // Lưu idNguoiDung
                        tenPhuongThucThanhToan: hoaDon.tenPhuongThucThanhToan,
                        tenNguoiNhan: hoaDon.tenNguoiNhan,
                        diaChi: hoaDon.diaChi,
                        sdtNguoiNhan: hoaDon.sdtNguoiNhan,
                        ghiChu: hoaDon.ghiChu,
                        ngayTao: hoaDon.ngayTao,
                        thanhTien: hoaDon.thanhTien,
                        tinh: hoaDon.tenTinh,
                        huyen: hoaDon.tenHuyen,
                        xa: hoaDon.tenXa,
                        giaTriMavoucher: hoaDon.giaTriMavoucher, // Đảm bảo mã voucher được lấy chính xác
                        kieuGiamGia: hoaDon.kieuGiamGia,
                    };

                    // Cập nhật chi tiết sản phẩm
                    if (hoaDon.listSanPhamChiTiet && hoaDon.listSanPhamChiTiet.length > 0) {
                        $scope.orderlist = {
                            listSanPhamChiTiet: hoaDon.listSanPhamChiTiet,
                            thanhTien: hoaDon.thanhTien,
                            phiShip: hoaDon.phiShip,
                            giaTien: hoaDon.giaTien,
                            maSPCT: hoaDon.maSPCT,
                            tenSanPham: hoaDon.tenSanPham,
                            tenmausac: hoaDon.tenmausac, // Đảm bảo trường này tồn tại
                            tenchatlieu: hoaDon.tenchatlieu, // Đảm bảo trường này tồn tại
                            tenkichthuoc: hoaDon.tenkichthuoc, // Đảm bảo trường này tồn tại
                            tongtien: hoaDon.tongtien,
                            giaKhuyenMai: hoaDon.giaKhuyenMai
                        };

                        // Lấy hình ảnh cho từng sản phẩm
                        $scope.orderlist.listSanPhamChiTiet.forEach((item) => {
                            $http.get(`http://localhost:8080/api/nguoi_dung/hinh_anh/${item.idSanPham}`)
                                .then(function (imageResponse) {
                                    // Gán hình ảnh cho sản phẩm nếu có
                                    item.urlAnh = imageResponse.data[0]?.urlAnh || '';
                                })
                                .catch(function (error) {
                                    console.error("Lỗi khi lấy hình ảnh sản phẩm:", error);
                                });
                        });
                    }
                } else {
                    console.error("Dữ liệu hóa đơn không hợp lệ:", response.data);
                }
            })
            .catch(function (error) {
                console.error("Lỗi khi lấy thông tin hóa đơn:", error);
            });
    }
    $scope.submitReturnRequest = function () {
        if ($scope.orderHieu.idLoaiTrangThai === 7) {
            showAlert('error', 'Lỗi!', 'Không thể yêu cầu hoàn trả vì đơn hàng của bạn đã bị hủy!');
            return;
        }
    
        if (!$scope.orderlist?.listSanPhamChiTiet?.length) {
            showAlert('warning', 'Thông báo!', 'Không có sản phẩm nào trong đơn hàng để thực hiện hoàn trả!');
            return;
        }
    
        const filteredItems = $scope.orderlist.listSanPhamChiTiet.filter(
            item => item.selected && item.soLuongHoanTra > 0
        );
    
        if (!filteredItems.length) {
            showAlert('warning', 'Thông báo!', 'Vui lòng chọn ít nhất một sản phẩm và nhập số lượng hoàn trả hợp lệ!');
            return;
        }
    
        // Kiểm tra lý do từ select hoặc textarea
        let reasonToSend = $scope.selectedReason || $scope.otherReason;  
        if (!reasonToSend) {
            showAlert('warning', 'Thông báo!', 'Vui lòng chọn lý do đổi trả hoặc nhập lý do!');
            return;
        }
    
        const doiTraDTOList = filteredItems.map(item => ({
            idHoaDon: $scope.orderHieu.idHoaDon,
            idSanPhamChiTiet: item.idspct,
            soLuong: item.soLuongHoanTra,
            lyDo: reasonToSend,  // Gửi lý do đã chọn hoặc nhập
            trangThai: true,
            tongTien: item.soLuongHoanTra * item.tienSanPham
        }));
    
        $http.post("http://localhost:8080/api/nguoi_dung/doi-tra/tao-doi-tra", doiTraDTOList)
            .then(() => {
                showAlert('success', 'Thành công!', 'Yêu cầu hoàn trả của bạn đã được xử lý thành công!');
                resetForm();
            })
            .catch(error => {
                const errorMessage = error?.data?.message || 'Không thể xử lý yêu cầu hoàn trả do lỗi hệ thống. Vui lòng thử lại sau!';
                showAlert('error', 'Lỗi!', errorMessage);
            });
    };
    
    // Xóa lý do khác khi người dùng thay đổi lựa chọn lý do (giữ lại textarea để nhập tự do)
    $scope.resetOtherReason = function () {
        if ($scope.selectedReason !== 'Lý do khác') {
            $scope.otherReason = '';
        }
    };
    function resetForm() {
        $scope.orderlist.listSanPhamChiTiet.forEach(item => {
            item.selected = false;
            item.soLuongHoanTra = 0;
        });
        $scope.selectedReason = '';
        $scope.otherReason = ''; 
    }
    
    function showAlert(icon, title, text) {
        Swal.fire({
            icon: icon,
            title: title,
            text: text,
            confirmButtonText: 'Đồng ý'
        });
    }
    

    $scope.getTotalProductPrice = function () {
        let total = 0;

        // Kiểm tra xem $scope.orderDetails và listSanPhamChiTiet có tồn tại không
        if ($scope.orderlist && Array.isArray($scope.orderlist.listSanPhamChiTiet)) {
            angular.forEach($scope.orderlist.listSanPhamChiTiet, function (item) {
                total += item.tongtien || 0; // Thêm giá của mỗi sản phẩm vào tổng
            });
        } else {
            console.log("orderDetails or listSanPhamChiTiet is undefined or not an array.");
        }

        return total;
    };
// Sửa Phần NàyNày
    $scope.calculateDiscount = function () {
        let discount = 0;
        const totalPrice = $scope.getTotalProductPrice();
    
        if ($scope.orderHieu) {
            if ($scope.orderHieu.kieuGiamGia === false) {
                // Giảm theo phần trăm
                discount = (totalPrice * $scope.orderHieu.giaTriMavoucher) / 100;
            } else if ($scope.orderHieu.kieuGiamGia === true) {
                // Giảm theo số tiền
                discount = $scope.orderHieu.giaTriMavoucher;
            }
        }
    
        return Math.min(discount, totalPrice); // Không vượt quá tổng tiền
    };
    
    $scope.calculateTotalAfterDiscount = function () {
        const totalPrice = $scope.getTotalProductPrice();
        const discount = $scope.calculateDiscount();
        return Math.max(totalPrice - discount, 0); // Không bao giờ âm
    };

    // Sửa Phần NàyNày
    
    // Gọi API để lấy danh sách đơn hàng khi tải trang
    $scope.getOrderDetails = function () {
        const apiUrl = `http://localhost:8080/api/nguoi_dung/hoa_don/user/${$scope.idNguoiDung}`;

        $http.get(apiUrl)
            .then(function (response) {
                const hoaDonList = response.data && Array.isArray(response.data.hoaDon)
                    ? response.data.hoaDon
                    : [];

                if (hoaDonList.length > 0) {
                    $scope.orderData = hoaDonList;
                    console.log($scope.orderData);
                } else {
                    console.warn("Không tìm thấy đơn hàng nào.");
                    $scope.orderData = [];
                }
            })
            .catch(function (error) {
                console.error("Lỗi khi lấy thông tin hóa đơn:", error);
                $scope.orderData = [];
            });
    };

    // Gọi hàm getOrderDetails khi tải trang
    $scope.getOrderDetails();

    $scope.cancelOrder = function () {
        // Kiểm tra nếu $scope.orderHieu có giá trị hợp lệ
        if (!$scope.orderHieu) {
            Swal.fire({
                icon: 'error',
                title: 'Không có thông tin đơn hàng để hủy!',
                text: 'Vui lòng kiểm tra lại thông tin đơn hàng.',
            });
            return;
        }

        // Kiểm tra lại giá trị của idHoaDon và idNguoiDung
        var idHoaDon = $scope.orderHieu.idHoaDon;  // Lấy idHoaDon từ orderHieu
        var idLoaiTrangThai = 7;  // ID trạng thái "Đơn hàng bị hủy bỏ"
        var idNhanVien = $scope.orderHieu.idNguoiDung;  // Lấy idNhanVien từ orderHieu

        // Log dữ liệu để kiểm tra
        console.log('Cancel Order Details:');
        console.log('idHoaDon:', idHoaDon);  // Kiểm tra giá trị của idHoaDon
        console.log('idLoaiTrangThai:', idLoaiTrangThai);
        console.log('idNhanVien:', idNhanVien);  // Kiểm tra giá trị của idNhanVien

        // Kiểm tra nếu idHoaDon và idNhanVien có giá trị hợp lệ
        if (!idHoaDon || !idNhanVien) {
            Swal.fire({
                icon: 'error',
                title: 'Thông tin không hợp lệ!',
                text: 'Thông tin hóa đơn hoặc nhân viên không hợp lệ!',
            });
            return;
        }

        // Gọi API để cập nhật trạng thái đơn hàng bằng phương thức POST và tham số truyền qua query string
        $http.post('http://localhost:8080/api/admin/hoa_don/updateLoaiTrangThai', null, {
            params: {
                idHoaDon: idHoaDon,
                idLoaiTrangThai: idLoaiTrangThai,
                idNhanVien: idNhanVien
            }
        })
            .then(function (response) {
                console.log('API Response:', response);
                if (response.data.success) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Đơn hàng đã hủy bỏ thành công!',
                        text: 'Đơn hàng đã được cập nhật trạng thái thành công.',
                    });

                    // Cập nhật trạng thái của đơn hàng
                    $scope.orderHieu.tenLoaiTrangThai = "Đơn hàng bị hủy bỏ";

                    // Đóng modal sau khi hủy thành công
                    $('#orderDetailsModal').modal('hide');  // Đóng modal bằng jQuery
                    $scope.getOrderDetails(); // Load lại bảng
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Có lỗi xảy ra!',
                        text: 'Thông báo lỗi: ' + response.data.message,
                    });
                }
            })
            .catch(function (error) {
                console.error('Error occurred while canceling the order:', error);
                Swal.fire({
                    icon: 'error',
                    title: 'Có lỗi xảy ra!',
                    text: 'Đã xảy ra lỗi khi hủy đơn hàng. Vui lòng thử lại sau.',
                });
            });
    };





    // Ví dụ cách gọi hàm getOrderDetailsByMaHoaDon trong một sự kiện (ví dụ: khi người dùng nhấn nút chi tiết)
    $scope.showOrderDetails = function (hoaDon) {
        if (hoaDon && hoaDon.maHoaDon) {
            // Lưu thông tin hóa đơn vào $scope.orderHieu
            $scope.orderHieu = hoaDon;
            // Log thông tin của hóa đơn để kiểm tra
            console.log("Đơn hàng đã chọn:", $scope.orderHieu);
            // Kiểm tra giá trị của idLoaiTrangThai
            console.log("Checking idLoaiTrangThai:", $scope.orderHieu.idLoaiTrangThai);
            console.log("Checking idLoaiTrangThai in showOrderDetails:", $scope.orderHieu.idLoaiTrangThai);
            // Gọi API để lấy chi tiết đơn hàng nếu cần (nếu cần gọi API chi tiết)
            getOrderDetailsByMaHoaDon(hoaDon.maHoaDon);
        } else {
            console.error("Mã hóa đơn không hợp lệ!");
        }
    };
    $scope.toggleSelectAll = function () {
        // Nếu selectAll được chọn, tất cả sản phẩm sẽ được chọn
        angular.forEach($scope.orderlist.listSanPhamChiTiet, function (item) {
            item.selected = $scope.selectAll;
        });
    };
    // Function to update quantity input status based on checkbox selection
    $scope.updateQuantityStatus = function (item) {
        // Kiểm tra trạng thái checkbox của sản phẩm
        if (!item.selected) {
            item.soLuongHoanTra = ''; // Nếu bỏ chọn thì reset số lượng hoàn trả
        }
    };


}

    