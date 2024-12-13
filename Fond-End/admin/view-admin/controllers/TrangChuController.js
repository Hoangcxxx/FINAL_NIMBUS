window.TrangChuController = function ($scope, $http) {
    $scope.updateInvoiceStatus = function () {
        const selectedInvoice = JSON.parse(localStorage.getItem('selectedInvoice'));
        $http.put("http://localhost:8080/api/admin/hoa_don_ban_hang/" + selectedInvoice.idHoaDon + "/update-status", { trangThai: true })
            .then(function (response) {
                console.log("Kết quả cập nhật:", response.data);
            })
    };
    $scope.createPaymentHistory = function (amount) {
        // Lấy thông tin từ localStorage
        const selectedInvoice = JSON.parse(localStorage.getItem('selectedInvoice'));
        const selectedUser = JSON.parse(localStorage.getItem('selectedUser'));
        const totalPriceInfo = JSON.parse(localStorage.getItem('totalPriceInfo'));
        // Kiểm tra tính hợp lệ
        // Dữ liệu gửi lên server
        const paymentData = {
            idHoaDon: selectedInvoice.idHoaDon,
            idNguoiDung: selectedUser.idNguoiDung,
            soTienThanhToan: totalPriceInfo.totalPrice,
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
    $scope.deleteGioHangChiTietByUserId = function () {
        // Lấy thông tin người dùng từ localStorage
        let userFromStorage = JSON.parse(localStorage.getItem('selectedUser'));
        const userId = userFromStorage.idNguoiDung;

        // Gửi yêu cầu xóa chi tiết giỏ hàng
        $http.delete("http://localhost:8080/api/admin/gio_hang/delete/user/" + userId)
            .then(function (response) {
            })
            .catch(function (error) {
                console.error("Lỗi khi xóa chi tiết giỏ hàng:", error);
            });
    };
    $scope.createPaymentMethod = function () {
        // Lấy thông tin người dùng từ localStorage
        let userFromStorage = JSON.parse(localStorage.getItem('selectedUser'));
        const selectedInvoice = JSON.parse(localStorage.getItem('selectedInvoice'));
        const totalPriceInfo = JSON.parse(localStorage.getItem('totalPriceInfo'));
        const paymentMethodFromStorage = JSON.parse(localStorage.getItem('selectedPaymentMethod'));

        var paymentMethod = {
            phuongThucThanhToan: { idPTThanhToan: paymentMethodFromStorage.id },
            soTienThanhToan: Number($scope.customerPaid),
            ngayGiaoDich: new Date().toISOString(),
            ghiChu: "Thanh toán hóa đơn " + selectedInvoice.maHoaDon
        };

        // Tạo phương thức thanh toán
        $http.post("http://localhost:8080/api/admin/phuong_thuc_thanh_toan_hoa_don/" + selectedInvoice.idHoaDon + "/thanh-toan", paymentMethod)
            .then(function (response) {
                console.log("Phản hồi từ POST:", response.data);
                if (response.data && response.data.idThanhToanHoaDon) {
                    // Phương thức thanh toán đã được tạo thành công, tiếp tục cập nhật hóa đơn
                    var updatedInvoice = {
                        phiShip: 0,
                        ngayThanhToan: new Date().toISOString(),
                        thanhTien: totalPriceInfo.totalPrice,
                        idThanhToanHoaDon: response.data.idThanhToanHoaDon,
                        setSdtNguoiNhan: userFromStorage.sdt, // Lấy số điện thoại từ localStorage
                        trangThaiHoaDon: $scope.selectedPhuongThucThanhToan === 2 ? 4 : 5,
                        idVoucher: $scope.selectedVoucher ? $scope.selectedVoucher.idVoucher : null
                    };

                    console.log("Cấu trúc của updatedInvoice trước khi gửi:", updatedInvoice);
                    return $http.put("http://localhost:8080/api/admin/hoa_don_ban_hang/cap-nhat/" + selectedInvoice.idHoaDon, updatedInvoice)
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
    $scope.processCartItems = async function () {
        const paymentInfo = JSON.parse(localStorage.getItem('paymentInfo'));
        const totalPriceInfo = JSON.parse(localStorage.getItem('totalPriceInfo'));
        const selectedPaymentMethod = JSON.parse(localStorage.getItem('selectedPaymentMethod'));
        $scope.hoaDonChiTietDTO = [];
        const selectedInvoice = JSON.parse(localStorage.getItem('selectedInvoice'));
        // Lấy giỏ hàng từ localStorage
        const cartItemsFromStorage = JSON.parse(localStorage.getItem('cartItems'));

        // Kiểm tra xem có dữ liệu giỏ hàng hay không
        if (!cartItemsFromStorage || cartItemsFromStorage.length === 0) {
            console.log("Giỏ hàng trống hoặc không tìm thấy giỏ hàng trong localStorage");
            return;
        }

        // Duyệt qua các sản phẩm trong giỏ hàng
        cartItemsFromStorage.forEach(function (item) {
            const detail = {
                idHoaDon: selectedInvoice.idHoaDon,
                idSanPhamChiTiet: item.idSanPhamChiTiet,
                soLuong: item.soLuong,
                tongTien: totalPriceInfo.totalPrice,
                soTienThanhToan: selectedPaymentMethod.id === 3
                    ? totalPriceInfo.totalPrice
                    : parseFloat(paymentInfo.tienKhachDua),
                tienTraLai: selectedPaymentMethod.id === 3
                    ? 0
                    : $scope.tienThua,
                moTa: item.moTa || ''
            };
            $scope.hoaDonChiTietDTO.push(detail);
        });
    };

    $scope.thanhtoanck = async function () {
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const status = urlParams.get('status'); // Lấy giá trị của tham số "status"

        // Chỉ thực thi nếu trạng thái là "PAID"
        if (status !== 'PAID') {
            console.log("Trạng thái không hợp lệ hoặc thanh toán chưa hoàn tất. Không thực hiện hành động.");
            return;
        }
        try {
            // Các bước thanh toá
            // Lấy dữ liệu từ sessionStorage
            let userFromStorage = JSON.parse(localStorage.getItem('selectedUser'));

            // Kiểm tra dữ liệu đã lấy được
            console.log("User from sessionStorage: ", userFromStorage);


            const userId = userFromStorage.idNguoiDung;

            // Cập nhật trạng thái hóa đơn và các thao tác khác
            await $scope.updateInvoiceStatus();
            await $scope.createPaymentHistory();

            await Promise.all([
                $scope.deleteGioHangChiTietByUserId(),
                $scope.createPaymentMethod(),
                $scope.deleteCart()
            ]);

            await $scope.processCartItems();

            // Tạo hóa đơn chi tiết
            const response = await $http.post(
                "http://localhost:8080/api/admin/hoa_don_chi_tiet/create",
                $scope.hoaDonChiTietDTO,
                { params: { userId: userId } }
            );
            console.log("Hóa đơn chi tiết:", response.data);

            // Trừ voucher nếu có
            if ($scope.selectedVoucher) {
                await $scope.deductVoucherQuantity();
            }


            Swal.fire({
                title: 'Thành công!',
                text: 'Khách hàng đã thanh toán thành công!',
                icon: 'success',
                timer: 2400,
                showConfirmButton: false
            }).then(() => {
                // Điều hướng về trang admin sau khi thông báo kết thúc
                window.location.href = 'http://127.0.0.1:5501/admin.html#!/ban_hang';
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
    };
    $scope.thanhtoanck();
};


